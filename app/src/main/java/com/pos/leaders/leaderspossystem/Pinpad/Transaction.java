package com.pos.leaders.leaderspossystem.Pinpad;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.Pinpad.Credix.CreditTerms;
import com.pos.leaders.leaderspossystem.Pinpad.Credix.Currency;
import com.pos.leaders.leaderspossystem.Pinpad.Credix.NumberOfPaymentException;
import com.pos.leaders.leaderspossystem.Pinpad.Credix.RequestType;
import com.pos.leaders.leaderspossystem.Pinpad.Credix.TransactionType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static com.pos.leaders.leaderspossystem.Pinpad.PinpadActivity.PAYMENTS_MAX_NUMBER;
import static com.pos.leaders.leaderspossystem.Pinpad.PinpadActivity.PAYMENTS_MIN_NUMBER;

/**
 * Created by KARAM on 24/05/2018.
 */

public class Transaction {
    float amount;
    String creditTerms = CreditTerms.REGULAR_CREDIT;
    String currency = Currency.ILS;
    boolean printVoucher = false;
    String requestType = RequestType.QUERY_EXECUTION_IN_ACCORDANCE_WITH_TERMINAL;
    String transactionType = TransactionType.CHARGE;

    float firstPaymentAmount;
    float paymentAmount;
    float numberOfPayments;

    String comments;//255 characters

    //// TODO: 27/05/2018 ask about this attribute and how to use it.
    String transactionCode;// = "REGULAR_TRANSACTION"; //REGULAR_TRANSACTION or PHONE_TRANSACTION or SIGNATURE_ONLY_TRANSACTION

    //by phone transaction
    String cardNumber;
    String cardExpirationDate;//YYMM
    String cardHolderName;
    String cardHolderId;
    String ccv;//up to 4 digits

/*
    public void setAsPhoneTransaction(){
        transactionCode = "PHONE_TRANSACTION";
    }
    public void setAsNormalTransaction(){
        transactionCode = "REGULAR_TRANSACTION";
    }
    public boolean isNormalTransaction(){
        return transactionCode.equals("REGULAR_TRANSACTION");
    }
*/
    public String make() throws JsonProcessingException, JSONException {
        JSONObject jsonObject = new JSONObject();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonInString = objectMapper.writeValueAsString(this);
        JSONObject data = new JSONObject(jsonInString);
        jsonObject.put("params",data);

        return jsonObject.toString();
    }

    public void setCreditTerms(String creditTerms) {
        this.creditTerms = creditTerms;

        switch (creditTerms) {
            case CreditTerms.REGULAR_CREDIT:
                break;
            case CreditTerms.CREDIT_PLUS30:
                break;
            case CreditTerms.FIXED_INSTALMENT_CREDIT:
                break;
            case CreditTerms.IMMEDIATE_CHARGE:
                break;
            case CreditTerms.PAYMENTS:
                break;
        }

    }


    public void setNumberOfPayments(int payments) throws NumberOfPaymentException {

        if (payments < PAYMENTS_MIN_NUMBER || payments > PAYMENTS_MAX_NUMBER){
            throw new NumberOfPaymentException(payments);
        }

        if (payments == 1) {
            this.creditTerms = CreditTerms.REGULAR_CREDIT;
            this.numberOfPayments = 1;
            return;
        }

        float totalPrice = Float.parseFloat(String.format(Locale.ENGLISH, "%.2f", amount));
        float fixedPayment = Float.parseFloat(String.format(Locale.ENGLISH, "%.2f", totalPrice / payments));
        float firstPayment = Float.parseFloat(String.format(Locale.ENGLISH, "%.2f", totalPrice - (fixedPayment * (payments - 1))));

        this.amount = totalPrice;
        Log.i("total price", totalPrice + "");
        this.firstPaymentAmount = firstPayment;
        Log.i("first payment", firstPayment + "");
        this.paymentAmount = fixedPayment;
        Log.i("fixed payment", fixedPayment + "");

        this.numberOfPayments = payments;
        Log.i("number of payments payment", payments + "");

        if (payments > 1) {
            this.creditTerms = CreditTerms.PAYMENTS;
            this.numberOfPayments--;
        } else if (payments == 1) {
            this.creditTerms = CreditTerms.REGULAR_CREDIT;
        }

    }

}
