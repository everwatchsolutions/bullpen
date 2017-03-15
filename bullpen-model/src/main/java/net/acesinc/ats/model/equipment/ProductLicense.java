package net.acesinc.ats.model.equipment;

import net.acesinc.ats.model.company.Company;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Myles on 2/6/17.
 */
public class ProductLicense {
    private String id;
    private Date expirationDate;
    private String productName;
    private String licenseKey;
    private Company company;

    public String getId(){
        return id;
    }

    public Date getExpirationDate(){
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate){
        this.expirationDate = expirationDate;
    }

    public String getExpDateString() {
        String date = "N/A";
        if (expirationDate != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            date = df.format(expirationDate);
        }
        return date;
    }

    public String getProductName(){
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public String getLicenseKey(){
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey){
        this.licenseKey = licenseKey;
    }

    public Company getCompany(){
        return company;
    }

    public void setCompany(Company company){
        this.company = company;
    }

}