package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/24/2017.
 */

public class SumPoint {
    private long sumPointId;
    private long orderId;
    private int pointAmount;
    private long customerId;


    public SumPoint() {
    }

    public SumPoint(long sumPointId, long orderId, int pointAmount, long customerId) {
        this.sumPointId = sumPointId;
        this.orderId = orderId;
        this.pointAmount = pointAmount;
        this.customerId = customerId;
    }

    public long getSumPointId() {
        return sumPointId;
    }

    public void setSumPointId(long sumPointId) {
        this.sumPointId = sumPointId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(int pointAmount) {
        this.pointAmount = pointAmount;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
