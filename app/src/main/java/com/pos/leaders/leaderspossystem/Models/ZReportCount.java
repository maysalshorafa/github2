package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/13/2019.
 */

public class ZReportCount {
    private long zReportCountId;
    private long zReportCountZReportId;
    private int cashCount;
    private int checkCount;
    private  int creditCount;
    private int invoiceCount;
    private int creditInvoiceCount;
    private int firstTYpeCount;
    private int secondTypeCount;
    private int thirdTypeCount;
    private int fourthTypeCount;
    private int invoiceReceiptCount;

    public ZReportCount() {
    }

    public ZReportCount(long zReportCountId, int cashCount, int checkCount, int creditCount, int invoiceCount, int creditInvoiceCount, int firstTYpeCount, int secondTypeCount, int thirdTypeCount, int fourthTypeCount, int invoiceReceiptCount,long zReportCountZReportId) {
        this.zReportCountId = zReportCountId;
        this.cashCount = cashCount;
        this.checkCount = checkCount;
        this.creditCount = creditCount;
        this.invoiceCount = invoiceCount;
        this.creditInvoiceCount = creditInvoiceCount;
        this.firstTYpeCount = firstTYpeCount;
        this.secondTypeCount = secondTypeCount;
        this.thirdTypeCount = thirdTypeCount;
        this.fourthTypeCount = fourthTypeCount;
        this.invoiceReceiptCount = invoiceReceiptCount;
        this.zReportCountZReportId=zReportCountZReportId;
    }

    public long getzReportCountZReportId() {
        return zReportCountZReportId;
    }

    public void setzReportCountZReportId(long zReportCountZReportId) {
        this.zReportCountZReportId = zReportCountZReportId;
    }

    public long getzReportCountId() {
        return zReportCountId;
    }

    public void setzReportCountId(long zReportCountId) {
        this.zReportCountId = zReportCountId;
    }

    public int getCashCount() {
        return cashCount;
    }

    public void setCashCount(int cashCount) {
        this.cashCount = cashCount;
    }

    public int getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(int checkCount) {
        this.checkCount = checkCount;
    }

    public int getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(int creditCount) {
        this.creditCount = creditCount;
    }

    public int getInvoiceCount() {
        return invoiceCount;
    }

    public void setInvoiceCount(int invoiceCount) {
        this.invoiceCount = invoiceCount;
    }

    public int getCreditInvoiceCount() {
        return creditInvoiceCount;
    }

    public void setCreditInvoiceCount(int creditInvoiceCount) {
        this.creditInvoiceCount = creditInvoiceCount;
    }

    public int getFirstTYpeCount() {
        return firstTYpeCount;
    }

    public void setFirstTYpeCount(int firstTYpeCount) {
        this.firstTYpeCount = firstTYpeCount;
    }

    public int getSecondTypeCount() {
        return secondTypeCount;
    }

    public void setSecondTypeCount(int secondTypeCount) {
        this.secondTypeCount = secondTypeCount;
    }

    public int getThirdTypeCount() {
        return thirdTypeCount;
    }

    public void setThirdTypeCount(int thirdTypeCount) {
        this.thirdTypeCount = thirdTypeCount;
    }

    public int getFourthTypeCount() {
        return fourthTypeCount;
    }

    public void setFourthTypeCount(int fourthTypeCount) {
        this.fourthTypeCount = fourthTypeCount;
    }

    public int getInvoiceReceiptCount() {
        return invoiceReceiptCount;
    }

    public void setInvoiceReceiptCount(int invoiceReceiptCount) {
        this.invoiceReceiptCount = invoiceReceiptCount;
    }

    @Override
    public String toString() {
        return "ZReportCount{" +
                "zReportCountId=" + zReportCountId +
                ", zReportCountZReportId=" + zReportCountZReportId +
                ", cashCount=" + cashCount +
                ", checkCount=" + checkCount +
                ", creditCount=" + creditCount +
                ", invoiceCount=" + invoiceCount +
                ", creditInvoiceCount=" + creditInvoiceCount +
                ", firstTYpeCount=" + firstTYpeCount +
                ", secondTypeCount=" + secondTypeCount +
                ", thirdTypeCount=" + thirdTypeCount +
                ", fourthTypeCount=" + fourthTypeCount +
                ", invoiceReceiptCount=" + invoiceReceiptCount +
                '}';
    }
}
