/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.common;

import net.acesinc.ats.model.company.Company;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 *
 * @author andrewserff
 */
public class CategoryItem {
    private String id;
    private String name;
    private String displayName;
    @DBRef
    private Category category;
    @DBRef
    private Company ownerCompany;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the ownerCompany
     */
    public Company getOwnerCompany() {
        return ownerCompany;
    }

    /**
     * @param ownerCompany the ownerCompany to set
     */
    public void setOwnerCompany(Company ownerCompany) {
        this.ownerCompany = ownerCompany;
    }

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
}
