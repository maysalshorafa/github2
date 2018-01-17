package com.pos.leaders.leaderspossystem.Models.Currency;

import java.util.Date;

/**
 * Created by Win8.1 on 9/24/2017.
 */

public class CurrencyOperation {
    private long id;
    private Date createDate;
    private long operation_id;
    private String operation_type;
    private double amount;
    private long currency_type;

    public CurrencyOperation() {
    }

    public CurrencyOperation(long id, Date createDate, long operation_id, String operation_type, double amount, long currency_type) {
        this.id = id;
        this.createDate = createDate;
        this.operation_id = operation_id;
        this.operation_type = operation_type;
        this.amount = amount;
        this.currency_type = currency_type;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(long operation_id) {
        this.operation_id = operation_id;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
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
}
