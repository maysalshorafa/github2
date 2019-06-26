package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ImageView;

import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.XReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.XReport;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import POSSDK.POSSDK;

public class ReportZDetailsActivity extends Activity {
    Button btCancel,btPrint;
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
    public static ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
    Bitmap newBitmap =null;
    boolean fromDashBoard=false;
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
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            x= getIntent().getExtras().getBoolean(ReportsManagementActivity.COM_LEADPOS_XREPORT_FLAG);
            if (x==true) {
                id = (long) extras.get(ReportsManagementActivity.COM_LEADPOS_XREPORT_ID);
                from = (long) extras.get(ReportsManagementActivity.COM_LEADPOS_XREPORT_FORM);
                to = (long) extras.get(ReportsManagementActivity.COM_LEADPOS_XREPORT_TO);
                totalZReportAmount = (double) extras.get(ReportsManagementActivity.COM_LEADPOS_XREPORT_TOTAL_AMOUNT);
                amount = (double) extras.get(ReportsManagementActivity.COM_LEADPOS_XREPORT_AMOUNT);
            } else {
                id = (long) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_ID);
                from = (long) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_FORM);
                to = (long) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_TO);
                totalZReportAmount = (double) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_TOTAL_AMOUNT);
                amount = (double) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_AMOUNT);

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

            XReport xReport = xReportDBAdapter.getByID(id);
            PdfUA pdfUA = new PdfUA();

            try {
                pdfUA.createXReport(ReportZDetailsActivity.this,xReport);
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
            ZReport zReport = zReportDBAdapter.getByID(id);

            if (isCopy) {
                PdfUA pdfUA = new PdfUA();

                try {
                    pdfUA.createZReport(ReportZDetailsActivity.this,zReport,false);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try
                {
                    File path = new File( Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName());
                    File file = new File(path,"zreport.pdf");
                    RandomAccessFile f = new RandomAccessFile(file, "r");
                    byte[] data = new byte[(int)f.length()];
                    f.readFully(data);
                    pdfLoadImages(data);
                    Log.d("bitmapsize",bitmapList.size()+"");
                }
                catch(Exception ignored)
                {

                }


            }else {
              PdfUA pdfUA = new PdfUA();

                try {
                    pdfUA.createZReport(ReportZDetailsActivity.this,zReport,true);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                       finish();
                        Intent intent = new Intent(ReportZDetailsActivity.this,LogInActivity.class);
                        startActivity(intent);
                    }else {
                        pt.PrintReport(newBitmap);
                        onBackPressed();
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
                ProgressDialog progressDialog = ProgressDialog.show(ReportZDetailsActivity.this, "", "Opening...");

                @Override
                protected void onPostExecute(String html)
                {
                    Log.d("bitmapsize2222",bitmapList.size()+"");
                    newBitmap= combineImageIntoOne(bitmapList);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ((ImageView)findViewById(R.id.reportZDetails_ivInvoice)).setImageBitmap(newBitmap);

                    //after async close progress dialog
                    progressDialog.dismiss();
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

}