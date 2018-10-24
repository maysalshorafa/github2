package com.pos.leaders.leaderspossystem.Tools;

/**
 * Created by Win8.1 on 10/24/2018.
 */

public enum CreditInvoiceStatus {
    OPEN("open"),INPROGRESS("InProgress"),
    DONE("done");
    CreditInvoiceStatus(final String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return value;
    }
}
