/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.acesinc.ats.web.data.UploadFile;
import net.acesinc.ats.web.service.box.BoxSessionResponse;
import net.acesinc.ats.web.service.box.BoxUploadResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author andrewserff
 */
@Service
@PropertySource({"classpath:${spring.profiles.active}/application.properties"})
public class BoxViewerService {

    private static final Logger log = LoggerFactory.getLogger(BoxViewerService.class);
    @Autowired
    private GridFsOperations mongoFs;
    @Autowired
    private MongoOperations mongo;
    @Autowired
    private Environment env;

    @Async
    public void saveFileToViewingService(UploadFile uploadFile) {
        String id = uploadFile.getStorageId();
        String requestId = uploadFile.getRequestId();
        try {

            log.info("Sending file [ " + id + " ] to Box Viewer Service");

//            sendProgress(requestId, 0, "Locating Resume", false);
            //Step 1: Find the File
            Query query = new Query(GridFsCriteria.where("_id").is(new ObjectId(id)));
            List<GridFSDBFile> files = mongoFs.find(query);
            if (files != null && files.size() > 0) {
                GridFSDBFile file = files.get(0);
                //Step 2: Send it to Box
                sendFileToBoxService(file);

            }
        } catch (Throwable t) {
            log.error("Error processing resume", t);
//            sendProgress(requestId, 100, "Processing Failed", false, true);
        }
    }

    @Async
    public void sendFileToBoxServiceAsync(GridFSDBFile file) {
        sendFileToBoxService(file);
    }
    
    public void sendFileToBoxService(GridFSDBFile file) {
        BoxUploadResponse resp = uploadFileToBox(file);
        if (resp != null) {
            log.debug("Successfully uploaded file [ " + file.getFilename() + " ] to Box with id [ " + resp.getId() + " ]");
            file.put("box.id", resp.getId());

            DBObject mongoFile = mongo.getCollection("fs.files").findOne(file.getId());
            DBObject metadata = (DBObject) mongoFile.get("metadata");
            if (metadata == null) {
                metadata = new BasicDBObject();
            }

            metadata.put("boxId", resp.getId());
            mongoFile.put("metadata", metadata);

            mongo.getCollection("fs.files").save(mongoFile);
        }
    }

    public BoxUploadResponse uploadFileToBox(GridFSDBFile file) {
        String apiKey = env.getProperty("box.viewer.api.key");
        String url = env.getProperty("box.viewer.upload.url");
        String supportedTypeList = env.getProperty("box.viewer.supported.fileTypes");
        String ext = FilenameUtils.getExtension(file.getFilename());
        if (!supportedTypeList.contains(ext.toLowerCase())) {
            log.debug("Box does not support files of type [ " + ext + " ].  Skipping upload of file [ " + file.getFilename() + " ] to Box Service");
            return null;
        }
        

        try {
            log.info("Uploading file [ " + file.getFilename() + " ] to Box Viewer Service at [ " + url + " ] with API Key [ " + apiKey + " ]");
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("file", file.getInputStream(), ContentType.create(file.getContentType()), file.getFilename());
            HttpEntity entity = builder.build();
            post.setEntity(entity);
            post.addHeader("Authorization", "Token " + apiKey);

            try (CloseableHttpResponse response = client.execute(post)) {
                log.debug(response.getStatusLine().toString());
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
                    HttpEntity respEntity = response.getEntity();
                    String json = EntityUtils.toString(respEntity);
                    log.debug("Box Upload Response JSON: " + json);
                    ObjectMapper mapper = new ObjectMapper();
                    BoxUploadResponse boxResp = mapper.readValue(json, BoxUploadResponse.class);

                    EntityUtils.consume(respEntity);
                    return boxResp;
                } else {
                    log.warn("Box Upload Request Failed with status : " + response.getStatusLine().toString());
                }
            }
        } catch (IOException ioe) {
            log.error("Error uploading file to Box Viewer Service", ioe);
        }
        return null;
    }

    public BoxSessionResponse createBoxViewerSession(String boxId) {
        String apiKey = env.getProperty("box.viewer.api.key");
        String url = env.getProperty("box.viewer.session.url");

        try {
            log.info("Creating Viewer session for id [ " + boxId + " ] with Box Viewer Service at [ " + url + " ] with API Key [ " + apiKey + " ]");
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url);
            String jsonData = "{\"document_id\":\"" + boxId + "\"}";
            StringEntity params = new StringEntity(jsonData);
            post.setEntity(params);
            post.addHeader("content-type", "application/json");
            post.addHeader("Authorization", "Token " + apiKey);
            log.debug(post.toString());

            try (CloseableHttpResponse response = client.execute(post)) {
                log.debug(response.getStatusLine().toString());
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                    HttpEntity respEntity = response.getEntity();
                    String json = EntityUtils.toString(respEntity);
                    log.debug("Box Session Response JSON: " + json);
                    ObjectMapper mapper = new ObjectMapper();
                    BoxSessionResponse boxResp = mapper.readValue(json, BoxSessionResponse.class);

                    EntityUtils.consume(respEntity);
                    return boxResp;
                } else {
                    log.warn("Box Session Request Failed with status : " + response.getStatusLine().toString());
                }
            }
        } catch (IOException ioe) {
            log.error("Error creating Box Viewer Session", ioe);
        }
        return null;
    }
}
