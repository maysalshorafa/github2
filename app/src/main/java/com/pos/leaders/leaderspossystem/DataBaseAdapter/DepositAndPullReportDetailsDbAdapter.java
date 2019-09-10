package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.DepositAndPullDetailsReport;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by Win8.1 on 9/8/2019.
 */

public class DepositAndPullReportDetailsDbAdapter {
    // Table Name
    public static final String DEPOSIT_AND_PULL_DETAILS_REPORT_TABLE_NAME = "depositAndPull";
    // Column Names
    protected static final String DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_ID = "id";
    protected static final String DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_DEPOSIT_AND_PULL_ID = "depositAndPullId";
    protected static final String DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_AMOUNT = "amount";
    protected static final String DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_CURRENCY_TYPE = "currency_type";

    public static final String DATABASE_CREATE = "CREATE TABLE "+ DEPOSIT_AND_PULL_DETAILS_REPORT_TABLE_NAME
            +" ( `"+ DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_ID +"` INTEGER PRIMARY KEY AUTOINCREMENT, `"+ DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_DEPOSIT_AND_PULL_ID +"` INTEGER , `"+
            DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_AMOUNT +"` REAL,  `"+ DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_CURRENCY_TYPE +"` TEXT , " + ")";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public DepositAndPullReportDetailsDbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public DepositAndPullReportDetailsDbAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(Timestamp createDate, long byUser, double amount, String type, long lastZReportID) {
        DepositAndPullDetailsReport depositAndPullReport = new DepositAndPullDetailsReport(Util.idHealth(this.db,DEPOSIT_AND_PULL_REPORT_TABLE_NAME, DEPOSIT_AND_PULL_REPORT_COLUMN_ID), createDate, byUser, amount,type,lastZReportID);
        sendToBroker(MessageType.ADD_DEPOSIT_AND_PULL_REPORT, depositAndPullReport, this.context);
        try {
            return insertEntry(depositAndPullReport);
        } catch (SQLException ex) {
            Log.e(" DB insert", "inserting Entry at " + DEPOSIT_AND_PULL_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public long insertEntry(DepositAndPullReport depositAndPullReport) {
        ContentValues val = new ContentValues();

        val.put(DEPOSIT_AND_PULL_REPORT_COLUMN_ID, depositAndPullReport.getDepositAndPullReportId());
        val.put(DEPOSIT_AND_PULL_REPORT_COLUMN_BY_USER, depositAndPullReport.getByUserID());
        val.put(DEPOSIT_AND_PULL_REPORT_COLUMN_AMOUNT, depositAndPullReport.getAmount());
        val.put(DEPOSIT_AND_PULL_REPORT_COLUMN_AMOUNT, depositAndPullReport.getType());
        val.put(DEPOSIT_AND_PULL_REPORT_COLUMN_CREATE_DATE, String.valueOf(depositAndPullReport.getCreatedAt()));
        val.put(DEPOSIT_AND_PULL_REPORT_COLUMN_LASTZREPORTID,depositAndPullReport.getLastZReportID());
        try {
            return db.insert(DEPOSIT_AND_PULL_REPORT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(" DB insert", "inserting Entry at " + DEPOSIT_AND_PULL_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public DepositAndPullReport getByLastZReport(long lastZReportID){
        DepositAndPullReport depositAndPullReport;
        Cursor cursor = db.rawQuery("select * from " + DEPOSIT_AND_PULL_REPORT_TABLE_NAME + " where " + DEPOSIT_AND_PULL_REPORT_COLUMN_LASTZREPORTID + "='" + (lastZReportID ) + "'", null);
        if (cursor.getCount() < 1) {
            //cursor.close();
            cursor = db.rawQuery("select * from " + DEPOSIT_AND_PULL_REPORT_TABLE_NAME,null);
            //return null;
        }
        cursor.moveToFirst();
        depositAndPullReport = makeDepositAndPullReport(cursor);
        return depositAndPullReport;
    }
    public List<DepositAndPullReport> getListByLastZReport(long lastZReportID){
        List<DepositAndPullReport> depositAndPullReportList = new ArrayList<DepositAndPullReport>();
        Cursor cursor = db.rawQuery("select * from " + DEPOSIT_AND_PULL_REPORT_TABLE_NAME + " where " + DEPOSIT_AND_PULL_REPORT_COLUMN_LASTZREPORTID + "='" + (lastZReportID ) + "'", null);
        if (cursor.getCount() < 1) {
            //cursor.close();
            cursor = db.rawQuery("select * from " + DEPOSIT_AND_PULL_REPORT_TABLE_NAME,null);
            //return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            depositAndPullReportList.add(makeDepositAndPullReport(cursor));
            cursor.moveToNext();
        }

        return depositAndPullReportList;
    }
    public List<DepositAndPullReport> getBetween(Date fromDate, Date toDate){
        List<DepositAndPullReport> depositAndPullReportList = new ArrayList<DepositAndPullReport>();

        Cursor cursor = db.rawQuery("select * from " + DEPOSIT_AND_PULL_REPORT_TABLE_NAME + " where " + DEPOSIT_AND_PULL_REPORT_COLUMN_CREATE_DATE + "<='" + toDate.getTime() + "' and " + DEPOSIT_AND_PULL_REPORT_COLUMN_CREATE_DATE +
                ">='" + fromDate.getTime() + "'" + " order by " + DEPOSIT_AND_PULL_REPORT_COLUMN_ID + " desc", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            depositAndPullReportList.add(makeDepositAndPullReport(cursor));
            cursor.moveToNext();
        }

        return depositAndPullReportList;
    }

    public DepositAndPullReport getLastRow() throws Exception {
        DepositAndPullReport depositAndPullReport = null;
        Cursor cursor = db.rawQuery("select * from " + DEPOSIT_AND_PULL_REPORT_TABLE_NAME + " where id like '%"+ SESSION.POS_ID_NUMBER+"%' order by id desc", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
            throw new Exception("there is no rows on A Report Table");
        }
        cursor.moveToFirst();
        depositAndPullReport = makeDepositAndPullReport(cursor);
        cursor.close();

        return depositAndPullReport;
    }

    private DepositAndPullReport makeDepositAndPullReport(Cursor c){
        return new DepositAndPullReport(c.getLong(c.getColumnIndex(DEPOSIT_AND_PULL_REPORT_COLUMN_ID)),
                Timestamp.valueOf(c.getString(c.getColumnIndex(DEPOSIT_AND_PULL_REPORT_COLUMN_CREATE_DATE))),
                c.getLong(c.getColumnIndex(DEPOSIT_AND_PULL_REPORT_COLUMN_BY_USER)),
                c.getDouble(c.getColumnIndex(DEPOSIT_AND_PULL_REPORT_COLUMN_AMOUNT)),
                c.getString(c.getColumnIndex(DEPOSIT_AND_PULL_REPORT_COLUMN_TYPE)),
                c.getLong(c.getColumnIndex(DEPOSIT_AND_PULL_REPORT_COLUMN_LASTZREPORTID)));
    }
    public DepositAndPullReport getById(long id){
        DepositAndPullReport depositAndPullReport;
        Cursor cursor = db.rawQuery("select * from " + DEPOSIT_AND_PULL_REPORT_TABLE_NAME + " where " + DEPOSIT_AND_PULL_REPORT_COLUMN_ID + "='" + (id ) + "'", null);
        if (cursor.getCount() < 1) {
            //cursor.close();
            cursor = db.rawQuery("select * from " + DEPOSIT_AND_PULL_REPORT_TABLE_NAME,null);
            //return null;
        }
        cursor.moveToFirst();
        depositAndPullReport = makeDepositAndPullReport(cursor);
        return depositAndPullReport;
    }
}
