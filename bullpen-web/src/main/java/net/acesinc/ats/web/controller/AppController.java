/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import java.security.Principal;
import java.util.List;
import net.acesinc.ats.model.common.Website;
import net.acesinc.ats.model.company.Application;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.web.repository.CompanyRepository;
import net.acesinc.ats.web.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author dylan
 */
@Controller
public class AppController {


    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ConstantsController constants;

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @RequestMapping(value = {"/createapp"}, method = RequestMethod.GET)
    public String getCreateApp(Principal user, ModelMap model) {
        model.addAttribute("pageName", "Create App");
        return "create_app";
    }
    
    @RequestMapping(value = {"/createapplication"}, method = RequestMethod.POST)
    public String createApp(Principal user, ModelMap model, @RequestParam("name") String name,
            @RequestParam("description") String description,@RequestParam("url") String url) {
        model.addAttribute("pageName", "Dashboard");
       
        User u = userRepo.findByEmail(user.getName());
        Company c = u.getCompany();

        Application a = new Application(name,description);
        //POC p = new POC();
        
        Website w = new Website();
        w.setAddress(url);
        a.setUrl(w);
        List<Application> apps = c.getApplications();
        apps.add(a);
        c.setApplications(apps);
        
        companyRepo.save(c);
       
        return "redirect:/dashboard";
    }
    
    

   

}
