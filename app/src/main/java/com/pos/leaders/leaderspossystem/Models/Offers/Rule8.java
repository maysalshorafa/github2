package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 8/2/2017.
 */

public class Rule8 {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public double getParcent() {
        return Parcent;
    }

    public void setParcent(double price) {
        this.Parcent = price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    private   int id;
    private  double Parcent;
    private  int product_id;
    public Rule8(int id, double parcent,  int product_id) {
        this.id=id;
        this.Parcent = parcent;
        this.product_id = product_id;
    }

}
