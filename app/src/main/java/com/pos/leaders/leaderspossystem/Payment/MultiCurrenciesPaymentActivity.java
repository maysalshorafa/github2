package com.pos.leaders.leaderspossystem.Payment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SalesCartActivity;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

public class MultiCurrenciesPaymentActivity extends AppCompatActivity {

    public static final String LEADERS_POS_CASH_MULTI_CURRENCY_TOTAL_PRICE = "LEADERS_POS_CASH_MULTI_CURRENCY_TOTAL_PRICE";
    public static final String RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE = "RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE";

    private double totalPrice, excess, totalPaid = 0.0;

    private char defaultCurrency = '\u20aa';//ILS

    private char excessCurrency = '\u20aa';//ILS

    private TextView tvTotalPrice,tvExcess;
    private LinearLayout llTotalPriceBackground;
    MultiCurrenciesFragment mcf;

    private ListView lvPaymentTable;
    private PaymentTableAdapter paymentTableAdapter;
    private ArrayList<PaymentTable> paymentTables = new ArrayList<>();
    String currencyType="";
    List<Currency> currenciesList;
    private List<CurrencyType> currencyTypesList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove notification bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_multi_currencies_payment);

        //check extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            totalPrice = (double) extras.get(SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE);
        } else {
            onBackPressed();
        }

        tvTotalPrice = (TextView) findViewById(R.id.MultiCurrenciesPaymentActivity_tvTotalPriceValue);
        tvExcess = (TextView) findViewById(R.id.MultiCurrenciesPaymentActivity_tvReturn);
        llTotalPriceBackground = (LinearLayout) findViewById(R.id.MultiCurrenciesPaymentActivity_llPriceBackgrounf);
        lvPaymentTable = (ListView) findViewById(R.id.MultiCurrenciesPaymentActivity_lvPaymentList);

        tvTotalPrice.setText(Util.makePrice(totalPrice) + " " + defaultCurrency);
        paymentTables.add(new PaymentTable(totalPrice, Double.NaN, Double.NaN, "", new CurrencyType(1l, defaultCurrency + "")));

        paymentTableAdapter = new PaymentTableAdapter(this, R.layout.list_adapter_multi_currencies_payment, paymentTables);

        //set list view header
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_adapter_multi_currenceies_payment_header, lvPaymentTable, false);
        lvPaymentTable.addHeaderView(header, null, false);

        //set list value
        lvPaymentTable.setAdapter(paymentTableAdapter);
        paymentTableAdapter.notifyDataSetChanged();

        //set fragment
        mcf = new MultiCurrenciesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.MultiCurrenciesPaymentActivity_flNumberPad, mcf);
        transaction.commit();

        //Getting default currencies name and values
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        //get currency value
        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(MultiCurrenciesPaymentActivity.this);
        currencyDBAdapter.open();
        currenciesList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);


    }

    private void insertNewRow(double val, String currency,double currencyRate) {
        //get currency rate
        totalPaid += val*currencyRate;
        setExcess();
        updateView();

        PaymentTable lastPaymentTable = paymentTables.get(paymentTables.size() - 1);
        //(double due, double tendered, double change, String paymentMethod, CurrencyType currency)
        paymentTables.add(paymentTables.size() - 1, new PaymentTable(lastPaymentTable.getDue(), val, ((excess <= 0) ? excess : Double.NaN), PaymentMethod.CASH, new CurrencyType(1, currency + "")));

        lastPaymentTable.setDue(excess);
        lastPaymentTable.setTendered(Double.NaN);
        paymentTableAdapter.notifyDataSetChanged();

    }



    private void setExcess() {
        excess = totalPrice - totalPaid;
        tvExcess.setText(Util.makePrice(excess) + " " + excessCurrency);
    }

    private void updateView() {
        if (excess <= 0) {
            llTotalPriceBackground.setBackgroundColor(R.color.light_green1);
        } else {
            llTotalPriceBackground.setBackgroundColor(R.color.light_dangers1);
        }
    }

    public void paymentQuickPriceClick(View v) {
        double val = 0;
        switch (v.getId()) {
            case R.id.MultiCurrenciesPaymentActivity_btQuickPrice_1:
                val = 1;
                break;
            case R.id.MultiCurrenciesPaymentActivity_btQuickPrice_5:
                val = 5;
                break;
            case R.id.MultiCurrenciesPaymentActivity_btQuickPrice_10:
                val = 10;
                break;
            case R.id.MultiCurrenciesPaymentActivity_btQuickPrice_20:
                val = 20;
                break;
            case R.id.MultiCurrenciesPaymentActivity_btQuickPrice_50:
                val = 50;
                break;
            case R.id.MultiCurrenciesPaymentActivity_btQuickPrice_100:
                val = 100;
                break;
        }
        insertNewRow(val, defaultCurrency + "",getCurrencyRate(defaultCurrency+""));
    }

    public void multiCurrenciesConfirmClick(View v) {
        if (v.getId() ==R.id.multiCurrenciesFragment_btAddPayment) {
            currencyType=String.valueOf(mcf.currencySpinner.getSelectedItem().toString());
            double val = Double.parseDouble(mcf.amount.getText().toString());
            insertNewRow(val, currencyType + "",getCurrencyRate(currencyType));
            mcf.clearScreen();
        }
    }
    public  double getCurrencyRate(String currencyType){
        for (int i=0;i<currenciesList.size();i++){
            if(currenciesList.get(i).getCountry().equals(currencyType)) {
                return currenciesList.get(i).getRate();
            }
        }
          return 1;
    }
}
