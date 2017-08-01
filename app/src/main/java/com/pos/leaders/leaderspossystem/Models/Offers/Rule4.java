package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by KARAM on 01/08/2017.
 */

public class Rule4 extends Rule {

    private int quantity;
    private double discountPrice;

    public Rule4(){
        super(RULE4);
    }

    public Rule4(int quantity, double discountPrice){
        super(RULE4);
        this.quantity = quantity;
        this.discountPrice = discountPrice;
    }

    public Rule4(int id, int quantity, double discountPrice){
        super(id, RULE4);
        this.quantity = quantity;
        this.discountPrice = discountPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }
}
