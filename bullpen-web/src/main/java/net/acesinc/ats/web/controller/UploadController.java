/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.acesinc.ats.web.data.UploadFile;
import com.mongodb.gridfs.GridFSFile;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import net.acesinc.ats.model.candidate.BinaryStorageLocation;
import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.common.FileFormat;
import net.acesinc.ats.model.common.StoredFile;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.web.data.UploadResponse;
import net.acesinc.ats.web.repository.CandidateRepository;
import net.acesinc.ats.web.repository.CompanyRepository;
import net.acesinc.ats.web.repository.UserRepository;
import net.acesinc.ats.web.service.BoxViewerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author andrewserff
 */
@Controller
public class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    private GridFsOperations mongoFs;
    @Autowired
    private BoxViewerService boxService;
    @Autowired
    private CandidateRepository candidateRepo;

    
        @RequestMapping(value = "/resume/upload", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public @ResponseBody
    UploadResponse handleResumeUpload(Principal user, @RequestParam("file") MultipartFile file, Candidate c , @RequestParam("firstName") String first, @RequestParam("lastName") String last) {
        UploadResponse resp = handleFileUpload(user, file);
        
        if (resp != null && resp.getFiles() != null) {
            for (UploadFile uf : resp.getFiles()) {
                uf.setUrl("/resume/" + uf.getStorageId());
                uf.setHasChildProcesses(true);
                log.info("Sending Resume " + uf.getName() + " to Resume Parsing Service");
                if (c != null && c.getId() == null) {
                    log.debug("Non null candidate with null id.  Clearing it out");
                    c = null;
                }
               User u = userRepo.findByEmail(user.getName());
                c = new Candidate();
                List<StoredFile> resumes = new ArrayList();
                StoredFile resume = new StoredFile();
                resume.setStorageId(uf.getStorageId());
                resumes.add(resume);
                c.setResumes(resumes);
                c.setGivenName(first);
                c.setFamilyName(last);
                c.setFormattedName(first + " " + last);
                c.setOwnerCompany(u.getCompany());
                candidateRepo.save(c);
            }
        }

        return resp;
    }
   
    
   

    @RequestMapping(value = "/company/logo", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public @ResponseBody
    UploadResponse handleLogoUpload(Principal user, @RequestParam("file") MultipartFile file) {
        User u = userRepo.findByEmail(user.getName());

        UploadResponse resp = handleFileUpload(user, file);

        u.getCompany().setCompanyLogo(null);
        UploadFile uf = resp.getFiles().get(0);
        StoredFile logo = new StoredFile();
        logo.setDateAdded(new Date());
        logo.setFilename(uf.getName());
        logo.setStorageId(uf.getStorageId());
        try {
            logo.setFormat(FileFormat.fromMimeType(uf.getContentType()));
        } catch (Exception e) {
            //unknown type
            log.debug("Could not determine the format of file [ " + uf.getName() + " ] with content type [ " + uf.getContentType() + " ]");
        }

        Company c = u.getCompany();
        c.setCompanyLogo(logo);
        companyRepo.save(c);

        log.info("New Company Logo [ " + uf.getName() + " ] saved for Company [ " + c.getName() + " ]");

        return resp;
    }

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST, produces = {"application/xml", "application/json"})
    public @ResponseBody
    UploadResponse handleFileUpload(Principal user, @RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        log.debug("Handling file upload of file [ " + name + " ]");
        UploadResponse resp = new UploadResponse();
        List<UploadFile> files = new ArrayList<>();
        if (!file.isEmpty()) {
            try {
                GridFSFile gridFile = persistToGridFS(user, file.getInputStream(), name, file.getContentType());

                UploadFile uploadFile = new UploadFile();
                uploadFile.setRequestId(UUID.randomUUID().toString());
                uploadFile.setStorageId(gridFile.getId().toString());
                uploadFile.setName(name);
                uploadFile.setUrl("/view/file/" + gridFile.getId().toString());
                uploadFile.setSize(gridFile.getLength());
                uploadFile.setContentType(gridFile.getContentType());

                files.add(uploadFile);

                boxService.saveFileToViewingService(uploadFile);

                resp.setError(false);
                resp.setFiles(files);
                resp.setTextStatus("Successfully stored file [ " + name + " ] in MongoDB with id [ " + uploadFile.getStorageId() + " ]!");
            } catch (Exception e) {
                resp.setError(true);
                resp.setTextStatus("Upload of file " + name + " failed. => " + e.getMessage());
            }
        } else {
            resp.setError(true);
            resp.setTextStatus("You failed to upload " + name + " because the file was empty.");
        }
        log.info(resp.getTextStatus());
        return resp;
    }

    private GridFSFile persistToGridFS(Principal user, InputStream inputStream, String fileName, String contentType) {
        log.debug("Persisting to GridFS");

        DBObject metaData = new BasicDBObject();
        if (user != null) {
            User u = userRepo.findByEmail(user.getName());
            metaData.put("uploadedByUser", u.getId());
            metaData.put("source", "USER_UPLOAD");
        } else {
            metaData.put("source", "APPLICATION");
        }

        if (contentType == null || contentType.isEmpty()) {
            contentType = getContentTypeForFilename(fileName);
        }
        GridFSFile gridFile = mongoFs.store(inputStream, fileName, contentType, metaData);

        log.debug("Persisted to GridFS File : " + gridFile.getId());
        return gridFile;
    }

    private String getContentTypeForFilename(String fileName) {
        String lowerFilename = fileName.toLowerCase();
        if (lowerFilename.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else {
            return URLConnection.guessContentTypeFromName(fileName);
        }
    }
}

