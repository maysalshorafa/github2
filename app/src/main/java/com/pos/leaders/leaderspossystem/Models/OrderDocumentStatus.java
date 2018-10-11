package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/9/2018.
 */

public enum OrderDocumentStatus {
    DRAFT ("draft "),
    PENDING("pending"),
    READY("ready"),
    IMPLEMENTED("implemented")
    ,CLOSED("closed"),
    CANCELED("canceled");
    OrderDocumentStatus(final String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return value;
    }
}
