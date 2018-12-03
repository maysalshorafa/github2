package com.pos.leaders.leaderspossystem.Models;

import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 10/8/2018.
 */

public class OrderDocuments {
    private String type;
    private Timestamp date;
    private Timestamp fulfillmentDate;
    private Timestamp dueDate;
    private JSONObject Order;
    private JSONObject customer;
    private double total;
    private String comment;
    private String currency;
    private OrderDocumentStatus orderDocumentStatus;
    public OrderDocuments() {
    }

    public OrderDocumentStatus getOrderDocumentStatus() {
        return orderDocumentStatus;
    }

    public void setOrderDocumentStatus(OrderDocumentStatus orderDocumentStatus) {
        this.orderDocumentStatus = orderDocumentStatus;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public JSONObject getOrder() {
        return Order;
    }

    public void setOrder(JSONObject order) {
        Order = order;
    }

    public OrderDocuments(String type, Timestamp date, Timestamp fulfillmentDate, Timestamp dueDate,  double total, String comment, String currency,OrderDocumentStatus orderDocumentStatus) {
        this.type = type;
        this.date = date;
        this.fulfillmentDate = fulfillmentDate;
        this.dueDate = dueDate;
        this.total = total;
        this.comment = comment;
        this.currency = currency;
        this.orderDocumentStatus=orderDocumentStatus;
    }

    @Override
    public String toString() {
        return "OrderDocuments{" +
                "type='" + type + '\'' +
                ", date=" + date +
                ", fulfillmentDate=" + fulfillmentDate +
                ", dueDate=" + dueDate +
                ", Order=" + Order +
                ", customer=" + customer +
                ", total=" + total +
                ", comment='" + comment + '\'' +
                ", currency='" + currency + '\'' +
                ", orderDocumentStatus=" + orderDocumentStatus +
                '}';
    }
}
