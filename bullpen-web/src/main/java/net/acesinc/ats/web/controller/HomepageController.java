/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andrewserff
 */
@Controller
public class HomepageController {

    private static final Logger log = LoggerFactory.getLogger(HomepageController.class);
    private final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    
    
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String getHomepage(ModelMap model) {
        model.addAttribute("pageName", "Login");
        model.addAttribute("currentYear", yearFormat.format(new Date()));
        return "login";
    }
}
