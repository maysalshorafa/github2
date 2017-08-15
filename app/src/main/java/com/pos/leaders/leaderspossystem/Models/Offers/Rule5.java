package com.pos.leaders.leaderspossystem.Models.Offers;

/**
 * Created by Win8.1 on 8/3/2017.
 */

public class Rule5 extends Rule {
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

    int id;
    int gift_id;

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

    int price;
    int productID;
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


}
