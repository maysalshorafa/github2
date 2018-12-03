package com.pos.leaders.leaderspossystem.Models;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Win8.1 on 9/9/2018.
 */

public class ReceiptDocuments {
    private String type;
    private Timestamp date;
    private ArrayList<String> invoicesNumbers;
    private JSONObject customer;
    private double paidAmount;
    private String currency;
    private JSONObject payments;
    public ReceiptDocuments() {
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

    public ArrayList<String> getInvoicesNumbers() {
        return invoicesNumbers;
    }

    public void setInvoicesNumbers(ArrayList<String> invoicesNumbers) {
        this.invoicesNumbers = invoicesNumbers;
    }

    public JSONObject getCustomer() {
        return customer;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
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

    public JSONObject getPayments() {
        return payments;
    }

    public void setPayments(JSONObject payments) {
        this.payments = payments;
    }

    public ReceiptDocuments(String type, Timestamp date, ArrayList<String> invoicesNumbers, double paidAmount, String currency) {
        this.type = type;
        this.date = date;
        this.invoicesNumbers = invoicesNumbers;
        this.paidAmount = paidAmount;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "ReceiptDocuments{" +
                "type='" + type + '\'' +
                ", date=" + date +
                ", invoicesNumbers=" + invoicesNumbers +
                ", customer=" + customer +
                ", paidAmount=" + paidAmount +
                ", currency='" + currency + '\'' +
                ", payments=" + payments +
                '}';
    }
}
