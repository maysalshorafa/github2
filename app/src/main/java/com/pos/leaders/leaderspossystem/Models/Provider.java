package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Win8.1 on 7/2/2019.
 */

public class Provider {
    private long providerId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String job;
    private String phoneNumber;
    private String street;
    private boolean hide;
    private int city;
    String houseNumber;
    String postalCode;
    String country;
    String countryCode;
    Double balance;
    String providerCode;
    String providerIdentity;
    int branchId;
    public Provider() {
    }

    public Provider(long providerId, String firstName, String lastName, String gender, String email, String job, String phoneNumber, String street, boolean hide, int city,  String houseNumber, String postalCode, String country, String countryCode,double balance,String providerCode,String providerIdentity,int branchId) {
        this.providerId = providerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.job = job;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.hide = hide;
        this.city = city;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.country = country;
        this.countryCode = countryCode;
        this.balance=balance;
        this.providerCode =providerCode;
        this.providerIdentity =providerIdentity;
        this.branchId=branchId;
    }

    public Provider(Provider provider){
        this(provider.getProviderId(),provider.getFirstName(),provider.getLastName(),provider.getGender(),
                provider.getEmail(),provider.getJob(),provider.getPhoneNumber(),provider.getStreet(),
                provider.getHide(),provider.getCity(),provider.getHouseNumber()
                ,provider.getPostalCode(),provider.getCountry(),provider.getCountryCode(),provider.getBalance(),provider.getProviderCode(),provider.getProviderIdentity(),provider.getBranchId());
    }
    @JsonIgnore
    public String getFullName() {
        if (firstName == null)
            firstName = "";
        if (lastName == null)
            lastName = "";
        return firstName + " " + lastName;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getLastName() {
        return lastName;
    }
    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public long getProviderId() {
        return providerId;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getJob() {
        return job;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public boolean getHide() {
        return hide;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public void setBirthday(String birthday) {
        this.lastName = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.street = address;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getProviderIdentity() {
        return providerIdentity;
    }

    public void setProviderIdentity(String providerIdentity) {
        this.providerIdentity = providerIdentity;
    }

    @Override
    public String toString() {
        return "{" +
                "\""+"providerId"+ "\""+":" + "\""+providerId+"\"" +
                ","+"\""+"firstName"+ "\""+":" + "\""+ firstName+"\""  +
                ","+"\""+"lastName"+ "\""+":" + "\""+ lastName +"\"" +
                ","+"\""+"gender"+ "\""+":" + "\""+  gender +"\"" +
                ","+"\""+"email"+ "\""+":" + "\""+  email +"\"" +
                ","+"\""+"job"+ "\""+":" + "\""+ job +"\"" +
                ","+"\""+"phoneNumber"+ "\""+":" + "\""+  phoneNumber +"\"" +
                ","+"\""+"street"+ "\""+":" + "\""+ street +"\"" +
                ","+"\""+"hide"+ "\""+":" + "\""+  hide +"\"" +
                ","+"\""+"city"+ "\""+":" + "\""+  city +"\""  +
                ","+"\""+"houseNumber"+ "\""+":" + "\""+ houseNumber +"\"" +
                ","+"\""+"postalCode"+ "\""+":" + "\""+  postalCode +"\"" +
                ","+"\""+"country"+ "\""+":" + "\""+  country +"\"" +
                ","+"\""+"countryCode"+ "\""+":" + "\""+  countryCode +"\"" +
                ","+"\""+"balance"+ "\""+":" + "\""+ balance +"\""  +
                ","+"\""+"providerCode"+ "\""+":" + "\""+ providerCode +"\"" +
                ","+"\""+"providerIdentity"+ "\""+":" + "\""+ providerIdentity +"\"" +
                ","+"\""+"branchId"+ "\""+":" + "\""+ branchId +"\"" +
                '}';
    }
}
