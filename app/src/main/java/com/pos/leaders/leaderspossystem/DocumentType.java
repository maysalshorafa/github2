package com.pos.leaders.leaderspossystem;

public enum DocumentType {
    INVOICE ("invoice "),
    RECEIPT("receipt"),
    CREDIT_INVOICE("creditInvoice"),
    INVOICE_RECEIPT("invoiceReceipt"),ORDER_DOCUMENT("orderDocument"),IN_INVENTORY("inInventory"),
    OUT_INVENTORY("outInventory");;

    DocumentType(final String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return value;
    }

}
