package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 8/2/2017.
 */

public class Rule8 {

    private long id;
    private double Parcent;
    private long product_id;
    private int contain_club;

    public Rule8(long id, double parcent,  long product_id, int contain_club) {
        this.id=id;
        this.Parcent = parcent;
        this.product_id = product_id;
        this.contain_club=contain_club;
    }

    public long getId() {
        return id;
    }

    public double getParcent() {
        return Parcent;
    }

    public long getProduct_id() {
        return product_id;
    }

    public int getContain_club() {
        return contain_club;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setParcent(double price) {
        this.Parcent = price;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public void setContain_club(int contain_club) {
        this.contain_club = contain_club;
    }
}
