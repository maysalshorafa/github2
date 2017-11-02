package com.pos.leaders.leaderspossystem.CreditCard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by KARAM on 10/12/2016.
 */

public class CreditCardActivity extends AppCompatActivity {
    EditText etNumberOfPayments;
    TextView tvTotalPrice , tvCustomerName;
    Button btOK, btCancel;
    Spinner sCreditType;

    double totalPrice;
    String custmerName;

    String creditCardNumber = "";
    boolean nextStep = false;
    int numberOfPayments = 1;

    public static final String LEADERS_POS_CREDIT_CARD_ACTIVITY_BY_PHONE = "LEADERS_POS_CREDIT_CARD_ACTIVITY_BY_PHONE";
    public static final String LEADERS_POS_CREDIT_CARD_ACTIVITY_BY_PASS_CARD = "LEADERS_POS_CREDIT_CARD_ACTIVITY_BY_PASS_CARD";
    public static final String LEADERS_POS_CREDIT_CARD_TYPE = "LEADERS_POS_CREDIT_CARD_TYPE";
    public static final String LEADERS_POS_CREDIT_CARD_TOTAL_PRICE = "LEADERS_POS_CREDIT_CARD_TOTAL_PRICE";
    public static final String LEADERS_POS_CREDIT_CARD_CUSTOMER = "LEADERS_POS_CREDIT_CARD_CUSTOMER";

    public static final String LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY = "LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote = "LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote = "LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote";

    private boolean isByPhone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pass_credit_card);

        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.CENTER_VERTICAL;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = (float) 0.6;
        window.setAttributes(wlp);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.6), (int) (height * 0.5));

        //etFirstPayment=(EditText)findViewById(R.id.creditCardActivity_etFirstPayment);
        etNumberOfPayments = (EditText) findViewById(R.id.creditCardActivity_etNumberOfPayments);

        sCreditType = (Spinner) findViewById(R.id.creditCardActivity_sCreditType);

        tvTotalPrice = (TextView) findViewById(R.id.creditCardActivity_tvTotalPrice);
        tvCustomerName =(TextView)findViewById(R.id.custmer_name);
        btOK = (Button) findViewById(R.id.creditCardActivity_btnOK);
        btCancel = (Button) findViewById(R.id.creditCardActivity_btnCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            totalPrice = (double) extras.get(LEADERS_POS_CREDIT_CARD_TOTAL_PRICE);
            custmerName = (String) extras.get(LEADERS_POS_CREDIT_CARD_CUSTOMER);


            tvTotalPrice.setText(totalPrice + " " + getResources().getText(R.string.ins));
            tvCustomerName.setText(custmerName);
        } else {
            finish();
        }

        if (extras.getString(LEADERS_POS_CREDIT_CARD_TYPE).equals(LEADERS_POS_CREDIT_CARD_ACTIVITY_BY_PHONE)) {

            isByPhone = true;
            CreditCardByPhone fTP = new CreditCardByPhone();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.byPhoneFragment_fl, fTP);
            transaction.commit();
            getWindow().setLayout((int) (width * 0.6), (int) (height * 0.6));
        }

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isByPhone) {
                    TextView ccnum = (TextView) findViewById(R.id.creditCardByPhone_tvCCNum);
                    TextView ccv = (TextView) findViewById(R.id.creditCardByPhone_tvCCV);
                    TextView ce = (TextView) findViewById(R.id.creditCardByPhone_tvCE);
                    TextView id = (TextView) findViewById(R.id.creditCardByPhone_tvID);


                    long CCNum;
                    String CCV, CE, ID;
                    try {
                        CCNum = Long.parseLong(ccnum.getText().toString());
                        CCV = (ccv.getText().toString());
                        CE = (ce.getText().toString());
                        ID = (id.getText().toString());
                    }
                    catch (Exception e){
                        Toast.makeText(CreditCardActivity.this, getBaseContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //get last 4 digits form card number
                    long last4Digits = CCNum % 10000;

                    String Year, Month;
                    //check valid card expired
                    if (CE.length() == 3) {
                        Year = CE.substring(1, 3);
                        Month = CE.substring(0, 1);
                    } else if (CE.length() == 4) {
                        Year = CE.substring(2, 4);
                        Month = CE.substring(0, 2);

                    } else {
                        Toast.makeText(CreditCardActivity.this, getBaseContext().getString(R.string.valid_expired_card), Toast.LENGTH_SHORT).show();
                        return;
                        //// TODO: 11/04/2017 Card expired
                    }

                    if (Integer.parseInt(Year) > DateConverter.getYearWithTowChars()) {
                        //its valid
                    } else if(Integer.parseInt(Year)==DateConverter.getYearWithTowChars()){
                        if (Integer.parseInt(Month) >= DateConverter.getCurrentMonth()) {
                            //its valid
                        }
                    } else {
                        Toast.makeText(CreditCardActivity.this, getBaseContext().getString(R.string.expired_card), Toast.LENGTH_SHORT).show();
                    }

                    if(ID.length()!=9){
                        Toast.makeText(CreditCardActivity.this, getBaseContext().getString(R.string.valid_ID), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(CCV.length()!=3){
                        Toast.makeText(CreditCardActivity.this, getBaseContext().getString(R.string.valid_CCV), Toast.LENGTH_SHORT).show();
                        return;
                    }


                    final String _CreditCardNumber = (CCNum + ""), CardExpiry = CE, CVV2 = CCV, IDNumber = ID,ApprovalCode = "";

                    final double TransSum = totalPrice;
                    if(etNumberOfPayments.getText().toString().equals(""))
                        numberOfPayments = 1;
                    else
                        numberOfPayments = Integer.parseInt(etNumberOfPayments.getText().toString());
                    final int NumOfFixedPayments = numberOfPayments;

                    final ProgressDialog dialog_connection = new ProgressDialog(CreditCardActivity.this);
                    dialog_connection.setTitle(getBaseContext().getString(R.string.wait_for_accept));
                    dialog_connection.setOnShowListener(
                            new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            dialog_connection.dismiss();
                                            super.onPostExecute(aVoid);
                                        }
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            SoapObject soap = Arkom.ByPhone(_CreditCardNumber, CardExpiry, TransSum, CVV2, IDNumber, ApprovalCode, NumOfFixedPayments, 1);
                                            // TODO: 11/04/2017 Return to UI main thread this three value on activity result
                                            //Log.e("Answer", soap.getProperty("Answer").toString());
                                            //Log.e("MerchantNote", soap.getProperty("MerchantNote").toString());
                                            //Log.e("ClientNote", soap.getProperty("ClientNote").toString());
                                            returnTo(soap);
                                            return null;
                                        }
                                    }.execute();
                                }
                            }
                    );
                    dialog_connection.show();
                } else {
                    final ProgressDialog dialog = new ProgressDialog(CreditCardActivity.this);
                    final ProgressDialog dialog_connection = new ProgressDialog(CreditCardActivity.this);

                    numberOfPayments = Integer.parseInt(etNumberOfPayments.getText().toString());

                    tvTotalPrice.setFocusable(false);
                    btCancel.setFocusable(false);
                    btOK.setFocusable(false);
                    etNumberOfPayments.setFocusable(false);
                    sCreditType.setFocusable(false);

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (nextStep) {
                                dialog_connection.setTitle(getBaseContext().getString(R.string.wait_for_accept));
                                dialog_connection.show();
                            }
                        }
                    });
                    dialog_connection.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            creditCardNumber.replace("?", "");
                            creditCardNumber.replace(";", "");
                            creditCardNumber.replace("�", "");
                            creditCardNumber.replace("/", "");
                            Log.e("Card Number", creditCardNumber);
                            String validCCNUM = "";
                            char[] validChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '='};
                            String validSTR = "1234567890=";
                            for (char c : creditCardNumber.toCharArray()) {
                                if (validSTR.contains(c + "")) {
                                    validCCNUM = validCCNUM + c;
                                }
                            }
                            //Log.e("Card Number", validCCNUM);
                            final String CCNum = validCCNUM;
                            final int numberOfPayments = Integer.parseInt(etNumberOfPayments.getText().toString());
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected void onPostExecute(Void aVoid) {

                                    dialog_connection.dismiss();

                                    super.onPostExecute(aVoid);
                                }
                                @Override
                                protected Void doInBackground(Void... params) {
                                    SoapObject soap = Arkom.PassCard(CCNum ,numberOfPayments , totalPrice, "", 1);
                                    // TODO: 11/04/2017 Return to UI main thread this three value on activity result
                                    //Log.e("Answer", soap.getProperty("Answer").toString());
                                    //Log.e("MerchantNote", soap.getProperty("MerchantNote").toString());
                                    //Log.e("ClientNote", soap.getProperty("ClientNote").toString());
                                    if (soap.getProperty("MerchantNote").toString().equals("anyType{}")) {
                                        returnTo(null);
                                    }
                                    else {
                                        returnTo(soap);
                                    }
                                    return null;
                                }
                            }.execute();
                        }
                    });
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        boolean run = false;

                        @Override
                        public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {

                            if (event.getAction() == KeyEvent.ACTION_UP) {
                                if (event.getKeyCode() == KeyEvent.KEYCODE_EQUALS && run) {
                                    creditCardNumber += "=";
                                }
                                else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && run) {
                                    dialog.setMessage(creditCardNumber);
                                    nextStep = true;
                                    //Log.e("read", creditCardNumber);
                                    dialog.dismiss();
                                } else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && !run) {
                                    run = true;
                                } else {//enter pressed
                                    if (run)
                                        creditCardNumber += event.getNumber();
                                }
                                return true;
                            }
                            return false;
                        }
                    });
                    dialog.setTitle(getBaseContext().getString(R.string.transfer_credit_card));
                    dialog.show();
                }
            }
        });
    }

    public void returnTo(SoapObject soap) {
        if (soap != null) {
            Intent i = new Intent();
            i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY, soap.getProperty("Answer").toString());
            i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote, soap.getProperty("MerchantNote").toString());
            i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote, soap.getProperty("ClientNote").toString());
            setResult(RESULT_OK, i);
            finish();
        }
        else{
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
