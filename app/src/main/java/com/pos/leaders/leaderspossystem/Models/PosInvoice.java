package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 12/10/2018.
 */

public class PosInvoice {
    private long id;
    private double amount;
    private long lastZReportId;
    private String type;
    private String status;
    private String boID;
    private String paymentMethod;

    public PosInvoice() {
    }

    public PosInvoice(long id, double amount, long lastZReportId,String type,String status,String boID,String paymentMethod) {
        this.id = id;
        this.amount = amount;
        this.lastZReportId = lastZReportId;
        this.type=type;
        this.status=status;
        this.boID=boID;
        this.paymentMethod=paymentMethod;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getLastZReportId() {
        return lastZReportId;
    }

    public void setLastZReportId(long lastZReportId) {
        this.lastZReportId = lastZReportId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBoID() {
        return boID;
    }

    public void setBoID(String boID) {
        this.boID = boID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "PosInvoice{" +
                "id=" + id +
                ", amount=" + amount +
                ", lastZReportId=" + lastZReportId +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", boID='" + boID + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
