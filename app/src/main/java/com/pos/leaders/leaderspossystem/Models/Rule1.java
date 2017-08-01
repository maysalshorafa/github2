package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by KARAM on 31/07/2017.
 */

public class Rule1 extends Rule {

    int count;
    double price;

    public Rule1(int id, int count, double price) {
        super(id, RULE1);
        this.count = count;
        this.price = price;
    }

    public Rule1(){
        super(RULE1);
    }

}
