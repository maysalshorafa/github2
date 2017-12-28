package com.pos.leaders.leaderspossystem.Models.Currency;

import android.content.Context;

import java.util.Date;

/**
 * Created by Win8.1 on 9/25/2017.
 */

public class Currency {
    private long id;
    private String name;
    private String country;
    private String currencyCode;
    private double rate;

    public Currency() {

    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    private Date lastUpdate;


    public Currency(long id, String name, String country, String currencyCode, double rate, Date lastUpdate) {
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

    public void setRate(long rate) {
        this.rate = rate;
    }
}
