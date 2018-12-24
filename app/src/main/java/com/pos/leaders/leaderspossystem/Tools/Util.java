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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosInvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.DocumentType;
import com.pos.leaders.leaderspossystem.Models.InvoiceStatus;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.PosInvoice;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.PdfUA;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import org.json.JSONException;
import org.json.JSONObject;

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

import POSSDK.POSSDK;

import static com.pos.leaders.leaderspossystem.Tools.DocumentControl.pdfLoadImages;
import static com.pos.leaders.leaderspossystem.Tools.DocumentControl.pdfLoadImagesOpiningReport;


/**
 * Created by KARAM on 19/01/2017.
 */

public class Util {
    static POSSDK pos;
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
        double invoiceAmount=0;
        double creditInvoiceAmount=0;
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
        if(zReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoiceList(-1, InvoiceStatus.UNPAID.getValue());
            for (int i= 0 ;i<posInvoiceList.size();i++){
                invoiceAmount+=posInvoiceList.get(i).getAmount();
            }
            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.CREDIT_INVOICE.getValue());
            for (int i= 0 ;i<posCreditInvoiceList.size();i++){
                creditInvoiceAmount+=posCreditInvoiceList.get(i).getAmount();
            }
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
            for (int i= 0 ;i<posInvoiceList.size();i++){
                invoiceAmount+=posInvoiceList.get(i).getAmount();
            }
            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.CREDIT_INVOICE.getValue());
            for (int i= 0 ;i<posCreditInvoiceList.size();i++){
                creditInvoiceAmount+=posCreditInvoiceList.get(i).getAmount();
            }
        }
        zReport.setTotalSales(zReport.getTotalSales()+invoiceAmount+creditInvoiceAmount);
        zReport.setTotalPosSales(zReport.getTotalPosSales()+invoiceAmount+creditInvoiceAmount);
        long zID = zReportDBAdapter.insertEntry(zReport.getCreatedAt(), zReport.getByUser(), zReport.getStartOrderId(), zReport.getEndOrderId(),
                zReport.getTotalAmount()+aReportAmount+(zReport.getTotalAmount()*SETTINGS.tax/100),zReport.getTotalAmount(),cash_plus,check_plus,creditCard_plus,zReport.getTotalPosSales(),zReport.getTotalAmount()*SETTINGS.tax/100,aReportAmount,invoiceAmount,creditInvoiceAmount);
        zReport.setzReportId(zID);
        zReport.setInvoiceAmount(invoiceAmount);
        zReport.setCreditInvoiceAmount(creditInvoiceAmount);

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
            protected void onPostExecute(Void aVoid) {

                try
                {
                    File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                    File file = new File(path,SAMPLE_FILE);
                    RandomAccessFile f = new RandomAccessFile(file, "r");
                    byte[] data = new byte[(int)f.length()];
                    f.readFully(data);
                    pdfLoadImages(data,context);

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

    }

    public static void opiningReport(final Context context, final OpiningReport res){
        final String SAMPLE_FILE = "opiningreport.pdf";
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPostExecute(Void aVoid) {

                try
                {
                    File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                    File file = new File(path,SAMPLE_FILE);
                    RandomAccessFile f = new RandomAccessFile(file, "r");
                    byte[] data = new byte[(int)f.length()];
                    f.readFully(data);
                    pdfLoadImagesOpiningReport(data,context);

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
                        pdfUA.printOpiningReport(context,res);
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

    }
    public static void logInLogOutReport(final Context context, final JSONObject res){
        final String SAMPLE_FILE = "loginreport.pdf";
        new AsyncTask<Void, Void, Void>(){
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
                        pdfUA.printLogInLogOutUserReport(context,res);
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

    }
}
