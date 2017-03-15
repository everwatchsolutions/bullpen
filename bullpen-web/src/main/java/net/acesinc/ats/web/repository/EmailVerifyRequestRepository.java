/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.repository;

import net.acesinc.ats.web.data.EmailVerifyRequest;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author andrewserff
 */
public interface EmailVerifyRequestRepository extends PagingAndSortingRepository<EmailVerifyRequest, String> {
    public EmailVerifyRequest findByEmail(String email);
    public EmailVerifyRequest findByIdAndEmail(String id, String email);
}
