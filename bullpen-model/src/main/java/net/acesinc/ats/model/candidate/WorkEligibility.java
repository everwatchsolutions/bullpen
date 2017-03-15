/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author andrewserff
 */
public class WorkEligibility {
    @JsonProperty("CountryCode")
    private String countryCode;
    @JsonProperty("Permanent")
    private Boolean permanent;

    /**
     * @return the countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return the permanent
     */
    public Boolean getPermanent() {
        return permanent;
    }

    /**
     * @param permanent the permanent to set
     */
    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }
}
