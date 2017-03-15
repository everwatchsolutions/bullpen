/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.repository;

import java.util.List;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.company.CompanyInvite;
import net.acesinc.ats.model.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author andrewserff
 */
public interface CompanyInviteRepository extends PagingAndSortingRepository<CompanyInvite, String> {
    public List<CompanyInvite> findByUserWhoInvited(User u);
    public List<CompanyInvite> findByInvitedToCompany(Company c);
    public CompanyInvite findByEmailInvited(String email);
}
