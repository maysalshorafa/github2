package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Win8.1 on 2/3/2019.
 */

public class XReport  implements Serializable {
    private long xReportId;
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
    private double pullReportAmount;
    private double depositReportAmount;
    private double salesBeforeTax;
    private double salesWithTax;
    private double totalTax;
    public XReport() {


    }

    public XReport(long xReportId, Timestamp createdAt, long byUser, long startOrderId, long endOrderId, double totalAmount , double totalSales,double cashTotal,double checkTotal ,double creditTotal,double totalPosSales,double tax,double invoiceAmount, double creditInvoiceAmount,double shekelAmount,double usdAmount, double eurAmount,
                   double gbpAmount, double invoiceReceiptAmount,double pullReportAmount,double depositReportAmount,double salesBeforeTax,double salesWithTax,double totalTax) {
        this.xReportId = xReportId;
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
        this.pullReportAmount=pullReportAmount;
        this.depositReportAmount=depositReportAmount;
        this.salesBeforeTax=salesBeforeTax;
        this.salesWithTax=salesWithTax;
        this.totalTax=totalTax;
    }

    public XReport(long xReportId, Timestamp createdAt, long byUser, long startOrderId, long endOrderId, double amount) {
        this.xReportId = xReportId;
        this.createdAt = createdAt;
        this.byUser = byUser;
        this.startOrderId = startOrderId;
        this.endOrderId = endOrderId;
        this.totalSales=amount;
        this.totalAmount = totalAmount;
    }

    public XReport(long xReportId, Timestamp createdAt, Employee user, Order startSale, Order endSale) {
        this.xReportId = xReportId;
        this.createdAt = createdAt;
        this.user = user;
        this.startSale = startSale;
        this.endSale = endSale;

        this.byUser=user.getEmployeeId();
        this.startOrderId =startSale.getOrderId();
        this.endOrderId =endSale.getOrderId();
    }

    public XReport(long xReportId, Timestamp createdAt, Employee user, long startOrderId, Order endSale) {
        this.xReportId = xReportId;
        this.createdAt = createdAt;
        this.user = user;
        this.endSale = endSale;

        this.byUser=user.getEmployeeId();
        this.startOrderId = startOrderId;
        this.endOrderId =endSale.getOrderId();
    }

    public XReport(XReport xReport) {
        this.xReportId = xReport.xReportId;
        this.createdAt = xReport.createdAt;
        this.user = xReport.user;
        this.startSale = xReport.startSale;
        this.endSale = xReport.endSale;
        this.byUser = xReport.byUser;
        this.startOrderId = xReport.startOrderId;
        this.endOrderId = xReport.endOrderId;
        this.totalSales=xReport.totalSales;
        this.totalAmount =xReport.totalAmount;
    }

    public double getSalesBeforeTax() {
        return salesBeforeTax;
    }

    public void setSalesBeforeTax(double salesBeforeTax) {
        this.salesBeforeTax = salesBeforeTax;
    }

    public double getSalesWithTax() {
        return salesWithTax;
    }

    public void setSalesWithTax(double salesWithTax) {
        this.salesWithTax = salesWithTax;
    }

    public long getxReportId() {
        return xReportId;
    }

    public void setxReportId(long xReportId) {
        this.xReportId = xReportId;
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
    public double getPullReportAmount() {
        return pullReportAmount;
    }

    public void setPullReportAmount(double pullReportAmount) {
        this.pullReportAmount = pullReportAmount;
    }

    public double getDepositReportAmount() {
        return depositReportAmount;
    }

    public void setDepositReportAmount(double depositReportAmount) {
        this.depositReportAmount = depositReportAmount;
    }
    //region OpenFormat
    public String BKMVDATA(int rowNumber,String pc,int totalRows){

        String spaces = "";
        for (int i=0;i<50;i++){
            spaces += "\u0020";
        }
        return "Z900" + String.format(Util.locale,"%09d", rowNumber) + pc + String.format(Util.locale,"%015d", 1) + "&OF1.31&" + String.format(Util.locale,"%015d", totalRows) + spaces;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    @Override
    public String toString() {
        return "XReport{" +
                "xReportId=" + xReportId +
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
                ", pullReportAmount=" + pullReportAmount +
                ", depositReportAmount=" + depositReportAmount +
                ", salesBeforeTax=" + salesBeforeTax +
                ", salesWithTax=" + salesWithTax +
                ", totalTax=" + totalTax +
                '}';
    }
}
