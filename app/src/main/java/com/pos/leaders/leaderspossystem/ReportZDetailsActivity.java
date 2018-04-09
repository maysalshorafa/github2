package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFImage;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPaint;

import net.sf.andpdf.nio.ByteBuffer;
import net.sf.andpdf.refs.HardReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import POSSDK.POSSDK;
public class ReportZDetailsActivity extends Activity  {
    Button btCancel,btPrint;
    POSSDK pos;
    Bitmap p;
    PrintTools pt;
    String str;
    long from=0,to=0,id=0;
    boolean goBack = false;
    double totalZReportAmount =0.0 ;
    private WebView wv , wv1;
    private int ViewSize = 0;
    private int ViewSize1 = 0;
    public static final String SAMPLE_FILE = "randompdf.pdf";
    Bitmap page,page1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_report_zdetails);


        btCancel=(Button)findViewById(R.id.reportZDetails_btCancel);
        btPrint=(Button)findViewById(R.id.reportZDetails_btPrint);
       //pdfView= (PDFView)findViewById(R.id.pdfView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id=(long)extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_ID);
            from = (long) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_FORM);
            to = (long) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_TO);
            totalZReportAmount =(double)extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_TOTAL_AMOUNT);
            if (extras.containsKey(ZReportActivity.COM_LEADPOS_ZREPORT_HISTORY)) {
                goBack = extras.getBoolean(ZReportActivity.COM_LEADPOS_ZREPORT_HISTORY, false);

            }
        }
      // p=pt.createZReport(id,from,to,true,totalZReportAmount);
     try {
            PdfUA.createZReportPdf(getApplicationContext(),id,from,to,true,totalZReportAmount);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // p=pt.createXReport(id,from, SESSION._USER,new java.util.Date()); // testing xReport
      //  ((ImageView)findViewById(R.id.reportZDetails_ivInvoice)).setImageBitmap(p);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pt=new PrintTools(ReportZDetailsActivity.this);
                pt.PrintReport(page);
                goHome();
            }
        });

        PDFImage.sShowImages = true; // show images
        PDFPaint.s_doAntiAlias = true; // make text smooth
        HardReference.sKeepCaches = true; // save images in cache

        //Setup webview
        wv = (WebView)findViewById(R.id.webView1);
        wv.getSettings().setBuiltInZoomControls(true);//show zoom buttons
        wv.getSettings().setSupportZoom(true);//allow zoom
        //get the width of the webview
        wv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                ViewSize = wv.getWidth();
                wv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
      /***  wv1 = (WebView)findViewById(R.id.webView2);
        wv1.getSettings().setBuiltInZoomControls(true);//show zoom buttons
        wv1.getSettings().setSupportZoom(true);//allow zoom
        wv1.setInitialScale(1);
        wv1.getSettings().setLoadWithOverviewMode(true);
        wv1.getSettings().setUseWideViewPort(true);
        wv1.setBackgroundColor(0);
        //get the width of the webview
        wv1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                ViewSize1 = wv1.getWidth();
                wv1.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });**/
        try
        {
            File path = new File( Environment.getExternalStorageDirectory(), getPackageName() );
            File file = new File(path,SAMPLE_FILE);
            RandomAccessFile f = new RandomAccessFile(file, "r");
            byte[] data = new byte[(int)f.length()];

            f.readFully(data);
            pdfLoadImages(data);
            //pdfLoadImages1(data);
        }
        catch(Exception ignored)
        {

        }
    }


    private void goHome() {
        if (!goBack) {
            Intent intent = new Intent(ReportZDetailsActivity.this, LogInActivity.class);
            intent.putExtra("permissions_name", str);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(LogInActivity.LEADPOS_MAKE_A_REPORT, LogInActivity.LEADPOS_MAKE_A_REPORT);
            startActivity(intent);
        }
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            str = extras.getString("permissions_name");


        }

    }

    @Override
    public void onBackPressed() {
        goHome();
    }

    private List<Order> orderList(List<Sale> sales){
        List<Order> ol=new ArrayList<Order>();
        OrderDBAdapter orderDBAdapter=new OrderDBAdapter(this);
        orderDBAdapter.open();
        for (Sale s : sales) {
            ol.addAll(orderDBAdapter.getOrderBySaleID(s.getId()));
        }
        orderDBAdapter.close();
        return ol;
    }

private void pdfLoadImages(final byte[] data) {
    try {
        // run async
        new AsyncTask<Void, Void, String>() {
            // create and show a progress dialog
            ProgressDialog progressDialog = ProgressDialog.show(ReportZDetailsActivity.this, "", "Opening...");

            @Override
            protected void onPostExecute(String html) {
                //after async close progress dialog
                progressDialog.dismiss();
                //load the html in the webview
                wv.loadDataWithBaseURL("", html, "randompdf/html", "UTF-8", "");
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    //create pdf document object from bytes
                    ByteBuffer bb = ByteBuffer.NEW(data);
                    PDFFile pdf = new PDFFile(bb);
                    //Get the first page from the pdf doc
                    PDFPage PDFpage = pdf.getPage(1, true);
                    //create a scaling value according to the WebView Width
                    final float scale = ViewSize / PDFpage.getWidth() * 0.48f;
                    //convert the page into a bitmap with a scaling value
                     page = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
                    //save the bitmap to a byte array
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    stream.reset();
                    //convert the byte array to a base64 string
                    String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    //create the html + add the first image to the html
                    String html = "<!DOCTYPE html><html><body bgcolor=\"#ffffff\"><img src=\"data:image/png;base64," + base64 + "\" hspace=328 vspace=4><br>";
                    //loop though the rest of the pages and repeat the above
                    for (int i = 2; i <= pdf.getNumPages(); i++) {
                        PDFpage = pdf.getPage(i, true);
                        page = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
                        page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byteArray = stream.toByteArray();
                        stream.reset();
                        base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        html += "<img src=\"data:image/png;base64," + base64 + "\" hspace=10 vspace=10><br>";
                    }
                    stream.close();
                    html += "</body></html>";
                    return html;
                } catch (Exception e) {
                    Log.d("error", e.toString());
                }
                return null;
            }
        }.execute();
        System.gc();// run GC
    } catch (Exception e) {
        Log.d("error", e.toString());
    }
}
    private void pdfLoadImages1(final byte[] data) {
        try {
            // run async
            new AsyncTask<Void, Void, String>() {
                // create and show a progress dialog
                ProgressDialog progressDialog = ProgressDialog.show(ReportZDetailsActivity.this, "", "Opening...");

                @Override
                protected void onPostExecute(String html) {
                    //after async close progress dialog
                    progressDialog.dismiss();
                    //load the html in the webview
                    wv1.loadDataWithBaseURL("", html, "randompdf/html", "UTF-8", "");
                }

                @Override
                protected String doInBackground(Void... params) {
                    try {
                        //create pdf document object from bytes
                        ByteBuffer bb = ByteBuffer.NEW(data);
                        PDFFile pdf = new PDFFile(bb);
                        //Get the first page from the pdf doc
                        PDFPage PDFpage = pdf.getPage(1, true);
                        //create a scaling value according to the WebView Width
                        final float scale = ViewSize1 / PDFpage.getWidth() * 0.20f;
                        //convert the page into a bitmap with a scaling value
                        page1 = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
                        //save the bitmap to a byte array
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        page1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        stream.reset();
                        //convert the byte array to a base64 string
                        String base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                        //create the html + add the first image to the html
                        String html = "<!DOCTYPE html><html><body bgcolor=\"#ffffff\"><img src=\"data:image/png;base64," + base64 + "\" hspace=328 vspace=4><br>";
                        //loop though the rest of the pages and repeat the above
                        for (int i = 2; i <= pdf.getNumPages(); i++) {
                            PDFpage = pdf.getPage(i, true);
                            page1 = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
                            page1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byteArray = stream.toByteArray();
                            stream.reset();
                            base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                            html += "<img src=\"data:image/png;base64," + base64 + "\" hspace=10 vspace=10><br>";
                        }
                        stream.close();
                        html += "</body></html>";
                        return html;
                    } catch (Exception e) {
                        Log.d("error", e.toString());
                    }
                    return null;
                }
            }.execute();
            System.gc();// run GC
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
    }

}
