/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.repository;

import java.util.List;
import net.acesinc.ats.model.common.Certification;
import net.acesinc.ats.model.company.Company;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author andrewserff
 */
public interface CertificationRepository extends PagingAndSortingRepository<Certification, String> {
    public Certification findByNameAndOwnerCompany(String name, Company company);
    public List<Certification> findByOwnerCompany(Company company);
}
