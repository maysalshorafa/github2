package com.pos.leaders.leaderspossystem.Models;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 11/12/2018.
 */

public class ClosingReport{
    private long closingReportId;
    private double actualTotalValue;
    private double expectedTotalValue;
    private double differentTotalValue;
    private Timestamp createdAt;
    private long opiningReportId;
    private long lastOrderId;


    public ClosingReport(long closingReportId, double actualTotalValue, double expectedTotalValue, double differentTotalValue, Timestamp createdAt,long opiningReportId , long lastOrderId) {
        this.closingReportId = closingReportId;
        this.actualTotalValue = actualTotalValue;
        this.expectedTotalValue = expectedTotalValue;
        this.differentTotalValue = differentTotalValue;
        this.createdAt = createdAt;
        this.opiningReportId=opiningReportId;
        this.lastOrderId=lastOrderId;
    }



    public long getClosingReportId() {
        return closingReportId;
    }

    public double getActualTotalValue() {
        return actualTotalValue;
    }

    public double getExpectedTotalValue() {
        return expectedTotalValue;
    }

    public double getDifferentTotalValue() {
        return differentTotalValue;
    }

    public long getOpiningReportId() {
        return opiningReportId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public long getLastOrderId() {
        return lastOrderId;
    }

    public void setClosingReportId(long closingReportId) {
        this.closingReportId = closingReportId;
    }

    public void setActualTotalValue(double actualTotalValue) {
        this.actualTotalValue = actualTotalValue;
    }

    public void setExpectedTotalValue(double expectedTotalValue) {
        this.expectedTotalValue = expectedTotalValue;
    }

    public void setDifferentTotalValue(double differentTotalValue) {
        this.differentTotalValue = differentTotalValue;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setOpiningReportId(long opiningReportId) {
        this.opiningReportId = opiningReportId;
    }

    public void setLastOrderId(long lastOrderId) {
        this.lastOrderId = lastOrderId;
    }
}
