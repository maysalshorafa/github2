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

    public int getContain_club() {
        return contain_club;
    }

    public void setContain_club(int contain_club) {
        this.contain_club = contain_club;
    }

    private  int contain_club;
    public Rule7(int id, double price,  int product_id, int contain_club) {
        this.id = id;
        this.price = price;
        this.product_id = product_id;
        this.contain_club=contain_club;
    }

}