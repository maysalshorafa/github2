package com.pos.leaders.leaderspossystem.Models.Offers;

import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Order;

import java.util.List;

/**
 * Created by KARAM on 31/07/2017.
 */

public abstract class Rule {

    public static final String RULE1 = "RULE1";
    public static final String RULE2 = "RULE2";
    public static final String RULE3 = "RULE3";
    public static final String RULE4 = "RULE4";
    public static final String RULE5 = "RULE5";
    public static final String RULE6 = "RULE6";
    public static final String RULE7 = "RULE7";
    public static final String RULE8 = "RULE8";
    public static final String RULE9 = "RULE9";
    public static final String RULE10 = "RULE10";
    public static final String RULE11 = "RULE11";
    public static final String RULE12 = "RULE12";

    int id;
    String type;

    public Rule(int id,String type){
        this.id = id;
        this.type = type;
    }

    public Rule(String type){
        this.type = type;
    }

    public abstract void execute(List<Order> orders,Offer offer) throws Exception;

    public abstract boolean precondition(List<Order> orders);
}
