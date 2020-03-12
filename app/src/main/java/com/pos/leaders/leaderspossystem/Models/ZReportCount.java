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
    private int shekelCount;
    private int usdCount;
    private int eurCount;
    private int gbpCount;
    private int invoiceReceiptCount;
    private int minusGeneralItemCount;

    public ZReportCount() {
    }

    public ZReportCount(long zReportCountId, int cashCount, int checkCount, int creditCount, int invoiceCount, int creditInvoiceCount, int shekelCount, int usdCount, int eurCount, int gbpCount, int invoiceReceiptCount,long zReportCountZReportId,int minusGeneralItemCount) {
        this.zReportCountId = zReportCountId;
        this.cashCount = cashCount;
        this.checkCount = checkCount;
        this.creditCount = creditCount;
        this.invoiceCount = invoiceCount;
        this.creditInvoiceCount = creditInvoiceCount;
        this.shekelCount = shekelCount;
        this.usdCount = usdCount;
        this.eurCount = eurCount;
        this.gbpCount = gbpCount;
        this.invoiceReceiptCount = invoiceReceiptCount;
        this.zReportCountZReportId=zReportCountZReportId;
        this.minusGeneralItemCount=minusGeneralItemCount;
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

    public int getShekelCount() {
        return shekelCount;
    }

    public void setShekelCount(int shekelCount) {
        this.shekelCount = shekelCount;
    }

    public int getUsdCount() {
        return usdCount;
    }

    public void setUsdCount(int usdCount) {
        this.usdCount = usdCount;
    }

    public int getEurCount() {
        return eurCount;
    }

    public void setEurCount(int eurCount) {
        this.eurCount = eurCount;
    }

    public int getGbpCount() {
        return gbpCount;
    }

    public void setGbpCount(int gbpCount) {
        this.gbpCount = gbpCount;
    }

    public int getInvoiceReceiptCount() {
        return invoiceReceiptCount;
    }

    public void setInvoiceReceiptCount(int invoiceReceiptCount) {
        this.invoiceReceiptCount = invoiceReceiptCount;
    }

    public int getMinusGeneralItemCount() {
        return minusGeneralItemCount;
    }

    public void setMinusGeneralItemCount(int minusGeneralItemCount) {
        this.minusGeneralItemCount = minusGeneralItemCount;
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
                ", shekelCount=" + shekelCount +
                ", usdCount=" + usdCount +
                ", eurCount=" + eurCount +
                ", gbpCount=" + gbpCount +
                ", invoiceReceiptCount=" + invoiceReceiptCount +
                ", minusGeneralItemCount=" + minusGeneralItemCount +
                '}';
    }
}
