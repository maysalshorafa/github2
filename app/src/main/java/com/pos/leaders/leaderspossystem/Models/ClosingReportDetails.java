package com.pos.leaders.leaderspossystem.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Iterator;

/**
 * Created by Win8.1 on 11/12/2018.
 */
@Entity(tableName = "closing_report_details")
public class ClosingReportDetails implements Iterable<ClosingReportDetails> {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "closing_report_detailsId")
    private long closing_report_detailsId;
    @ColumnInfo(name = "closingReportId")
    private long closingReportId;
    @ColumnInfo(name = "actualValue")
    private double actualValue;
    @ColumnInfo(name = "expectedValue")
    private double expectedValue;
    @ColumnInfo(name = "differentValue")
    private double differentValue;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "currencyType")
    private String currencyType;

    @NonNull
    public long getClosing_report_details() {
        return closing_report_detailsId;
    }

    public long getClosingReportId() {
        return closingReportId;
    }

    public double getActualValue() {
        return actualValue;
    }

    public double getExpectedValue() {
        return expectedValue;
    }

    public double getDifferentValue() {
        return differentValue;
    }

    public String getType() {
        return type;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public ClosingReportDetails() {
    }

    public void setClosing_report_detailsId(@NonNull long closing_report_detailsId) {
        this.closing_report_detailsId = closing_report_detailsId;
    }

    public void setClosingReportId(long closingReportId) {
        this.closingReportId = closingReportId;
    }

    public void setActualValue(double actualValue) {
        this.actualValue = actualValue;
    }

    public void setExpectedValue(double expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void setDifferentValue(double differentValue) {
        this.differentValue = differentValue;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public ClosingReportDetails(@NonNull long closing_report_detailsId, long closingReportId, double actualValue, double expectedValue, double differentValue, String type, String currencyType) {
        this.closing_report_detailsId = closing_report_detailsId;
        this.closingReportId = closingReportId;
        this.actualValue = actualValue;
        this.expectedValue = expectedValue;
        this.differentValue = differentValue;
        this.type = type;
        this.currencyType = currencyType;
    }

    @Override
    public Iterator<ClosingReportDetails> iterator() {
        return null;
    }
}
