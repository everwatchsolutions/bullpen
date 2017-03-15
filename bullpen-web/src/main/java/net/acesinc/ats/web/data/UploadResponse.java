/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.data;

import java.util.List;

/**
 *
 * @author andrewserff
 */
public class UploadResponse {
    private boolean error;
    private String textStatus;
    private List<UploadFile> files;

    /**
     * @return the error
     */
    public boolean isError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * @return the textStatus
     */
    public String getTextStatus() {
        return textStatus;
    }

    /**
     * @param textStatus the textStatus to set
     */
    public void setTextStatus(String textStatus) {
        this.textStatus = textStatus;
    }

    /**
     * @return the files
     */
    public List<UploadFile> getFiles() {
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(List<UploadFile> files) {
        this.files = files;
    }
    
}
