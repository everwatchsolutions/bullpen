/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrewserff
 */
public class Website extends IdentifibleObject {
    private static final Logger log = LoggerFactory.getLogger(Website.class);
    
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Label")
    private WebsiteType label;
    @JsonProperty("LabelDescription")
    private String labelDescription;

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
        try {
            URL url = new URL(address);
            String protocol = url.getProtocol();
            String result = address.replaceFirst(protocol + ":", "");
            if (result.startsWith("//")) {
                result = result.substring(2);
            }
            this.setUniqueId(result);
        } catch (MalformedURLException e) {
            log.warn("Website Address [ " + address + " ] is not a valid URL");
            this.setUniqueId(address);
        }
    }

    /**
     * @return the label
     */
    public WebsiteType getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(WebsiteType label) {
        this.label = label;
    }

    /**
     * @return the labelDescription
     */
    public String getLabelDescription() {
        return labelDescription;
    }

    /**
     * @param labelDescription the labelDescription to set
     */
    public void setLabelDescription(String labelDescription) {
        this.labelDescription = labelDescription;
    }
    
    /**
     * @return the address without the prepending "http://" if it has it
     */
    public String getTrimmedAddress() {
        String result;
        if (this.getAddress().startsWith("http://")) {
            result = this.getAddress().substring(7);
        } else {
            result = this.getAddress();
        }
        
        return result;
    }
}
