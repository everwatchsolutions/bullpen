/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import net.acesinc.ats.model.company.Company;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author andrewserff
 */
public class User implements UserDetails{
    private String id;
    @DBRef
    private Company company;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String emailMD5Hash;
    private String password;
    private boolean enabled;
    private String profilePictureUrl;
    private ArrayList<GrantedAuthority> authorities;

    public User() {
        createAuthorities();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User && ((User)obj).getId().equals(id)) {
            return true;
        } else {
            return false;
        }
    }
    
    
    
    private void createAuthorities() {
        if (authorities == null) {
            authorities = new ArrayList<GrantedAuthority>();
            //All users get the ROLE_USER authority
            addAuthority(new SimpleGrantedAuthority("ROLE_USER"));
        }
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

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the profilePictureUrl
     */
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    /**
     * @param profilePictureUrl the profilePictureUrl to set
     */
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the emailMD5Hash
     */
    public String getEmailMD5Hash() {
        return emailMD5Hash;
    }

    /**
     * @param emailMD5Hash the emailMD5Hash to set
     */
    public void setEmailMD5Hash(String emailMD5Hash) {
        this.emailMD5Hash = emailMD5Hash;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public void addAuthority(GrantedAuthority auth) {
        createAuthorities();
        this.authorities.add(auth);
    }
    
     public void addAuthorityInvite(GrantedAuthority auth) {
        this.authorities.add(auth);
    }
    
    public void setAuthorities(Collection<? extends GrantedAuthority> auths) {
        this.authorities = new ArrayList(authorities);
    }
    @JsonIgnore
    public void setAuthorities(String role)
    {
       authorities = new ArrayList<GrantedAuthority>();
       addAuthorityInvite(new SimpleGrantedAuthority(role));
       
    }
    @JsonIgnore
    public String getAuthority()//assumes they can only have one role/authority
    {
        String[] parts = authorities.get(0).getAuthority().split("_");
        return  parts[1];
    }
    
    @Override
    public ArrayList<? extends GrantedAuthority> getAuthorities() {
        createAuthorities();
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @return the company
     */
    public Company getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    
}
