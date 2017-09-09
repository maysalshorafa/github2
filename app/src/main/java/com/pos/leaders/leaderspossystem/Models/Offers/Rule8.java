package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 8/2/2017.
 */

public class Rule8 {

    private long id;
    private double percent;
    private long productID;
    private int containClub;

    public Rule8(long id, double percent, long productID, int containClub) {
        this.id = id;
        this.percent = percent;
        this.productID = productID;
        this.containClub = containClub;
    }

    public Rule8() {
    }

    public long getId() {
        return id;
    }

    public double getPercent() {
        return percent;
    }

    public long getProductID() {
        return productID;
    }

    public int getContainClub() {
        return containClub;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPercent(double price) {
        this.percent = price;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public void setContainClub(int containClub) {
        this.containClub = containClub;
    }
}
