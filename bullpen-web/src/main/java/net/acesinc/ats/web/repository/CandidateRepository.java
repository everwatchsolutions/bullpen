/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.repository;

import java.util.List;
import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.company.Company;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author andrewserff
 */
public interface CandidateRepository extends PagingAndSortingRepository<Candidate, String>, QueryDslPredicateExecutor<Candidate> {
    public Candidate findByGivenNameAndFamilyName(String givenName, String familyName);
    public Candidate findByOwnerCompanyAndGivenNameAndFamilyName(Company c, String givenName, String familyName);
    public List<Candidate> findByOwnerCompanyOrderByFormattedNameAsc(Company c);
    public Candidate findByIdAndOwnerCompany(String id, Company c);
    }
