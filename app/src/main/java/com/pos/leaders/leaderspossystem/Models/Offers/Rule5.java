package com.pos.leaders.leaderspossystem.Models.Offers;

import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Order;

import java.util.List;

/**
 * Created by Win8.1 on 8/3/2017.
 */

public class Rule5 extends Rule {

    private int id;
    private int gift_id;
    private int price;
    private int productID;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGift_id() {
        return gift_id;
    }

    public void setGift_id(int gift_id) {
        this.gift_id = gift_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public Rule5(int id, int gift_id,int productID ,int price ) {
        super(id, RULE5);
        this.id=id;
        this.gift_id=gift_id;
        this.price=price;
        this.productID=productID;
    }

    public Rule5(){
        super(RULE5);
    }

    @Override
    public void execute(List<Order> orders, Offer offer) throws Exception {

    }
    @Override
    public boolean precondition(List<Order> orders) {
        return false;
    }
}
