/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author andrewserff
 */
public class Address extends IdentifibleObject {
    @JsonProperty("LocationName")
    private String locationName;
    @JsonProperty("CountryCode")
    private String countryCode;
    @JsonProperty("CountrySubDivisionCode")
    private String countrySubDivisionCode;
    @JsonProperty("CityName")
    private String cityName;
    private String state;
    @JsonProperty("PostalCode")
    private String postalCode;
    @JsonProperty("AddressLine")
    private String addressLine;
    @JsonProperty("Label")
    private String label;
    @JsonProperty("Latitude")
    private Long latitude;
    @JsonProperty("Longitude")
    private Long longitude;

    @Override
    public String getUniqueId() {
        if (this.uniqueId != null) {
            return super.getUniqueId();
        } else {
            return getAddressLine() + " " + getCityName() + ", " + state + " " + postalCode;
        }
    }

    
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
     * @return the countrySubDivisionCode
     */
    public String getCountrySubDivisionCode() {
        return countrySubDivisionCode;
    }

    /**
     * @param countrySubDivisionCode the countrySubDivisionCode to set
     */
    public void setCountrySubDivisionCode(String countrySubDivisionCode) {
        this.countrySubDivisionCode = countrySubDivisionCode;
    }

    /**
     * @return the cityName
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * @param cityName the cityName to set
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the addressLine
     */
    public String getAddressLine() {
        return addressLine;
    }

    /**
     * @param addressLine the addressLine to set
     */
    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the latitude
     */
    public Long getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public Long getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
