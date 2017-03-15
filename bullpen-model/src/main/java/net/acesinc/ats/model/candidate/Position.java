/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.candidate;

import net.acesinc.ats.model.common.Address;
import net.acesinc.ats.model.common.NameAndCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import net.acesinc.ats.model.common.IdentifibleObject;

/**
 *
 * @author andrewserff
 */
public class Position extends IdentifibleObject {
    @JsonProperty("Employer")
    private String employer;
    @JsonProperty("OrganizationUnitName")
    private String organizationUnitName;
    @JsonProperty("PositionTitle")
    private String positionTitle;
    @JsonProperty("ReferenceLocation")
    private Address referenceLocation;
    @JsonProperty("StartDate")
    private String startDate;
    @JsonProperty("EndDate")
    private String endDate;
    @JsonProperty("CurrentIndicator")
    private Boolean currentIndicator;
    @JsonProperty("Industry")
    private List<NameAndCode> industry;
    @JsonProperty("JobCategory")
    private List<NameAndCode> jobCategory;
    @JsonProperty("JobLevel")
    private List<NameAndCode> jobLevel;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("UserArea")
    private Object userArea;

    /**
     * @return the employer
     */
    public String getEmployer() {
        return employer;
    }

    /**
     * @param employer the employer to set
     */
    public void setEmployer(String employer) {
        this.employer = employer;
        this.setUniqueId(employer);
    }

    /**
     * @return the organizationUnitName
     */
    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    /**
     * @param organizationUnitName the organizationUnitName to set
     */
    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
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
     * @return the referenceLocation
     */
    public Address getReferenceLocation() {
        return referenceLocation;
    }

    /**
     * @param referenceLocation the referenceLocation to set
     */
    public void setReferenceLocation(Address referenceLocation) {
        this.referenceLocation = referenceLocation;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
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

    /**
     * @return the currentIndicator
     */
    public Boolean getCurrentIndicator() {
        return currentIndicator;
    }

    /**
     * @param currentIndicator the currentIndicator to set
     */
    public void setCurrentIndicator(Boolean currentIndicator) {
        this.currentIndicator = currentIndicator;
    }

    /**
     * @return the industry
     */
    public List<NameAndCode> getIndustry() {
        return industry;
    }

    /**
     * @param industry the industry to set
     */
    public void setIndustry(List<NameAndCode> industry) {
        this.industry = industry;
    }

    /**
     * @return the jobCategory
     */
    public List<NameAndCode> getJobCategory() {
        return jobCategory;
    }

    /**
     * @param jobCategory the jobCategory to set
     */
    public void setJobCategory(List<NameAndCode> jobCategory) {
        this.jobCategory = jobCategory;
    }

    /**
     * @return the jobLevel
     */
    public List<NameAndCode> getJobLevel() {
        return jobLevel;
    }

    /**
     * @param jobLevel the jobLevel to set
     */
    public void setJobLevel(List<NameAndCode> jobLevel) {
        this.jobLevel = jobLevel;
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
