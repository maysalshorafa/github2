package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 6/25/2019.
 */

public class PosSetting {
    private long posSettingId;
    private boolean enableCurrency;
    private boolean enableDuplicateInvoice;
    private boolean enableCreditCard;
    private boolean enablePinPad;
    private boolean enableCustomerMeasurement;
    private int noOfFloatPoint;
    private String printerType;
    private String posVersionNo;
    private String posDbVersionNo;
    private int branchId;
    private  String companyStatus;
    private String currencyCode;
    private String currencySymbol;
    private String country;
    private String customerEmail;
    private String customerEmailPassword;


    public PosSetting() {
    }

    public PosSetting(long posSettingId, boolean enableCurrency, boolean enableCreditCard, boolean enablePinPad, boolean enableCustomerMeasurement, int noOfFloatPoint, String printerType,String companyStatus, String posVersionNo, String posDbVersionNo, int branchId,String currencyCode,String currencySymbol,String country
    ,boolean enableDuplicateInvoice,String customerEmail,String customerEmailPassword) {
        this.posSettingId = posSettingId;
        this.enableCurrency = enableCurrency;
        this.enableCreditCard = enableCreditCard;
        this.enablePinPad = enablePinPad;
        this.enableCustomerMeasurement = enableCustomerMeasurement;
        this.noOfFloatPoint = noOfFloatPoint;
        this.printerType = printerType;
        this.companyStatus=companyStatus;
        this.posVersionNo = posVersionNo;
        this.posDbVersionNo = posDbVersionNo;
        this.branchId = branchId;
        this.currencyCode=currencyCode;
        this.currencySymbol=currencySymbol;
        this.country=country;
        this.enableDuplicateInvoice=enableDuplicateInvoice;
        this.customerEmail=customerEmail;
        this.customerEmailPassword=customerEmailPassword;

    }
    public PosSetting( boolean enableCurrency, boolean enableCreditCard, boolean enablePinPad, boolean enableCustomerMeasurement, int noOfFloatPoint, String printerType,String companyStatus, String posVersionNo, String posDbVersionNo, int branchId,String customerEmail,String customerEmailPassword) {
        this.enableCurrency = enableCurrency;
        this.enableCreditCard = enableCreditCard;
        this.enablePinPad = enablePinPad;
        this.enableCustomerMeasurement = enableCustomerMeasurement;
        this.noOfFloatPoint = noOfFloatPoint;
        this.printerType = printerType;
        this.companyStatus=companyStatus;
        this.posVersionNo = posVersionNo;
        this.posDbVersionNo = posDbVersionNo;
        this.branchId = branchId;
        this.customerEmail=customerEmail;
        this.customerEmailPassword=customerEmailPassword;

    }

    public String getCustomerEmailPassword() {
        return customerEmailPassword;
    }

    public void setCustomerEmailPassword(String customerEmailPassword) {
        this.customerEmailPassword = customerEmailPassword;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public long getPosSettingId() {
        return posSettingId;
    }

    public void setPosSettingId(long posSettingId) {
        this.posSettingId = posSettingId;
    }

    public boolean isEnableCurrency() {
        return enableCurrency;
    }

    public void setEnableCurrency(boolean enableCurrency) {
        this.enableCurrency = enableCurrency;
    }

    public boolean isEnableCreditCard() {
        return enableCreditCard;
    }

    public void setEnableCreditCard(boolean enableCreditCard) {
        this.enableCreditCard = enableCreditCard;
    }

    public boolean isEnablePinPad() {
        return enablePinPad;
    }

    public void setEnablePinPad(boolean enablePinPad) {
        this.enablePinPad = enablePinPad;
    }

    public boolean isEnableCustomerMeasurement() {
        return enableCustomerMeasurement;
    }

    public void setEnableCustomerMeasurement(boolean enableCustomerMeasurement) {
        this.enableCustomerMeasurement = enableCustomerMeasurement;
    }

    public int getNoOfFloatPoint() {
        return noOfFloatPoint;
    }

    public void setNoOfFloatPoint(int noOfFloatPoint) {
        this.noOfFloatPoint = noOfFloatPoint;
    }

    public String getPrinterType() {
        return printerType;
    }

    public void setPrinterType(String printerType) {
        this.printerType = printerType;
    }

    public String getPosVersionNo() {
        return posVersionNo;
    }

    public void setPosVersionNo(String posVersionNo) {
        this.posVersionNo = posVersionNo;
    }

    public String getPosDbVersionNo() {
        return posDbVersionNo;
    }

    public void setPosDbVersionNo(String posDbVersionNo) {
        this.posDbVersionNo = posDbVersionNo;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getCompanyStatus() {
        return companyStatus;
    }

    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isEnableDuplicateInvoice() {
        return enableDuplicateInvoice;
    }

    public void setEnableDuplicateInvoice(boolean enableDuplicateInvoice) {
        this.enableDuplicateInvoice = enableDuplicateInvoice;
    }

    @Override
    public String toString() {
        return "PosSetting{" +
                "posSettingId=" + posSettingId +
                ", enableCurrency=" + enableCurrency +
                ", enableDuplicateInvoice=" + enableDuplicateInvoice +
                ", enableCreditCard=" + enableCreditCard +
                ", enablePinPad=" + enablePinPad +
                ", enableCustomerMeasurement=" + enableCustomerMeasurement +
                ", noOfFloatPoint=" + noOfFloatPoint +
                ", printerType='" + printerType + '\'' +
                ", posVersionNo='" + posVersionNo + '\'' +
                ", posDbVersionNo='" + posDbVersionNo + '\'' +
                ", branchId=" + branchId +
                ", companyStatus='" + companyStatus + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                ", country='" + country + '\'' +

                        ", customerEmail='" + customerEmail + '\'' +


                        ", customerEmailPassword='" + customerEmailPassword + '\'' +
                '}';
    }
}
