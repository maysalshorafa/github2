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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ClosingReport;
import com.pos.leaders.leaderspossystem.Models.ClosingReportDetails;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Payment.PaymentMethod;
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
    EditText  checkActualValue ,creditActualValue ,actualFirstTypeValue ,actualSecondTypeValue , actualTirdTypeValue , actualFourthTypeValue,actualPayPointValue ;
    TextView  checkExpectedValue ,creditExpectedValue ,expectedFirstTypeValue ,expectedSecondTypeValue , expectedThirdTypeValue , expectedFourthTypeValue , expactedPayPointValue;
    TextView   checkDifferentValue ,creditDifferentValue ,differentFirstTypeValue ,differentSecondTypeValue , differentThirdTypeValue , differentFourthTypeValue
            ,firstType,secondType,thirdType,fourthType , diffrentPayPointValue;
    Button calculate , print ;
    double  expectedOpining=0, expectedCheck=0 ,expectedCredit=0 , expectedFirstType=0 , expectedSecondType=0 , expectedTirdType=0 , expectedFourthType=0 , expectedTotal=0,expectedOpiningShekel=0,expectedOpiningUsd=0,expectedOpiningEur=0,expectedOpiningGbp=0,expectedPayPointValue;
    double actualOpining=0 , actualCheck=0 , actualCredit=0 , actualFirstType=0 , actualSecondType=0 , actualThirdType=0 , actualFourthType=0 ,actualTotal=0,cashReceipt=0 , checkReceipt=0 , actualPayPoint=0;
    OpiningReport opiningReport = null;
    private List<CurrencyType> currencyTypesList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_closing_report);

        TitleBar.setTitleBar(this);

        //Getting default currencies name and values
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();


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
        actualPayPointValue=(EditText)findViewById(R.id.actualPayPointValue);
        checkActualValue = (EditText) findViewById(R.id.actualCheckValue);
        creditActualValue = (EditText) findViewById(R.id.actualCreditValue);
        actualFirstTypeValue = (EditText) findViewById(R.id.actualFirstTypeValue);
        actualSecondTypeValue = (EditText) findViewById(R.id.actualSecondTypeValue);
        actualTirdTypeValue = (EditText) findViewById(R.id.actualTirdTypeValue);
        actualFourthTypeValue = (EditText) findViewById(R.id.actualFourthTypeValue);
        expactedPayPointValue=(TextView)findViewById(R.id.expectedPayPointValue);
        checkExpectedValue =(TextView)findViewById(R.id.expectedCheckValue);
        creditExpectedValue =(TextView)findViewById(R.id.expectedCreditValue);
        expectedFirstTypeValue =(TextView)findViewById(R.id.expectedFirstTypeValue);
        expectedSecondTypeValue =(TextView)findViewById(R.id.expectedSecondTypeValue);
        expectedThirdTypeValue =(TextView)findViewById(R.id.expectedThirdTypeValue);
        expectedFourthTypeValue =(TextView)findViewById(R.id.expectedFourthTypeValue);
        checkDifferentValue =(TextView)findViewById(R.id.differentCheckValue);
        creditDifferentValue=(TextView)findViewById(R.id.differentCreditValue);
        differentFirstTypeValue =(TextView)findViewById(R.id.differentFirstTypeValue);
        differentSecondTypeValue =(TextView)findViewById(R.id.differentSecondTypeValue);
        differentThirdTypeValue =(TextView)findViewById(R.id.differentThirdTypeValue);
        differentFourthTypeValue =(TextView)findViewById(R.id.differentFourthTypeValue);
        diffrentPayPointValue=(TextView)findViewById(R.id.differentPayPoint);


        firstType=(TextView) findViewById(R.id.firstType);
        secondType=(TextView) findViewById(R.id.secondType);
        thirdType=(TextView) findViewById(R.id.thirdType);
        fourthType=(TextView) findViewById(R.id.fourthType);

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
                                                    actualTotal=actualFirstType+actualSecondType+actualThirdType+actualFourthType;
                                              long i =  closingReportDBAdapter.insertEntry(actualTotal,expectedTotal,expectedTotal,new Timestamp(System.currentTimeMillis()),opiningReport.getOpiningReportId(),order.getOrderId(), SESSION._EMPLOYEE.getEmployeeId());
                                               if(i>0){
                                                   if(actualSecondType==0&&expectedSecondType==0){

                                                   }else {
                                                       closingReportDetailsDBAdapter.insertEntry(i,actualSecondType,expectedSecondType,actualSecondType-expectedSecondType,CONSTANT.CASH,currencyTypesList.get(1).getType());

                                                   }
                                                   if(actualThirdType==0&&expectedSecondType==0){

                                                   }else {
                                                       closingReportDetailsDBAdapter.insertEntry(i,actualThirdType,expectedTirdType,actualThirdType-expectedTirdType,CONSTANT.CASH,currencyTypesList.get(2).getType());

                                                   }
                                                   if(actualFourthType==0&&expectedFourthType==0){

                                                   }else {
                                                       closingReportDetailsDBAdapter.insertEntry(i,actualFourthType,expectedFourthType,actualFourthType-expectedFourthType,CONSTANT.CASH,currencyTypesList.get(3).getType());

                                                   }
                                                   closingReportDetailsDBAdapter.insertEntry(i,actualCheck,expectedCheck,actualCheck-expectedCheck,CONSTANT.CHECKS,currencyTypesList.get(0).getType());
                                                   closingReportDetailsDBAdapter.insertEntry(i,actualCredit,expectedCredit,actualCredit-expectedCredit,CONSTANT.CREDIT_CARD,currencyTypesList.get(0).getType());
                                                   closingReportDetailsDBAdapter.insertEntry(i,actualFirstType,expectedFirstType,actualFirstType-expectedFirstType,CONSTANT.CASH,currencyTypesList.get(0).getType());
                                                   closingReportDetailsDBAdapter.insertEntry(i,actualPayPoint,expectedPayPointValue,actualPayPoint-expectedPayPointValue, PaymentMethod.PAY_POINT,currencyTypesList.get(0).getType());

                                                   //print report
                                                   JSONObject res = new JSONObject();
                                                   res.put("actualCheck",actualCheck);
                                                   res.put("actualCredit",actualCredit);
                                                   res.put("actualFirstType",actualFirstType);
                                                   res.put("actualSecondType",actualSecondType);
                                                   res.put("actualThirdType",actualThirdType);
                                                   res.put("actualFourthType",actualFourthType);
                                                   res.put("expectedCheck",expectedCheck);
                                                   res.put("expectedCredit",expectedCredit);
                                                   res.put("expectedFirstType",expectedFirstType);
                                                   res.put("expectedSecondType",expectedSecondType);
                                                   res.put("expectedTirdType",expectedTirdType);
                                                   res.put("actualPayPoint",actualPayPoint);
                                                   res.put("expectedPayPointValue",expectedPayPointValue);


                                                   res.put("expectedFourthType",expectedFourthType);
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
        double pullReportAmount=0;
        double depositReportAmount=0;
        List<ClosingReport>closingReportList = new ArrayList<>();
        List<OpiningReport>opiningReportList =new ArrayList<>();
        List<ClosingReportDetails>closingReportDetailsList = new ArrayList<>();
        ClosingReportDBAdapter closingReportDBAdapter = new ClosingReportDBAdapter(ClosingReportActivity.this);
        closingReportDBAdapter.open();
        OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(ClosingReportActivity.this);
        opiningReportDBAdapter.open();
        ClosingReportDetailsDBAdapter closingReportDetailsDBAdapter = new ClosingReportDetailsDBAdapter(ClosingReportActivity.this);
        closingReportDetailsDBAdapter.open();
        ZReport lastZReport=null;


        List<CurrencyType> currencyTypesList = null;
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(ClosingReportActivity.this);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();


        try {
            double totalZReportAmount=0;
            ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(ClosingReportActivity.this);
            zReportDBAdapter.open();
            lastZReport = Util.getLastZReport(ClosingReportActivity.this);
            Log.d("kooooo","jojo");
            Log.d("lastZReportClosingReport",lastZReport.toString());
            opiningReportList=opiningReportDBAdapter.getListByLastZReport(lastZReport.getzReportId());
            Log.d("opiningReportListClosing",opiningReportList.toString());
            if(closingReportDBAdapter.getLastRow()==null){
                Log.d("closingRe","nulll");

            }else {
             Log.d("clod",closingReportDBAdapter.getLastRow().toString());
                Log.d("closingReportListID",opiningReportList.get(0).getOpiningReportId()+"");
                closingReportList = closingReportDBAdapter.getClosingReportByOpiningID(opiningReportList.get(0).getOpiningReportId());
                Log.d("testClosingReportList",closingReportList.toString());
                for(int i =0 ;i<closingReportList.size();i++){
                    closingReportDetailsList=closingReportDetailsDBAdapter.getClosingReportByClosingID(closingReportList.get(i).getClosingReportId());
                    Log.d("testClosingReportDetailsList",closingReportDetailsList.toString());

                    for(int g=0 ; g<closingReportDetailsList.size();g++){
                        ClosingReportDetails closingReportDetails = closingReportDetailsList.get(g);
                        if(closingReportDetails.getType().equalsIgnoreCase(CONSTANT.CHECKS)) {
                            lastZReport.setCheckTotal(lastZReport.getCheckTotal() - closingReportDetails.getExpectedValue());
                        }
                        if(closingReportDetails.getType().equalsIgnoreCase(CONSTANT.CREDIT_CARD)) {
                            lastZReport.setCreditTotal(lastZReport.getCreditTotal() - closingReportDetails.getExpectedValue());
                        }
                        if(closingReportDetails.getType().equalsIgnoreCase(CONSTANT.CASH)&& closingReportDetails.getCurrencyType().equalsIgnoreCase(currencyTypesList.get(0).getType())) {
                           Log.d("jjjh","jjjjj");
                            Log.d("gghgh",lastZReport.getFirstTypeAmount()+"");
                            Log.d("gkkkkghgh",closingReportDetails.getExpectedValue()+"");
                            lastZReport.setFirstTypeAmount(lastZReport.getFirstTypeAmount() - closingReportDetails.getExpectedValue());
                        }
                        if (SETTINGS.enableCurrencies){
                        if(closingReportDetails.getType().equalsIgnoreCase(CONSTANT.CASH)&& closingReportDetails.getCurrencyType().equalsIgnoreCase(currencyTypesList.get(1).getType())) {
                            lastZReport.setSecondTypeAmount(lastZReport.getSecondTypeAmount() - closingReportDetails.getExpectedValue());
                        }
                        if(closingReportDetails.getType().equalsIgnoreCase(CONSTANT.CASH)&& closingReportDetails.getCurrencyType().equalsIgnoreCase(currencyTypesList.get(2).getType())) {

                            lastZReport.setThirdTypeAmount(lastZReport.getThirdTypeAmount() - closingReportDetails.getExpectedValue());
                        }
                        if(closingReportDetails.getType().equalsIgnoreCase(CONSTANT.CASH)&& closingReportDetails.getCurrencyType().equalsIgnoreCase(currencyTypesList.get(3).getType())) {
                            lastZReport.setFourthTypeAmount(lastZReport.getFourthTypeAmount() - closingReportDetails.getExpectedValue());
                        }

                    }}
                }
            }


           /* OrderDBAdapter orderDb = new OrderDBAdapter(getApplicationContext());
            orderDb.open();
            ClosingReportDBAdapter closingReportDBAdapter = new ClosingReportDBAdapter(getApplicationContext());
            closingReportDBAdapter.open();
            CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(getApplicationContext());
            cashPaymentDBAdapter.open();
            ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(getApplicationContext());
            checksDBAdapter.open();
            CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(getApplicationContext());
            creditCardPaymentDBAdapter.open();
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
                long orderId = p.getOrderId();
                List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(orderId);
                if (p.getAmount()<0){
                    cash_minus+=p.getAmount();
                }
                for(int i=0;i<cashPaymentList.size();i++){
                    cash_plus+=cashPaymentList.get(i).getAmount();
                }
                List<Check>checkList=checksDBAdapter.getPaymentBySaleID(orderId);
                for(int i=0;i<checkList.size();i++){
                    check_plus+=checkList.get(i).getAmount();
                }
                List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
                for(int i=0;i<creditCardPayments.size();i++){
                    creditCard_plus+=creditCardPayments.get(i).getAmount();
                }
              /*  switch (p.getPaymentWay()) {
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
                expectedTotal+=p.getAmount();*/



//with Currency
          /*  if (SETTINGS.enableCurrencies) {
                for (CurrencyOperation cp : currencyOperationList) {
                    switch (cp.getCurrencyType()) {
                        case "ILS":
                                sheqle_plus += cp.getAmount();
                            break;
                        case "USD":
                                usd_plus += cp.getAmount();
                            break;
                        case "EUR":
                                eur_plus += cp.getAmount();
                            break;
                        case "GBP":
                                gbp_plus += cp.getAmount();
                            break;
                    }
                }
                for (CurrencyReturns cp : currencyReturnList) {
                    switch ((int) cp.getCurrency_type()) {
                        case CONSTANT.Shekel:
                                sheqle_minus += cp.getAmount();
                            break;
                        case CONSTANT.USD:
                                usd_minus += cp.getAmount();
                            break;
                        case CONSTANT.EUR:
                                eur_minus += cp.getAmount();
                            break;
                        case CONSTANT.GBP:
                                gbp_minus += cp.getAmount();
                            break;
                    }
                }
            }*/
            //calculate receipt Amount
         /*   ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(ClosingReportActivity.this);
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
                    DrawerDepositAndPullReportDbAdapter drawerDepositAndPullReportDbAdapter=new DrawerDepositAndPullReportDbAdapter(ClosingReportActivity.this);
                    drawerDepositAndPullReportDbAdapter.open();
                    List<DepositAndPullReport>depositAndPullReportList=drawerDepositAndPullReportDbAdapter.getListByLastZReport(-1);
                    for(int i=0;i<depositAndPullReportList.size();i++){
                        if(depositAndPullReportList.get(i).getType().equals("Pull")){
                            pullReportAmount+=depositAndPullReportList.get(i).getAmount();
                        }
                        else {
                            depositReportAmount+=depositAndPullReportList.get(i).getAmount();
                        }
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
                    DrawerDepositAndPullReportDbAdapter drawerDepositAndPullReportDbAdapter=new DrawerDepositAndPullReportDbAdapter(ClosingReportActivity.this);
                    drawerDepositAndPullReportDbAdapter.open();
                    List<DepositAndPullReport>depositAndPullReportList=drawerDepositAndPullReportDbAdapter.getListByLastZReport(zReport.getzReportId());
                    for(int i=0;i<depositAndPullReportList.size();i++){
                        if(depositAndPullReportList.get(i).getType().equals("Pull")){
                            pullReportAmount+=depositAndPullReportList.get(i).getAmount();
                        }
                        else {
                            depositReportAmount+=depositAndPullReportList.get(i).getAmount();
                        }
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
            }*/
            expectedTotal+=cashReceipt;
            expectedCheck=lastZReport.getCheckTotal();
            expectedCredit=lastZReport.getCreditTotal();
            expectedFirstType=lastZReport.getFirstTypeAmount();
            expectedSecondType=lastZReport.getSecondTypeAmount();
            expectedTirdType=lastZReport.getThirdTypeAmount();
            expectedFourthType=lastZReport.getFourthTypeAmount();
            checkExpectedValue.setText(Util.makePrice(expectedCheck));
            creditExpectedValue.setText(Util.makePrice(expectedCredit));
            expectedPayPointValue=lastZReport.getTotalPayPoint();
            expactedPayPointValue.setText(Util.makePrice(expectedPayPointValue));


            Log.d("zreportLast",lastZReport.toString());

            firstType.setText(currencyTypesList.get(0).getType());
            Log.d("shsus",currencyTypesList.get(0).getType());
            expectedFirstTypeValue.setText(Util.makePrice(expectedFirstType));



            if (SETTINGS.enableCurrencies){
                visibleIfGone(secondType,currencyTypesList.get(1).getType());
                visibleIfGone(thirdType,currencyTypesList.get(2).getType());
                visibleIfGone(fourthType,currencyTypesList.get(3).getType());
                visibleIfGone(expectedSecondTypeValue,Util.makePrice(expectedSecondType));
                visibleIfGone(expectedThirdTypeValue,Util.makePrice(expectedTirdType));
                visibleIfGone(expectedFourthTypeValue,Util.makePrice(expectedFourthType));

                /*
                firstType.setText(currencyTypesList.get(0).getType());
                secondType.setText(currencyTypesList.get(1).getType());
                thirdType.setText(currencyTypesList.get(2).getType());
                fourthType.setText(currencyTypesList.get(3).getType());
                expectedFirstTypeValue.setText(Util.makePrice(expectedFirstType));
            expectedSecondTypeValue.setText(Util.makePrice(expectedSecondType));
            expectedThirdTypeValue.setText(Util.makePrice(expectedTirdType));
            expectedFourthTypeValue.setText(Util.makePrice(expectedFourthType));*/
            }
            else {
                goneIfVisible(secondType);
                goneIfVisible(thirdType);
                goneIfVisible(fourthType);
                goneIfVisible(expectedSecondTypeValue);
                goneIfVisible(expectedThirdTypeValue);
                goneIfVisible(expectedFourthTypeValue);

                /*firstType.setVisibility(View.GONE);
                secondType.setVisibility(View.GONE);
                thirdType.setVisibility(View.GONE);
                fourthType.setVisibility(View.GONE);
                expectedSecondTypeValue.setVisibility(View.GONE);
                expectedThirdTypeValue.setVisibility(View.GONE);
                expectedFourthTypeValue.setVisibility(View.GONE);*/
            }
        } catch (Exception e) {
            Log.d("exceeeption",e.toString());
            e.printStackTrace();
            sendLogFile();
        }


    }
    public static void visibleIfGone (View v,String text)
    {
        if (v.getVisibility() == View.INVISIBLE){
            v.setVisibility(View.VISIBLE);
            ((TextView)v).setText(text);
        }
        else {
            ((TextView)v).setText(text);
        }
    }

    public static void goneIfVisible (View v)
    {
        if (v.getVisibility() == View.VISIBLE)
            v.setVisibility(View.INVISIBLE);
    }
    private void calculateMethodAmount() throws Exception {



      if(!checkActualValue.getText().toString().equals("")){
          actualCheck= Double.parseDouble(checkActualValue.getText().toString());
      }
        if(!actualPayPointValue.getText().toString().equals("")){
            actualPayPoint= Double.parseDouble(actualPayPointValue.getText().toString());
        }
      if(!creditActualValue.getText().toString().equals("")){
          actualCredit= Double.parseDouble(creditActualValue.getText().toString());
      }
      if(!actualFirstTypeValue.getText().toString().equals("")){
          actualFirstType= Double.parseDouble(actualFirstTypeValue.getText().toString());
      }
      if(!actualSecondTypeValue.getText().toString().equals("")){
            actualSecondType= Double.parseDouble(actualSecondTypeValue.getText().toString());
        }

        if(!actualTirdTypeValue.getText().toString().equals("")){
            actualThirdType= Double.parseDouble(actualTirdTypeValue.getText().toString());
        }
        if(!actualFourthTypeValue.getText().toString().equals("")){
            actualFourthType= Double.parseDouble(actualFourthTypeValue.getText().toString());
        }

      checkDifferentValue.setText(Util.makePrice(actualCheck-expectedCheck));
        diffrentPayPointValue.setText(Util.makePrice(actualPayPoint-expectedPayPointValue));
      creditDifferentValue.setText(Util.makePrice(actualCredit-expectedCredit));
        differentFirstTypeValue.setText(Util.makePrice(actualFirstType-expectedFirstType));


       if (SETTINGS.enableCurrencies){

           visibleIfGone(differentSecondTypeValue,Util.makePrice(actualSecondType-expectedSecondType));
           visibleIfGone(differentThirdTypeValue,Util.makePrice(actualThirdType-expectedTirdType));
           visibleIfGone(differentFourthTypeValue,Util.makePrice(actualFourthType-expectedFourthType));
          /* differentFirstTypeValue.setText(Util.makePrice(actualFirstType-expectedFirstType));
            differentSecondTypeValue.setText(Util.makePrice(actualSecondType-expectedSecondType));
            differentThirdTypeValue.setText(Util.makePrice(actualThirdType-expectedTirdType));
            differentFourthTypeValue.setText(Util.makePrice(actualFourthType-expectedFourthType));*/
        }
        else {
          // goneIfVisible(differentFirstTypeValue);
           goneIfVisible(differentSecondTypeValue);
           goneIfVisible(differentThirdTypeValue);
           goneIfVisible(differentFourthTypeValue);
          /* differentFirstTypeValue.setText(Util.makePrice(actualFirstType-expectedFirstType));
            differentSecondTypeValue.setVisibility(View.INVISIBLE);
            differentThirdTypeValue.setVisibility(View.INVISIBLE);
            differentFourthTypeValue.setVisibility(View.INVISIBLE);*/

        }

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
