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
    @JsonIgnore
    private Order startSale;
    private long endOrderId;
    @JsonIgnore
    private Order endSale;
    private double totalSales;
    private double totalAmount;
    private double tax;
    private double cashTotal;
    private double checkTotal;
    private  double creditTotal;
    private double totalPosSales;
    private double invoiceAmount;
    private double creditInvoiceAmount;
    private double shekelAmount;
    private double usdAmount;
    private double eurAmount;
    private double gbpAmount;
    private double invoiceReceiptAmount;
    public ZReport() {


    }

    public ZReport(long zReportId, Timestamp createdAt, long byUser, long startOrderId, long endOrderId, double totalAmount , double totalSales,double cashTotal,double checkTotal ,double creditTotal,double totalPosSales,double tax,double invoiceAmount, double creditInvoiceAmount,double shekelAmount,double usdAmount, double eurAmount,
                   double gbpAmount, double invoiceReceiptAmount) {
        this.zReportId = zReportId;
        this.createdAt = createdAt;
        this.byUser = byUser;
        this.startOrderId = startOrderId;
        this.endOrderId = endOrderId;
        this.totalSales=totalSales;
        this.totalAmount = totalAmount;
        this.tax=tax;
        this.cashTotal=cashTotal;
        this.checkTotal=checkTotal;
        this.creditTotal=creditTotal;
        this.totalPosSales=totalPosSales;
        this.invoiceAmount=invoiceAmount;
        this.creditInvoiceAmount=creditInvoiceAmount;
        this.shekelAmount=shekelAmount;
        this.usdAmount=usdAmount;
        this.eurAmount=eurAmount;
        this.gbpAmount=gbpAmount;
        this.invoiceReceiptAmount=invoiceReceiptAmount;
    }

    public ZReport(long zReportId, Timestamp createdAt, long byUser, long startOrderId, long endOrderId, double amount) {
        this.zReportId = zReportId;
        this.createdAt = createdAt;
        this.byUser = byUser;
        this.startOrderId = startOrderId;
        this.endOrderId = endOrderId;
        this.totalSales=amount;
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
        this.totalSales=zReport.totalSales;
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
    public double getTotalAmount() {
        return totalAmount;
    }


    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(double cashTotal) {
        this.cashTotal = cashTotal;
    }

    public double getCheckTotal() {
        return checkTotal;
    }

    public void setCheckTotal(double checkTotal) {
        this.checkTotal = checkTotal;
    }

    public double getCreditTotal() {
        return creditTotal;
    }

    public void setCreditTotal(double creditTotal) {
        this.creditTotal = creditTotal;
    }

    public double getTotalPosSales() {
        return totalPosSales;
    }

    public void setTotalPosSales(double totalPosSales) {
        this.totalPosSales = totalPosSales;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public double getCreditInvoiceAmount() {
        return creditInvoiceAmount;
    }

    public void setCreditInvoiceAmount(double creditInvoiceAmount) {
        this.creditInvoiceAmount = creditInvoiceAmount;
    }

    public double getShekelAmount() {
        return shekelAmount;
    }

    public void setShekelAmount(double shekelAmount) {
        this.shekelAmount = shekelAmount;
    }

    public double getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(double usdAmount) {
        this.usdAmount = usdAmount;
    }

    public double getEurAmount() {
        return eurAmount;
    }

    public void setEurAmount(double eurAmount) {
        this.eurAmount = eurAmount;
    }

    public double getGbpAmount() {
        return gbpAmount;
    }

    public void setGbpAmount(double gbpAmount) {
        this.gbpAmount = gbpAmount;
    }

    public double getInvoiceReceiptAmount() {
        return invoiceReceiptAmount;
    }

    public void setInvoiceReceiptAmount(double invoiceReceiptAmount) {
        this.invoiceReceiptAmount = invoiceReceiptAmount;
    }

    //region OpenFormat
    public String BKMVDATA(int rowNumber,String pc,int totalRows){

        String spaces = "";
        for (int i=0;i<50;i++){
            spaces += "\u0020";
        }
        return "Z900" + String.format(Util.locale,"%09d", rowNumber) + pc + String.format(Util.locale,"%015d", 1) + "&OF1.31&" + String.format(Util.locale,"%015d", totalRows) + spaces;
    }

    @Override
    public String toString() {
        return "ZReport{" +
                "zReportId=" + zReportId +
                ", createdAt=" + createdAt +
                ", byUser=" + byUser +
                ", user=" + user +
                ", startOrderId=" + startOrderId +
                ", startSale=" + startSale +
                ", endOrderId=" + endOrderId +
                ", endSale=" + endSale +
                ", totalSales=" + totalSales +
                ", totalAmount=" + totalAmount +
                ", tax=" + tax +
                ", cashTotal=" + cashTotal +
                ", checkTotal=" + checkTotal +
                ", creditTotal=" + creditTotal +
                ", totalPosSales=" + totalPosSales +
                ", invoiceAmount=" + invoiceAmount +
                ", creditInvoiceAmount=" + creditInvoiceAmount +
                ", shekelAmount=" + shekelAmount +
                ", usdAmount=" + usdAmount +
                ", eurAmount=" + eurAmount +
                ", gbpAmount=" + gbpAmount +
                ", invoiceReceiptAmount=" + invoiceReceiptAmount +
                '}';
    }
//endregion OpenFormat
}
