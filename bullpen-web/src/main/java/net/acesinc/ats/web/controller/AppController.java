/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import net.acesinc.ats.model.common.Website;
import net.acesinc.ats.model.company.Application;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.company.POC;
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
    
    @RequestMapping(value = {"/application"}, method = RequestMethod.GET)
    public String getApplicationPage(Principal user, ModelMap model, @RequestParam("name") String name,
            @RequestParam("description") String description) {
        model.addAttribute("pageName", "Application");
        
        User u = userRepo.findByEmail(user.getName());
        Company c = u.getCompany();
        
        Application app = null ;
        List<Application> applications = c.getApplications();


        for(Application a: applications)
        {
            if(a.getName().equals(name) && a.getDescription().equals(description))
                app = a;
        }
        model.addAttribute("application", app);
        model.addAttribute("poc",app.getPOCs().get(0));//need to change when we have more than 1 poc
        return "application";
    }
    
    @RequestMapping(value = {"/createapplication"}, method = RequestMethod.POST)
    public String createApp(Principal user, ModelMap model, @RequestParam("name") String name,
            @RequestParam("description") String description,@RequestParam("url") String url,
            @RequestParam("poc[][name]") String[] names, @RequestParam("poc[][email]") String[] emails,
        @RequestParam("poc[][phone]") String[] phones) {
        model.addAttribute("pageName", "Dashboard");
        
       
        User u = userRepo.findByEmail(user.getName());
        Company c = u.getCompany();

        Application a = new Application(name,description);
        //change to loop later to make for multiple POCs
        POC p = new POC();
        p.setEmail(emails[0]);
        p.setName(names[0]);
        p.setPhone(phones[0]);
        List<POC> pocs = a.getPOCs();
        pocs.add(p);
        a.setPOCs(pocs);
        
        Website w = new Website();
        w.setAddress(url);
        a.setUrl(w);

        List<Application> apps = c.getApplications();
        apps.add(a);
        c.setApplications(apps);
        
        companyRepo.save(c);
       
        return "redirect:/dashboard";
    }
    
     @RequestMapping(value = {"/saveapplication"}, method = RequestMethod.POST)
    public String updateApplication(Principal user, ModelMap model, @RequestParam("name") String name,
            @RequestParam("description") String description,@RequestParam("url") String url,
            @RequestParam("poc[][name]") String[] names, @RequestParam("poc[][email]") String[] emails,
        @RequestParam("poc[][phone]") String[] phones,  @RequestParam("id") String id) {
        model.addAttribute("pageName", "Dashboard");
        
        User u = userRepo.findByEmail(user.getName());
        Company c = u.getCompany();
        
        
         Application app = null;
         int counter = -1;
         for(Application a: c.getApplications())
        {
            
            if(a.getId().equals(id))
            {
                app =  a;
                
            }
            
            counter++;
          
        }
         
         if(app != null){
         
        app.setId(name.trim());
        app.setDescription(description);
        app.setName(name);
        Website w = new Website();
        w.setAddress(url);
        app.setUrl(w);
        
        POC p = app.getPOCs().get(0);
        p.setEmail(emails[0]);
        p.setName(names[0]);
        p.setPhone(phones[0]);
        app.getPOCs().set(0, p);
        
        c.getApplications().set(counter, app);
        companyRepo.save(c);
         }
        
        return "redirect:/dashboard";
        
    }

    @RequestMapping(value = {"/deleteapplication"}, method = RequestMethod.POST)
    public String deleteApp(Principal user, ModelMap model, @RequestParam("name") String name,
                            @RequestParam("description") String description) {

        Company c = userRepo.findByEmail(user.getName()).getCompany();
        List<Application> applications = c.getApplications();
        Application appForDeletion = null ;//= applications.get(applications.indexOf(new Application(name, description)));

        for(Application a: applications)
        {
            if(a.getName().equals(name) && a.getDescription().equals(description))
                appForDeletion = a;
        }
        
        if (appForDeletion != null) {
                applications.remove(appForDeletion);
        }

        c.setApplications(applications);
        companyRepo.save(c);

        return "redirect:/dashboard";
    }

}
