package com.pos.leaders.leaderspossystem.Models.Offers;

import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Order;

import java.util.List;

/**
 * Created by Win8.1 on 8/3/2017.
 */

public class Rule5 extends Rule {

    private long id;
    private long gift_id;
    private int price;
    private long productID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGift_id() {
        return gift_id;
    }

    public void setGift_id(long gift_id) {
        this.gift_id = gift_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public Rule5(long id, long gift_id,long productID ,int price ) {
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
