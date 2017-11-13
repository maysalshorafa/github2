package com.pos.leaders.leaderspossystem.Tools;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;

import com.pos.leaders.leaderspossystem.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TempCashActivty extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private List<CurrencyType> currencyTypesList = null;
    public static final String LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY = "LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_FIRSTCURRENCY_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_FIRSTCURRENCY_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_ID_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_ID_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_ID_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_ID_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_EXCESSVALUE = "LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_EXCESSVALUE";


    TextView custmer_name;
    TextView tv, tvTotalInserted;
    EditText tvTotalInsertedForFirstCurrency, tvTotalInsertedForSecondCurrency;
    String custmer_nameS;
    Spinner spinnerForFirstCurrency, spinnerForSecondCurrency, spinnerForTotalPrice;

    Button btnDone;

    double totalPrice = 0.0;
    float totalPid = 0.0f;

    long saleId = 0;
    long firstCurrencyId, secondCurrencyId, totalPriceSpinnerId = 0;

    String insertedValueForFirstCurrency = "0", insertedValueForSecondCurrency = "0", valueForTotalPriceCurrency, insertedValue;

    CashPaymentDBAdapter cashpayment;
    double firstCurruncyValue, secondCurrency, exceesValue, valueForTotalPrice = 0;
    DateConverter dateConverter;
    Date today;

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


        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.85));
        dateConverter = new DateConverter();
        today = new Date();
        dateConverter.toDate(today);

        custmer_name = (TextView) findViewById(R.id.custmer_name);
        spinnerForFirstCurrency = (Spinner) findViewById(R.id.spinnerForFirstCurrency);
        spinnerForSecondCurrency = (Spinner) findViewById(R.id.spinnerForSecondCurrency);
        spinnerForTotalPrice = (Spinner) findViewById(R.id.spinnerForTotalPrice);


        btnDone = (Button) findViewById(R.id.cashActivity_BTNDone);
        btnDone.setTextAppearance(getApplicationContext(), R.style.AppTheme_bt_dark);
        tv = (TextView) findViewById(R.id.cashActivity_TVRequired);
        tvTotalInserted = (TextView) findViewById(R.id.cashActivity_TVTotalInserted);
        tvTotalInsertedForFirstCurrency = (EditText) findViewById(R.id.cashActivity_TVTotalInsertedForfirstCurrency);
        tvTotalInsertedForSecondCurrency = (EditText) findViewById(R.id.cashActivity_TVTotalInsertedForSecondCurrency);

        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
        currencyTypeDBAdapter.open();


        cashpayment = new CashPaymentDBAdapter(this);
        cashpayment.open();
        spinnerForFirstCurrency.setOnItemSelectedListener(this);
        spinnerForSecondCurrency.setOnItemSelectedListener(this);
        spinnerForTotalPrice.setOnItemSelectedListener(this);
        tvTotalInsertedForSecondCurrency.setOnClickListener(this);
        tvTotalInsertedForFirstCurrency.setOnClickListener(this);
        btnDone.setEnabled(false);
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();

        final List<String> currency = new ArrayList<String>();
        for (int i = 0; i < currencyTypesList.size(); i++) {
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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            totalPrice = (double) extras.get("_Price");
            custmer_nameS = (String) extras.get("_custmer");
            saleId = (long) extras.get("_SaleId");
            tv.setText(totalPrice + " ");
            valueForTotalPriceCurrency = tv.getText().toString();

            custmer_name.setText(custmer_nameS);
            custmer_nameS = "";
        } else {
            finish();
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                totalPid = (float) (firstCurruncyValue + secondCurrency);
                //// TODO: 01/12/2016 return how match inserted money
                Intent i = new Intent();
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY, totalPid);
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_FIRSTCURRENCY_AMOUNT, Double.parseDouble(tvTotalInserted.getText().toString()));
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_AMOUNT, firstCurruncyValue);
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_AMOUNT, secondCurrency);
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_SECONDCURRENCY_ID_AMOUNT, firstCurrencyId);
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_THIRDCURRENCY_ID_AMOUNT, secondCurrencyId);
                setResult(RESULT_OK, i);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(TempCashActivty.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat(LEAD_POS_RESULT_INTENT_CODE_Temp_CASH_ACTIVITY_EXCESSVALUE, (float) exceesValue);
                editor.apply();
                CurrencyReturnsCustomDialogActivity cdd = new CurrencyReturnsCustomDialogActivity(TempCashActivty.this);
                cdd.show();
            }
        });

        tvTotalInsertedForFirstCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(TempCashActivty.this);
                currencyDBAdapter.open();
                firstCurrencyId = spinnerForFirstCurrency.getSelectedItemId();

                Currency currency = currencyDBAdapter.getSpeficCurrencys(spinnerForFirstCurrency.getSelectedItem().toString());
                insertedValueForFirstCurrency = tvTotalInsertedForFirstCurrency.getText().toString();
                try {
                    firstCurruncyValue = (double) (Double.parseDouble(insertedValueForFirstCurrency) * currency.getRate());
                } catch (NumberFormatException e) {
                    firstCurruncyValue = 0; // your default value
                }
                tvTotalInserted.setText(Double.toString(firstCurruncyValue + secondCurrency));
                if (firstCurruncyValue + secondCurrency >= totalPrice)
                    tvTotalInserted.setTextColor(getResources().getColor(R.color.Green));
                else
                    tvTotalInserted.setTextColor(Color.BLACK);

                exceesValue = (firstCurruncyValue + secondCurrency) - totalPrice;
                if (exceesValue >= 0) {
                    btnDone.setEnabled(true);
                    btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));
                } else {
                    btnDone.setEnabled(false);
                    btnDone.setBackground(getResources().getDrawable(R.drawable.btn_primary));
                }
                currencyDBAdapter.close();
            }
        });


        tvTotalInsertedForSecondCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(TempCashActivty.this);
                currencyDBAdapter.open();
                secondCurrencyId = spinnerForSecondCurrency.getSelectedItemId();
                Currency currency = currencyDBAdapter.getSpeficCurrencys(spinnerForSecondCurrency.getSelectedItem().toString());
                insertedValueForSecondCurrency = tvTotalInsertedForSecondCurrency.getText().toString();
                try {
                    secondCurrency = (double) (Double.parseDouble(insertedValueForSecondCurrency) * currency.getRate());
                } catch (NumberFormatException e) {
                    secondCurrency = 0; // your default value
                }
                tvTotalInserted.setText(Double.toString(firstCurruncyValue + secondCurrency));
                if (firstCurruncyValue + secondCurrency >= totalPrice) {
                    tvTotalInserted.setTextColor(getResources().getColor(R.color.Green));
                } else {


                    tvTotalInserted.setTextColor(Color.BLACK);
                }
                exceesValue = (firstCurruncyValue + secondCurrency) - totalPrice;
                if (exceesValue >= 0) {
                    btnDone.setEnabled(true);
                    btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));
                } else {
                    btnDone.setEnabled(false);
                    btnDone.setBackground(getResources().getDrawable(R.drawable.btn_primary));
                }
                currencyDBAdapter.close();
            }
        });

        spinnerForTotalPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(TempCashActivty.this);
                currencyDBAdapter.open();
                totalPriceSpinnerId = spinnerForTotalPrice.getSelectedItemId();
                Currency currency = currencyDBAdapter.getSpeficCurrencys(spinnerForTotalPrice.getSelectedItem().toString());

                valueForTotalPrice = Double.parseDouble(valueForTotalPriceCurrency) / currency.getRate();
                tv.setText(valueForTotalPrice + "");
                Toast.makeText(TempCashActivty.this, "rate" + currency.getRate(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
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
