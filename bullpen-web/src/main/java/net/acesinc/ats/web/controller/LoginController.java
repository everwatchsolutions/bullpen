/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author andrewserff
 */
@Controller
public class LoginController {
    private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    
    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String getLoginPage(ModelMap model) {
        model.addAttribute("pageName", "Login");
        model.addAttribute("currentYear", yearFormat.format(new Date()));
        return "login";
    }
}
