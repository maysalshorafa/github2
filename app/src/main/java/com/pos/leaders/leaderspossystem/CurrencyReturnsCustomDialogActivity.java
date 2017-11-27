package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;

import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.CashActivity;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class CurrencyReturnsCustomDialogActivity extends Dialog{

    private Activity c;
    private Button done;
    private TextView tvExcess;
    private Spinner returnSpener;
    private double excess =0;
    Currency rCurrency;
    public CurrencyReturnsCustomDialogActivity(Activity a,double excess) {
        super(a);
        this.c = a;
        this.excess = excess;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        done = (Button) findViewById(R.id.btn_done);

        setCanceledOnTouchOutside(false);
        tvExcess = (TextView) findViewById(R.id.cashActivity_TVExcess);
        returnSpener = (Spinner) findViewById(R.id.spinnerForReturnValue);
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this.c);
        currencyTypeDBAdapter.open();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrencyReturnsDBAdapter currencyReturnsDBAdapter = new CurrencyReturnsDBAdapter(getContext());
                currencyReturnsDBAdapter.open();
               double returnCurrencyValue = Double.parseDouble(tvExcess.getText().toString());
                currencyReturnsDBAdapter.insertEntry(SESSION._SALE.getId(),returnCurrencyValue , new Date(), rCurrency.getId());
                currencyReturnsDBAdapter.close();
                cancel();
            }
        });

        List<CurrencyType> currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(getContext());
        currencyDBAdapter.open();
        final List<Currency> currencyList = currencyDBAdapter.getAllCurrency(currencyTypesList);
        currencyDBAdapter.close();

        final List<String> currency = new ArrayList<String>();
        for (int i = 0; i < currencyTypesList.size(); i++) {
            currency.add(currencyTypesList.get(i).getType());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, currency);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rCurrency = currencyList.get(0);

        // attaching data adapter to spinner
        returnSpener.setAdapter(dataAdapter);

        tvExcess.setText(String.format(new Locale("en"), "%.2f", excess));

        returnSpener.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Currency currency = currencyList.get(arg2);
                tvExcess.setText(Util.makePrice(excess/ currency.getRate()));
                rCurrency = currencyList.get(arg2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
}
