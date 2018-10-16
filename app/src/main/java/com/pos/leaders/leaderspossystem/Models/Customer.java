package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Win8.1 on 7/3/2017.
 */

public class Customer {
    private long customerId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String job;
    private String phoneNumber;
    private String street;
    private boolean hide;
    private int city;
    private long club;
    String houseNumber;
    String postalCode;
    String country;
    String countryCode;
    Double balance;
    CustomerType customerType;
    public Customer() {
    }

    public Customer(long customerId, String firstName, String lastName, String gender, String email, String job, String phoneNumber, String street, boolean hide, int city, long club, String houseNumber, String postalCode, String country, String countryCode,double balance,CustomerType customerType) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.job = job;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.hide = hide;
        this.city = city;
        this.club = club;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.country = country;
        this.countryCode = countryCode;
        this.balance=balance;
        this.customerType=customerType;
    }

    public Customer(Customer customer){
        this(customer.getCustomerId(),customer.getFirstName(),customer.getLastName(),customer.getGender(),
                customer.getEmail(),customer.getJob(),customer.getPhoneNumber(),customer.getStreet(),
                customer.getHide(),customer.getCity(),customer.getClub(),customer.getHouseNumber()
                ,customer.getPostalCode(),customer.getCountry(),customer.getCountryCode(),customer.getBalance(),customer.getCustomerType());
    }
    @JsonIgnore
    public String getFullName() {
        if (firstName == null)
            firstName = "";
        if (lastName == null)
            lastName = "";
        return firstName + " " + lastName;
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

    public long getClub() {
        return club;
    }

    public void setClub(long club) {
        this.club = club;
    }

    public long getCustomerId() {
        return customerId;
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

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
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

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    @Override
    public String toString() {
        return "{" +
                "\""+"customerId"+ "\""+":" + "\""+customerId+"\"" +
                ","+"\""+"firstName"+ "\""+":" + "\""+ firstName+"\""  +
                ","+"\""+"lastName"+ "\""+":" + "\""+ lastName +"\"" +
                ","+"\""+"gender"+ "\""+":" + "\""+  gender +"\"" +
                ","+"\""+"email"+ "\""+":" + "\""+  email +"\"" +
                ","+"\""+"job"+ "\""+":" + "\""+ job +"\"" +
                ","+"\""+"phoneNumber"+ "\""+":" + "\""+  phoneNumber +"\"" +
                ","+"\""+"street"+ "\""+":" + "\""+ street +"\"" +
                ","+"\""+"hide"+ "\""+":" + "\""+  hide +"\"" +
                ","+"\""+"city"+ "\""+":" + "\""+  city +"\"" +
                ","+"\""+"club"+ "\""+":" + "\""+ club +"\"" +
                ","+"\""+"houseNumber"+ "\""+":" + "\""+ houseNumber +"\"" +
                ","+"\""+"postalCode"+ "\""+":" + "\""+  postalCode +"\"" +
                ","+"\""+"country"+ "\""+":" + "\""+  country +"\"" +
                ","+"\""+"countryCode"+ "\""+":" + "\""+  countryCode +"\"" +
                ","+"\""+"balance"+ "\""+":" + "\""+ balance +"\"" +
                '}';
    }

}
