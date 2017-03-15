/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.common;

/**
 *
 * @author andrewserff
 */
public enum EducationLevel {
    none("None"),
    highschool("High School"),
    bachelors("Bachelors"),
    masters("Masters"),
    doctorate("Doctorate");
    
    private String displayName;
    private String name;

    private EducationLevel() {
    }
    
    private EducationLevel(String displayName) {
        this.displayName = displayName;
        this.name = name();
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name();
    }
}
