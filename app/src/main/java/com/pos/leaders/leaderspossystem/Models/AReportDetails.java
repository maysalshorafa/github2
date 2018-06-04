package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 12/3/2017.
 */

public class AReportDetails {
    private long aReportDetailsId;
    private long a_report_id;
    private double amount;
    private long type;
    private double amount_in_basic_currency ;


    public AReportDetails() {
    }

    public AReportDetails(long aReportDetailsId, long a_report_id, double amount, long type, double amount_in_basic_currency) {
        this.aReportDetailsId = aReportDetailsId;
        this.a_report_id = a_report_id;
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

    public long getA_report_id() {
        return a_report_id;
    }

    public void setA_report_id(long a_report_id) {
        this.a_report_id = a_report_id;
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
        this(a.getaReportDetailsId(),a.getA_report_id(),a.getAmount(),a.getType(),a.getAmount_in_basic_currency());
    }
}
