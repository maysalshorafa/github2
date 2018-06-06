package com.pos.leaders.leaderspossystem.Models;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 9/28/2017.
 */

public class CustomerAssistant {
    private long custAssistantId;
    private long order_id;
    private long customerAssistantID;
    private double amount;
    private int type;
    private String salesCase;
    private Timestamp saleDate;



    public CustomerAssistant() {
    }

    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }


    public CustomerAssistant(long custAssistantId, long order_id, long customerAssistantID, double amount, int type, String salesCase, Timestamp saleDate) {
        this.custAssistantId = custAssistantId;
        this.order_id = order_id;
        this.customerAssistantID = customerAssistantID;
        this.amount = amount;
        this.type = type;
        this.salesCase = salesCase;
        this.saleDate=saleDate;
    }

    public long getCustAssistantId() {
        return custAssistantId;
    }

    public void setCustAssistantId(long custAssistantId) {
        this.custAssistantId = custAssistantId;
    }

    public long getOrder_id() {
        return order_id;
    }

    public Timestamp getSaleDate() {
        return saleDate;
    }

    public long getCustomerAssistantID() {
        return customerAssistantID;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
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
