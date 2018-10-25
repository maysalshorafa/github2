package com.pos.leaders.leaderspossystem.Models;

import com.pos.leaders.leaderspossystem.Tools.CreditInvoiceStatus;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Win8.1 on 10/24/2018.
 */

public class CreditInvoiceDocument {
        private String type;
        private Timestamp date;
        private JSONObject customer;
        private double total;
        private CreditInvoiceStatus status;
        private String currency;
        private JSONObject user;
        private ArrayList<JSONObject> cartDetailsList;
        private double cartDiscount;
        private String reference;
        public CreditInvoiceDocument() {
        }

    public double getCartDiscount() {
        return cartDiscount;
    }

    public void setCartDiscount(double cartDiscount) {
        this.cartDiscount = cartDiscount;
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

        public JSONObject getCustomer() {
            return customer;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public CreditInvoiceStatus getStatus() {
            return status;
        }

        public void setStatus(CreditInvoiceStatus status) {
            this.status = status;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public CreditInvoiceDocument(String type, Timestamp date, double total, CreditInvoiceStatus status, String currency, double cartDiscount, String reference) {
        this.type = type;
        this.date = date;
        this.total = total;
        this.status = status;
        this.currency = currency;
        this.cartDiscount = cartDiscount;
        this.reference = reference;

    }
}
