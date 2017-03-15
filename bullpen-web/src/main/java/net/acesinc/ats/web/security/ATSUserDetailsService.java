/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.security;

import net.acesinc.ats.model.user.User;
import net.acesinc.ats.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author andrewserff
 */
@Service
public class ATSUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepo.findByEmail(username);
        if (u == null) {
            throw new UsernameNotFoundException("User [ " + username + " ] Not Found!");
        } else {
            return u;
        }
    }
    
}
