package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 7/27/2017.
 */

public class Rule3 {
    private   int id ;
    private   double parcent;
    private  int contain;




    int club_contain;
    public int getContain() {
        return contain;
    }

    public void setContain(int contain) {
        this.contain = contain;
    }

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


    public Rule3(int id,double parcent,int contain) {
        this.id=id;
        this.parcent=parcent;
        this.contain=contain;
    }



}
