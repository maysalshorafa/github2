package com.pos.leaders.leaderspossystem.Tools;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.CurrencyReturnsCustomDialogActivity;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencysDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Currency.Currencys;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.TouchPadFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TempCashActivty extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private List<CurrencyType> currencyTypesList=null;
    public static final String LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY = "LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_FIRSTCURRENCY_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_FIRSTCURRENCY_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_ID_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_ID_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_ID_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_ID_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_EXCESSVALUE = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_EXCESSVALUE";


    TextView custmer_name;
String touchPadPressed="";
    TextView tv ,tvTotalInserted ;
    EditText  tvTotalInsertedForFirstCurrency,tvTotalInsertedForSecondCurrency;
    String custmer_nameS;
   Spinner  spinnerForFirstCurrency,spinnerForSecondCurrency, spinnerForTotalPrice;
    Button btnDone;

    double totalPrice = 0.0;
    float totalPid = 0.0f;
    long saleId=0;
long firstCurrencyId,secondCurrencyId, totalPriceSpinnerId =0;

    double basicCurrencyValue;
    String insertedValueForFirstCurrency ,insertedValueForSecondCurrency, valueForTotalPriceCurrency;
    double valueInsertedForDolar;
    CashPaymentDBAdapter cashpayment;
    double firstCurruncyValue,secondCurrency,exceesValue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.test_cash);
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

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.95));

        showTouchPad(false);
        custmer_name=(TextView)findViewById(R.id.custmer_name);
        spinnerForFirstCurrency = (Spinner)findViewById(R.id.spinnerForFirstCurrency);
        spinnerForSecondCurrency = (Spinner)findViewById(R.id.spinnerForSecondCurrency);
        spinnerForTotalPrice = (Spinner)findViewById(R.id.spinnerForTotalPrice);

        btnDone=(Button)findViewById(R.id.cashActivity_BTNDone);
        tv = (TextView) findViewById(R.id.cashActivity_TVRequired);
        tvTotalInserted = (TextView) findViewById(R.id.cashActivity_TVTotalInserted);
        tvTotalInsertedForFirstCurrency = (EditText) findViewById(R.id.cashActivity_TVTotalInsertedForfirstCurrency);
        tvTotalInsertedForSecondCurrency = (EditText) findViewById(R.id.cashActivity_TVTotalInsertedForSecondCurrency);

        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
        currencyTypeDBAdapter.open();

        cashpayment=new CashPaymentDBAdapter(this);
        cashpayment.open();
        spinnerForFirstCurrency.setOnItemSelectedListener(this);
        spinnerForSecondCurrency.setOnItemSelectedListener(this);
        spinnerForTotalPrice.setOnItemSelectedListener(this);
        btnDone.setEnabled(false);
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();

        final List<String> currency = new ArrayList<String>();
        for (int i = 0;  i < currencyTypesList.size(); i++) {
            currency.add(currencyTypesList.get(i).getType());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currency);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerForFirstCurrency.setAdapter(dataAdapter);
        spinnerForSecondCurrency.setAdapter(dataAdapter);
        spinnerForTotalPrice.setAdapter(dataAdapter);


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //tvTotalInserted.setText(tv.getText());

                btnDone.setEnabled(true);
                btnDone.setBackground(getResources().getDrawable(R.drawable.btn_primary));
                btnDone.setPadding(50,10,50,10);
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            totalPrice = (double) extras.get("_Price");
            custmer_nameS= (String) extras.get("_custmer");
            saleId = (long) extras.get("_SaleId");
            Toast.makeText(TempCashActivty.this,"id"+saleId,Toast.LENGTH_LONG).show();

            tv.setText(totalPrice + " " );
            valueForTotalPriceCurrency = tv.getText().toString();
            custmer_name.setText(custmer_nameS);
            custmer_nameS="";
        } else {
            finish();
        }





        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
totalPid= (float) (basicCurrencyValue+firstCurruncyValue+secondCurrency);
                //// TODO: 01/12/2016 return how match inserted money
                Intent i=new Intent();
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY,totalPid);
                    i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_FIRSTCURRENCY_AMOUNT,Double.parseDouble(tvTotalInserted.getText().toString()));
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_AMOUNT,Double.parseDouble(tvTotalInsertedForFirstCurrency.getText().toString()));
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_AMOUNT,Double.parseDouble(tvTotalInsertedForSecondCurrency.getText().toString()));
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_ID_AMOUNT,firstCurrencyId);
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_ID_AMOUNT,secondCurrencyId);
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_ID_AMOUNT,exceesValue);
                setResult(RESULT_OK,i);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TempCashActivty.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_EXCESSVALUE, (float) exceesValue);
                editor.apply();
                CurrencyReturnsCustomDialogActivity cdd=new CurrencyReturnsCustomDialogActivity(TempCashActivty.this);
                cdd.show();
            }
        });


   /**     tvTotalInserted.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //do something here
                    String value = tvTotalInserted.getText().toString();
                if(value.length()>0) {
                    basicCurrencyValue = Double.parseDouble(value);
                }
                else{
                    basicCurrencyValue = 0;
                }
                    exceesValue = (basicCurrencyValue+firstCurruncyValue+secondCurrency) - totalPrice;

                    if (exceesValue >=0) {
                        btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));

                        btnDone.setEnabled(true);


                    }


                return false;
            }
        });**/
        tvTotalInsertedForFirstCurrency.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    CurrencysDBAdapter currencysDBAdapter = new CurrencysDBAdapter(TempCashActivty.this);
                    currencysDBAdapter.open();

                    firstCurrencyId = spinnerForFirstCurrency.getSelectedItemId();
                    Currencys currencys = currencysDBAdapter.getSpeficCurrencys(spinnerForFirstCurrency.getSelectedItem().toString(), new Date());

                    insertedValueForFirstCurrency = tvTotalInsertedForFirstCurrency.getText().toString();
                    firstCurruncyValue = (double) (Double.parseDouble(insertedValueForFirstCurrency) * currencys.getRate());
                    //   tvTotalInsertedForFirstCurrency.setText(firstCurruncyValue+"");
                    //do something here

                    exceesValue = (basicCurrencyValue + firstCurruncyValue + secondCurrency) - totalPrice;

                    if (exceesValue >= 0) {
                        btnDone.setEnabled(true);
                        btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));


                    }

                    currencysDBAdapter.close();
                }

                return false;
            }
        });
        tvTotalInsertedForSecondCurrency.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    CurrencysDBAdapter currencysDBAdapter = new CurrencysDBAdapter(TempCashActivty.this);
                    currencysDBAdapter.open();

                    secondCurrencyId = spinnerForSecondCurrency.getSelectedItemId();

                    Currencys currencys = currencysDBAdapter.getSpeficCurrencys(spinnerForSecondCurrency.getSelectedItem().toString(), new Date());

                    insertedValueForSecondCurrency = tvTotalInsertedForSecondCurrency.getText().toString();
                    secondCurrency = (double) (Double.parseDouble(insertedValueForSecondCurrency) * currencys.getRate());
                    // tvTotalInsertedForSecondCurrency.setText(secondCurrency+"");
                    //do something here

                    exceesValue = (basicCurrencyValue + firstCurruncyValue + secondCurrency) - totalPrice;
                    if (exceesValue >=0) {
                        btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));
                        btnDone.setEnabled(true);


                    }

                    currencysDBAdapter.close();
                }
                return false;
            }
        });



                spinnerForTotalPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        CurrencysDBAdapter currencysDBAdapter=new CurrencysDBAdapter(TempCashActivty.this);
                        currencysDBAdapter.open();

                        totalPriceSpinnerId = spinnerForTotalPrice.getSelectedItemId();
                        Currencys currencys=currencysDBAdapter.getSpeficCurrencys(spinnerForTotalPrice.getSelectedItem().toString(),new Date());

                        exceesValue= Double.parseDouble(valueForTotalPriceCurrency)/currencys.getRate();
                        tv.setText(exceesValue+"");


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }
    private void calcTotalInserted() {
        totalPid = 0;
        String value= tvTotalInserted.getText().toString();
        basicCurrencyValue=Integer.parseInt(value);

        float deltaPrice = (float)(basicCurrencyValue -(float)totalPrice );
        if (deltaPrice >= 0) {
            btnDone.setEnabled(true);
            btnDone.setBackground(getResources().getDrawable(R.drawable.btn_primary));
            btnDone.setPadding(50,10,50,10);

        } else {
            btnDone.setEnabled(false);
            btnDone.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
            btnDone.setPadding(50,10,50,10);

        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {


    }
    private void showTouchPad(boolean b) {
            TouchPadFragment fTP = new TouchPadFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.cashActivity_fragmentTochPad, fTP);
            transaction.commit();


    }
    public void touchPadClick(View view) {
        switch (view.getId()) {
            case R.id.touchPadFragment_bt0:
                touchPadPressed += 0;
                break;
            case R.id.touchPadFragment_bt1:
                touchPadPressed += 1;
                break;
            case R.id.touchPadFragment_bt2:
                touchPadPressed += 2;
                break;
            case R.id.touchPadFragment_bt3:
                touchPadPressed += 3;
                break;
            case R.id.touchPadFragment_bt4:
                touchPadPressed += 4;
                break;
            case R.id.touchPadFragment_bt5:
                touchPadPressed += 5;
                break;
            case R.id.touchPadFragment_bt6:
                touchPadPressed += 6;
                break;
            case R.id.touchPadFragment_bt7:
                touchPadPressed += 7;
                break;
            case R.id.touchPadFragment_bt8:
                touchPadPressed += 8;
                break;
            case R.id.touchPadFragment_bt9:
                touchPadPressed += 9;
                break;
            case R.id.touchPadFragment_btCE:
                if(!touchPadPressed.equals(""))
                    touchPadPressed = Util.removeLastChar(touchPadPressed);

                break;
            case R.id.touchPadFragment_btEnter:
                if(!touchPadPressed.equals("")){
                   tvTotalInserted.setText(touchPadPressed);
                String value = tvTotalInserted.getText().toString();
                if(value.length()>0) {
                    basicCurrencyValue = Double.parseDouble(value);
                }

                exceesValue = (basicCurrencyValue+firstCurruncyValue+secondCurrency) - totalPrice;

                if (exceesValue >=0) {
                    btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));

                    btnDone.setEnabled(true);
                }else{
                        btnDone.setBackground(getResources().getDrawable(R.drawable.btn_primary));

                        btnDone.setEnabled(false);

                }
                touchPadPressed = "";}
                break;
            case R.id.touchPadFragment_btDot:
                if(touchPadPressed.indexOf(".")<0)
                    touchPadPressed += ".";
                break;
        }
        TextView tirh=(TextView)this.findViewById(R.id.touchPadFragment_tvView);
        tirh.setText(touchPadPressed);
    }

}
