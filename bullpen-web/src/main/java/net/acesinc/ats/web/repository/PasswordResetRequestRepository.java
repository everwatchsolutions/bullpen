/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.repository;

import net.acesinc.ats.web.data.PasswordResetRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author andrewserff
 */
public interface PasswordResetRequestRepository extends PagingAndSortingRepository<PasswordResetRequest, String> {
    public PasswordResetRequest findByEmail(String email);
    public PasswordResetRequest findByIdAndEmail(String id, String email);
}
