package com.pos.leaders.leaderspossystem.CreditCard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Printer.SM_S230I.MiniPrinterFunctions;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CreditCardTransactionType;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.ksoap2.serialization.SoapObject;

import java.util.regex.Pattern;

public class MainCreditCardActivity extends AppCompatActivity {
    private static final String LOG_TAG = "Credit Card T";

    public static final String LEADERS_POS_CREDIT_CARD_TOTAL_PRICE = "LEADERS_POS_CREDIT_CARD_TOTAL_PRICE";

    public static final String LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY = "LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote = "LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote = "LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote";
    private static String etCCValue2;

    Button btCancel, btDone, btByPhone, btAdvance, btClear;
    LinearLayout llAdvance, llByPhone;
    TextView tvTotalPrice, tvCCValue, tvMessage;
    static EditText etCCValue;
    EditText etPaymentsNumber;
    Spinner spCreditType;

    boolean advanceMode = false, byPhoneMode = false;
    double totalPrice = 0;

    //page 9 on Arkom documents
    //1:normal
    int creditType = CreditCardTransactionType.NORMAL;
    int numberOfPayments = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide activity title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_credit_card);

        //set custom title bar
        TitleBar.setTitleBar(this);

        //init views
        btDone      = (Button) findViewById(R.id.MainCreditCardActivity_btDone);
        btAdvance   = (Button) findViewById(R.id.MainCreditCardActivity_btAdvance);
        btByPhone   = (Button) findViewById(R.id.MainCreditCardActivity_btByPhone);
        btCancel    = (Button) findViewById(R.id.MainCreditCardActivity_btCancel);
        btClear     = (Button) findViewById(R.id.MainCreditCardActivity_btClear);

        tvTotalPrice= (TextView) findViewById(R.id.MainCreditCardActivity_tvTotalPrice);
        tvMessage   = (TextView) findViewById(R.id.MainCreditCardActivity_tvMessage);

        etCCValue = (EditText) findViewById(R.id.MainCreditCardActivity_tvCCValue);
        etPaymentsNumber = (EditText) findViewById(R.id.MainCreditCardActivity_etPaymentsNumber);

        llAdvance   = (LinearLayout) findViewById(R.id.MainCreditCardActivity_llAdvance);
        llByPhone   = (LinearLayout) findViewById(R.id.MainCreditCardActivity_llByPhone);

        spCreditType= (Spinner) findViewById(R.id.MainCreditCardActivity_spPaymentType);

        //check extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            totalPrice = (double) extras.get(LEADERS_POS_CREDIT_CARD_TOTAL_PRICE);
            tvTotalPrice.setText(Util.makePrice(totalPrice) + " " + getResources().getText(R.string.ins));
        } else {
            finish();
        }

        btDone.setClickable(false);

        btAdvance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceMode = !advanceMode;
                AdvanceMode();
            }
        });
        btByPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byPhoneMode = !byPhoneMode;
                ByPhoneMode();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCCValue.setText("");
                //btDone.setClickable(false);
            }
        });

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog_connection = new ProgressDialog(MainCreditCardActivity.this);
                dialog_connection.setTitle(getBaseContext().getString(R.string.wait_for_accept));
                dialog_connection.show();
                String creditCardNumber = etCCValue.getText().toString();
                Log.i(LOG_TAG, creditCardNumber);
                if(creditCardNumber.isEmpty()){
                    //input is empty
                    dialog_connection.dismiss();
                    Toast.makeText(MainCreditCardActivity.this, getString(R.string.transfer_credit_card), Toast.LENGTH_SHORT).show();
                    return;
                }
                String str = creditCardNumber;

                if (str.contains("?;")) {
                    creditCardNumber = str.split(Pattern.quote("?;"))[1];
                }

                creditCardNumber=creditCardNumber.replace("?", "");
                creditCardNumber.replace("?", "");
                creditCardNumber.replace(";", "");
                creditCardNumber.replace("�", "");
                creditCardNumber.replace("/", "");
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


                if (advanceMode) {
                    String pn = etPaymentsNumber.getText().toString();
                    //trying to case input
                    int ipn;
                    try {
                        ipn = Integer.parseInt(pn);
                    }
                    catch (NumberFormatException nfe){
                        Toast.makeText(MainCreditCardActivity.this, getString(R.string.invalid_payments_number), Toast.LENGTH_LONG).show();
                        dialog_connection.dismiss();
                        return;
                    }
                    //check valid number
                    if (ipn > 1 && ipn < 37) {
                        numberOfPayments = ipn;
                        int ccT = spCreditType.getSelectedItemPosition();

                        if (ccT == 0)
                            creditType = CreditCardTransactionType.PAYMENTS;//תשלומים
                        else if (ccT == 1)
                            creditType = CreditCardTransactionType.CREDIT;//קרדיט/קרדיט בתשלומים קבועים

                        doTransaction(creditCardNumber, numberOfPayments, creditType);

                    } else {
                        //out of maximum payment number
                        Toast.makeText(MainCreditCardActivity.this, getString(R.string.invalid_payments_number), Toast.LENGTH_LONG).show();
                        dialog_connection.dismiss();
                        return;
                    }
                } else {
                    //normal mode with 1 payment number and normal credit type
                    doTransaction(creditCardNumber, 1, 1);
                }

                dialog_connection.dismiss();
            }
        });

        etCCValue.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_ENTER||keyCode==KeyEvent.KEYCODE_TAB)
                {
                    // Just ignore the [Enter] key
                    return true;
                }
                // Handle all other keys in the default way
                return false;
            }
        });
        etCCValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!btDone.isClickable())
                    btDone.setClickable(true);
            }
        });

        //disable copy paste methods
        etCCValue.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        AdvanceMode();
        ByPhoneMode();

        //hide soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        if (SETTINGS.printer == PrinterType.SM_S230I) {
            String portSettings = "portable;escpos;l";
            String port = "BT:";
            MiniPrinterFunctions.MCRStart2(MainCreditCardActivity.this, port,portSettings);

        }

    }
    public static void fillETCCNumber(String str) {
        for (String s :str.split("\u001C")){
            //"\u0002E11\u001C\u001CB0000000000000000^00000000000000000000000000^0000201000000000000000000000000\u001C12012548044640700=0360588178931119011"
            if (s.contains("=")) {
                etCCValue.setText(s);
                return;
            }
        }
    }



    private void AdvanceMode(){
        if (advanceMode)
            llAdvance.setVisibility(View.VISIBLE);
        else
            llAdvance.setVisibility(View.GONE);
    }
    private void ByPhoneMode(){
        if (byPhoneMode)
            llByPhone.setVisibility(View.VISIBLE);
        else
            llByPhone.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }


    private void doTransaction(final String cardNumber,final int paymentsNumber,final int creditType) {
        final ProgressDialog dialog_connection = new ProgressDialog(MainCreditCardActivity.this);
        dialog_connection.setTitle(getBaseContext().getString(R.string.wait_for_accept));
        dialog_connection.show();

        new AsyncTask<SoapObject, Void, SoapObject>() {
            @Override
            protected void onPostExecute(SoapObject aVoid) {
                dialog_connection.dismiss();
                returnTo(aVoid);
                super.onPostExecute(aVoid);
            }
            @Override
            protected SoapObject doInBackground(SoapObject... params) {
                SoapObject soap = Arkom.PassCard(cardNumber, paymentsNumber, totalPrice, "", creditType);
                // TODO: 11/04/2017 Return to UI main thread this three value on activity result
                //Log.e("Answer", soap.getProperty("Answer").toString());
                //Log.e("MerchantNote", soap.getProperty("MerchantNote").toString());
                //Log.e("ClientNote", soap.getProperty("ClientNote").toString());

                //

                return soap;
            }
        }.execute();
    }
    private void returnTo(SoapObject soap){
        if (soap != null) {

            String answer = soap.getProperty("Answer").toString();
            try {
                CreditCardPayment creditCardPayment = Result.read(answer);

                creditCardPayment.setAnswer(answer);
                creditCardPayment.setAmount(totalPrice);
                creditCardPayment.setTransactionType(creditType);
                creditCardPayment.setTransactionId(soap.getProperty("TransactionID").toString());

                //soap.getProperty("MerchantNote").toString().equals("anyType{}");

                Intent i = new Intent();
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY, answer);
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote, soap.getProperty("MerchantNote").toString());
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote, soap.getProperty("ClientNote").toString());
                SESSION._TEMP_CREDITCARD_PAYMNET = creditCardPayment;
                Log.i(LOG_TAG, creditCardPayment.toString());
                setResult(RESULT_OK, i);
                finish();


            } catch (CreditCardResultException e) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MainCreditCardActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MainCreditCardActivity.this);
                }
                builder.setTitle("שגיאה")
                        .setMessage(e.getMessage())
                        .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                close();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show();
            }
        }
        else{
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }
    private void close() {
        MainCreditCardActivity.this.onBackPressed();
       // btClear.callOnClick();
       // this.setResult(RESULT_CANCELED,new Intent());
        //finish();
    }
}
