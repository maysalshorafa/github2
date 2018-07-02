package com.pos.leaders.leaderspossystem.Payment;

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

    public PaymentTable() {
    }

    public PaymentTable(double due, double tendered, double change, String paymentMethod, CurrencyType currency) {
        this.due = due;
        this.tendered = tendered;
        this.change = change;
        this.paymentMethod = paymentMethod;
        this.currency = currency;
    }

    //region Getters

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


    //endregion Getters

    //region Setters

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


    //endregion Setters

}
