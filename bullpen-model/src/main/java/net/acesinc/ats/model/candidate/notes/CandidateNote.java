/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.candidate.notes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author andrewserff
 */
public abstract class CandidateNote  {

    public static final String titleFieldName = "note-title";
    private Date dateCreated;
    
    public CandidateNote() {
        dateCreated = new Date();
        
       
    }
    
    public void setTitle(String title) {
        //setRequiredInfoFieldValue(titleFieldName, title);
    }
    public String getTitle() {
        return (String)"implment" ;//getRequiredInfoFieldValue(titleFieldName);
    }

    /**
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    
    @Override
    public String toString(){
        return this.getTitle();
}
    
}
