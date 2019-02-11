package com.pos.leaders.leaderspossystem.Models.Currency;

import java.sql.Timestamp;


/**
 * Created by Win8.1 on 9/24/2017.
 */

public class CurrencyOperation {
    private long currencyOperationId;
    private Timestamp createdAt;
    private long operation_id;
    private String operation_type;
    private double amount;
    private String currency_type;

    public CurrencyOperation() {
    }

    public CurrencyOperation(long currencyOperationId, Timestamp createdAt, long operation_id, String operation_type, double amount, String currency_type) {
        this.currencyOperationId = currencyOperationId;
        this.createdAt = createdAt;
        this.operation_id = operation_id;
        this.operation_type = operation_type;
        this.amount = amount;
        this.currency_type = currency_type;
    }


    public long getCurrencyOperationId() {
        return currencyOperationId;
    }

    public void setCurrencyOperationId(long currencyOperationId) {
        this.currencyOperationId = currencyOperationId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    @Override
    public String toString() {
        return "CurrencyOperation{" +
                "currencyOperationId=" + currencyOperationId +
                ", createdAt=" + createdAt +
                ", operation_id=" + operation_id +
                ", operation_type='" + operation_type + '\'' +
                ", amount=" + amount +
                ", currency_type='" + currency_type + '\'' +
                '}';
    }
}
