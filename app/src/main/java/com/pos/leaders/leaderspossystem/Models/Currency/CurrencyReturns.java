package com.pos.leaders.leaderspossystem.Models.Currency;

/**
 * Created by Win8.1 on 9/28/2017.
 */

public class CurrencyReturns {
    private long currencyReturnsId;
    private long saleId;
    private double amount;
    private long currency_type;
    private long createDate;
    public CurrencyReturns() {
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }


    public long getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(long currency_type) {
        this.currency_type = currency_type;
    }




    // Constructors
    public CurrencyReturns(long currencyReturnsId, long saleId, double amount, long createDate, long currency_type) {
        this.currencyReturnsId = currencyReturnsId;
        this.amount = amount;
        this.saleId = saleId;
        this.currency_type=currency_type;
        this.createDate=createDate;
    }

    public CurrencyReturns(CurrencyReturns p) {
        this(p.getCurrencyReturnsId(),  p.getSaleId(),p.getAmount(),p.getCreateDate(),p.getCurrency_type());
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
                ", createDate='" + createDate + '\'' +
                ", saleId=" + saleId +
                '}';
    }
}
