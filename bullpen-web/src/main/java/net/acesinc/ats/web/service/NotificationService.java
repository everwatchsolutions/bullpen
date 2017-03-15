/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.service;

import net.acesinc.ats.model.candidate.Candidate;
import net.acesinc.ats.model.candidate.notes.CandidateNote;
import net.acesinc.ats.model.opening.Opening;
import net.acesinc.ats.model.user.User;
import net.acesinc.ats.model.workflow.WorkflowState;
import net.acesinc.ats.web.data.CandidateUpdateNotification;
import net.acesinc.ats.web.data.MessageNotification;
import net.acesinc.ats.web.data.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author andrewserff
 */
@Service
@PropertySource({"classpath:${spring.profiles.active}/application.properties"})
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired
    private EmailService emailService;
    @Autowired
    private SimpMessagingTemplate brokerChannel;

    
    public void nofifyUsersOfCandidateNote(Candidate c, CandidateNote note, User editor) {
        if (c.getInterestedUsers() != null) {
            log.debug("Notifying " + c.getInterestedUsers().size() + " users of new Note for Candidate [ " + c.getFormattedName() + " ]");
            for (User u : c.getInterestedUsers()) {
                notifyUserOfCandidateNote(u, c, note, editor);
            }
        }
    }
    public void notifyUsersOfCandidateStateTransition(Candidate c, WorkflowState oldState, WorkflowState newState, User editor) {
        if (c.getInterestedUsers() != null) {
            log.debug("Notifying " + c.getInterestedUsers().size() + " users of new Workflow State for Candidate [ " + c.getFormattedName() + " ]");
            for (User u : c.getInterestedUsers()) {
                notifyUserOfStateTransition(u, c, oldState, newState, editor);
            }
        }
    }
    
    public void notifyUserOfCandidateNote(User u, Candidate c, CandidateNote note, User editor) {
        String subject = "Candidate " + c.getFormattedName() + " has been updated";
        doCandidateNotify(u, c, subject);
        emailService.sendUserCandidateNoteAddedEmail(u, subject, c, note, editor);
    }
    public void notifyUserOfStateTransition(User u, Candidate c, WorkflowState oldState, WorkflowState newState, User editor) {
        String subject = "Candidate " + c.getFormattedName() + " now in State " + newState.getDisplayName();
        doCandidateNotify(u, c, subject);
        emailService.sendUserCandidateStateTransitionEmail(u, subject, c, oldState, newState, editor);
    }
    public void nofifyUserOfApplicantForOpening(User u, Candidate applicant, Opening o) {
        String subject = "Application Recieved for Opening " + o.getPositionTitle() + "!";
        doCandidateNotify(u, applicant, subject);
        emailService.sendApplicationRecievedEmail(u, applicant, o);
    }
    public void sendEmail(String to, String from,String subject, String body)
    {
        emailService.sendEmail(to, from, subject, body);
    }
    
    private void doCandidateNotify(User user, final Candidate c, final String message) {
        CandidateUpdateNotification n = new CandidateUpdateNotification();
        n.setCandidate(c);
        n.setMessage(message);
        
        Notification<CandidateUpdateNotification> notification = new Notification<>("candidate-message");
        notification.setPayload(n);

        doNotify(user, n);
    }

    @Async
    private void doNotify(User user, MessageNotification message) {
        Notification<MessageNotification> notification = new Notification<>("message");
        notification.setPayload(message);

        String topicName = "/topic/user/" + user.getId() + "/notifications";
        if (log.isDebugEnabled()) {
            log.debug("Sending notification to topic [ " + topicName + " ]: " + notification.toString());
        }

        brokerChannel.convertAndSend(topicName, notification);
    }
    
}
