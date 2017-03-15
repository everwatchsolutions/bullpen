/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.repository;

import net.acesinc.ats.model.candidate.CandidateHistory;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author andrewserff
 */
public interface CandidateHistoryRepository extends PagingAndSortingRepository<CandidateHistory, String> {
    public CandidateHistory findByCandidateId(String candidateId);
}
