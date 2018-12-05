package com.pos.leaders.leaderspossystem.Tools;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.IdsCounterDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.PdfUA;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pos.leaders.leaderspossystem.Tools.DocumentControl.pdfLoadImages;


/**
 * Created by KARAM on 19/01/2017.
 */

public class Util {

    private static final String INSTALLATION = "INSTALLATION.rd";
    private static final String LOG_TAG = "Tools_Util";
    public static Locale locale = new Locale("en");


    public synchronized static boolean isFirstLaunch(Context context, boolean CreateFile) {
        String sID = null;
        boolean launchFlag = false;
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists()) {
                    launchFlag = true;
                    if (CreateFile)
                        writeInstallationFile(installation);
                } else
                    sID = readInstallationFile(installation);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return launchFlag;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");// read only mode
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();

        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }


    public static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    public static double convertSign(Double d) {
        return d*-1;
    }



    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static File getStorageDir(String dirName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "LeadPOSBackup/" + dirName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    public static String makePrice(double d) {
        String format = "0.";
        if (SETTINGS.decimalNumbers == 0) {
            format = "0";
        } else {
            for (int i = SETTINGS.decimalNumbers; i > 0; i--) {
                format += "0";
            }
        }
        DecimalFormat form = new DecimalFormat(format,new DecimalFormatSymbols(Locale.ENGLISH));
        return form.format(d);
    }

    public static String spaces(int num) {
        String s = "";
        for (int i = 0; i < num; i++) {
            s += "\u0020";
        }
        return s;
    }

    public static String x12V99(double d) {
        double x = d;
        if (d < 0)
            x *= -1;

        String str = "";
        str += (String.format(locale, "%012d", ((Double) x).intValue()));
        int fl = ((Double) ((x - Math.floor(x) + 0.001) * 100)).intValue();
        str += (String.format(locale, "%02d", fl));
        return str;
    }

    public static String x9V99(double d) {
        double x = d;
        if (d < 0)
            x *= -1;

        String str = "";
        str += (String.format(locale, "%09d", ((Double) x).intValue()));
        int fl = ((Double) ((x - Math.floor(x) + 0.001) * 100)).intValue();
        str += (String.format(locale, "%02d", fl));
        return str;
    }

    public static String _8V99(double d) {
        double x = d;
        if (d < 0)
            x *= -1;

        String str = "";
        str += (String.format(locale, "%08d", ((Double) x).intValue()));
        int fl = ((Double) ((x - Math.floor(x) + 0.001) * 100)).intValue();
        str += (String.format(locale, "%02d", fl));
        return str;
    }

    public static File createFileFromInputStream(InputStream inputStream, String fileName) {

        try {
            File f = new File(fileName);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        } catch (IOException e) {
            //Logging exception
        }

        return null;
    }

    private static boolean checkID(long id) {
        String sID = String.format(new Locale("en"), "%019d", id);
        String idPrefix = sID.substring(0, sID.length() - 16);
        return Long.parseLong(idPrefix) == SESSION.POS_ID_NUMBER;
    }

    private static long getCurrentLastID(SQLiteDatabase db, String tableName, String idField) {
        long i = 0;
        Cursor cursor = db.rawQuery("select  " + tableName + "  from  " + IdsCounterDBAdapter.IDS_COUNTER_TABLE_NAME + ";", null);
        if (cursor.getCount() < 1) // don`t have any sale yet
        {
            cursor.close();
            i = (long) SESSION.POS_ID_NUMBER * SESSION.firstIDOffset;
        } else {
            cursor.moveToFirst();
            i = Long.parseLong(cursor.getString(0));
        }
        ContentValues values = new ContentValues();
        if (i == 0) {
            i = (long) SESSION.POS_ID_NUMBER * SESSION.firstIDOffset;
            values.put(tableName, i);
        } else {
            i = i + 1;
            values.put(tableName, i);
        }
        db.update(IdsCounterDBAdapter.IDS_COUNTER_TABLE_NAME, values, null, null);
        return i;

    }

    public static long idHealth(SQLiteDatabase db, String tableName, String idField) {
        return getCurrentLastID(db, tableName, idField);
    }

    private static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSyncServiceRunning(Context context) {
        return isMyServiceRunning(SyncMessage.class, context);
    }
    public static void killSyncService(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (SyncMessage.class.getName().equals(service.service.getClassName())) {
                context.stopService(new Intent().setComponent(service.service));
            }
        }
    }

    // Methods To Test Input Value Type
    public static final boolean isDouble(String value) {
        if (isInteger(value)) {
            return true;
        }
        Pattern pattern = Pattern.compile("^[+-]?(\\d+)(\\D)(\\d+)");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;

    }

    public static final boolean isFloat(String value) {
        if (isInteger(value)) {
            return true;
        }
        Pattern pattern = Pattern.compile("^[+-]?(\\d+)(\\D)(\\d+)");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;

    }

    public static final boolean isDecimal(String value) {
        Pattern pattern = Pattern.compile("^[+-]?(\\d+)(\\D)(\\d+)");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;

    }

    public static final boolean isBoolean(String value) {

        if (value.equals("1") || value.equals("0")) {
            return true;
        }
        return false;

    }

    public static final boolean isBit(String value) {
        Pattern pattern = Pattern.compile("^(0|1)$");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;

    }

    public static final boolean isInteger(String value) {

        Pattern pattern = Pattern.compile("[+-]?\\b[0-9]+\\b");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;
    }

    public static final boolean isString(String value) {
        Pattern pattern = Pattern.compile("^((?!badword).)*$");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;
    }

    public static final boolean isEmail(String value) {
        Pattern pattern = Pattern.compile("/^\\w+([\\.-]?\\w+)@\\w+([\\.-]?\\w+)(\\.\\w{2,3})+$/");

        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;
    }

    public static final boolean isLong(String value) {
        Pattern pattern = Pattern.compile("^-?\\d{1,19}$");
        Matcher matcher = pattern.matcher(value);
        boolean result = matcher.matches();
        return result;
    }
    public static String getString(String value)  {
        ArrayList<String> test = new ArrayList<String>();
        for(int i= 0 ; i<value.length();i++){
           if (String.valueOf(value.charAt(i)).equals("'")) {
                test.add("\\\\'");
           }
           /*
           if (String.valueOf(value.charAt(i)).equals("י")) {
                test.add("י\\");
           }*/
           else  if(String.valueOf(value.charAt(i)).equals("\"")){
               test.add("\\\"");

           }
           else {
                test.add(String.valueOf(value.charAt(i)));
            }
        }
        String a= "";
        for(int i=0;i<test.size();i++){
            a+=test.get(i);
        }
        return a;
    }





    public static Object cloneObject(Object obj){
        try{
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(field.get(obj) == null || Modifier.isFinal(field.getModifiers())){
                    continue;
                }
                if(field.getType().isPrimitive() || field.getType().equals(String.class)
                        || field.getType().getSuperclass().equals(Number.class)
                        || field.getType().equals(Boolean.class)){
                    field.set(clone, field.get(obj));
                }else{
                    Object childObj = field.get(obj);
                    if(childObj == obj){
                        field.set(clone, clone);
                    }else{
                        field.set(clone, cloneObject(field.get(obj)));
                    }
                }
            }
            return clone;
        }catch(Exception e){
            return null;
        }
    }
    public static  ZReport insertZReport(ZReport zReport,Context context)
    {
        double aReportAmount = 0;
        ZReport lastZReport = getLastZReport(context);

        if (lastZReport == null) {
            lastZReport = new ZReport();
            lastZReport.setEndOrderId(0);
            lastZReport.setzReportId(0);
        }
        OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(context);
        opiningReportDBAdapter.open();
        List<OpiningReport> opiningReportList = opiningReportDBAdapter.getListByLastZReport(lastZReport.getzReportId());
        for (int i=0;i<opiningReportList.size();i++){
            aReportAmount+=opiningReportList.get(i).getAmount();
        }
        double cash_plus = 0, cash_minus = 0;
        double check_plus = 0, check_minus = 0;
        double creditCard_plus = 0, creditCard_minus = 0;
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        OrderDBAdapter orderDBAdapter = new OrderDBAdapter(context);
        orderDBAdapter.open();
        List<Order> sales = orderDBAdapter.getBetween(zReport.getStartOrderId(),zReport.getEndOrderId());
        List<Payment> payments = paymentList(sales,context);
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
        }
        long zID = zReportDBAdapter.insertEntry(zReport.getCreatedAt(), zReport.getByUser(), zReport.getStartOrderId(), zReport.getEndOrderId(),
                zReport.getTotalAmount()+aReportAmount+(zReport.getTotalAmount()*SETTINGS.tax/100),zReport.getTotalAmount(),cash_plus,check_plus,creditCard_plus,zReport.getTotalPosSales(),zReport.getTotalAmount()*SETTINGS.tax/100,aReportAmount);
        zReport.setzReportId(zID);
        return zReport;
    }
    public static List<Payment> paymentList(List<Order> sales,Context context) {
        List<Payment> pl = new ArrayList<Payment>();
        PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(context);
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        CurrencyReturnsDBAdapter currencyReturnsDBAdapter = new CurrencyReturnsDBAdapter(context);
        paymentDBAdapter.open();
        for (Order s : sales) {
            List<Payment> payments = paymentDBAdapter.getPaymentBySaleID(s.getOrderId());
            /**
             if (SETTINGS.enableCurrencies) {
             for (Payment _p : payments) {
             _p.setCashPayments(cashPaymentDBAdapter.getPaymentBySaleID(_p.getOrderId()));
             _p.setCurrencyReturns(currencyReturnsDBAdapter.getCurencyReturnBySaleID(_p.getOrderId()));
             }
             }**/

            pl.addAll(payments);
        }
        paymentDBAdapter.close();
        return pl;
    }
    public static ZReport getLastZReport(Context c) {
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(c);

        zReportDBAdapter.open();
        ZReport zReport = null;
        try {
            zReport = zReportDBAdapter.getLastRow();
        } catch (Exception e) {
            Log.w("Z Report ", e.getMessage());
        }
        zReportDBAdapter.close();
        return zReport;
    }
    public static OpiningReport getLastAReport(Context context) {
        OpiningReportDBAdapter aReportDBAdapter = new OpiningReportDBAdapter(context);
        aReportDBAdapter.open();
        OpiningReport aReport = null;

        try {
            aReport = aReportDBAdapter.getLastRow();

        } catch (Exception e) {
            Log.w("A Report ", e.getMessage());
        }

        aReportDBAdapter.close();
        return aReport;
    }

    public static void sendClosingReport(final Context context, final String res){
        final String SAMPLE_FILE = "closingreport.pdf";

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
                        PdfUA pdfUA = new PdfUA();

                        try {
                            pdfUA.printClosingReport(context,res);
                        } catch (DocumentException e) {
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

    }/**

     }else if(paymentWays.equalsIgnoreCase(CONSTANT.CREDIT_CARD)){
     long paymentID = paymentDBAdapter.receiptInsertEntry(CREDIT_CARD,Double.parseDouble(String.valueOf(invoice.getDocumentsData().getDouble("total"))), Long.parseLong(invoiceOrderIdsList.get(0).toString()));

     }

     final Payment payment = paymentDBAdapter.getPaymentByID( Long.parseLong(invoiceOrderIds.get(0).toString()));
     final JSONObject newJsonObject = new JSONObject(payment.toString());
     String paymentWay = newJsonObject.getString("paymentWay");
     long orderId = newJsonObject.getLong("orderId");
     List<CashPayment> cashPaymentList = new ArrayList<CashPayment>();
     List<Payment> paymentList = new ArrayList<Payment>();
     List<CreditCardPayment> creditCardPaymentList = new ArrayList<CreditCardPayment>();
     List<Check> checkList = new ArrayList<Check>();
     if(paymentWay.equalsIgnoreCase(CONSTANT.CASH)){
     //get cash payment detail by order id
     CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
     cashPaymentDBAdapter.open();
     cashPaymentDBAdapter.insertEntry(Long.parseLong(invoiceOrderIds.get(0).toString()), Double.parseDouble(String.valueOf(invoice.getDocumentsData().getDouble("total"))), 0, new Timestamp(System.currentTimeMillis()),1);
     cashPaymentList = cashPaymentDBAdapter.getPaymentBySaleID(orderId);
     JSONArray jsonArray = new JSONArray(cashPaymentList.toString());
     newJsonObject.put("paymentDetails",jsonArray);
     }
     if(paymentWay.equalsIgnoreCase(CONSTANT.CREDIT_CARD)){
     //get credit payment detail by order id
     CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(context);
     creditCardPaymentDBAdapter.open();

     creditCardPaymentList = creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
     JSONArray jsonArray = new JSONArray(creditCardPaymentList.toString());
     newJsonObject.put("paymentDetails",jsonArray);
     }
     if(paymentWay.equalsIgnoreCase(CONSTANT.CHECKS)){

     //get check payment detail by order id
     ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(context);
     checksDBAdapter.open();
     for (Check check : SESSION._CHECKS_HOLDER) {
     checksDBAdapter.insertEntry(check.getCheckNum(), check.getBankNum(), check.getBranchNum(), check.getAccountNum(), check.getAmount(), check.getCreatedAt(), Long.parseLong(invoiceOrderIdsList.get(0)));
     }
     SESSION._CHECKS_HOLDER = null;
     checkList = checksDBAdapter.getPaymentBySaleID(orderId);
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
    Log.i("Payment", newJsonObject.toString());
    String payRes=transmit.authPost(ApiURL.Payment, newJsonObject.toString(), SESSION.token);
    Log.i("Payment log", payRes);
    ReceiptDocuments documents = new ReceiptDocuments("Receipt",new Timestamp(System.currentTimeMillis()), invoiceIdsList,Double.parseDouble(String.valueOf(invoice.getDocumentsData().getDouble("total"))),"ILS");
    String doc = mapper.writeValueAsString(documents);
    JSONObject docJson= new JSONObject(doc);
    String type = docJson.getString("type");
    docJson.remove("type");
    docJson.put("@type",type);
    docJson.put("customer",customerJson);
    Log.d("Document vale", docJson.toString());
    com.pos.leaders.leaderspossystem.Models.Invoice invoiceA = new Invoice(DocumentType.RECEIPT,docJson,docNum);
    Log.d("Receipt log",invoiceA.toString());
    String res=transmit.authPost(ApiURL.Documents,invoiceA.toString(), SESSION.token);
    JSONObject jsonObject = new JSONObject(res);
    String msgData = jsonObject.getString(MessageKey.responseBody);
    Log.d("receiptResult",res);
    Invoice invoice1 = newInvoice;
    JSONObject updataInvoice =invoice1.getDocumentsData();
    double total= updataInvoice.getDouble("total");
    Log.d("totalPaid",total+"");
    updataInvoice.remove("totalPaid");
    updataInvoice.put("totalPaid",total);
    updataInvoice.remove("invoiceStatus");
    updataInvoice.put("invoiceStatus", InvoiceStatus.PAID);
    invoice1.setDocumentsData(updataInvoice);
    Log.d("invoiceRes1232",invoice1.toString());

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
     }
     **/

}
