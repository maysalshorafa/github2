package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;

/**
 * Created by KARAM on 12/04/2017.
 */

public class AReport {
    private long aReportId;
    private Timestamp createdAt;
    private long byUserID;
    @JsonIgnore
    private Employee byUser;
    private double amount;
    private long lastOrderId;
    @JsonIgnore
    private Order lastSale;
    private long lastZReportID;
    @JsonIgnore
    private ZReport lastZReport;


    //region Constructors

    public AReport() {}

    public AReport(Timestamp createdAt, int byUserID, double amount, long lastOrderId, long lastZReportID) {
        this.createdAt = createdAt;
        this.byUserID = byUserID;
        this.amount = amount;
        this.lastOrderId = lastOrderId;
        this.lastZReportID = lastZReportID;
    }

    public AReport(Timestamp createdAt, Employee byUser, Order lastSale, ZReport lastZReport, double amount) {
        this.createdAt = createdAt;
        this.byUser = byUser;
        this.lastSale = lastSale;
        this.lastZReport = lastZReport;
        this.amount = amount;
    }

    public AReport(long aReportId, Timestamp createdAt, long byUserID, double amount, long lastOrderId, long lastZReportID) {
        this.aReportId = aReportId;
        this.createdAt = createdAt;
        this.byUserID = byUserID;
        this.amount = amount;
        this.lastOrderId = lastOrderId;
        this.lastZReportID = lastZReportID;
    }
    //endregion Constructors

    //region Getter

    public long getaReportId() {
        return aReportId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public long getByUserID() {
        return byUserID;
    }

    public Employee getByUser() {
        return byUser;
    }

    public double getAmount() {
        return amount;
    }

    public long getLastOrderId() {
        return lastOrderId;
    }

    public Order getLastSale() {
        return lastSale;
    }

    public long getLastZReportID() {
        return lastZReportID;
    }

    public ZReport getLastZReport() {
        return lastZReport;
    }


    //endregion Getter

    //region Setter

    public void setaReportId(long aReportId) {
        this.aReportId = aReportId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setByUserID(long byUserID) {
        this.byUserID = byUserID;
    }

    public void setByUser(Employee byUser) {
        this.byUser = byUser;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setLastOrderId(long lastOrderId) {
        this.lastOrderId = lastOrderId;
    }

    public void setLastSale(Order lastSale) {
        this.lastSale = lastSale;
    }

    public void setLastZReportID(long lastZReportID) {
        this.lastZReportID = lastZReportID;
    }

    public void setLastZReport(ZReport lastZReport) {
        this.lastZReport = lastZReport;
    }


    //endregion Setter

    //region OpenFormat

    public String BKMVDATA(int rowNumber,String pc){

        String spaces = "";
        for (int i=0;i<50;i++){
            spaces += "\u0020";
        }
        return "A100" + String.format(Util.locale,"%09d", rowNumber) + pc + String.format(Util.locale,"%015d", 1) + "&OF1.31&" + spaces;
    }

    //endregion OpenFormat
}
