package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 6/21/2020.
 */

public class ReciptObject {
    boolean isAll;
    boolean isPartially;
    double allAmount;
    double partialAmount;

    public ReciptObject() {
    }

    public ReciptObject(boolean isAll, boolean isPartially, double allAmount, double partialAmount) {
        this.isAll = isAll;
        this.isPartially = isPartially;
        this.allAmount = allAmount;
        this.partialAmount = partialAmount;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public boolean isPartially() {
        return isPartially;
    }

    public void setPartially(boolean partially) {
        isPartially = partially;
    }

    public double getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(double allAmount) {
        this.allAmount = allAmount;
    }

    public double getPartialAmount() {
        return partialAmount;
    }

    public void setPartialAmount(double partialAmount) {
        this.partialAmount = partialAmount;
    }

    @Override
    public String toString() {
        return "ReciptObject{" +
                "isAll=" + isAll +
                ", isPartially=" + isPartially +
                ", allAmount=" + allAmount +
                ", partialAmount=" + partialAmount +
                '}';
    }
}
