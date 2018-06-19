package com.pos.leaders.leaderspossystem.Models.Currency;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 9/28/2017.
 */

public class CurrencyReturns {
    private long currencyReturnsId;
    private long orderId;
    private double amount;
    private long currency_type;
    private Timestamp createdAt;
    private boolean hide;

    // Constructors
    public CurrencyReturns() {
    }

    public CurrencyReturns(long currencyReturnsId, long orderId, double amount, Timestamp createdAt, long currency_type) {
        this.currencyReturnsId = currencyReturnsId;
        this.amount = amount;
        this.orderId = orderId;
        this.currency_type=currency_type;
        this.createdAt = createdAt;
    }

    public CurrencyReturns(CurrencyReturns p) {
        this(p.getCurrencyReturnsId(),  p.getOrderId(),p.getAmount(),p.getCreatedAt(),p.getCurrency_type());
    }

    //region Getters
    public long getCurrencyReturnsId() {
        return currencyReturnsId;
    }

    public long getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public long getCurrency_type() {
        return currency_type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public boolean isHide() {
        return hide;
    }

    //endregion

    //region setters
    public void setCurrencyReturnsId(long currencyReturnsId) {
        this.currencyReturnsId = currencyReturnsId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrency_type(long currency_type) {
        this.currency_type = currency_type;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    //endregion

    @Override
    public String toString() {
        return "Payment{" +
                "currencyReturnsId=" + currencyReturnsId +
                ",currency_type ='" + currency_type + '\'' +
                ", amount='" + amount + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
