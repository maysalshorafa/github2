package com.pos.leaders.leaderspossystem.Models;

import java.util.Date;

/**
 * Created by Win8.1 on 9/28/2017.
 */

public class CustomerAssest {
    private long id ;

    public CustomerAssest(long id, long order_id, long custmerAssestID, double amount, int type, String salescase) {
        this.id = id;
        this.order_id = order_id;
        this.custmerAssestID = custmerAssestID;
        this.amount = amount;
        this.type = type;
        this.salescase = salescase;
    }

    private long order_id;
    private long custmerAssestID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getCustmerAssestID() {
        return custmerAssestID;
    }

    public void setCustmerAssestID(long custmerAssestID) {
        this.custmerAssestID = custmerAssestID;
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

    public String getSalescase() {
        return salescase;
    }

    public void setSalescase(String salescase) {
        this.salescase = salescase;
    }

    private double amount;
    private int type;
    private String salescase;


}
