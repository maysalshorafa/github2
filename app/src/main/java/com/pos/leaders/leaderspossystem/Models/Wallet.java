package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 8/19/2018.
 */

public class Wallet {
    WalletStatus status;
    double creditAmount;
    long customerId;

    public Wallet(WalletStatus status, double creditAmount, long customerId) {
        this.status = status;
        this.creditAmount = creditAmount;
        this.customerId = customerId;
    }
    public  Wallet(){

    }
    public WalletStatus getStatus() {
        return status;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "{" +
                "\""+"status"+ "\""+":" + "\""+status+"\"" +
                ","+"\""+"creditAmount"+ "\""+":" + "\""+ creditAmount+"\""  +
                ","+"\""+"customerId"+ "\""+":" + "\""+ + customerId +"\"" +
                '}';
    }
}
