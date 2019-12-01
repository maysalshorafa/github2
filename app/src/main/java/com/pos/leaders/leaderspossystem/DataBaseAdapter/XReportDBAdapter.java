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
import com.pos.leaders.leaderspossystem.Models.XReport;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 2/3/2019.
 */

public class XReportDBAdapter {
    // Table Name
    protected static final String X_REPORT_TABLE_NAME = "x_report";
    // Column Names
    protected static final String X_REPORT_COLUMN_ID = "id";
    protected static final String X_REPORT_COLUMN_CREATEDATE = "createDate";
    protected static final String X_REPORT_COLUMN_BYUSER = "byUser";
    protected static final String X_REPORT_COLUMN_STARTORDERID = "startOrderId";
    protected static final String X_REPORT_COLUMN_ENDORDERID = "endOrderId";
    protected static final String X_REPORT_COLUMN_TOTAL_AMOUNT = "amount";
    protected static final String X_REPORT_COLUMN_TOTAL_SALES_AMOUNT = "totalSales";
    protected static final String X_REPORT_COLUMN_TAX = "tax";
    protected static final String X_REPORT_COLUMN_CASH_AMOUNT = "cashTotal";
    protected static final String X_REPORT_COLUMN_CHECK_AMOUNT = "checkTotal";
    protected static final String X_REPORT_COLUMN_CREDIT_AMOUNT = "creditTotal";
    protected static final String X_REPORT_COLUMN_TOTAL_POS_SALES = "totalPosSales";
    protected static final String X_REPORT_COLUMN_INVOICE_AMOUNT = "totalInvoiceAmount";
    protected static final String X_REPORT_COLUMN_CREDIT_INVOICE_AMOUNT = "totalCreditInvoiceAmount";
    protected static final String X_REPORT_COLUMN_SHEKEL_AMOUNT = "shekelAmount";
    protected static final String X_REPORT_COLUMN_USD_AMOUNT = "usdAmount";
    protected static final String X_REPORT_COLUMN_EUR_AMOUNT = "eurAmount";
    protected static final String X_REPORT_COLUMN_GBP_AMOUNT = "gbpAmount";
    protected static final String X_REPORT_COLUMN_INVOICE_RECEIPT_AMOUNT = "totalInvoiceReceiptAmount";
    protected static final String X_REPORT_COLUMN_PULL_REPORT_AMOUNT= "pullReportAmount";
    protected static final String X_REPORT_COLUMN_DEPOSIT_REPORT_AMOUNT= "depositReportAmount";
    protected static final String X_REPORT_COLUMN_SALES_BEFORE_TAX_REPORT= "salesBeforeTaxReport";
    protected static final String X_REPORT_COLUMN_SALES_WITH_TAX_REPORT= "salesWithTaxReport";
    protected static final String X_REPORT_COLUMN_TOTAL_TAX_REPORT= "totalTaxReport";

    public static final String DATABASE_CREATE = "CREATE TABLE `" + X_REPORT_TABLE_NAME + "` ( `" + X_REPORT_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT," +
            " `" + X_REPORT_COLUMN_CREATEDATE + "` TIMESTAMP DEFAULT current_timestamp, `" + X_REPORT_COLUMN_BYUSER + "`INTEGER," +
            " `" + X_REPORT_COLUMN_STARTORDERID + "` INTEGER, `" + X_REPORT_COLUMN_ENDORDERID + "` INTEGER," +
            " `" + X_REPORT_COLUMN_TOTAL_AMOUNT + "` REAL,`"  + X_REPORT_COLUMN_TOTAL_SALES_AMOUNT + "` REAL," +
            " `" + X_REPORT_COLUMN_TAX + "` REAL,`" + X_REPORT_COLUMN_CASH_AMOUNT + "` REAL default 0.0, `" + X_REPORT_COLUMN_CHECK_AMOUNT + "` REAL default 0.0," +
            " `" + X_REPORT_COLUMN_CREDIT_AMOUNT + "` REAL default 0.0,`" + X_REPORT_COLUMN_TOTAL_POS_SALES + "` REAL,`" +
            X_REPORT_COLUMN_INVOICE_AMOUNT + "` REAL default 0.0,`" + X_REPORT_COLUMN_SHEKEL_AMOUNT + "` REAL default 0.0,`" + X_REPORT_COLUMN_USD_AMOUNT + "` REAL default 0.0,`" + X_REPORT_COLUMN_EUR_AMOUNT + "` REAL default 0.0,`" + X_REPORT_COLUMN_GBP_AMOUNT + "` REAL default 0.0,`" + X_REPORT_COLUMN_PULL_REPORT_AMOUNT + "` REAL default 0.0,`" + X_REPORT_COLUMN_DEPOSIT_REPORT_AMOUNT + "` REAL default 0.0,`" + X_REPORT_COLUMN_INVOICE_RECEIPT_AMOUNT +  "` REAL default 0.0,`"+X_REPORT_COLUMN_SALES_BEFORE_TAX_REPORT + "` REAL default 0.0,`"+X_REPORT_COLUMN_TOTAL_TAX_REPORT + "` REAL default 0.0,`"+X_REPORT_COLUMN_SALES_WITH_TAX_REPORT +
            "` REAL default 0.0,`" +
            X_REPORT_COLUMN_CREDIT_INVOICE_AMOUNT + "` REAL default 0.0)";

    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public XReportDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public XReportDBAdapter open() throws SQLException {
        try {
            this.db = dbHelper.getWritableDatabase();
            return this;

        } catch (SQLException s) {
            new Exception("Error with DB Open");
            return this;

        }
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }
    public long insertEntry(Timestamp creatingDate, long byUserID, long startSaleID, long endSaleID, double amount, double totalSales, double totalCashAmount , double totalCheckAmount , double totalCreditAmount, double totalPosSalesAmount, double amountWithTax, double invoiceAmount , double creditInvoiceAmount, double shekelAmount, double usdAmount , double eurAmount , double gbpAmount, double invoiceReceiptAmount,double pullReportAmount,double depositReportAmount,double salesBeforeTax,double salesWithTax,double totalTax){
        XReport xReport = new XReport(Util.idHealth(this.db, X_REPORT_TABLE_NAME, X_REPORT_COLUMN_ID),creatingDate, byUserID, startSaleID, endSaleID,amount,totalSales,totalCashAmount,totalCheckAmount,totalCreditAmount,totalPosSalesAmount,amountWithTax,invoiceAmount,creditInvoiceAmount,shekelAmount,usdAmount,eurAmount,gbpAmount,invoiceReceiptAmount,pullReportAmount,depositReportAmount,salesBeforeTax,salesWithTax,totalTax);
        sendToBroker(MessageType.ADD_X_REPORT, xReport, this.context);
        try {
            return insertEntry(xReport);
        } catch (SQLException ex) {
            Log.e(X_REPORT_TABLE_NAME +" DB insert", "inserting Entry at " + X_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(XReport xReport) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(X_REPORT_COLUMN_ID, xReport.getxReportId());
        val.put(X_REPORT_COLUMN_CREATEDATE, String.valueOf(xReport.getCreatedAt()));
        val.put(X_REPORT_COLUMN_BYUSER, xReport.getByUser());
        val.put(X_REPORT_COLUMN_STARTORDERID, xReport.getStartOrderId());
        val.put(X_REPORT_COLUMN_ENDORDERID, xReport.getEndOrderId());
        val.put(X_REPORT_COLUMN_TOTAL_SALES_AMOUNT, xReport.getTotalSales());
        val.put(X_REPORT_COLUMN_TOTAL_AMOUNT, xReport.getTotalAmount());
        val.put(X_REPORT_COLUMN_CASH_AMOUNT,xReport.getCashTotal());
        val.put(X_REPORT_COLUMN_CHECK_AMOUNT,xReport.getCheckTotal());
        val.put(X_REPORT_COLUMN_CREDIT_AMOUNT,xReport.getCreditTotal());
        val.put(X_REPORT_COLUMN_TAX,xReport.getTax());
        val.put(X_REPORT_COLUMN_TOTAL_POS_SALES,xReport.getTotalPosSales());
        val.put(X_REPORT_COLUMN_INVOICE_AMOUNT,xReport.getInvoiceAmount());
        val.put(X_REPORT_COLUMN_CREDIT_INVOICE_AMOUNT,xReport.getCreditInvoiceAmount());
        val.put(X_REPORT_COLUMN_SHEKEL_AMOUNT,xReport.getShekelAmount());
        val.put(X_REPORT_COLUMN_USD_AMOUNT,xReport.getUsdAmount());
        val.put(X_REPORT_COLUMN_EUR_AMOUNT,xReport.getEurAmount());
        val.put(X_REPORT_COLUMN_INVOICE_RECEIPT_AMOUNT,xReport.getInvoiceReceiptAmount());
        val.put(X_REPORT_COLUMN_PULL_REPORT_AMOUNT,xReport.getPullReportAmount());
        val.put(X_REPORT_COLUMN_DEPOSIT_REPORT_AMOUNT,xReport.getDepositReportAmount());
        val.put(X_REPORT_COLUMN_SALES_BEFORE_TAX_REPORT,xReport.getSalesBeforeTax());
        val.put(X_REPORT_COLUMN_SALES_WITH_TAX_REPORT,xReport.getSalesWithTax());
        val.put(X_REPORT_COLUMN_TOTAL_TAX_REPORT,xReport.getTotalTax());
        try {
            return db.insert(X_REPORT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(X_REPORT_TABLE_NAME +" DB insert", "inserting Entry at " + X_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public XReport getByID(long id) {
        XReport xReport = null;
        Cursor cursor = db.rawQuery("select * from " + X_REPORT_TABLE_NAME + " where "+ X_REPORT_COLUMN_ID +"='" + id + "'", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
            return xReport;
        }
        cursor.moveToFirst();
        xReport = makeXReport(cursor);
        cursor.close();

        return xReport;
    }

    public List<XReport> getAll() {
        List<XReport> xReports = new ArrayList<XReport>();

        Cursor cursor = db.rawQuery("select * from " + X_REPORT_TABLE_NAME, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            xReports.add(makeXReport(cursor));
            cursor.moveToNext();
        }

        return xReports;
    }


    public List<XReport> getBetween(Date fromDate, Date toDate){
        List<XReport> xReports = new ArrayList<XReport>();

        Cursor cursor = db.rawQuery("select * from " + X_REPORT_TABLE_NAME + " where "+ X_REPORT_COLUMN_CREATEDATE +"<='"+toDate.getTime()+"' and "+ X_REPORT_COLUMN_CREATEDATE +
                ">='"+fromDate.getTime()+"'"+" order by "+ X_REPORT_COLUMN_ID +" desc", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            xReports.add(makeXReport(cursor));
            cursor.moveToNext();
        }

        return xReports;
    }

    public XReport getLastRow() throws Exception {
        XReport xReport = null;
        Cursor cursor = db.rawQuery("select * from " + X_REPORT_TABLE_NAME + " where id like '"+ SESSION.POS_ID_NUMBER+"%' order by id desc", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
            throw new Exception("there is no rows on X Report Table");
        }
        cursor.moveToFirst();
        xReport = makeXReport(cursor);
        cursor.close();

        return xReport;
    }
    private XReport makeXReport(Cursor c){
        return new XReport(c.getLong(c.getColumnIndex(X_REPORT_COLUMN_ID)),
                Timestamp.valueOf(c.getString(c.getColumnIndex(X_REPORT_COLUMN_CREATEDATE))),
                c.getLong(c.getColumnIndex(X_REPORT_COLUMN_BYUSER)),
                c.getLong(c.getColumnIndex(X_REPORT_COLUMN_STARTORDERID)),
                c.getLong(c.getColumnIndex(X_REPORT_COLUMN_ENDORDERID)),
                c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_TOTAL_AMOUNT)),
                c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_TOTAL_SALES_AMOUNT)),c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_CASH_AMOUNT)),
                c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_CHECK_AMOUNT)),c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_CREDIT_AMOUNT)),c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_TOTAL_POS_SALES)),c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_TAX)),
                c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_INVOICE_AMOUNT)),
                c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_CREDIT_INVOICE_AMOUNT)),  c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_SHEKEL_AMOUNT)),  c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_USD_AMOUNT)),
                c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_EUR_AMOUNT)),
                c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_GBP_AMOUNT)),
                c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_INVOICE_RECEIPT_AMOUNT)),
                c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_PULL_REPORT_AMOUNT)),
                c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_DEPOSIT_REPORT_AMOUNT)), c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_SALES_BEFORE_TAX_REPORT)),
                        c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_SALES_WITH_TAX_REPORT))
        , c.getDouble(c.getColumnIndex(X_REPORT_COLUMN_TOTAL_TAX_REPORT)));
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
    public List<XReport> calculateXReportAmount(){
        List<XReport> xReportList = new ArrayList<XReport>();
        Cursor cursor = db.rawQuery("select * from "+ X_REPORT_TABLE_NAME,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            xReportList.add(makeXReport(cursor));
            cursor.moveToNext();
        }

        return xReportList;
    }

    public void updateEntry(XReport xReport) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(X_REPORT_COLUMN_TOTAL_SALES_AMOUNT, xReport.getTotalSales());
        val.put(X_REPORT_COLUMN_TOTAL_AMOUNT, xReport.getTotalAmount());
        val.put(X_REPORT_COLUMN_TOTAL_POS_SALES, xReport.getTotalPosSales());

        String where = X_REPORT_COLUMN_ID + " = ?";
        db.update(X_REPORT_TABLE_NAME, val, where, new String[]{xReport.getxReportId() + ""});
    }
    public List<XReport> getBetweenTwoDates(long from, long to){
        List<XReport> xReportList = new ArrayList<XReport>();

        Cursor cursor = db.rawQuery("select * from " + X_REPORT_TABLE_NAME + " where " + X_REPORT_COLUMN_CREATEDATE + " between datetime("+from+"/1000, 'unixepoch') and datetime("+to+"/1000, 'unixepoch')", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            xReportList.add(makeXReport(cursor));
            cursor.moveToNext();
        }
        return xReportList;
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM " + X_REPORT_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public double getXReportAmount( long from, long to) {
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
    public static String addColumnReal(String columnName) {
        String dbc = "ALTER TABLE " + X_REPORT_TABLE_NAME
                + " add column " + columnName + " REAL default 0.0;";
        return dbc;
    }
}
