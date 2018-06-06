package com.pos.leaders.leaderspossystem.Models.Currency;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 9/28/2017.
 */

public class CurrencyReturns {
    private long currencyReturnsId;
    private long saleId;
    private double amount;
    private long currency_type;
    private Timestamp createdAt;
    public CurrencyReturns() {
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
    public CurrencyReturns(long currencyReturnsId, long saleId, double amount, Timestamp createdAt, long currency_type) {
        this.currencyReturnsId = currencyReturnsId;
        this.amount = amount;
        this.saleId = saleId;
        this.currency_type=currency_type;
        this.createdAt = createdAt;
    }

    public CurrencyReturns(CurrencyReturns p) {
        this(p.getCurrencyReturnsId(),  p.getSaleId(),p.getAmount(),p.getCreatedAt(),p.getCurrency_type());
    }

    // Getters
    public long getCurrencyReturnsId() {
        return currencyReturnsId;
    }


    public long getSaleId() {
        return saleId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "currencyReturnsId=" + currencyReturnsId +
                ",currency_type ='" + currency_type + '\'' +
                ", amount='" + amount + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", saleId=" + saleId +
                '}';
    }
}
