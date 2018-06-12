package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;

/**
 * Created by KARAM on 05/01/2017.
 */

public class ZReport {
    private long zReportId;
    private Timestamp createdAt;
    private long byUser;
    @JsonIgnore
    private Employee user;
    private long startOrderId;
    private double amount;
    @JsonIgnore
    private double totalAmount;
    @JsonIgnore
    private Order startSale;
    private long endOrderId;
    @JsonIgnore
    private Order endSale;

    public ZReport() {

    }

    public ZReport(long zReportId, Timestamp createdAt, long byUser, long startOrderId, long endOrderId, double amount, double totalAmount) {
        this.zReportId = zReportId;
        this.createdAt = createdAt;
        this.byUser = byUser;
        this.startOrderId = startOrderId;
        this.endOrderId = endOrderId;
        this.amount=amount;
        this.totalAmount = totalAmount;
    }

    public ZReport(long zReportId, Timestamp createdAt, long byUser, long startOrderId, long endOrderId, double amount) {
        this.zReportId = zReportId;
        this.createdAt = createdAt;
        this.byUser = byUser;
        this.startOrderId = startOrderId;
        this.endOrderId = endOrderId;
        this.amount=amount;
        this.totalAmount = totalAmount;
    }

    public ZReport(long zReportId, Timestamp createdAt, Employee user, Order startSale, Order endSale) {
        this.zReportId = zReportId;
        this.createdAt = createdAt;
        this.user = user;
        this.startSale = startSale;
        this.endSale = endSale;

        this.byUser=user.getEmployeeId();
        this.startOrderId =startSale.getOrderId();
        this.endOrderId =endSale.getOrderId();
    }

    public ZReport(long zReportId, Timestamp createdAt, Employee user, long startOrderId, Order endSale) {
        this.zReportId = zReportId;
        this.createdAt = createdAt;
        this.user = user;
        this.endSale = endSale;

        this.byUser=user.getEmployeeId();
        this.startOrderId = startOrderId;
        this.endOrderId =endSale.getOrderId();
    }

    public ZReport(ZReport zReport) {
        this.zReportId = zReport.zReportId;
        this.createdAt = zReport.createdAt;
        this.user = zReport.user;
        this.startSale = zReport.startSale;
        this.endSale = zReport.endSale;
        this.byUser = zReport.byUser;
        this.startOrderId = zReport.startOrderId;
        this.endOrderId = zReport.endOrderId;
        this.amount=zReport.amount;
        this.totalAmount =zReport.totalAmount;
    }

    public long getzReportId() {
        return zReportId;
    }

    public void setzReportId(long zReportId) {
        this.zReportId = zReportId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public long getByUser() {
        return byUser;
    }

    public void setByUser(long byUser) {
        this.byUser = byUser;
    }

    public Employee getUser() {
        return user;
    }

    public void setUser(Employee user) {
        this.user = user;
    }

    public long getStartOrderId() {
        return startOrderId;
    }

    public void setStartOrderId(long startOrderId) {
        this.startOrderId = startOrderId;
    }

    public Order getStartSale() {
        return startSale;
    }

    public void setStartSale(Order startSale) {
        this.startSale = startSale;
    }

    public long getEndOrderId() {
        return endOrderId;
    }

    public void setEndOrderId(long endOrderId) {
        this.endOrderId = endOrderId;
    }

    public Order getEndSale() {
        return endSale;
    }

    public void setEndSale(Order endSale) {
        this.endSale = endSale;
    }

    public double getAmount() {
        return amount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    //region OpenFormat
    public String BKMVDATA(int rowNumber,String pc,int totalRows){

        String spaces = "";
        for (int i=0;i<50;i++){
            spaces += "\u0020";
        }
        return "Z900" + String.format(Util.locale,"%09d", rowNumber) + pc + String.format(Util.locale,"%015d", 1) + "&OF1.31&" + String.format(Util.locale,"%015d", totalRows) + spaces;
    }
    //endregion OpenFormat
}
