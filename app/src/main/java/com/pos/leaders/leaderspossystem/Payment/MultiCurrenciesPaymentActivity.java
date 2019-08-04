package com.pos.leaders.leaderspossystem.Payment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.ChecksActivity;
import com.pos.leaders.leaderspossystem.CreditCard.MainCreditCardActivity;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SalesCartActivity;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.SalesCartActivity.REQUEST_CHECKS_ACTIVITY_CODE;
import static com.pos.leaders.leaderspossystem.SalesCartActivity.REQUEST_CREDIT_CARD_ACTIVITY_CODE;

public class MultiCurrenciesPaymentActivity extends AppCompatActivity {
    public static final String LEADERS_POS_CASH_MULTI_CURRENCY_TOTAL_PRICE = "LEADERS_POS_CASH_MULTI_CURRENCY_TOTAL_PRICE";
    public static final String RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE = "RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE";

    private double totalPrice_defaultCurrency, excess_defaultCurrency, totalPaid = 0.0;
    private double totalPrice, excess, selectedCurrencyRate = 1 , valRow=0;
    double actualCurrencyRate=1.0;

    private String defaultCurrency = "ILS";//ILS
//'\u20aa'
    private String excessCurrency = "ILS";//ILS

    private TextView tvTotalPrice,tvExcess,tvTotalPriceWithMultiCurrency,tvActualCurrencyRate;
    private Spinner spCurrency;
    private LinearLayout llTotalPriceBackground,llMultiCurrencyHeaderLayout;
    MultiCurrenciesFragment mcf;

    private ListView lvPaymentTable;
    private PaymentTableAdapter paymentTableAdapter;
    private ArrayList<PaymentTable> paymentTables = new ArrayList<>();
    String currencyType="";
    List<Currency> currenciesList;
    private List<CurrencyType> currencyTypesList = null;
    private List<String> currenciesNames = null;
    Button btCheckOut;
    String currentMethod="",currencyRow="";
    private static android.support.v7.app.ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove notification bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_multi_currencies_payment);

        //region set title bar
        TitleBar.setTitleBar(this);
        btCheckOut=(Button)findViewById(R.id.MultiCurrenciesTitlebar_btCheckOut);
        btCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (excess <= 0 && totalPrice>0) {
                    Log.d("PaymentTables",paymentTables.toString());
                    Intent i = new Intent();
                    i.putExtra(RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE, paymentTables.toString());
                    i.putExtra( SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE,totalPrice);
                    setResult(RESULT_OK, i);
                    finish();
                }
                if(excess<=0 && totalPrice<0){
                    paymentTables.add(new PaymentTable(Double.parseDouble(Util.makePrice(totalPrice)),0, 0, "", new CurrencyType(1l, defaultCurrency + ""),1));
                    insertNewRow(totalPrice*-1, spCurrency.getSelectedItem().toString(),getCurrencyRate(spCurrency.getSelectedItem().toString()),PaymentMethod.CASH);
                    Log.d("PaymentTables",paymentTables.toString());
                    Intent i = new Intent();
                    i.putExtra(RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE, paymentTables.toString());
                    i.putExtra( SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE,totalPrice);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });
        //endregion title bar

        //check extras
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            totalPrice = (double) extras.get(SalesCartActivity.COM_POS_LEADERS_LEADERSPOSSYSTEM_MAIN_ACTIVITY_CART_TOTAL_PRICE);
        } else {
            onBackPressed();
        }
        tvTotalPrice = (TextView) findViewById(R.id.MultiCurrenciesPaymentActivity_tvTotalPriceValue);
        tvTotalPriceWithMultiCurrency = (TextView) findViewById(R.id.MultiCurrenciesPaymentActivity_tvTotalPriceValueWithMultiCurrency);
        tvExcess = (TextView) findViewById(R.id.MultiCurrenciesPaymentActivity_tvReturn);
        llTotalPriceBackground = (LinearLayout) findViewById(R.id.MultiCurrenciesPaymentActivity_llPriceBackgrounf);
        llMultiCurrencyHeaderLayout = (LinearLayout) findViewById(R.id.MultiCurrenciesPaymentActivity_llHeader);
        lvPaymentTable = (ListView) findViewById(R.id.MultiCurrenciesPaymentActivity_lvPaymentList);
        spCurrency = (Spinner) findViewById(R.id.MultiCurrenciesPaymentActivity_spCurrency);
        tvTotalPrice.setText(Util.makePrice(totalPrice) + " " + defaultCurrency);
        tvActualCurrencyRate=(TextView)findViewById(R.id.MultiCurrenciesPaymentActivity_tvActualCurrencyRate);
        paymentTables.add(new PaymentTable(Double.parseDouble(Util.makePrice(totalPrice)), Double.NaN, Double.NaN, "", new CurrencyType(1l, defaultCurrency + ""),1));

        paymentTableAdapter = new PaymentTableAdapter(MultiCurrenciesPaymentActivity.this, R.layout.list_adapter_multi_currencies_payment, paymentTables);
/**
        //set list view header
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_adapter_multi_currenceies_payment_header, lvPaymentTable, false);
        lvPaymentTable.addHeaderView(header, null, false);**/

        //set list value
        lvPaymentTable.setAdapter(paymentTableAdapter);
        paymentTableAdapter.notifyDataSetChanged();

        //set fragment
        mcf = new MultiCurrenciesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.MultiCurrenciesPaymentActivity_flNumberPad, mcf);
        transaction.commit();

        //region spinner
        //Getting default currencies name and values
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        //get currency value
        CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(MultiCurrenciesPaymentActivity.this);
        currencyDBAdapter.open();
        currenciesList = currencyDBAdapter.getAllCurrencyLastUpdate(currencyTypesList);
        currencyDBAdapter.close();

        currenciesNames = new ArrayList<String>();
        for (int i = 0; i < currencyTypesList.size(); i++) {
            currenciesNames.add(currencyTypesList.get(i).getType());
        }

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currenciesNames);

        // Drop down layout style - list view with radio button
        // attaching data adapter to spinner
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spCurrency.setAdapter(dataAdapter);
        spCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCurrencyRate = getCurrencyRate(currenciesNames.get(position));
                actualCurrencyRate=Double.parseDouble(Util.makePrice(totalPrice)) / Double.parseDouble(Util.makePrice((totalPrice / selectedCurrencyRate)));
                setExcess();
                if(selectedCurrencyRate>1) {
                    tvTotalPriceWithMultiCurrency.setVisibility(View.VISIBLE);
                    tvActualCurrencyRate.setVisibility(view.VISIBLE);
                    tvActualCurrencyRate.setText(actualCurrencyRate+"");

                    tvTotalPriceWithMultiCurrency.setText(Double.parseDouble(Util.makePrice(totalPrice))+"/"+selectedCurrencyRate+"="+Util.makePrice(totalPrice / actualCurrencyRate) + " " + currenciesNames.get(position));
                }else {

                    actualCurrencyRate=1;
                    tvTotalPriceWithMultiCurrency.setVisibility(View.GONE);
                    tvTotalPriceWithMultiCurrency.setText("");
                    tvActualCurrencyRate.setVisibility(View.GONE);
                    tvActualCurrencyRate.setText("");

                }
                mcf.currencySpinner.setSelection(position);
                mcf.paymentMethodSpinner.setSelection(position);
                updateLastRow();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //endregion spinner
        setExcess();
        updateView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void deleteRow(int position) {
        paymentTables.remove(position);
        ArrayList<PaymentTable> tempArray = new ArrayList<>(paymentTables);
        paymentTables.clear();
        paymentTables.add(new PaymentTable(Double.parseDouble(Util.makePrice(totalPrice)), Double.NaN, Double.NaN, "", new CurrencyType(1l, defaultCurrency + ""),1));
        excess = totalPrice/getCurrencyRate(tempArray.get(0).getCurrency().getType());
        totalPaid = 0;
        for(int i=0;i<tempArray.size()-1;i++) {
            insertNewRow(tempArray.get(i).getTendered(), tempArray.get(i).getCurrency().getType(), getCurrencyRate(tempArray.get(i).getCurrency().getType()), tempArray.get(i).getPaymentMethod());
        }
        if (tempArray.size()-1 == 0) {
            setExcess();
            updateView();
            updateLastRow();
        }
    }

    private void updateLastRow() {
        PaymentTable lastPaymentTable = paymentTables.get(paymentTables.size() - 1);
        lastPaymentTable.setDue(Double.parseDouble(Util.makePrice(excess)));
        lastPaymentTable.setCurrency(new CurrencyType(1l,spCurrency.getSelectedItem().toString()));
        lastPaymentTable.setTendered(Double.NaN);
        paymentTableAdapter.notifyDataSetChanged();
    }

    private void insertNewRow(double val, String currency,double currencyRate,String paymentMethod) {
     //   if(val>0&&val<=10000){
            if(excess<=0)
                return;
        if(paymentMethod.equals(PaymentMethod.CASH)) {
            //get currency rate
            totalPaid += val * actualCurrencyRate;
            double beforeChangeExcess = excess;
            setExcess();
            updateView();
            paymentTables.add(paymentTables.size() - 1, new PaymentTable(beforeChangeExcess, val, ((excess <= 0) ? (excess) : Double.NaN), PaymentMethod.CASH, new CurrencyType(1, currency + ""), actualCurrencyRate));
            updateLastRow();
            lvPaymentTable.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        }else if(paymentMethod.equals(PaymentMethod.CHECK)) {
            currentMethod= CONSTANT.CHECKS;
            valRow=val;
            currencyRow=currency;
            Intent intent = new Intent(MultiCurrenciesPaymentActivity.this, ChecksActivity.class);

            intent.putExtra("_Price", val);
            intent.putExtra("_custmer", "general");

            startActivityForResult(intent, REQUEST_CHECKS_ACTIVITY_CODE);

        }
        else if(paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            currentMethod= CONSTANT.CREDIT_CARD;
            valRow=val;
            currencyRow=currency;
            Intent intent = new Intent(MultiCurrenciesPaymentActivity.this, MainCreditCardActivity.class);
            intent.putExtra(MainCreditCardActivity.LEADERS_POS_CREDIT_CARD_TOTAL_PRICE, val);
            startActivityForResult(intent, REQUEST_CREDIT_CARD_ACTIVITY_CODE);

        }
      /*  }
        else {
            Toast.makeText(MultiCurrenciesPaymentActivity.this,getString(R.string.please_insert_correct_value_in_multi_currency_activity),Toast.LENGTH_LONG).show();
        }*/
    }

    private void setExcess() {
        excess = Double.parseDouble(Util.makePrice(totalPrice/actualCurrencyRate)) - Double.parseDouble(Util.makePrice(totalPaid/actualCurrencyRate)) ;
        tvExcess.setText(Util.makePrice(excess));
    }

    private void updateView() {
        if (excess <= 0) {
            llTotalPriceBackground.setBackgroundColor(getResources().getColor(R.color.light_green1));
            btCheckOut.setBackground(getResources().getDrawable(R.drawable.bt_green_enabled));

        } else {
        llTotalPriceBackground.setBackgroundColor(getResources().getColor(R.color.light_dangers1));
         btCheckOut.setBackground(getResources().getDrawable(R.drawable.bt_dark));

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
        insertNewRow(val, mcf.currencySpinner.getSelectedItem().toString(),getCurrencyRate(mcf.currencySpinner.getSelectedItem().toString()), mcf.paymentMethodSpinner.getSelectedItem().toString());
    }

    public void multiCurrenciesConfirmClick(View v) {
        if (v.getId() ==R.id.multiCurrenciesFragment_btAddPayment) {
            if((mcf.amount.getText().toString()!="")) {
                currencyType = String.valueOf(mcf.currencySpinner.getSelectedItem().toString());
                double val = Double.parseDouble(mcf.amount.getText().toString());
                insertNewRow(val, currencyType + "", getCurrencyRate(currencyType), mcf.paymentMethodSpinner.getSelectedItem().toString());
                mcf.clearScreen();
            }
        }
        if (v.getId() ==R.id.list_header_multi_currencies_payment_delete) {
            paymentTables.clear();
            excess=totalPrice;
            totalPaid=0;
            paymentTables.add(new PaymentTable(Double.parseDouble(Util.makePrice(totalPrice)), Double.NaN, Double.NaN, "", new CurrencyType(1l, defaultCurrency + ""),actualCurrencyRate));
            setExcess();
            updateView();
            paymentTableAdapter.notifyDataSetChanged();
        }
    }
    public double getCurrencyRate(String currencyType){
        for (int i=0;i<currenciesList.size();i++){
            if(currenciesList.get(i).getCountry().equals(currencyType)) {
                return currenciesList.get(i).getRate();
            }
        }
        return 1;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            new AlertDialog.Builder(MultiCurrenciesPaymentActivity.this)
                    .setTitle(getString(R.string.cancel_invoice))
                    .setMessage(getString(R.string.are_you_want_to_cancel_payment_activity))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECKS_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                //get currency rate
                totalPaid += valRow * actualCurrencyRate;
                double beforeChangeExcess = excess;
                setExcess();
                updateView();
                paymentTables.add(paymentTables.size() - 1, new PaymentTable(beforeChangeExcess, valRow, ((excess <= 0) ? (excess) : Double.NaN), PaymentMethod.CHECK, new CurrencyType(1, currencyRow + ""), actualCurrencyRate));
                updateLastRow();
                lvPaymentTable.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            }
        }
        if (requestCode == REQUEST_CREDIT_CARD_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                //get currency rate
                totalPaid += valRow * actualCurrencyRate;
                double beforeChangeExcess = excess;
                setExcess();
                updateView();
                paymentTables.add(paymentTables.size() - 1, new PaymentTable(beforeChangeExcess, valRow, ((excess <= 0) ? (excess) : Double.NaN), PaymentMethod.CREDIT_CARD, new CurrencyType(1, currencyRow + ""), actualCurrencyRate));
                updateLastRow();
                lvPaymentTable.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            }
        }
    }

}
