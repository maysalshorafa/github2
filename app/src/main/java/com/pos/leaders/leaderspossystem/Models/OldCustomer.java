package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by KARAM on 20/05/2017.
 */

public class OldCustomer {
    int id;
    String firstName;
    String lastName;
    String street;
    String houseNumber;
    String city;
    String postalCode;
    String country;
    String countryCode;
    public OldCustomer() {
        this.id = 0;
        this.firstName = "";
        this.lastName = "";
        this.street = "";
        this.houseNumber = "";
        this.city = "";
        this.postalCode = "";
        this.country = "";
        this.countryCode = "";
    }

    public OldCustomer(int id, String firstName, String lastName, String street, String houseNumber, String city, String postalCode, String country, String countryCode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.countryCode = countryCode;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
