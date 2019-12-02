package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
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

public class OpiningReportDBAdapter {
    // Table Name
    public static final String OPINING_REPORT_TABLE_NAME = "a_report";
    // Column Names
    protected static final String OPINING_REPORT_COLUMN_ID = "id";
    protected static final String OPINING_REPORT_COLUMN_CREATEDATE = "createDate";
    protected static final String OPINING_REPORT_COLUMN_BYUSER = "byEmployee";
    protected static final String OPINING_REPORT_COLUMN_AMOUNT = "amount";
    protected static final String OPINING_REPORT_COLUMN_LASTSALEID = "lastSaleID";
    protected static final String OPINING_REPORT_COLUMN_LASTZREPORTID = "lastZReportID";


    public static final String DATABASE_CREATE = "CREATE TABLE "+ OPINING_REPORT_TABLE_NAME
            +" ( `"+ OPINING_REPORT_COLUMN_ID +"` INTEGER PRIMARY KEY AUTOINCREMENT, `"+ OPINING_REPORT_COLUMN_CREATEDATE +"` TIMESTAMP DEFAULT current_timestamp,  `"
            + OPINING_REPORT_COLUMN_BYUSER +"` INTEGER, " +
            " `"+ OPINING_REPORT_COLUMN_AMOUNT +"` REAL,  `"+ OPINING_REPORT_COLUMN_LASTSALEID +"` INTEGER , " +
              "`"+ OPINING_REPORT_COLUMN_LASTZREPORTID +"` INTEGER , " +
            "FOREIGN KEY(`"+ OPINING_REPORT_COLUMN_BYUSER +"`) REFERENCES `employees.id` )";
    public static final String DATABASE_UPDATE_FROM_V1_TO_V2 = "alter table a_report rename to opining_report;";

    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public OpiningReportDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public OpiningReportDBAdapter open() throws SQLException {

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
        OpiningReport aReport = new OpiningReport(Util.idHealth(this.db,OPINING_REPORT_TABLE_NAME, OPINING_REPORT_COLUMN_ID), createDate, byUser, amount, lastSaleID, lastZReport);
        sendToBroker(MessageType.ADD_OPINING_REPORT, aReport, this.context);
        try {
            return insertEntry(aReport);
        } catch (SQLException ex) {
            Log.e(" DB insert", "inserting Entry at " + OPINING_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public long insertEntry(OpiningReport aReport) {
        ContentValues val = new ContentValues();

        val.put(OPINING_REPORT_COLUMN_ID, aReport.getOpiningReportId());
        val.put(OPINING_REPORT_COLUMN_BYUSER, aReport.getByUserID());
        val.put(OPINING_REPORT_COLUMN_AMOUNT, aReport.getAmount());
        val.put(OPINING_REPORT_COLUMN_LASTSALEID, aReport.getLastOrderId());
        val.put(OPINING_REPORT_COLUMN_LASTZREPORTID, aReport.getLastZReportID());
        try {
            return db.insert(OPINING_REPORT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(" DB insert", "inserting Entry at " + OPINING_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    public long upDateEntry(OpiningReport aReport) {
        ContentValues val = new ContentValues();

        val.put(OPINING_REPORT_COLUMN_ID, aReport.getOpiningReportId());
        val.put(OPINING_REPORT_COLUMN_BYUSER, aReport.getByUserID());
        val.put(OPINING_REPORT_COLUMN_AMOUNT, aReport.getAmount());
        val.put(OPINING_REPORT_COLUMN_LASTSALEID, aReport.getLastOrderId());
        val.put(OPINING_REPORT_COLUMN_LASTZREPORTID, aReport.getLastZReportID());
        try {
            String where = OPINING_REPORT_COLUMN_ID + " = ?";
            db.update(OPINING_REPORT_TABLE_NAME, val, where, new String[]{aReport.getOpiningReportId() + ""});
        } catch (SQLException ex) {
            Log.e(" DB Update", "update Entry at " + OPINING_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
        return 1;
    }

    public OpiningReport getByLastZReport(long lastZReportID){
        OpiningReport aReport;
        Cursor cursor = db.rawQuery("select * from " + OPINING_REPORT_TABLE_NAME + " where " + OPINING_REPORT_COLUMN_LASTZREPORTID + "='" + (lastZReportID ) + "'", null);
        if (cursor.getCount() < 1) {
            //cursor.close();
            cursor = db.rawQuery("select * from " + OPINING_REPORT_TABLE_NAME,null);
            //return null;
        }
        cursor.moveToFirst();
        aReport = makeAReport(cursor);
        return aReport;
    }
    public  List<OpiningReport> getListByLastZReport(long lastZReportID){
        List<OpiningReport> aReports = new ArrayList<OpiningReport>();
        try {
            if(dbHelper==null) {
                open();
            }
            Cursor cursor = db.rawQuery("select * from " + OPINING_REPORT_TABLE_NAME + " where " + OPINING_REPORT_COLUMN_LASTZREPORTID + "='" + (lastZReportID ) + "'", null);
        if (cursor.getCount() < 1) {
            //cursor.close();
            cursor = db.rawQuery("select * from " + OPINING_REPORT_TABLE_NAME,null);
            //return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            aReports.add(makeAReport(cursor));
            cursor.moveToNext();
        }


        } catch (Exception e) {
        Log.d("eeeeeee",e.toString());
        }

        return aReports;
    }
    public List<OpiningReport> getBetween(Date fromDate, Date toDate){
        List<OpiningReport> aReports = new ArrayList<OpiningReport>();
        try {
            if(dbHelper==null) {
                open();
            }
        Cursor cursor = db.rawQuery("select * from " + OPINING_REPORT_TABLE_NAME + " where " + OPINING_REPORT_COLUMN_CREATEDATE + "<='" + toDate.getTime() + "' and " + OPINING_REPORT_COLUMN_CREATEDATE +
                ">='" + fromDate.getTime() + "'" + " order by " + OPINING_REPORT_COLUMN_ID + " desc", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            aReports.add(makeAReport(cursor));
            cursor.moveToNext();
        }

close();
    } catch (Exception e) {
        Log.d("exception",e.toString());
    }

        return aReports;
    }

    public OpiningReport getLastRow() throws Exception {
        OpiningReport aReport = null;
        Cursor cursor = db.rawQuery("select * from " + OPINING_REPORT_TABLE_NAME + " where id like '%"+SESSION.POS_ID_NUMBER+"%' order by id desc", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
             throw new Exception("there is no rows on A Report Table");
        }
        cursor.moveToFirst();
        aReport = makeAReport(cursor);
        cursor.close();

        return aReport;
    }

    private OpiningReport makeAReport(Cursor c){
        return new OpiningReport(c.getLong(c.getColumnIndex(OPINING_REPORT_COLUMN_ID)),
                Timestamp.valueOf(c.getString(c.getColumnIndex(OPINING_REPORT_COLUMN_CREATEDATE))),
                c.getLong(c.getColumnIndex(OPINING_REPORT_COLUMN_BYUSER)),
                c.getDouble(c.getColumnIndex(OPINING_REPORT_COLUMN_AMOUNT)),
                c.getLong(c.getColumnIndex(OPINING_REPORT_COLUMN_LASTSALEID)),
                c.getLong(c.getColumnIndex(OPINING_REPORT_COLUMN_LASTZREPORTID)));
    }
    public OpiningReport getById(long id){
        OpiningReport aReport;
        Cursor cursor = db.rawQuery("select * from " + OPINING_REPORT_TABLE_NAME + " where " + OPINING_REPORT_COLUMN_ID + "='" + (id ) + "'", null);
        if (cursor.getCount() < 1) {
            //cursor.close();
            cursor = db.rawQuery("select * from " + OPINING_REPORT_TABLE_NAME,null);
            //return null;
        }
        cursor.moveToFirst();
        aReport = makeAReport(cursor);
        return aReport;
    }
}
