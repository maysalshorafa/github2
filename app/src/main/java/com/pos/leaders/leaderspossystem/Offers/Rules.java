package com.pos.leaders.leaderspossystem.Offers;

/**
 * Created by Win8.1 on 7/31/2018.
 */

public enum Rules {
    product_sku("productSku"),
    quantity("quantity");

    Rules(final String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return value;
    }
}
