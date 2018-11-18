package com.pos.leaders.leaderspossystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import java.util.ArrayList;
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
                try {
                    calculateMethodAmount();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });




    }

  private void calculateMethodAmount() throws Exception {
        long aReportId = 0;
        double usd_plus = 0, usd_minus = 0;
        double eur_plus = 0, eur_minus = 0;
        double gbp_plus = 0, gbp_minus = 0;
        double sheqle_plus = 0, sheqle_minus = 0;
        double aReportAmount = 0;
        OrderDBAdapter orderDb = new OrderDBAdapter(getApplicationContext());
        orderDb.open();
      OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(getApplicationContext());
      opiningReportDBAdapter.open();
      OpiningReport opiningReport = opiningReportDBAdapter.getLastRow();
      Order order =orderDb.getLast();

        List<Order> orders = orderDb.getBetween(opiningReport.getLastOrderId(), order.getOrderId());

        orderDb.close();

        double aReportDetailsForFirstCurrency=0;
        double aReportDetailsForSecondCurrency=0;
        double aReportDetailsForThirdCurrency=0;
        double aReportDetailsForForthCurrency=0;

        // get payment , cashPayment , returnList
        List<Payment> payments = paymentList(orders);
        List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(orders);
        List<CurrencyReturns> currencyReturnList = returnPaymentList(orders);
        double cash_plus = 0, cash_minus = 0;
        double check_plus = 0, check_minus = 0;
        double creditCard_plus = 0, creditCard_minus = 0;
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
        

      //  return BitmapInvoice.xPrint(context, user, date.getTime(), usd_plus+aReportDetailsForSecondCurrency, usd_minus, eur_plus+aReportDetailsForForthCurrency, eur_minus, gbp_plus+aReportDetailsForThirdCurrency, gbp_minus, sheqle_plus+aReportDetailsForFirstCurrency, sheqle_minus, cash_plus, cash_minus, check_plus, check_minus, creditCard_plus, creditCard_minus, Double.parseDouble(Util.makePrice(aReportAmount)));
}
    // get Payment List
    public List<Payment> paymentList(List<Order> sales) {
        List<Payment> pl = new ArrayList<Payment>();
        PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(getApplicationContext());
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(getApplicationContext());
        CurrencyReturnsDBAdapter currencyReturnsDBAdapter = new CurrencyReturnsDBAdapter(getApplicationContext());
        paymentDBAdapter.open();
        for (Order s : sales) {
            List<Payment> payments = paymentDBAdapter.getPaymentBySaleID(s.getOrderId());
            /**
             if (SETTINGS.enableCurrencies) {
             for (Payment _p : payments) {
             _p.setCashPayments(cashPaymentDBAdapter.getPaymentBySaleID(_p.getOrderId()));
             _p.setCurrencyReturns(currencyReturnsDBAdapter.getCurencyReturnBySaleID(_p.getOrderId()));
             }
             }**/

            pl.addAll(payments);
        }
        paymentDBAdapter.close();
        return pl;
    }

    // get Cash Payment List
    public  List<CashPayment> cashPaymentList(List<Order> sales) {
        List<CashPayment> pl = new ArrayList<CashPayment>();
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(getApplicationContext());
        cashPaymentDBAdapter.open();
        for (Order s : sales) {
            List<CashPayment> payments = cashPaymentDBAdapter.getPaymentBySaleID(s.getOrderId());
            pl.addAll(payments);
        }
        cashPaymentDBAdapter.close();
        return pl;
    }

    // get Currency Return  List
    public  List<CurrencyReturns> returnPaymentList(List<Order> sales) {
        List<CurrencyReturns> pl = new ArrayList<CurrencyReturns>();
        CurrencyReturnsDBAdapter currencyDBAdapter = new CurrencyReturnsDBAdapter(getApplicationContext());
        currencyDBAdapter.open();
        for (Order s : sales) {
            List<CurrencyReturns> payments = currencyDBAdapter.getCurencyReturnBySaleID(s.getOrderId());
            pl.addAll(payments);
        }
        currencyDBAdapter.close();
        return pl;
    }
    public  List<CurrencyOperation> currencyOperationPaymentList(List<Order> sales) {
        List<CurrencyOperation> pl = new ArrayList<CurrencyOperation>();
        CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(getApplicationContext());
        currencyOperationDBAdapter.open();
        for (Order s : sales) {
            List<CurrencyOperation> payments = currencyOperationDBAdapter.getCurrencyOperationByOrderID(s.getOrderId());
            pl.addAll(payments);
        }
        currencyOperationDBAdapter.close();
        return pl;
    }
}
