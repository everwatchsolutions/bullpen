/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.service;

import java.text.SimpleDateFormat;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.company.CompanyInvite;

import net.acesinc.ats.model.user.User;

import net.acesinc.ats.web.data.EmailVerifyRequest;
import net.acesinc.ats.web.data.PasswordResetRequest;
import net.acesinc.ats.web.repository.EmailVerifyRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

/**
 *
 * @author andrewserff
 */
@Service
@PropertySource({"classpath:${spring.profiles.active}/application.properties"})
public abstract class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private SpringTemplateEngine templatingEngine;
    @Autowired
    private EmailVerifyRequestRepository emailVerifyRepo;

    @Value("${email.noReplyEmailAddress}")
    private String noReplyEmailAddress;// = "ACES ATS <noreply@ats.acesinc.net>";
    @Value("${email.supportEmailAddress}")
    private String supportEmailAddress;// = "support@ats.acesinc.net";
    @Value("${baseUrl}")
    private String baseUrl;// = "http://localhost:8080";
    private SimpleDateFormat prettyDateFormat = new SimpleDateFormat("dd MMMMM yyyy @ hh:mm aa");

    public Context addDefaultInfoToContext(Context ctx, Company company, User user) {
        String companyLogoUrl = baseUrl + "/assets/images/ats-logo-email.png";
        if (company != null) {
            if (company.getCompanyLogo() != null) {
                companyLogoUrl = baseUrl + "/view/file/" + company.getCompanyLogo().getStorageId();
            }
            ctx.setVariable("companyId", company.getShortName());
            ctx.setVariable("companyName", company.getName());
        }
        ctx.setVariable("companyLogoUrl", companyLogoUrl);
        ctx.setVariable("baseUrl", baseUrl);
        ctx.setVariable("contactEmail", supportEmailAddress);

        if (user != null) {
            ctx.setVariable("user", user);
            ctx.setVariable("userName", user.getFirstName());
            ctx.setVariable("userFullName", user.getFullName());
            ctx.setVariable("userEmail", user.getEmail());
        }

        return ctx;
    }

    @Async
    public void sendWelcomeEmail(User user) {
        EmailVerifyRequest req = new EmailVerifyRequest();
        req.setEmail(user.getEmail());
        emailVerifyRepo.save(req);

        final Context ctx = new Context();
        addDefaultInfoToContext(ctx, user.getCompany(), user);
        
        ctx.setVariable("verifyToken", req.getId());

        // Create the HTML body using Thymeleaf 
        final String htmlContent = this.templatingEngine.process("welcome-email.html", ctx);

        String subject = "Welcome to Bullpen";
        sendEmail(user.getEmail(), noReplyEmailAddress, subject, htmlContent);
    }

    @Async
    public void sendPasswordResetEmail(PasswordResetRequest req) {
        final Context ctx = new Context();
        addDefaultInfoToContext(ctx, null, null);

        ctx.setVariable("resetToken", req.getId());
        ctx.setVariable("email", req.getEmail());

        // Create the HTML body using Thymeleaf 
        final String htmlContent = this.templatingEngine.process("password-reset.html", ctx);

        String subject = "Password Reset for Bullpen";

        log.info("Sending password reset email to [ " + req.getEmail() + " ] with resetToken [ " + req.getId() + " ]");
        sendEmail(req.getEmail(), noReplyEmailAddress, subject, htmlContent);
    }

    @Async
    public void sendCompanyInviteEmail(CompanyInvite invite) {
        final Context ctx = new Context();
        addDefaultInfoToContext(ctx, invite.getInvitedToCompany(), null);

        ctx.setVariable("inviteId", invite.getId());
        ctx.setVariable("email", invite.getEmailInvited());

        // Create the HTML body using Thymeleaf 
        final String htmlContent = this.templatingEngine.process("company-invite.html", ctx);

        String subject = "You've been invited to the " + invite.getInvitedToCompany().getName() + " Bullpen Business Platform!";

        log.info("Sending Company Invite for [ " + invite.getInvitedToCompany().getName() + " ] to [ " + invite.getEmailInvited() + " ] with invite token [ " + invite.getId() + " ]");
        sendEmail(invite.getEmailInvited(), noReplyEmailAddress, subject, htmlContent);
    }

    
   
    
    @Async
    public abstract void sendEmail(String toAddress, String fromAddress, String subject, String message);
}
