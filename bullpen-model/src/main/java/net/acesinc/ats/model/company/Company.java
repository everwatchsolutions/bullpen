/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.acesinc.ats.model.common.Address;
import net.acesinc.ats.model.common.StoredFile;
import net.acesinc.ats.model.user.User;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 *
 * @author andrewserff
 */
public class Company {

    private String id;
    private String name;
    private String shortName;
    private Address primaryAddress;
    private List<Address> companyLocations;
    @DBRef
    private List<Company> partnerCompanies;
    private StoredFile companyLogo;
    private String companyDescription;
    private String websiteUrl;
    private String publicContactEmail;

    private String openingListFooter;
    private String openingFooter;
    private List<String> departments;
    private Map<String, String> presetNotes;
    private List<Application> applications = new ArrayList<>();
    

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

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
     * @return the primaryAddress
     */
    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    /**
     * @param primaryAddress the primaryAddress to set
     */
    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    /**
     * @return the companyLocations
     */
    public List<Address> getCompanyLocations() {
        return companyLocations;
    }

    /**
     * @param companyLocations the companyLocations to set
     */
    public void setCompanyLocations(List<Address> companyLocations) {
        this.companyLocations = companyLocations;
    }

    /**
     * @return the partnerCompanies
     */
    public List<Company> getPartnerCompanies() {
        return partnerCompanies;
    }

    /**
     * @param partnerCompanies the partnerCompanies to set
     */
    public void setPartnerCompanies(List<Company> partnerCompanies) {
        this.partnerCompanies = partnerCompanies;
    }

    /**
     * @return the companyLogo
     */
    public StoredFile getCompanyLogo() {
        return companyLogo;
    }

    /**
     * @param companyLogo the companyLogo to set
     */
    public void setCompanyLogo(StoredFile companyLogo) {
        this.companyLogo = companyLogo;
    }

    /**
     * @return the companyDescription
     */
    public String getCompanyDescription() {
        return companyDescription;
    }

    /**
     * @param companyDescription the companyDescription to set
     */
    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * @return the websiteUrl
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /**
     * @param websiteUrl the websiteUrl to set
     */
    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    /**
     * @return the publicContactEmail
     */
    public String getPublicContactEmail() {
        return publicContactEmail;
    }

    /**
     * @param publicContactEmail the publicContactEmail to set
     */
    public void setPublicContactEmail(String publicContactEmail) {
        this.publicContactEmail = publicContactEmail;
    }

    @JsonIgnore
    public String getOpeningListFooter() {
        return openingListFooter;
    }

    @JsonIgnore
    public void setOpeningListFooter(String openingListFooter) {
        this.openingListFooter = openingListFooter;
    }

    @JsonIgnore
    public String getOpeningFooter() {
        return openingFooter;
    }

    @JsonIgnore
    public void setOpeningFooter(String openingFooter) {
        this.openingFooter = openingFooter;
    }
    
    public List<String> getDepartments() {
        return departments;
    }
    
    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }
    
    public Map<String, String> getPresetNotes() {
        return presetNotes;
    }
    
    public void setPresetNotes(Map<String, String> presetNotes) {
        this.presetNotes = presetNotes;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
    
   
}
