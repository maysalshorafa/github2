package com.pos.leaders.leaderspossystem.Tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DocumentType;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.InvoiceStatus;
import com.pos.leaders.leaderspossystem.Models.OrderDocumentStatus;
import com.pos.leaders.leaderspossystem.Models.OrderDocuments;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.ReceiptDocuments;
import com.pos.leaders.leaderspossystem.PdfUA;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
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

/**
 * Created by Win8.1 on 10/8/2018.
 */

public class DocumentControl {
    public static Locale locale = new Locale("en");
    static String invoiceNum;
    static double  customerGeneralLedger=0.0;
    static  InvoiceImg invoiceImg;
    static boolean success=false;
    public static boolean sendOrderDocument(final Context context){
        invoiceImg = new InvoiceImg(context);
        ObjectMapper mapper = new ObjectMapper();
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                print(invoiceImg.OrderDocument( SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE,invoiceNum),context);
                success= true;
                // SalesCartActivity.clearCart();

            }
            @Override
            protected Void doInBackground(Void... voids) {
                MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                JSONObject customerData = new JSONObject();
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    customerData.put("customerId", SESSION._ORDERS.getCustomer().getCustomerId());
                    Log.d("customer",customerData.toString());
                    String docOrder = mapper.writeValueAsString(SESSION._ORDERS);
                    JSONObject orderJson = new JSONObject(docOrder);
                    String docOrderDetails = mapper.writeValueAsString(SESSION._ORDER_DETAILES);
                    JSONArray orderDetailsJson = new JSONArray(docOrderDetails);
                    Log.d("OrderDetailsJson",orderDetailsJson.toString());
                    orderJson.put("orderDetails",orderDetailsJson);
                    Log.d("orderJson",orderJson.toString());
                    OrderDocuments documents = new OrderDocuments("OrderDocument",new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),SESSION._ORDERS.getTotalPrice(),"test","ILS", OrderDocumentStatus.READY);
                    String doc = mapper.writeValueAsString(documents);
                    Log.d("doc",doc.toString());

                    JSONObject docJson= new JSONObject(doc);
                    String type = docJson.getString("type");
                    docJson.remove("type");
                    docJson.put("@type",type);
                    docJson.put("customer",customerData);
                    docJson.put("order",orderJson);
                    Log.d("Document vale", docJson.toString());
                    BoInvoice invoice = new BoInvoice(DocumentType.ORDER_DOCUMENT,docJson,"");
                    Log.d("Invoice log",invoice.toString());
                    String res=transmit.authPost(ApiURL.Documents,invoice.toString(), SESSION.token);
                    JSONObject jsonObject = new JSONObject(res);
                    String msgData = jsonObject.getString(MessageKey.responseBody);
                    JSONObject msgDataJson = new JSONObject(msgData);
                    JSONObject jsonObject1=msgDataJson.getJSONObject("documentsData");
                    invoiceNum = msgDataJson.getString("docNum");
                    Log.d("Invoice log res", res+"");
                    Log.d("Invoice Num", invoiceNum);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
        return success;
    }

    private static void print(Bitmap bitmap, Context context) {
        PrintTools printTools = new PrintTools(context);
        printTools.PrintReport(bitmap);
    }
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
    public static void print(Context context, ArrayList<Bitmap> bitmapList){
        PrintTools pt=new PrintTools(context);
        for (int i= 1;i<bitmapList.size(); i++) {
            Log.d("bitmapsize",bitmapList.size()+"");
            pt.PrintReport(bitmapList.get(i));

        }

    }
    public static void sendDoc(final Context context, final BoInvoice invoice, String paymentWays){
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
            Payment payment =new Payment(0,CASH,Double.parseDouble(String.valueOf(invoice.getDocumentsData().getDouble("total"))),0);
            final JSONObject newJsonObject = new JSONObject(payment.toString());
            if(paymentWays.equalsIgnoreCase(CONSTANT.CASH)){
                CashPayment cashPayment = new CashPayment(0,0, Double.parseDouble(String.valueOf(invoice.getDocumentsData().getDouble("total"))), 0, new Timestamp(System.currentTimeMillis()));
                cashPaymentList.add(cashPayment);
                JSONArray jsonArray = new JSONArray(cashPaymentList.toString());
                newJsonObject.put("paymentDetails",jsonArray);
            }
          /*  if(paymentWay.equalsIgnoreCase(CONSTANT.CREDIT_CARD)){
                CreditCardPayment creditCardPayment
                //get credit payment detail by order id
                CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(context);
                creditCardPaymentDBAdapter.open();
                creditCardPaymentList = creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
                JSONArray jsonArray = new JSONArray(creditCardPaymentList.toString());
                newJsonObject.put("paymentDetails",jsonArray);
            }**/
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
                    //     print(invoiceImg.Invoice( SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE,invoiceNum));

                    //clearCart();

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
                        JSONObject jsonObject = new JSONObject(res);
                        String msgData = jsonObject.getString(MessageKey.responseBody);
                        Log.d("receiptResult",res);
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



    }}
