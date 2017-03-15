/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.acesinc.ats.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * @author andrewserff
 */
public class Email extends IdentifibleObject {

    @JsonProperty("Address")
    private String address;
    private String addressMD5Hash;
    @JsonProperty("Label")
    private ContactType label;
    @JsonProperty("Preferred")
    private PreferenceType preferred;

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
        this.setUniqueId(address);
        setAddressMD5Hash(DigestUtils.md5Hex(address));
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

    /**
     * @return the addressMD5Hash
     */
    public String getAddressMD5Hash() {
        return addressMD5Hash;
    }

    /**
     * @param addressMD5Hash the addressMD5Hash to set
     */
    public void setAddressMD5Hash(String addressMD5Hash) {
        this.addressMD5Hash = addressMD5Hash;
    }
}
