package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/24/2017.
 */

public class UsedPoint {
    public long usedPointId;
    public long orderId ;
    public int unUsed_point_amount;
    public long customerId;

    public UsedPoint() {
    }

    public UsedPoint(long usedPointId, long orderId, int unUsed_point_amount, long customerId) {
        this.usedPointId = usedPointId;
        this.orderId = orderId;
        this.unUsed_point_amount = unUsed_point_amount;
        this.customerId = customerId;
    }

    public long getUsedPointId() {

        return usedPointId;
    }

    public void setUsedPointId(long usedPointId) {
        this.usedPointId = usedPointId;
    }

    public long getSaleId() {
        return orderId;
    }

    public void setSaleId(long saleId) {
        this.orderId = saleId;
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
}
