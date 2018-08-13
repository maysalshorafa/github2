package com.pos.leaders.leaderspossystem;

import java.util.HashMap;
import java.util.Map;

public enum DocumentType {
    INVOICE_RECIPE("INVOICE_RECIPE");

    DocumentType(final String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return value;
    }

}
