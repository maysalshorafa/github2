package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 7/27/2017.
 */

public class Rule3 {
    private long id;
    private double percent;
    private int contain;

    public Rule3(long id,double percent,int contain) {
        this.id = id;
        this.percent = percent;
        this.contain = contain;
    }

    public Rule3() {

    }

    public long getId() {
        return id;
    }

    public double getPercent() {
        return percent;
    }

    public int getContain() {
        return contain;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public void setContain(int contain) {
        this.contain = contain;
    }
}
