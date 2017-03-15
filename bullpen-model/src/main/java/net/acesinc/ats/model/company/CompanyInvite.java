/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.company;

import java.util.Date;
import net.acesinc.ats.model.user.User;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 *
 * @author andrewserff
 */
public class CompanyInvite {
    private String id;
    @DBRef
    private Company invitedToCompany;
    private String emailInvited;
    @DBRef
    private User userWhoInvited;
    @DBRef
    private User usedByUser;
    private Date dateCreated;
    private Date dateAccepted;
    private String role;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the userWhoInvited
     */
    public User getUserWhoInvited() {
        return userWhoInvited;
    }

    /**
     * @param userWhoInvited the userWhoInvited to set
     */
    public void setUserWhoInvited(User userWhoInvited) {
        this.userWhoInvited = userWhoInvited;
    }

    /**
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the dateAccepted
     */
    public Date getDateAccepted() {
        return dateAccepted;
    }

    /**
     * @param dateAccepted the dateAccepted to set
     */
    public void setDateAccepted(Date dateAccepted) {
        this.dateAccepted = dateAccepted;
    }

    /**
     * @return the usedByUser
     */
    public User getUsedByUser() {
        return usedByUser;
    }

    /**
     * @param usedByUser the usedByUser to set
     */
    public void setUsedByUser(User usedByUser) {
        this.usedByUser = usedByUser;
    }

    /**
     * @return the invitedToCompany
     */
    public Company getInvitedToCompany() {
        return invitedToCompany;
    }

    /**
     * @param invitedToCompany the invitedToCompany to set
     */
    public void setInvitedToCompany(Company invitedToCompany) {
        this.invitedToCompany = invitedToCompany;
    }

    /**
     * @return the emailInvited
     */
    public String getEmailInvited() {
        return emailInvited;
    }

    /**
     * @param emailInvited the emailInvited to set
     */
    public void setEmailInvited(String emailInvited) {
        this.emailInvited = emailInvited;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
