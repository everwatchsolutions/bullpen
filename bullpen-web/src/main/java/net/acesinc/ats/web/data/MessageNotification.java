/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.data;

import java.util.Date;

/**
 *
 * @author andrewserff
 */
public class MessageNotification {
    private String message;
    private Date notificationTime;

    public MessageNotification() {
        notificationTime = new Date();
    }

    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the notificationTime
     */
    public Date getNotificationTime() {
        return notificationTime;
    }

    /**
     * @param notificationTime the notificationTime to set
     */
    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }
    
    
}
