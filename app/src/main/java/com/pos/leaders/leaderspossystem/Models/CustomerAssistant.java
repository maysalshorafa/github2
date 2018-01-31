package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 9/28/2017.
 */

public class CustomerAssistant {
    private long id ;
    private long order_id;
    private long customerAssistantID;
    private double amount;
    private int type;
    private String salesCase;
    private long saleDate;



    public CustomerAssistant() {
    }

    public void setSaleDate(long saleDate) {
        this.saleDate = saleDate;
    }


    public CustomerAssistant(long id, long order_id, long customerAssistantID, double amount, int type, String salesCase, long saleDate) {
        this.id = id;
        this.order_id = order_id;
        this.customerAssistantID = customerAssistantID;
        this.amount = amount;
        this.type = type;
        this.salesCase = salesCase;
        this.saleDate=saleDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrder_id() {
        return order_id;
    }

    public long getSaleDate() {
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
