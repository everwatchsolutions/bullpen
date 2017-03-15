/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author andrewserff
 */
public class Competency extends IdentifibleObject {
    @JsonProperty("CompetencyID")
    private String competencyID;
    @JsonProperty("CompetencyName")
    private String competencyName;
    @JsonProperty("CompetencyLevel")
    private String competencyLevel;

    /**
     * @return the competencyID
     */
    public String getCompetencyID() {
        return competencyID;
    }

    /**
     * @param competencyID the competencyID to set
     */
    public void setCompetencyID(String competencyID) {
        this.competencyID = competencyID;
    }

    /**
     * @return the competencyName
     */
    public String getCompetencyName() {
        return competencyName;
    }

    /**
     * @param competencyName the competencyName to set
     */
    public void setCompetencyName(String competencyName) {
        this.competencyName = competencyName;
        this.setUniqueId(competencyName);
    }

    /**
     * @return the competencyLevel
     */
    public String getCompetencyLevel() {
        return competencyLevel;
    }

    /**
     * @param competencyLevel the competencyLevel to set
     */
    public void setCompetencyLevel(String competencyLevel) {
        this.competencyLevel = competencyLevel;
    }
}
