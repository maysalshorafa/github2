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
import android.widget.TextView;

import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.XReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportCountDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Check;
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
            zReportShekelCount,zReportTotalShekel,zReportUsdCount,zReportTotalUsd,zReportEurCount,zReportTotalEur,
            zReportGbpCount,zReportTotalGbp,zReportCreditCardCount,zReportTotalCreditCard,zReportCheckCount,
            zReportTotalCheck,zReportTotalAmount,zReportOpiningReportAmount,zReportOpiningReportCount,zReportPullReportAmount
            ,zReportDepositReportAmount,zReportShekelAmount,zReportUsdAmount,zReportGbpAmount,zReportEurAmount
            ,zReportPosSales;
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
        zReportShekelCount=(TextView)findViewById(R.id.zReportShekelCount);
        zReportTotalShekel=(TextView)findViewById(R.id.zReportTotalShekel);
        zReportUsdCount=(TextView)findViewById(R.id.zReportUsdCount);
        zReportTotalUsd=(TextView)findViewById(R.id.zReportTotalUsd);
        zReportEurCount=(TextView)findViewById(R.id.zReportEurCount);
        zReportTotalEur=(TextView)findViewById(R.id.zReportTotalEur);
        zReportGbpCount=(TextView)findViewById(R.id.zReportGbpCount);
        zReportTotalGbp=(TextView)findViewById(R.id.zReportTotalGbp);
        zReportCreditCardCount=(TextView)findViewById(R.id.zReportCreditCardCount);
        zReportTotalCreditCard=(TextView)findViewById(R.id.zReportTotalCreditCard);
        zReportCheckCount=(TextView)findViewById(R.id.zReportCheckCount);
        zReportTotalCheck=(TextView)findViewById(R.id.zReportTotalCheck);
        zReportTotalAmount=(TextView)findViewById(R.id.zReportTotalAmount);
        zReportOpiningReportAmount=(TextView)findViewById(R.id.zReportOpiningReportAmount);
        zReportOpiningReportCount=(TextView)findViewById(R.id.zReportOpiningReportCount);
        zReportPullReportAmount=(TextView)findViewById(R.id.zReportPullReportAmount);
        zReportDepositReportAmount=(TextView)findViewById(R.id.zReportDepositReportAmount);
        zReportShekelAmount=(TextView)findViewById(R.id.zReportShekelAmount);
        zReportUsdAmount=(TextView)findViewById(R.id.zReportUsdAmount);
        zReportGbpAmount=(TextView)findViewById(R.id.zReportGbpAmount);
        zReportEurAmount=(TextView)findViewById(R.id.zReportEurAmount);
        zReportPosSales=(TextView)findViewById(R.id.zReportPosSales);

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
                invoiceReceiptCountText.setText(zReportCount.getInvoiceReceiptCount()+"");
                zReportTotalInvoiceReceipt.setText(Util.makePrice(xReport.getInvoiceReceiptAmount()));
                invoiceCountText.setText(zReportCount.getInvoiceCount()+"");
                zReportInvoice.setText(Util.makePrice(xReport.getInvoiceAmount()));
                creditInvoiceCount.setText(zReportCount.getCreditInvoiceCount()+"");
                zReportCreditInvoice.setText(Util.makePrice(xReport.getCreditInvoiceAmount()));
                zReportTotalSales.setText(Util.makePrice(xReport.getTotalSales()));
                zReportCashPaymentCount.setText(zReportCount.getCashCount()+"");
                zReportTotalCashPayment.setText(Util.makePrice(xReport.getCashTotal()));
                zReportShekelCount.setText(zReportCount.getShekelCount()+"");
                zReportTotalShekel.setText(Util.makePrice(xReport.getShekelAmount()));
                zReportUsdCount.setText(zReportCount.getUsdCount()+"");
                zReportTotalUsd.setText(Util.makePrice(xReport.getUsdAmount()));
                zReportEurCount.setText(zReportCount.getEurCount()+"");
                zReportTotalEur.setText(Util.makePrice(xReport.getEurAmount()));
                zReportGbpCount.setText(zReportCount.getGbpCount()+"");
                zReportTotalGbp.setText(Util.makePrice(xReport.getGbpAmount()));
                zReportCreditCardCount.setText(zReportCount.getCreditCount()+"");
                zReportTotalCreditCard.setText(Util.makePrice(xReport.getCreditTotal()));
                zReportCheckCount.setText(zReportCount.getCheckCount()+"");
                zReportTotalCheck.setText(Util.makePrice(xReport.getCheckTotal()));
                zReportTotalAmount.setText(Util.makePrice(xReport.getTotalAmount()));
                zReportOpiningReportAmount.setText(Util.makePrice(aReportAmount));
                zReportOpiningReportCount.setText(opiningReportList.size()+"");
                zReportPullReportAmount.setText(Util.makePrice(xReport.getPullReportAmount()));
                zReportDepositReportAmount.setText(Util.makePrice(xReport.getDepositReportAmount()));
                zReportShekelAmount.setText(Util.makePrice(aReportDetailsForFirstCurrency));
                zReportUsdAmount.setText(Util.makePrice(aReportDetailsForSecondCurrency));
                zReportGbpAmount.setText(Util.makePrice(aReportDetailsForThirdCurrency));;
                zReportEurAmount.setText(Util.makePrice(aReportDetailsForForthCurrency));;
                zReportPosSales.setText(Util.makePrice(xReport.getTotalPosSales()));;

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
            ZReportCountDbAdapter zReportCountDbAdapter = new ZReportCountDbAdapter(ReportZDetailsActivity.this);
            zReportCountDbAdapter.open();
            final ZReportCount zReportCount = zReportCountDbAdapter.getByID(zReport.getzReportId());
            Log.d("testZreportCountfffff",zReportCount.toString());
            getCountForZReport(ReportZDetailsActivity.this,zReport);

            try {
                invoiceReceiptCountText.setText(zReportCount.getInvoiceReceiptCount()+"");
                zReportTotalInvoiceReceipt.setText(Util.makePrice(zReport.getInvoiceReceiptAmount()));
                invoiceCountText.setText(zReportCount.getInvoiceCount()+"");
                zReportInvoice.setText(Util.makePrice(zReport.getInvoiceAmount()));
                creditInvoiceCount.setText(zReportCount.getCreditInvoiceCount()+"");
                zReportCreditInvoice.setText(Util.makePrice(zReport.getCreditInvoiceAmount()));
                zReportTotalSales.setText(Util.makePrice(zReport.getTotalSales()));
                zReportCashPaymentCount.setText(zReportCount.getCashCount()+"");
                zReportTotalCashPayment.setText(Util.makePrice(zReport.getCashTotal()));
                zReportShekelCount.setText(zReportCount.getShekelCount()+"");
                zReportTotalShekel.setText(Util.makePrice(zReport.getShekelAmount()));
                zReportUsdCount.setText(zReportCount.getUsdCount()+"");
                zReportTotalUsd.setText(Util.makePrice(zReport.getUsdAmount()));
                zReportEurCount.setText(zReportCount.getEurCount()+"");
                zReportTotalEur.setText(Util.makePrice(zReport.getEurAmount()));
                zReportGbpCount.setText(zReportCount.getGbpCount()+"");
                zReportTotalGbp.setText(Util.makePrice(zReport.getGbpAmount()));
                zReportCreditCardCount.setText(zReportCount.getCreditCount()+"");
                zReportTotalCreditCard.setText(Util.makePrice(zReport.getCreditTotal()));
                zReportCheckCount.setText(zReportCount.getCheckCount()+"");
                zReportTotalCheck.setText(Util.makePrice(zReport.getCheckTotal()));
                zReportTotalAmount.setText(Util.makePrice(zReport.getTotalAmount()));
                zReportOpiningReportAmount.setText(Util.makePrice(aReportAmount));
                zReportOpiningReportCount.setText(opiningReportList.size()+"");
                zReportPullReportAmount.setText(Util.makePrice(zReport.getPullReportAmount()));
                zReportDepositReportAmount.setText(Util.makePrice(zReport.getDepositReportAmount()));
                zReportShekelAmount.setText(Util.makePrice(aReportDetailsForFirstCurrency));
                zReportUsdAmount.setText(Util.makePrice(aReportDetailsForSecondCurrency));
                zReportGbpAmount.setText(Util.makePrice(aReportDetailsForThirdCurrency));;
                zReportEurAmount.setText(Util.makePrice(aReportDetailsForForthCurrency));;
                zReportPosSales.setText(Util.makePrice(zReport.getTotalPosSales()));;

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
                            e.printStackTrace();
                        } catch (IOException e) {
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
                    pt.PrintReport(newBitmap);
                    onBackPressed();
                }else {
                    if(fromDashBoard){
                        pt.PrintReport(newBitmap);
                   //     onBackPressed();

                    }else {
                        pt.PrintReport(newBitmap);
              // onBackPressed();
                    }

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