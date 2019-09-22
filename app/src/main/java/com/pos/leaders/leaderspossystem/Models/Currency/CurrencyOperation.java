package com.pos.leaders.leaderspossystem.Models.Currency;

import java.sql.Timestamp;


/**
 * Created by Win8.1 on 9/24/2017.
 */

public class CurrencyOperation {
    private long currencyOperationId;
    private Timestamp createdAt;
    private long operationId;
    private String operationType;
    private double amount;
    private String currencyType;
    private String paymentWay;
    public CurrencyOperation() {
    }

    public CurrencyOperation(long currencyOperationId, Timestamp createdAt, long operation_id, String operation_type, double amount, String currency_type,String payment_way) {
        this.currencyOperationId = currencyOperationId;
        this.createdAt = createdAt;
        this.operationId = operation_id;
        this.operationType = operation_type;
        this.amount = amount;
        this.currencyType = currency_type;
        this.paymentWay =payment_way;
    }

    public String getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
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

    public long getOperationId() {
        return operationId;
    }

    public void setOperationId(long operationId) {
        this.operationId = operationId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    @Override
    public String toString() {
        return "CurrencyOperation{" +
                "currencyOperationId=" + currencyOperationId +
                ", createdAt=" + createdAt +
                ", operationId=" + operationId +
                ", operationType='" + operationType + '\'' +
                ", amount=" + amount +
                ", currencyType='" + currencyType + '\'' +
                ", paymentWay='" + paymentWay + '\'' +
                '}';
    }
}
