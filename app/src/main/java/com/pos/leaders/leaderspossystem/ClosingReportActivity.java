package com.pos.leaders.leaderspossystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Printer.BitmapInvoice;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.List;

public class ClosingReportActivity extends AppCompatActivity {
    EditText cashActualValue , checkActualValue ,creditActualValue ,shekelActualValue ,usdActualValue , eurActualValue , gbpActualValue , totalActualValue;
    TextView cashExpectedValue , checkExpectedValue ,creditExpectedValue ,shekelExpectedValue ,usdExpectedValue , eurAExpectedValue , gbpExpectedValue , totalExpectedValue;
    TextView cashDifferentValue , checkDifferentValue ,creditDifferentValue ,shekelDifferentValue ,usdDifferentValue , eurDifferentValue , gbpDifferentValue , totalDifferentValue;
    Button calculate , print ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closing_report);
        cashActualValue = (EditText) findViewById(R.id.actualCashValue);
        checkActualValue = (EditText) findViewById(R.id.actualCheckValue);
        creditActualValue = (EditText) findViewById(R.id.actualCreditValue);
        shekelActualValue = (EditText) findViewById(R.id.actualShekelValue);
        usdActualValue = (EditText) findViewById(R.id.actualShekelValue);
        eurActualValue = (EditText) findViewById(R.id.actualEurValue);
        gbpActualValue = (EditText) findViewById(R.id.actualGbpValue);
        totalActualValue = (EditText) findViewById(R.id.actualTotalValue);
        cashExpectedValue =(TextView)findViewById(R.id.expectedCashValue);
        checkExpectedValue =(TextView)findViewById(R.id.expectedCheckValue);
        creditExpectedValue =(TextView)findViewById(R.id.expectedCreditValue);
        shekelExpectedValue =(TextView)findViewById(R.id.expectedShekelValue);
        usdExpectedValue =(TextView)findViewById(R.id.expectedUsdValue);
        eurAExpectedValue =(TextView)findViewById(R.id.expectedEurValue);
        gbpExpectedValue =(TextView)findViewById(R.id.expectedGbpValue);
        totalExpectedValue =(TextView)findViewById(R.id.expectedTotalValue);
        cashDifferentValue =(TextView)findViewById(R.id.differentCashValue);
        checkDifferentValue =(TextView)findViewById(R.id.differentCheckValue);
        creditDifferentValue=(TextView)findViewById(R.id.differentCreditValue);
        shekelDifferentValue =(TextView)findViewById(R.id.differentShekelValue);
        usdDifferentValue =(TextView)findViewById(R.id.differentUsdValue);
        eurDifferentValue =(TextView)findViewById(R.id.differentEurValue);
        gbpDifferentValue =(TextView)findViewById(R.id.differentGbpValue);
        totalDifferentValue =(TextView)findViewById(R.id.differentTotalValue);
        calculate = (Button)findViewById(R.id.closingReportBtnCalculate);
        print=(Button)findViewById(R.id.closingReportBtnPrint);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calculate Method Amount
                calculateMethodAmount();

            }
        });




    }

    private void calculateMethodAmount() {
        long aReportId = 0;
        double usd_plus = 0, usd_minus = 0;
        double eur_plus = 0, eur_minus = 0;
        double gbp_plus = 0, gbp_minus = 0;
        double sheqle_plus = 0, sheqle_minus = 0;
        double aReportAmount = 0;
        OrderDBAdapter orderDb = new OrderDBAdapter(getApplicationContext());
        orderDb.open();


        List<Order> orders = orderDb.getBetween(endSaleId, id);

        orderDb.close();

        AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(context);
        aReportDBAdapter.open();
        AReport aReport = aReportDBAdapter.getByLastZReport((int) id);
        try {
            aReportAmount = aReportDBAdapter.getLastRow().getAmount();
            aReportId = aReportDBAdapter.getLastRow().getaReportId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        double aReportDetailsForFirstCurrency=0;
        double aReportDetailsForSecondCurrency=0;
        double aReportDetailsForThirdCurrency=0;
        double aReportDetailsForForthCurrency=0;

        // get payment , cashPayment , returnList
        List<Payment> payments = paymentList(sales);
        List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(sales);
        List<CurrencyReturns> currencyReturnList = returnPaymentList(sales);
        if (SETTINGS.enableCurrencies) {
            AReportDetailsDBAdapter aReportDetailsDBAdapter=new AReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();

            aReportDetailsForFirstCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.Shekel, aReportId);
            aReportDetailsForSecondCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.USD, aReportId);
            aReportDetailsForThirdCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.GBP, aReportId);
            aReportDetailsForForthCurrency = aReportDetailsDBAdapter.getLastRow(CONSTANT.EUR, aReportId);
        }
        double cash_plus = 0, cash_minus = 0;
        double check_plus = 0, check_minus = 0;
        double creditCard_plus = 0, creditCard_minus = 0;
        aReportDBAdapter.close();
        for (Payment p : payments) {
            int i = 0;
            switch (p.getPaymentWay()) {

                case CONSTANT.CASH:
                    if (p.getAmount() > 0)
                        cash_plus += p.getAmount();
                    else
                        cash_minus += p.getAmount();
                    break;
                case CONSTANT.CREDIT_CARD:
                    if (p.getAmount() > 0)
                        creditCard_plus += p.getAmount();
                    else
                        creditCard_minus += p.getAmount();
                    break;
                case CONSTANT.CHECKS:
                    if (p.getAmount() > 0)
                        check_plus += p.getAmount();
                    else
                        check_minus += p.getAmount();
                    break;
            }
        }


//with Currency
        if (SETTINGS.enableCurrencies) {

            for (CurrencyOperation cp : currencyOperationList) {
                switch (cp.getCurrency_type()) {

                    case "ILS":
                        if (cp.getAmount() > 0)
                            sheqle_plus += cp.getAmount();
                        break;
                    case "USD":
                        if (cp.getAmount() > 0)
                            usd_plus += cp.getAmount();
                        break;
                    case "EUR":
                        if (cp.getAmount() > 0)
                            eur_plus += cp.getAmount();

                        break;
                    case "GBP":
                        if (cp.getAmount() > 0)
                            gbp_plus += cp.getAmount();
                        break;
                }
            }
            for (CurrencyReturns cp : currencyReturnList) {
                switch ((int) cp.getCurrency_type()) {

                    case CONSTANT.Shekel:
                        if (cp.getAmount() > 0)
                            sheqle_minus += cp.getAmount();
                        break;
                    case CONSTANT.USD:
                        if (cp.getAmount() > 0)
                            usd_minus += cp.getAmount();
                        break;
                    case CONSTANT.EUR:
                        if (cp.getAmount() > 0)
                            eur_minus += cp.getAmount();

                        break;
                    case CONSTANT.GBP:
                        if (cp.getAmount() > 0)
                            gbp_minus += cp.getAmount();
                        break;
                }
            }

        }
        return BitmapInvoice.xPrint(context, user, date.getTime(), usd_plus+aReportDetailsForSecondCurrency, usd_minus, eur_plus+aReportDetailsForForthCurrency, eur_minus, gbp_plus+aReportDetailsForThirdCurrency, gbp_minus, sheqle_plus+aReportDetailsForFirstCurrency, sheqle_minus, cash_plus, cash_minus, check_plus, check_minus, creditCard_plus, creditCard_minus, Double.parseDouble(Util.makePrice(aReportAmount)));




}
}
