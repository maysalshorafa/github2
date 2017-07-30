package com.pos.leaders.leaderspossystem.Models;

import android.content.Context;

import com.pos.leaders.leaderspossystem.MainActivity;

/**
 * Created by KARAM on 19/10/2016.
 */

public class OfferRule {
    public OfferRule(Context context) {
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    private int id;
    private int  rule;



    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    private   int product_id;

    // Constructor
    public OfferRule(int id, int rule,int product_id) {
        this.id = id;
       this.rule = rule;
        this.product_id=product_id;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getRule() {
        return rule;
    }
    public int getProduct_id() {
        return product_id;
    }

}