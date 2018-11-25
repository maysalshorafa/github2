package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 12/3/2017.
 */

public class OpiningReportDetails {
    private long opiningReportDetailsId;
    private long opiningReportId;
    private double amount;
    private long type;
    private double amount_in_basic_currency ;
    //{\"amount\":888.0,\"type\":0,\"amount_in_basic_currency\":888.0,\"hide\":false,\"areportDetailsId\":\"40000000000000000\",\"areportId\":40000000000000000}

    public OpiningReportDetails() {
    }

    public OpiningReportDetails(long aReportDetailsId, long opiningReportId, double amount, long type, double amount_in_basic_currency) {
        this.opiningReportDetailsId = aReportDetailsId;
        this.opiningReportId = opiningReportId;
        this.amount = amount;
        this.type = type;
        this.amount_in_basic_currency = amount_in_basic_currency;
    }

    public long getOpiningReportDetailsId() {
        return opiningReportDetailsId;

    }

    public void setOpiningReportDetailsId(long opiningReportDetailsId) {
        this.opiningReportDetailsId = opiningReportDetailsId;
    }

    public long getOpiningReportId() {
        return opiningReportId;
    }

    public void setOpiningReportId(long opiningReportId) {
        this.opiningReportId = opiningReportId;
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
    public OpiningReportDetails(OpiningReportDetails a) {
        this(a.getOpiningReportDetailsId(),a.getOpiningReportId(),a.getAmount(),a.getType(),a.getAmount_in_basic_currency());
    }
}
