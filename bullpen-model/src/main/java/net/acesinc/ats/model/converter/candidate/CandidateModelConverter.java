/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.converter.candidate;

import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.converter.ModelConverter;

/**
 *
 * @author andrewserff
 */
public abstract class CandidateModelConverter extends ModelConverter<Candidate> {

    public CandidateModelConverter() {
        super(Candidate.class);
    }
}
