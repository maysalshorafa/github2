package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 11/12/2018.
 */
public class ClosingReportDetails{
    private long closingReportDetailsId;
    private long closingReportId;
    private double actualValue;
    private double expectedValue;
    private double differentValue;
    private String type;
    private String currencyType;

    public ClosingReportDetails() {
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

    public ClosingReportDetails(long closingReportDetailsId, long closingReportId, double actualValue, double expectedValue, double differentValue, String type, String currencyType) {
        this.closingReportDetailsId = closingReportDetailsId;
        this.closingReportId = closingReportId;
        this.actualValue = actualValue;
        this.expectedValue = expectedValue;
        this.differentValue = differentValue;
        this.type = type;
        this.currencyType = currencyType;
    }

    public long getClosingReportDetailsId() {
        return closingReportDetailsId;
    }

    public void setClosingReportDetailsId(long closingReportDetailsId) {
        this.closingReportDetailsId = closingReportDetailsId;
    }

    @Override
    public String toString() {
        return "ClosingReportDetails{" +
                "closing_report_detailsId=" + closingReportDetailsId +
                ", closingReportId=" + closingReportId +
                ", actualValue=" + actualValue +
                ", expectedValue=" + expectedValue +
                ", differentValue=" + differentValue +
                ", type='" + type + '\'' +
                ", currencyType='" + currencyType + '\'' +
                '}';
    }
}
