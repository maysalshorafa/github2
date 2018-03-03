package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;

import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CurrencyReturnsCustomDialogActivity extends Dialog {

    private Activity c;
    private Button done;
    private TextView tvExcess;
    private Spinner returnSpener;
    private double excess = 0;
    Currency rCurrency;
    private Sale sale;

    public CurrencyReturnsCustomDialogActivity() {
        super(null);
    }

    public CurrencyReturnsCustomDialogActivity(@NonNull Context context, @StyleRes int themeResId, Activity c) {
        super(context, themeResId);
        this.c = c;
    }

    public CurrencyReturnsCustomDialogActivity(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, Activity c) {
        super(context, cancelable, cancelListener);
        this.c = c;
    }

    public CurrencyReturnsCustomDialogActivity(Activity a, double excess, Sale sale) {
        super(a);
        this.c = a;
        this.excess = excess;
        this.sale = sale;
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
        if (!SETTINGS.enableCurrencies) {
            returnSpener.setVisibility(View.INVISIBLE);
        }
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this.c);
        currencyTypeDBAdapter.open();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SETTINGS.enableCurrencies) {
                    CurrencyReturnsDBAdapter currencyReturnsDBAdapter = new CurrencyReturnsDBAdapter(getContext());
                    currencyReturnsDBAdapter.open();
                    double returnCurrencyValue = Double.parseDouble(tvExcess.getText().toString());
                    currencyReturnsDBAdapter.insertEntry(sale.getId(), returnCurrencyValue, new Date(), rCurrency.getId());
                    currencyReturnsDBAdapter.close();
                }
                cancel();
            }
        });

        List<CurrencyType> currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(getContext());
        currencyDBAdapter.open();
        final List<Currency> currencyList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
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
                tvExcess.setText(Util.makePrice(excess / currency.getRate()));
                rCurrency = currencyList.get(arg2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
}
