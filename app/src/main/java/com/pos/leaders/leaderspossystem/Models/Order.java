package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.Date;

/**
 * Created by KARAM on 19/10/2016.
 */

public class Order {
	private long id;
	private long productId;
	private int count;
	private double userOffer;
	private long saleId;

	private double original_price;
	private double price;
	private double discount;

	@JsonIgnore
	private Product product;

	public long getCustmerAssestId() {
		return custmerAssestId;
	}

	public void setCustmerAssestId(long custmerAssestId) {
		this.custmerAssestId = custmerAssestId;
	}

	private  long custmerAssestId;

	//region Constructors
	public Order(long id, long productId, int count, double userOffer, long saleId,long custmerAssestId) {
		this.id = id;
		this.productId = productId;
		this.count = count;
		this.userOffer = userOffer;
		this.saleId = saleId;
		this.custmerAssestId=custmerAssestId;
	}

    public Order(long id, long productId, int count, double userOffer, long saleId, double price, double original_price, double discount,long custmerAssestId) {
        this.id = id;
        this.productId = productId;
        this.count = count;
        this.userOffer = userOffer;
        this.saleId = saleId;
        this.price = price;
        this.original_price = original_price;
        this.discount = discount;
    	this.custmerAssestId=custmerAssestId;
    }

	public Order(long id, long productId, int count, double userOffer, long saleId, Product product,long custmerAssestId) {
		this.id = id;
		this.productId = productId;
		this.count = count;
		this.userOffer = userOffer;
		this.saleId = saleId;
		this.product = product;
		this.custmerAssestId=custmerAssestId;
	}
	public Order(long productId, int count, double userOffer, long saleId, Product product) {
		this.productId = productId;
		this.count = count;
		this.userOffer = userOffer;
		this.saleId = saleId;
		this.product = product;

	}
	public Order(int count, double userOffer, Product product) {
        this.count = count;
        this.userOffer = userOffer;
        this.product = product;
        this.productId = product.getId();
        this.price = product.getPrice();
        this.original_price = product.getPrice();
        this.discount = 0;
    }

    public Order(int count, double userOffer, Product product,double price, double original_price,double discount) {
        this.count = count;
        this.userOffer = userOffer;
        this.product = product;
        this.productId=product.getId();
        this.price = price;
        this.original_price = original_price;
        this.discount = discount;
    }

	public Order(Order o) {
        this(o.getId(), o.getProductId(), o.getCount(), o.getUserOffer(), o.getSaleId(), o.getPrice(), o.getOriginal_price(), o.getDiscount(),o.getCustmerAssestId());
    }

	public Order newInstance(Order o) {
		return new Order(o.getId(), o.getProductId(), o.getCount(), o.getUserOffer(), o.getSaleId(),o.getCustmerAssestId());
	}

	public Order() {
	}
	//endregion Constructors

    //region Getters

	public long getProductId() {
		return productId;
	}

	public long getId() {
		return id;
	}

	public int getCount() {
		return count;
	}

	public double getUserOffer() {
		return userOffer;
	}

	public long getSaleId() {
		return saleId;
	}

	public Product getProduct() {
		return product;
	}

	@JsonIgnore
	public double getItemTotalPrice() {

		return (count * (original_price * (1-(discount / 100))));
	}
	@JsonIgnore
	public double getOriginal_price() {
		return original_price;
	}

	public double getPrice() {
		return (original_price * (1-(discount / 100)));
	}

	public double getDiscount() {
		return discount;
	}

	//endregion Getters


	//region Setters

	public int setCount(int count) {
        if (count > 1)
            this.count = count;
        return this.count;
    }

	public void setProduct(Product p){
		this.product=p;
	}

	public void setDiscount(double discount){
		this.discount = discount;
	}

	public void setPrice(double price){
		this.price = (original_price * (discount / 100));
	}


	//endregion Setters

	//region Methods

	public int increaseCount(){
		this.count++;
		return this.count;
	}

	public int decreaseCount(){
		if(this.count>1) {
			this.count--;
		}
		return this.count;
	}

	public String DKMVDATA(int rowNumber,String companyID,Date date) {
        String s = "320", OP = "+", mOP = "-";
        double totalDiscount = (getItemTotalPrice() * discount / 100);
        double noTax = price / (1 + (SETTINGS.tax / 100));
        if(product.getName().length()>29){
            product.setName(product.getName().substring(0,29));
        }

        return "D110" + String.format(Util.locale, "%09d", rowNumber) + companyID + s + String.format(Util.locale, "%020d", saleId) + String.format(Util.locale, "%04d", id)
                + s + String.format(Util.locale, "%020d", saleId) + "3" + String.format(Util.locale, "%20s", productId) + String.format(Util.locale, "%30s", "sale")
                + Util.spaces(50) + String.format(Util.locale, "%30s", product.getBarCode()) + Util.spaces(20)
                + "+" + String.format(Util.locale, "%012d", count) + String.format(Util.locale, "%04d", (int) ((count - Math.floor(count) + 0.00001) * 10000))
                + OP + Util.x12V99(noTax) + mOP + Util.x12V99(totalDiscount) + OP + Util.x12V99((noTax-totalDiscount)*count)
                + String.format(Util.locale, "%02.0f", SETTINGS.tax) + String.format(Util.locale, "%02d", (int) ((SETTINGS.tax - Math.floor(SETTINGS.tax) + 0.001) * 100))
                + Util.spaces(7) + DateConverter.getYYYYMMDD(date) + String.format(Util.locale, "%07d", saleId) + Util.spaces(7) + Util.spaces(21);
    }

	//endregion Methods


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productId=" + productId +
                ", count=" + count +
                ", userOffer=" + userOffer +
                ", saleId=" + saleId +
                ", original_price=" + original_price +
                ", price=" + price +
                ", discount=" + discount +
                ", product=" + product +
                '}';
    }
}
