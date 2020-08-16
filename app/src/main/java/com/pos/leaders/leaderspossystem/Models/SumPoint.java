package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/24/2017.
 */

public class SumPoint {
    private long sumPointId;
    private long orderId;
    private int pointAmount;
    private long customerId;
    private int totalPoint;


    public SumPoint() {
    }

    public SumPoint(long sumPointId, long orderId, int pointAmount, long customerId,int totalPoint) {
        this.sumPointId = sumPointId;
        this.orderId = orderId;
        this.pointAmount = pointAmount;
        this.customerId = customerId;
        this.totalPoint=totalPoint;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
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

    @Override
    public String toString() {
        return "SumPoint{" +
                "sumPointId=" + sumPointId +
                ", orderId=" + orderId +
                ", pointAmount=" + pointAmount +
                ", customerId=" + customerId +
                ", totalPoint=" + totalPoint +
                '}';
    }
}
