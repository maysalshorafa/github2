package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.Date;

/**
 * Created by KARAM on 19/10/2016.
 */

public class OrderDetails {
	private long id;
	private long product_id;
	private int quantity;
	private double userOffer;
	private long order_id;

	private double unit_price;
	private double paid_amount;
	private double discount;

	@JsonIgnore
	private Product product;

	public long getCustomer_assistance_id() {
		return customer_assistance_id;
	}

	public void setCustomer_assistance_id(long customer_assistance_id) {
		this.customer_assistance_id = customer_assistance_id;
	}

	private  long customer_assistance_id;

	//region Constructors
	public OrderDetails(long id, long product_id, int quantity, double userOffer, long order_id, long customer_assistance_id) {
		this.id = id;
		this.product_id = product_id;
		this.quantity = quantity;
		this.userOffer = userOffer;
		this.order_id = order_id;
		this.customer_assistance_id = customer_assistance_id;
	}

    public OrderDetails(long id, long product_id, int quantity, double userOffer, long order_id, double paid_amount, double original_price, double discount, long customer_assistance_id) {
        this.id = id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.userOffer = userOffer;
        this.order_id = order_id;
        this.paid_amount = paid_amount;
        this.unit_price = original_price;
        this.discount = discount;
    	this.customer_assistance_id = customer_assistance_id;
    }

	public OrderDetails(long id, long product_id, int quantity, double userOffer, long order_id, Product product, long customer_assistance_id) {
		this.id = id;
		this.product_id = product_id;
		this.quantity = quantity;
		this.userOffer = userOffer;
		this.order_id = order_id;
		this.product = product;
		this.customer_assistance_id = customer_assistance_id;
	}
	public OrderDetails(long product_id, int quantity, double userOffer, long order_id, Product product) {
		this.product_id = product_id;
		this.quantity = quantity;
		this.userOffer = userOffer;
		this.order_id = order_id;
		this.product = product;

	}
	public OrderDetails(int quantity, double userOffer, Product product) {
        this.quantity = quantity;
        this.userOffer = userOffer;
        this.product = product;
        this.product_id = product.getId();
        this.paid_amount = product.getPrice();
        this.unit_price = product.getPrice();
        this.discount = 0;
    }

    public OrderDetails(int quantity, double userOffer, Product product, double paid_amount, double original_price, double discount) {
        this.quantity = quantity;
        this.userOffer = userOffer;
        this.product = product;
        this.product_id =product.getId();
        this.paid_amount = paid_amount;
        this.unit_price = original_price;
        this.discount = discount;
    }

	public OrderDetails(OrderDetails o) {
        this(o.getId(), o.getProduct_id(), o.getQuantity(), o.getUserOffer(), o.getOrder_id(), o.getPaid_amount(), o.getUnit_price(), o.getDiscount(),o.getCustomer_assistance_id());
    }

	public OrderDetails newInstance(OrderDetails o) {
		return new OrderDetails(o.getId(), o.getProduct_id(), o.getQuantity(), o.getUserOffer(), o.getOrder_id(),o.getCustomer_assistance_id());
	}

	public OrderDetails() {
	}
	//endregion Constructors

    //region Getters

	public long getProduct_id() {
		return product_id;
	}

	public long getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getUserOffer() {
		return userOffer;
	}

	public long getOrder_id() {
		return order_id;
	}

	public Product getProduct() {
		return product;
	}

	@JsonIgnore
	public double getItemTotalPrice() {

		return (quantity * (unit_price * (1-(discount / 100))));
	}
	@JsonIgnore
	public double getUnit_price() {
		return unit_price;
	}

	public double getPaid_amount() {
		return (unit_price * (1-(discount / 100)));
	}

	public double getDiscount() {
		return discount;
	}

	//endregion Getters


	//region Setters

	public int setCount(int count) {
        if (count > 1)
            this.quantity = count;
        return this.quantity;
    }

	public void setProduct(Product p){
		this.product=p;
	}

	public void setDiscount(double discount){
		this.discount = discount;
	}

	public void setPaid_amount(double paid_amount){
		this.paid_amount = (unit_price * (discount / 100));
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
        double noTax = paid_amount / (1 + (SETTINGS.tax / 100));
        if(product.getName().length()>29){
            product.setName(product.getName().substring(0,29));
        }

        return "D110" + String.format(Util.locale, "%09d", rowNumber) + companyID + s + String.format(Util.locale, "%020d", order_id) + String.format(Util.locale, "%04d", id)
                + s + String.format(Util.locale, "%020d", order_id) + "3" + String.format(Util.locale, "%20s", product_id) + String.format(Util.locale, "%30s", "sale")
                + Util.spaces(50) + String.format(Util.locale, "%30s", product.getBarCode()) + Util.spaces(20)
                + "+" + String.format(Util.locale, "%012d", quantity) + String.format(Util.locale, "%04d", (int) ((quantity - Math.floor(quantity) + 0.00001) * 10000))
                + OP + Util.x12V99(noTax) + mOP + Util.x12V99(totalDiscount) + OP + Util.x12V99((noTax-totalDiscount)* quantity)
                + String.format(Util.locale, "%02.0f", SETTINGS.tax) + String.format(Util.locale, "%02d", (int) ((SETTINGS.tax - Math.floor(SETTINGS.tax) + 0.001) * 100))
                + Util.spaces(7) + DateConverter.getYYYYMMDD(date) + String.format(Util.locale, "%07d", order_id) + Util.spaces(7) + Util.spaces(21);
    }

	//endregion Methods


    @Override
    public String toString() {
        return "OrderDetails{" +
                "id=" + id +
                ", product_id=" + product_id +
                ", quantity=" + quantity +
                ", userOffer=" + userOffer +
                ", order_id=" + order_id +
                ", unit_price=" + unit_price +
                ", paid_amount=" + paid_amount +
                ", discount=" + discount +
                ", product=" + product +
                '}';
    }
}
