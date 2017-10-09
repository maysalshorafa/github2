package com.pos.leaders.leaderspossystem.Models;

import android.graphics.Path;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by KARAM on 19/10/2016.
 */

public class Sale {
	private long id;
	private long byUser;
	private Date saleDate;
	private int replacementNote;
	private boolean cancelling;
	private double totalPrice;
	private double totalPaid;
	private long customer_id;

	@JsonIgnore
    private String customer_name;
	@JsonIgnore
	private List<Order> orders;
    @JsonIgnore
	private User user;
    @JsonIgnore
	private Payment payment;


    @JsonIgnore
    private Locale locale = new Locale("en");

	// region Constructors


    public Sale() {
    }

    public Sale(long id, long byUser, Date saleDate, int replacementNote, boolean cancelling, double totalPrice, double totalPaid, long customer_id, String customer_name) {
		this.id = id;
		this.byUser = byUser;
		this.saleDate=saleDate;
		this.replacementNote = replacementNote;
		this.cancelling = cancelling;
		this.totalPrice = totalPrice;
		this.totalPaid=totalPaid;
		this.customer_id=customer_id;
		this.customer_name=customer_name;
	}

	public Sale(long byUser, Date saleDate, int replacementNote, boolean cancelling, double totalPrice,double totalPaid) {
		this.byUser = byUser;
		this.saleDate=saleDate;
		this.replacementNote = replacementNote;
		this.cancelling = cancelling;
		this.totalPrice = totalPrice;
		this.totalPaid=totalPaid;
	}

	public Sale(long id, Date saleDate, int replacementNote, boolean cancelling, double totalPrice,double totalPaid,User user) {
		this.id = id;
		this.byUser = user.getId();
		this.saleDate=saleDate;
		this.replacementNote = replacementNote;
		this.cancelling = cancelling;
		this.totalPrice = totalPrice;
		this.totalPaid=totalPaid;

		this.user=user;
	}

	public Sale(Sale s) {
		this(s.getId(),s.getByUser(),s.getSaleDate(),s.getReplacementNote(),s.isCancelling(),s.getTotalPrice(),s.getTotalPaid(),s.getCustomer_id(),s.getCustomer_name());
	}

	public static Sale newInstance(Sale s){
		return new Sale(s.getId(),s.getByUser(),s.getSaleDate(),s.getReplacementNote(),s.isCancelling(),s.getTotalPrice(),s.getTotalPaid(),s.getCustomer_id(),s.getCustomer_name());
	}

	//endregion

	//region Getter

    public long getId() {
        return id;
    }

    public long getByUser() {
        return byUser;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public int getReplacementNote() {
        return replacementNote;
    }

    public boolean isCancelling() {
        return cancelling;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getTotalPaid() {
        return totalPaid;
    }
    public List<Order> getOrders() {
        return orders;
    }

    public User getUser() {
        return user;
    }

    public Payment getPayment() {
        return payment;
    }

    public Locale getLocale() {
        return locale;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }



    //endregion

	//region Setter


    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public void setCancelling(boolean cancelling) {
        this.cancelling = cancelling;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setByUser(long byUser) {
        this.byUser = byUser;
    }


    public void setReplacementNote(int replacementNote) {
        this.replacementNote = replacementNote;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    //endregion

	@Override
	public String toString() {
		return "Sale{" +
				"id=" + id +
				", byUser=" + byUser +
				", saleDate=" + saleDate +
				", replacementNote=" + replacementNote +
				", cancelling=" + cancelling +
				", totalPrice=" + totalPrice +
				", totalPaid=" + totalPaid +
				", user=" + user +
				'}';
	}

	public String BKMVDATA(int rowNumber, String pc) {

		String recordType = "320", OP = "+", mOP = "-";

		if (totalPrice < 0){
			recordType = "330";
			OP = "-";
			//totalPaid *= -1;
		}
		double totalPriceBeforeDiscount = 0;
		for (Order o : orders) {
			totalPriceBeforeDiscount += (o.getOriginal_price()*o.getCount());
		}
		if(totalPrice<0){
			totalPrice *= -1;

		}
		double totalSaved = totalPriceBeforeDiscount - totalPrice;
		if(totalSaved<0)
			totalSaved *= -1;
		//totalSaved = (totalSaved / (1 + SETTINGS.tax / 100));
		if(totalPaid < 0) {
			totalSaved = 0;
			totalPriceBeforeDiscount = totalPrice;
		}

		double noTax = totalPrice / (1 + (SETTINGS.tax / 100));
		if(noTax<0)
			noTax *= -1;
		double tax = totalPrice-noTax;
		String name = "";
		if(user.getFullName().length()>9)
			name = user.getFullName().substring(0, 9);
		else{
			name = String.format("%9s", user.getFullName());
		}


		return "C100" + String.format(locale, "%09d", rowNumber) + pc + recordType + String.format(locale, "%020d", id) + DateConverter.getYYYYMMDD(saleDate) + DateConverter.getHHMM(saleDate)
				+ String.format(locale, "%50s", "Customer") + Util.spaces(50) + Util.spaces(10) + Util.spaces(30) + Util.spaces(8) + Util.spaces(30) + Util.spaces(2) + Util.spaces(15) + Util.spaces(9)
				+ DateConverter.getYYYYMMDD(saleDate) + Util.spaces(15) + Util.spaces(3)
				+ OP + Util.x12V99(totalPriceBeforeDiscount/(1+(SETTINGS.tax/100)))
				+ mOP + Util.x12V99(((totalSaved)/(1+(SETTINGS.tax/100))))
				+ OP + Util.x12V99(noTax)
				+ OP + Util.x12V99(tax+0.004)
				+ OP + Util.x12V99(totalPrice)
				+ OP + String.format(locale, "%09.0f", 0.0f) + String.format(locale, "%02d", (int) ((0.0f - Math.floor(0.0f) + 0.001) * 100))
				+ Util.spaces(13) + "a0" + Util.spaces(8) + "b0" + "0" + DateConverter.getYYYYMMDD(saleDate) + Util.spaces(7) + name + String.format(locale, "%07d", id)
				+ Util.spaces(13);


	}
	static class test{
		public static void main1(String[] args){
			double num = 414.70;
			double tax = 17.0;

			double withouttax = num / (1 + (tax / 100));

			String str = String.format("%2.2f", withouttax - 0.005);
			System.out.println(str);


			String st = String.format("%2.2f", num - Double.parseDouble(str));
			System.out.println((num-withouttax)+"\n"+Util.x12V99(num-withouttax+0.004));
			//print(Double.parseDouble(st));
		}

	}
}
