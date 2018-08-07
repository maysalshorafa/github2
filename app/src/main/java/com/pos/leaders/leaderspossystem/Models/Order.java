package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

/**
 * Created by KARAM on 19/10/2016.
 */
//// TODO: 20/06/2018 Optimize: remove cusomerName

public class Order {
	private long orderId;
	private long byUser;
	private Timestamp createdAt;
	private int replacementNote;
	private boolean status;
	private double totalPrice;
	private double totalPaidAmount;
	private long customerId;


	@JsonIgnore
	public double cartDiscount = 0;
	@JsonIgnore
	private String customer_name = null;
	@JsonIgnore
	private List<OrderDetails> orders;
	@JsonIgnore
	private Employee user;
	@JsonIgnore
	private Payment payment;
	@JsonIgnore
	private Customer customer;

	@JsonIgnore
	private Locale locale = new Locale("en");

	// region Constructors


	public Order() {
	}

	public Order(long orderId, long byUser, Timestamp createdAt, int replacementNote, boolean status, double totalPrice, double totalPaidAmount, long customerId, String customer_name) {
		this.orderId = orderId;
		this.byUser = byUser;
		this.createdAt = createdAt;
		this.replacementNote = replacementNote;
		this.status = status;
		this.totalPrice = totalPrice;
		this.totalPaidAmount = totalPaidAmount;
		this.customerId = customerId;
		this.customer_name = customer_name;
	}

	public Order(long orderId, long byUser, Timestamp createdAt, int replacementNote, boolean status, double totalPrice, double totalPaidAmount, long customerId, String customer_name, Customer customer) {
		this.orderId = orderId;
		this.byUser = byUser;
		this.createdAt = createdAt;
		this.replacementNote = replacementNote;
		this.status = status;
		this.totalPrice = totalPrice;
		this.totalPaidAmount = totalPaidAmount;
		this.customerId = customerId;
		this.customer_name = customer_name;
		this.customer = customer;
	}

	public Order(long byUser, Timestamp createdAt, int replacementNote, boolean status, double totalPrice, double totalPaidAmount) {
		this.byUser = byUser;
		this.createdAt = createdAt;
		this.replacementNote = replacementNote;
		this.status = status;
		this.totalPrice = totalPrice;
		this.totalPaidAmount = totalPaidAmount;
	}

	public Order(long orderId, Timestamp createdAt, int replacementNote, boolean status, double totalPrice, double totalPaidAmount, Employee user) {
		this.orderId = orderId;
		this.byUser = user.getEmployeeId();
		this.createdAt = createdAt;
		this.replacementNote = replacementNote;
		this.status = status;
		this.totalPrice = totalPrice;
		this.totalPaidAmount = totalPaidAmount;

		this.user = user;
	}

	public Order(Order s) {
		this(s.getOrderId(), s.getByUser(), s.getCreatedAt(), s.getReplacementNote(), s.isStatus(), s.getTotalPrice(), s.getTotalPaidAmount(), s.getCustomerId(), s.getCustomer_name(), s.getCustomer());
	}

	public static Order newInstance(Order s) {
		return new Order(s.getOrderId(), s.getByUser(), s.getCreatedAt(), s.getReplacementNote(), s.isStatus(), s.getTotalPrice(), s.getTotalPaidAmount(), s.getCustomerId(), s.getCustomer_name());
	}

	//endregion

	//region Getter

	public long getOrderId() {
		return orderId;
	}

	public long getByUser() {
		return byUser;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public int getReplacementNote() {
		return replacementNote;
	}

	public boolean isStatus() {
		return status;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public double getTotalPaidAmount() {
		return totalPaidAmount;
	}

	public List<OrderDetails> getOrders() {
		return orders;
	}

	public Employee getUser() {
		return user;
	}

	public Payment getPayment() {
		return payment;
	}

	public Locale getLocale() {
		return locale;
	}

	public long getCustomerId() {
		return customerId;
	}

	public String getCustomer_name() {
		return customer_name;
	}


	//endregion

	//region Setter


	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public void setByUser(long byUser) {
		this.byUser = byUser;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setReplacementNote(int replacementNote) {
		this.replacementNote = replacementNote;
	}


	public void setTotalPaidAmount(double total_paid_amount) {
		this.totalPaidAmount = total_paid_amount;
	}

	public void setOrders(List<OrderDetails> orders) {
		this.orders = orders;
	}

	public void setUser(Employee user) {
		this.user = user;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	//endregion

	@Override
	public String toString() {
		return "ORDER_DETAILS{" +
				"orderId=" + orderId +
				", byUser=" + byUser +
				", order_date=" + createdAt +
				", replacementNote=" + replacementNote +
				", status=" + status +
				", total_price=" + totalPrice +
				", total_paid_amount=" + totalPaidAmount +
				", user=" + user +
				'}';
	}

	public String BKMVDATA(int rowNumber, String pc) {

		String recordType = "320", OP = "+", mOP = "-";

		if (totalPrice < 0) {
			recordType = "330";
			OP = "-";
			//total_paid_amount *= -1;
		}
		double totalPriceBeforeDiscount = 0;
		for (OrderDetails o : orders) {
			totalPriceBeforeDiscount += (o.getUnitPrice() * o.getQuantity());
		}
		if (totalPrice < 0) {
			totalPrice *= -1;

		}
		double totalSaved = totalPriceBeforeDiscount - totalPrice;
		if (totalSaved < 0)
			totalSaved *= -1;
		//totalSaved = (totalSaved / (1 + SETTINGS.tax / 100));
		if (totalPaidAmount < 0) {
			totalSaved = 0;
			totalPriceBeforeDiscount = totalPrice;
		}

		double noTax = totalPrice / (1 + (SETTINGS.tax / 100));
		if (noTax < 0)
			noTax *= -1;
		double tax = totalPrice - noTax;
		String name = "";
		if (user.getFullName().length() > 9)
			name = user.getFullName().substring(0, 9);
		else {
			name = String.format("%9s", user.getFullName());
		}


		return "C100" + String.format(locale, "%09d", rowNumber) + pc + recordType + String.format(locale, "%020d", orderId) + DateConverter.getYYYYMMDD(new Timestamp(System.currentTimeMillis())) + DateConverter.getHHMM(new Timestamp(System.currentTimeMillis()))
				+ String.format(locale, "%50s", "OldCustomer") + Util.spaces(50) + Util.spaces(10) + Util.spaces(30) + Util.spaces(8) + Util.spaces(30) + Util.spaces(2) + Util.spaces(15) + Util.spaces(9)
				+ DateConverter.getYYYYMMDD(new Timestamp(System.currentTimeMillis())) + Util.spaces(15) + Util.spaces(3)
				+ OP + Util.x12V99(totalPriceBeforeDiscount / (1 + (SETTINGS.tax / 100)))
				+ mOP + Util.x12V99(((totalSaved) / (1 + (SETTINGS.tax / 100))))
				+ OP + Util.x12V99(noTax)
				+ OP + Util.x12V99(tax + 0.004)
				+ OP + Util.x12V99(totalPrice)
				+ OP + String.format(locale, "%09.0f", 0.0f) + String.format(locale, "%02d", (int) ((0.0f - Math.floor(0.0f) + 0.001) * 100))
				+ Util.spaces(13) + "a0" + Util.spaces(8) + "b0" + "0" + DateConverter.getYYYYMMDD(new Timestamp(System.currentTimeMillis())) + Util.spaces(7) + name + String.format(locale, "%07d", orderId)
				+ Util.spaces(13);


	}

	static class test {
		public static void main1(String[] args) {
			double num = 414.70;
			double tax = 17.0;

			double withouttax = num / (1 + (tax / 100));

			String str = String.format("%2.2f", withouttax - 0.005);
			System.out.println(str);


			String st = String.format("%2.2f", num - Double.parseDouble(str));
			System.out.println((num - withouttax) + "\n" + Util.x12V99(num - withouttax + 0.004));
			//print(Double.parseDouble(st));
		}

	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}
}
