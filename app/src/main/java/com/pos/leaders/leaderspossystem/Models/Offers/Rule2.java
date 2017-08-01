package com.pos.leaders.leaderspossystem.Models.Offers;

import com.pos.leaders.leaderspossystem.Models.Product;

/**
 * Created by KARAM on 01/08/2017.
 */

public class Rule2 extends Rule {
    //Buy x get the last item with p
    private int count;
    private float percent;

    public Rule2(){
        super(RULE2);
    }

    public Rule2(int count, float percent){
        super(RULE2);
        this.count = count;
        this.percent = percent;
    }

    public Rule2(int id, int count, float percent){
        super(id,RULE2);
        this.count = count;
        this.percent = percent;
    }

    public int getCount() {
        return count;
    }

    public float getPercent() {
        return percent;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
