package com.pos.leaders.leaderspossystem.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Timestamp;
import java.util.Iterator;

/**
 * Created by Win8.1 on 11/12/2018.
 */
@Entity(tableName = "closing_report")
public class ClosingReport implements Iterable<ClosingReport> {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "closingReportId")
    private long closingReportId;
    @ColumnInfo(name = "actualTotalValue")
    private double actualTotalValue;
    @ColumnInfo(name = "expectedTotalValue")
    private double expectedTotalValue;
    @ColumnInfo(name = "differentTotalValue")
    private double differentTotalValue;
    @ColumnInfo(name = "createdAt")
    private Timestamp createdAt;
    @ColumnInfo(name = "opiningReportId")
    private long opiningReportId;


    public ClosingReport() {
    }

    public ClosingReport(@NonNull long closingReportId, double actualTotalValue, double expectedTotalValue, double differentTotalValue, Timestamp createdAt,long opiningReportId) {
        this.closingReportId = closingReportId;
        this.actualTotalValue = actualTotalValue;
        this.expectedTotalValue = expectedTotalValue;
        this.differentTotalValue = differentTotalValue;
        this.createdAt = createdAt;
        this.opiningReportId=opiningReportId;
    }

    @NonNull
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

    public void setClosingReportId(@NonNull long closingReportId) {
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

    @Override
    public Iterator<ClosingReport> iterator() {
        return null;
    }
}
