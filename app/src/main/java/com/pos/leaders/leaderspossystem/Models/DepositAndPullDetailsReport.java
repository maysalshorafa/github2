package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 9/10/2019.
 */

public class DepositAndPullDetailsReport {
    private long depositAndPullDetailsId;
    private long depositAndPullId;
    private double amount;
    private String currencyType;

    public long getDepositAndPullDetailsId() {
        return depositAndPullDetailsId;
    }

    public void setDepositAndPullDetailsId(long depositAndPullDetailsId) {
        this.depositAndPullDetailsId = depositAndPullDetailsId;
    }

    public long getDepositAndPullId() {
        return depositAndPullId;
    }

    public void setDepositAndPullId(long depositAndPullId) {
        this.depositAndPullId = depositAndPullId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public DepositAndPullDetailsReport() {
    }

    public DepositAndPullDetailsReport(long depositAndPullDetailsId, long depositAndPullId, double amount, String currencyType) {
        this.depositAndPullDetailsId = depositAndPullDetailsId;
        this.depositAndPullId = depositAndPullId;
        this.amount = amount;
        this.currencyType = currencyType;
    }

    @Override
    public String toString() {
        return "DepositAndPullDetailsReport{" +
                "depositAndPullDetailsId=" + depositAndPullDetailsId +
                ", depositAndPullId=" + depositAndPullId +
                ", amount=" + amount +
                ", currencyType='" + currencyType + '\'' +
                '}';
    }
}
