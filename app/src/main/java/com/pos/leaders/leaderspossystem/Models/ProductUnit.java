package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 8/19/2018.
 */
public enum ProductUnit {
    QUANTITY("QUANTITY"),
    WEIGHT("WEIGHT"),
    LENGTH("LENGTH"),
    BARCODEWITHWEIGHT("BARCODEWITHWEIGHT")
    ,BARCODEWITHPRICE("BARCODEWITHPRICE");
    private String value;
    public String getValue() {
        return value;
    }
    ProductUnit(final String value) {
        this.value = value;
    }
}
