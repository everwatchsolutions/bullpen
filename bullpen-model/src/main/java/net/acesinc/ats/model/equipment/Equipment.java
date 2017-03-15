package net.acesinc.ats.model.equipment;

import net.acesinc.ats.model.common.StoredFile;
import net.acesinc.ats.model.company.Company;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Myles on 1/10/17.
 */
public class Equipment {
    private String id;
    private String type;
    private String usedFor;
    private Date datePurchased;
    private String model;
    private String assignedTo;
    private Date dateAssigned;
    private String serial;
    private Company company;
    private StoredFile agreement;


    public String getId(){
        return id;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getUsedFor(){
        return usedFor;
    }

    public void setUsedFor(String usedFor){
        this.usedFor = usedFor;
    }

    public Date getDatePurchased(){
        return datePurchased;
    }


    public void setDatePurchased(Date datePurchased){
        this.datePurchased = datePurchased;

    }

    public String getDatePurchasedString() {
        String date = "N/A";
        if (datePurchased != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            date = df.format(datePurchased);
        }
        return date;
    }

    public String getDateAssignedString() {
        String date = "";
        if (dateAssigned != null) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            date = df.format(dateAssigned);
        }
        return date;
    }

    public String getModel(){
        return model;
    }

    public void setModel(String model){
        this.model = model;

    }


    public String getAssignedTo(){
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo){
        this.assignedTo = assignedTo;
    }

    public Date getDateAssigned(){
        return dateAssigned;
    }

    public void setDateAssigned(Date dateAssigned){
        this.dateAssigned = dateAssigned;
    }

    public String getSerial(){
        return serial;
    }

    public void setSerial(String serial){
        this.serial=serial;
    }

    public Company getCompany(){
        return company;
    }

    public void setCompany(Company company){
        this.company=company;
    }

    public StoredFile getAgreement(){
        return agreement;
    }

    public void setAgreement(StoredFile agreement){
        this.agreement = agreement;
    }

}
