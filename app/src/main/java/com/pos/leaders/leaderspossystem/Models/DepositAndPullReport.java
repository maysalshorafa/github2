package com.pos.leaders.leaderspossystem.Models;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 9/8/2019.
 */

public class DepositAndPullReport {
    private long depositAndPullReportId;
    private Timestamp createdAt;
    private long byUserID;
    private double amount;
    private String type;
    private long lastZReportID;

    public DepositAndPullReport() {
    }

    public DepositAndPullReport(long depositAndPullReportId, Timestamp createdAt, long byUserID, double amount, String type,long lastZReportID) {
        this.depositAndPullReportId = depositAndPullReportId;
        this.createdAt = createdAt;
        this.byUserID = byUserID;
        this.amount = amount;
        this.type = type;
        this.lastZReportID=lastZReportID;
    }

    public long getDepositAndPullReportId() {
        return depositAndPullReportId;
    }

    public void setDepositAndPullReportId(long depositAndPullReportId) {
        this.depositAndPullReportId = depositAndPullReportId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public long getByUserID() {
        return byUserID;
    }

    public void setByUserID(long byUserID) {
        this.byUserID = byUserID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getLastZReportID() {
        return lastZReportID;
    }

    public void setLastZReportID(long lastZReportID) {
        this.lastZReportID = lastZReportID;
    }

    @Override
    public String toString() {
        return "DepositAndPullReport{" +
                "depositAndPullReportId=" + depositAndPullReportId +
                ", createdAt=" + createdAt +
                ", byUserID=" + byUserID +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", lastZReportID=" + lastZReportID +
                '}';
    }
}

