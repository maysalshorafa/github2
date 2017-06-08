package com.pos.leaders.leaderspossystem.Models;

import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;

/**
 * Created by KARAM on 12/04/2017.
 */

public class AReport {
    private int id;
    private long creationDate;
    private int byUserID;
    private User byUser;
    private double amount;
    private int lastSaleID;
    private Sale lastSale;
    private int lastZReportID;
    private ZReport lastZReport;


    //region Constructors

    public AReport() {}

    public AReport(long creationDate, int byUserID, double amount, int lastSaleID, int lastZReportID) {
        this.creationDate = creationDate;
        this.byUserID = byUserID;
        this.amount = amount;
        this.lastSaleID = lastSaleID;
        this.lastZReportID = lastZReportID;
    }

    public AReport(long creationDate, User byUser, Sale lastSale, ZReport lastZReport, double amount) {
        this.creationDate = creationDate;
        this.byUser = byUser;
        this.lastSale = lastSale;
        this.lastZReport = lastZReport;
        this.amount = amount;
    }

    public AReport(int id, long creationDate, int byUserID, double amount, int lastSaleID, int lastZReportID) {
        this.id = id;
        this.creationDate = creationDate;
        this.byUserID = byUserID;
        this.amount = amount;
        this.lastSaleID = lastSaleID;
        this.lastZReportID = lastZReportID;
    }
    //endregion Constructors

    //region Getter

    public int getId() {
        return id;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public int getByUserID() {
        return byUserID;
    }

    public User getByUser() {
        return byUser;
    }

    public double getAmount() {
        return amount;
    }

    public int getLastSaleID() {
        return lastSaleID;
    }

    public Sale getLastSale() {
        return lastSale;
    }

    public int getLastZReportID() {
        return lastZReportID;
    }

    public ZReport getLastZReport() {
        return lastZReport;
    }


    //endregion Getter

    //region Setter

    public void setId(int id) {
        this.id = id;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public void setByUserID(int byUserID) {
        this.byUserID = byUserID;
    }

    public void setByUser(User byUser) {
        this.byUser = byUser;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setLastSaleID(int lastSaleID) {
        this.lastSaleID = lastSaleID;
    }

    public void setLastSale(Sale lastSale) {
        this.lastSale = lastSale;
    }

    public void setLastZReportID(int lastZReportID) {
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
