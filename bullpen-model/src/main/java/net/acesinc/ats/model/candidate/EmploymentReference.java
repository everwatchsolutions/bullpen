/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.candidate;

import net.acesinc.ats.model.common.ReferenceType;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.acesinc.ats.model.common.IdentifibleObject;

/**
 *
 * @author andrewserff
 */
public class EmploymentReference extends IdentifibleObject {
    @JsonProperty("refereeTypeCode")
    private ReferenceType refereeTypeCode;
    @JsonProperty("FormattedName")
    private String formattedName;
    @JsonProperty("PositionTitle")
    private String positionTitle;
    @JsonProperty("OrganizationTitle")
    private String organizationTitle;
    @JsonProperty("OrganizationName")
    private String organizationName;
    @JsonProperty("PreferredPhone")
    private String preferredPhone;
    @JsonProperty("PreferredEmail")
    private String perferredEmail;
    @JsonProperty("YearsKnownNumber")
    private String yearsKnownNumber;
    @JsonProperty("RelationshipTypeCode")
    private String relationshipTypeCode;
    @JsonProperty("Comment")
    private String comment;

    /**
     * @return the refereeTypeCode
     */
    public ReferenceType getRefereeTypeCode() {
        return refereeTypeCode;
    }

    /**
     * @param refereeTypeCode the refereeTypeCode to set
     */
    public void setRefereeTypeCode(ReferenceType refereeTypeCode) {
        this.refereeTypeCode = refereeTypeCode;
    }

    /**
     * @return the formattedName
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * @param formattedName the formattedName to set
     */
    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
        this.setUniqueId(formattedName);
    }

    /**
     * @return the positionTitle
     */
    public String getPositionTitle() {
        return positionTitle;
    }

    /**
     * @param positionTitle the positionTitle to set
     */
    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    /**
     * @return the organizationTitle
     */
    public String getOrganizationTitle() {
        return organizationTitle;
    }

    /**
     * @param organizationTitle the organizationTitle to set
     */
    public void setOrganizationTitle(String organizationTitle) {
        this.organizationTitle = organizationTitle;
    }

    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the preferredPhone
     */
    public String getPreferredPhone() {
        return preferredPhone;
    }

    /**
     * @param preferredPhone the preferredPhone to set
     */
    public void setPreferredPhone(String preferredPhone) {
        this.preferredPhone = preferredPhone;
    }

    /**
     * @return the perferredEmail
     */
    public String getPerferredEmail() {
        return perferredEmail;
    }

    /**
     * @param perferredEmail the perferredEmail to set
     */
    public void setPerferredEmail(String perferredEmail) {
        this.perferredEmail = perferredEmail;
    }

    /**
     * @return the yearsKnownNumber
     */
    public String getYearsKnownNumber() {
        return yearsKnownNumber;
    }

    /**
     * @param yearsKnownNumber the yearsKnownNumber to set
     */
    public void setYearsKnownNumber(String yearsKnownNumber) {
        this.yearsKnownNumber = yearsKnownNumber;
    }

    /**
     * @return the relationshipTypeCode
     */
    public String getRelationshipTypeCode() {
        return relationshipTypeCode;
    }

    /**
     * @param relationshipTypeCode the relationshipTypeCode to set
     */
    public void setRelationshipTypeCode(String relationshipTypeCode) {
        this.relationshipTypeCode = relationshipTypeCode;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
