package com.pos.leaders.leaderspossystem.Tools;

/**
 * Created by Win8.1 on 7/27/2017.
 */

public class Rule3 {
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

    public static double getParcent() {
    }
    
    public double getParcent() {
        return parcent;
    }

    public void setParcent(double parcent) {
        this.parcent = parcent;
    }

    private   int id ;
    private   int offer_id;
    private   double parcent;
    public Rule3(int offer_id,double parcent,int id) {
        this.id=id;
        this.offer_id=offer_id;
        this.parcent=parcent;
    }



}
