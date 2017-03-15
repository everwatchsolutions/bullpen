/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/*import net.acesinc.ats.model.candidate.PreferenceType;
*
 *
 * @author andrewserff
 */
public class Phone extends IdentifibleObject {
    @JsonProperty("Number")
    private String number;
    @JsonProperty("Label")
    private ContactType label;
    @JsonProperty("Preferred")
    private PreferenceType preferred;

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
        this.setUniqueId(number);
    }

    /**
     * @return the label
     */
    public ContactType getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(ContactType label) {
        this.label = label;
    }

    /**
     * @return the preferred
     */
    public PreferenceType getPreferred() {
        return preferred;
    }

    /**
     * @param preferred the preferred to set
     */
    public void setPreferred(PreferenceType preferred) {
        this.preferred = preferred;
    }
}
