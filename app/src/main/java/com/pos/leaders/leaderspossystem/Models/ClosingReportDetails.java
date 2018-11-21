package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 11/12/2018.
 */
public class ClosingReportDetails{
    private long closing_report_detailsId;
    private long closingReportId;
    private double actualValue;
    private double expectedValue;
    private double differentValue;
    private String type;
    private String currencyType;

    public ClosingReportDetails() {
    }

    public long getClosing_report_detailsId() {
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


    public void setClosing_report_detailsId(long closing_report_detailsId) {
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

    public ClosingReportDetails(long closing_report_detailsId, long closingReportId, double actualValue, double expectedValue, double differentValue, String type, String currencyType) {
        this.closing_report_detailsId = closing_report_detailsId;
        this.closingReportId = closingReportId;
        this.actualValue = actualValue;
        this.expectedValue = expectedValue;
        this.differentValue = differentValue;
        this.type = type;
        this.currencyType = currencyType;
    }

}
