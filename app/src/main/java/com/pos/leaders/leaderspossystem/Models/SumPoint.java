package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/24/2017.
 */

public class SumPoint {
    private long id;
    private long saleId;
    private int pointAmount;
    private long customer_id;

    public SumPoint(long id, long saleId, int pointAmount, long customer_id) {
        this.id = id;
        this.saleId = saleId;
        this.pointAmount = pointAmount;
        this.customer_id = customer_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSaleId() {
        return saleId;
    }

    public void setSaleId(long saleId) {
        this.saleId = saleId;
    }

    public int getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(int pointAmount) {
        this.pointAmount = pointAmount;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }
}