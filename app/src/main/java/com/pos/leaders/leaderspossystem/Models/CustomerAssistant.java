package com.pos.leaders.leaderspossystem.Models;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 9/28/2017.
 */

public class CustomerAssistant {
    private long custAssistantId;
    private long orderId;
    private long customerAssistantID;
    private double amount;
    private int type;
    private String salesCase;
    private Timestamp createdAt;



    public CustomerAssistant() {
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public CustomerAssistant(long custAssistantId, long orderId, long customerAssistantID, double amount, int type, String salesCase, Timestamp createdAt) {
        this.custAssistantId = custAssistantId;
        this.orderId = orderId;
        this.customerAssistantID = customerAssistantID;
        this.amount = amount;
        this.type = type;
        this.salesCase = salesCase;
        this.createdAt = createdAt;
    }

    public long getCustAssistantId() {
        return custAssistantId;
    }

    public void setCustAssistantId(long custAssistantId) {
        this.custAssistantId = custAssistantId;
    }

    public long getOrderId() {
        return orderId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public long getCustomerAssistantID() {
        return customerAssistantID;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setCustomerAssistantID(long customerAssistantID) {
        this.customerAssistantID = customerAssistantID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSalesCase() {
        return salesCase;
    }

    public void setSalesCase(String salesCase) {
        this.salesCase = salesCase;
    }




}
