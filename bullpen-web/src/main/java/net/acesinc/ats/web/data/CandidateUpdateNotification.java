/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.data;

import net.acesinc.ats.model.candidate.Candidate;

/**
 *
 * @author andrewserff
 */
public class CandidateUpdateNotification extends MessageNotification {
    private Candidate candidate;

    public CandidateUpdateNotification() {
        super();
    }
    
    /**
     * @return the candidate
     */
    public Candidate getCandidate() {
        return candidate;
    }

    /**
     * @param candidate the candidate to set
     */
    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }
    
    
}
