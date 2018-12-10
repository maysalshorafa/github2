package com.pos.leaders.leaderspossystem;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClosingReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClosingReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ClosingReport;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ClosingReportActivity extends AppCompatActivity {
    EditText cashActualValue , checkActualValue ,creditActualValue ,shekelActualValue ,usdActualValue , eurActualValue , gbpActualValue , totalActualValue;
    TextView cashExpectedValue , checkExpectedValue ,creditExpectedValue ,shekelExpectedValue ,usdExpectedValue , eurAExpectedValue , gbpExpectedValue , totalExpectedValue;
    TextView cashDifferentValue , checkDifferentValue ,creditDifferentValue ,shekelDifferentValue ,usdDifferentValue , eurDifferentValue , gbpDifferentValue , totalDifferentValue;
    Button calculate , print ;
    double expectedCash=0 , expectedCheck=0 ,expectedCredit=0 , expectedShekel=0 , expectedUsd=0 , expectedEur=0 , expectedGbp=0 , expectedTotal=0;
    double actualCash=0 , actualCheck=0 , actualCredit=0 , actualShekel=0 , actualUsd=0 , actualEur=0 , actualGbp=0 ,actualTotal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_closing_report);

        TitleBar.setTitleBar(this);
        final ClosingReportDBAdapter closingReportDBAdapter = new ClosingReportDBAdapter(getApplicationContext());
        closingReportDBAdapter.open();
        final ClosingReportDetailsDBAdapter closingReportDetailsDBAdapter = new ClosingReportDetailsDBAdapter(getApplicationContext());
        closingReportDetailsDBAdapter.open();
        final OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(getApplicationContext());
        opiningReportDBAdapter.open();
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
        try {
            calculateActualAmount();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(ClosingReportActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                        .setTitle(getString(R.string.printer))
                        .setMessage(getString(R.string.are_you_want_to_print_and_save_closing_report))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                                try {
                                                    OrderDBAdapter orderDBAdapter = new OrderDBAdapter(getApplicationContext());
                                                    orderDBAdapter.open();
                                                    Order order = orderDBAdapter.getLast();
                                                    OpiningReport opiningReport = opiningReportDBAdapter.getLastRow();
                                              long i =  closingReportDBAdapter.insertEntry(actualTotal,expectedTotal,expectedTotal,new Timestamp(System.currentTimeMillis()),opiningReport.getOpiningReportId(),order.getOrderId());
                                               if(i>0){
                                                        closingReportDetailsDBAdapter.insertEntry(i,actualCash,expectedCash,actualCash-expectedCash,CONSTANT.CASH,"Shekel");
                                                   if(actualUsd==0&&expectedUsd==0){

                                                   }else {
                                                       closingReportDetailsDBAdapter.insertEntry(i,actualUsd,expectedUsd,actualTotal-expectedUsd,CONSTANT.CASH,"USD");

                                                   }
                                                   if(actualEur==0&&expectedEur==0){

                                                   }else {
                                                       closingReportDetailsDBAdapter.insertEntry(i,actualEur,expectedEur,actualEur-expectedEur,CONSTANT.CASH,"EUR");

                                                   }
                                                   if(actualGbp==0&&expectedGbp==0){

                                                   }else {
                                                       closingReportDetailsDBAdapter.insertEntry(i,actualGbp,expectedGbp,actualGbp-expectedGbp,CONSTANT.CASH,"GBP");

                                                   }
                                                   closingReportDetailsDBAdapter.insertEntry(i,actualCheck,expectedCheck,actualCheck-expectedCheck,CONSTANT.CHECKS,"Shekel");
                                                   closingReportDetailsDBAdapter.insertEntry(i,actualCredit,expectedCredit,actualCredit-expectedCredit,CONSTANT.CREDIT_CARD,"Shekel");
                                                    //print report
                                                   JSONObject res = new JSONObject();
                                                   res.put("actualCash",actualCash);
                                                   res.put("actualCheck",actualCheck);
                                                   res.put("actualCredit",actualCredit);
                                                   res.put("actualShekel",actualShekel);
                                                   res.put("actualUsd",actualUsd);
                                                   res.put("actualEur",actualEur);
                                                   res.put("actualGbp",actualGbp);
                                                   res.put("actualTotal",actualTotal);
                                                   res.put("expectedCash",expectedCash);
                                                   res.put("expectedCheck",expectedCheck);
                                                   res.put("expectedCredit",expectedCredit);
                                                   res.put("expectedShekel",expectedShekel);
                                                   res.put("expectedUsd",expectedUsd);
                                                   res.put("expectedEur",expectedEur);
                                                   res.put("expectedGbp",expectedGbp);
                                                   res.put("expectedTotal",expectedTotal);
                                                  Util.sendClosingReport(ClosingReportActivity.this,res.toString());
                                               }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
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
        });





    }

    private void calculateActualAmount() throws Exception {
        long aReportId = 0;
        double usd_plus = 0, usd_minus = 0;
        double eur_plus = 0, eur_minus = 0;
        double gbp_plus = 0, gbp_minus = 0;
        double sheqle_plus = 0, sheqle_minus = 0;
        double aReportAmount = 0;

        OrderDBAdapter orderDb = new OrderDBAdapter(getApplicationContext());
        orderDb.open();
        ClosingReportDBAdapter closingReportDBAdapter = new ClosingReportDBAdapter(getApplicationContext());
        closingReportDBAdapter.open();
        ClosingReport closingReport = closingReportDBAdapter.getLastRow();
        Order order =orderDb.getLast();
        Order order1 =orderDb.getFirst();
        List<Order> orders;
        if(closingReport==null){
           orders = orderDb.getBetween(order1.getOrderId(), order.getOrderId());

        }else {
         orders = orderDb.getBetweenTwoSalesForClosingReport(closingReport.getLastOrderId(), order.getOrderId());
        }
        orderDb.close();


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
            expectedTotal+=p.getAmount();
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
        expectedCash=cash_plus-cash_minus;
        expectedCheck=check_plus-check_minus;
        expectedCredit=creditCard_plus-creditCard_minus;
        expectedShekel=sheqle_plus-sheqle_minus;
        expectedUsd=usd_plus-usd_minus;
        expectedEur=eur_plus-eur_minus;
        expectedGbp=gbp_plus-gbp_minus;
        cashExpectedValue.setText(Util.makePrice(expectedCash));
        checkExpectedValue.setText(Util.makePrice(expectedCheck));
        creditExpectedValue.setText(Util.makePrice(expectedCredit));
        shekelExpectedValue.setText(Util.makePrice(expectedShekel));
        usdExpectedValue.setText(Util.makePrice(expectedUsd));
        eurAExpectedValue.setText(Util.makePrice(expectedEur));
        gbpExpectedValue.setText(Util.makePrice(expectedGbp));
        totalExpectedValue.setText(Util.makePrice(expectedTotal));

    }
  private void calculateMethodAmount() throws Exception {

      if(!cashActualValue.getText().toString().equals("")){
         actualCash= Double.parseDouble(cashActualValue.getText().toString());
      }
      if(!checkActualValue.getText().toString().equals("")){
          actualCheck= Double.parseDouble(checkActualValue.getText().toString());
      }
      if(!creditActualValue.getText().toString().equals("")){
          actualCredit= Double.parseDouble(creditActualValue.getText().toString());
      }
      if(!shekelActualValue.getText().toString().equals("")){
          actualCash= Double.parseDouble(shekelActualValue.getText().toString());
      }
      if(!usdActualValue.getText().toString().equals("")){
          actualUsd= Double.parseDouble(usdActualValue.getText().toString());
      }
      if(!eurActualValue.getText().toString().equals("")){
          actualEur= Double.parseDouble(eurActualValue.getText().toString());
      }
      if(!totalActualValue.getText().toString().equals("")){
          actualTotal= Double.parseDouble(totalActualValue.getText().toString());
      }
      cashDifferentValue.setText(Util.makePrice(actualCash-expectedCash));
      checkDifferentValue.setText(Util.makePrice(actualCheck-expectedCheck));
      creditDifferentValue.setText(Util.makePrice(actualCredit-expectedCredit));
      shekelDifferentValue.setText(Util.makePrice(actualShekel-expectedShekel));
      usdDifferentValue.setText(Util.makePrice(actualUsd-expectedUsd));
      eurDifferentValue.setText(Util.makePrice(actualEur-expectedEur));
      gbpDifferentValue.setText(Util.makePrice(actualGbp-expectedGbp));
      totalDifferentValue.setText(Util.makePrice(actualTotal-expectedTotal));

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
