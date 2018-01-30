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
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

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

    public long insertEntry(long creatingDate,long byUserID,long startSaleID,long endSaleID){
        ZReport zReport = new ZReport(Util.idHealth(this.db, Z_REPORT_TABLE_NAME, Z_REPORT_COLUMN_ID), new Date(creatingDate), byUserID, startSaleID, endSaleID);
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
        val.put(Z_REPORT_COLUMN_ID, zReport.getId());

        val.put(Z_REPORT_COLUMN_CREATEDATE, zReport.getCreationDate().getTime());
        val.put(Z_REPORT_COLUMN_BYUSER, zReport.getByUser());
        val.put(Z_REPORT_COLUMN_STARTSALEID, zReport.getStartSaleId());
        val.put(Z_REPORT_COLUMN_ENDSALEID, zReport.getEndSaleId());
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
                DateConverter.stringToDate(c.getString(c.getColumnIndex(Z_REPORT_COLUMN_CREATEDATE))),
                c.getLong(c.getColumnIndex(Z_REPORT_COLUMN_BYUSER)),
                c.getLong(c.getColumnIndex(Z_REPORT_COLUMN_STARTSALEID)),
                c.getLong(c.getColumnIndex(Z_REPORT_COLUMN_ENDSALEID)));
    }

}
