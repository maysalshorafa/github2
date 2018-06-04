package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.Util;

/**
 * Created by KARAM on 05/01/2017.
 */

public class ZReport {
    private long zReportId;
    private long creationDate;
    private long byUser;
    @JsonIgnore
    private User user;
    private long startSaleId;
    private double amount;
    @JsonIgnore
    private double total_amount;
    @JsonIgnore
    private Order startSale;
    private long endSaleId;
    @JsonIgnore
    private Order endSale;

    public ZReport() {

    }

    public ZReport(long zReportId, long creationDate, long byUser, long startSaleId, long endSaleId, double amount, double total_amount) {
        this.zReportId = zReportId;
        this.creationDate = creationDate;
        this.byUser = byUser;
        this.startSaleId = startSaleId;
        this.endSaleId = endSaleId;
        this.amount=amount;
        this.total_amount=total_amount;
    }

    public ZReport(long zReportId, long creationDate, long byUser, long startSaleId, long endSaleId, double amount) {
        this.zReportId = zReportId;
        this.creationDate = creationDate;
        this.byUser = byUser;
        this.startSaleId = startSaleId;
        this.endSaleId = endSaleId;
        this.amount=amount;
        this.total_amount=total_amount;
    }

    public ZReport(long zReportId, long creationDate, User user, Order startSale, Order endSale) {
        this.zReportId = zReportId;
        this.creationDate = creationDate;
        this.user = user;
        this.startSale = startSale;
        this.endSale = endSale;

        this.byUser=user.getUserId();
        this.startSaleId=startSale.getOrderId();
        this.endSaleId=endSale.getOrderId();
    }

    public ZReport(long zReportId, long creationDate, User user, long startSaleId, Order endSale) {
        this.zReportId = zReportId;
        this.creationDate = creationDate;
        this.user = user;
        this.endSale = endSale;

        this.byUser=user.getUserId();
        this.startSaleId=startSaleId;
        this.endSaleId=endSale.getOrderId();
    }

    public ZReport(ZReport zReport) {
        this.zReportId = zReport.zReportId;
        this.creationDate = zReport.creationDate;
        this.user = zReport.user;
        this.startSale = zReport.startSale;
        this.endSale = zReport.endSale;
        this.byUser = zReport.byUser;
        this.startSaleId = zReport.startSaleId;
        this.endSaleId = zReport.endSaleId;
        this.amount=zReport.amount;
        this.total_amount=zReport.total_amount;
    }

    public long getzReportId() {
        return zReportId;
    }

    public void setzReportId(long zReportId) {
        this.zReportId = zReportId;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getByUser() {
        return byUser;
    }

    public void setByUser(long byUser) {
        this.byUser = byUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getStartSaleId() {
        return startSaleId;
    }

    public void setStartSaleId(long startSaleId) {
        this.startSaleId = startSaleId;
    }

    public Order getStartSale() {
        return startSale;
    }

    public void setStartSale(Order startSale) {
        this.startSale = startSale;
    }

    public long getEndSaleId() {
        return endSaleId;
    }

    public void setEndSaleId(long endSaleId) {
        this.endSaleId = endSaleId;
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

    public double getTotal_amount() {
        return total_amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
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
