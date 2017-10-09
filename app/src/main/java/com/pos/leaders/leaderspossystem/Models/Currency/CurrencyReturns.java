package com.pos.leaders.leaderspossystem.Models.Currency;

import java.util.Date;

/**
 * Created by Win8.1 on 9/28/2017.
 */

public class CurrencyReturns {
    private long id;
    private long saleId;
    private double amount;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    private Date createDate;
    public long getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(long currency_type) {
        this.currency_type = currency_type;
    }

    private long currency_type;


    // Constructors
    public CurrencyReturns(long id,  long saleId, double amount,Date createDate,long currency_type) {
        this.id = id;
        this.amount = amount;
        this.saleId = saleId;
        this.currency_type=currency_type;
        this.createDate=createDate;
    }

    public CurrencyReturns(CurrencyReturns p) {
        this(p.getId(),  p.getSaleId(),p.getAmount(),p.getCreateDate(),p.getCurrency_type());
    }

    // Getters
    public long getId() {
        return id;
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
                "id=" + id +
                ",currency_type ='" + currency_type + '\'' +
                ", amount='" + amount + '\'' +
                ", createDate='" + createDate + '\'' +
                ", saleId=" + saleId +
                '}';
    }
}
