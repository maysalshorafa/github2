package com.pos.leaders.leaderspossystem.Models.Currency;

/**
 * Created by Win8.1 on 9/27/2017.
 */

public class CashPayment {
    private long cashPaymentId;
    private long saleId;
    private double amount;
    private long currency_type;
    private long createDate;

    public CashPayment() {
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }



    public long getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(long currency_type) {
        this.currency_type = currency_type;
    }




    // Constructors
    public CashPayment(long cashPaymentId, long saleId, double amount, long currency_type, long createDate) {
        this.cashPaymentId = cashPaymentId;
        this.amount = amount;
        this.saleId = saleId;
        this.currency_type=currency_type;
        this.createDate=createDate;
    }

    public CashPayment(CashPayment p) {
        this(p.getCashPaymentId(),  p.getSaleId(),p.getAmount(),p.getCurrency_type(),p.getCreateDate());
    }

    // Getters
    public long getCashPaymentId() {
        return cashPaymentId;
    }


    public long getSaleId() {
        return saleId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "cashPaymentId=" + cashPaymentId +
                ",currency_type ='" + currency_type + '\'' +
                ", amount='" + amount + '\'' +
                ", saleId=" + saleId +
                '}';
    }


}
