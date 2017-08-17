package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;

import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    protected static final String Z_REPORT_COLUMN_STARTSALEID = "startSaleID";
    protected static final String Z_REPORT_COLUMN_ENDSALEID = "endSaleID";


    public static final String DATABASE_CREATE = "CREATE TABLE "+Z_REPORT_TABLE_NAME+" ( `"+Z_REPORT_COLUMN_ID+"` INTEGER PRIMARY KEY AUTOINCREMENT, `"+Z_REPORT_COLUMN_CREATEDATE+"` TEXT DEFAULT current_timestamp,  `"+Z_REPORT_COLUMN_BYUSER+"` INTEGER, " +
            " `"+Z_REPORT_COLUMN_STARTSALEID+"` INTEGER,  `"+Z_REPORT_COLUMN_ENDSALEID+"` INTEGER , " +
            "FOREIGN KEY(`"+Z_REPORT_COLUMN_BYUSER+"`) REFERENCES `users.id` )";
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

    public long insertEntry(Date createDate, long byUser, long startSaleID, long endSaleID) {
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(Z_REPORT_COLUMN_ID, Util.idHealth(this.db, Z_REPORT_TABLE_NAME, Z_REPORT_COLUMN_ID));


        val.put(Z_REPORT_COLUMN_CREATEDATE, createDate.getTime());
        val.put(Z_REPORT_COLUMN_BYUSER, (int)byUser);
        val.put(Z_REPORT_COLUMN_STARTSALEID, startSaleID);
        val.put(Z_REPORT_COLUMN_ENDSALEID, endSaleID);
        try {
            return db.insert(Z_REPORT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(Z_REPORT_TABLE_NAME+" DB insert", "inserting Entry at " + Z_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(ZReport zReport) {
        return insertEntry(zReport.getCreationDate(),zReport.getByUser(),zReport.getStartSaleId(),zReport.getEndSaleId());
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

    public List<ZReport> getAllUserID(long userId) {
        List<ZReport> zReports = new ArrayList<ZReport>();

        Cursor cursor = db.rawQuery("select * from " + Z_REPORT_TABLE_NAME + " where "+Z_REPORT_COLUMN_ID+"='"+userId+"' order by "+Z_REPORT_COLUMN_ID+" desc", null);
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
        Cursor cursor = db.rawQuery("select * from " + Z_REPORT_TABLE_NAME + " order by id desc", null);
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
                DateConverter.stringToDate(c.getString(c.getColumnIndex(Z_REPORT_COLUMN_CREATEDATE))),
                c.getLong(c.getColumnIndex(Z_REPORT_COLUMN_BYUSER)),
                c.getLong(c.getColumnIndex(Z_REPORT_COLUMN_STARTSALEID)),
                c.getLong(c.getColumnIndex(Z_REPORT_COLUMN_ENDSALEID)));
    }

}
