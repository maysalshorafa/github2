package com.pos.leaders.leaderspossystem.Pinpad.Credix;

import static com.pos.leaders.leaderspossystem.Pinpad.PinpadActivity.PAYMENTS_MIN_NUMBER;

/**
 * Created by KARAM on 27/05/2018.
 */

public class NumberOfPaymentException extends Exception {
    private int numberOfPayments;
    public NumberOfPaymentException(int numberOfPayments) {
        this.numberOfPayments = numberOfPayments;
    }

    @Override
    public String getMessage() {
        return "Not allowed number of payments "+numberOfPayments + " try to " + ((numberOfPayments < PAYMENTS_MIN_NUMBER) ? "increment" : "decrease")+" the number of payments";
    }
}
