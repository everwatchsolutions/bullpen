/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.repository;

import net.acesinc.ats.model.company.Company;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author andrewserff
 */
public interface CompanyRepository extends PagingAndSortingRepository<Company, String> {
    public Company findByNameIgnoreCase(String name);
    public Company findByShortName(String shortName);
}
