package com.pos.leaders.leaderspossystem.Models;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.Date;

/**
 * Created by KARAM on 19/10/2016.
 */

public class OrderDetails {
	private long orderDetailsId;
	private long productId;
	private int quantity;
	private double userOffer;
	private long orderId;

	private double unitPrice;
	private double paidAmount;
	private double discount;

	@JsonIgnore
	public double rowDiscount = 0;

	@JsonIgnore
	private Product product;

	@JsonIgnore
	public Offer offer;

	public long getCustomer_assistance_id() {
		return customer_assistance_id;
	}

	public void setCustomer_assistance_id(long customer_assistance_id) {
		this.customer_assistance_id = customer_assistance_id;
	}

	private  long customer_assistance_id;

	//region Constructors
	public OrderDetails(long orderDetailsId, long productId, int quantity, double userOffer, long orderId, long customer_assistance_id) {
		this.orderDetailsId = orderDetailsId;
		this.productId = productId;
		this.quantity = quantity;
		this.userOffer = userOffer;
		this.orderId = orderId;
		this.customer_assistance_id = customer_assistance_id;
	}

    public OrderDetails(long orderDetailsId, long productId, int quantity, double userOffer, long orderId, double paidAmount, double original_price, double discount, long customer_assistance_id) {
        this.orderDetailsId = orderDetailsId;
        this.productId = productId;
        this.quantity = quantity;
        this.userOffer = userOffer;
        this.orderId = orderId;
        this.paidAmount = paidAmount;
        this.unitPrice = original_price;
        this.discount = discount;
    	this.customer_assistance_id = customer_assistance_id;
    }

	public OrderDetails(long orderDetailsId, long productId, int quantity, double userOffer, long orderId, Product product, long customer_assistance_id) {
		this.orderDetailsId = orderDetailsId;
		this.productId = productId;
		this.quantity = quantity;
		this.userOffer = userOffer;
		this.orderId = orderId;
		this.product = product;
		this.customer_assistance_id = customer_assistance_id;
	}
	public OrderDetails(long productId, int quantity, double userOffer, long orderId, Product product) {
		this.productId = productId;
		this.quantity = quantity;
		this.userOffer = userOffer;
		this.orderId = orderId;
		this.product = product;

	}
	public OrderDetails(int quantity, double userOffer, Product product) {
        this.quantity = quantity;
        this.userOffer = userOffer;
        this.product = product;
        this.productId = product.getProductId();
        this.paidAmount = product.getPrice();
        this.unitPrice = product.getPrice();
        this.discount = 0;
    }

    public OrderDetails(int quantity, double userOffer, Product product, double paidAmount, double original_price, double discount) {
        this.quantity = quantity;
        this.userOffer = userOffer;
        this.product = product;
        this.productId =product.getProductId();
        this.paidAmount = paidAmount;
        this.unitPrice = original_price;
        this.discount = discount;
    }

	public OrderDetails(OrderDetails o) {
        this(o.getOrderDetailsId(), o.getProductId(), o.getQuantity(), o.getUserOffer(), o.getOrderId(), o.getPaidAmount(), o.getUnitPrice(), o.getDiscount(),o.getCustomer_assistance_id());
    }

	public OrderDetails newInstance(OrderDetails o) {
		return new OrderDetails(o.getOrderDetailsId(), o.getProductId(), o.getQuantity(), o.getUserOffer(), o.getOrderId(),o.getCustomer_assistance_id());
	}

	public OrderDetails() {
	}
	//endregion Constructors

    //region Getters

	public long getProductId() {
		return productId;
	}

	public long getOrderDetailsId() {
		return orderDetailsId;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getUserOffer() {
		return userOffer;
	}

	public long getOrderId() {
		return orderId;
	}

	public Product getProduct() {
		return product;
	}

	@JsonIgnore
	public double getItemTotalPrice() {
		double tempPrice = (quantity * (unitPrice * (1 - (discount / 100))));
		Log.e("log orde", "tempPrice: " + tempPrice);
		Log.e("log orde", "unitPrice: " + unitPrice);
		Log.e("log orde", "discount: " + discount);
		Log.e("log orde", "quantity: " + quantity);

		//there is no discount for this row
		if(rowDiscount==0) return tempPrice;
		Log.e("log orde", "calculate new price: " + (tempPrice - (tempPrice * (rowDiscount / 100))));
		Log.e("log orde", "rowDiscount: " + rowDiscount);

		//calculate the price with row discount after the offer discount
		return (tempPrice - (tempPrice * (rowDiscount / 100)));
	}
	@JsonIgnore
	public double getUnitPrice() {
		return unitPrice;
	}

	public double getPaidAmount() {
		return (unitPrice * (1-(discount / 100)));
	}

	public double getDiscount() {
		return discount;
	}

	//endregion Getters


	//region Setters

	public int setCount(int count) {
        if (count >=1)
            this.quantity = count;
        return this.quantity;
    }

	public void setProduct(Product p){
		this.product=p;
	}

	public void setDiscount(double discount){
		this.discount = discount;
	}

	public void setPaidAmount(double paidAmount){
		this.paidAmount = (unitPrice * (discount / 100));
	}


	//endregion Setters

	//region Methods

	public int increaseCount(){
		this.quantity++;
		return this.quantity;
	}

	public int decreaseCount(){
		if(this.quantity >1) {
			this.quantity--;
		}
		return this.quantity;
	}

	public String DKMVDATA(int rowNumber,String companyID,Date date) {
        String s = "320", OP = "+", mOP = "-";
        double totalDiscount = (getItemTotalPrice() * discount / 100);
        double noTax = paidAmount / (1 + (SETTINGS.tax / 100));
        if(product.getDisplayName().length()>29){
            product.setName(product.getDisplayName().substring(0,29));
        }

        return "D110" + String.format(Util.locale, "%09d", rowNumber) + companyID + s + String.format(Util.locale, "%020d", orderId) + String.format(Util.locale, "%04d", orderDetailsId)
                + s + String.format(Util.locale, "%020d", orderId) + "3" + String.format(Util.locale, "%20s", productId) + String.format(Util.locale, "%30s", "sale")
                + Util.spaces(50) + String.format(Util.locale, "%30s", product.getSku()) + Util.spaces(20)
                + "+" + String.format(Util.locale, "%012d", quantity) + String.format(Util.locale, "%04d", (int) ((quantity - Math.floor(quantity) + 0.00001) * 10000))
                + OP + Util.x12V99(noTax) + mOP + Util.x12V99(totalDiscount) + OP + Util.x12V99((noTax-totalDiscount)* quantity)
                + String.format(Util.locale, "%02.0f", SETTINGS.tax) + String.format(Util.locale, "%02d", (int) ((SETTINGS.tax - Math.floor(SETTINGS.tax) + 0.001) * 100))
                + Util.spaces(7) + DateConverter.getYYYYMMDD(date) + String.format(Util.locale, "%07d", orderId) + Util.spaces(7) + Util.spaces(21);
    }

	//endregion Methods


    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderDetailsId=" + orderDetailsId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", userOffer=" + userOffer +
                ", orderId=" + orderId +
                ", unitPrice=" + unitPrice +
                ", paidAmount=" + paidAmount +
                ", discount=" + discount +
                ", product=" + product +
                '}';
    }

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
}
