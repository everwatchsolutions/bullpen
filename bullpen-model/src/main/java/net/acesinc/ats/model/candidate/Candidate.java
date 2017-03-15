/*
 * To change this licenses header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.candidate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.acesinc.ats.model.common.StoredFile;
import net.acesinc.ats.model.common.Competency;
import net.acesinc.ats.model.common.Attachment;
import net.acesinc.ats.model.common.Gender;
import net.acesinc.ats.model.common.Website;
import net.acesinc.ats.model.common.Address;
import net.acesinc.ats.model.common.NameAndCode;
import net.acesinc.ats.model.common.Phone;
import net.acesinc.ats.model.common.Email;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.acesinc.ats.model.candidate.notes.CandidateNote;
import net.acesinc.ats.model.common.EducationLevel;
import net.acesinc.ats.model.common.FileFormat;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.user.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This entire model is based on the HR-XML 3.3 JSON Lightweight Standard.
 * Because of this, we may not be capturing everything that might be needed to
 * output a full HR-XML 3.3 compliant document if we needed to. Also, because
 * this is trying to conform to the JSON standard, the variable names were
 * specifically chosen to match that of the standard so in theory, we can use
 * something like Jackson to map a hr-xml json document directly into this java
 * model. In short, don't change the variable names!
 *
 * @author andrewserff
 */
@Document
public class Candidate {

    private static final Logger log = LoggerFactory.getLogger(Candidate.class);

    private String id;
    @DBRef
    private Company ownerCompany;
    @JsonProperty("CandidateID")
    private String candidateID;
    @JsonProperty("CandidateURI")
    private String candidateURI;
    @JsonProperty("GivenName")
    private String givenName;
    @JsonProperty("FamilyName")
    private String familyName;
    @JsonProperty("FormattedName")
    private String formattedName;
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;
    @JsonProperty("Gender")
    private Gender gender;
    @JsonProperty("DisabilityIndicator")
    private Boolean disabilityIndicator;
    @JsonProperty("DisabilitySummary")
    private List<String> disabilitySummary;
    @JsonProperty("Address")
    private List<Address> address;
    @JsonProperty("Phone")
    private List<Phone> phone;
    @JsonProperty("Email")
    private List<Email> email;
    @JsonProperty("Web")
    private List<Website> web;
    @JsonProperty("PersonCompetency")
    private List<Competency> personCompetencies;
    @JsonProperty("WorkEligibility")
    private List<WorkEligibility> workEligibilities; //Visas
    @JsonProperty("EducationOrganizationAttendance")
    private List<EducationalOrganization> educationalOrganizations;
    @JsonProperty("PositionHistory")
    private List<Position> positionHistory;
    @JsonProperty("Certification")
    private List<Certification> certifications;
    @JsonProperty("License")
    private List<License> licenses;
    @JsonProperty("EmploymentReferences")
    private List<EmploymentReference> employmentReferences;
    @JsonProperty("SpecialCommitment")
    private List<NameAndCode> specialCommitments;
    @JsonProperty("Attachment")
    private List<Attachment> attachments;
    @JsonProperty("UserArea")
    private Object userArea;
    //things we might be missing:
    //MilitaryStatus
    //Languages
    //
    //Custom Fields for our application
    private List<StoredFile> resumes;
    private Date lastContact;
    private Date dateApplied;
    private String title;
    private Double yearsOfExperience;
    private EducationLevel highestEducationLevel;


    private List<CandidateNote> notes;
    @DBRef
    private List<User> interestedUsers;
    private boolean archive = false;
    private int level;
    private int securityVerify;
    private Date startDate;
    private Date securityStart;
    private Date securityEnd;
    private String preferedLocation;

    private HashMap<String, Date> applications = new HashMap<String, Date>();

    private List<String> EEO = new ArrayList<>();

    public void addCandidateNote(CandidateNote note) {
        if (notes == null) {
            notes = new ArrayList<>();
        }
        notes.add(note);
    }

    public void addInterestedUser(User u) {
        if (interestedUsers == null) {
            interestedUsers = new ArrayList<>();
        }
        if (!interestedUsers.contains(u)) {
            interestedUsers.add(u);
        }
    }

    public Candidate updateCandidate(Candidate updatedInfo) {
        if (updatedInfo == null) {
            log.warn("Tried to update Candidate with null info");
            return this;
        }

        if (updatedInfo.getAddress() != null) {
            if (getAddress() != null) {
                for (Address newObj : updatedInfo.getAddress()) {
                    boolean has = false;
                    for (Address existing : getAddress()) {
                        if (existing.getAddressLine().equalsIgnoreCase(newObj.getAddressLine())) {
                            //we already have this email
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        getAddress().add(newObj);
                    }
                }
            } else {
                setAddress(updatedInfo.getAddress());
            }
        }

        //FIXME I don't know how to compare Attachments...
        setAttachments(updatedInfo.getAttachments());

        if (getCandidateID() == null) {
            setCandidateID(updatedInfo.getCandidateID());
        }

        if (getCandidateURI() == null) {
            setCandidateURI(updatedInfo.getCandidateURI());
        }

        if (updatedInfo.getCertifications() != null) {
            if (getCertifications() != null) {
                for (Certification newObj : updatedInfo.getCertifications()) {
                    boolean has = false;
                    for (Certification existing : getCertifications()) {
                        if (existing.getCertificationName().equalsIgnoreCase(newObj.getCertificationName())) {
                            //we already have this email
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        getCertifications().add(newObj);
                    }
                }
            } else {
                setCertifications(updatedInfo.getCertifications());
            }
        }

        if (getDateOfBirth() == null) {
            setDateOfBirth(updatedInfo.getDateOfBirth());
        }

        setDisabilityIndicator(updatedInfo.getDisabilityIndicator());
        setDisabilitySummary(updatedInfo.getDisabilitySummary());

        if (updatedInfo.getEducationalOrganizations() != null) {
            if (getEducationalOrganizations() != null) {
                for (EducationalOrganization newObj : updatedInfo.getEducationalOrganizations()) {
                    boolean has = false;
                    for (EducationalOrganization existing : getEducationalOrganizations()) {
                        if (existing.getSchool().equalsIgnoreCase(newObj.getSchool())) {
                            //we already have this email
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        getEducationalOrganizations().add(newObj);
                    }
                }
            } else {
                setEducationalOrganizations(updatedInfo.getEducationalOrganizations());
            }
        }

        //add any missing emails
        if (updatedInfo.getEmail() != null) {
            if (getEmail() != null) {
                for (Email e : updatedInfo.getEmail()) {
                    boolean has = false;
                    for (Email existingEmail : getEmail()) {
                        if (existingEmail.getAddress().equalsIgnoreCase(e.getAddress())) {
                            //we already have this email
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        getEmail().add(e);
                    }
                }
            } else {
                setEmail(updatedInfo.getEmail());
            }
        }

        setEmploymentReferences(updatedInfo.getEmploymentReferences());
        setGender(updatedInfo.getGender());

        if (updatedInfo.getLicenses() != null) {
            if (getLicenses() != null) {
                for (License newObj : updatedInfo.getLicenses()) {
                    boolean has = false;
                    for (License existing : getLicenses()) {
                        if (existing.getLicenseName().equalsIgnoreCase(newObj.getLicenseName())) {
                            //we already have this email
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        getLicenses().add(newObj);
                    }
                }
            } else {
                setLicenses(updatedInfo.getLicenses());
            }
        }

        if (updatedInfo.getPersonCompetencies() != null) {
            if (getPersonCompetencies() != null) {
                for (Competency newObj : updatedInfo.getPersonCompetencies()) {
                    boolean has = false;
                    for (Competency existing : getPersonCompetencies()) {
                        if (existing.getCompetencyName().equalsIgnoreCase(newObj.getCompetencyName())) {
                            //we already have this email
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        getPersonCompetencies().add(newObj);
                    }
                }
            } else {
                setPersonCompetencies(updatedInfo.getPersonCompetencies());
            }
        }

        //add any missing phones
        if (updatedInfo.getPhone() != null) {
            if (getPhone() != null) {
                for (Phone p : updatedInfo.getPhone()) {
                    boolean has = false;
                    for (Phone existingPhone : getPhone()) {
                        if (existingPhone.getNumber().equalsIgnoreCase(p.getNumber())) {
                            //we already have this email
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        getPhone().add(p);
                    }
                }
            } else {
                setPhone(updatedInfo.getPhone());
            }
        }

        if (updatedInfo.getPositionHistory() != null) {
            if (getPositionHistory() != null) {
                for (Position newObj : updatedInfo.getPositionHistory()) {
                    boolean has = false;
                    for (Position existing : getPositionHistory()) {
                        if (existing.getEmployer().equalsIgnoreCase(newObj.getEmployer())) {
                            //we already have this email
                            if (existing.getEndDate() != null && newObj.getEndDate() != null) {
                                if (!existing.getEndDate().equals(newObj.getEndDate())) {
                                    existing.setEndDate(newObj.getEndDate());
                                }
                            } else if (existing.getEndDate() == null && newObj.getEndDate() != null) {
                                existing.setEndDate(newObj.getEndDate());
                            }
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        getPositionHistory().add(newObj);
                    }
                }
            } else {
                setPositionHistory(updatedInfo.getPositionHistory());
            }
        }

        if (updatedInfo.getSpecialCommitments() != null) {
            if (getSpecialCommitments() != null) {
                for (NameAndCode newObj : updatedInfo.getSpecialCommitments()) {
                    boolean has = false;
                    for (NameAndCode existing : getSpecialCommitments()) {
                        if (existing.getName().equalsIgnoreCase(newObj.getName())) {
                            //we already have this one
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        getSpecialCommitments().add(newObj);
                    }
                }
            } else {
                setSpecialCommitments(updatedInfo.getSpecialCommitments());
            }
        }

        setUserArea(updatedInfo.getUserArea());

        if (updatedInfo.getWeb() != null) {
            if (getWeb() != null) {
                for (Website newObj : updatedInfo.getWeb()) {
                    boolean has = false;
                    for (Website existing : getWeb()) {
                        if (existing.getAddress().equalsIgnoreCase(newObj.getAddress())) {
                            //we already have this one
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        getWeb().add(newObj);
                    }
                }
            } else {
                setWeb(updatedInfo.getWeb());
            }
        }
        setWorkEligibilities(updatedInfo.getWorkEligibilities());

        return this;
    }

    public void addStoredResume(StoredFile resume) {
        if (resumes == null) {
            resumes = new ArrayList<>();
        }
        resumes.add(resume);
        setLastContact(new Date());
    }

    @JsonIgnore
    public StoredFile getMostRecentResume() {
        StoredFile mostRecent = null;
        if (resumes != null) {
            for (StoredFile r : resumes) {
                if (r.getFormat() == null) { //this should no longer happen, but protecting from a NPE just in case
                    continue;
                }
                if (mostRecent == null && (!r.getFormat().equals(FileFormat.HRXML))) {
                    mostRecent = r;
                }
                if (r.getDateAdded().after(mostRecent.getDateAdded()) && !r.getFormat().equals(FileFormat.HRXML)) {
                    mostRecent = r;
                }
            }
        }
        return mostRecent;
    }

    public Candidate createCopy() {
        Candidate copy = new Candidate();

        if (getAddress() != null) {
            copy.setAddress(new ArrayList<>(getAddress()));
        }
        if (getAttachments() != null) {
            copy.setAttachments(new ArrayList<>(getAttachments()));
        }

        copy.setCandidateID(copy.getCandidateID());
        copy.setCandidateURI(copy.getCandidateURI());

        if (getCertifications() != null) {
            copy.setCertifications(new ArrayList<>(getCertifications()));
        }
        copy.setDateOfBirth(getDateOfBirth());
        copy.setDisabilityIndicator(getDisabilityIndicator());
        if (getDisabilitySummary() != null) {
            copy.setDisabilitySummary(new ArrayList<>(getDisabilitySummary()));
        }
        if (getEducationalOrganizations() != null) {
            copy.setEducationalOrganizations(new ArrayList<>(getEducationalOrganizations()));
        }
        if (getEmail() != null) {
            copy.setEmail(new ArrayList<>(getEmail()));
        }
        if (getEmploymentReferences() != null) {
            copy.setEmploymentReferences(new ArrayList<>(getEmploymentReferences()));
        }
        copy.setFamilyName(getFamilyName());
        copy.setFormattedName(getFormattedName());
        copy.setGender(getGender());
        copy.setGivenName(getGivenName());
        if (getLicenses() != null) {
            copy.setLicenses(new ArrayList<>(getLicenses()));
        }
        copy.setOwnerCompany(getOwnerCompany());
        if (getPersonCompetencies() != null) {
            copy.setPersonCompetencies(new ArrayList<>(getPersonCompetencies()));
        }
        if (getPhone() != null) {
            copy.setPhone(new ArrayList<>(getPhone()));
        }
        if (getPositionHistory() != null) {
            copy.setPositionHistory(new ArrayList<>(getPositionHistory()));
        }
        if (getResumes() != null) {
            copy.setResumes(new ArrayList<>(getResumes()));
        }
        if (getSpecialCommitments() != null) {
            copy.setSpecialCommitments(new ArrayList<>(getSpecialCommitments()));
        }
        copy.setUserArea(getUserArea());
        if (getWeb() != null) {
            copy.setWeb(new ArrayList<>(getWeb()));
        }
        if (getWorkEligibilities() != null) {
            copy.setWorkEligibilities(new ArrayList<>(getWorkEligibilities()));
        }

        return copy;
    }

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
     * @return the candidateID
     */
    public String getCandidateID() {
        return candidateID;
    }

    /**
     * @param candidateID the candidateID to set
     */
    public void setCandidateID(String candidateID) {
        this.candidateID = candidateID;
    }

    /**
     * @return the candidateURI
     */
    public String getCandidateURI() {
        return candidateURI;
    }

    /**
     * @param candidateURI the candidateURI to set
     */
    public void setCandidateURI(String candidateURI) {
        this.candidateURI = candidateURI;
    }

    /**
     * @return the givenName
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * @param givenName the givenName to set
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * @return the familyName
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * @param familyName the familyName to set
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
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
    }

    /**
     * @return the dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return the disabilityIndicator
     */
    public Boolean getDisabilityIndicator() {
        return disabilityIndicator;
    }

    /**
     * @param disabilityIndicator the disabilityIndicator to set
     */
    public void setDisabilityIndicator(Boolean disabilityIndicator) {
        this.disabilityIndicator = disabilityIndicator;
    }

    /**
     * @return the disabilitySummary
     */
    public List<String> getDisabilitySummary() {
        return disabilitySummary;
    }

    /**
     * @param disabilitySummary the disabilitySummary to set
     */
    public void setDisabilitySummary(List<String> disabilitySummary) {
        this.disabilitySummary = disabilitySummary;
    }

    /**
     * @return the address
     */
    public List<Address> getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(List<Address> address) {
        this.address = address;
    }

    /**
     * @return the phone
     */
    public List<Phone> getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    /**
     * @return the email
     */
    public List<Email> getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(List<Email> email) {
        this.email = email;
    }

    /**
     * @return the web
     */
    public List<Website> getWeb() {
        return web;
    }

    /**
     * @param web the web to set
     */
    public void setWeb(List<Website> web) {
        this.web = web;
    }

    /**
     * @return the personCompetencies
     */
    public List<Competency> getPersonCompetencies() {
        return personCompetencies;
    }

    /**
     * @param personCompetencies the personCompetencies to set
     */
    public void setPersonCompetencies(List<Competency> personCompetencies) {
        this.personCompetencies = personCompetencies;
    }

    /**
     * @return the workEligibilities
     */
    public List<WorkEligibility> getWorkEligibilities() {
        return workEligibilities;
    }

    /**
     * @param workEligibilities the workEligibilities to set
     */
    public void setWorkEligibilities(List<WorkEligibility> workEligibilities) {
        this.workEligibilities = workEligibilities;
    }

    /**
     * @return the educationalOrganizations
     */
    public List<EducationalOrganization> getEducationalOrganizations() {
        return educationalOrganizations;
    }

    /**
     * @param educationalOrganizations the educationalOrganizations to set
     */
    public void setEducationalOrganizations(List<EducationalOrganization> educationalOrganizations) {
        this.educationalOrganizations = educationalOrganizations;
    }

    /**
     * @return the positionHistory
     */
    public List<Position> getPositionHistory() {
        return positionHistory;
    }

    /**
     * @param positionHistory the positionHistory to set
     */
    public void setPositionHistory(List<Position> positionHistory) {
        this.positionHistory = positionHistory;
    }

    /**
     * @return the certifications
     */
    public List<Certification> getCertifications() {
        return certifications;
    }

    /**
     * @param certifications the certifications to set
     */
    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    /**
     * @return the licenses
     */
    public List<License> getLicenses() {
        return licenses;
    }

    /**
     * @param licenses the licenses to set
     */
    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    /**
     * @return the employmentReferences
     */
    public List<EmploymentReference> getEmploymentReferences() {
        return employmentReferences;
    }

    /**
     * @param employmentReferences the employmentReferences to set
     */
    public void setEmploymentReferences(List<EmploymentReference> employmentReferences) {
        this.employmentReferences = employmentReferences;
    }

    /**
     * @return the specialCommitments
     */
    public List<NameAndCode> getSpecialCommitments() {
        return specialCommitments;
    }

    /**
     * @param specialCommitments the specialCommitments to set
     */
    public void setSpecialCommitments(List<NameAndCode> specialCommitments) {
        this.specialCommitments = specialCommitments;
    }

    /**
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
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

    /**
     * @return the resumes
     */
    public List<StoredFile> getResumes() {
        return resumes;
    }

    /**
     * @param resumes the resumes to set
     */
    public void setResumes(List<StoredFile> resumes) {
        this.resumes = resumes;
    }

    /**
     * @return the ownerCompany
     */
    public Company getOwnerCompany() {
        return ownerCompany;
    }

    /**
     * @param ownerCompany the ownerCompany to set
     */
    public void setOwnerCompany(Company ownerCompany) {
        this.ownerCompany = ownerCompany;
    }

    /**
     * @return the lastContact
     */
    public Date getLastContact() {
        return lastContact;
    }

    /**
     * @param lastContact the lastContact to set
     */
    public void setLastContact(Date lastContact) {
        this.lastContact = lastContact;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the yearsOfExperience
     */
    public Double getYearsOfExperience() {
        return yearsOfExperience;
    }

    /**
     * @param yearsOfExperience the yearsOfExperience to set
     */
    public void setYearsOfExperience(Double yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    /**
     * @return the highestEducationLevel
     */
    public EducationLevel getHighestEducationLevel() {
        return highestEducationLevel;
    }

    /**
     * @param highestEducationLevel the highestEducationLevel to set
     */
    public void setHighestEducationLevel(EducationLevel highestEducationLevel) {
        this.highestEducationLevel = highestEducationLevel;
    }

   
    /**
     * @return the notes
     */
    public List<CandidateNote> getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(List<CandidateNote> notes) {
        this.notes = notes;
    }

    /**
     * @return the interestedUsers
     */
    public List<User> getInterestedUsers() {
        return interestedUsers;
    }

    /**
     * @param interestedUsers the interestedUsers to set
     */
    public void setInterestedUsers(List<User> interestedUsers) {
        this.interestedUsers = interestedUsers;
    }

    public List<String> getEEO() {
        return EEO;
    }

    public void setEEO(List<String> EEO) {
        this.EEO = EEO;
    }

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }
    
    
    @JsonIgnore
    public String getDateAppliedString() {
        String date = "";
        if (dateApplied != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            date = df.format(dateApplied);
        }
        return date;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    @JsonIgnore
    public void setLevel(int level) {
        this.level = level;
    }

    @JsonIgnore
    public int getLevel() {
        return level;
    }

    @JsonIgnore
    public Date getSecurityStart() {
        return securityStart;
    }

    @JsonIgnore
    public String getSecurityStartString() {
        String date = "";
        if (securityStart != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            date = df.format(securityStart);
        }
        return date;
    }

    public void setSecurityStart(Date date) {
        this.securityStart = date;
    }

    @JsonIgnore
    public Date getSecurityEnd() {
        return securityEnd;
    }

    @JsonIgnore
    public String getSecurityEndString() {
        String date = "";
        if (securityEnd != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            date = df.format(securityEnd);
        }
        return date;
    }

    @JsonIgnore
    public void setSecurityEnd(Date date) {
        this.securityEnd = date;
    }

    @JsonIgnore
    public int getSecurityVerify() {
        return securityVerify;
    }

    @JsonIgnore
    public void setSecurityVerify(int verify) {
        this.securityVerify = verify;
    }

    /**
     * @return The website in web corresponding to Facebook. if none, returns
     * default facebook.com
     */
    @JsonIgnore
    public String getFacebook() {
        if (web != null) {
            for (Website w : web) {
                if (w.getAddress().contains("facebook") && !w.getAddress().equals("http://facebook.com")) {
                    return w.getAddress();
                }
            }
        }

        return "http://facebook.com";
    }

    /**
     * Removes previous facebook URLs and adds the new one
     *
     * @param newAddress The new website that a user will pass in through a text
     * box
     */
    public void updateFacebook(String newAddress) {
        Website w2 = new Website();
        w2.setAddress(newAddress);

        int index = web.size() - 1;
        List<Website> temp = web;

        while (index >= 0) {
            if (temp.get(index).getAddress().contains("facebook")) {
                web.remove(index);
            }
            index--;
        }

        web.add(w2);
    }

    /**
     * @return The website in web corresponding to Twitter. if none, returns
     * default twitter.com
     */
    @JsonIgnore
    public String getTwitter() {
        if (web != null) {
            for (Website w : web) {
                if (w.getAddress().contains("twitter") && !w.getAddress().equals("http://twitter.com")) {
                    return w.getAddress();
                }
            }
        }

        return "http://twitter.com";
    }

    /**
     * Removes previous twitter URLs and adds the new one
     *
     * @param newAddress The new website that a user will pass in through a text
     * box
     */
    public void updateTwitter(String newAddress) {
        Website w2 = new Website();
        w2.setAddress(newAddress);

        int index = web.size() - 1;
        List<Website> temp = web;

        while (index >= 0) {
            if (temp.get(index).getAddress().contains("twitter")) {
                web.remove(index);
            }
            index--;
        }

        web.add(w2);
    }

    /**
     * @return The website in web corresponding to Linkedin. if none, returns
     * default linkedin.com
     */
    @JsonIgnore
    public String getLinkedin() {
        if (web != null) {
            for (Website w : web) {
                if (w.getAddress().contains("linkedin") && !w.getAddress().equals("http://linkedin.com")) {
                    return w.getAddress();
                }
            }
        }

        return "http://linkedin.com";
    }

    /**
     * Removes previous linkedin URLs and adds the new one
     *
     * @param newAddress The new website that a user will pass in through a text
     * box
     */
    public void updateLinkedin(String newAddress) {
        Website w2 = new Website();
        w2.setAddress(newAddress);

        int index = web.size() - 1;
        List<Website> temp = web;

        while (index >= 0) {
            if (temp.get(index).getAddress().contains("linkedin")) {
                web.remove(index);
            }
            index--;
        }

        web.add(w2);
    }

    /**
     * @return The website in web corresponding to Github. if none, returns
     * default github.com
     */
    @JsonIgnore
    public String getGithub() {
        if (web != null) {
            for (Website w : web) {
                if (w.getAddress().contains("github") && !w.getAddress().equals("http://github.com")) {
                    return w.getAddress();
                }
            }
        }

        return "http://github.com";
    }

    /**
     * Removes previous github URLs and adds the new one
     *
     * @param newAddress The new website that a user will pass in through a text
     * box
     */
    public void updateGithub(String newAddress) {
        Website w2 = new Website();
        w2.setAddress(newAddress);

        int index = web.size() - 1;
        List<Website> temp = web;

        while (index >= 0) {
            if (temp.get(index).getAddress().contains("github")) {
                web.remove(index);
            }
            index--;
        }

        web.add(w2);
    }

    @JsonIgnore
    public HashMap<String, Date> getApplications() {
        return applications;
    }

    @JsonIgnore
    public void setApplications(HashMap<String, Date> applications) {
        this.applications = applications;
    }

    @JsonIgnore
    public Date getStartDate() {
        return startDate;
    }

    @JsonIgnore
    public String getStartDateString() {
        String date = "";
        if (startDate != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            date = df.format(startDate);
        }
        return date;
    }

    @JsonIgnore
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @JsonIgnore
    public String getPreferedLocation() {
        return preferedLocation;
    }
    
     @JsonIgnore
    public void setPreferedLocation(String preferedLocation) {
        this.preferedLocation = preferedLocation;
    }

}
