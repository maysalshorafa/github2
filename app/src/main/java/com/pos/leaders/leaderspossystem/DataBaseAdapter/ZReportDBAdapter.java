package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 05/01/2017.
 */

public class ZReportDBAdapter {
    // Table Name
    protected static final String Z_REPORT_TABLE_NAME = "z_report";
    // Column Names
    protected static final String Z_REPORT_COLUMN_ID = "id";
    protected static final String Z_REPORT_COLUMN_CREATEDATE = "createDate";
    protected static final String Z_REPORT_COLUMN_BYUSER = "byUser";
    protected static final String Z_REPORT_COLUMN_STARTORDERID = "startOrderId";
    protected static final String Z_REPORT_COLUMN_ENDORDERID = "endOrderId";
    protected static final String Z_REPORT_COLUMN_TOTAL_AMOUNT= "amount";
    protected static final String Z_REPORT_COLUMN_TOTAL_SALES_AMOUNT= "totalSales";
    protected static final String Z_REPORT_COLUMN_TAX= "tax";
    protected static final String Z_REPORT_COLUMN_CASH_AMOUNT= "cashTotal";
    protected static final String Z_REPORT_COLUMN_CHECK_AMOUNT= "checkTotal";
    protected static final String Z_REPORT_COLUMN_CREDIT_AMOUNT= "creditTotal";
    protected static final String Z_REPORT_COLUMN_TOTAL_POS_SALES= "totalPosSales";
    protected static final String Z_REPORT_COLUMN_INVOICE_AMOUNT= "totalInvoiceAmount";
    protected static final String Z_REPORT_COLUMN_CREDIT_INVOICE_AMOUNT= "totalCreditInvoiceAmount";

    public static final String DATABASE_CREATE = "CREATE TABLE `" + Z_REPORT_TABLE_NAME + "` ( `" + Z_REPORT_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT," +
            " `" + Z_REPORT_COLUMN_CREATEDATE + "` TIMESTAMP DEFAULT current_timestamp, `" + Z_REPORT_COLUMN_BYUSER + "`INTEGER," +
            " `" + Z_REPORT_COLUMN_STARTORDERID + "` INTEGER, `" + Z_REPORT_COLUMN_ENDORDERID + "` INTEGER," +
            " `" + Z_REPORT_COLUMN_TOTAL_AMOUNT + "` REAL,`"  + Z_REPORT_COLUMN_TOTAL_SALES_AMOUNT + "` REAL," +
            " `" + Z_REPORT_COLUMN_TAX + "` REAL,`" + Z_REPORT_COLUMN_CASH_AMOUNT + "` REAL default 0.0, `" + Z_REPORT_COLUMN_CHECK_AMOUNT + "` REAL default 0.0," +
            " `" + Z_REPORT_COLUMN_CREDIT_AMOUNT + "` REAL default 0.0,`" + Z_REPORT_COLUMN_TOTAL_POS_SALES + "` REAL,`" + Z_REPORT_COLUMN_INVOICE_AMOUNT + "` REAL default 0.0,`" + Z_REPORT_COLUMN_CREDIT_INVOICE_AMOUNT + "` REAL default 0.0)";
    public static final String DATABASE_UPDATE_FROM_V2_TO_V3[] = {"alter table z_report rename to z_report_v3;", DATABASE_CREATE + "; ",
            "insert into z_report (id,createDate,startOrderId,endOrderId,amount) " +
                    "select id,createDate,startOrderId,endOrderId,amount from z_report_v3;"};
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public ZReportDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public ZReportDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }
    public long insertEntry(Timestamp creatingDate, long byUserID, long startSaleID, long endSaleID, double amount, double total_amount,double totalCashAmount , double totalCheckAmount , double totalCreditAmount,double totalPosSalesAmount,double amountWithTax,double invoiceAmount , double creditInvoiceAmount){
        ZReport zReport = new ZReport(Util.idHealth(this.db, Z_REPORT_TABLE_NAME, Z_REPORT_COLUMN_ID),creatingDate, byUserID, startSaleID, endSaleID,amount,total_amount,totalCashAmount,totalCheckAmount,totalCreditAmount,totalPosSalesAmount,amountWithTax,invoiceAmount,creditInvoiceAmount);
        sendToBroker(MessageType.ADD_Z_REPORT, zReport, this.context);
        try {
            return insertEntry(zReport);
        } catch (SQLException ex) {
            Log.e(Z_REPORT_TABLE_NAME+" DB insert", "inserting Entry at " + Z_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(ZReport zReport) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Z_REPORT_COLUMN_ID, zReport.getzReportId());
        val.put(Z_REPORT_COLUMN_CREATEDATE, String.valueOf(zReport.getCreatedAt()));
        val.put(Z_REPORT_COLUMN_BYUSER, zReport.getByUser());
        val.put(Z_REPORT_COLUMN_STARTORDERID, zReport.getStartOrderId());
        val.put(Z_REPORT_COLUMN_ENDORDERID, zReport.getEndOrderId());
        val.put(Z_REPORT_COLUMN_TOTAL_SALES_AMOUNT, zReport.getTotalSales());
        val.put(Z_REPORT_COLUMN_TOTAL_AMOUNT, zReport.getTotalAmount());
        val.put(Z_REPORT_COLUMN_CASH_AMOUNT,zReport.getCashTotal());
        val.put(Z_REPORT_COLUMN_CHECK_AMOUNT,zReport.getCheckTotal());
        val.put(Z_REPORT_COLUMN_CREDIT_AMOUNT,zReport.getCreditTotal());
        val.put(Z_REPORT_COLUMN_TAX,zReport.getTax());
        val.put(Z_REPORT_COLUMN_TOTAL_POS_SALES,zReport.getTotalPosSales());
        val.put(Z_REPORT_COLUMN_INVOICE_AMOUNT,zReport.getInvoiceAmount());
        val.put(Z_REPORT_COLUMN_CREDIT_INVOICE_AMOUNT,zReport.getCreditInvoiceAmount());
        try {
            return db.insert(Z_REPORT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(Z_REPORT_TABLE_NAME+" DB insert", "inserting Entry at " + Z_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public ZReport getByID(long id) {
        ZReport zReport = null;
        Cursor cursor = db.rawQuery("select * from " + Z_REPORT_TABLE_NAME + " where "+Z_REPORT_COLUMN_ID+"='" + id + "'", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
            return zReport;
        }
        cursor.moveToFirst();
        zReport = makeZReport(cursor);
        cursor.close();

        return zReport;
    }

    public List<ZReport> getAll() {
        List<ZReport> zReports = new ArrayList<ZReport>();

        Cursor cursor = db.rawQuery("select * from " + Z_REPORT_TABLE_NAME + " order by "+Z_REPORT_COLUMN_ID+" desc", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            zReports.add(makeZReport(cursor));
            cursor.moveToNext();
        }

        return zReports;
    }

    public List<ZReport> getBetween(Date fromDate,Date toDate){
        List<ZReport> zReports = new ArrayList<ZReport>();

        Cursor cursor = db.rawQuery("select * from " + Z_REPORT_TABLE_NAME + " where "+Z_REPORT_COLUMN_CREATEDATE+"<='"+toDate.getTime()+"' and "+Z_REPORT_COLUMN_CREATEDATE+
                ">='"+fromDate.getTime()+"'"+" order by "+Z_REPORT_COLUMN_ID+" desc", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            zReports.add(makeZReport(cursor));
            cursor.moveToNext();
        }

        return zReports;
    }

    public ZReport getLastRow() throws Exception {
        ZReport zReport = null;
        Cursor cursor = db.rawQuery("select * from " + Z_REPORT_TABLE_NAME + " where id like '"+ SESSION.POS_ID_NUMBER+"%' order by id desc", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
            throw new Exception("there is no rows on Z Report Table");
        }
        cursor.moveToFirst();
        zReport = makeZReport(cursor);
        cursor.close();

        return zReport;
    }
    private ZReport makeZReport(Cursor c){
            return new ZReport(c.getLong(c.getColumnIndex(Z_REPORT_COLUMN_ID)),
                Timestamp.valueOf(c.getString(c.getColumnIndex(Z_REPORT_COLUMN_CREATEDATE))),
                c.getLong(c.getColumnIndex(Z_REPORT_COLUMN_BYUSER)),
                c.getLong(c.getColumnIndex(Z_REPORT_COLUMN_STARTORDERID)),
                c.getLong(c.getColumnIndex(Z_REPORT_COLUMN_ENDORDERID)),
                c.getDouble(c.getColumnIndex(Z_REPORT_COLUMN_TOTAL_AMOUNT)),
                c.getDouble(c.getColumnIndex(Z_REPORT_COLUMN_TOTAL_SALES_AMOUNT)),c.getDouble(c.getColumnIndex(Z_REPORT_COLUMN_CASH_AMOUNT)),
                                c.getDouble(c.getColumnIndex(Z_REPORT_COLUMN_CHECK_AMOUNT)),c.getDouble(c.getColumnIndex(Z_REPORT_COLUMN_CREDIT_AMOUNT)),c.getDouble(c.getColumnIndex(Z_REPORT_COLUMN_TOTAL_POS_SALES)),c.getDouble(c.getColumnIndex(Z_REPORT_COLUMN_TAX)),
           c.getDouble(c.getColumnIndex(Z_REPORT_COLUMN_INVOICE_AMOUNT)),
                c.getDouble(c.getColumnIndex(Z_REPORT_COLUMN_CREDIT_INVOICE_AMOUNT)));
    }
    public double getZReportAmount( long from, long to) {
        double amount =0 , amountPlus =0 , amountMinus =0;
        OrderDBAdapter saleDBAdapter = new OrderDBAdapter(context);
        saleDBAdapter.open();
        List<Order> sales = saleDBAdapter.getBetween(from, to);
        saleDBAdapter.close();
        List<Payment> payments = paymentList(sales);

        for (Payment p : payments) {
            if(p.getAmount()>0){
                amountPlus+=p.getAmount();

            }else {
                amountMinus+=p.getAmount();
            }
        }
        amount = amountPlus+amountMinus;

        return amount;
    }
    public List<Payment> paymentList(List<Order> sales) {
        List<Payment> pl = new ArrayList<Payment>();
        PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(context);
        paymentDBAdapter.open();
        for (Order s : sales) {
            List<Payment> payments = paymentDBAdapter.getPaymentBySaleID(s.getOrderId());
            pl.addAll(payments);
        }
        paymentDBAdapter.close();
        return pl;
    }
    public double zReportTotalAmount(){
        Cursor cursor = db.rawQuery(" select sum(amount) from " + Z_REPORT_TABLE_NAME + " where id like '%"+SESSION.POS_ID_NUMBER+"%'", null);

        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        return  0;
    }
    public double zReportTotalAmountUpDate(long id){
        Cursor cursor = db.rawQuery(" select sum(amount) from " + Z_REPORT_TABLE_NAME + " where id like '%"+SESSION.POS_ID_NUMBER+"%'" + "and id <= " + id , null);

        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        return  0;
    }
    public List<ZReport> calculateZReportAmount(){
        List<ZReport> zReportList = new ArrayList<ZReport>();
        Cursor cursor = db.rawQuery("select * from "+Z_REPORT_TABLE_NAME,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
                zReportList.add(makeZReport(cursor));
            cursor.moveToNext();
        }

        return zReportList;
    }
    public void  test(){
        ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(context);
        zReportDBAdapter.open();
        List<ZReport> zReportList = calculateZReportAmount();
        List<ZReport> zl = new ArrayList<ZReport>();
        zl.addAll(zReportList);
        double totalAmount =0;
        for (int  i= 0 ; i<zl.size();i++){
            ZReport zReport1 = zl.get(i);
            double amount = zReportDBAdapter.zReportTotalAmountUpDate(zReport1.getzReportId());
            totalAmount+=amount;
                ZReport zReport =new ZReport(zl.get(i).getzReportId(),zl.get(i).getCreatedAt(),zl.get(i).getByUser(),zl.get(i).getStartOrderId(),zl.get(i).getEndOrderId(),zl.get(i).getTotalAmount(),
                    amount,zl.get(i).getCashTotal(),zl.get(i).getCheckTotal(),zl.get(i).getCreditTotal(),totalAmount,zl.get(i).getTax(),zl.get(i).getInvoiceAmount(),zl.get(i).getCreditInvoiceAmount());
            updateEntry(zReport);
        }
    }
    public void updateEntry(ZReport zReport) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Z_REPORT_COLUMN_TOTAL_SALES_AMOUNT, zReport.getTotalSales());
        val.put(Z_REPORT_COLUMN_TOTAL_AMOUNT, zReport.getTotalAmount());

        String where = Z_REPORT_COLUMN_ID + " = ?";
        db.update(Z_REPORT_TABLE_NAME, val, where, new String[]{zReport.getzReportId() + ""});
    }
    public List<ZReport> getBetweenTwoDates(long from, long to){
        List<ZReport> zReportList = new ArrayList<ZReport>();

        Cursor cursor = db.rawQuery("select * from " + Z_REPORT_TABLE_NAME + " where " + Z_REPORT_COLUMN_CREATEDATE + " between datetime("+from+"/1000, 'unixepoch') and datetime("+to+"/1000, 'unixepoch')", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
                zReportList.add(makeZReport(cursor));
            cursor.moveToNext();
        }
        return zReportList;
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + Z_REPORT_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
