package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by KARAM on 11/07/2018.
 */

public enum ProductStatus {
    DRAFT("DRAFT"),
    PENDING("PENDING"),
    PRIVATE("PRIVATE"),
    PUBLISHED("PUBLISHED");

    private String value;
    public String getValue() {
        return value;
    }
    ProductStatus(final String value) {
        this.value = value;
    }
}
