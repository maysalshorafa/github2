package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/24/2017.
 */

public class UsedPoint {
    public long id;
    public int unUsed_point_amount;
    public long customerId;

    public UsedPoint() {
    }

    public UsedPoint(long id, int unUsed_point_amount, long customerId) {
        this.id = id;
        this.unUsed_point_amount = unUsed_point_amount;
        this.customerId = customerId;
    }

    public UsedPoint( int unUsed_point_amount, long customerId) {
        this.id = id;
        this.unUsed_point_amount = unUsed_point_amount;
        this.customerId = customerId;
    }

    public int getUnUsed_point_amount() {
        return unUsed_point_amount;
    }

    public void setUnUsed_point_amount(int unUsed_point_amount) {
        this.unUsed_point_amount = unUsed_point_amount;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UsedPoint{" +
                "id=" + id +
                ", unUsed_point_amount=" + unUsed_point_amount +
                ", customerId=" + customerId +
                '}';
    }
}
