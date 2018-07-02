package com.pos.leaders.leaderspossystem.Payment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SalesCartActivity;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;

public class MultiCurrenciesPaymentActivity extends AppCompatActivity {

    public static final String LEADERS_POS_CASH_MULTI_CURRENCY_TOTAL_PRICE = "LEADERS_POS_CASH_MULTI_CURRENCY_TOTAL_PRICE";
    public static final String RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE = "RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE";

    private double totalPrice, excess, totalPaid = 0.0;

    private char defaultCurrency = '\u20aa';//ILS
    private char excessCurrency = '\u20aa';//ILS

    private TextView tvTotalPrice,tvExcess;
    private LinearLayout llTotalPriceBackground;

    private ListView lvPaymentTable;
    private PaymentTableAdapter paymentTableAdapter;
    private ArrayList<PaymentTable> paymentTables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    }

    private void insertNewRow(double val, String currency) {
        //// TODO: 02/07/2018
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
        insertNewRow(val, defaultCurrency + "");
    }
}
