package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/24/2017.
 */

public class UsedPoint {
    public long id;
    public long saleId ;
    public int unUsedpoint_amount;
    public long custmerId;

    public UsedPoint(long id, long saleId, int unUsedpoint_amount, long custmerId) {
        this.id = id;
        this.saleId = saleId;
        this.unUsedpoint_amount = unUsedpoint_amount;
        this.custmerId = custmerId;
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

    public int getUnUsedpoint_amount() {
        return unUsedpoint_amount;
    }

    public void setUnUsedpoint_amount(int unUsedpoint_amount) {
        this.unUsedpoint_amount = unUsedpoint_amount;
    }

    public long getCustmerId() {
        return custmerId;
    }

    public void setCustmerId(long custmerId) {
        this.custmerId = custmerId;
    }
}
