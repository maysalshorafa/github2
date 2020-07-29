package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.XReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportCountDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.XReport;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Models.ZReportCount;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import POSSDK.POSSDK;

public class ReportZDetailsActivity extends Activity {
    Button btCancel,btPrint;
      double cashAmount=0;
      int invoiceReceiptCount=0 ,invoiceCount=0 , CreditInvoiceCount=0 , ShekelCount=0 ,UsdCount=0 , EurCount=0,GbpCount=0 ,checkCount=0 , creditCardCount=0 ,receiptInvoiceAmountCheck=0 , cashCount=0,receiptInvoiceAmount=0;

    TextView invoiceReceiptCountText,zReportTotalInvoiceReceipt,invoiceCountText,zReportInvoice,creditInvoiceCount,
            zReportCreditInvoice,zReportTotalSales,zReportCashPaymentCount,zReportTotalCashPayment,
            zReportFirstTypeCount,zReportTotalFirstType,zReportSecondTypeCount,zReportTotalSecondType,zReportThirdTypeCount,zReportTotalThirdType,
            zReportFourthTypeCount,zReportTotalFourthType,zReportCreditCardCount,zReportTotalCreditCard,zReportCheckCount,
            zReportTotalCheck,zReportTotalAmount,zReportOpiningReportAmount,zReportOpiningReportCount,zReportPullReportAmount
            ,zReportDepositReportAmount,zReportFirstTypeAmount,zReportSecondTypeAmount,zReportThirdTypeAmount,zReportFourthTypeAmount
            ,zReportPosSales,zReportSalesWithTax,zReportSalesBeforeTax,zReportTotalPrice,zReportTotalTax,minusGeneralItemCount
            ,zReportMinusGeneralItem,firstType,secondType,thirdType,fourthType,firstTypeCount,secondTypeCount,thirdTypeCount,fourthTypeCount ,totalPayPoint,totalPointCount;
    POSSDK pos;
    public static  Bitmap p;
    PrintTools pt;
    String str;
    long from=0,to=0,id=0;
    boolean goBack = false;
    double totalZReportAmount =0.0 ;
    double amount =0;
    boolean isCopy=false;
    boolean x= false;
    ZReport zReport ;
    XReport xReport;
    public static ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
    Bitmap newBitmap =null;
    boolean fromDashBoard=false;
    Serializable ObjectZREport;
    static List<List<Check>> checkList = new ArrayList<>();
    static List<OpiningReport> opiningReportList=new ArrayList<>();
    static double aReportDetailsForFirstCurrency=0;
    static double aReportDetailsForSecondCurrency=0;
    static double aReportDetailsForThirdCurrency=0;
    static double aReportDetailsForForthCurrency=0;
    static double aReportAmount = 0;
    Bitmap newBitmap2=null;
    LinearLayout CurrencyLayout,CountLinear,TotalLinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_report_zdetails);

        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(getApplicationContext());
        zReportDBAdapter.open();



        btCancel = (Button) findViewById(R.id.reportZDetails_btCancel);
        btPrint = (Button) findViewById(R.id.reportZDetails_btPrint);

        invoiceReceiptCountText=(TextView)findViewById(R.id.invoiceReceiptCount);
        zReportTotalInvoiceReceipt=(TextView)findViewById(R.id.zReportTotalInvoiceReceipt);
        invoiceCountText=(TextView)findViewById(R.id.invoiceCount);
        zReportInvoice=(TextView)findViewById(R.id.zReportInvoice);
        creditInvoiceCount=(TextView)findViewById(R.id.creditInvoiceCount);
        zReportCreditInvoice=(TextView)findViewById(R.id.zReportCreditInvoice);
        zReportTotalSales=(TextView)findViewById(R.id.zReportTotalSales);
        zReportCashPaymentCount=(TextView)findViewById(R.id.zReportCashPaymentCount);
        zReportTotalCashPayment=(TextView)findViewById(R.id.zReportTotalCashPayment);
        zReportFirstTypeCount=(TextView)findViewById(R.id.zReportFirstTypeCount);
        zReportTotalFirstType=(TextView)findViewById(R.id.zReportTotalFirstType);
        zReportSecondTypeCount=(TextView)findViewById(R.id.zReportSecondTypeCount);
        zReportTotalSecondType=(TextView)findViewById(R.id.zReportTotalSecondType);
        zReportThirdTypeCount=(TextView)findViewById(R.id.zReportThirdTypeCount);
        zReportTotalThirdType=(TextView)findViewById(R.id.zReportTotalThirdType);
        zReportFourthTypeCount=(TextView)findViewById(R.id.zReportFourthTypeCount);
        zReportTotalFourthType=(TextView)findViewById(R.id.zReportTotalFourthType);
        zReportCreditCardCount=(TextView)findViewById(R.id.zReportCreditCardCount);
        zReportTotalCreditCard=(TextView)findViewById(R.id.zReportTotalCreditCard);
        zReportCheckCount=(TextView)findViewById(R.id.zReportCheckCount);
        zReportTotalCheck=(TextView)findViewById(R.id.zReportTotalCheck);
        zReportTotalAmount=(TextView)findViewById(R.id.zReportTotalAmount);
        zReportOpiningReportAmount=(TextView)findViewById(R.id.zReportOpiningReportAmount);
        zReportOpiningReportCount=(TextView)findViewById(R.id.zReportOpiningReportCount);
        zReportPullReportAmount=(TextView)findViewById(R.id.zReportPullReportAmount);
        zReportDepositReportAmount=(TextView)findViewById(R.id.zReportDepositReportAmount);
        zReportFirstTypeAmount=(TextView)findViewById(R.id.zReportFirstTypeAmount);
        zReportSecondTypeAmount=(TextView)findViewById(R.id.zReportSecondTypeAmount);
        zReportThirdTypeAmount=(TextView)findViewById(R.id.zReportThirdTypeAmount);
        zReportFourthTypeAmount=(TextView)findViewById(R.id.zReportFourthTypeAmount);
        zReportPosSales=(TextView)findViewById(R.id.zReportPosSales);
        zReportSalesWithTax=(TextView)findViewById(R.id.zReportSalesWithTax);
        zReportSalesBeforeTax=(TextView)findViewById(R.id.zReportSalesBeforeTax);
        zReportTotalTax=(TextView)findViewById(R.id.Tax);
        zReportTotalPrice=(TextView)findViewById(R.id.TotalPrice);


        CountLinear=(LinearLayout)findViewById(R.id.CountLinear);
        CurrencyLayout=(LinearLayout) findViewById(R.id.CurrencyLayout);
        TotalLinear=(LinearLayout) findViewById(R.id.TotalLinear);
        firstType=(TextView) findViewById(R.id.firstType);
        secondType=(TextView) findViewById(R.id.secondType);
        thirdType=(TextView) findViewById(R.id.thirdType);
        fourthType=(TextView) findViewById(R.id.fourthType);



        firstTypeCount=(TextView) findViewById(R.id.firstTypeCount);
        secondTypeCount=(TextView) findViewById(R.id.secondTypeCount);
        thirdTypeCount=(TextView) findViewById(R.id.thirdTypeCount);
        fourthTypeCount=(TextView) findViewById(R.id.fourthTypeCount);
        totalPayPoint =(TextView)findViewById(R.id.zReportTotalPayPoint);
        totalPointCount=(TextView)findViewById(R.id.zReportPayPointCount);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            x= getIntent().getExtras().getBoolean(ReportsManagementActivity.COM_LEADPOS_XREPORT_FLAG);
            if (x==true) {
                Intent i = getIntent();
                xReport = (XReport) i.getSerializableExtra("ObjectXReport");
                Log.d("xReportObject",xReport.toString());
            } else {
               /* id = (long) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_ID);
                from = (long) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_FORM);
                to = (long) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_TO);
                totalZReportAmount = (double) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_TOTAL_AMOUNT);
                amount = (double) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_AMOUNT);
*/
                Intent i = getIntent();
                 zReport = (ZReport) i.getSerializableExtra("ObjectZReport");
             //   Log.d("zReportObject",zReport.toString());

            }
            if (extras.containsKey(ZReportActivity.COM_LEADPOS_ZREPORT_HISTORY)) {
                goBack = extras.getBoolean(ZReportActivity.COM_LEADPOS_ZREPORT_HISTORY, false);
                isCopy = true;

            }
            if (extras.containsKey(ZReportActivity.COM_LEADPOS_ZREPORT_FROM_DASH_BOARD)) {
                fromDashBoard= (boolean)extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_FROM_DASH_BOARD);
                Log.d("tttttttttt",fromDashBoard+"");

            }
        }
        XReportDBAdapter xReportDBAdapter = new XReportDBAdapter(ReportZDetailsActivity.this);
        xReportDBAdapter.open();
        pt = new PrintTools(ReportZDetailsActivity.this);
        if (x==true) {
            List<CurrencyType> currencyTypesList = null;
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(ReportZDetailsActivity.this);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currencyTypeDBAdapter.close();
            ZReportCountDbAdapter zReportCountDbAdapter = new ZReportCountDbAdapter(ReportZDetailsActivity.this);
            zReportCountDbAdapter.open();
            ZReportCount zReportCount=null;
            ZReport zReport=null;
            try {
                zReportCount = zReportCountDbAdapter.getLastRow();
                 zReport = zReportDBAdapter.getLastRow();

            } catch (Exception e) {
                e.printStackTrace();
            }
            getCountForZReport(ReportZDetailsActivity.this,zReport);

            try {
                firstTypeCount.setText(currencyTypesList.get(0).getType());
                firstType.setText(currencyTypesList.get(0).getType());
                if (SETTINGS.enableCurrencies) {
                   /* CurrencyLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,6f));
                    CountLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,6f));
                    TotalLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,6f));*/
                    visibleIfGone(secondTypeCount,currencyTypesList.get(1).getType());
                    visibleIfGone(thirdTypeCount,currencyTypesList.get(2).getType());
                    visibleIfGone(fourthTypeCount,currencyTypesList.get(3).getType());
                    visibleIfGone(zReportSecondTypeCount,zReportCount.getSecondTypeCount()+"");
                    visibleIfGone(zReportTotalSecondType,Util.makePrice(xReport.getSecondTypeAmount()));
                    visibleIfGone(zReportThirdTypeCount,zReportCount.getThirdTypeCount()+"");
                    visibleIfGone(zReportTotalThirdType,Util.makePrice(xReport.getThirdTypeAmount()));
                    visibleIfGone(zReportFourthTypeCount,zReportCount.getFourthTypeCount()+"");
                    visibleIfGone(zReportTotalFourthType,Util.makePrice(xReport.getFourthTypeAmount()));
                    visibleIfGone(secondType,currencyTypesList.get(1).getType());
                    visibleIfGone(thirdType,currencyTypesList.get(2).getType());
                    visibleIfGone(fourthType,currencyTypesList.get(3).getType());
                    visibleIfGone(zReportSecondTypeAmount,Util.makePrice(aReportDetailsForSecondCurrency));
                    visibleIfGone(zReportThirdTypeAmount,Util.makePrice(aReportDetailsForThirdCurrency));
                    visibleIfGone(zReportFourthTypeAmount,Util.makePrice(aReportDetailsForForthCurrency));

                   /* secondTypeCount.setText(currencyTypesList.get(1).getType());
                    thirdTypeCount.setText(currencyTypesList.get(2).getType());
                    fourthTypeCount.setText(currencyTypesList.get(3).getType());

                    zReportSecondTypeCount.setText(zReportCount.getSecondTypeCount()+"");
                    zReportTotalSecondType.setText(Util.makePrice(xReport.getSecondTypeAmount()));
                    zReportThirdTypeCount.setText(zReportCount.getThirdTypeCount()+"");
                    zReportTotalThirdType.setText(Util.makePrice(xReport.getThirdTypeAmount()));
                    zReportFourthTypeCount.setText(zReportCount.getFourthTypeCount()+"");
                    zReportTotalFourthType.setText(Util.makePrice(xReport.getFourthTypeAmount()));

                    secondType.setText(currencyTypesList.get(1).getType());
                    thirdType.setText(currencyTypesList.get(2).getType());
                    fourthType.setText(currencyTypesList.get(3).getType());


                    zReportSecondTypeAmount.setText(Util.makePrice(aReportDetailsForSecondCurrency));
                    zReportThirdTypeAmount.setText(Util.makePrice(aReportDetailsForThirdCurrency));;
                    zReportFourthTypeAmount.setText(Util.makePrice(aReportDetailsForForthCurrency));;*/
                }
                else {
                   /* CurrencyLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,3f));
                    CountLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,3f));
                    TotalLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,3f));*/

                    goneIfVisible(secondTypeCount);
                    goneIfVisible(thirdTypeCount);
                    goneIfVisible(fourthTypeCount);
                    goneIfVisible(zReportSecondTypeCount);
                    goneIfVisible(zReportTotalSecondType);
                    goneIfVisible(zReportThirdTypeCount);
                    goneIfVisible(zReportTotalThirdType);
                    goneIfVisible(zReportFourthTypeCount);
                    goneIfVisible(zReportTotalFourthType);
                    goneIfVisible(secondType);
                    goneIfVisible(thirdType);
                    goneIfVisible(fourthType);
                    goneIfVisible(zReportSecondTypeAmount);
                    goneIfVisible(zReportThirdTypeAmount);
                    goneIfVisible(zReportFourthTypeAmount);
                    /*secondTypeCount.setVisibility(View.GONE);
                    thirdTypeCount.setVisibility(View.GONE);
                    fourthTypeCount.setVisibility(View.GONE);

                    zReportSecondTypeCount.setVisibility(View.GONE);
                    zReportTotalSecondType.setVisibility(View.GONE);
                    zReportThirdTypeCount.setVisibility(View.GONE);
                    zReportTotalThirdType.setVisibility(View.GONE);
                    zReportFourthTypeCount.setVisibility(View.GONE);
                    zReportTotalFourthType.setVisibility(View.GONE);

                    secondType.setVisibility(View.GONE);
                    thirdType.setVisibility(View.GONE);
                    fourthType.setVisibility(View.GONE);


                    zReportSecondTypeAmount.setVisibility(View.GONE);
                    zReportThirdTypeAmount.setVisibility(View.GONE);
                    zReportFourthTypeAmount.setVisibility(View.GONE);*/
                }
                invoiceReceiptCountText.setText(zReportCount.getInvoiceReceiptCount()+"");
                zReportTotalInvoiceReceipt.setText(Util.makePrice(xReport.getInvoiceReceiptAmount()));
                totalPayPoint.setText(Util.makePrice(xReport.getPayPoint()));
                invoiceCountText.setText(zReportCount.getInvoiceCount()+"");
                zReportInvoice.setText(Util.makePrice(xReport.getInvoiceAmount()));
                creditInvoiceCount.setText(zReportCount.getCreditInvoiceCount()+"");
                zReportCreditInvoice.setText(Util.makePrice(xReport.getCreditInvoiceAmount()));
                zReportTotalSales.setText(Util.makePrice(xReport.getTotalSales()));
                zReportCashPaymentCount.setText(zReportCount.getCashCount()+"");
                zReportTotalCashPayment.setText(Util.makePrice(xReport.getCashTotal()));
                zReportFirstTypeCount.setText(zReportCount.getFirstTYpeCount()+"");
                zReportTotalFirstType.setText(Util.makePrice(xReport.getFirstTypeAmount()));

                totalPointCount.setText(zReportCount.getPayPointCount()+"");
                zReportCreditCardCount.setText(zReportCount.getCreditCount()+"");
                zReportTotalCreditCard.setText(Util.makePrice(xReport.getCreditTotal()));
                zReportCheckCount.setText(zReportCount.getCheckCount()+"");
                zReportTotalCheck.setText(Util.makePrice(xReport.getCheckTotal()));
                zReportTotalAmount.setText(Util.makePrice(xReport.getTotalAmount()));
                zReportOpiningReportAmount.setText(Util.makePrice(aReportAmount));
                zReportOpiningReportCount.setText(opiningReportList.size()+"");
                zReportPullReportAmount.setText(Util.makePrice(xReport.getPullReportAmount()));
                zReportDepositReportAmount.setText(Util.makePrice(xReport.getDepositReportAmount()));
                zReportFirstTypeAmount.setText(Util.makePrice(aReportDetailsForFirstCurrency));


                zReportPosSales.setText(Util.makePrice(xReport.getTotalPosSales()));
                zReportTotalTax.setText(xReport.getTotalTax()+"");
                zReportSalesBeforeTax.setText(xReport.getSalesBeforeTax()+"");
                zReportSalesWithTax.setText(xReport.getSalesWithTax()+"");
                zReportTotalPrice.setText(Util.makePrice(xReport.getSalesBeforeTax()+xReport.getSalesWithTax()+xReport.getTotalTax()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            PdfUA pdfUA = new PdfUA();

            try {
                pdfUA.createXReport(ReportZDetailsActivity.this,xReport,zReportCount);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try
            {
                File path = new File( Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName());
                File file = new File(path,"xreport.pdf");
                RandomAccessFile f = new RandomAccessFile(file, "r");
                byte[] data = new byte[(int)f.length()];
                f.readFully(data);
                pdfLoadImages(data);
                Log.d("bitmapsize",bitmapList.size()+"");
            }
            catch(Exception ignored)
            {

            }

        } else {
            List<CurrencyType> currencyTypesList = null;
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(ReportZDetailsActivity.this);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currencyTypeDBAdapter.close();
            ZReportCountDbAdapter zReportCountDbAdapter = new ZReportCountDbAdapter(ReportZDetailsActivity.this);
            zReportCountDbAdapter.open();
            final ZReportCount zReportCount = zReportCountDbAdapter.getByID(zReport.getzReportId());
            if(zReportCount!=null) {
                Log.d("testZreportCountfffff", zReportCount.toString());
                getCountForZReport(ReportZDetailsActivity.this, zReport);
            }
            try {

                firstTypeCount.setText(currencyTypesList.get(0).getType());
                firstType.setText(currencyTypesList.get(0).getType());
                if (SETTINGS.enableCurrencies) {
                   /* CurrencyLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,6f));
                    CountLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,6f));
                    TotalLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,6f));*/
                    visibleIfGone(secondTypeCount,currencyTypesList.get(1).getType());
                    visibleIfGone(thirdTypeCount,currencyTypesList.get(2).getType());
                    visibleIfGone(fourthTypeCount,currencyTypesList.get(3).getType());
                    visibleIfGone(zReportSecondTypeCount,zReportCount.getSecondTypeCount()+"");
                    visibleIfGone(zReportTotalSecondType,Util.makePrice(zReport.getSecondTypeAmount()));
                    visibleIfGone(zReportThirdTypeCount,zReportCount.getThirdTypeCount()+"");
                    visibleIfGone(zReportTotalThirdType,Util.makePrice(zReport.getThirdTypeAmount()));
                    visibleIfGone(zReportFourthTypeCount,zReportCount.getFourthTypeCount()+"");
                    visibleIfGone(zReportTotalFourthType,Util.makePrice(zReport.getFourthTypeAmount()));
                    visibleIfGone(secondType,currencyTypesList.get(1).getType());
                    visibleIfGone(thirdType,currencyTypesList.get(2).getType());
                    visibleIfGone(fourthType,currencyTypesList.get(3).getType());
                    visibleIfGone(zReportSecondTypeAmount,Util.makePrice(aReportDetailsForSecondCurrency));
                    visibleIfGone(zReportThirdTypeAmount,Util.makePrice(aReportDetailsForThirdCurrency));
                    visibleIfGone(zReportFourthTypeAmount,Util.makePrice(aReportDetailsForForthCurrency));




                    /*secondTypeCount.setText(currencyTypesList.get(1).getType());
                    thirdTypeCount.setText(currencyTypesList.get(2).getType());
                    fourthTypeCount.setText(currencyTypesList.get(3).getType());

                    zReportSecondTypeCount.setText(zReportCount.getSecondTypeCount()+"");
                    zReportTotalSecondType.setText(Util.makePrice(zReport.getSecondTypeAmount()));
                    zReportThirdTypeCount.setText(zReportCount.getThirdTypeCount()+"");
                    zReportTotalThirdType.setText(Util.makePrice(zReport.getThirdTypeAmount()));
                    zReportFourthTypeCount.setText(zReportCount.getFourthTypeCount()+"");
                    zReportTotalFourthType.setText(Util.makePrice(zReport.getFourthTypeAmount()));

                    secondType.setText(currencyTypesList.get(1).getType());
                    thirdType.setText(currencyTypesList.get(2).getType());
                    fourthType.setText(currencyTypesList.get(3).getType());


                    zReportSecondTypeAmount.setText(Util.makePrice(aReportDetailsForSecondCurrency));
                    zReportThirdTypeAmount.setText(Util.makePrice(aReportDetailsForThirdCurrency));;
                    zReportFourthTypeAmount.setText(Util.makePrice(aReportDetailsForForthCurrency));;*/
                }
                else {
                   // CurrencyLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,3f));
                   // CountLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,3f));
                  //  TotalLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,3f));
                    goneIfVisible(secondTypeCount);
                    goneIfVisible(thirdTypeCount);
                    goneIfVisible(fourthTypeCount);
                    goneIfVisible(zReportSecondTypeCount);
                    goneIfVisible(zReportTotalSecondType);
                    goneIfVisible(zReportThirdTypeCount);
                    goneIfVisible(zReportTotalThirdType);
                    goneIfVisible(zReportFourthTypeCount);
                    goneIfVisible(zReportTotalFourthType);
                    goneIfVisible(secondType);
                    goneIfVisible(thirdType);
                    goneIfVisible(fourthType);
                    goneIfVisible(zReportSecondTypeAmount);
                    goneIfVisible(zReportThirdTypeAmount);
                    goneIfVisible(zReportFourthTypeAmount);
                  /*  secondTypeCount.setVisibility(View.GONE);
                    thirdTypeCount.setVisibility(View.GONE);
                    fourthTypeCount.setVisibility(View.GONE);

                    zReportSecondTypeCount.setVisibility(View.GONE);
                    zReportTotalSecondType.setVisibility(View.GONE);
                    zReportThirdTypeCount.setVisibility(View.GONE);
                    zReportTotalThirdType.setVisibility(View.GONE);
                    zReportFourthTypeCount.setVisibility(View.GONE);
                    zReportTotalFourthType.setVisibility(View.GONE);

                    secondType.setVisibility(View.GONE);
                    thirdType.setVisibility(View.GONE);
                    fourthType.setVisibility(View.GONE);


                    zReportSecondTypeAmount.setVisibility(View.GONE);
                    zReportThirdTypeAmount.setVisibility(View.GONE);
                    zReportFourthTypeAmount.setVisibility(View.GONE);*/
                }




                invoiceReceiptCountText.setText(zReportCount.getInvoiceReceiptCount()+"");
                zReportTotalInvoiceReceipt.setText(Util.makePrice(zReport.getInvoiceReceiptAmount()));
                invoiceCountText.setText(zReportCount.getInvoiceCount()+"");
                zReportInvoice.setText(Util.makePrice(zReport.getInvoiceAmount()));
                creditInvoiceCount.setText(zReportCount.getCreditInvoiceCount()+"");
                zReportCreditInvoice.setText(Util.makePrice(zReport.getCreditInvoiceAmount()));
                zReportTotalSales.setText(Util.makePrice(zReport.getTotalSales()));
                zReportCashPaymentCount.setText(zReportCount.getCashCount()+"");
                zReportTotalCashPayment.setText(Util.makePrice(zReport.getCashTotal()));
                zReportFirstTypeCount.setText(zReportCount.getFirstTYpeCount()+"");
                Log.d("kwdjkd",Util.makePrice(zReport.getFirstTypeAmount()));
                zReportTotalFirstType.setText(Util.makePrice(zReport.getFirstTypeAmount()));
                totalPayPoint.setText(Util.makePrice(zReport.getTotalPayPoint()));
                totalPointCount.setText(zReportCount.getPayPointCount());






                zReportCreditCardCount.setText(zReportCount.getCreditCount()+"");
                zReportTotalCreditCard.setText(Util.makePrice(zReport.getCreditTotal()));
                zReportCheckCount.setText(zReportCount.getCheckCount()+"");
                zReportTotalCheck.setText(Util.makePrice(zReport.getCheckTotal()));
                zReportTotalAmount.setText(Util.makePrice(zReport.getTotalAmount()));
                zReportOpiningReportAmount.setText(Util.makePrice(aReportAmount));
                zReportOpiningReportCount.setText(opiningReportList.size()+"");
                zReportPullReportAmount.setText(Util.makePrice(zReport.getPullReportAmount()));
                zReportDepositReportAmount.setText(Util.makePrice(zReport.getDepositReportAmount()));
                zReportFirstTypeAmount.setText(Util.makePrice(aReportDetailsForFirstCurrency));



                zReportPosSales.setText(Util.makePrice(zReport.getTotalPosSales()));
                zReportTotalTax.setText(zReport.getTotalTax()+"");
                zReportSalesBeforeTax.setText(zReport.getSalesBeforeTax()+"");
                zReportSalesWithTax.setText(zReport.getSalesWithTax()+"");
                zReportTotalPrice.setText(Util.makePrice(zReport.getSalesBeforeTax()+zReport.getSalesWithTax()+zReport.getTotalTax()));
              Log.d("zReportDetials",zReport.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isCopy) {
                new AsyncTask<Void, Void, String>() {
                    Bitmap page;
                    Context a =ReportZDetailsActivity.this;

                    // create and show a progress dialog

                    ProgressDialog progressDialog = ProgressDialog.show(a, "", "Opening...");

                    @Override
                    protected void onPostExecute(String html) {
                        try
                        {
                            File path = new File( Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName());
                            File file = new File(path,"zreport.pdf");
                            RandomAccessFile f = new RandomAccessFile(file, "r");
                            byte[] data = new byte[(int)f.length()];
                            f.readFully(data);

                            pdfLoadImages(data);

                            Log.d("errrrr",bitmapList.size()+"");

                        }
                        catch(Exception ignored)
                        {
                            Log.d("errrrr",ignored.toString());
                        }

                        //after async close progress dialog
                        progressDialog.dismiss();
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        PdfUA pdfUA = new PdfUA();

                        try {
                            pdfUA.createZReport(ReportZDetailsActivity.this,zReport,zReportCount,false);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();


            }else {

                new AsyncTask<Void, Void, String>() {
                    Bitmap page;
                    Context a =ReportZDetailsActivity.this;

                    // create and show a progress dialog

                    ProgressDialog progressDialog = ProgressDialog.show(a, "", "Opening...");

                    @Override
                    protected void onPostExecute(String html) {
                        try
                        {
                            File path = new File( Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName());
                            File file = new File(path,"zreport.pdf");
                            RandomAccessFile f = new RandomAccessFile(file, "r");
                            byte[] data = new byte[(int)f.length()];
                            f.readFully(data);

                            pdfLoadImages(data);

                            Log.d("errrrr",bitmapList.size()+"");

                        }
                        catch(Exception ignored)
                        {
                            Log.d("errrrr",ignored.toString());
                        }

                        //after async close progress dialog
                        progressDialog.dismiss();
                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        PdfUA pdfUA = new PdfUA();

                        try {
                            pdfUA.createZReport(ReportZDetailsActivity.this,zReport,zReportCount,true);
                        } catch (DocumentException e) {
                            Log.d("testPdf","iiiii");
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.d("testPdf","iiiii");

                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();




                //p= bitmapList.get(bitmapList.size());

            }

        }

        // p=pt.createXReport(id,from, SESSION._EMPLOYEE,new java.util.Date()); // testing xReport

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(x==true) {
                    newBitmap2= newBitmap;
                    pt.PrintReport(newBitmap2);
                    onBackPressed();
                }else {
                    if(fromDashBoard){
                        newBitmap2=newBitmap;
                        pt.PrintReport(newBitmap2);

                   //     onBackPressed();

                    }else {
                        newBitmap2= newBitmap;
                        pt.PrintReport(newBitmap2);

              // onBackPressed();
                    }
                    btPrint.setClickable(false);

                }

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            str = extras.getString("permissions_name");


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
    private List<OrderDetails> orderList(List<Order> sales){
        List<OrderDetails> ol=new ArrayList<OrderDetails>();
        OrderDetailsDBAdapter orderDBAdapter=new OrderDetailsDBAdapter(this);
        orderDBAdapter.open();
        for (Order s : sales) {
            ol.addAll(orderDBAdapter.getOrderBySaleID(s.getOrderId()));
        }
        orderDBAdapter.close();
        return ol;
    }


    private void pdfLoadImages(final byte[] data)
    {
        bitmapList=new ArrayList<>();
        try
        {
            // run async
            new AsyncTask<Void, Void, String>()
            {
                // create and show a progress dialog

                @Override
                protected void onPostExecute(String html)
                {
                    Log.d("bitmapsize2222",bitmapList.size()+"");
                    newBitmap= combineImageIntoOne(bitmapList);
                    //after async close progress dialog
                    //load the html in the webview
                  //  wv.loadDataWithBaseURL("", html, "text/html","UTF-8", "");
                }

                @Override
                protected String doInBackground(Void... params)
                {
                    try
                    {
                        //create pdf document object from bytes
                        ByteBuffer bb = ByteBuffer.NEW(data);
                        PDFFile pdf = new PDFFile(bb);
                        //Get the first page from the pdf doc
                        PDFPage PDFpage = pdf.getPage(1, true);
                        //create a scaling value according to the WebView Width
                        final float scale = 800 / PDFpage.getWidth() * 0.80f;
                        //convert the page into a bitmap with a scaling value
                        Bitmap page = PDFpage.getImage((int)(PDFpage.getWidth() * scale), (int)(PDFpage.getHeight() * scale), null, true, true);
                        //save the bitmap to a byte array
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        stream.reset();
                        //convert the byte array to a base64 string
                        String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        //create the html + add the first image to the html
                        String html = "<!DOCTYPE html><html><body bgcolor=\"#ffffff\"><img src=\"data:image/png;base64," + base64 + "\" hspace=328 vspace=4><br>";                        //loop though the rest of the pages and repeat the above
                        for(int i = 1; i <= pdf.getNumPages(); i++)
                        {
                            PDFpage = pdf.getPage(i, true);
                            page = PDFpage.getImage((int)(PDFpage.getWidth() * scale), (int)(PDFpage.getHeight() * scale), null, true, true);
                            page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            bitmapList.add(page);
                            byteArray = stream.toByteArray();
                            stream.reset();
                            base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                            html += "<img src=\"data:image/png;base64,"+base64+"\" hspace=10 vspace=10><br>";
                        }
                        stream.close();
                        html += "</body></html>";
                        Log.d("mmmmmmm",bitmapList.size()+"");

                        return html;
                    }
                    catch (Exception e)
                    {
                        Log.d("error", e.toString());
                    }
                    return null;
                }
            }.execute();
            System.gc();// run GC
        }
        catch (Exception e)
        {
            Log.d("error", e.toString());
        }
    }
    private Bitmap combineImageIntoOne(ArrayList<Bitmap> bitmap) {
        int w = 0, h = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            if (i < bitmap.size() - 1) {
                w = bitmap.get(i).getWidth() > bitmap.get(i + 1).getWidth() ? bitmap.get(i).getWidth() : bitmap.get(i + 1).getWidth();
            }
            h += bitmap.get(i).getHeight();
        }

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            Log.d("HTML", "Combine: "+i+"/"+bitmap.size()+1);

            top = (i == 0 ? 0 : top+bitmap.get(i).getHeight());
            canvas.drawBitmap(bitmap.get(i), 0f, top, null);
        }
        return temp;
    }
    public void getCountForZReport(Context context, ZReport z) {
        List<CurrencyType> currencyTypesList = null;
        List<Currency> currencyList=new ArrayList<>();
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(ReportZDetailsActivity.this);
        currencyTypeDBAdapter.open();
        currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        Log.d("currencyTypesListooo",currencyTypesList.toString());

        for (int i=0;i<currencyTypesList.size();i++){
            CurrencyDBAdapter currencyDBAdapter =new CurrencyDBAdapter(this);
            currencyDBAdapter.open();
            currencyList.add(currencyDBAdapter.getCurrencyByCode(currencyTypesList.get(i).getType()));
            currencyDBAdapter.close();
        }
        Log.d("currencyKdd",currencyList.toString());
        Log.d("hyyg",currencyList.get(0).getId()+"");

        currencyTypeDBAdapter.close();
        ZReportDBAdapter zReportDBAdapter =new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        JSONObject res = new JSONObject();
        aReportAmount=0;
        opiningReportList=new ArrayList<>();
        aReportDetailsForFirstCurrency=0;
        aReportDetailsForSecondCurrency=0;
        aReportDetailsForThirdCurrency=0;
        aReportDetailsForForthCurrency=0;

        OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(context);
        opiningReportDBAdapter.open();
        if(zReportDBAdapter.getProfilesCount()==1) {
            opiningReportList = opiningReportDBAdapter.getListByLastZReport(-1);

        }else {

            opiningReportList = opiningReportDBAdapter.getListByLastZReport(z.getzReportId());
        }
        for (int i=0;i<opiningReportList.size();i++){
            aReportAmount+=opiningReportList.get(i).getAmount();
        }
        if (SETTINGS.enableCurrencies) {
            OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();
            for (int a=0 ;a<opiningReportList.size();a++) {
                //aReportAmount+=opiningReportList.get(a).getAmount();
                OpiningReport opiningReport = opiningReportList.get(a);
                aReportDetailsForFirstCurrency+= aReportDetailsDBAdapter.getLastRow(currencyList.get(0).getId(), opiningReport.getOpiningReportId());
                aReportDetailsForSecondCurrency+= aReportDetailsDBAdapter.getLastRow(currencyList.get(1).getId(), opiningReport.getOpiningReportId());
                aReportDetailsForThirdCurrency+= aReportDetailsDBAdapter.getLastRow(currencyList.get(2).getId(), opiningReport.getOpiningReportId());
                aReportDetailsForForthCurrency+= aReportDetailsDBAdapter.getLastRow(currencyList.get(3).getId(), opiningReport.getOpiningReportId());
            }


        }
        else {
            OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();
            for (int a=0 ;a<opiningReportList.size();a++) {
                OpiningReport opiningReport = opiningReportList.get(a);
                aReportDetailsForFirstCurrency += aReportDetailsDBAdapter.getLastRow(currencyList.get(0).getId(), opiningReport.getOpiningReportId());
            }
        }
       /* checkList=new ArrayList<>();
        cashAmount=0;
        invoiceReceiptCount=0 ;invoiceCount=0; CreditInvoiceCount=0 ; ShekelCount=0 ;UsdCount=0 ;EurCount=0; GbpCount=0 ;checkCount=0 ; creditCardCount=0 ;receiptInvoiceAmountCheck=0 ; cashCount=0;receiptInvoiceAmount=0;
        OrderDBAdapter orderDb = new OrderDBAdapter(context);
        orderDb.open();
        invoiceReceiptCount = orderDb.getBetween(z.getStartOrderId(),z.getEndOrderId()).size();

        if(zReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoiceList(-1, InvoiceStatus.UNPAID.getValue());
            invoiceCount+=posInvoiceList.size();

            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
            CreditInvoiceCount+=posCreditInvoiceList.size();

            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
            receiptInvoiceAmountCheck+=posReceiptList.size();

        }else {
            ZReport zReport1=null;
            try {
                zReport1 = zReportDBAdapter.getLastRow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoiceList(zReport1.getzReportId(), InvoiceStatus.UNPAID.getValue());
            invoiceCount+=posInvoiceList.size();


            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
            CreditInvoiceCount+=posCreditInvoiceList.size();
            List<PosInvoice>posReceiptListCheck = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
            receiptInvoiceAmountCheck+=posReceiptListCheck.size();
        }*/
      /*  List<Long>orderIds= new ArrayList<>();
        List<Payment> payments = paymentList(orderDb.getBetween(z.getStartOrderId(),z.getEndOrderId()),context);
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();
        for (Payment p : payments) {
            long orderId = p.getOrderId();
            List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<cashPaymentList.size();i++){
                cashCount+=1;
                cashAmount+=p.getAmount();
            }
            List<Check>checkList=checksDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<checkList.size();i++){
                checkCount+=1;
            }
            List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
            for(int i=0;i<creditCardPayments.size();i++){
                creditCardCount+=1;            }
        }
        if(orderIds.size()>0){
            for (int id = 0;id<orderIds.size();id++){
                ChecksDBAdapter checkDb= new ChecksDBAdapter(context);
                checkDb.open();
                List<Check> c = checkDb.getPaymentBySaleID(orderIds.get(id));
                checkList.add(c);
            }
        }
        //with Currency
        if (SETTINGS.enableCurrencies) {
            List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(orderDb.getBetween(z.getStartOrderId(),z.getEndOrderId()),context);
            for (CurrencyOperation cp : currencyOperationList) {
                switch (cp.getCurrencyType()) {
                    case "ILS":
                        ShekelCount+=1;
                        break;
                    case "USD":
                        UsdCount+=1;
                        break;
                    case "EUR":
                        EurCount+=1;

                        break;
                    case "GBP":
                        GbpCount+=1;
                        break;
                }
            }

        }
        if(zReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
            receiptInvoiceAmount+=posReceiptList.size();

        }else {
            ZReport zReport1=null;
            try {
                zReport1 = zReportDBAdapter.getLastRow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
            receiptInvoiceAmount+=posReceiptList.size();

        }
        ShekelCount+=receiptInvoiceAmount;
        checkCount+=receiptInvoiceAmountCheck;*/

    }
    public  void getCountForXReport(Context context, XReport x) {
        aReportAmount=0;
        opiningReportList=new ArrayList<>();
        aReportDetailsForFirstCurrency=0;
        aReportDetailsForSecondCurrency=0;
        aReportDetailsForThirdCurrency=0;
        aReportDetailsForForthCurrency=0;

        OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(context);
        opiningReportDBAdapter.open();
        opiningReportList = opiningReportDBAdapter.getListByLastZReport(x.getxReportId()-1);
        for (int i=0;i<opiningReportList.size();i++){
            aReportAmount+=opiningReportList.get(i).getAmount();
        }
        if (SETTINGS.enableCurrencies) {
            OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
            aReportDetailsDBAdapter.open();
            for (int a=0 ;a<opiningReportList.size();a++) {
                //aReportAmount+=opiningReportList.get(a).getAmount();
                OpiningReport opiningReport = opiningReportList.get(a);
                aReportDetailsForFirstCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.Shekel, opiningReport.getOpiningReportId());
                aReportDetailsForSecondCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.USD, opiningReport.getOpiningReportId());
                aReportDetailsForThirdCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.GBP, opiningReport.getOpiningReportId());
                aReportDetailsForForthCurrency+= aReportDetailsDBAdapter.getLastRow(CONSTANT.EUR, opiningReport.getOpiningReportId());
            }

        }
       /* checkList=new ArrayList<>();
        cashAmount=0;
        invoiceReceiptCount=0 ;invoiceCount=0; CreditInvoiceCount=0 ; ShekelCount=0 ;UsdCount=0 ;EurCount=0; GbpCount=0 ;checkCount=0 ; creditCardCount=0 ;receiptInvoiceAmountCheck=0 ; cashCount=0;receiptInvoiceAmount=0;
        OrderDBAdapter orderDb = new OrderDBAdapter(context);
        orderDb.open();
        XReportDBAdapter xReportDBAdapter = new XReportDBAdapter(context);
        xReportDBAdapter.open();
        invoiceReceiptCount = orderDb.getBetween(x.getStartOrderId(),x.getEndOrderId()).size();
        if(xReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoiceList(-1, InvoiceStatus.UNPAID.getValue());
            invoiceCount+=posInvoiceList.size();

            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
            CreditInvoiceCount+=posCreditInvoiceList.size();

            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
            receiptInvoiceAmountCheck+=posReceiptList.size();

        }else {
            XReport xReport1=null;
            try {
                xReport1 = xReportDBAdapter.getLastRow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoiceList(xReport1.getxReportId(), InvoiceStatus.UNPAID.getValue());
            invoiceCount+=posInvoiceList.size();

            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(xReport1.getxReportId(), DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
            CreditInvoiceCount+=posCreditInvoiceList.size();

            List<PosInvoice>posReceiptListCheck = posInvoiceDBAdapter.getPosInvoiceListByType(xReport1.getxReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
            receiptInvoiceAmountCheck+=posReceiptListCheck.size();
        }*/
       /* List<Long>orderIds= new ArrayList<>();
        List<Payment> payments = paymentList(orderDb.getBetween(x.getStartOrderId(),x.getEndOrderId()),context);
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();
        for (Payment p : payments) {
            long orderId = p.getOrderId();
            List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<cashPaymentList.size();i++){
                cashCount+=1;
                cashAmount+=p.getAmount();
            }
            List<Check>checkList=checksDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<checkList.size();i++){
                checkCount+=1;
            }
            List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
            for(int i=0;i<creditCardPayments.size();i++){
                creditCardCount+=1;            }
            *//*int i = 0;
            switch (p.getPaymentWay()) {

                case CONSTANT.CASH:
                  cashCount+=1;
                    cashAmount+=p.getAmount();
                    break;
                case CONSTANT.CREDIT_CARD:
                 creditCardCount+=1;
                    break;
                case CONSTANT.CHECKS:
                  checkCount+=1;
                    orderIds.add(p.getOrderId());
                    break;
            }*//*
        }
        if(orderIds.size()>0){
            for (int id = 0;id<orderIds.size();id++){
                ChecksDBAdapter checkDb= new ChecksDBAdapter(context);
                checkDb.open();
                List<Check> c = checkDb.getPaymentBySaleID(orderIds.get(id));
                checkList.add(c);
            }
        }
        //with Currency
        if (SETTINGS.enableCurrencies) {
            List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(orderDb.getBetween(x.getStartOrderId(),x.getEndOrderId()),context);
            for (CurrencyOperation cp : currencyOperationList) {
                switch (cp.getCurrencyType()) {
                    case "ILS":
                        ShekelCount+=1;
                        break;
                    case "USD":
                        UsdCount+=1;
                        break;
                    case "EUR":
                        EurCount+=1;

                        break;
                    case "GBP":
                        GbpCount+=1;
                        break;
                }
            }

        }
        if(xReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
            receiptInvoiceAmount+=posReceiptList.size();

        }else {
            XReport xReport1=null;
            try {
                xReport1 = xReportDBAdapter.getLastRow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(xReport1.getxReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
            receiptInvoiceAmount+=posReceiptList.size();

        }
        ShekelCount+=receiptInvoiceAmount;
        checkCount+=receiptInvoiceAmountCheck;
*/
    }
}