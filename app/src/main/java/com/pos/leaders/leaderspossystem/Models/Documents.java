package com.pos.leaders.leaderspossystem.Models;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Win8.1 on 8/27/2018.
 */
public class Documents {
    private String type;
    private Timestamp date;
    private Timestamp fulfillmentDate;
    private Timestamp dueDate;
    private JSONObject customer;
    private double total;
    private double totalPaid;
    private double balanceDue;
    private InvoiceStatus invoiceStatus;
    private String publicNote;
    private String privateNote;
    private String currency;
    private JSONObject user;
    private ArrayList<JSONObject>cartDetailsList;
    private double cartDiscount;
    public Documents() {
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Timestamp getFulfillmentDate() {
        return fulfillmentDate;
    }

    public void setFulfillmentDate(Timestamp fulfillmentDate) {
        this.fulfillmentDate = fulfillmentDate;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }
    public JSONObject getCustomer() {
        return customer;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public double getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(double balanceDue) {
        this.balanceDue = balanceDue;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getPublicNote() {
        return publicNote;
    }

    public void setPublicNote(String publicNote) {
        this.publicNote = publicNote;
    }

    public String getPrivateNote() {
        return privateNote;
    }

    public void setPrivateNote(String privateNote) {
        this.privateNote = privateNote;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCustomer(JSONObject customer) {
        this.customer = customer;
    }

    public JSONObject getUser() {
        return user;
    }

    public void setUser(JSONObject user) {
        this.user = user;
    }

    public ArrayList<JSONObject> getCartDetailsList() {
        return cartDetailsList;
    }

    public void setCartDetailsList(ArrayList<JSONObject> cartDetailsList) {
        this.cartDetailsList = cartDetailsList;
    }

    public Documents(String type, Timestamp date, Timestamp fulfillmentDate, Timestamp dueDate, double total, double totalPaid, double balanceDue, InvoiceStatus invoiceStatus, String publicNote, String privateNote, String currency,double cartDiscount) {
        this.type = type;
        this.date = date;
        this.fulfillmentDate = fulfillmentDate;
        this.dueDate = dueDate;
        this.total = total;
        this.totalPaid = totalPaid;
        this.balanceDue = balanceDue;
        this.invoiceStatus = invoiceStatus;
        this.publicNote = publicNote;
        this.privateNote = privateNote;
        this.currency = currency;
        this.cartDiscount=cartDiscount;
    }

    @Override
    public String toString() {
        return "Documents{" +
                "type='" + type + '\'' +
                ", date=" + date +
                ", fulfillmentDate=" + fulfillmentDate +
                ", dueDate=" + dueDate  +
                ", customer=" + customer +
                ", total=" + total +
                ", totalPaid=" + totalPaid +
                ", balanceDue=" + balanceDue +
                ", invoiceStatus=" + invoiceStatus +
                ", publicNote='" + publicNote + '\'' +
                ", privateNote='" + privateNote + '\'' +
                ", currency='" + currency + '\'' +
                ", user=" + user +
                ", cartDetailsList=" + cartDetailsList +
                ", cartDiscount=" + cartDiscount +
                '}';
    }
}
