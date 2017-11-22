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
    private String currency_code;
    private double rate;

    public Currency(Context context) {

    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    private Date createDate;


    public Currency(long id, String name, String country, String currency_code, double rate, Date createDate) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.currency_code = currency_code;
        this.rate = rate;
        this.createDate=createDate;
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

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }
}
