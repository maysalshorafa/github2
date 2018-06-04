package com.pos.leaders.leaderspossystem.Models.Currency;

/**
 * Created by Win8.1 on 9/25/2017.
 */

public class CurrencyType {

    private long currencyTypeId;
    private String type;

    public CurrencyType() {
    }

    public CurrencyType(long currencyTypeId, String type) {
        this.currencyTypeId = currencyTypeId;
        this.type = type;
    }

    public long getCurrencyTypeId() {
        return currencyTypeId;
    }

    public void setCurrencyTypeId(long currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
