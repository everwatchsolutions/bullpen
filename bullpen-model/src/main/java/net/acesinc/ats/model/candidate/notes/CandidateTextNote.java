/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.candidate.notes;

import java.util.List;


/**
 *
 * @author andrewserff
 */
public class CandidateTextNote extends CandidateNote {

    public static final String textFieldName = "note-text";
    
    public CandidateTextNote() {
        super();
    
      
        
       
    }
    
    public void setText(String text) {
        //setRequiredInfoFieldValue(textFieldName, text);
    }
    public String getText() {
        return (String) "implent" ;//getRequiredInfoFieldValue(textFieldName);
    }
    
      @Override
    public String toString(){
        return this.getTitle() + " " + this.getText();

    
}
    
}
