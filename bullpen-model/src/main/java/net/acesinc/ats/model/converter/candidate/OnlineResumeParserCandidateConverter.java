/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.converter.candidate;

/**
 *
 * @author andrewserff
 */
public class OnlineResumeParserCandidateConverter extends HRXML25CandidateConverter {

    @Override
    protected String getFormattedNameNodeName() {
        return "FullName";
    }

    @Override
    protected String getPersonalPhoneNodeName() {
        return "PhoneBasic";
    }

    @Override
    protected String getFaxNodeName() {
        return "FaxPhone";
    }

    @Override
    protected String getMobilePhoneNodeName() {
        return "phone_Mobile";
    }
    
    
    
    
}
