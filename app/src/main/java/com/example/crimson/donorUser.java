package com.example.crimson;

public class donorUser {
    public String userId, firstName, lastName, sex, nationalID, bloodType, phoneNo, lastDonationDate;

    public donorUser(){

    }

    public String getLastDonationDate() {
        return lastDonationDate;
    }

    public void setLastDonationDate(String lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }

    public donorUser(String userId, String firstName, String lastName, String sex, String nationalID, String bloodType, String phoneNo, String lastDonationDate){
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.nationalID = nationalID;
        this.bloodType = bloodType;
        this.phoneNo = phoneNo;
        this.lastDonationDate = lastDonationDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalID() {
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

}







