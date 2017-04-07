/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.company;


import java.util.ArrayList;
import java.util.List;
import net.acesinc.ats.model.common.StoredFile;
import net.acesinc.ats.model.common.Website;

/**
 *
 * @author dylankolson
 */
public class Application {
    
    private String name;
    private String id;
    private Website url;
    private String description;
    private StoredFile screenshot;
    private List<POC> POCs = new ArrayList();

    public Application(String name, String description) {
        this.name = name;
        this.id = name.replaceAll("\\s+","");
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StoredFile getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(StoredFile screenshot) {
        this.screenshot = screenshot;
    }

    public List<POC> getPOCs() {
        return POCs;
    }

    public void setPOCs(List<POC> POCs) {
        this.POCs = POCs;
    }

    public Website getUrl() {
        return url;
    }

    public void setUrl(Website url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
    
    
    
}
