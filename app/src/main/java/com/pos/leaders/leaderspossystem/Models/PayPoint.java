package com.pos.leaders.leaderspossystem.Models;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 7/27/2020.
 */

public class PayPoint {
    private long payPointId;
    private long orderId;
    private double amount;
    private long currency_type;
    private Timestamp createdAt;
    private double currencyRate;
    private double actualCurrencyRate;

    public PayPoint() {
    }

    public PayPoint(long payPointId, long orderId, double amount, long currency_type, Timestamp createdAt, double currencyRate, double actualCurrencyRate) {
        this.payPointId = payPointId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency_type = currency_type;
        this.createdAt = createdAt;
        this.currencyRate = currencyRate;
        this.actualCurrencyRate = actualCurrencyRate;
    }

    public long getPayPointId() {
        return payPointId;
    }

    public void setPayPointId(long payPointId) {
        this.payPointId = payPointId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(long currency_type) {
        this.currency_type = currency_type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public double getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        this.currencyRate = currencyRate;
    }

    public double getActualCurrencyRate() {
        return actualCurrencyRate;
    }

    public void setActualCurrencyRate(double actualCurrencyRate) {
        this.actualCurrencyRate = actualCurrencyRate;
    }

    @Override
    public String toString() {
        return "{" +"\"@type\":" + "\"PayPoint\""+
                ",\"payPointId\":" +payPointId +
                ",\"currency_type\":" + currency_type  +
                ",\"amount\":" + amount +
                ",\"orderId\":" + orderId +
                ",\"currencyRate\":" + currencyRate +
                ",\"actualCurrencyRate\":" + actualCurrencyRate +
                "}";
    }

}
