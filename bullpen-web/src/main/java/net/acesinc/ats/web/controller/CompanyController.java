/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.acesinc.ats.model.common.Address;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.web.data.Result;
import net.acesinc.ats.web.repository.CompanyRepository;
import net.acesinc.ats.web.repository.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author andrewserff
 */
@Controller
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    private ConstantsController constants;
 

    private void populateCompanyProfileModel(User u, ModelMap model) {
        if (u != null) {
            model.addAttribute("company", u.getCompany());
            model.addAttribute("companyUsers", userRepo.findByCompany(u.getCompany()));
            constants.populateModel(u.getCompany(), model);
        }
        model.addAttribute("pageName", "Company Profile");

    }

    @RequestMapping(value = "/company/profile", method = RequestMethod.GET)
    public String getCompanyProfilePage(Principal user, ModelMap model) {
        User u = userRepo.findByEmail(user.getName());
        populateCompanyProfileModel(u, model);
        return "company";
    }
    
    @RequestMapping(value = "/company/remove-location", method = RequestMethod.POST)
    public String removeCompanyLocation(Principal user, ModelMap model, @RequestParam("location") String currentLocation) {
        User u = userRepo.findByEmail(user.getName());
        Company c = u.getCompany();
        if (c != null) {
            if (c.getCompanyLocations() != null) {
                for (int i = 0; i < c.getCompanyLocations().size(); i ++) {
                    if (c.getCompanyLocations().get(i).getLocationName().equals(currentLocation)) { 
                        c.getCompanyLocations().remove(i);                       
                        companyRepo.save(c);
                    }
                }
            } else {
                model.addAttribute("error", true);
                model.addAttribute("errMessage", "An error occurred when trying to delete location. Please try again later.");
            }
        } else {
            model.addAttribute("error", true);
            model.addAttribute("errMessage", "An error occurred when trying to delete location. Please try again later.");
        }
        
        populateCompanyProfileModel(u, model);
        return "redirect:/company/profile";
    }
    
    @RequestMapping(value = "/editRole", method = RequestMethod.POST)
    public ResponseEntity editUserRole(Principal user, ModelMap model, @RequestParam("userEdit") String userEmail, @RequestParam("premission") String role) {
 
        User editUser = userRepo.findByEmail(userEmail);
        editUser.setAuthorities(role);
        userRepo.save(editUser);
        editUser = null;
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    @RequestMapping(value = "/company/profile", method = RequestMethod.POST)
    public String updateCompanyProfile(Principal user, ModelMap model, HttpServletRequest req,
            @RequestParam("companyName") String companyName, @RequestParam("companyAddress") String companyAddress, @RequestParam("companyCity") String companyCity, @RequestParam("companyState") String companyState, @RequestParam("companyZipcode") String companyZipcode,
             @RequestParam("companyDescription") String companyDescription,  @RequestParam("companyWebsite") String companyWebsite, @RequestParam("companyEmail") String companyEmail) {
        model.addAttribute("pageName", "Company Profile");
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            Company c = u.getCompany();
            c.setName(companyName);


            
            Address primaryAddr = c.getPrimaryAddress();
            primaryAddr.setLocationName(companyName);
            primaryAddr.setAddressLine(companyAddress);
            primaryAddr.setCityName(companyCity);
            primaryAddr.setState(companyState);
            primaryAddr.setPostalCode(companyZipcode);
            c.setPrimaryAddress(primaryAddr);

            c.setCompanyDescription(companyDescription);
            c.setWebsiteUrl(companyWebsite);
            c.setPublicContactEmail(companyEmail);

            int index = 1;
            List<Address> addys = c.getCompanyLocations();
            if (addys == null) {
                addys = new ArrayList<>();
            }
            int existingAddys = addys.size();
            boolean newAddress = false;
            while (req.getParameter("loc-address-" + index) != null) {
                Address addy = null;
                if (existingAddys >= index) {
                    newAddress = false;
                    addy = addys.get(index - 1);
                } else {
                    newAddress = true;
                    addy = new Address();
                }
                                
                if (req.getParameter("loc-name-" + index).trim() != "") {
                    addy.setLocationName(req.getParameter("loc-name-" + index));
                    addy.setAddressLine(req.getParameter("loc-address-" + index));
                    addy.setCityName(req.getParameter("loc-city-" + index));
                    addy.setState(req.getParameter("loc-state-" + index));
                    addy.setPostalCode(req.getParameter("loc-zipcode-" + index));
                } else {
                    model.addAttribute("error", true);
                    model.addAttribute("errMessage", "Please enter a valid name when creating a Company Location");
                    populateCompanyProfileModel(u, model);
                    return "company";
                }
                
                if (newAddress) {
                    addys.add(addy);
                }

                index++;
            }
            if (!addys.isEmpty()) {
                c.setCompanyLocations(addys);
            }
            
            companyRepo.save(c);

        }
        populateCompanyProfileModel(u, model);
        return "company";
    }

    @RequestMapping(value = "/company/location", method = RequestMethod.GET, produces = {"application/xml", "application/json"})
    public @ResponseBody
    Result getCompanyLocationByName(Principal user, @RequestParam("locationName") String locationName) {
        User u = userRepo.findByEmail(user.getName());
        if (u != null) {
            if ("primary".equalsIgnoreCase(locationName)) {
                return Result.ok(u.getCompany().getPrimaryAddress());
            } else {
                List<Address> addys = u.getCompany().getCompanyLocations();
                for (Address a : addys) {
                    if (a.getLocationName().equals(locationName)) {
                        return Result.ok(a);
                    }
                }
            }
        }
        return Result.error(null, "Location not found");
    }

    public String getDefaultShortName(String name) throws UnsupportedEncodingException {
        String shortName = constants.convertToShortName(name);
        shortName = URLEncoder.encode(shortName, "UTF-8");
        Company test = companyRepo.findByShortName(shortName);
        if (test != null) {
            shortName = shortName + "-" + RandomStringUtils.randomAlphanumeric(8);
            shortName = URLEncoder.encode(shortName, "UTF-8");
        }
        return shortName;
    }

    public Company getCompanyByNameOrId(String name) {
        Company c = companyRepo.findByShortName(name);
        if (c == null) {
            //maybe it's an id then
            c = companyRepo.findOne(name);
        }
        return c;
    }
}
