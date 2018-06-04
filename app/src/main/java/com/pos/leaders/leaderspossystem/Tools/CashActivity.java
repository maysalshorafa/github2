package com.pos.leaders.leaderspossystem.Tools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.MainActivity;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;

import com.pos.leaders.leaderspossystem.R;

import java.util.ArrayList;
import java.util.List;

public class CashActivity extends AppCompatActivity {
    private List<CurrencyType> currencyTypesList = null;
    public static final String LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_TOTAL_PAID = "LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_TOTAL_PAID";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_FIRST_CURRENCY_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_TEMP_CASH_ACTIVITY_FIRST_CURRENCY_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_SECOND_CURRENCY_AMOUNT = "LEAD_POS_RESULT_INTENT_CODE_TEMP_CASH_ACTIVITY_SECOND_CURRENCY_AMOUNT";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_SECOND_CURRENCY_ID = "LEAD_POS_RESULT_INTENT_CODE_TEMP_CASH_ACTIVITY_SECOND_CURRENCY_ID";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_FIRST_CURRENCY_ID = "LEAD_POS_RESULT_INTENT_CODE_TEMP_CASH_ACTIVITY_FIRST_CURRENCY_ID";
    public static final String LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_EXCESS_VALUE = "LEAD_POS_RESULT_INTENT_CODE_TEMP_CASH_ACTIVITY_EXCESS_VALUE";

    TextView tvTotalPrice, tvTotalPaid, tvTotalPaidText, tvRequiredAmount;
    EditText etFirstCurrency, etSecondCurrency;
    Spinner sFirstCurrency, sSecondCurrency, spinnerForTotalPrice, spinnerForRequiredAmount;

    Button btnDone;

    double totalPrice = 0.0;
    double totalPid = 0.0f;
    double firstCurrencyInDefaultValue = 0, secondCurrencyInDefaultValue = 0, totalPriceInDefaultValue = 0, requiredAmount = 0;
    double excessValue;


    List<Currency> currenciesList;
    List<String> currenciesNames;

    Currency totalPriceCurrency;
    Currency fCurrency;
    Currency sCurrency;
    Currency rCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set pop up window
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


        //check extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            totalPrice = (double) extras.get(MainActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE);
            totalPriceInDefaultValue = totalPrice;
        } else {
            onBackPressed();
        }


        //connect object to views
        btnDone = (Button) findViewById(R.id.cashActivity_BTNDone);

        btnDone.setEnabled(false);

        tvTotalPrice = (TextView) findViewById(R.id.cashActivity_TVRequired);
        tvTotalPrice.setText(valueView(totalPrice));
        tvTotalPaidText = (TextView) findViewById(R.id.cashActivity_TVTotalInsertedText);
        tvTotalPaid = (TextView) findViewById(R.id.cashActivity_TVTotalInserted);
        tvRequiredAmount = (TextView) findViewById(R.id.cashActivity_TVRequiredAmount);
        spinnerForTotalPrice = (Spinner) findViewById(R.id.spinnerForTotalPrice);
        spinnerForRequiredAmount = (Spinner) findViewById(R.id.cashActivity_SPRequiredAmount);

        etFirstCurrency = (EditText) findViewById(R.id.cashActivity_TVTotalInsertedForfirstCurrency);
        etSecondCurrency = (EditText) findViewById(R.id.cashActivity_TVTotalInsertedForSecondCurrency);
        sFirstCurrency = (Spinner) findViewById(R.id.spinnerForFirstCurrency);
        sSecondCurrency = (Spinner) findViewById(R.id.spinnerForSecondCurrency);


        //Getting default currencies name and values
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();

        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(CashActivity.this);
        currencyDBAdapter.open();
        currenciesList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);

        currenciesNames = new ArrayList<String>();
        for (int i = 0; i < currencyTypesList.size(); i++) {
            currenciesNames.add(currencyTypesList.get(i).getType());
        }

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currenciesNames);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sFirstCurrency.setAdapter(dataAdapter);
        sSecondCurrency.setAdapter(dataAdapter);
        spinnerForTotalPrice.setAdapter(dataAdapter);
        spinnerForRequiredAmount.setAdapter(dataAdapter);
        fCurrency = currenciesList.get(0);
        sCurrency = currenciesList.get(0);
        rCurrency = currenciesList.get(0);
        totalPriceCurrency = currenciesList.get(0);

        //set listeners
        //region Spinners
        sFirstCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fCurrency = currenciesList.get(position);
                spinnerChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sSecondCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sCurrency = currenciesList.get(position);
                spinnerChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerForTotalPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                totalPriceCurrency = currenciesList.get(arg2);
                totalPrice = totalPriceInDefaultValue / totalPriceCurrency.getRate();
                tvTotalPrice.setText(valueView(totalPrice));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinnerForRequiredAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rCurrency = currenciesList.get(position);
                tvRequiredAmount.setText(valueView(((totalPriceInDefaultValue) - (firstCurrencyInDefaultValue + secondCurrencyInDefaultValue)) / rCurrency.getRate()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //endregion

        illegalValue();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPid = (firstCurrencyInDefaultValue + secondCurrencyInDefaultValue);

                Intent i = new Intent();
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_TOTAL_PAID, totalPid);
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_FIRST_CURRENCY_ID, fCurrency.getCurrencyId());
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_FIRST_CURRENCY_AMOUNT, firstCurrencyInDefaultValue / fCurrency.getRate());
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_SECOND_CURRENCY_AMOUNT, secondCurrencyInDefaultValue / sCurrency.getRate());
                i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_SECOND_CURRENCY_ID, sCurrency.getCurrencyId());
                if(totalPriceInDefaultValue<0){
                    i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_EXCESS_VALUE, 0);

                }else {
                    i.putExtra(LEAD_POS_RESULT_INTENT_CODE_CASH_ACTIVITY_EXCESS_VALUE, excessValue);
                }

                setResult(RESULT_OK, i);

                finish();
            }
        });

        etFirstCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etFirstCurrency.getText().toString().equals("")) {
                    firstCurrencyInDefaultValue = Double.parseDouble(etFirstCurrency.getText().toString()) * fCurrency.getRate();

                } else {
                    firstCurrencyInDefaultValue = 0;
                }
                calculatePaid();
            }
        });

        etSecondCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etSecondCurrency.getText().toString().equals("")) {
                    secondCurrencyInDefaultValue = Double.parseDouble(etSecondCurrency.getText().toString()) * sCurrency.getRate();

                } else {
                    secondCurrencyInDefaultValue = 0;
                }
                calculatePaid();
            }
        });

    }

    private void illegalValue() {
        tvTotalPaidText.setTextColor(getResources().getColor(R.color.Red));
        tvTotalPaid.setTextColor(getResources().getColor(R.color.Red));
        btnDone.setEnabled(false);
        btnDone.setBackground(getResources().getDrawable(R.drawable.bt_dangers_pressed));
    }

    private void legalValue() {
        tvTotalPaidText.setTextColor(getResources().getColor(R.color.Green));
        tvTotalPaid.setTextColor(getResources().getColor(R.color.Green));
        btnDone.setEnabled(true);
        btnDone.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));
    }

    private String valueView(double a) {
        return Util.makePrice(a);
    }

    private void calculateExcess() {
        excessValue = (firstCurrencyInDefaultValue + secondCurrencyInDefaultValue) - totalPriceInDefaultValue;
        if (excessValue >= 0)
            legalValue();
        else
            illegalValue();
    }

    private void calculatePaid() {
        tvTotalPaid.setText(valueView((firstCurrencyInDefaultValue + secondCurrencyInDefaultValue) / totalPriceCurrency.getRate()));
        tvRequiredAmount.setText(valueView(((totalPriceInDefaultValue) - (firstCurrencyInDefaultValue + secondCurrencyInDefaultValue)) / rCurrency.getRate()));
        calculateExcess();
    }

    private void spinnerChanged() {
        if (!etFirstCurrency.getText().toString().equals(""))
            firstCurrencyInDefaultValue = Double.parseDouble(etFirstCurrency.getText().toString()) * fCurrency.getRate();
        if (!etSecondCurrency.getText().toString().equals(""))
            secondCurrencyInDefaultValue = Double.parseDouble(etSecondCurrency.getText().toString()) * sCurrency.getRate();
        calculatePaid();
    }

}