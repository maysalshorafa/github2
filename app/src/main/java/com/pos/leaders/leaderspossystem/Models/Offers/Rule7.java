package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 7/31/2017.
 */

public class Rule7 {

    private   long id;
    private  double price;
    private  long product_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }



    public int getContain_club() {
        return contain_club;
    }

    public void setContain_club(int contain_club) {
        this.contain_club = contain_club;
    }

    private  int contain_club;
    public Rule7(long id, double price,  long product_id, int contain_club) {
        this.id = id;
        this.price = price;
        this.product_id = product_id;
        this.contain_club=contain_club;
    }

}