package com.pos.leaders.leaderspossystem.Tools;

/**
 * Created by Win8.1 on 7/27/2017.
 */

public class Rule3 {
    public static int id ;
    public static int offer_id;
    public static  double parcent;
    public Rule3(int offer_id,double parcent,int id) {
        this.id=id;
        this.offer_id=offer_id;
        this.parcent=parcent;
    }


    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Rule3.id = id;
    }

    public static int getOffer_id() {
        return offer_id;
    }

    public static void setOffer_id(int offer_id) {
        Rule3.offer_id = offer_id;
    }



    public static double getParcent() {
        return parcent;
    }

    public static void setParcent(int parcent) {
        Rule3.parcent = parcent;
    }


}
