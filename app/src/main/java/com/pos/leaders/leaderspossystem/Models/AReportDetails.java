package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 12/3/2017.
 */

public class AReportDetails {
    private long aReportDetailsId;
    private long aReportId;
    private double amount;
    private long type;
    private double amount_in_basic_currency ;


    public AReportDetails() {
    }

    public AReportDetails(long aReportDetailsId, long aReportId, double amount, long type, double amount_in_basic_currency) {
        this.aReportDetailsId = aReportDetailsId;
        this.aReportId = aReportId;
        this.amount = amount;
        this.type = type;
        this.amount_in_basic_currency = amount_in_basic_currency;
    }

    public long getaReportDetailsId() {
        return aReportDetailsId;

    }

    public void setaReportDetailsId(long aReportDetailsId) {
        this.aReportDetailsId = aReportDetailsId;
    }

    public long getaReportId() {
        return aReportId;
    }

    public void setaReportId(long aReportId) {
        this.aReportId = aReportId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount_in_basic_currency() {
        return amount_in_basic_currency;
    }

    public void setAmount_in_basic_currency(double amount_in_basic_currency) {
        this.amount_in_basic_currency = amount_in_basic_currency;
    }
    public AReportDetails(AReportDetails a) {
        this(a.getaReportDetailsId(),a.getaReportId(),a.getAmount(),a.getType(),a.getAmount_in_basic_currency());
    }
}
