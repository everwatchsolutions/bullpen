/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.common;

import java.util.Date;
import net.acesinc.ats.model.candidate.BinaryStorageLocation;

/**
 *
 * @author andrewserff
 */
public class StoredFile {
    private String storageId;
    private String filename;
    private BinaryStorageLocation storageLocation;
    private Date dateAdded;
    private FileFormat format;
    private boolean preferred;

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
     * @return the storageLocation
     */
    public BinaryStorageLocation getStorageLocation() {
        return storageLocation;
    }

    /**
     * @param storageLocation the storageLocation to set
     */
    public void setStorageLocation(BinaryStorageLocation storageLocation) {
        this.storageLocation = storageLocation;
    }

    /**
     * @return the dateAdded
     */
    public Date getDateAdded() {
        return dateAdded;
    }

    /**
     * @param dateAdded the dateAdded to set
     */
    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * @return the preferred
     */
    public boolean isPreferred() {
        return preferred;
    }

    /**
     * @param preferred the preferred to set
     */
    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    /**
     * @return the format
     */
    public FileFormat getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(FileFormat format) {
        this.format = format;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
}
