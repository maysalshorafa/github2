package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by KARAM on 19/10/2016.
 */

public class Payment {
	private long paymentId;
	private String paymentWay;
	private long orderId;
	private double amount;

	@JsonIgnore
    private Locale locale = new Locale("en");
	@JsonIgnore
	private List<CashPayment> cashPayments = new ArrayList<>();
	@JsonIgnore
	private List<CurrencyReturns> currencyReturns = new ArrayList<>();

	// Constructors
	public Payment(long paymentId, String paymentWay, double amount, long orderId) {
		this.paymentId = paymentId;
		this.paymentWay = paymentWay;
		this.amount = amount;
		this.orderId = orderId;
	}

	public Payment(Payment p) {
		this(p.getPaymentId(), p.getPaymentWay(), p.getAmount(), p.getOrderId());
	}

	public Payment() {
	}

	//region Getters
	public long getPaymentId() {
		return paymentId;
	}

	public String getPaymentWay() {
		return paymentWay;
	}

	public long getOrderId() {
		return orderId;
	}

	public double getAmount() {
		return amount;
	}

	@JsonIgnore
	public List<CashPayment> getCashPayments() {
		return cashPayments;
	}
    @JsonIgnore
	public List<CurrencyReturns> getCurrencyReturns() {
		return currencyReturns;
	}




	//endregion

	//region Setters

	@JsonIgnore
	public void setCashPayments(List<CashPayment> cashPayments) {
		this.cashPayments = cashPayments;
	}

	@JsonIgnore
	public void setCurrencyReturns(List<CurrencyReturns> currencyReturns) {
		this.currencyReturns = currencyReturns;
	}



	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	public void setPaymentWay(String paymentWay) {
		this.paymentWay = paymentWay;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	//endregion Setters


	@Override
	public String toString() {
		return "Payment{" +
				"accountingId=" + paymentId +
				", paymentWay='" + paymentWay + '\'' +
				", amount='" + amount + '\'' +
				", orderId=" + orderId +
				'}';
	}

    public String BKMVDATA(int rowNumber, String companyID, Date date,Order sale){
        String s = "320",OP="+";
        if(amount<0) {
            s = "330";
            OP = "-";
			//amount *= -1;
		}
        int totalItems = 0;
        double totalDiscount = 0;
        for(OrderDetails o: sale.getOrders())
        {
            totalItems += o.getQuantity();
            totalDiscount += (o.getItemTotalPrice() * o.getDiscount() / 100);
        }
        if(amount<0)
            totalDiscount = 0;
        totalItems = 1;
        double noTax = sale.getTotalPrice() / (1 + (SETTINGS.tax / 100));
        if(noTax<0)
            noTax *= -1;
        return "D110" + String.format(locale, "%09d", rowNumber) + companyID + s + String.format(locale, "%020d", orderId) + String.format(locale, "%04d", paymentId) + s + String.format(locale, "%020d", orderId) + "3" + String.format(locale, "%020d", orderId) + String.format(locale, "%30s", "sale") + Util.spaces(50) + Util.spaces(30) +
                String.format(locale, "%20s", "unit")
                + "+" + String.format(locale, "%012d", totalItems) + String.format(locale, "%04d", (int) ((totalItems - Math.floor(totalItems) + 0.00001) * 10000))
                + OP + Util.x12V99(noTax)
                + OP + Util.x12V99(totalDiscount)
                + OP + Util.x12V99(noTax)
                + String.format(locale, "%02.0f", SETTINGS.tax) + String.format(locale, "%02d", (int) ((SETTINGS.tax - Math.floor(SETTINGS.tax) + 0.001) * 100))
                + Util.spaces(7) + DateConverter.getYYYYMMDD(date) + String.format(locale, "%07d", orderId) + Util.spaces(7) + Util.spaces(21);

    }
}
