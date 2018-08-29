package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 8/19/2018.
 */

public enum WalletStatus {
    ACTIVE ("Active"),
    DELETED ("deleted "),
    PENDING("pending"),
    LOCKED ("locked ");
    private String value;
    public String getValue() {
        return value;
    }
    WalletStatus(final String value) {
        this.value = value;
    }
}
