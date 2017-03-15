/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/*import net.acesinc.ats.model.candidate.AccessCredential;
*
 *
 * @author andrewserff
 */
public class Attachment {
    @JsonProperty("EmbeddedData")
    private String embeddedData;
    @JsonProperty("EmbeddedText")
    private String embeddedText;
    @JsonProperty("URI")
    private String uri;
    @JsonProperty("FileName")
    private String fileName;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("FileType")
    private String fileType;
    @JsonProperty("DocumentTitle")
    private String documentTitle;
    @JsonProperty("AccessCredentials")
    private List<AccessCredential> accessCredentials;
    @JsonProperty("UserArea")
    private Object userArea;

    /**
     * @return the embeddedData
     */
    public String getEmbeddedData() {
        return embeddedData;
    }

    /**
     * @param embeddedData the embeddedData to set
     */
    public void setEmbeddedData(String embeddedData) {
        this.embeddedData = embeddedData;
    }

    /**
     * @return the embeddedText
     */
    public String getEmbeddedText() {
        return embeddedText;
    }

    /**
     * @param embeddedText the embeddedText to set
     */
    public void setEmbeddedText(String embeddedText) {
        this.embeddedText = embeddedText;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the documentTitle
     */
    public String getDocumentTitle() {
        return documentTitle;
    }

    /**
     * @param documentTitle the documentTitle to set
     */
    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    /**
     * @return the accessCredentials
     */
    public List<AccessCredential> getAccessCredentials() {
        return accessCredentials;
    }

    /**
     * @param accessCredentials the accessCredentials to set
     */
    public void setAccessCredentials(List<AccessCredential> accessCredentials) {
        this.accessCredentials = accessCredentials;
    }

    /**
     * @return the userArea
     */
    public Object getUserArea() {
        return userArea;
    }

    /**
     * @param userArea the userArea to set
     */
    public void setUserArea(Object userArea) {
        this.userArea = userArea;
    }
    
}
