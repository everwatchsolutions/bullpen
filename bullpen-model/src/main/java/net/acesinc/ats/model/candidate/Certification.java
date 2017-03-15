/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.acesinc.ats.model.common.IdentifibleObject;

/**
 *
 * @author andrewserff
 */
public class Certification extends IdentifibleObject {
    @JsonProperty("CertificationTypeCode")
    private String certificationTypeCode;
    @JsonProperty("CertificationName")
    private String certificationName;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("IssuingAuthorityName")
    private String issuingAuthorityName;
    @JsonProperty("FirstIssuedDate")
    private String firstIssuedDate;
    @JsonProperty("EndDate")
    private String endDate;

    
    /**
     * @return the certificationTypeCode
     */
    public String getCertificationTypeCode() {
        return certificationTypeCode;
    }

    /**
     * @param certificationTypeCode the certificationTypeCode to set
     */
    public void setCertificationTypeCode(String certificationTypeCode) {
        this.certificationTypeCode = certificationTypeCode;
    }

    /**
     * @return the certificationName
     */
    public String getCertificationName() {
        return certificationName;
    }

    /**
     * @param certificationName the certificationName to set
     */
    public void setCertificationName(String certificationName) {
        this.certificationName = certificationName;
        setUniqueId(certificationName.replaceAll(" ", "-"));
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
