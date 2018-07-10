package com.pos.leaders.leaderspossystem.Models.Currency;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 9/25/2017.
 */

public class Currency {
    private long id;
    private String name;
    private String country;
    private String currencyCode;
    private double rate;
    private Timestamp lastUpdate;

    public Currency() {

    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }




    public Currency(long id, String name, String country, String currencyCode, double rate, Timestamp lastUpdate) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.currencyCode = currencyCode;
        this.rate = rate;
        this.lastUpdate = lastUpdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", rate=" + rate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
