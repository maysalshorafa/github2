package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 12/10/2018.
 */

public class PosInvoice {
    private long id;
    private double amount;
    private long lastZReportId;

    public PosInvoice() {
    }

    public PosInvoice(long id, double amount, long lastZReportId) {
        this.id = id;
        this.amount = amount;
        this.lastZReportId = lastZReportId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getLastZReportId() {
        return lastZReportId;
    }

    public void setLastZReportId(long lastZReportId) {
        this.lastZReportId = lastZReportId;
    }
}
