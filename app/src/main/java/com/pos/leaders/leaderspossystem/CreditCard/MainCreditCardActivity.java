package com.pos.leaders.leaderspossystem.CreditCard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DocumentType;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Printer.SM_S230I.MiniPrinterFunctions;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SalesCartActivity;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.CreditCardTransactionType;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.DocumentControl;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.sunmi.aidl.MSCardService;
import com.sunmi.aidl.callback;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.regex.Pattern;

public class MainCreditCardActivity extends AppCompatActivity {
    private static final String LOG_TAG = "Credit Card T";

    public static final String LEADERS_POS_CREDIT_CARD_TOTAL_PRICE = "LEADERS_POS_CREDIT_CARD_TOTAL_PRICE";
    public static final String LEADERS_POS_CREDIT_CARD_FROM_MULTI_CURRENCY = "LEADERS_POS_CREDIT_CARD_FROM_MULTI_CURRENCY";

    public static final String LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY = "LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote = "LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote = "LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote";
    private static String etCCValue2;

    Button btCancel,btStart, btDone, btByPhone, btAdvance, btClear;
    LinearLayout llAdvance, llByPhone;
    TextView tvTotalPrice, tvCCValue, tvMessage;
    static EditText etCCValue;
    EditText etPaymentsNumber;
    Spinner spCreditType;
    boolean advanceMode = false, byPhoneMode = false,advanceModeForPhone=false;
    double totalPrice = 0;
   ProgressDialog dialog_connection;
    //page 9 on Arkom documents
    //1:normal
    int creditType = CreditCardTransactionType.NORMAL;
    int numberOfPayments = 1;
    boolean creditReceipt=false;
    boolean fromMultiCurrency=false;
    JSONObject invoiceJson=new JSONObject();
    BoInvoice invoice ;
    int NumOfFixedPayments=1;

    private MSCardService sendservice;
    ServiceConnection serviceConnection;
    msCardReaderCallBack msCardReaderCallback=new msCardReaderCallBack();
    boolean doneButtonLock = false;
    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        try {
            TitleBar.removeTitleBard();
        } catch (IllegalArgumentException iae) {

        }
            if(dialog_connection.isShowing()){
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        dialog_connection.dismiss();
                    }
                });
            }

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide activity title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_credit_card);

        //set custom title bar
        TitleBar.setTitleBar(this);

        Intent intent = new Intent();
        intent.setPackage("com.sunmi.mscardservice");
        intent.setAction("com.sunmi.mainservice.MainService");
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                sendservice = MSCardService.Stub.asInterface(service);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {}
        };

        try {
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        } catch (Exception e) {
            Log.e("Sunmi MSC ", e.getMessage());
        }



        //init views
        btDone      = (Button) findViewById(R.id.MainCreditCardActivity_btDone);
        btStart      = (Button) findViewById(R.id.MainCreditCardActivity_btStart);
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
        dialog_connection = new ProgressDialog(MainCreditCardActivity.this);

        //check extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.containsKey("creditReceipt")){
                creditReceipt=true;
                totalPrice = (double) extras.get("_Price");
                tvTotalPrice.setText(Util.makePrice(totalPrice) + " " + SETTINGS.currencySymbol);
                try {
                    invoiceJson=new JSONObject(extras.getString("invoice"));
                    JSONObject docJson = invoiceJson.getJSONObject("documentsData");
                    docJson.remove("@type");
                    docJson.put("type","Invoice");
                    invoiceJson.remove("documentsData");
                    invoiceJson.put("documentsData",docJson);
                    invoice=new BoInvoice(DocumentType.INVOICE,docJson,invoiceJson.getString("docNum"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                creditReceipt=false;
                fromMultiCurrency=extras.getBoolean(LEADERS_POS_CREDIT_CARD_FROM_MULTI_CURRENCY);
                totalPrice = (double) extras.get(LEADERS_POS_CREDIT_CARD_TOTAL_PRICE);
                tvTotalPrice.setText(Util.makePrice(totalPrice) + " " + SETTINGS.currencySymbol);
            }
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
                final Dialog creditByPhoneDialog = new Dialog(MainCreditCardActivity.this);
                 creditByPhoneDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                 creditByPhoneDialog.show();
                 creditByPhoneDialog.setContentView(R.layout.credit_card_by_phone);
                 final EditText cardNoEt = (EditText)creditByPhoneDialog.findViewById(R.id.EtCreditCardByPhoneCreditCardNo);
                 final EditText expireDateEt =(EditText)creditByPhoneDialog.findViewById(R.id.EtCreditCardByPhoneCreditCardExpiredDate);
                 final EditText ccvEt =(EditText)creditByPhoneDialog.findViewById(R.id.EtCreditCardByPhoneCreditCcv);
                 final EditText identificationNo =(EditText)creditByPhoneDialog.findViewById(R.id.EtCreditCardByPhoneCreditCardId);
                final EditText numberOfPaymentsEt =(EditText)creditByPhoneDialog.findViewById(R.id.EtNumberOfPayments);
                final LinearLayout  llAdvanceByPhone   = (LinearLayout) creditByPhoneDialog.findViewById(R.id.MainCreditCardActivityByPhone_llAdvance);
               final Spinner spCreditTypeByPhone= (Spinner) creditByPhoneDialog.findViewById(R.id.MainCreditCardActivityByPhone_spPaymentType);


                        Button done = (Button)creditByPhoneDialog.findViewById(R.id.btn_done);
                 Button cancel = (Button)creditByPhoneDialog.findViewById(R.id.btn_cancel);
                Button advanced = (Button)creditByPhoneDialog.findViewById(R.id.btn_advancedByPhone);

                cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                creditByPhoneDialog.dismiss();
                }
                });
                advanced.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llAdvanceByPhone.setVisibility(View.VISIBLE);
                        advanceModeForPhone=true;
                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long CCNum;
                        String CCV, CE, ID;
                        try {
                            CCNum = Long.parseLong(cardNoEt.getText().toString());
                            CCV = (ccvEt.getText().toString());
                            CE = (expireDateEt.getText().toString());
                            ID = (identificationNo.getText().toString());
                        } catch (Exception e) {
                            Toast.makeText(MainCreditCardActivity.this, getBaseContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MainCreditCardActivity.this, getBaseContext().getString(R.string.valid_expired_card), Toast.LENGTH_SHORT).show();
                            return;
                            //// TODO: 11/04/2017 Card expired
                        }

                        if (Integer.parseInt(Year) > DateConverter.getYearWithTowChars()) {
                            //its valid
                        } else if (Integer.parseInt(Year) == DateConverter.getYearWithTowChars()) {
                            if (Integer.parseInt(Month) >= DateConverter.getCurrentMonth()) {
                                //its valid
                            }
                        } else {
                            Toast.makeText(MainCreditCardActivity.this, getBaseContext().getString(R.string.expired_card), Toast.LENGTH_SHORT).show();
                        }

                        if (ID.length() != 9) {
                            Toast.makeText(MainCreditCardActivity.this, getBaseContext().getString(R.string.valid_ID), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (CCV.length() != 3) {
                            Toast.makeText(MainCreditCardActivity.this, getBaseContext().getString(R.string.valid_CCV), Toast.LENGTH_SHORT).show();
                            return;
                        }


                        final String _CreditCardNumber = (CCNum + ""), CardExpiry = CE, CVV2 = CCV, IDNumber = ID, ApprovalCode = "";

                        final double TransSum = totalPrice;
                        if (advanceModeForPhone) {
                            advanceModeForPhone=false;
                            String pn = numberOfPaymentsEt.getText().toString();
                            //trying to case input
                            int ipn;
                            try {
                                ipn = Integer.parseInt(pn);
                            } catch (NumberFormatException nfe) {
                                Toast.makeText(MainCreditCardActivity.this, getString(R.string.invalid_payments_number), Toast.LENGTH_LONG).show();
                                creditByPhoneDialog.dismiss();
                                return;
                            }
                            //check valid number
                            if (ipn > 1 && ipn < 37) {
                                numberOfPayments = ipn;
                                int ccT = spCreditTypeByPhone.getSelectedItemPosition();

                                if (ccT == 0)
                                    creditType = CreditCardTransactionType.PAYMENTS;//תשלומים
                                else if (ccT == 1)
                                    creditType = CreditCardTransactionType.CREDIT;//קרדיט/קרדיט בתשלומים קבועים

                                NumOfFixedPayments = numberOfPayments;
                            } else {
                                //out of maximum payment number
                                Toast.makeText(MainCreditCardActivity.this, getString(R.string.invalid_payments_number), Toast.LENGTH_LONG).show();
                                creditByPhoneDialog.dismiss();
                                return;
                            }
                        }


                        new AsyncTask<SoapObject, Void, SoapObject>() {
                            @Override
                            protected void onPostExecute(SoapObject aVoid) {
                                creditByPhoneDialog.dismiss();
                                returnTo(aVoid);
                                super.onPostExecute(aVoid);
                            }
                            @Override
                            protected SoapObject doInBackground(SoapObject... params) {
                                SoapObject soap = Arkom.ByPhone(_CreditCardNumber, CardExpiry, TransSum, CVV2, IDNumber, ApprovalCode, NumOfFixedPayments, creditType);
                                // TODO: 11/04/2017 Return to UI main thread this three value on activity result
                                //Log.e("Answer", soap.getProperty("Answer").toString());
                                //Log.e("MerchantNote", soap.getProperty("MerchantNote").toString());
                                //Log.e("ClientNote", soap.getProperty("ClientNote").toString());

                                //

                                return soap;
                            }
                        }.execute();
                    }});


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
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start lisinning to mscard
                new Thread(sendable).start();
                if(btDone.isClickable())
                    btDone.setClickable(false);
            }
        });

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!doneButtonLock) {
                    doneButtonLock=true;
                    dialog_connection.setTitle(getBaseContext().getString(R.string.wait_for_accept));
                    dialog_connection.show();
                    String creditCardNumber = etCCValue.getText().toString();
                    Log.i(LOG_TAG, creditCardNumber);
                    if (creditCardNumber.isEmpty()) {
                        //input is empty
                        dialog_connection.dismiss();
                        Toast.makeText(MainCreditCardActivity.this, getString(R.string.transfer_credit_card), Toast.LENGTH_SHORT).show();
                        doneButtonLock = false;
                        return;
                    }
                    String str = creditCardNumber;

                    if (str.contains("?;")) {
                        creditCardNumber = str.split(Pattern.quote("?;"))[1];
                    }

                    creditCardNumber = creditCardNumber.replace("?", "");
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

                    Log.i(LOG_TAG, CCNum);
                    if (advanceMode) {
                        String pn = etPaymentsNumber.getText().toString();
                        //trying to case input
                        int ipn;
                        try {
                            ipn = Integer.parseInt(pn);
                        } catch (NumberFormatException nfe) {
                            Toast.makeText(MainCreditCardActivity.this, getString(R.string.invalid_payments_number), Toast.LENGTH_LONG).show();
                            dialog_connection.dismiss();
                            doneButtonLock = false;
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

                            doTransaction(CCNum, numberOfPayments, creditType);

                        } else {
                            //out of maximum payment number
                            Toast.makeText(MainCreditCardActivity.this, getString(R.string.invalid_payments_number), Toast.LENGTH_LONG).show();
                            dialog_connection.dismiss();
                            doneButtonLock = false;
                            return;
                        }
                    } else {
                        //normal mode with 1 payment number and normal credit type
                        doTransaction(CCNum, 1, 1);
                    }

                    dialog_connection.dismiss();
                }
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
     //   final ProgressDialog dialog_connection = new ProgressDialog(MainCreditCardActivity.this);
        dialog_connection.setTitle(getBaseContext().getString(R.string.wait_for_accept));
        dialog_connection.show();

        new AsyncTask<SoapObject, Void, SoapObject>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                btDone.setClickable(false);
                doneButtonLock = true;
            }

            @Override
            protected void onPostExecute(SoapObject aVoid) {
                if(dialog_connection.isShowing()){
                    dialog_connection.dismiss();
                }
                doneButtonLock = false;
                btDone.setClickable(true);
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
                if(creditReceipt){
                    SESSION._TEMP_CREDITCARD_PAYMNET = creditCardPayment;

                    DocumentControl.sendDoc(MainCreditCardActivity.this,invoice, CONSTANT.CREDIT_CARD,totalPrice, soap.getProperty("MerchantNote").toString());

                }
                else {
                    Intent i = new Intent();
                    i.putExtra(LEADERS_POS_CREDIT_CARD_FROM_MULTI_CURRENCY, fromMultiCurrency);
                    i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY, answer);
                    i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_MerchantNote, soap.getProperty("MerchantNote").toString());
                    i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CREDIT_CARD_ACTIVITY_ClientNote, soap.getProperty("ClientNote").toString());
                    i.putExtra( SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE,totalPrice);
                    SESSION._TEMP_CREDITCARD_PAYMNET = creditCardPayment;
                    Log.i(LOG_TAG, creditCardPayment.toString());
                    setResult(RESULT_OK, i);
                    finish();
                }



            } catch (CreditCardResultException e) {
                if(!isFinishing()){

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MainCreditCardActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MainCreditCardActivity.this);
                }
                builder.setTitle(getString(R.string.error))
                        .setMessage(e.getMessage())
                        .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(RESULT_CANCELED);
                                finish();
                                close();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setCancelable(false)
                        .show();
            }
            }
        }
        else{
            if(totalPrice<0){
                if(SESSION._EMPLOYEE.getEmployeeId()!=2) {
                    Toast.makeText(this, "This Operation just for master employee !!", Toast.LENGTH_LONG).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }else {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }

    }
    private void close() {
        MainCreditCardActivity.this.onBackPressed();
       // btClear.callOnClick();
       // this.setResult(RESULT_CANCELED,new Intent());
        //finish();
    }


    Runnable sendable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                sendservice.readRawMSCard(20000, msCardReaderCallback);
            } catch (RemoteException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    };
    class msCardReaderCallBack extends callback.Stub {

        @Override
        public void MSCardInfo(final boolean isSuccess, final byte[] m1, final byte[] m2, final byte[] m3)
                throws RemoteException {
            runOnUiThread(new Runnable() {

                @SuppressWarnings("deprecation")
                @Override
                public void run() {
                    try {
                        etCCValue.setText(  new String(m2, "US-ASCII").split("\0")[0]);
                        Log.e("Card Numbers", new String(m1, "US-ASCII").split("\0")[0]
                                + new String(m2, "US-ASCII").split("\0")[0]
                                + new String(m3, "US-ASCII").split("\0")[0] );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(!btDone.isClickable())
                        btDone.setClickable(true);
                }
            });
        }

    }
    @Override
    protected void onPause() {

        if(dialog_connection.isShowing()){
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    dialog_connection.dismiss();
                }
            });
        }
        super.onPause();
    }


    @Override
    protected void onResume() {

        if(dialog_connection.isShowing()){
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    dialog_connection.dismiss();
                }
            });
        }
        super.onResume();
    }


}
