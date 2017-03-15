/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import com.mongodb.gridfs.GridFSDBFile;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import net.acesinc.ats.web.data.Result;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author andrewserff
 */
@Controller
public class DownloadController {

    private static final Logger log = LoggerFactory.getLogger(DownloadController.class);
    @Autowired
    private GridFsOperations mongoFs;

    @RequestMapping(value = {"/resume/{id}", "/view/file/{id}"}, method = RequestMethod.GET)
    protected ResponseEntity<byte[]> streamGridFSFile(@PathVariable("id") String contentId, HttpServletResponse response, ModelMap model) throws IOException {
        Query query = new Query(GridFsCriteria.where("_id").is(new ObjectId(contentId)));
        List<GridFSDBFile> files = mongoFs.find(query);
        if (files != null && files.size() > 0) {
            GridFSDBFile file = files.get(0);
            
            byte[] data = IOUtils.toByteArray(file.getInputStream());
            log.debug("Content of length [ " + data.length + " ] Found.  Streaming back to client");
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType(file.getContentType());
            headers.setContentType(type);
            headers.setContentLength(file.getLength());
            if (!type.equals(MediaType.IMAGE_GIF) && !type.equals(MediaType.IMAGE_JPEG) && !type.equals(MediaType.IMAGE_PNG) && !type.equals(MediaType.TEXT_PLAIN)) {
                headers.setContentDispositionFormData(file.getFilename(), file.getFilename());
            }
            
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> dataResp = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
            return dataResp;
        } else {
            log.debug("Content not found");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }
    
    @RequestMapping(value ="/resume/{id}/metadata", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    protected @ResponseBody Result getBoxInfo(@PathVariable("id") String contentId) {
        Query query = new Query(GridFsCriteria.where("_id").is(new ObjectId(contentId)));
        List<GridFSDBFile> files = mongoFs.find(query);
        if (files != null && files.size() > 0) {
            GridFSDBFile file = files.get(0);
            return Result.ok(file.getMetaData());
        } else {
            return Result.error(null, "Could not find file with id [ " + contentId + " ]");
        }
    }
}
