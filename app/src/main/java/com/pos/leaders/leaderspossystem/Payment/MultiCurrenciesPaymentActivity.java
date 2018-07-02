package com.pos.leaders.leaderspossystem.Payment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SalesCartActivity;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.w3c.dom.Text;

public class MultiCurrenciesPaymentActivity extends AppCompatActivity {

    public static final String LEADERS_POS_CASH_MULTI_CURRENCY_TOTAL_PRICE = "LEADERS_POS_CASH_MULTI_CURRENCY_TOTAL_PRICE";
    public static final String RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE = "RESULT_INTENT_CODE_CASH_MULTI_CURRENCY_ACTIVITY_FULL_RESPONSE";

    private double totalPrice, excess, totalPaid = 0.0;

    private char defaultCurrency = '\u20aa';//ILS
    private char excessCurrency = '\u20aa';//ILS

    private TextView tvTotalPrice,tvExcess;
    private LinearLayout llTotalPriceBackground;
    private Button btQuick_1,btQuick_5,btQuick_10,btQuick_20,btQuick_50, btQuick_100;

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
        setQuickPriceButton();

        tvTotalPrice.setText(Util.makePrice(totalPrice) + " " + defaultCurrency);

    }

    private void setQuickPriceButton() {
        btQuick_1 = (Button) findViewById(R.id.MultiCurrenciesPaymentActivity_btQuickPrice_1);
        btQuick_5 = (Button) findViewById(R.id.MultiCurrenciesPaymentActivity_btQuickPrice_5);
        btQuick_10 = (Button) findViewById(R.id.MultiCurrenciesPaymentActivity_btQuickPrice_10);
        btQuick_20 = (Button) findViewById(R.id.MultiCurrenciesPaymentActivity_btQuickPrice_20);
        btQuick_50 = (Button) findViewById(R.id.MultiCurrenciesPaymentActivity_btQuickPrice_50);
        btQuick_100 = (Button) findViewById(R.id.MultiCurrenciesPaymentActivity_btQuickPrice_100);

        btQuick_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    quickPrice

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
}
