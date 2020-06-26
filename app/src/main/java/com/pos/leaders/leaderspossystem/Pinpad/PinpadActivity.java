package com.pos.leaders.leaderspossystem.Pinpad;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.credix.pinpad.jni.PinPadAPI;
import com.credix.pinpad.jni.PinPadResponse;
import com.credix.pinpad.jni.PinPadSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Pinpad.Credix.NumberOfPaymentException;
import com.pos.leaders.leaderspossystem.Pinpad.Credix.ResponseCode;
import com.pos.leaders.leaderspossystem.Pinpad.Credix.SendRequestType;
import com.pos.leaders.leaderspossystem.Pinpad.Credix.TransactionCode;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SalesCartActivity;
import com.pos.leaders.leaderspossystem.Tools.CreditCardTransactionType;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.pos.leaders.leaderspossystem.SettingsTab.PinpadTap.PINPAD_IP;
import static com.pos.leaders.leaderspossystem.SettingsTab.PinpadTap.PINPAD_PASSWORD;
import static com.pos.leaders.leaderspossystem.SettingsTab.PinpadTap.PINPAD_PREFERENCES;
import static com.pos.leaders.leaderspossystem.SettingsTab.PinpadTap.PINPAD_USERNAME;

public class PinpadActivity extends AppCompatActivity {

    public static final String LEADERS_POS_PIN_PAD_TOTAL_PRICE = "LEADERS_POS_PIN_PAD_TOTAL_PRICE";
    public static final String RESULT_INTENT_CODE_PIN_PAD_ACTIVITY_FULL_RESPONSE = "RESULT_INTENT_CODE_PIN_PAD_ACTIVITY_FULL_RESPONSE";

    private NumberPicker np;

    private Button btDone, btCancel, btByPhone;
    private TextView tvTotalPrice;

    int payments = 1;
    double totalPrice;

    SharedPreferences pinpadSP;
    public static String username = "", password = "", ip = "";

    public static final int PAYMENTS_MAX_NUMBER = 36;
    public static final int PAYMENTS_MIN_NUMBER = 1;
    boolean creditReceipt=false;
    JSONObject invoiceJson=new JSONObject();
    List<BoInvoice> invoice ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide activity title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pinpad);

        //set custom title bar
        TitleBar.setTitleBar(this);

        tvTotalPrice = (TextView) findViewById(R.id.pinpadActivity_tvTotalPrice);

        //check extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("creditReceipt")) {
                creditReceipt = true;
                totalPrice = (double) extras.get("_Price");
                tvTotalPrice.setText(Util.makePrice(totalPrice) + " " + SETTINGS.currencySymbol);
                invoice = (List<BoInvoice>) extras.get("invoice");
            } else {
                creditReceipt=false;
                totalPrice = (double) extras.get(LEADERS_POS_PIN_PAD_TOTAL_PRICE);
                tvTotalPrice.append(" " + Util.makePrice(totalPrice) + " " + SETTINGS.currencySymbol);
            }
        }else {
            finish();
        }
        pinpadSP = this.getSharedPreferences(PINPAD_PREFERENCES, 0);
        if (pinpadSP.contains(PINPAD_IP) || pinpadSP.contains(PINPAD_USERNAME)) {
            ip = (pinpadSP.getString(PINPAD_IP, ""));
            username = (pinpadSP.getString(PINPAD_USERNAME, ""));
            password = (pinpadSP.getString(PINPAD_PASSWORD, ""));
        }

        if (ip.equals("") || username.equals("") || password.equals("")) {
            Toast.makeText(this, "Please config your PinPad.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        //init Views
        np = (NumberPicker) findViewById(R.id.pinpadActivity_np);
        btCancel = (Button) findViewById(R.id.pinpadActivity_btCancel);
        btByPhone = (Button) findViewById(R.id.pinpadActivity_btByPhone);
        btDone = (Button) findViewById(R.id.pinpadActivity_btDone);

        np.setMaxValue(36);
        np.setMinValue(1);
        np.setValue(payments);

        //Done button
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payments = np.getValue();

                Transaction transaction = new Transaction();
                transaction.amount = (float) totalPrice;

                try {
                    transaction.setNumberOfPayments(payments);
                } catch (NumberOfPaymentException e) {
                    Log.e("PinPad", e.getMessage());
                    Toast.makeText(PinpadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                new PinPadTransaction().execute(transaction);
            }
        });

        //byPhone button
        btByPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            payments = np.getValue();

            Transaction transaction = new Transaction();
            transaction.amount = (float) totalPrice;
            transaction.printVoucher = false;
            transaction.transactionCode = TransactionCode.PHONE_TRANSACTION;
            transaction.cardNumber = "";
            transaction.ccv = "";
            transaction.cardHolderId = "";

            try {
                transaction.setNumberOfPayments(payments);
            } catch (NumberOfPaymentException e) {
                Log.e("PinPad", e.getMessage());
                Toast.makeText(PinpadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            new PinPadTransaction().execute(transaction);
            }
        });

        //Cancel button
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void successResponse(JSONObject jsonObject) {
        JSONObject jo = null;
        try {
            jo = jsonObject.getJSONObject("data");
        } catch (JSONException e) {/*ignore*/}

        if(creditReceipt){

            CreditCardPayment ccp = new CreditCardPayment();
            try {
                JSONObject tr = jsonObject.getJSONObject("transaction");

                ccp.setAmount(tr.getDouble("amount"));
                ccp.setAnswer(jo.toString());
                ccp.setTransactionId(tr.getString("uid"));
                ccp.setCreditCardCompanyName(tr.getString("cardBrand"));
                ccp.setLast4Digits(tr.getString("cardNumber"));
                ccp.setCardholder(tr.getString("cardHolderName"));
                ccp.setPaymentsNumber(0);
                ccp.setTransactionType(CreditCardTransactionType.NORMAL);

                if (tr.getInt("numberOfPayments") > 0) {
                    ccp.setPaymentsNumber(tr.getInt("numberOfPayments") + 1);
                    ccp.setFirstPaymentAmount(tr.getDouble("firstPaymentAmount"));
                    ccp.setOtherPaymentAmount(tr.getDouble("paymentAmount"));
                    ccp.setTransactionType(CreditCardTransactionType.PAYMENTS);
                }

                if (!tr.getString("transactionType2").equals("CHARGE")) {
                    ccp.setTransactionType(CreditCardTransactionType.CREDIT);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            SESSION._TEMP_CREDITCARD_PAYMNET=ccp;
          //  DocumentControl.sendReciptDoc(PinpadActivity.this,invoice, CONSTANT.CREDIT_CARD,totalPrice,"");

        }else {
            Intent i = new Intent();
            i.putExtra(RESULT_INTENT_CODE_PIN_PAD_ACTIVITY_FULL_RESPONSE, jo.toString());
            i.putExtra(SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE, totalPrice);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    private void failResponse(String message) {
        //make alert showing the error message
        new AlertDialog.Builder(this)
                .setTitle(R.string.transaction_error)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //on click go bvack to main screen with negative result
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .show();
    }

    private class PinPadTransaction extends AsyncTask<Transaction, Void, JSONObject> {

        private PinPadAPI pinPad;
        private Integer res;
        private ProgressDialog waitDialog;
        private static final int TIMEOUT = 1000;

        @Override
        protected void onPreExecute() {
            pinPad = new PinPadAPI();
            waitDialog = new ProgressDialog(PinpadActivity.this);
            waitDialog.setTitle(getString(R.string.pinpad_wait_for_finish));
            waitDialog.show();

            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Transaction... params) {
            Transaction transaction = params[0];

            pinPad.open(ip, username, password);

            PinPadSession requestSession = new PinPadSession();

            try {
                res = pinPad.sendRequest(requestSession, SendRequestType.TRANSACTION, transaction.make());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            for (int i = 0; i < 10; i++) {
                // Get transaction response
                res = pinPad.isResponseReady(requestSession, TIMEOUT);
                if (res > 0) {
                    PinPadResponse response = new PinPadResponse();
                    pinPad.getResponse(requestSession, response, res);
                    Log.d("getting response", response.get());
                    try {
                        return new JSONObject(response.get());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }

            if (res == 0) {
                // Cancel transaction
                PinPadSession cancelSession = new PinPadSession();
                res = pinPad.sendRequest(cancelSession, SendRequestType.CANCEL, null);

                // Get cancel response
                res = pinPad.isResponseReady(cancelSession, TIMEOUT);
                if (res > 0) {
                    PinPadResponse response = new PinPadResponse();
                    pinPad.getResponse(cancelSession, response, res);
                    JSONObject cancelResponse;
                    Log.d("cancel response", response.toString());
                    try {
                        cancelResponse = new JSONObject(response.get());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Get transaction response
            if (res > 0) {
                PinPadResponse response = new PinPadResponse();
                pinPad.getResponse(requestSession, response, res);
                Log.d("transaction response", response.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //close pinpad connection
            pinPad.close();
            //closing wait dialog
            waitDialog.dismiss();

            //reading response object
            if (jsonObject != null) {
                if (jsonObject.has("code")) {
                    int code = 0;
                    try {
                        code = jsonObject.getInt("code");
                        Log.d("response", jsonObject.toString());
                        switch (code) {
                            case ResponseCode.PP_SUCCESS:
                                //success response
                                successResponse(jsonObject);
                                return;
                            case ResponseCode.PP_INVALID_JSON_FORMAT:
                                failResponse(jsonObject.getString("msg"));
                                return;
                            case ResponseCode.PP_USER_ABORT_DETECTED:
                                failResponse(jsonObject.getString("msg"));
                                return;
                            case ResponseCode.PP_MISSING_MANDATORY_PARAMS:
                                failResponse(jsonObject.getString("msg"));
                                return;
                            case ResponseCode.PP_COMMAND_NOT_FOUND:
                                failResponse(jsonObject.getString("msg"));
                                return;
                            case ResponseCode.PP_COMMAND_NOT_ALLOWED:
                                failResponse(jsonObject.getString("msg"));
                                return;
                            case ResponseCode.PP_CONNECTION_FAILURE:
                                failResponse(jsonObject.getString("msg"));
                                return;
                            case ResponseCode.PP_CARD_AID_BLOCK:
                                failResponse(jsonObject.getString("msg"));
                                return;
                            case ResponseCode.PP_EMV_TRANSACTION_FAILED:
                                failResponse(jsonObject.getString("msg"));
                                return;
                            case ResponseCode.PP_SHVA_GENERAL_ERROR:
                                failResponse(jsonObject.getJSONObject("data").getString("message"));
                                return;
                            case ResponseCode.PP_INVALID_PARAMS:
                                failResponse(jsonObject.getString("msg"));
                                return;
                            case ResponseCode.PP_CREDIX_GENERAL_ERROR:
                                failResponse(jsonObject.getString("msg"));
                                return;
                            case ResponseCode.PP_PROTOCOL_ERROR:
                                failResponse(jsonObject.getJSONObject("data").getString("message"));
                                return;
                        }
                    } catch (JSONException e) {/*ignore this exception*/}
                }
            } else {
                //connection error with pin-pad
                failResponse(getString(R.string.pinpad_connection_error));
            }
            super.onPostExecute(jsonObject);
        }
    }
}