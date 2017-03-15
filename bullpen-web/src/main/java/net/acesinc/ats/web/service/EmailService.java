/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.service;

import java.text.SimpleDateFormat;
import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.candidate.notes.CandidateFileNote;
import net.acesinc.ats.model.candidate.notes.CandidateMeetingNote;
import net.acesinc.ats.model.candidate.notes.CandidateNote;
import net.acesinc.ats.model.candidate.notes.CandidateTextNote;
import net.acesinc.ats.model.company.Company;
import net.acesinc.ats.model.company.CompanyInvite;
import net.acesinc.ats.model.opening.Opening;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.model.workflow.WorkflowState;
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

        String subject = "Welcome to PolarisATS";
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

        String subject = "Password Reset for PolarisATS";

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

        String subject = "You've been invited to the " + invite.getInvitedToCompany().getName() + " ATS!";

        log.info("Sending Company Invite for [ " + invite.getInvitedToCompany().getName() + " ] to [ " + invite.getEmailInvited() + " ] with invite token [ " + invite.getId() + " ]");
        sendEmail(invite.getEmailInvited(), noReplyEmailAddress, subject, htmlContent);
    }

    @Async
    public void sendApplicationRecievedEmail(User user, Candidate candidate, Opening opening) {
        final Context ctx = new Context();

        addDefaultInfoToContext(ctx, user.getCompany(), user);

        ctx.setVariable("candidateName", candidate.getFormattedName());
        ctx.setVariable("candidateId", candidate.getId());
        if (candidate.getEmail() != null && !candidate.getEmail().isEmpty()) {
            ctx.setVariable("candidateEmailHash", candidate.getEmail().get(0).getAddressMD5Hash());
        }

        ctx.setVariable("openingId", opening.getShortName());
        ctx.setVariable("openingTitle", opening.getPositionTitle());

        // Create the HTML body using Thymeleaf 
        final String htmlContent = this.templatingEngine.process("application-received.html", ctx);

        String subject = "Application Recieved for Opening " + opening.getPositionTitle() + "!";

        log.info("Sending Application Received email for [ " + opening.getOwnerCompany().getName() + ":" + opening.getPositionTitle() + " ] to [ " + user.getEmail() + " ]");
        sendEmail(user.getEmail(), noReplyEmailAddress, subject, htmlContent);
    }

    @Async
    public void sendApplicationThankYouEmail(String receipient, Candidate candidate, Opening opening) {
        final Context ctx = new Context();

        addDefaultInfoToContext(ctx, opening.getOwnerCompany(), null);

        ctx.setVariable("candidateName", candidate.getFormattedName());

        ctx.setVariable("openingId", opening.getShortName());
        ctx.setVariable("openingTitle", opening.getPositionTitle());
        ctx.setVariable("companyEmail", opening.getOwnerCompany().getPublicContactEmail());
        ctx.setVariable("companyWebsite", opening.getOwnerCompany().getWebsiteUrl());
        ctx.setVariable("companyName", opening.getOwnerCompany().getName());

        log.debug("Creating Application Thank You Email");
        // Create the HTML body using Thymeleaf 
        final String htmlContent = this.templatingEngine.process("application-thankyou.html", ctx);

        String subject = "Thank you for applying at " + opening.getOwnerCompany().getName() + "!";

        log.info("Sending Application Received Thank You email for [ " + opening.getOwnerCompany().getName() + ":" + opening.getPositionTitle() + " ] to Candidate [ " + receipient + " ]");
        sendEmail(receipient, noReplyEmailAddress, subject, htmlContent);
    }

    @Async
    public void sendUserCandidateNoteAddedEmail(User u, String subject, Candidate candidate, CandidateNote note, User editor) {
        final Context ctx = new Context();

        addDefaultInfoToContext(ctx, u.getCompany(), u);

        ctx.setVariable("editorFullName", editor.getFullName());
        ctx.setVariable("candidateName", candidate.getFormattedName());
        ctx.setVariable("candidateId", candidate.getId());
        if (candidate.getEmail() != null && !candidate.getEmail().isEmpty()) {
            ctx.setVariable("candidateEmailHash", candidate.getEmail().get(0).getAddressMD5Hash());
        }

        ctx.setVariable("noteType", note.getName());
        ctx.setVariable("noteName", note.getDisplayName());
        ctx.setVariable("noteTitle", note.getTitle());
        if (note instanceof CandidateTextNote) {
            ctx.setVariable("noteText", ((CandidateTextNote) note).getText());
        } else if (note instanceof CandidateMeetingNote) {
            ctx.setVariable("noteMeetingDate", prettyDateFormat.format(((CandidateMeetingNote) note).getMeetingDate()));
            ctx.setVariable("noteMeetingLocation", ((CandidateMeetingNote) note).getMeetingLocation());
            ctx.setVariable("noteMeetingDesc", ((CandidateMeetingNote) note).getMeetingDescription());
        } else if (note instanceof CandidateFileNote) {
            ctx.setVariable("noteFilename", ((CandidateFileNote) note).getStoredFile().getFilename());
        }

        // Create the HTML body using Thymeleaf 
        final String htmlContent = this.templatingEngine.process("candidate-note-added.html", ctx);

        log.info("Sending User [ " + u.getFullName() + " ] notification email with subject [ " + subject + " ]");
        sendEmail(u.getEmail(), noReplyEmailAddress, subject, htmlContent);
    }

    @Async
    public void sendUserCandidateStateTransitionEmail(User u, String subject, Candidate candidate, WorkflowState oldState, WorkflowState newState, User editor) {
        final Context ctx = new Context();

        addDefaultInfoToContext(ctx, u.getCompany(), u);

        ctx.setVariable("editorFullName", editor.getFullName());
        ctx.setVariable("candidateName", candidate.getFormattedName());
        ctx.setVariable("candidateId", candidate.getId());
        if (candidate.getEmail() != null && !candidate.getEmail().isEmpty()) {
            ctx.setVariable("candidateEmailHash", candidate.getEmail().get(0).getAddressMD5Hash());
        }

        ctx.setVariable("oldStateName", oldState.getDisplayName());
        ctx.setVariable("newStateName", newState.getDisplayName());

        // Create the HTML body using Thymeleaf 
        final String htmlContent = this.templatingEngine.process("candidate-state-transition.html", ctx);

        log.info("Sending User [ " + u.getFullName() + " ] notification email with subject [ " + subject + " ]");
        sendEmail(u.getEmail(), noReplyEmailAddress, subject, htmlContent);
    }

    @Async
    public abstract void sendEmail(String toAddress, String fromAddress, String subject, String message);
}
