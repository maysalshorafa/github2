package com.pos.leaders.leaderspossystem.Models;

import android.content.Context;
import android.widget.Space;
import android.widget.Switch;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by KARAM on 19/10/2016.
 */

public class Payment {
	private long id;
	private String paymentWay;
	private long saleId;
	private double amount;

    private Locale locale = new Locale("en");

	// Constructors
	public Payment(long id, String paymentWay, double amount, long saleId) {
		this.id = id;
		this.paymentWay = paymentWay;
		this.amount = amount;
		this.saleId = saleId;
	}

	public Payment(Payment p) {
		this(p.getId(), p.getPaymentWay(), p.getAmount(), p.getSaleId());
	}

	// Getters
	public long getId() {
		return id;
	}

	public String getPaymentWay() {
		return paymentWay;
	}

	public long getSaleId() {
		return saleId;
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "Payment{" +
				"id=" + id +
				", paymentWay='" + paymentWay + '\'' +
				", amount='" + amount + '\'' +
				", saleId=" + saleId +
				'}';
	}

    public String BKMVDATA(int rowNumber, String companyID, Date date,Sale sale){
        String s = "320",OP="+";
        if(amount<0) {
            s = "330";
            OP = "-";
			//amount *= -1;
		}
        int totalItems = 0;
        double totalDiscount = 0;
        for(Order o: sale.getOrders())
        {
            totalItems += o.getCount();
            totalDiscount += (o.getItemTotalPrice() * o.getDiscount() / 100);
        }
        if(amount<0)
            totalDiscount = 0;
        totalItems = 1;
        double noTax = sale.getTotalPrice() / (1 + (SETTINGS.tax / 100));
        if(noTax<0)
            noTax *= -1;
        return "D110" + String.format(locale, "%09d", rowNumber) + companyID + s + String.format(locale, "%020d", saleId) + String.format(locale, "%04d", id) + s + String.format(locale, "%020d", saleId) + "3" + String.format(locale, "%020d", saleId) + String.format(locale, "%30s", "sale") + Util.spaces(50) + Util.spaces(30) +
                String.format(locale, "%20s", "unit")
                + "+" + String.format(locale, "%012d", totalItems) + String.format(locale, "%04d", (int) ((totalItems - Math.floor(totalItems) + 0.00001) * 10000))
                + OP + Util.x12V99(noTax)
                + OP + Util.x12V99(totalDiscount)
                + OP + Util.x12V99(noTax)
                + String.format(locale, "%02.0f", SETTINGS.tax) + String.format(locale, "%02d", (int) ((SETTINGS.tax - Math.floor(SETTINGS.tax) + 0.001) * 100))
                + Util.spaces(7) + DateConverter.getYYYYMMDD(date) + String.format(locale, "%07d", saleId) + Util.spaces(7) + Util.spaces(21);

    }
}
