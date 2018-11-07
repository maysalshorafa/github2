package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 12/04/2017.
 */

public class AReportDBAdapter {
    // Table Name
    protected static final String A_REPORT_TABLE_NAME = "a_report";
    // Column Names
    protected static final String A_REPORT_COLUMN_ID = "id";
    protected static final String A_REPORT_COLUMN_CREATEDATE = "createDate";
    protected static final String A_REPORT_COLUMN_BYUSER = "byEmployee";
    protected static final String A_REPORT_COLUMN_AMOUNT = "amount";
    protected static final String A_REPORT_COLUMN_LASTSALEID = "lastSaleID";
    protected static final String A_REPORT_COLUMN_LASTZREPORTID = "lastZReportID";


    public static final String DATABASE_CREATE = "CREATE TABLE "+ A_REPORT_TABLE_NAME
            +" ( `"+ A_REPORT_COLUMN_ID +"` INTEGER PRIMARY KEY AUTOINCREMENT, `"+ A_REPORT_COLUMN_CREATEDATE +"` TIMESTAMP DEFAULT current_timestamp,  `"
            + A_REPORT_COLUMN_BYUSER +"` INTEGER, " +
            " `"+ A_REPORT_COLUMN_AMOUNT +"` REAL,  `"+ A_REPORT_COLUMN_LASTSALEID +"` INTEGER , " +
              "`"+ A_REPORT_COLUMN_LASTZREPORTID +"` INTEGER , " +
            "FOREIGN KEY(`"+ A_REPORT_COLUMN_BYUSER +"`) REFERENCES `employees.id` )";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public AReportDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public AReportDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(Timestamp createDate, long byUser, double amount, long lastSaleID, long lastZReport) {
        AReport aReport = new AReport(Util.idHealth(this.db, A_REPORT_TABLE_NAME, A_REPORT_COLUMN_ID), createDate, byUser, amount, lastSaleID, lastZReport);
        sendToBroker(MessageType.ADD_A_REPORT, aReport, this.context);
        try {
            return insertEntry(aReport);
        } catch (SQLException ex) {
            Log.e(A_REPORT_TABLE_NAME +" DB insert", "inserting Entry at " + A_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public long insertEntry(AReport aReport) {
        ContentValues val = new ContentValues();

        val.put(A_REPORT_COLUMN_ID, aReport.getaReportId());
        val.put(A_REPORT_COLUMN_BYUSER, aReport.getByUserID());
        val.put(A_REPORT_COLUMN_AMOUNT, aReport.getAmount());
        val.put(A_REPORT_COLUMN_LASTSALEID, aReport.getLastOrderId());
        val.put(A_REPORT_COLUMN_LASTZREPORTID, aReport.getLastZReportID());
        try {
            return db.insert(A_REPORT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(A_REPORT_TABLE_NAME +" DB insert", "inserting Entry at " + A_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public AReport getByLastZReport(long lastZReportID){
        AReport aReport;
        Cursor cursor = db.rawQuery("select * from " + A_REPORT_TABLE_NAME + " where " + A_REPORT_COLUMN_LASTZREPORTID + "='" + (lastZReportID ) + "'", null);
        if (cursor.getCount() < 1) {
            //cursor.close();
            cursor = db.rawQuery("select * from " + A_REPORT_TABLE_NAME,null);
            //return null;
        }
        cursor.moveToFirst();
        aReport = makeAReport(cursor);
        return aReport;
    }

    public List<AReport> getBetween(Date fromDate,Date toDate){
        List<AReport> aReports = new ArrayList<AReport>();

        Cursor cursor = db.rawQuery("select * from " + A_REPORT_TABLE_NAME + " where " + A_REPORT_COLUMN_CREATEDATE + "<='" + toDate.getTime() + "' and " + A_REPORT_COLUMN_CREATEDATE +
                ">='" + fromDate.getTime() + "'" + " order by " + A_REPORT_COLUMN_ID + " desc", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            aReports.add(makeAReport(cursor));
            cursor.moveToNext();
        }

        return aReports;
    }

    public AReport getLastRow() throws Exception {
        AReport aReport = null;
        Cursor cursor = db.rawQuery("select * from " + A_REPORT_TABLE_NAME + " where id like '"+SESSION.POS_ID_NUMBER+"%' order by id desc", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
             throw new Exception("there is no rows on Z Report Table");
        }
        cursor.moveToFirst();
        aReport = makeAReport(cursor);
        cursor.close();

        return aReport;
    }

    private AReport makeAReport(Cursor c){
        return new AReport(c.getLong(c.getColumnIndex(A_REPORT_COLUMN_ID)),
                Timestamp.valueOf(c.getString(c.getColumnIndex(A_REPORT_COLUMN_CREATEDATE))),
                c.getLong(c.getColumnIndex(A_REPORT_COLUMN_BYUSER)),
                c.getDouble(c.getColumnIndex(A_REPORT_COLUMN_AMOUNT)),
                c.getLong(c.getColumnIndex(A_REPORT_COLUMN_LASTSALEID)),
                c.getLong(c.getColumnIndex(A_REPORT_COLUMN_LASTZREPORTID)));
    }
}
