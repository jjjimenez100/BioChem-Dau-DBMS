package ph.biochem.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Patient {
    private SimpleStringProperty MRN;
    private SimpleStringProperty name;
    private SimpleStringProperty homeAddress;
    private SimpleStringProperty contactNumber;
    private SimpleStringProperty gender;
    private SimpleStringProperty companyName;
    private SimpleStringProperty date;
    private SimpleStringProperty occupation;

    public Patient(String MRN, String name, String homeAddress, String contactNumber, String gender, String companyName, String date, String occupation) {
        this.MRN = new SimpleStringProperty(MRN);
        this.name = new SimpleStringProperty(name);
        this.homeAddress = new SimpleStringProperty(homeAddress);
        this.contactNumber = new SimpleStringProperty(contactNumber);
        this.gender = new SimpleStringProperty(gender);
        this.companyName = new SimpleStringProperty(companyName);
        this.date = new SimpleStringProperty(date);
        this.occupation = new SimpleStringProperty(occupation);
    }

    public String getDate(){
        return date.get();
    }

    public void setDate(String date){
        this.date.set(date);
    }

    public String getMRN() {
        return MRN.get();
    }

    public void setMRN(String MRN) {
        this.MRN.set(MRN);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getHomeAddress() {
        return homeAddress.get();
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress.set(homeAddress);
    }

    public String getContactNumber() {
        return contactNumber.get();
    }


    public void setContactNumber(String contactNumber) {
        this.contactNumber.set(contactNumber);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getOccupation() {
        return occupation.get();
    }

    public void setOccupation(String occupation) {
        this.occupation.set(occupation);
    }
}
