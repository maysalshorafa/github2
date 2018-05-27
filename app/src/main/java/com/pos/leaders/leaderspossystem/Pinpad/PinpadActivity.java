package com.pos.leaders.leaderspossystem.Pinpad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.pos.leaders.leaderspossystem.CreditCard.MainCreditCardActivity;
import com.pos.leaders.leaderspossystem.Pinpad.Credix.NumberOfPaymentException;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.json.JSONException;
import org.json.JSONObject;

import static com.pos.leaders.leaderspossystem.SettingsTab.PinpadTap.PINPAD_IP;
import static com.pos.leaders.leaderspossystem.SettingsTab.PinpadTap.PINPAD_PASSWORD;
import static com.pos.leaders.leaderspossystem.SettingsTab.PinpadTap.PINPAD_PREFERENCES;
import static com.pos.leaders.leaderspossystem.SettingsTab.PinpadTap.PINPAD_USERNAME;

public class PinpadActivity extends AppCompatActivity {

    public static final String LEADERS_POS_PIN_PAD_TOTAL_PRICE = "LEADERS_POS_PIN_PAD_TOTAL_PRICE";
    public static final String RESULT_INTENT_CODE_PIN_PAD_ACTIVITY_FULL_RESPONSE = "RESULT_INTENT_CODE_PIN_PAD_ACTIVITY_FULL_RESPONSE";

    private NumberPicker np;

    private Button btDone, btCancel, btAdvance;
    private TextView tvTotalPrice;

    int payments = 1;
    double totalPrice;

    SharedPreferences pinpadSP;
    String username = "", password = "", ip = "";

    public static final int PAYMENTS_MAX_NUMBER = 36;
    public static final int PAYMENTS_MIN_NUMBER = 1;


    JSONObject responseJSON;

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
            totalPrice = (double) extras.get(LEADERS_POS_PIN_PAD_TOTAL_PRICE);
            tvTotalPrice.append(" "+Util.makePrice(totalPrice) + " " + getResources().getText(R.string.ins));
        } else {
            finish();
        }


        pinpadSP = this.getSharedPreferences(PINPAD_PREFERENCES, 0);
        if (pinpadSP.contains(PINPAD_IP) || pinpadSP.contains(PINPAD_USERNAME)) {
            ip = (pinpadSP.getString(PINPAD_IP, ""));
            username = (pinpadSP.getString(PINPAD_USERNAME, ""));
            password = (pinpadSP.getString(PINPAD_PASSWORD, ""));
        }

        if(ip.equals("")||username.equals("")||password.equals("")) {
            Toast.makeText(this, "Please config your PinPad.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }


        //init Views
        np = (NumberPicker) findViewById(R.id.pinpadActivity_np);
        btCancel = (Button) findViewById(R.id.pinpadActivity_btCancel);
        btAdvance = (Button) findViewById(R.id.pinpadActivity_btAdvance);
        btDone = (Button) findViewById(R.id.pinpadActivity_btDone);

        np.setMaxValue(36);
        np.setMinValue(1);
        np.setValue(payments);


        //Done button
        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payments = np.getValue();

                PinPadAPI pinpad = new PinPadAPI();
                Integer res;

                pinpad.open(ip, username, password);

                PinPadSession requestSession = new PinPadSession();
                Transaction transaction = new Transaction();
                transaction.amount = (float)totalPrice;

                try {
                    transaction.setNumberOfPayments(payments);
                } catch (NumberOfPaymentException e) {
                    Log.e("PinPad", e.getMessage());
                    Toast.makeText(PinpadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Log.i("Request", transaction.make());
                    res = pinpad.sendRequest(requestSession, "transaction", transaction.make());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    Toast.makeText(PinpadActivity.this, "You have an error", Toast.LENGTH_SHORT).show();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PinpadActivity.this, "You have an error", Toast.LENGTH_SHORT).show();
                    return;
                }


                final ProgressDialog dialog_connection = new ProgressDialog(PinpadActivity.this);
                dialog_connection.setTitle(getBaseContext().getString(R.string.wait_for_accept));
                dialog_connection.show();



                for (int i = 0; i < 10; i++) {
                    // Get transaction response
                    res = pinpad.isResponseReady(requestSession, 1000);
                    if (res > 0) {
                        PinPadResponse response = new PinPadResponse();
                        pinpad.getResponse(requestSession, response, res);
                        Log.e("response", response.get());

                        try {
                            responseJSON = new JSONObject(response.get());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pinpad.close();
                            dialog_connection.dismiss();
                            return;
                        }

                        if (responseJSON.has("code")) {
                            int code = 0;
                            try {
                                code = responseJSON.getInt("code");
                            } catch (JSONException e){/*ignore this exception*/}

                            switch (code) {
                                case 60000:
                                    //success response
                                    pinpad.close();
                                    successResponse(responseJSON);
                                    dialog_connection.dismiss();
                                    return;
                                case 60001:
                                    pinpad.close();
                                    dialog_connection.dismiss();
                                    return;

                                case 60002:
                                    pinpad.close();
                                    dialog_connection.dismiss();
                                    return;
                            }
                        }

                        /************** response example **************//*
                        {"code":60002,"msg":"עסקה לא הושלמה"}
                        {"code":60000,"data":{"receipt":{"aid":{"category":"BOTH","index":21,"name":"זיהוי יישום בשבב","value":""},"appVersion":{"category":"BOTH","index":3,"name":"גרסת תוכנה","value":"ABS001617i"},"arc":{"category":"SELLER","index":15,"name":"ARC","value":""},"atc":{"category":"BOTH","index":12,"name":"סידורי ש","value":""},"authCodeManpik":{"category":"BOTH","index":19,"name":"גורם מאשר","value":""},"authNo":{"category":"BOTH","index":18,"name":"מספר אישור","value":""},"cardName":{"category":"BOTH","index":6,"name":"שם כרטיס","value":"ישראכרט"},"cardNumber":{"category":"SELLER","index":8,"name":"מספר כרטיס","value":"0136058817"},"cardSeqNumber":{"category":"BOTH","index":13,"name":"סידורי כ","value":""},"cashbackAmount":{"category":"BOTH","index":38,"name":"סכום במזומן","value":""},"clientCardNumber":{"category":"CLIENT","index":7,"name":"מספר כרטיס","value":"8817"},"clientPhone":{"category":"SELLER","index":42,"name":"מספר טלפון של לקוח","value":"\\r\\n____________________"},"clientSignature":{"category":"SELLER","index":41,"name":"חתימת לקוח","value":"\\r\\n____________________"},"compRetailerNum":{"category":"BOTH","index":4,"name":"מספר עסק בחברת האשראי","value":"0071506"},"constMsg":{"category":"BOTH","index":40,"name":"","value":""},"conversionAmount":{"category":"BOTH","index":25,"name":"סכום העסקה לאחר המרה","value":""},"conversionCurrency":{"category":"BOTH","index":26,"name":"מטבע ההמרה","value":""},"conversionProvider":{"category":"BOTH","index":28,"name":"גורם המרה","value":""},"conversionRate":{"category":"BOTH","index":27,"name":"שער המרה","value":""},"creditTerms":{"category":"BOTH","index":22,"name":"סוג אשראי","value":"רגיל"},"currency":{"category":"BOTH","index":24,"name":"מטבע","value":"ש''ח"},"deferMonths":{"category":"BOTH","index":32,"name":"דחוי","value":""},"dspBalance":{"category":"CLIENT","index":43,"name":"","value":""},"dspF111":{"category":"BOTH","index":44,"name":"","value":""},"dueDate":{"category":"BOTH","index":33,"name":"חיוב במועד","value":""},"firstPayment":{"category":"BOTH","index":29,"name":"תשלום ראשון","value":""},"ipayAmount":{"category":"BOTH","index":36,"name":"סכום הנחה בקניה","value":""},"ipayNumber":{"category":"BOTH","index":37,"name":"מספר יחידות הטבה","value":""},"lumpSum":{"category":"BOTH","index":39,"name":"סה''כ כולל תשר ומזומן","value":""},"netAmount":{"category":"BOTH","index":35,"name":"סכום נטו","value":""},"noPayments":{"category":"BOTH","index":30,"name":"מספר תשלומים","value":""},"notFirstPayment":{"category":"BOTH","index":31,"name":"תשלום נוסף","value":""},"panEntryMode":{"category":"BOTH","index":20,"name":"אופן ביצוע העסקה","value":"מגנטי"},"rrn":{"category":"BOTH","index":11,"name":"RRN","value":""},"terminalName":{"category":"BOTH","index":1,"name":"שם מסוף","value":"מסוף טסט דנגוט"},"terminalNumber":{"category":"BOTH","index":2,"name":"מספר מסוף","value":"0882214"},"tip":{"category":"BOTH","index":34,"name":"תשר","value":""},"tranType":{"category":"BOTH","index":17,"name":"סוג עסקה","value":"חובה"},"transAmount":{"category":"BOTH","index":23,"name":"סכום העסקה","value":"1.50"},"transDateTime":{"category":"BOTH","index":5,"name":"תאריך ושעת העסקה","value":"23\/05\/18 16:43"},"tsi":{"category":"SELLER","index":14,"name":"TSI","value":""},"tvr":{"category":"SELLER","index":16,"name":"TVR","value":""},"uid":{"category":"BOTH","index":10,"name":"UID","value":"18052316432608822141749"},"voucherNumber":{"category":"BOTH","index":9,"name":"מספר שובר","value":"06001001"}},"transaction":{"aid":"","amount":1.5,"approvalNumber":"","approvalSource":"INVALID","arc":"Y1","atc":"","authCodeManpik":"INVALID","authCodeSolek":"INVALID","businessId":2538,"cardBrand":"ISRACAR
                        {"code":60012,"msg":"Invalid Parameter: 'cardExpirationDate'"}
                        {"code":60011,"data":{"code":406,"message":"סה''כ סכום העסקה שונה מסכום תשלום ראשון + סכום תשלום קבוע * מספר תשלומים","solek":"לא תקין"},"msg":"Declined"}
                         */
                        break;
                    }
                }


                if (res == 0) {
                    // Cancel transaction
                    PinPadSession cancelSession = new PinPadSession();
                    res = pinpad.sendRequest(cancelSession, "cancel", null);

                    // Get cancel response
                    res = pinpad.isResponseReady(cancelSession, 1000);
                    if (res > 0) {
                        PinPadResponse response = new PinPadResponse();
                        pinpad.getResponse(cancelSession, response, res);
                        Log.e("res can", response.toString());
                        try {
                            responseJSON = new JSONObject(response.get());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    res = pinpad.isResponseReady(requestSession, 1000);
                    pinpad.close();
                    dialog_connection.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                }

                // Get transaction response
                if (res > 0) {
                    PinPadResponse response = new PinPadResponse();
                    pinpad.getResponse(requestSession, response, res);
                    Log.e("Response ", response.toString());
                }

                pinpad.close();
                dialog_connection.dismiss();
            }
        });

        //Advance button
        btAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PinpadActivity.this, "Advance mode is under construction.", Toast.LENGTH_SHORT).show();

            }
        });

        //Cancel button
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void successResponse(JSONObject jsonObject) {
        JSONObject jo = null;
        try {
            jo = jsonObject.getJSONObject("data");
        } catch (JSONException e) {/*ignore*/}


        Intent i = new Intent();
        i.putExtra(RESULT_INTENT_CODE_PIN_PAD_ACTIVITY_FULL_RESPONSE, jo.toString());

        setResult(RESULT_OK, i);
        finish();
    }


}
