package com.pos.leaders.leaderspossystem.Payment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 7/2/2018.
 */

public class MultiCurrenciesFragment extends Fragment {
    String touchPadPressed ="";
    Button bt1 , bt2 , bt3 , bt4 , bt5 , bt6 ,bt7, bt8 , bt9  , btClear , btConfirmation , btDot , bt0,btCredit ;
    ImageButton btCE;
    TextView amount;
    Spinner currencySpinner , paymentMethodSpinner;
    List<Currency> currenciesList;
    private List<CurrencyType> currencyTypesList = null;
    List<String> currenciesNames,paymentMethod;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.multi_currencies_fragment, container, false);
        bt9=(Button)v.findViewById(R.id.multiCurrenciesFragment_bt9);
        bt8=(Button)v.findViewById(R.id.multiCurrenciesFragment_bt8);
        bt7=(Button)v.findViewById(R.id.multiCurrenciesFragment_bt7);
        bt6=(Button)v.findViewById(R.id.multiCurrenciesFragment_bt6);
        bt5=(Button)v.findViewById(R.id.multiCurrenciesFragment_bt5);
        bt4=(Button)v.findViewById(R.id.multiCurrenciesFragment_bt4);
        bt3=(Button)v.findViewById(R.id.multiCurrenciesFragment_bt3);
        bt2=(Button)v.findViewById(R.id.multiCurrenciesFragment_bt2);
        bt1=(Button)v.findViewById(R.id.multiCurrenciesFragment_bt1);
        bt0=(Button)v.findViewById(R.id.multiCurrenciesFragment_bt0);
        btCE=(ImageButton) v.findViewById(R.id.multiCurrenciesFragment_btCE);
        btClear=(Button)v.findViewById(R.id.multiCurrenciesFragment_btClear);
        btDot=(Button)v.findViewById(R.id.multiCurrenciesFragment_btDot);
        btCredit=(Button)v.findViewById(R.id.multiCurrenciesFragment_btCredit);

        btConfirmation=(Button)v.findViewById(R.id.multiCurrenciesFragment_btAddPayment);
        amount=(TextView)v.findViewById(R.id.multiCurrenciesFragment_amount);
        currencySpinner=(Spinner)v.findViewById(R.id.multiCurrenciesFragment_currenciesSpinner);
        if (!SETTINGS.enableCurrencies){
            currencySpinner.setEnabled(false);}
        paymentMethodSpinner=(Spinner)v.findViewById(R.id.multiCurrenciesFragment_paymentWaySpinner);
        //Getting default currencies name and values
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(getContext());
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(getContext());
        currencyDBAdapter.open();
        currenciesList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
        currenciesNames = new ArrayList<String>();
        paymentMethod=new ArrayList<>();
        for (int i = 0; i < currencyTypesList.size(); i++) {
            currenciesNames.add(currencyTypesList.get(i).getType());
        }
        paymentMethod.add(0,getString(R.string.cash));
        paymentMethod.add(1,getString(R.string.checks));
        paymentMethod.add(2,getString(R.string.credit_card));
        paymentMethod.add(3,getString(R.string.pinpad));
        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, currenciesNames);
        final ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, paymentMethod);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        currencySpinner.setAdapter(dataAdapter);
        paymentMethodSpinner.setAdapter(dataAdapter1);
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadPressed += 9;
                amount.setText(touchPadPressed);
            }
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadPressed += 8;
                amount.setText(touchPadPressed);

            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadPressed += 7;
                amount.setText(touchPadPressed);

            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadPressed += 6;
                amount.setText(touchPadPressed);

            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadPressed += 5;
                amount.setText(touchPadPressed);

            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadPressed += 4;
                amount.setText(touchPadPressed);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadPressed += 3;
                amount.setText(touchPadPressed);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadPressed += 2;
                amount.setText(touchPadPressed);
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadPressed += 1;
                amount.setText(touchPadPressed);
            }
        });
        bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchPadPressed += 0;
                amount.setText(touchPadPressed);

            }
        });
        btCE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!touchPadPressed.equals(""))
                    touchPadPressed = Util.removeLastChar(touchPadPressed);
                amount.setText(touchPadPressed);
            }
        });
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clearScreen();
            }
        });
        btDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (touchPadPressed.indexOf(".") < 0)
                    touchPadPressed += ".";
                amount.setText(touchPadPressed);
            }
        });

        btCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SESSION._EMPLOYEE.getEmployeeId()!=2) {
                    Toast.makeText(getContext(), "This Operation just for master employee !!", Toast.LENGTH_LONG).show();

                }else {
                    if (!touchPadPressed.equals("")) {
                        double newValue = Util.convertSign(Double.parseDouble(touchPadPressed));
                        touchPadPressed = String.valueOf(newValue);
                        amount.setText(touchPadPressed);

                    }
                }

            }
        });
        return v;
    }
    public void clearScreen() {
        touchPadPressed="";
        amount.setText(touchPadPressed);
    }
}
