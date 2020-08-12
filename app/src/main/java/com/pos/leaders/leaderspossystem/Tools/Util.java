package com.pos.leaders.leaderspossystem.Tools;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.IdsCounterDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.InventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosInvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosSettingDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.XReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.DocumentType;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.CustomerAssistant;
import com.pos.leaders.leaderspossystem.Models.DepositAndPullReport;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.PosInvoice;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.XReport;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.PdfUA;
import com.pos.leaders.leaderspossystem.Printer.HPRT_TP805;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SetUpManagement;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.Service.SyncMessage;

import org.json.JSONArray;
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
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import HPRTAndroidSDK.HPRTPrinterHelper;
import POSAPI.POSInterfaceAPI;
import POSAPI.POSUSBAPI;
import POSSDK.POSSDK;

import static android.content.Context.MODE_PRIVATE;
import static com.pos.leaders.leaderspossystem.Tools.DocumentControl.pdfLoadImages;
import static com.pos.leaders.leaderspossystem.Tools.DocumentControl.pdfLoadImagesClosingReport;
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
    public static  ZReport insertZReport(Context context)
    {
        double totalZReportAmount=0;
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        ZReport lastZReport = Util.getLastZReport(context);
        if (lastZReport == null) {
            lastZReport = new ZReport();
            lastZReport.setEndOrderId(0);
        }else {
            OrderDBAdapter orderDBAdapter = new OrderDBAdapter(context);
            orderDBAdapter.open();
            Order order = orderDBAdapter.getLast();
            lastZReport.setEndOrderId(order.getOrderId());
        }
        double amount = zReportDBAdapter.getZReportAmount(lastZReport.getStartOrderId(), lastZReport.getEndOrderId());

        Log.d("amount",amount+"amount");
        try {
            ZReport zReport1 = zReportDBAdapter.getLastRow();
            ZReport zReport =zReportDBAdapter.getByID(zReport1.getzReportId()-1);
            if(zReport!=null) {
                totalZReportAmount = zReport.getTotalPosSales() + amount+lastZReport.getInvoiceAmount()+lastZReport.getCreditInvoiceAmount();
            }else {
                totalZReportAmount=zReport.getInvoiceReceiptAmount();
            }
        } catch (Exception e) {
            totalZReportAmount=amount;
            e.printStackTrace();
        }
        //sales Amount
        lastZReport.setTotalAmount(Double.parseDouble(Util.makePrice(amount)));
        lastZReport.setInvoiceReceiptAmount(Double.parseDouble(Util.makePrice(amount)));
        lastZReport.setTotalSales(Double.parseDouble(Util.makePrice(amount+lastZReport.getInvoiceAmount()+lastZReport.getCreditInvoiceAmount())));
        lastZReport.setTotalPosSales(Double.parseDouble(Util.makePrice(totalZReportAmount)));
        Log.d("getTotalPosuTITL1",lastZReport.getTotalPosSales()+"poss");
        lastZReport.setCloseOpenReport("close");
        zReportDBAdapter.updateEntry(lastZReport);
        zReportDBAdapter.close();

       /* Log.d("ZREport",zReport.toString());

        double aReportAmount = 0;
        double invoiceAmount=0;
        double creditInvoiceAmount=0;
        double receiptInvoiceAmount=0;
        double pullReportAmount=0;
        double depositReportAmount=0;
        double receiptInvoiceAmountCheck=0;
        double sheqle_plus = 0, sheqle_minus = 0;
        double usd_plus = 0, usd_minus = 0;
        double eur_plus = 0, eur_minus = 0;
        double gbp_plus = 0, gbp_minus = 0;
        double aReportDetailsForFirstCurrency=0;
        double aReportDetailsForSecondCurrency=0;
        double aReportDetailsForThirdCurrency=0;
        double aReportDetailsForForthCurrency=0;
        double cash_plus = 0, cash_minus = 0;
        double check_plus = 0, check_minus = 0;
        double creditCard_plus = 0, creditCard_minus = 0;
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
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        OrderDBAdapter orderDBAdapter = new OrderDBAdapter(context);
        orderDBAdapter.open();
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();
        List<Order> sales = orderDBAdapter.getBetween(zReport.getStartOrderId(),zReport.getEndOrderId());
        List<CurrencyReturns> currencyReturnList = returnPaymentList(sales,context);
        List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(sales,context);
        List<Payment> payments = paymentList(sales,context);
        for (Payment p : payments) {

            long orderId = p.getOrderId();
            List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(orderId);
            if (p.getAmount()<0){
                cash_minus+=p.getAmount();
            }
            for(int i=0;i<cashPaymentList.size();i++){
                cash_plus+=cashPaymentList.get(i).getAmount();
            }
            List<Check>checkList=checksDBAdapter.getPaymentBySaleID(orderId);
            for(int i=0;i<checkList.size();i++){
                check_plus+=checkList.get(i).getAmount();
            }
            List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
            for(int i=0;i<creditCardPayments.size();i++){
                creditCard_plus+=creditCardPayments.get(i).getAmount();
            }
            /**switch (p.getPaymentWay()) {

                case CONSTANT.CASH:
                        cash_plus += p.getAmount();
                    break;
                case CONSTANT.CREDIT_CARD:
                        creditCard_plus += p.getAmount();
                    break;
                case CONSTANT.CHECKS:
                        check_plus += p.getAmount();
                    break;
            }
        }

//with Currency

        if (SETTINGS.enableCurrencies) {
            for (CurrencyOperation cp : currencyOperationList) {
                switch (cp.getCurrencyType()) {

                    case "ILS":
                            sheqle_plus += cp.getAmount();
                        break;
                    case "USD":
                            usd_plus += cp.getAmount();
                        break;
                    case "EUR":
                            eur_plus += cp.getAmount();

                        break;
                    case "GBP":
                            gbp_plus += cp.getAmount();
                        break;
                }
            }


            for (CurrencyReturns cp : currencyReturnList) {
                switch ((int) cp.getCurrency_type()) {
                    case CONSTANT.Shekel:
                            sheqle_minus += cp.getAmount();
                        break;
                    case CONSTANT.USD:
                            usd_minus += cp.getAmount();
                        break;
                    case CONSTANT.EUR:
                        if (cp.getAmount() > 0)
                            eur_minus += cp.getAmount();

                        break;
                    case CONSTANT.GBP:
                            gbp_minus += cp.getAmount();
                        break;
                }
            }

        }else {
            sheqle_plus+=cash_plus;
        }
        if(zReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
            for (int i= 0 ;i<posReceiptList.size();i++){
                receiptInvoiceAmount+=posReceiptList.get(i).getAmount();
            }
            DrawerDepositAndPullReportDbAdapter drawerDepositAndPullReportDbAdapter=new DrawerDepositAndPullReportDbAdapter(context);
            drawerDepositAndPullReportDbAdapter.open();
            List<DepositAndPullReport>depositAndPullReportList=drawerDepositAndPullReportDbAdapter.getListByLastZReport(-1);
            for(int i=0;i<depositAndPullReportList.size();i++){
                if(depositAndPullReportList.get(i).getType().equals("Pull")){
                pullReportAmount+=depositAndPullReportList.get(i).getAmount();
            }
            else {
                    depositReportAmount+=depositAndPullReportList.get(i).getAmount();

                }
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
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
            for (int i= 0 ;i<posReceiptList.size();i++){
                receiptInvoiceAmount+=posReceiptList.get(i).getAmount();
            }
            DrawerDepositAndPullReportDbAdapter drawerDepositAndPullReportDbAdapter=new DrawerDepositAndPullReportDbAdapter(context);
            drawerDepositAndPullReportDbAdapter.open();
            List<DepositAndPullReport>depositAndPullReportList=drawerDepositAndPullReportDbAdapter.getListByLastZReport(zReport1.getzReportId());
            for(int i=0;i<depositAndPullReportList.size();i++){
                if(depositAndPullReportList.get(i).getType().equals("Pull")){
                    pullReportAmount+=depositAndPullReportList.get(i).getAmount();
                }
                else {
                    depositReportAmount+=depositAndPullReportList.get(i).getAmount();

                }
            }
        }

        if(zReportDBAdapter.getProfilesCount()==0){
            PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
            posInvoiceDBAdapter.open();
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoice(-1);
            for (int i= 0 ;i<posInvoiceList.size();i++){
                invoiceAmount+=posInvoiceList.get(i).getAmount();
            }
            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
            for (int i= 0 ;i<posCreditInvoiceList.size();i++){
                creditInvoiceAmount+=posCreditInvoiceList.get(i).getAmount();
            }
            List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
            for (int i= 0 ;i<posReceiptList.size();i++){
                receiptInvoiceAmountCheck+=posReceiptList.get(i).getAmount();
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
            List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoice(zReport1.getzReportId());
            for (int i= 0 ;i<posInvoiceList.size();i++){
                invoiceAmount+=posInvoiceList.get(i).getAmount();
            }
            List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
            for (int i= 0 ;i<posCreditInvoiceList.size();i++){
                creditInvoiceAmount+=posCreditInvoiceList.get(i).getAmount();
            }
            List<PosInvoice>posReceiptListCheck = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
            for (int i= 0 ;i<posReceiptListCheck.size();i++){
                receiptInvoiceAmountCheck+=posReceiptListCheck.get(i).getAmount();
            }

        }
        zReport.setTotalPosSales(zReport.getTotalPosSales()+invoiceAmount);
        zReport.setTotalAmount(zReport.getTotalAmount()+aReportAmount+receiptInvoiceAmount);
        zReport.setTotalSales(zReport.getTotalSales()+invoiceAmount+creditInvoiceAmount);
        if(SETTINGS.enableCurrencies){
            sheqle_plus=(sheqle_plus-sheqle_minus)+aReportDetailsForFirstCurrency+receiptInvoiceAmount;
            sheqle_plus+=depositReportAmount-pullReportAmount;
        }else {
            sheqle_plus=(sheqle_plus-sheqle_minus)+aReportAmount+receiptInvoiceAmount;
            sheqle_plus+=depositReportAmount-pullReportAmount;

        }
        long zID = zReportDBAdapter.insertEntry(zReport.getCreatedAt(), zReport.getByUser(), zReport.getStartOrderId(), zReport.getEndOrderId(),
                zReport.getTotalAmount(),zReport.getTotalSales(),cash_plus,check_plus+receiptInvoiceAmountCheck,creditCard_plus
                ,Double.parseDouble(Util.makePrice(zReport.getTotalPosSales())),(zReport.getTotalAmount()/(1+SETTINGS.tax/100))*(SETTINGS.tax/100),invoiceAmount,
                creditInvoiceAmount,sheqle_plus,(usd_plus-usd_minus)+aReportDetailsForSecondCurrency,(eur_plus-eur_minus)+aReportDetailsForThirdCurrency,(gbp_plus-gbp_minus)+aReportDetailsForForthCurrency,zReport.getInvoiceReceiptAmount(),pullReportAmount,depositReportAmount);
        zReport.setzReportId(zID);
        zReport.setInvoiceAmount(invoiceAmount);
        zReport.setCreditInvoiceAmount(creditInvoiceAmount);
        ZReport finalZ = zReportDBAdapter.getByID(zReport.getzReportId());
*/
        return lastZReport;
    }
    public static  ZReport tempinsertZReport(ZReport zReport ,Context context)

        {
            Log.d("ZREport",zReport.toString());

            double aReportAmount = 0;
            double invoiceAmount=0;
            double creditInvoiceAmount=0;
            double receiptInvoiceAmount=0;
            double receiptInvoiceAmountCheck=0;
            double firstType_plus = 0, firstType_minus = 0;
            double secondType_plus = 0, secondType_minus = 0;
            double thirdType_plus = 0, thirdType_minus = 0;
            double fourthType_plus = 0, fourthType_minus = 0;
            double aReportDetailsForFirstCurrency=0;
            double aReportDetailsForSecondCurrency=0;
            double aReportDetailsForThirdCurrency=0;
            double aReportDetailsForForthCurrency=0;
            double cash_plus = 0, cash_minus = 0;
            double check_plus = 0, check_minus = 0;
            double creditCard_plus = 0, creditCard_minus = 0;
            ZReport lastZReport = getLastZReport(context);

            if (lastZReport == null) {
                lastZReport = new ZReport();
                lastZReport.setEndOrderId(0);
                lastZReport.setzReportId(0);
            }
            List<CurrencyType> currencyTypesList = null;
            List<Currency> currencyList=new ArrayList<>();
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();


            for (int i=0;i<currencyTypesList.size();i++){
                CurrencyDBAdapter currencyDBAdapter =new CurrencyDBAdapter(context);
                currencyDBAdapter.open();
                currencyList.add(currencyDBAdapter.getCurrencyByCode(currencyTypesList.get(i).getType()));
                currencyDBAdapter.close();
            }


            currencyTypeDBAdapter.close();
            OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(context);
            opiningReportDBAdapter.open();
            List<OpiningReport> opiningReportList = opiningReportDBAdapter.getListByLastZReport(lastZReport.getzReportId());
            for (int i=0;i<opiningReportList.size();i++){
                aReportAmount+=opiningReportList.get(i).getAmount();
            }
            if (SETTINGS.enableCurrencies) {
                OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
                aReportDetailsDBAdapter.open();
                for (int a=0 ;a<opiningReportList.size();a++) {
                    //aReportAmount+=opiningReportList.get(a).getAmount();
                    OpiningReport opiningReport = opiningReportList.get(a);
                    aReportDetailsForFirstCurrency+= aReportDetailsDBAdapter.getLastRow((int) currencyList.get(0).getId(), opiningReport.getOpiningReportId());
                    aReportDetailsForSecondCurrency+= aReportDetailsDBAdapter.getLastRow((int) currencyList.get(1).getId(), opiningReport.getOpiningReportId());
                    aReportDetailsForThirdCurrency+= aReportDetailsDBAdapter.getLastRow((int) currencyList.get(2).getId(), opiningReport.getOpiningReportId());
                    aReportDetailsForForthCurrency+= aReportDetailsDBAdapter.getLastRow((int) currencyList.get(3).getId(), opiningReport.getOpiningReportId());
                }

            }
           else {
                OpiningReportDetailsDBAdapter aReportDetailsDBAdapter=new OpiningReportDetailsDBAdapter(context);
                aReportDetailsDBAdapter.open();
                for (int a=0 ;a<opiningReportList.size();a++) {
                    OpiningReport opiningReport = opiningReportList.get(a);
                    aReportDetailsForFirstCurrency += aReportDetailsDBAdapter.getLastRow((int) currencyList.get(0).getId(), opiningReport.getOpiningReportId());
                }
            }
            ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(context);
            zReportDBAdapter.open();
            OrderDBAdapter orderDBAdapter = new OrderDBAdapter(context);
            orderDBAdapter.open();
            List<Order> sales = orderDBAdapter.getBetween(zReport.getStartOrderId(),zReport.getEndOrderId());
            List<CurrencyReturns> currencyReturnList = returnPaymentList(sales,context);
            List<CurrencyOperation>currencyOperationList=currencyOperationPaymentList(sales,context);
            List<Payment> payments = paymentList(sales,context);
           /* for (Payment p : payments) {
                int i = 0;
                switch (p.getPaymentWay()) {

                    case CONSTANT.CASH:
                        cash_plus += p.getAmount();
                        break;
                    case CONSTANT.CREDIT_CARD:
                        creditCard_plus += p.getAmount();
                        break;
                    case CONSTANT.CHECKS:
                        check_plus += p.getAmount();
                        break;
                }
            }*/

//with Currency

            if (SETTINGS.enableCurrencies) {
                for (CurrencyOperation cp : currencyOperationList) {


                    if (cp.getCurrencyType().equals(currencyTypesList.get(0).getType())){

                        firstType_plus+=cp.getAmount();
                    }
                    else   if (cp.getCurrencyType().equals(currencyTypesList.get(1).getType())){
                        secondType_plus += cp.getAmount();
                    }
                    else   if (cp.getCurrencyType().equals(currencyTypesList.get(2).getType())){
                        thirdType_plus += cp.getAmount();
                    }
                    else if (cp.getCurrencyType().equals(currencyTypesList.get(3).getType())){
                        fourthType_plus += cp.getAmount();
                    }


                   /* switch (cp.getCurrencyType()) {

                        case "ILS":
                            sheqle_plus += cp.getAmount();
                            break;
                        case "USD":
                            usd_plus += cp.getAmount();
                            break;
                        case "EUR":
                            eur_plus += cp.getAmount();

                            break;
                        case "GBP":
                            gbp_plus += cp.getAmount();
                            break;
                    }*/
                }


                for (CurrencyReturns cp : currencyReturnList) {
                    if ((int) cp.getCurrency_type()==(int) currencyList.get(0).getId()){
                        firstType_minus+=cp.getAmount();
                    }

                   else if ((int) cp.getCurrency_type()==(int) currencyList.get(1).getId()){
                        secondType_minus+=cp.getAmount();
                    }
                   else if ((int) cp.getCurrency_type()==(int) currencyList.get(2).getId()){
                        thirdType_minus+=cp.getAmount();
                    }
                   else if ((int) cp.getCurrency_type()==(int) currencyList.get(3).getId()){
                        fourthType_minus+=cp.getAmount();
                    }


                  /*  switch ((int) cp.getCurrency_type()) {
                        case CONSTANT.Shekel:
                            sheqle_minus += cp.getAmount();
                            break;
                        case CONSTANT.USD:
                            usd_minus += cp.getAmount();
                            break;
                        case CONSTANT.EUR:
                            if (cp.getAmount() > 0)
                                eur_minus += cp.getAmount();

                            break;
                        case CONSTANT.GBP:
                            gbp_minus += cp.getAmount();
                            break;
                    }*/
                }

            }else {
                firstType_plus+=cash_plus;
            }
            if(zReportDBAdapter.getProfilesCount()==0){
                PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
                posInvoiceDBAdapter.open();
                List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
                for (int i= 0 ;i<posReceiptList.size();i++){
                    receiptInvoiceAmount+=posReceiptList.get(i).getAmount();
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
                List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CASH);
                for (int i= 0 ;i<posReceiptList.size();i++){
                    receiptInvoiceAmount+=posReceiptList.get(i).getAmount();
                }
            }

            if(zReportDBAdapter.getProfilesCount()==0){
                PosInvoiceDBAdapter posInvoiceDBAdapter =new PosInvoiceDBAdapter(context);
                posInvoiceDBAdapter.open();
                List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoice(-1);
                for (int i= 0 ;i<posInvoiceList.size();i++){
                    invoiceAmount+=posInvoiceList.get(i).getAmount();
                }
                List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
                for (int i= 0 ;i<posCreditInvoiceList.size();i++){
                    creditInvoiceAmount+=posCreditInvoiceList.get(i).getAmount();
                }
                List<PosInvoice>posReceiptList = posInvoiceDBAdapter.getPosInvoiceListByType(-1, DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
                for (int i= 0 ;i<posReceiptList.size();i++){
                    receiptInvoiceAmountCheck+=posReceiptList.get(i).getAmount();
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
                List<PosInvoice>posInvoiceList = posInvoiceDBAdapter.getPosInvoice(zReport1.getzReportId());
                for (int i= 0 ;i<posInvoiceList.size();i++){
                    invoiceAmount+=posInvoiceList.get(i).getAmount();
                }
                List<PosInvoice>posCreditInvoiceList = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.CREDIT_INVOICE.getValue(),CONSTANT.CASH);
                for (int i= 0 ;i<posCreditInvoiceList.size();i++){
                    creditInvoiceAmount+=posCreditInvoiceList.get(i).getAmount();
                }
                List<PosInvoice>posReceiptListCheck = posInvoiceDBAdapter.getPosInvoiceListByType(zReport1.getzReportId(), DocumentType.RECEIPT.getValue(),CONSTANT.CHECKS);
                for (int i= 0 ;i<posReceiptListCheck.size();i++){
                    receiptInvoiceAmountCheck+=posReceiptListCheck.get(i).getAmount();
                }

            }
            zReport.setTotalPosSales(zReport.getTotalPosSales()+invoiceAmount);
            zReport.setTotalAmount(zReport.getTotalAmount()+aReportAmount+receiptInvoiceAmount);
            zReport.setTotalSales(zReport.getTotalSales()+invoiceAmount+creditInvoiceAmount);
            if(SETTINGS.enableCurrencies){
                firstType_plus=(firstType_plus-firstType_minus)+aReportDetailsForFirstCurrency+receiptInvoiceAmount;
            }else {
                firstType_plus=(firstType_plus-firstType_minus)+aReportAmount+receiptInvoiceAmount;
            }
            long zID = zReportDBAdapter.insertEntry(zReport.getCreatedAt(), zReport.getByUser(), zReport.getStartOrderId(), zReport.getEndOrderId(),
                    zReport.getTotalAmount(),zReport.getTotalSales(),cash_plus,check_plus+receiptInvoiceAmountCheck,creditCard_plus
                    ,Double.parseDouble(Util.makePrice(zReport.getTotalPosSales())),(zReport.getTotalAmount()/(1+SETTINGS.tax/100))*(SETTINGS.tax/100),invoiceAmount,
                    creditInvoiceAmount,firstType_plus,(secondType_plus-secondType_minus)+aReportDetailsForSecondCurrency,(thirdType_plus-thirdType_minus)+aReportDetailsForThirdCurrency,(fourthType_plus-fourthType_minus)+aReportDetailsForForthCurrency,zReport.getInvoiceReceiptAmount(),0,0,"close",zReport.getSalesBeforeTax(),zReport.getSalesWithTax(),zReport.getTotalTax(),zReport.getTotalPayPoint());
            zReport.setzReportId(zID);
            zReport.setInvoiceAmount(invoiceAmount);
            zReport.setCreditInvoiceAmount(creditInvoiceAmount);
            ZReport finalZ = zReportDBAdapter.getByID(zReport.getzReportId());

    return  finalZ;
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
    static int b=0;
    public static int sendClosingReport(final Context context, final String res){
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
                    b=  pdfLoadImagesClosingReport(data,context);

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

        return b;}

    public static void opiningReport(final Context context, final OpiningReport res, final ArrayList<String>currencyType, final ArrayList<Double>currencyAmount){

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
                        pdfUA.printOpiningReport(context,res,currencyType,currencyAmount);
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
    public static void pullAndDepositReport(final Context context, final DepositAndPullReport res, final ArrayList<String>currencyType, final ArrayList<Double>currencyAmount, final String type){

        final String SAMPLE_FILE = "depositAndPullReport.pdf";
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
                        pdfUA.printDepositAndPullReport(context,res,currencyType,currencyAmount,type);
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

    public static void logInLogOutReport(final Context context, final JSONObject res, final String reciveEmail){
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
                    pdfLoadImages(data,context,"");

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
                        SendLog.sendListFile(reciveEmail,context.getPackageName(),"loginreport.pdf");


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
    public static void salesManReport(final Context context, final List<CustomerAssistant>customerAssistantList, final double amount, final Timestamp from , final Timestamp to){
        final String SAMPLE_FILE = "salesmanreport.pdf";
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
                    pdfLoadImages(data,context,"");

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
                        pdfUA.createSalesManReport(context,customerAssistantList,amount,from ,to);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }
    public static void printPauseInvoice(final Context context, final List<OrderDetails>orderDetailsList, final double carDiscount){
        final String SAMPLE_FILE = "pauseInvoice.pdf";
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
                    pdfLoadImages(data,context,"Pause");

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
                        pdfUA.createPauseInvoice(context,orderDetailsList,carDiscount);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    public static  void printAndOpenCashBoxBTP880(final Context context, final Bitmap bitmap) {
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
            }

            @Override
            protected Void doInBackground(Void... params) {
                InvoiceImg invoiceImg = new InvoiceImg(context);
                pos.imageStandardModeRasterPrint(bitmap, CONSTANT.PRINTER_PAGE_WIDTH);
                return null;
            }
        }.execute();
    }
    static int a=0;
    public static int printAndOpenCashBoxHPRT_TP805(final Context context , final Bitmap bitmap) {
        if (HPRT_TP805.connect(context)) {
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
                        a=   HPRTPrinterHelper.CutPaper(HPRTPrinterHelper.HPRT_PARTIAL_CUT_FEED, 240);
                        //   Toast.makeText(context,a + " ",Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /**  try {
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
                     }*/

                    dialog.cancel();
                }

                @Override
                protected Void doInBackground(Void... params) {
                    byte b = 0;
                    try {
                        HPRTPrinterHelper.PrintBitmap(bitmap, b, b, 300);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } else {
            new android.support.v7.app.AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle(context.getString(R.string.printer))
                    .setMessage(context.getString(R.string.please_connect_the_printer))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return a;}
    public static  XReport insertXReport(  Context context)
    {
       double totalZReportAmount=0;
        /*(long xReportId, Timestamp createdAt, long byUser, long startOrderId, long endOrderId, double totalAmount , double totalSales,double cashTotal,double checkTotal ,double creditTotal,double totalPosSales,double tax,double invoiceAmount, double creditInvoiceAmount,double shekelAmount,double usdAmount, double eurAmount,
        double gbpAmount, double invoiceReceiptAmount,double pullReportAmount,double depositReportAmount)*/

        ZReportDBAdapter zReportDBAdapter =new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        ZReport zReport = null;
        try {
            zReport = zReportDBAdapter.getLastRow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (zReport == null) {
            zReport = new ZReport();
            zReport.setEndOrderId(0);
        }else {
            OrderDBAdapter orderDBAdapter = new OrderDBAdapter(context);
            orderDBAdapter.open();
            Order order = orderDBAdapter.getLast();
            zReport.setEndOrderId(order.getOrderId());
        }

        double amount = zReportDBAdapter.getZReportAmount(zReport.getStartOrderId(), zReport.getEndOrderId());
        try {
         //   ZReport zReport1 = zReportDBAdapter.getLastRow();
            ZReport z =zReportDBAdapter.getByID(zReport.getzReportId()-1);
            Log.d("zjojo","eoeo"+z.toString());
            totalZReportAmount=z.getTotalPosSales()+amount;
        } catch (Exception e) {
            totalZReportAmount=amount;
            e.printStackTrace();
        }
        //sales Amount
        zReport.setTotalAmount(Double.parseDouble(Util.makePrice(amount)));
        zReport.setTotalSales(Double.parseDouble(Util.makePrice(amount)));
        zReport.setInvoiceReceiptAmount(Double.parseDouble(Util.makePrice(amount)));
        zReport.setTotalPosSales(Double.parseDouble(Util.makePrice(totalZReportAmount)));
        Log.d("getTotalPosuTIL",zReport.getTotalPosSales()+"poss");
        XReportDBAdapter xReportDBAdapter=new XReportDBAdapter(context);
        xReportDBAdapter.open();
        long xID = xReportDBAdapter.insertEntry(new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE.getEmployeeId(), zReport.getStartOrderId(), zReport.getEndOrderId(),
                zReport.getTotalAmount(),zReport.getTotalSales(),zReport.getCashTotal(),zReport.getCheckTotal(),zReport.getCreditTotal()
                ,zReport.getTotalPosSales(),zReport.getTotalAmount()*SETTINGS.tax/100,zReport.getInvoiceAmount(),
                zReport.getCreditInvoiceAmount(),zReport.getFirstTypeAmount(),zReport.getSecondTypeAmount(),zReport.getThirdTypeAmount(),zReport.getFourthTypeAmount(),zReport.getInvoiceReceiptAmount(),zReport.getPullReportAmount(),zReport.getDepositReportAmount(),zReport.getSalesBeforeTax(),zReport.getSalesWithTax(),zReport.getTotalTax(),zReport.getTotalPayPoint());
        XReport finalX = xReportDBAdapter.getByID(xID);

        return finalX;
    }
    // get Currency Return  List
    public static List<CurrencyReturns> returnPaymentList(List<Order> sales,Context context) {
        List<CurrencyReturns> pl = new ArrayList<CurrencyReturns>();
        CurrencyReturnsDBAdapter currencyDBAdapter = new CurrencyReturnsDBAdapter(context);
        currencyDBAdapter.open();
        for (Order s : sales) {
            List<CurrencyReturns> payments = currencyDBAdapter.getCurencyReturnBySaleID(s.getOrderId());
            pl.addAll(payments);
        }
        currencyDBAdapter.close();
        return pl;
    }
    public static List<CurrencyOperation> currencyOperationPaymentList(List<Order> sales,Context context) {
        List<CurrencyOperation> pl = new ArrayList<CurrencyOperation>();
        CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(context);
        for (Order s : sales) {
            currencyOperationDBAdapter.open();

            List<CurrencyOperation> payments = currencyOperationDBAdapter.getCurrencyOperationByOrderID(s.getOrderId());
            pl.addAll(payments);
            currencyOperationDBAdapter.close();

        }
        return pl;
    }
    static List<Bitmap> bitmapList=new ArrayList<Bitmap>();

    public static List<Bitmap> ZReport(final Context context, final ZReport zReport, final boolean source){
        final String SAMPLE_FILE = "zreport.pdf";
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
                    pdfLoadImages(data,context,"");
                    Log.d("tesrr11",bitmapList.size()+"");

                }
                catch(Exception ignored)
                {

                }
            }
            @Override
            protected Void doInBackground(Void... voids) {
                MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);

                return null;
            }
        }.execute();


        return bitmapList;
    }


    public static void addPosSetting(final Context context) {
        new AsyncTask<Void, Void, String>() {

            // create and show a progress dialog

            ProgressDialog progressDialog = ProgressDialog.show(context, "", "Opening...");
            JSONObject jsonObject=new JSONObject();
            @Override
            protected void onPostExecute(String html) {
                try {
                    Log.d("ttttttttttttt",jsonObject.toString());
                    if(jsonObject.getString(MessageKey.status).equals("200")) {
                        //03-11 16:18:47.482 20608-20721/com.pos.leaders.leaderspossystem E/CCC: {"logTag":"CompanyCredentials Resource","status":"200","responseType":"All objects are successfully returned","responseBody":[{"companyName":"LeadTest","companyID":1,"tax":17.0,"returnNote":"thanks","endOfReturnNote":14,"ccun":"null","ccpw":"null"},{"companyName":"LeadTest","companyID":2,"tax":17.0,"returnNote":"thanks","endOfReturnNote":14,"ccun":"null","ccpw":"null"}]}

                        JSONObject respnse;

                        try {
                            respnse = jsonObject.getJSONObject(MessageKey.responseBody);
                        }
                        catch (JSONException e){
                            JSONArray jsonArray = jsonObject.getJSONArray(MessageKey.responseBody);
                            respnse = jsonArray.getJSONObject(0);
                        }

                        InventoryDbAdapter inventoryDbAdapter = new InventoryDbAdapter(context);
                        inventoryDbAdapter.open();
                        long i = inventoryDbAdapter.insertEntry(respnse.getString("name"), respnse.getLong("inventoryId"), respnse.getString("productsIdWithQuantityList"),
                                respnse.getInt("branchId"), 0);
                        inventoryDbAdapter.close();

                        if (i>= 1) {
                            SharedPreferences cSharedPreferences = context.getSharedPreferences("POS_Management", MODE_PRIVATE);
                            boolean creditCardEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CREDIT_CARD, false);
                            boolean pinPadEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PIN_PAD, false);
                            boolean currencyEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CURRENCY, false);
                            boolean customerMeasurementEnable = cSharedPreferences.getBoolean(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_CUSTOMER_MEASUREMENT, false);
                            int floatP = Integer.parseInt(cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_FLOAT_POINT, "2"));
                            String printerType = cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_PRINTER_TYPE, PrinterType.HPRT_TP805.name());
                            String customerEmail = cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_CUSTOMER_EMAIL,"lead2018pos@gmail.com");
                            String customerEmailPassWord = cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_CUSTOMER_EMAIL_PassWord,"lead2018POS@gmail.com");

                            String companyStatus = cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_COMPANY_STATUS, CompanyStatus.BO_COMPANY.name());
                            int branchI = Integer.parseInt(cSharedPreferences.getString(SetUpManagement.LEAD_POS_RESULT_INTENT_SET_UP_MANAGEMENT_ACTIVITY_ENABLE_BRANCH_ID, "0"));
                            PackageInfo pInfo = null;
                            try {
                                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            String verCode = pInfo.versionName;
                            PosSettingDbAdapter posSettingDbAdapter = new PosSettingDbAdapter(context);
                            posSettingDbAdapter.open();
                            posSettingDbAdapter.insertEntry(currencyEnable,creditCardEnable,pinPadEnable,customerMeasurementEnable,floatP,printerType,companyStatus,verCode, DbHelper.DATABASE_VERSION+"",branchI,SETTINGS.currencyCode,SETTINGS.currencySymbol,SETTINGS.country,SETTINGS.enableDuplicateInvoice,customerEmail,customerEmailPassWord);
                            //finish();
                        } else {
                            Log.e("setup",jsonObject.getString(MessageKey.responseType));
                            //Toast.makeText(SetupNewPOSOnlineActivity.context, SetupNewPOSOnlineActivity.context.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Log.e("setup",jsonObject.getString(MessageKey.responseType));
                        //Toast.makeText(SetupNewPOSOnlineActivity.context, SetupNewPOSOnlineActivity.context.getString(R.string.try_again)+": ", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //after async close progress dialog
                progressDialog.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                try {

                    String res = messageTransmit.authGet(ApiURL.INVENTORY + "/forPos", SESSION.token);
                    Log.e("CCC", res);
                    Log.e("CCC",SETTINGS.BO_SERVER_URL.toString());

                    try {
                        jsonObject = new JSONObject(res);
                    } catch (JSONException e) {
                        JSONArray jsonArray = new JSONArray(res);
                        jsonObject = jsonArray.getJSONObject(0);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
            }.execute();



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
    public static void getInventoryForBranchId(final Context context) {
        new AsyncTask<Void, Void, String>() {

            // create and show a progress dialog

            ProgressDialog progressDialog = ProgressDialog.show(context, "", "Opening...");
            JSONObject jsonObject=new JSONObject();
            @Override
            protected void onPostExecute(String html) {
                try {
                    if(jsonObject.getString(MessageKey.status).equals("200")) {
                        //03-11 16:18:47.482 20608-20721/com.pos.leaders.leaderspossystem E/CCC: {"logTag":"CompanyCredentials Resource","status":"200","responseType":"All objects are successfully returned","responseBody":[{"companyName":"LeadTest","companyID":1,"tax":17.0,"returnNote":"thanks","endOfReturnNote":14,"ccun":"null","ccpw":"null"},{"companyName":"LeadTest","companyID":2,"tax":17.0,"returnNote":"thanks","endOfReturnNote":14,"ccun":"null","ccpw":"null"}]}

                        JSONObject respnse;

                        try {
                            respnse = jsonObject.getJSONObject(MessageKey.responseBody);
                        }
                        catch (JSONException e){
                            JSONArray jsonArray = jsonObject.getJSONArray(MessageKey.responseBody);
                            respnse = jsonArray.getJSONObject(0);
                        }

                        SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(context);
                        settingsDBAdapter.open();
                        settingsDBAdapter.updateEntry( SETTINGS.companyID,SETTINGS.companyName,  SETTINGS.posID, (float) SETTINGS.tax, SETTINGS.returnNote,SETTINGS.endOfInvoice,SETTINGS.ccNumber,SETTINGS.ccPassword,
                                respnse.getInt("branchId"),SETTINGS.currencyCode,SETTINGS.currencySymbol,SETTINGS.country);
                        ProductDBAdapter productDBAdapter = new ProductDBAdapter(context);
                        List<Product>productList=productDBAdapter.getAllProducts();
                        CategoryDBAdapter categoryDBAdapter = new CategoryDBAdapter(context);
                        for(int i= 0;i<productList.size();i++){
                            categoryDBAdapter.open();
                            productDBAdapter.open();
                            Product product = productList.get(i);
                            Category category = categoryDBAdapter.getDepartmentByID(product.getCategoryId());
                            if(category.getBranchId()==0||category.getBranchId()==SETTINGS.branchId){

                            }else {
                                productDBAdapter.deleteEntry(product.getProductId());
                            }
                            productDBAdapter.close();
                            categoryDBAdapter.close();

                        }
                }
                    else {
                        Log.e("setup",jsonObject.getString(MessageKey.responseType));
                        //Toast.makeText(SetupNewPOSOnlineActivity.context, SetupNewPOSOnlineActivity.context.getString(R.string.try_again)+": ", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //after async close progress dialog
                progressDialog.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                MessageTransmit messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                try {

                    String res = messageTransmit.authGet(ApiURL.INVENTORY + "/forPos", SESSION.token);
                    Log.e("CCC", res);

                    try {
                        jsonObject = new JSONObject(res);
                    } catch (JSONException e) {
                        JSONArray jsonArray = new JSONArray(res);
                        jsonObject = jsonArray.getJSONObject(0);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();



    }
    public static Bitmap removeMargins2(Bitmap bmp, int color) {
        // TODO Auto-generated method stub


        long dtMili = System.currentTimeMillis();
        int MTop = 0, MBot = 0, MLeft = 0, MRight = 0;
        boolean found1 = false, found2 = false;

        int[] bmpIn = new int[bmp.getWidth() * bmp.getHeight()];
        int[][] bmpInt = new int[bmp.getWidth()][bmp.getHeight()];

        bmp.getPixels(bmpIn, 0, bmp.getWidth(), 0, 0, bmp.getWidth(),
                bmp.getHeight());

        for (int ii = 0, contX = 0, contY = 0; ii < bmpIn.length; ii++) {
            bmpInt[contX][contY] = bmpIn[ii];
            contX++;
            if (contX >= bmp.getWidth()) {
                contX = 0;
                contY++;
                if (contY >= bmp.getHeight()) {
                    break;
                }
            }
        }

        for (int hP = 0; hP < bmpInt[0].length && !found2; hP++) {
            // looking for MTop
            for (int wP = 0; wP < bmpInt.length && !found2; wP++) {
                if (bmpInt[wP][hP] != color) {
                    Log.e("MTop 2", "Pixel found @" + hP);
                    MTop = hP;
                    found2 = true;
                    break;
                }
            }
        }
        found2 = false;

        for (int hP = bmpInt[0].length - 1; hP >= 0 && !found2; hP--) {
            // looking for MBot
            for (int wP = 0; wP < bmpInt.length && !found2; wP++) {
                if (bmpInt[wP][hP] != color) {
                    Log.e("MBot 2", "Pixel found @" + hP);
                    MBot = bmp.getHeight() - hP;
                    found2 = true;
                    break;
                }
            }
        }
        found2 = false;

        for (int wP = 0; wP < bmpInt.length && !found2; wP++) {
            // looking for MLeft
            for (int hP = 0; hP < bmpInt[0].length && !found2; hP++) {
                if (bmpInt[wP][hP] != color) {
                    Log.e("MLeft 2", "Pixel found @" + wP);
                    MLeft = wP;
                    found2 = true;
                    break;
                }
            }
        }
        found2 = false;

        for (int wP = bmpInt.length - 1; wP >= 0 && !found2; wP--) {
            // looking for MRight
            for (int hP = 0; hP < bmpInt[0].length && !found2; hP++) {
                if (bmpInt[wP][hP] != color) {
                    Log.e("MRight 2", "Pixel found @" + wP);
                    MRight = bmp.getWidth() - wP;
                    found2 = true;
                    break;
                }
            }

        }
        found2 = false;

        int sizeY = bmp.getHeight() - MBot - MTop, sizeX = bmp.getWidth()
                - MRight - MLeft;

        Bitmap bmp2 = Bitmap.createBitmap(bmp, MLeft, MTop, sizeX, sizeY);
        dtMili = (System.currentTimeMillis() - dtMili);
        Log.e("Margin   2",
                "Time needed " + dtMili + "mSec\nh:" + bmp.getWidth() + "w:"
                        + bmp.getHeight() + "\narray x:" + bmpInt.length + "y:"
                        + bmpInt[0].length);
        return bmp2;
    }
}

