package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.ZReportCount;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;

/**
 * Created by Win8.1 on 10/13/2019.
 */

public class ZReportCountDbAdapter {
    // Table Name
    protected static final String Z_REPORT_COUNT_TABLE_NAME = "z_report";
    // Column Names
    protected static final String Z_REPORT_COUNT_COLUMN_ID = "id";
    protected static final String Z_REPORT_COUNT_COLUMN_ZREPORT_ID = "zreport_id";

    protected static final String Z_REPORT_COUNT_COLUMN_CASH= "cashCount";
    protected static final String Z_REPORTCOUNT_COLUMN_CHECK= "checkCount";
    protected static final String Z_REPORT_COUNT_COLUMN_CREDIT= "creditCount";
    protected static final String Z_REPORT_COUNT_COLUMN_INVOICE= "totalInvoiceCount";
    protected static final String Z_REPORT_COUNT_COLUMN_CREDIT_INVOICE= "totalCreditInvoiceCount";
    protected static final String Z_REPORT_COUNT_COLUMN_FIRST_TYPE= "firstTypeCount";
    protected static final String Z_REPORT_COUNT_COLUMN_SECOND_TYPE= "secondTypeCount";
    protected static final String Z_REPORT_COUNT_COLUMN_THIRD_TYPE= "thirdTypeCount";
    protected static final String Z_REPORT_COLUMN_COUNT_FOURTH_TYPE= "fourthTypeCount";
    protected static final String Z_REPORT_COLUMN_COUNT_INVOICE_RECEIPT= "totalInvoiceReceiptCount";

    public static final String DATABASE_CREATE = "CREATE TABLE `" + Z_REPORT_COUNT_TABLE_NAME + "` ( `" + Z_REPORT_COUNT_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT,"
            +"`" + Z_REPORT_COUNT_COLUMN_ZREPORT_ID + "` INTEGER default 0, `" + Z_REPORT_COUNT_COLUMN_CASH + "` INTEGER default 0," +
            " `" + Z_REPORTCOUNT_COLUMN_CHECK + "` INTEGER default 0," +
            " `" + Z_REPORT_COUNT_COLUMN_CREDIT + "` INTEGER default 0,`" + Z_REPORT_COUNT_COLUMN_INVOICE + "` INTEGER,`" +
            Z_REPORT_COUNT_COLUMN_CREDIT_INVOICE + "` INTEGER default 0,`" +  Z_REPORT_COUNT_COLUMN_FIRST_TYPE + "` INTEGER default 0,`" +  Z_REPORT_COUNT_COLUMN_SECOND_TYPE + "` INTEGER default 0,`"
            + Z_REPORT_COUNT_COLUMN_THIRD_TYPE + "` INTEGER default 0,`"  + Z_REPORT_COLUMN_COUNT_FOURTH_TYPE + "` INTEGER default 0, "+"`" +
            Z_REPORT_COLUMN_COUNT_INVOICE_RECEIPT + "` INTEGER default 0)";


    public static final String DATABASE_UPDATE_FROM_V9_TO_V10[] = {"alter table z_report rename to z_reportCount_v;", DATABASE_CREATE + "; ",
            "insert into z_report (id,zreport_id,cashCount,checkCount,creditCount,totalInvoiceCount,totalCreditInvoiceCount,firstTypeCount,secondTypeCount,thirdTypeCount,fourthTypeCount,totalInvoiceReceiptCount) " +
                    "select id,zreport_id,cashCount,checkCount,creditCount,totalInvoiceCount,totalCreditInvoiceCount,shekelCount,usdCount,eurCount,gbpCount,totalInvoiceReceiptCount from z_reportCount_v;"};


    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public ZReportCountDbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public ZReportCountDbAdapter open() throws SQLException {
            this.db = dbHelper.getWritableDatabase();
            return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(ZReportCount zReport) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Z_REPORT_COUNT_COLUMN_ID, Util.idHealth(this.db, Z_REPORT_COUNT_TABLE_NAME, Z_REPORT_COUNT_COLUMN_ID));
        val.put(Z_REPORT_COUNT_COLUMN_ZREPORT_ID,zReport.getzReportCountZReportId());
        val.put(Z_REPORT_COUNT_COLUMN_CASH,zReport.getCashCount());
        val.put(Z_REPORTCOUNT_COLUMN_CHECK,zReport.getCheckCount());
        val.put(Z_REPORT_COUNT_COLUMN_CREDIT,zReport.getCreditCount());
        val.put(Z_REPORT_COUNT_COLUMN_INVOICE,zReport.getInvoiceCount());
        val.put(Z_REPORT_COUNT_COLUMN_CREDIT_INVOICE,zReport.getCreditInvoiceCount());
        val.put(Z_REPORT_COUNT_COLUMN_FIRST_TYPE,zReport.getFirstTYpeCount());
        val.put(Z_REPORT_COUNT_COLUMN_SECOND_TYPE,zReport.getSecondTypeCount());
        val.put(Z_REPORT_COUNT_COLUMN_THIRD_TYPE,zReport.getThirdTypeCount());
        val.put(Z_REPORT_COLUMN_COUNT_FOURTH_TYPE,zReport.getFourthTypeCount());
        val.put(Z_REPORT_COLUMN_COUNT_INVOICE_RECEIPT,zReport.getInvoiceReceiptCount());
        Log.d("testZReportCount",zReport.toString());


        try {
            return db.insert(Z_REPORT_COUNT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(Z_REPORT_COUNT_TABLE_NAME+" DB insert", "inserting Entry at " + Z_REPORT_COUNT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public ZReportCount getByID(long id) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        ZReportCount zReport = null;
        Cursor cursor = db.rawQuery("select * from " + Z_REPORT_COUNT_TABLE_NAME + " where "+Z_REPORT_COUNT_COLUMN_ZREPORT_ID+"='" + id + "'", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
            close();
            return zReport;
        }
        cursor.moveToFirst();
        zReport = makeZReport(cursor);
        cursor.close();
         close();
        return zReport;
    }
    private ZReportCount makeZReport(Cursor c){

        return new ZReportCount(c.getLong(c.getColumnIndex(Z_REPORT_COUNT_COLUMN_ID)),
               c.getInt(c.getColumnIndex(Z_REPORT_COUNT_COLUMN_CASH)),
                c.getInt(c.getColumnIndex(Z_REPORTCOUNT_COLUMN_CHECK)),c.getInt(c.getColumnIndex(Z_REPORT_COUNT_COLUMN_CREDIT)),
                c.getInt(c.getColumnIndex(Z_REPORT_COUNT_COLUMN_INVOICE)),
                c.getInt(c.getColumnIndex(Z_REPORT_COUNT_COLUMN_CREDIT_INVOICE)),  c.getInt(c.getColumnIndex(Z_REPORT_COUNT_COLUMN_FIRST_TYPE)),
                c.getInt(c.getColumnIndex(Z_REPORT_COUNT_COLUMN_SECOND_TYPE)),
                c.getInt(c.getColumnIndex(Z_REPORT_COUNT_COLUMN_THIRD_TYPE)),  c.getInt(c.getColumnIndex(Z_REPORT_COLUMN_COUNT_FOURTH_TYPE)),
                c.getInt(c.getColumnIndex(Z_REPORT_COLUMN_COUNT_INVOICE_RECEIPT))
                ,c.getLong(c.getColumnIndex(Z_REPORT_COUNT_COLUMN_ZREPORT_ID)));
    }
    public void updateEntry(ZReportCount zReport) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        ContentValues val = new ContentValues();
        val.put(Z_REPORT_COUNT_COLUMN_CASH,zReport.getCashCount());
        val.put(Z_REPORTCOUNT_COLUMN_CHECK,zReport.getCheckCount());
        val.put(Z_REPORT_COUNT_COLUMN_CREDIT,zReport.getCreditCount());
        val.put(Z_REPORT_COUNT_COLUMN_INVOICE,zReport.getInvoiceCount());
        val.put(Z_REPORT_COUNT_COLUMN_CREDIT_INVOICE,zReport.getCreditInvoiceCount());
        val.put(Z_REPORT_COUNT_COLUMN_FIRST_TYPE,zReport.getFirstTYpeCount());
        val.put(Z_REPORT_COUNT_COLUMN_SECOND_TYPE,zReport.getSecondTypeCount());
        val.put(Z_REPORT_COUNT_COLUMN_THIRD_TYPE,zReport.getThirdTypeCount());
        val.put(Z_REPORT_COLUMN_COUNT_FOURTH_TYPE,zReport.getFourthTypeCount());
        val.put(Z_REPORT_COLUMN_COUNT_INVOICE_RECEIPT,zReport.getInvoiceReceiptCount());
        Log.d("testZReport",zReport.toString());

        String where = Z_REPORT_COUNT_COLUMN_ID + " = ?";
        db.update(Z_REPORT_COUNT_TABLE_NAME, val, where, new String[]{zReport.getzReportCountId() + ""});
        close();
    }
    public ZReportCount getLastRow() throws Exception {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        ZReportCount zReport = null;
        Cursor cursor = db.rawQuery("select * from " + Z_REPORT_COUNT_TABLE_NAME + " where id like '"+ SESSION.POS_ID_NUMBER+"%' order by id desc", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
            throw new Exception("there is no rows on Z Report Table");
        }
        cursor.moveToFirst();
        zReport = makeZReport(cursor);
        cursor.close();
       close();
        return zReport;
    }
}
