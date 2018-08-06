package com.pos.leaders.leaderspossystem.Offers;

/**
 * Created by Win8.1 on 7/31/2018.
 */

public enum Operation {
    And("And"),
    OR("OR");

    Operation(final String val) {
        this.val = val;
    }
    private String val;
    public String getVal() {
        return val;
    }
}
