package com.pos.leaders.leaderspossystem;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosInvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ClosingReport;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.OpiningReportDetails;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.PosInvoice;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.PrinterType;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import HPRTAndroidSDK.HPRTPrinterHelper;

import static com.pos.leaders.leaderspossystem.Tools.SendLog.sendLogFile;

public class ClosingReportActivity extends AppCompatActivity {
    EditText  checkActualValue ,creditActualValue ,shekelActualValue ,usdActualValue , eurActualValue , gbpActualValue ;
    TextView  checkExpectedValue ,creditExpectedValue ,shekelExpectedValue ,usdExpectedValue , eurAExpectedValue , gbpExpectedValue ;
    TextView   checkDifferentValue ,creditDifferentValue ,shekelDifferentValue ,usdDifferentValue , eurDifferentValue , gbpDifferentValue ;
    Button calculate , print ;
    double  expectedOpining=0, expectedCheck=0 ,expectedCredit=0 , expectedShekel=0 , expectedUsd=0 , expectedEur=0 , expectedGbp=0 , expectedTotal=0,expectedOpiningShekel=0,expectedOpiningUsd=0,expectedOpiningEur=0,expectedOpiningGbp=0;
    double actualOpining=0 , actualCheck=0 , actualCredit=0 , actualShekel=0 , actualUsd=0 , actualEur=0 , actualGbp=0 ,actualTotal=0,cashReceipt=0 , checkReceipt=0;
    OpiningReport opiningReport = null;
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
        try {
            opiningReport = opiningReportDBAdapter.getLastRow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkActualValue = (EditText) findViewById(R.id.actualCheckValue);
        creditActualValue = (EditText) findViewById(R.id.actualCreditValue);
        shekelActualValue = (EditText) findViewById(R.id.actualShekelValue);
        usdActualValue = (EditText) findViewById(R.id.actualUsdValue);
        eurActualValue = (EditText) findViewById(R.id.actualEurValue);
        gbpActualValue = (EditText) findViewById(R.id.actualGbpValue);
        checkExpectedValue =(TextView)findViewById(R.id.expectedCheckValue);
        creditExpectedValue =(TextView)findViewById(R.id.expectedCreditValue);
        shekelExpectedValue =(TextView)findViewById(R.id.expectedShekelValue);
        usdExpectedValue =(TextView)findViewById(R.id.expectedUsdValue);
        eurAExpectedValue =(TextView)findViewById(R.id.expectedEurValue);
        gbpExpectedValue =(TextView)findViewById(R.id.expectedGbpValue);
        checkDifferentValue =(TextView)findViewById(R.id.differentCheckValue);
        creditDifferentValue=(TextView)findViewById(R.id.differentCreditValue);
        shekelDifferentValue =(TextView)findViewById(R.id.differentShekelValue);
        usdDifferentValue =(TextView)findViewById(R.id.differentUsdValue);
        eurDifferentValue =(TextView)findViewById(R.id.differentEurValue);
        gbpDifferentValue =(TextView)findViewById(R.id.differentGbpValue);
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
                                                    actualTotal=actualShekel+actualUsd+actualEur+actualGbp;
                                              long i =  closingReportDBAdapter.insertEntry(actualTotal,expectedTotal,expectedTotal,new Timestamp(System.currentTimeMillis()),opiningReport.getOpiningReportId(),order.getOrderId(), SESSION._EMPLOYEE.getEmployeeId());
                                               if(i>0){
                                                   if(actualUsd==0&&expectedUsd==0){

                                                   }else {
                                                       closingReportDetailsDBAdapter.insertEntry(i,actualUsd,expectedUsd,actualUsd-expectedUsd,CONSTANT.CASH,"USD");

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
                                                   closingReportDetailsDBAdapter.insertEntry(i,actualOpining,expectedOpining,actualOpining-expectedOpining,getString(R.string.opening_report),"Shekel");
                                                   //print report
                                                   JSONObject res = new JSONObject();
                                                   res.put("actualCheck",actualCheck);
                                                   res.put("actualCredit",actualCredit);
                                                   res.put("actualShekel",actualShekel);
                                                   res.put("actualUsd",actualUsd);
                                                   res.put("actualEur",actualEur);
                                                   res.put("actualGbp",actualGbp);
                                                   res.put("expectedCheck",expectedCheck);
                                                   res.put("expectedCredit",expectedCredit);
                                                   res.put("expectedShekel",expectedShekel);
                                                   res.put("expectedUsd",expectedUsd);
                                                   res.put("expectedEur",expectedEur);
                                                   res.put("expectedGbp",expectedGbp);
                                                  int result = Util.sendClosingReport(ClosingReportActivity.this,res.toString());

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

    private void calculateActualAmount(){
        long aReportId = 0;
        double usd_plus = 0, usd_minus = 0;
        double eur_plus = 0, eur_minus = 0;
        double gbp_plus = 0, gbp_minus = 0;
        double sheqle_plus = 0, sheqle_minus = 0;
        double aReportAmount = 0;
        try {
            OrderDBAdapter orderDb = new OrderDBAdapter(getApplicationContext());
            orderDb.open();
            ClosingReportDBAdapter closingReportDBAdapter = new ClosingReportDBAdapter(getApplicationContext());
            closingReportDBAdapter.open();

            ClosingReport closingReport = null;
            closingReport = closingReportDBAdapter.getLastRow();
            ZReportDBAdapter zReportDBAdapter1 = new ZReportDBAdapter(ClosingReportActivity.this);
            zReportDBAdapter1.open();
            Order order =orderDb.getLast();
            List<Order> orders;
            if(closingReport==null&&zReportDBAdapter1.getProfilesCount()>0){
                ZReport zReport1 =zReportDBAdapter1.getLastRow();

                orders = orderDb.getBetween(zReport1.getEndOrderId(), order.getOrderId());

            }else if(closingReport==null&&zReportDBAdapter1.getProfilesCount()==0){
                orders = orderDb.getBetween(0, order.getOrderId());
            }
            else {
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
            //calculate receipt Amount
            ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(ClosingReportActivity.this);
            zReportDBAdapter.open();
            PosInvoiceDBAdapter posInvoiceDBAdapter = new PosInvoiceDBAdapter(ClosingReportActivity.this);
            posInvoiceDBAdapter.open();
            ZReport zReport =null;
            try {
                if(zReportDBAdapter.getProfilesCount()==0){
                    List<PosInvoice>tempPosCashInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(-1,DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
                    List<PosInvoice>newPosInvoiceListCash =new ArrayList<>();
                    for(int i=0;i<tempPosCashInvoiceList.size();i++){
                        if(tempPosCashInvoiceList.get(i).getLastZReportId()==opiningReport.getLastZReportID()){
                            newPosInvoiceListCash.add(tempPosCashInvoiceList.get(i));
                        }
                    }

                    for (int i=0;i<newPosInvoiceListCash.size();i++) {
                        cashReceipt+=newPosInvoiceListCash.get(i).getAmount();

                    }

                    //checkReceipt
                    List<PosInvoice>tempPosCheckInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(-1,DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
                    List<PosInvoice>newPosInvoiceListCheck =new ArrayList<>();
                    for(int i=0;i<tempPosCheckInvoiceList.size();i++){
                        if(tempPosCheckInvoiceList.get(i).getLastZReportId()==opiningReport.getLastZReportID()){
                            newPosInvoiceListCheck.add(tempPosCheckInvoiceList.get(i));
                        }
                    }

                    for (int i=0;i<newPosInvoiceListCheck.size();i++) {
                        checkReceipt+=newPosInvoiceListCheck.get(i).getAmount();

                    }
                }else {
                    zReport=zReportDBAdapter.getLastRow();
                    List<PosInvoice>tempPosCashInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport.getzReportId(),DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
                    List<PosInvoice>newPosInvoiceListCash =new ArrayList<>();
                    for(int i=0;i<tempPosCashInvoiceList.size();i++){
                        if(tempPosCashInvoiceList.get(i).getLastZReportId()==opiningReport.getLastZReportID()){
                            newPosInvoiceListCash.add(tempPosCashInvoiceList.get(i));
                        }
                    }

                    for (int i=0;i<newPosInvoiceListCash.size();i++) {
                        cashReceipt+=newPosInvoiceListCash.get(i).getAmount();

                    }

                    List<PosInvoice>tempPosCheckInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport.getzReportId(),DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
                    List<PosInvoice>newPosInvoiceListCheck =new ArrayList<>();
                    for(int i=0;i<tempPosCheckInvoiceList.size();i++){
                        if(tempPosCheckInvoiceList.get(i).getLastZReportId()==opiningReport.getLastZReportID()){
                            newPosInvoiceListCheck.add(tempPosCheckInvoiceList.get(i));
                        }
                    }

                    for (int i=0;i<newPosInvoiceListCheck.size();i++) {
                        checkReceipt+=newPosInvoiceListCheck.get(i).getAmount();

                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

            expectedOpining=Double.parseDouble(Util.makePrice(opiningReport.getAmount()));
            OpiningReportDetailsDBAdapter opiningReportDetailsDBAdapter = new OpiningReportDetailsDBAdapter(ClosingReportActivity.this);
            opiningReportDetailsDBAdapter.open();
            List<OpiningReportDetails>opiningReportDetailsList=opiningReportDetailsDBAdapter.getListOpiningReport(opiningReport.getOpiningReportId());
            if(opiningReportDetailsList.size()>0) {
                for (int i = 0; i < opiningReportDetailsList.size(); i++) {
                    if (opiningReportDetailsList.get(i).getType() == 0) {
                        expectedOpiningShekel+=opiningReportDetailsList.get(i).getAmount();
                    } else if (opiningReportDetailsList.get(i).getType() == 1) {
                        expectedOpiningUsd+=opiningReportDetailsList.get(i).getAmount();

                    } else if (opiningReportDetailsList.get(i).getType() == 2) {
                        expectedOpiningGbp+=opiningReportDetailsList.get(i).getAmount();

                    } else if (opiningReportDetailsList.get(i).getType() == 3) {
                        expectedOpiningEur+=opiningReportDetailsList.get(i).getAmount();

                    }
                }
            }
            expectedTotal+=cashReceipt;
            expectedCheck=(check_plus-check_minus)+checkReceipt;
            expectedCredit=creditCard_plus-creditCard_minus;
            expectedShekel=sheqle_plus-sheqle_minus+expectedOpiningShekel+cashReceipt;
            expectedUsd=usd_plus-usd_minus+expectedOpiningUsd;
            expectedEur=eur_plus-eur_minus+expectedOpiningEur;
            expectedGbp=gbp_plus-gbp_minus+expectedOpiningGbp;
            checkExpectedValue.setText(Util.makePrice(expectedCheck));
            creditExpectedValue.setText(Util.makePrice(expectedCredit));
            if(SETTINGS.enableCurrencies){
                shekelExpectedValue.setText(Util.makePrice(expectedShekel));
            }else {
                expectedShekel=cash_plus+opiningReport.getAmount()-sheqle_plus;
                shekelExpectedValue.setText(Util.makePrice(expectedShekel));

            }
            usdExpectedValue.setText(Util.makePrice(expectedUsd));
            eurAExpectedValue.setText(Util.makePrice(expectedEur));
            gbpExpectedValue.setText(Util.makePrice(expectedGbp));
        } catch (Exception e) {
            Log.d("exceeeption",e.toString());
            e.printStackTrace();
            sendLogFile();
        }


    }  private void calculateMethodAmount() throws Exception {
      if(!checkActualValue.getText().toString().equals("")){
          actualCheck= Double.parseDouble(checkActualValue.getText().toString());
      }
      if(!creditActualValue.getText().toString().equals("")){
          actualCredit= Double.parseDouble(creditActualValue.getText().toString());
      }
      if(!shekelActualValue.getText().toString().equals("")){
          actualShekel= Double.parseDouble(shekelActualValue.getText().toString());
      }
      if(!usdActualValue.getText().toString().equals("")){
          actualUsd= Double.parseDouble(usdActualValue.getText().toString());
      }
      if(!eurActualValue.getText().toString().equals("")){
          actualEur= Double.parseDouble(eurActualValue.getText().toString());
      }
      if(!gbpActualValue.getText().toString().equals("")){
          actualGbp= Double.parseDouble(gbpActualValue.getText().toString());
      }
      checkDifferentValue.setText(Util.makePrice(actualCheck-expectedCheck));
      creditDifferentValue.setText(Util.makePrice(actualCredit-expectedCredit));
      shekelDifferentValue.setText(Util.makePrice(actualShekel-expectedShekel));
      usdDifferentValue.setText(Util.makePrice(actualUsd-expectedUsd));
      eurDifferentValue.setText(Util.makePrice(actualEur-expectedEur));
      gbpDifferentValue.setText(Util.makePrice(actualGbp-expectedGbp));
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
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {


            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(SETTINGS.printer.equals(PrinterType.HPRT_TP805)){
                try {
                    HPRTPrinterHelper.OpenCashdrawer(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        HPRTPrinterHelper.OpenCashdrawer(1);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        try {
                            HPRTPrinterHelper.OpenCashdrawer(2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }}else if(SETTINGS.printer.equals(PrinterType.SUNMI_T1)){
                    try {
                        AidlUtil.getInstance().openCashBox();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            @Override
            protected Void doInBackground(Void... params) {
                byte b = 0;
                return null;
            }
        }.execute();
        return pl;
    }
}
