package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by KARAM on 31/10/2017.
 */

public class CreditCardPayment {
    private long creditCardPaymentId;
    private long saleId;
    private double amount;
    private String CreditCardCompanyName;
    private int transactionType;
    private String last4Digits;
    private String transactionId;
    private String answer;
    private int paymentsNumber;
    private double firstPaymentAmount;
    private double otherPaymentAmount;
    private String cardholder;
    private long createDate;


    public CreditCardPayment() {
    }

    public CreditCardPayment(long saleId, double amount, String creditCardCompanyName, int transactionType, String last4Digits, String transactionId, String answer, int paymentsNumber, double firstPaymentAmount, double otherPaymentAmount, String cardholder, long createDate) {
        this.saleId = saleId;
        this.amount = amount;
        CreditCardCompanyName = creditCardCompanyName;
        this.transactionType = transactionType;
        this.last4Digits = last4Digits;
        this.transactionId = transactionId;
        this.answer = answer;
        this.paymentsNumber = paymentsNumber;
        this.firstPaymentAmount = firstPaymentAmount;
        this.otherPaymentAmount = otherPaymentAmount;
        this.cardholder = cardholder;
        this.createDate = createDate;
    }

    public CreditCardPayment(long creditCardPaymentId, long saleId, double amount, String creditCardCompanyName, int transactionType, String last4Digits, String transactionId, String answer, int paymentsNumber, double firstPaymentAmount, double otherPaymentAmount, String cardholder, long createDate) {
        this.creditCardPaymentId = creditCardPaymentId;
        this.saleId = saleId;
        this.amount = amount;
        CreditCardCompanyName = creditCardCompanyName;
        this.transactionType = transactionType;
        this.last4Digits = last4Digits;
        this.transactionId = transactionId;
        this.answer = answer;
        this.paymentsNumber = paymentsNumber;
        this.firstPaymentAmount = firstPaymentAmount;
        this.otherPaymentAmount = otherPaymentAmount;
        this.cardholder = cardholder;
        this.createDate = createDate;
    }

    //region Getters

    public long getCreditCardPaymentId() {
        return creditCardPaymentId;
    }

    public long getSaleId() {
        return saleId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCreditCardCompanyName() {
        return CreditCardCompanyName;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public String getLast4Digits() {
        return last4Digits;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAnswer() {
        return answer;
    }

    public int getPaymentsNumber() {
        return paymentsNumber;
    }

    public double getFirstPaymentAmount() {
        return firstPaymentAmount;
    }

    public double getOtherPaymentAmount() {
        return otherPaymentAmount;
    }

    public String getCardholder() {
        return cardholder;
    }

    public long getCreateDate() {
        return createDate;
    }


    //endregion Getters

    //region Setters

    public void setCreditCardPaymentId(long creditCardPaymentId) {
        this.creditCardPaymentId = creditCardPaymentId;
    }

    public void setSaleId(long saleId) {
        this.saleId = saleId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCreditCardCompanyName(String creditCardCompanyName) {
        CreditCardCompanyName = creditCardCompanyName;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public void setLast4Digits(String last4Digits) {
        this.last4Digits = last4Digits;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setPaymentsNumber(int paymentsNumber) {
        this.paymentsNumber = paymentsNumber;
    }

    public void setFirstPaymentAmount(double firstPaymentAmount) {
        this.firstPaymentAmount = firstPaymentAmount;
    }

    public void setOtherPaymentAmount(double otherPaymentAmount) {
        this.otherPaymentAmount = otherPaymentAmount;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }


    //endregion Setters

    @Override
    public String toString() {
        return "CreditCardPayment{" +
                "accountingId=" + creditCardPaymentId +
                ", saleId=" + saleId +
                ", amount=" + amount +
                ", CreditCardCompanyName='" + CreditCardCompanyName + '\'' +
                ", transactionType=" + transactionType +
                ", last4Digits='" + last4Digits + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", answer='" + answer + '\'' +
                ", paymentsNumber=" + paymentsNumber +
                ", firstPaymentAmount=" + firstPaymentAmount +
                ", otherPaymentAmount=" + otherPaymentAmount +
                ", cardholder='" + cardholder + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
