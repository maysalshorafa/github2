package com.pos.leaders.leaderspossystem.Models.Currency;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 9/27/2017.
 */

public class CashPayment {
    private long cashPaymentId;
    private long orderId;
    private double amount;
    private long currency_type;
    private Timestamp createdAt;
    private double currencyRate;
    private double actualCurrencyRate;
    public CashPayment() {
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }



    public long getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(long currency_type) {
        this.currency_type = currency_type;
    }




    // Constructors
    public CashPayment(long cashPaymentId, long orderId, double amount, long currency_type, Timestamp createdAt,double currencyRate , double actualCurrencyRate) {
        this.cashPaymentId = cashPaymentId;
        this.amount = amount;
        this.orderId = orderId;
        this.currency_type=currency_type;
        this.createdAt = createdAt;
        this.currencyRate=currencyRate;
        this.actualCurrencyRate=actualCurrencyRate;
    }

    public CashPayment(CashPayment p) {
        this(p.getCashPaymentId(),  p.getOrderId(),p.getAmount(),p.getCurrency_type(),p.getCreatedAt(),p.getCurrencyRate(),p.getActualCurrencyRate());
    }

    // Getters

    public double getActualCurrencyRate() {
        return actualCurrencyRate;
    }

    public void setActualCurrencyRate(double actualCurrencyRate) {
        this.actualCurrencyRate = actualCurrencyRate;
    }

    public long getCashPaymentId() {
        return cashPaymentId;
    }


    public long getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setCashPaymentId(long cashPaymentId) {
        this.cashPaymentId = cashPaymentId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        this.currencyRate = currencyRate;
    }

    @Override
    public String toString() {
        return "{" +"\"@type\":" + "\"CashPayment\""+
                ",\"cashPaymentId\":" +cashPaymentId +
                ",\"currency_type\":" + currency_type  +
                ",\"amount\":" + amount +
                ",\"orderId\":" + orderId +
                ",\"currencyRate\":" + currencyRate +
                ",\"actualCurrencyRate\":" + actualCurrencyRate +
                "}";
    }



}
