package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/24/2017.
 */

public class UsedPoint {
    public long usedPointId;
    public long saleId ;
    public int unUsed_point_amount;
    public long customer_id;

    public UsedPoint() {
    }

    public UsedPoint(long usedPointId, long saleId, int unUsed_point_amount, long customer_id) {
        this.usedPointId = usedPointId;
        this.saleId = saleId;
        this.unUsed_point_amount = unUsed_point_amount;
        this.customer_id = customer_id;
    }

    public long getUsedPointId() {

        return usedPointId;
    }

    public void setUsedPointId(long usedPointId) {
        this.usedPointId = usedPointId;
    }

    public long getSaleId() {
        return saleId;
    }

    public void setSaleId(long saleId) {
        this.saleId = saleId;
    }

    public int getUnUsed_point_amount() {
        return unUsed_point_amount;
    }

    public void setUnUsed_point_amount(int unUsed_point_amount) {
        this.unUsed_point_amount = unUsed_point_amount;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }
}
