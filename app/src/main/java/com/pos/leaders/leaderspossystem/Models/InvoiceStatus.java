package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 8/29/2018.
 */

public enum InvoiceStatus{
    DRAFT ("draft "),
    SENT("sent"),
    VIEWED("viewed"),
    PARTIALLY_PAID("partially_paid")
    ,PAID("paid"),
    UNPAID("unpaid"),
    OVERDUE("overdue");
    InvoiceStatus(final String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return value;
    }
}