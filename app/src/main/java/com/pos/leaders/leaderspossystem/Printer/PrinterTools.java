package com.pos.leaders.leaderspossystem.Printer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.PdfUA;
import com.pos.leaders.leaderspossystem.Printer.SM_S230I.MiniPrinterFunctions;
import com.pos.leaders.leaderspossystem.Printer.SUNMI_T1.AidlUtil;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import HPRTAndroidSDK.HPRTPrinterHelper;
import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;

/**
 * Created by Win8.1 on 3/3/2019.
 */

public class PrinterTools {
    static POSSDK pos;


    public static void printAndOpenCashBox(String mainAns, final String mainMer, final String mainCli, int source, Context context, Activity c) {
        switch (SETTINGS.printer) {
            case BTP880:
                printAndOpenCashBoxBTP880(mainAns, mainMer, mainCli,context,c);
                break;
            case SUNMI_T1:
                printAndOpenCashBoxSUNMI_T1(mainAns, mainMer, mainCli,context,c);
                break;
           case HPRT_TP805:
                printAndOpenCashBoxHPRT_TP805(mainAns, mainMer, mainCli,context,c);
                break;
            case SM_S230I:
                printAndOpenCashBoxSM_S230I(mainAns, mainMer, mainCli,context,c);
                break;
        }

    }
    private static void printAndOpenCashBoxBTP880(final String mainAns, final String mainMer, final String mainCli, final Context context, final Activity activity) {
        final POSInterfaceAPI posInterfaceAPI = new POSUSBAPI(context);
        // final UsbPrinter printer = new UsbPrinter(1155, 30016);

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                dialog.show();
                ////Hebrew 15 Windows-1255

                int i = posInterfaceAPI.OpenDevice();
                pos = new POSSDK(posInterfaceAPI);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                pos.systemFeedLine(2);
                pos.systemCutPaper(66, 0);
                pos.cashdrawerOpen(0, 20, 20);

                posInterfaceAPI.CloseDevice();
                dialog.cancel();
                activity.finish();

            }

            @Override
            protected Void doInBackground(Void... params) {
                PdfUA pdfUA = new PdfUA();

                try {
                    pdfUA.createNormalInvoice(context,SESSION._ORDER_DETAILES,SESSION._ORDERS,false,mainMer);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try
                {
                    File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                    File file = new File(path,"normalInvoice.pdf");
                    RandomAccessFile f = new RandomAccessFile(file, "r");
                    byte[] data = new byte[(int)f.length()];
                    f.readFully(data);
                    Log.d("bitmapsize",context.toString()+"");

                    pdfLoadImages(data,context);
                }
                catch(Exception ignored)
                {

                }
                InvoiceImg invoiceImg = new InvoiceImg(context);
                Log.d("payyyyy",SESSION._ORDERS.getPayment().toString());
                pos.imageStandardModeRasterPrint(newBitmap, CONSTANT.PRINTER_PAGE_WIDTH);

              //  pos.imageStandardModeRasterPrint(invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null), CONSTANT.PRINTER_PAGE_WIDTH);

                return null;
            }
        }.execute();
    }
    private static void printAndOpenCashBoxSUNMI_T1(String mainAns, final String mainMer, final String mainCli,Context context,Activity activity) {
        //AidlUtil.getInstance().connectPrinterService(this);
        if (AidlUtil.getInstance().isConnect()) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

            dialog.show();
            PdfUA pdfUA = new PdfUA();

            try {
                pdfUA.createNormalInvoice(context,SESSION._ORDER_DETAILES,SESSION._ORDERS,false,mainMer);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try
            {
                File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                File file = new File(path,"normalInvoice.pdf");
                RandomAccessFile f = new RandomAccessFile(file, "r");
                byte[] data = new byte[(int)f.length()];
                f.readFully(data);
                Log.d("bitmapsize",context.toString()+"");

                pdfLoadImages(data,context);
            }
            catch(Exception ignored)
            {

            }
         try {
                    Bitmap bitmap = newBitmap;
                 AidlUtil.getInstance().printBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //cut
                AidlUtil.getInstance().print3Line();
                AidlUtil.getInstance().cut();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                AidlUtil.getInstance().openCashBox();
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.cancel();
           activity.finish();
        } else {
            new android.support.v7.app.AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle(context.getString(R.string.printer))
                    .setMessage(context.getString(R.string.please_connect_the_printer))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           // finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            //Toast.makeText(this, "Printer Connect Error!", Toast.LENGTH_LONG).show();
        }
    }
    private static void printAndOpenCashBoxHPRT_TP805(final String mainAns, final String mainMer, final String mainCli, final Context context, final Activity activity) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();

                }

                @Override
                protected void onPostExecute(Void aVoid) {
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
                    }


                    dialog.cancel();
                    activity.finish();
                }

                @Override
                protected Void doInBackground(Void... params) {

                    PdfUA pdfUA = new PdfUA();

                    try {
                        pdfUA.createNormalInvoice(context,SESSION._ORDER_DETAILES,SESSION._ORDERS,false,mainMer);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try
                    {
                        File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                        File file = new File(path,"normalInvoice.pdf");
                        RandomAccessFile f = new RandomAccessFile(file, "r");
                        byte[] data = new byte[(int)f.length()];
                        f.readFully(data);
                        Log.d("bitmapsize",context.toString()+"");

                        pdfLoadImages(data,context);

                    }
                    catch(Exception ignored)
                    {

                    }
                  InvoiceImg invoiceImg = new InvoiceImg(context);
                    byte b = 0;
                    try {
                        Bitmap bitmap =newBitmap;
                        HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);

                    } catch (Exception e) {
                    }
                    return null;
                }
            }.execute();
    }
    public static ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
    static Bitmap newBitmap =null;
    private static void pdfLoadImages(final byte[] data, final Context context)
    {
        bitmapList=new ArrayList<>();
        try
        {
            // run async
            new AsyncTask<Void, Void, String>()
            {
                PrintTools pt=new PrintTools(context);
                // create and show a progress dialog
            //    ProgressDialog progressDialog = ProgressDialog.show(context, "", "Opening...");

                @Override
                protected void onPostExecute(String html)
                {
                    Log.d("bitmapsize2222",bitmapList.size()+"");
                        newBitmap = combineImageIntoOne(bitmapList);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //    pt.PrintReport(newBitmap);
                    //after async close progress dialog
                  //  progressDialog.dismiss();
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

                        return html;
                    }
                    catch (Exception e)
                    {
                        Log.d("error1", e.toString());
                    }
                    return null;
                }
            }.execute();
            System.gc();// run GC
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("error", e.toString());
        }
    }
    private static Bitmap combineImageIntoOne(ArrayList<Bitmap> bitmap) {
        int w = 0, h = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            if (i < bitmap.size() - 1) {
                w = bitmap.get(i).getWidth() > bitmap.get(i + 1).getWidth() ? bitmap.get(i).getWidth() : bitmap.get(i + 1).getWidth();
            }
            h += bitmap.get(i).getHeight();
        }
        Bitmap temp;
        if(w==0||h==0){
            temp=bitmapList.get(0);
        }else {
        temp= Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(temp);
            int top = 0;
            for (int i = 0; i < bitmap.size(); i++) {
                Log.d("HTML", "Combine: "+i+"/"+bitmap.size()+1);

                top = (i == 0 ? 0 : top+bitmap.get(i).getHeight());
                canvas.drawBitmap(bitmap.get(i), 0f, top, null);
            }
        }

        return temp;
    }
    private static void printAndOpenCashBoxSM_S230I(String mainAns, final String mainMer, final String mainCli, final Context context, final Activity activity) {
        if (true) {
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(context.getString(R.string.wait_for_finish_printing));

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    dialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //feed paper

                    dialog.cancel();
                    activity.finish();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    InvoiceImg invoiceImg = new InvoiceImg(context);
                    byte b = 0;
                    try {
                        //// TODO: 13/06/2018 adding pinpad support
                        PdfUA pdfUA = new PdfUA();

                        try {
                            pdfUA.createNormalInvoice(context,SESSION._ORDER_DETAILES,SESSION._ORDERS,false,mainMer);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try
                        {
                            File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                            File file = new File(path,"normalInvoice.pdf");
                            RandomAccessFile f = new RandomAccessFile(file, "r");
                            byte[] data = new byte[(int)f.length()];
                            f.readFully(data);
                            Log.d("bitmapsize",context.toString()+"");

                            pdfLoadImages(data,context);
                        }
                        catch(Exception ignored)
                        {

                        }
                            Bitmap bitmap = invoiceImg.normalInvoice(SESSION._ORDERS.getOrderId(), SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE, null);
                           printSMS230(newBitmap,context);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }

    }
    private static void printSMS230(Bitmap bitmap, Context context) {
        String portSettings = "portable;escpos;l";
        String port = "BT:";
        int paperWidth = 576;
        paperWidth = 832; // 4inch (832 dot)
        paperWidth = 576; // 3inch (576 dot)1
        paperWidth = 384; // 2inch (384 dot)
        MiniPrinterFunctions.PrintBitmapImage(context, port, portSettings, bitmap, paperWidth, true, true);

    }
}


