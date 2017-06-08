package com.pos.leaders.leaderspossystem.Models;

import com.pos.leaders.leaderspossystem.Tools.Util;

/**
 * Created by KARAM on 20/05/2017.
 */

public class Accounting {
    int id;
    int key;
    String name;
    int trialBalance;
    String trialBalanceDescription;
    int customerID;
    Customer customer;
    public double totalRequired, totalCredit;

    public Accounting() {
    }

    public Accounting(int id, int key, String name, int trialBalance, String trialBalanceDescription, Customer customer) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.trialBalance = trialBalance;
        this.trialBalanceDescription = trialBalanceDescription;
        this.customer = customer;
        this.totalRequired = 0;
        this.totalCredit = 0;
    }

    //region Getters
    public int getId() {
        return id;
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getTrialBalance() {
        return trialBalance;
    }

    public String getTrialBalanceDescription() {
        return trialBalanceDescription;
    }

    public int getCustomerID() {
        return customerID;
    }

    public Customer getCustomer() {
        return customer;
    }
    //endregion Getters

    //region Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrialBalance(int trialBalance) {
        this.trialBalance = trialBalance;
    }

    public void setTrialBalanceDescription(String trialBalanceDescription) {
        this.trialBalanceDescription = trialBalanceDescription;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    //endregion Setters

    public String BKMVDATA(int rowNumber,String companyID){
        String OP = "+", mOP = "-";
        trialBalanceDescription = "tbd" + key;
        return "B110" + String.format(Util.locale, "%09d", rowNumber) + companyID + String.format(Util.locale, "%15s", key)
                + String.format(Util.locale, "%50s", name) + String.format(Util.locale, "%15s", trialBalance)
                + String.format(Util.locale, "%30s", trialBalanceDescription) + String.format(Util.locale, "%50s", customer.getStreet())
                + String.format(Util.locale, "%10s", customer.getHouseNumber()) + String.format(Util.locale, "%30s", customer.getCity())
                + String.format(Util.locale, "%8s", customer.getPostalCode()) + String.format(Util.locale, "%30s", customer.getCountry())
                + String.format(Util.locale, "%2s", customer.getCountryCode()) + Util.spaces(15)
                + OP + Util.x12V99(0)
                + (totalRequired >= 0 ? OP : mOP) + Util.x12V99(totalRequired)
                + (totalCredit >= 0 ? OP : mOP) + Util.x12V99(totalCredit)
                + Util.spaces(4) + Util.spaces(9) + Util.spaces(7) + OP + Util.x12V99(0) + Util.spaces(3) + Util.spaces(16);


        /*
         String s = "320",OP="+";
        if(amount<0) {
            s = "330";
            OP = "-";
			//amount *= -1;
		}
        int totalItems = 0;
        double totalDiscount = 0;
        for(Order o: sale.getOrders())
        {
            totalItems += o.getCount();
            totalDiscount += (o.getItemTotalPrice() * o.getDiscount() / 100);
        }
        if(amount<0)
            totalDiscount = 0;
        totalItems = 1;
        double noTax = sale.getTotalPrice() / (1 + (SETTINGS.tax / 100));
        if(noTax<0)
            noTax *= -1;
        return "D110" + String.format(locale, "%09d", rowNumber) + companyID + s + String.format(locale, "%020d", saleId) + String.format(locale, "%04d", id) + s + String.format(locale, "%020d", saleId) + "3" + String.format(locale, "%020d", saleId) + String.format(locale, "%30s", "sale") + Util.spaces(50) + Util.spaces(30) +
                String.format(locale, "%20s", "unit")
                + "+" + String.format(locale, "%012d", totalItems) + String.format(locale, "%04d", (int) ((totalItems - Math.floor(totalItems) + 0.00001) * 10000))
                + OP + Util.x12V99(noTax)
                + OP + Util.x12V99(totalDiscount)
                + OP + Util.x12V99(noTax)
                + String.format(locale, "%02.0f", SETTINGS.tax) + String.format(locale, "%02d", (int) ((SETTINGS.tax - Math.floor(SETTINGS.tax) + 0.001) * 100))
                + Util.spaces(7) + DateConverter.getYYYYMMDD(date) + String.format(locale, "%07d", saleId) + Util.spaces(7) + Util.spaces(21);


         */
    }
}
