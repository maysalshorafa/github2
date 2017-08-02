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
    private  double price;
    private  int product_id;
    public Rule7(int id, double price,  int product_id) {
        this.id = id;
        this.price = price;
        this.product_id = product_id;
    }

}