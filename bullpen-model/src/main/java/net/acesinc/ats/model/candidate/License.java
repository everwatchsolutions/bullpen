/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author andrewserff
 */
public class License {
    @JsonProperty("LicenseTypeCode")
    private String licenseTypeCode;
    @JsonProperty("LicenseName")
    private String licenseName;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("IssuingAuthorityName")
    private String issuingAuthorityName;
    @JsonProperty("FirstIssuedDate")
    private String firstIssuedDate;
    @JsonProperty("EndDate")
    private String endDate;

    /**
     * @return the licenseTypeCode
     */
    public String getLicenseTypeCode() {
        return licenseTypeCode;
    }

    /**
     * @param licenseTypeCode the licenseTypeCode to set
     */
    public void setLicenseTypeCode(String licenseTypeCode) {
        this.licenseTypeCode = licenseTypeCode;
    }

    /**
     * @return the licenseName
     */
    public String getLicenseName() {
        return licenseName;
    }

    /**
     * @param licenseName the licenseName to set
     */
    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
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
     * @return the issuingAuthorityName
     */
    public String getIssuingAuthorityName() {
        return issuingAuthorityName;
    }

    /**
     * @param issuingAuthorityName the issuingAuthorityName to set
     */
    public void setIssuingAuthorityName(String issuingAuthorityName) {
        this.issuingAuthorityName = issuingAuthorityName;
    }

    /**
     * @return the firstIssuedDate
     */
    public String getFirstIssuedDate() {
        return firstIssuedDate;
    }

    /**
     * @param firstIssuedDate the firstIssuedDate to set
     */
    public void setFirstIssuedDate(String firstIssuedDate) {
        this.firstIssuedDate = firstIssuedDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
}
