package com.pos.leaders.leaderspossystem.Offers;

/**
 * Created by Win8.1 on 7/31/2018.
 */

public enum Rules {
    RULES("rules"),
    product_sku("productSku"),
    quantity("quantity"),
    category("category"),
    categoryList("categoryList"),offerCategoryList("offerCategoryList");

    Rules(final String value) {
        this.value = value;
    }
    private String value;
    public String getValue() {
        return value;
    }
}
