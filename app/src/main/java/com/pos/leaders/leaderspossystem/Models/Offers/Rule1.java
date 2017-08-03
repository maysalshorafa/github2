package com.pos.leaders.leaderspossystem.Models.Offers;

import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Order;

import java.util.List;

/**
 * Created by KARAM on 31/07/2017.
 */

public class Rule1 extends Rule {

    private int quantity;
    private double price;

    public Rule1(int id, int quantity, double price) {
        super(id, RULE1);
        this.quantity = quantity;
        this.price = price;
    }

    public Rule1(int quantity, double price) {
        super(RULE1);
        this.quantity = quantity;
        this.price = price;
    }

    public Rule1(){
        super(RULE1);
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void execute(List<Order> orders, Offer offer) {
        for (Order o : orders) {
            if (offer.getProducts().contains(o.getProduct())) {
                if (o.getCount() == this.quantity) {
                    o.setPrice(price / quantity);
                } else if (o.getCount() > this.quantity) {
                    Order _o = o;
                    _o.setCount(o.getCount() - quantity);
                    orders.add(_o);
                    o.setCount(quantity);
                    o.setPrice(price / quantity);
                }
                break;
            }
        }
    }

    @Override
    public boolean precondition(List<Order> orders) {
        if(orders.size()>=quantity)
            return true;
        return false;
    }
}
