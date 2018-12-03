package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/16/2018.
 */

public enum CustomerType {
    NORMAL ("normal"),
    CREDIT("credit");
    private String value;
    public String getValue() {
        return value;
    }
    CustomerType(final String value) {
        this.value = value;
    }
}
