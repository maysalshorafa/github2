package com.pos.leaders.leaderspossystem.Payment;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;

/**
 * Created by KARAM on 02/07/2018.
 */

public class PaymentTable {
    private double due;
    private double tendered;
    private double change;
    private String paymentMethod;
    private CurrencyType currency;
    private double actualCurrencyRate;
    private String tempCurrency;
    public PaymentTable() {
    }

    public PaymentTable(String tempCurrency ,double due, double tendered, double change, String paymentMethod, CurrencyType currency,double actualCurrencyRate) {
        this.due = due;
        this.tendered = tendered;
        this.change = change;
        this.paymentMethod = paymentMethod;
        this.currency = currency;
        this.actualCurrencyRate=actualCurrencyRate;
        this.tempCurrency=tempCurrency;
    }

    //region Getters

    public String getTempCurrency() {
        return tempCurrency;
    }

    public double getDue() {
        return due;
    }

    public double getTendered() {
        return tendered;
    }

    public double getChange() {
        return change;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public double getActualCurrencyRate() {
        return actualCurrencyRate;
    }
//endregion Getters

    //region Setters

    public void setTempCurrency(String tempCurrency) {
        this.tempCurrency = tempCurrency;
    }

    public void setDue(double due) {
        this.due = due;
    }

    public void setTendered(double tendered) {
        this.tendered = tendered;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public void setActualCurrencyRate(double actualCurrencyRate) {
        this.actualCurrencyRate = actualCurrencyRate;
    }
//endregion Setters

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            if(e.getMessage()!=null) {
                Log.e(getClass().getName(), e.getMessage(), e);
            }
            return "";
        }
    }
}
