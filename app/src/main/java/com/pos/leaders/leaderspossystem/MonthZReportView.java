package com.pos.leaders.leaderspossystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportCountDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Models.ZReportCount;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthZReportView extends AppCompatActivity {
    private List<ZReport> zReportList;
    private List<ZReportCount> zReportCountList;
    private ZReportDBAdapter zReportDBAdapter;
    ZReportCountDbAdapter zReportCountDbAdapter;
    private EmployeeDBAdapter userDBAdapter;
    Button btCancel,btPrint;
    Bitmap p;
    PrintTools pt;
    String str;
    Date from, to;
    EditText etFrom, etTo;
    private static final int DIALOG_FROM_DATE = 825;
    private static final int DIALOG_TO_DATE = 324;
    private final static int DAY_MINUS_ONE_SECOND = 86399999;
    ImageView imageView ;
    public static ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
    Bitmap newBitmap =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_month_zreport_view);

        TitleBar.setTitleBar(this);
        btCancel=(Button)findViewById(R.id.reportZDetails_btCancel);
        btPrint=(Button)findViewById(R.id.reportZDetails_btPrint);
        etFrom = (EditText) findViewById(R.id.zReportManagement_ETFrom);
        etTo = (EditText) findViewById(R.id.zReportManagement_ETTo);
        imageView=(ImageView)findViewById(R.id.reportZDetails_ivInvoice);

        etFrom.setFocusable(false);
        etFrom.setText(DateConverter.getBeforeMonth().split(" ")[0]);
        from = DateConverter.stringToDate(DateConverter.getBeforeMonth());
        etTo.setFocusable(false);
        etTo.setText(DateConverter.currentDateTime().split(" ")[0]);
        to = DateConverter.stringToDate(DateConverter.currentDateTime());

        etFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_FROM_DATE);
            }
        });

        etTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TO_DATE);
            }
        });

        //region Init
        zReportDBAdapter = new ZReportDBAdapter(this);
        zReportCountDbAdapter=new ZReportCountDbAdapter(this);
        userDBAdapter = new EmployeeDBAdapter(this);
        userDBAdapter.open();
        zReportDBAdapter.open();
        zReportCountDbAdapter.open();
        zReportList = new ArrayList<ZReport>();
        zReportCountList=new ArrayList<>();
        setDate();
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newBitmap!=null){
                pt.PrintReport(newBitmap);
                onBackPressed();}
            }
        });
    }

    @Override
    protected void onDestroy() {
        zReportDBAdapter.close();
        userDBAdapter.close();
        zReportCountDbAdapter.close();
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    private void setDate() {
        ZReportCount zReportCount=null;
        double totalAmount=0;
        double totalSales=0;
        double cashTotal=0;
        double checkTotal=0;
        double creditTotal=0;
        double totalPosSales=0;
        double invoiceAmount=0;
        double creditInvoiceAmount=0;
        double shekelAmount=0;
        double usdAmount=0;
        double eurAmount=0;
        double gbpAmount=0;
        double invoiceReceiptAmount=0;

        double salesBeforeTax=0;
        double salesWithTax=0;
        double totalTax=0;

        int cashTotalC=0;
        int checkTotalC=0;
        int creditTotalC=0;
        int totalPosSalesC=0;
        int invoiceAmountC=0;
        int creditInvoiceAmountC=0;
        int shekelAmountC=0;
        int usdAmountC=0;
        int eurAmountC=0;
        int gbpAmountC=0;
        int invoiceReceiptAmountC=0;

        zReportDBAdapter.open();
        zReportList=new ArrayList<>();
        zReportList = zReportDBAdapter.getBetweenTwoDates(from.getTime(), to.getTime()+ DAY_MINUS_ONE_SECOND);
        zReportCountList=new ArrayList<>();
        for(int i=0;i<zReportList.size();i++){
           ZReportCount zReportCt=zReportCountDbAdapter.getByID(zReportList.get(i).getzReportId());
            zReportCountList.add(zReportCt);
        }

        if(zReportCountList.size()>0){

        for (int i=0;i<zReportCountList.size();i++){

            cashTotalC+=zReportCountList.get(i).getCashCount();
            checkTotalC+=zReportCountList.get(i).getCheckCount();
            creditTotalC+=zReportCountList.get(i).getCreditCount();
            invoiceAmountC+=zReportCountList.get(i).getInvoiceCount();
            creditInvoiceAmountC+=zReportCountList.get(i).getCreditInvoiceCount();
            shekelAmountC+=zReportCountList.get(i).getShekelCount();
            usdAmountC+=zReportCountList.get(i).getUsdCount();
            eurAmountC+=zReportCountList.get(i).getEurCount();
            gbpAmountC+=zReportCountList.get(i).getGbpCount();
            invoiceReceiptAmountC+=zReportCountList.get(i).getInvoiceReceiptCount();
        }
        }
        Log.d("ZreportList",zReportList.toString());

        if(zReportList.size()>0){
            imageView.setVisibility(View.VISIBLE);
            ZReport zReport = null;
            for(int i = 0 ; i<zReportList.size();i++){
            totalAmount+=zReportList.get(i).getTotalAmount();
            cashTotal+=zReportList.get(i).getCashTotal();
            checkTotal+=zReportList.get(i).getCheckTotal();
            creditTotal+=zReportList.get(i).getCreditTotal();
            totalPosSales+=zReportList.get(i).getTotalPosSales();
            invoiceAmount+=zReportList.get(i).getInvoiceAmount();
            creditInvoiceAmount+=zReportList.get(i).getCreditInvoiceAmount();
            shekelAmount+=zReportList.get(i).getShekelAmount();
            usdAmount+=zReportList.get(i).getUsdAmount();
            eurAmount+=zReportList.get(i).getEurAmount();
            gbpAmount+=zReportList.get(i).getGbpAmount();
            salesBeforeTax+=zReportList.get(i).getSalesBeforeTax();
            salesWithTax+=zReportList.get(i).getSalesWithTax();
            totalTax+=zReportList.get(i).getTotalTax();


            invoiceReceiptAmount+=zReportList.get(i).getInvoiceReceiptAmount();

            }
            totalSales=invoiceReceiptAmount+invoiceAmount+creditInvoiceAmount;

        zReport=new ZReport(0,new Timestamp(System.currentTimeMillis()),zReportList.get(0).getByUser(),0,0,totalAmount,totalSales,cashTotal,checkTotal,creditTotal,totalPosSales,zReportList.get(0).getTax(),invoiceAmount,creditInvoiceAmount,shekelAmount,usdAmount,eurAmount,gbpAmount,invoiceReceiptAmount,0,0,"close",salesBeforeTax,salesWithTax,totalTax);
            Log.d("zReportList",zReport.toString());


                zReportCount=new ZReportCount(0,cashTotalC,checkTotalC,creditTotalC,invoiceAmountC,creditInvoiceAmountC,shekelAmountC,usdAmountC,eurAmountC,gbpAmountC,invoiceReceiptAmountC,0);
                zReport=new ZReport(0,new Timestamp(System.currentTimeMillis()),zReportList.get(0).getByUser(),0,0,totalAmount,totalSales,cashTotal,checkTotal,creditTotal,zReportList.get(zReportList.size()-1).getTotalPosSales(),zReportList.get(0).getTax(),invoiceAmount,creditInvoiceAmount,shekelAmount,usdAmount,eurAmount,gbpAmount,invoiceReceiptAmount,0,0,"close",salesBeforeTax,salesWithTax,totalTax);


            PdfUA pdfUA = new PdfUA();

            try {
                pdfUA.createMonthZReport(MonthZReportView.this,zReport,from,to,zReportCount);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try
            {
                File path = new File( Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName());
                File file = new File(path,"mounthzreport.pdf");
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
        pt=new PrintTools(MonthZReportView.this);
        }else {
            imageView.setVisibility(View.INVISIBLE);
        }


    }



    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_FROM_DATE) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, onFromDateSetListener, Integer.parseInt(from.toString().split(" ")[5]), from.getMonth(), Integer.parseInt(from.toString().split(" ")[2]));
            //datePickerDialog.getDatePicker().setMaxDate(to.getTime());
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            return datePickerDialog;
        } else if (id == DIALOG_TO_DATE) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, onToDateSetListener, Integer.parseInt(to.toString().split(" ")[5]), to.getMonth(), Integer.parseInt(to.toString().split(" ")[2]));
            //datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            //datePickerDialog.getDatePicker().setMinDate(from.getTime());
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener onFromDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            etFrom.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            from = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
            view.setMaxDate(to.getTime());
            setDate();
        }
    };

    private DatePickerDialog.OnDateSetListener onToDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            etTo.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            to = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
            view.setMinDate(from.getTime());
            setDate();
        }
    };
    private void pdfLoadImages(final byte[] data)
    {
        bitmapList=new ArrayList<>();
        try
        {
            // run async
            new AsyncTask<Void, Void, String>()
            {
                // create and show a progress dialog
                ProgressDialog progressDialog = ProgressDialog.show(MonthZReportView.this, "", "Opening...");

                @Override
                protected void onPostExecute(String html)
                {
                    Log.d("bitmapsize2222",bitmapList.size()+"");
                    if(newBitmap!=null){
                    newBitmap= Util.removeMargins2(newBitmap, Color.WHITE);
                    }
                    newBitmap= combineImageIntoOne(bitmapList);
                    imageView.setImageBitmap(newBitmap);

                    //after async close progress dialog
                    progressDialog.dismiss();
                    //load the html in the webview
                    //  wv.loadDataW
                    // ithBaseURL("", html, "text/html","UTF-8", "");
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
