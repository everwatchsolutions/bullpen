/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.candidate;

import net.acesinc.ats.model.common.AttendanceStatusType;
import net.acesinc.ats.model.common.Address;
import net.acesinc.ats.model.common.NameAndCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import net.acesinc.ats.model.common.IdentifibleObject;

/**
 *
 * @author andrewserff
 */
public class EducationalOrganization extends IdentifibleObject {
    @JsonProperty("School")
    private String school;
    @JsonProperty("SubSchool")
    private List<String> subSchool;
    @JsonProperty("ReferenceLocation")
    private Address referenceLocation;
    @JsonProperty("EducationLevel")
    private List<NameAndCode> educationLevel;
    @JsonProperty("AttendanceStatusCode")
    private AttendanceStatusType attendanceStatusCode;
    @JsonProperty("AttendanceStartDate")
    private String attendanceStartDate;
    @JsonProperty("AttendanceEndDate")
    private String attendanceEndDate;
    @JsonProperty("DegreeType")
    private List<NameAndCode> degreeType;
    @JsonProperty("DegreeDate")
    private String degreeDate;
    @JsonProperty("MajorProgramName")
    private List<String> majorProgramName;
    @JsonProperty("MinorProgramName")
    private List<String> minorProgramName;
    @JsonProperty("AcademicHonors")
    private String academicHonors;
    @JsonProperty("Comment")
    private String comment;
    @JsonProperty("UserArea")
    private Object userArea;

    /**
     * @return the school
     */
    public String getSchool() {
        return school;
    }

    /**
     * @param school the school to set
     */
    public void setSchool(String school) {
        this.school = school;
        this.setUniqueId(school);
    }

    /**
     * @return the subSchool
     */
    public List<String> getSubSchool() {
        return subSchool;
    }

    /**
     * @param subSchool the subSchool to set
     */
    public void setSubSchool(List<String> subSchool) {
        this.subSchool = subSchool;
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
     * @return the educationLevel
     */
    public List<NameAndCode> getEducationLevel() {
        return educationLevel;
    }

    /**
     * @param educationLevel the educationLevel to set
     */
    public void setEducationLevel(List<NameAndCode> educationLevel) {
        this.educationLevel = educationLevel;
    }

    /**
     * @return the attendanceStatusCode
     */
    public AttendanceStatusType getAttendanceStatusCode() {
        return attendanceStatusCode;
    }

    /**
     * @param attendanceStatusCode the attendanceStatusCode to set
     */
    public void setAttendanceStatusCode(AttendanceStatusType attendanceStatusCode) {
        this.attendanceStatusCode = attendanceStatusCode;
    }

    /**
     * @return the attendanceStartDate
     */
    public String getAttendanceStartDate() {
        return attendanceStartDate;
    }

    /**
     * @param attendanceStartDate the attendanceStartDate to set
     */
    public void setAttendanceStartDate(String attendanceStartDate) {
        this.attendanceStartDate = attendanceStartDate;
    }

    /**
     * @return the attendanceEndDate
     */
    public String getAttendanceEndDate() {
        return attendanceEndDate;
    }

    /**
     * @param attendanceEndDate the attendanceEndDate to set
     */
    public void setAttendanceEndDate(String attendanceEndDate) {
        this.attendanceEndDate = attendanceEndDate;
    }

    /**
     * @return the degreeType
     */
    public List<NameAndCode> getDegreeType() {
        return degreeType;
    }

    /**
     * @param degreeType the degreeType to set
     */
    public void setDegreeType(List<NameAndCode> degreeType) {
        this.degreeType = degreeType;
    }

    /**
     * @return the degreeDate
     */
    public String getDegreeDate() {
        return degreeDate;
    }

    /**
     * @param degreeDate the degreeDate to set
     */
    public void setDegreeDate(String degreeDate) {
        this.degreeDate = degreeDate;
    }

    /**
     * @return the majorProgramName
     */
    public List<String> getMajorProgramName() {
        return majorProgramName;
    }

    /**
     * @param majorProgramName the majorProgramName to set
     */
    public void setMajorProgramName(List<String> majorProgramName) {
        this.majorProgramName = majorProgramName;
    }

    /**
     * @return the minorProgramName
     */
    public List<String> getMinorProgramName() {
        return minorProgramName;
    }

    /**
     * @param minorProgramName the minorProgramName to set
     */
    public void setMinorProgramName(List<String> minorProgramName) {
        this.minorProgramName = minorProgramName;
    }

    /**
     * @return the academicHonors
     */
    public String getAcademicHonors() {
        return academicHonors;
    }

    /**
     * @param academicHonors the academicHonors to set
     */
    public void setAcademicHonors(String academicHonors) {
        this.academicHonors = academicHonors;
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
