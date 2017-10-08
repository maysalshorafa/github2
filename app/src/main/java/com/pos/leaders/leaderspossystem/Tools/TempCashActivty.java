package com.pos.leaders.leaderspossystem.Tools;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencysDBAdapter;
import com.pos.leaders.leaderspossystem.MainActivity;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Currency.Currencys;
import com.pos.leaders.leaderspossystem.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TempCashActivty extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private List<CurrencyType> currencyTypesList=null;
    public static final String LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY = "LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY";
    TextView custmer_name;

    TextView tv, tvExcess;
    EditText tvTotalInserted , tvTotalInsertedForFirstCurrency,tvTotalInsertedForSecondCurrency;
    String custmer_nameS;
   Spinner  spinnerForFirstCurrency,spinnerForSecondCurrency,spinnerForReturnValue;
    Button btnDone;

    double totalPrice = 0.0;
    float totalPid = 0.0f;
    long saleId=0;
long firstCurrencyId,secondCurrencyId,returnSpenerId=0;

    double basicCurrencyValue;
    String insertedValueForFirstCurrency ,insertedValueForSecondCurrency,valueForReturnCurrency;
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
        setContentView(R.layout.activity_temp_cash_activty);

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

        custmer_name=(TextView)findViewById(R.id.custmer_name);
        spinnerForFirstCurrency = (Spinner)findViewById(R.id.spinnerForFirstCurrency);
        spinnerForSecondCurrency = (Spinner)findViewById(R.id.spinnerForSecondCurrency);
        spinnerForReturnValue= (Spinner)findViewById(R.id.spinnerForReturnValue);

        btnDone=(Button)findViewById(R.id.cashActivity_BTNDone);
        tv = (TextView) findViewById(R.id.cashActivity_TVRequired);
        tvExcess = (TextView) findViewById(R.id.cashActivity_TVExcess);
        tvExcess.setText(0 + " ");
        tvExcess.setTextColor(getResources().getColor(R.color.primaryColor));
        tvTotalInserted = (EditText) findViewById(R.id.cashActivity_TVTotalInserted);
        tvTotalInsertedForFirstCurrency = (EditText) findViewById(R.id.cashActivity_TVTotalInsertedForfirstCurrency);
        tvTotalInsertedForSecondCurrency = (EditText) findViewById(R.id.cashActivity_TVTotalInsertedForSecondCurrency);
        valueForReturnCurrency = tvExcess.getText().toString();

        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
        currencyTypeDBAdapter.open();
        final CurrencyReturnsDBAdapter currencyReturnsDBAdapter=new CurrencyReturnsDBAdapter(this);
currencyReturnsDBAdapter.open();
        cashpayment=new CashPaymentDBAdapter(this);
        cashpayment.open();
        spinnerForFirstCurrency.setOnItemSelectedListener(this);
        spinnerForSecondCurrency.setOnItemSelectedListener(this);
        spinnerForReturnValue.setOnItemSelectedListener(this);

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
        spinnerForReturnValue.setAdapter(dataAdapter);


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //tvTotalInserted.setText(tv.getText());
                tvExcess.setText(0 + " " );
                tvExcess.setTextColor(getResources().getColor(R.color.primaryColor));
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
                setResult(RESULT_OK,i);
cashpayment.insertEntry(saleId,Double.parseDouble(tvTotalInserted.getText().toString()),0,new Date());
                cashpayment.insertEntry(saleId,Double.parseDouble(tvTotalInsertedForFirstCurrency.getText().toString()),firstCurrencyId,new Date());
                cashpayment.insertEntry(saleId,Double.parseDouble(tvTotalInsertedForSecondCurrency.getText().toString()),secondCurrencyId,new Date());


                currencyReturnsDBAdapter.insertEntry(saleId,Double.parseDouble(tvExcess.getText().toString()), new Date(),returnSpenerId);
currencyReturnsDBAdapter.close();
                cashpayment.close();

               /** if(spinner2.getSelectedItemId()==1){
                    tvTotalInsertedForDollar.setVisibility(View.VISIBLE);
                    insertedValueForDolar= tvTotalInsertedForDollar.getText().toString();
                    valueInsertedForDolar= (double) (Double.parseDouble(insertedValueForDolar));
                    Toast.makeText(TempCashActivty.this,"for dolar"+valueInsertedForDolar,Toast.LENGTH_LONG).show();
                }
                if(tvTotalInsertedForDollar.isShown())
                {
                    cashpayment.insertEntry(saleId,Double.parseDouble(tvTotalInsertedForDollar.getText().toString()),1,new Date());
                }**/


                finish();
            }
        });


        tvTotalInserted.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //do something here
                    String value = tvTotalInserted.getText().toString();
                    basicCurrencyValue = Double.parseDouble(value);
                    exceesValue = (basicCurrencyValue+firstCurruncyValue+secondCurrency) - totalPrice;
                    tvExcess.setText(exceesValue + " ");
                    if (exceesValue >=0) {
                        btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));

                        btnDone.setEnabled(true);
                        valueForReturnCurrency = tvExcess.getText().toString();

                    }


                return false;
            }
        });
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
                    tvExcess.setText(exceesValue + " ");
                    if (exceesValue >= 0) {
                        btnDone.setEnabled(true);
                        btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));
                        valueForReturnCurrency = tvExcess.getText().toString();

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
                    tvExcess.setText(exceesValue + " ");
                    if (exceesValue >=0) {
                        btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));
                        btnDone.setEnabled(true);
                        valueForReturnCurrency = tvExcess.getText().toString();

                    }

                    currencysDBAdapter.close();
                }
                return false;
            }
        });



        spinnerForReturnValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                CurrencysDBAdapter currencysDBAdapter=new CurrencysDBAdapter(TempCashActivty.this);
                currencysDBAdapter.open();

                returnSpenerId =spinnerForReturnValue.getSelectedItemId();
                Currencys currencys=currencysDBAdapter.getSpeficCurrencys(spinnerForReturnValue.getSelectedItem().toString(),new Date());

                exceesValue= Double.parseDouble(valueForReturnCurrency)/currencys.getRate();
                tvExcess.setText(exceesValue+"");


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
            tvExcess.setTextColor(getResources().getColor(R.color.primaryColor));
        } else {
            btnDone.setEnabled(false);
            btnDone.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
            btnDone.setPadding(50,10,50,10);
            tvExcess.setTextColor(getResources().getColor(R.color.dangerColor));
        }

        tvExcess.setText(String.format("%.2f",deltaPrice+valueInsertedForDolar) + " ");
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

}
