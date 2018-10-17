package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 10/17/2018.
 */

public class Invoice {
    private long id;
   private String invoiceId;



    private long customerID;

    public Invoice() {
    }

    public Invoice(long id, String invoiceId,long customerID) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.customerID=customerID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvoiceId() {
        return invoiceId;
    }
    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", invoiceId='" + invoiceId + '\'' +
                ", customerID=" + customerID +
                '}';
    }
}
