package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 7/27/2017.
 */

public class Rule3 {
    private   int id ;
    private   double parcent;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public double getParcent() {
        return parcent;
    }

    public void setParcent(double parcent) {
        this.parcent = parcent;
    }


    public Rule3(double parcent) {
        this.parcent=parcent;
    }



}
