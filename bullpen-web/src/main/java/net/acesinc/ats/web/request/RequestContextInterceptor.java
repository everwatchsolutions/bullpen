/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author andrewserff
 */
@Service
public class RequestContextInterceptor extends HandlerInterceptorAdapter {
    
    @Autowired
    private UserRepository userRepo;
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model) throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User u = null;
            if (o instanceof User) {
                u = (User) o;
            } else if (o instanceof String) {
                String email = o.toString();
                if (!"".equals(email) && email.contains("@")) {
                    u = userRepo.findByEmail(email);
                } else {
//                    log.debug("Unable to locate user for email [ " + email + " ]");
                }
            }
            
            if (u != null) {
                if (model != null) {
                    model.addObject("user", u);
                }
            }
        }
    }
}
