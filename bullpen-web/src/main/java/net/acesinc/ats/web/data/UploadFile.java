/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.data;

/**
 *
 * @author andrewserff
 */
public class UploadFile {
    private String requestId;
    private String storageId;
    private String name;
    private String url;
    private String error;
    private long size;
    private String contentType;
    private Boolean hasChildProcesses;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the error
     */
    public String isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * @return the storageId
     */
    public String getStorageId() {
        return storageId;
    }

    /**
     * @param storageId the storageId to set
     */
    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    /**
     * @return the requestId
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the hasChildProcesses
     */
    public Boolean getHasChildProcesses() {
        return hasChildProcesses;
    }

    /**
     * @param hasChildProcesses the hasChildProcesses to set
     */
    public void setHasChildProcesses(Boolean hasChildProcesses) {
        this.hasChildProcesses = hasChildProcesses;
    }
    
}
