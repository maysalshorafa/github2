package com.pos.leaders.leaderspossystem.Models.Offers;

import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Product;

import java.util.List;

/**
 * Created by KARAM on 01/08/2017.
 */

public class Rule2 extends Rule {
    //Buy x get the last item with p
    private int quantity;
    private float percent;

    public Rule2(){
        super(RULE2);
    }

    public Rule2(int quantity, float percent){
        super(RULE2);
        this.quantity = quantity;
        this.percent = percent;
    }

    public Rule2(int id, int quantity, float percent){
        super(id,RULE2);
        this.quantity = quantity;
        this.percent = percent;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPercent() {
        return percent;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    @Override
    public void execute(List<Order> orders, Offer offer) throws Exception{
        if(!precondition(orders))
            throw new Exception("cant execute this rule because the quantity is too small");
        double min = orders.get(0).getPrice();
        int indexOfMin = 0;
        for (int i=0;i<orders.size();i++) {
            if(orders.get(i).getPrice()<min){
                min = orders.get(i).getPrice();
                indexOfMin = i;
            }
        }
        orders.get(indexOfMin).setPrice(orders.get(indexOfMin).getPrice()*this.percent);
    }

    @Override
    public boolean precondition(List<Order> orders) {
        if(orders.size()>=quantity)
            return true;
        return false;
    }
}
