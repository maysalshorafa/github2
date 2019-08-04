package com.pos.leaders.leaderspossystem.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosInvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.DocumentType;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.InvoiceStatus;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.PosInvoice;
import com.pos.leaders.leaderspossystem.Models.ReceiptDocuments;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.PdfUA;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.pos.leaders.leaderspossystem.Tools.CONSTANT.CASH;
import static com.pos.leaders.leaderspossystem.Tools.CONSTANT.CHECKS;

/**
 * Created by Win8.1 on 10/8/2018.
 */

public class DocumentControl {
    public static Locale locale = new Locale("en");
    static String invoiceNum;
    static JSONObject receiptJsonObject = new JSONObject();
    static Bitmap newBitmap =null;

    public static void pdfLoadImages(final byte[] data, final Context context) {

        final ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
        try {
            // run async
            new AsyncTask<Void, Void, String>() {
                Bitmap page;
                Context a =context;

                // create and show a progress dialog

                ProgressDialog progressDialog = ProgressDialog.show(a, "", "Opening...");

                @Override
                protected void onPostExecute(String html) {
                  print(context,bitmapList);
                    //after async close progress dialog
                    progressDialog.dismiss();
                    ((Activity)context).finish();
                    //load the html in the webview
                    //	wv1.loadDataWithBaseURL("", html, "randompdf/html", "UTF-8", "");
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
                        final float scale = 800 / PDFpage.getWidth() * 0.80f;
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
                        for (int i = 0; i <= pdf.getNumPages(); i++) {
                            PDFpage = pdf.getPage(i, true);
                            page = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
                            page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            bitmapList.add(page);
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
            e.printStackTrace();
        }
    }

    public static void pdfLoadImagesOpiningReport(final byte[] data, final Context context) {

        final ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
        newBitmap=null;
        try {
            // run async
            new AsyncTask<Void, Void, String>() {
                Bitmap page;
                Context a =context;

                // create and show a progress dialog

                ProgressDialog progressDialog = ProgressDialog.show(a, "", "Opening...");

                @Override
                protected void onPostExecute(String html) {

                   PrintTools pt=new PrintTools(context);
                    if(bitmapList.size()>=3){

                    newBitmap= combineImageIntoOne(bitmapList);
                        pt.PrintReport(newBitmap);

                    }else {
                        print(context,bitmapList);
                    }

                    //after async close progress dialog
                    progressDialog.dismiss();
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
                        final float scale = 800 / PDFpage.getWidth() * 0.80f;
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
                        for (int i = 0; i <= pdf.getNumPages(); i++) {
                            PDFpage = pdf.getPage(i, true);
                            page = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
                            page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            bitmapList.add(page);
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
            e.printStackTrace();
        }
    }
    static int r =0;
    public static int pdfLoadImagesClosingReport(final byte[] data, final Context context) {

        final ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
        try {
            // run async
            new AsyncTask<Void, Void, String>() {
                Bitmap page;
                Context a =context;

                // create and show a progress dialog

                ProgressDialog progressDialog = ProgressDialog.show(a, "", "Opening...");

                @Override
                protected void onPostExecute(String html) {
                    for(int i=1;i<bitmapList.size();i++){
                        print(context,bitmapList);
                    }
                    //   print(context,bitmapList);
                    //after async close progress dialog
                    progressDialog.dismiss();
                    //load the html in the webview
                    //	wv1.loadDataWithBaseURL("", html, "randompdf/html", "UTF-8", "");
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
                        final float scale = 800 / PDFpage.getWidth() * 0.80f;
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
                        for (int i = 0; i <= pdf.getNumPages(); i++) {
                            PDFpage = pdf.getPage(i, true);
                            page = PDFpage.getImage((int) (PDFpage.getWidth() * scale), (int) (PDFpage.getHeight() * scale), null, true, true);
                            page.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            bitmapList.add(page);
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
            e.printStackTrace();
        }
   return r; }


    public static void print(Context context, ArrayList<Bitmap> bitmapList){
        PrintTools pt=new PrintTools(context);
        for (int i= 1;i<bitmapList.size(); i++) {
            Log.d("bitmapsize",bitmapList.size()+"");
            pt.PrintReport(bitmapList.get(i));

        }

    }

    public static void sendDoc(final Context context, final BoInvoice invoice, final String paymentWays, final double totalPaid){
        List<CashPayment> cashPaymentList = new ArrayList<CashPayment>();
        List<Payment> paymentList = new ArrayList<Payment>();
        List<CreditCardPayment> creditCardPaymentList = new ArrayList<CreditCardPayment>();
        List<Check> checkList = new ArrayList<Check>();
        final String SAMPLE_FILE = "receipt.pdf";
        final BoInvoice newInvoice;
        try {
            JSONObject jsonObject = new JSONObject(invoice.toString());
            JSONObject docDataJson = jsonObject.getJSONObject("documentsData");
            final ArrayList<String> invoiceIdsList = new ArrayList<String>();
            final JSONObject customerJson =docDataJson.getJSONObject("customer");
            final String docNum = invoice.getDocNum();
            invoiceIdsList.add(docNum);
            PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(context);
            paymentDBAdapter.open();
            JSONObject DocData = invoice.getDocumentsData();
            if(DocData.has("type")){
                DocData.remove("type");
                DocData.put("@type","Invoice");
            }
            newInvoice=new BoInvoice(DocumentType.INVOICE,DocData,invoice.getDocNum());
            Payment payment=null;
            if(paymentWays.equals(CASH)) {
                 payment = new Payment(0,  totalPaid, 0);
            }else {
                 payment = new Payment(0, totalPaid, 0);
            }
            final JSONObject newJsonObject = new JSONObject(payment.toString());
            if(paymentWays.equalsIgnoreCase(CONSTANT.CASH)){
                CashPayment cashPayment = new CashPayment(0,0,totalPaid, 0, new Timestamp(System.currentTimeMillis()),1,1);
                cashPaymentList.add(cashPayment);
                JSONArray jsonArray = new JSONArray(cashPaymentList.toString());
                newJsonObject.put("paymentDetails",jsonArray);
            }
           if(paymentWays.equalsIgnoreCase(CONSTANT.CREDIT_CARD)){
                JSONArray jsonArray = new JSONArray(SESSION._TEMP_CREDITCARD_PAYMNET.toString());
                newJsonObject.put("paymentDetails",jsonArray);
            }
            if(paymentWays.equalsIgnoreCase(CONSTANT.CHECKS)){

                //get check payment detail by order id
                ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(context);
                checksDBAdapter.open();
                for (Check check : SESSION._CHECKS_HOLDER) {
                    checkList.add(check);
                }
                SESSION._CHECKS_HOLDER = null;
                JSONArray jsonArray = new JSONArray(checkList.toString());
                newJsonObject.put("paymentDetails",jsonArray);
            }

            new AsyncTask<Void, Void, Void>(){
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    try {
                        if(receiptJsonObject.get("status").equals("200")){

                        try
                        {
                            File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                            File file = new File(path,SAMPLE_FILE);
                            RandomAccessFile f = new RandomAccessFile(file, "r");
                            byte[] data = new byte[(int)f.length()];
                            f.readFully(data);
                            pdfLoadImages(data,context);
                            //pdfLoadImages1(data);
                        }
                        catch(Exception ignored)
                        {

                        }
                        }else {
                            new android.support.v7.app.AlertDialog.Builder(context)
                                    .setTitle(context.getString(R.string.invoice))
                                    .setMessage(context.getString(R.string.cant_make_invoice_check_internet_connection))
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                protected Void doInBackground(Void... voids) {
                    MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        ReceiptDocuments documents = new ReceiptDocuments("Receipt",new Timestamp(System.currentTimeMillis()), invoiceIdsList,Double.parseDouble(String.valueOf(invoice.getDocumentsData().getDouble("total"))),"ILS");
                        String doc = mapper.writeValueAsString(documents);
                        JSONObject docJson= new JSONObject(doc);
                        JSONArray paymentJsonArray = new JSONArray();
                        paymentJsonArray.put(newJsonObject);
                        String type = docJson.getString("type");
                        docJson.remove("type");
                        docJson.put("@type",type);
                        docJson.put("customer",customerJson);
                        docJson.put("payments",paymentJsonArray);
                        Log.d("Document vale", docJson.toString());
                        BoInvoice invoiceA = new BoInvoice(DocumentType.RECEIPT,docJson,docNum);
                        Log.d("Receipt log",invoiceA.toString());
                        String res=transmit.authPost(ApiURL.Documents,invoiceA.toString(), SESSION.token);
                         receiptJsonObject = new JSONObject(res);
                        String msgData = receiptJsonObject.getString(MessageKey.responseBody);

                        Log.d("receiptResult",res);
                        if(receiptJsonObject.get("status").equals("200")){
                            ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(context);
                            zReportDBAdapter.open();
                            ZReport zReport =null;
                            try {
                                zReport = zReportDBAdapter.getLastRow();
                                PosInvoiceDBAdapter posInvoiceDBAdapter = new PosInvoiceDBAdapter(context);
                                posInvoiceDBAdapter.open();
                                if(paymentWays.equals(CASH)) {
                                    posInvoiceDBAdapter.insertEntry(totalPaid, zReport.getzReportId(), DocumentType.RECEIPT.getValue(), "", invoiceNum,CONSTANT.CASH);
                                }else {
                                    posInvoiceDBAdapter.insertEntry(totalPaid, zReport.getzReportId(), DocumentType.RECEIPT.getValue(), "", invoiceNum,CONSTANT.CHECKS);
                                }
                            } catch (Exception e) {
                                PosInvoiceDBAdapter posInvoiceDBAdapter = new PosInvoiceDBAdapter(context);
                                posInvoiceDBAdapter.open();
                                if(paymentWays.equals(CASH)) {
                                    posInvoiceDBAdapter.insertEntry(totalPaid,-1,DocumentType.RECEIPT.getValue(),"",invoiceNum,CONSTANT.CASH);
                                }else {
                                    posInvoiceDBAdapter.insertEntry(totalPaid,-1,DocumentType.RECEIPT.getValue(),"",invoiceNum,CONSTANT.CHECKS);

                                }
                                e.printStackTrace();
                            }

                            BoInvoice invoice1 = newInvoice;
                            JSONObject updataInvoice =invoice1.getDocumentsData();
                            double total= updataInvoice.getDouble("total");
                            Log.d("totalPaid",total+"");
                            updataInvoice.remove("totalPaid");
                            updataInvoice.remove("balanceDue");
                            updataInvoice.put("totalPaid",total);
                            updataInvoice.put("balanceDue",0);
                            updataInvoice.remove("invoiceStatus");
                            updataInvoice.put("invoiceStatus", InvoiceStatus.PAID);
                            invoice1.setDocumentsData(updataInvoice);
                            Log.d("invoiceRes",invoice1.toString());

                            String upDataInvoiceRes=transmit.authPutInvoice(ApiURL.Documents,invoice1.toString(), SESSION.token,docNum);
                            Log.d("invoiceRes",upDataInvoiceRes);
                            JSONObject upDateInvoice = new JSONObject(upDataInvoiceRes);
                            if(upDateInvoice.get("status").equals("200")){
                                PosInvoiceDBAdapter posInvoiceDBAdapter = new PosInvoiceDBAdapter(context);
                                posInvoiceDBAdapter.open();
                                PosInvoice posInvoice = posInvoiceDBAdapter.getPodInvoiceByBoId(docNum);
                                if(posInvoice!=null){
                                posInvoice.setStatus(InvoiceStatus.PAID.getValue());
                                posInvoiceDBAdapter.updateEntry(posInvoice);
                            }}
                            String response = upDateInvoice.getString(MessageKey.responseBody);
                            PdfUA pdfUA = new PdfUA();

                            try {
                                pdfUA.printReceiptReport(context,msgData);
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } catch (JSONException e) {
            e.printStackTrace();
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

       Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        for (int i = 1; i <= bitmap.size()-1; i++) {
            Log.d("HTML", "Combine: "+i+"/"+bitmap.size()+1);

            top = top+bitmap.get(i).getHeight();
            canvas.drawBitmap(bitmap.get(i), 0f, top, null);
        }
        return temp;
    }}