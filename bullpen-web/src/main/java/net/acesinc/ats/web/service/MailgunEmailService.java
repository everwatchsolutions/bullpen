/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.web.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 *
 * @author andrewserff
 */
@Service
@PropertySource({"classpath:${spring.profiles.active}/application.properties"})
public class MailgunEmailService extends EmailService {
    private static final Logger log = LoggerFactory.getLogger(MailgunEmailService.class);
    
    @Value("${email.mailgun.api.key}")
    private String mailGunApiKey;// = "key-75879be021165ddbdfee3f6935629b45";
    @Value("${email.mailgun.api.resourcename}")
    private String mailGunDomainResourceName;// = "sandboxcf530c730a564135b8d4eba20603b0ef.mailgun.org";
    
    @Override
    public void sendEmail(String toAddress, String fromAddress, String subject, String message) {
        log.info("Sending email to [ " + toAddress + " ] from [ " + fromAddress + " ] with subject [ " + subject + " ] via MailGun[" + mailGunDomainResourceName + "]");
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api",mailGunApiKey));
        WebResource webResource = client.resource("https://api.mailgun.net/v2/" + mailGunDomainResourceName + "/messages");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", fromAddress);
        formData.add("to", toAddress);
        formData.add("subject", subject);
        formData.add("html", message);
        ClientResponse resp = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        if (ClientResponse.Status.OK.equals(ClientResponse.Status.fromStatusCode(resp.getStatus()))) {
            log.debug("Email [ " + subject + " ] sent successfully to [ " + toAddress + " ]");
        } else {
            log.error("FAILED[Code:" + resp.getStatus() + "] Sending email to [ " + toAddress + " ] from [ " + fromAddress + " ] with subject [ " + subject + " ] via MailGun[" + mailGunDomainResourceName + "]");
        }
    }
    
    public static void main(String[] args) {
        EmailService emailer = new MailgunEmailService();
        emailer.sendEmail("andrew@serff.net", "andrew.serff@acesinc.net", "Test from MailGun", "This is a test email from MailGun.  Did you get it???");
    }

}
