/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import java.security.Principal;
import net.acesinc.ats.model.company.Application;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.web.repository.CandidateRepository;
import net.acesinc.ats.web.repository.CompanyRepository;
import net.acesinc.ats.web.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andrewserff
 */
@Controller
public class DashboardController {


    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    private CandidateRepository candidateRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ConstantsController constants;

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @RequestMapping(value = {"/dashboard"}, method = RequestMethod.GET)
    public String getHomepage(Principal user, ModelMap model) {
        model.addAttribute("pageName", "Dashboard");
        User u = userRepo.findByEmail(user.getName());
        Company c = u.getCompany();
        model.addAttribute("companies", companyRepo.findAll());
        model.addAttribute("apps", c.getApplications());
        

        return "dashboard";
    }

   

}
