package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 6/25/2019.
 */

public class PosSetting {
    private long posSettingId;
    private boolean enableCurrency;
    private boolean enableCreditCard;
    private boolean enablePinPad;
    private boolean enableCustomerMeasurement;
    private int noOfFloatPoint;
    private String printerType;
    private String posVersionNo;
    private String posDbVersionNo;
    private int branchId;

    public PosSetting() {
    }

    public PosSetting(long posSettingId, boolean enableCurrency, boolean enableCreditCard, boolean enablePinPad, boolean enableCustomerMeasurement, int noOfFloatPoint, String printerType, String posVersionNo, String posDbVersionNo, int branchId) {
        this.posSettingId = posSettingId;
        this.enableCurrency = enableCurrency;
        this.enableCreditCard = enableCreditCard;
        this.enablePinPad = enablePinPad;
        this.enableCustomerMeasurement = enableCustomerMeasurement;
        this.noOfFloatPoint = noOfFloatPoint;
        this.printerType = printerType;
        this.posVersionNo = posVersionNo;
        this.posDbVersionNo = posDbVersionNo;
        this.branchId = branchId;
    }
    public PosSetting( boolean enableCurrency, boolean enableCreditCard, boolean enablePinPad, boolean enableCustomerMeasurement, int noOfFloatPoint, String printerType, String posVersionNo, String posDbVersionNo, int branchId) {
        this.enableCurrency = enableCurrency;
        this.enableCreditCard = enableCreditCard;
        this.enablePinPad = enablePinPad;
        this.enableCustomerMeasurement = enableCustomerMeasurement;
        this.noOfFloatPoint = noOfFloatPoint;
        this.printerType = printerType;
        this.posVersionNo = posVersionNo;
        this.posDbVersionNo = posDbVersionNo;
        this.branchId = branchId;
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

    @Override
    public String toString() {
        return "PosSetting{" +
                "posSettingId=" + posSettingId +
                ", enableCurrency=" + enableCurrency +
                ", enableCreditCard=" + enableCreditCard +
                ", enablePinPad=" + enablePinPad +
                ", enableCustomerMeasurement=" + enableCustomerMeasurement +
                ", noOfFloatPoint=" + noOfFloatPoint +
                ", printerType='" + printerType + '\'' +
                ", posVersionNo='" + posVersionNo + '\'' +
                ", posDbVersionNo='" + posDbVersionNo + '\'' +
                ", branchId=" + branchId +
                '}';
    }
}
