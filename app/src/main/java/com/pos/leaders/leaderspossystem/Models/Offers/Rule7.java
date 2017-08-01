package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 7/31/2017.
 */

public class Rule7 {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    private   int id;
    private  int offer_id;
    private  double price;
    private  int product_id;
    public Rule7(int id, double price, int offer_id, int product_id) {
        this.id = id;
        this.offer_id = offer_id;
        this.price = price;
        this.product_id = product_id;
    }

}