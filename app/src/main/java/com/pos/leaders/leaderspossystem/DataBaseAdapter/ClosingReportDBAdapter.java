package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.ClosingReport;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 11/19/2018.
 */

public class ClosingReportDBAdapter {
    public static final String CLOSING_REPORT_TABLE_NAME = "closing_report";
    // Column Names
    protected static final String CLOSING_REPORT_COLUMN_ID = "id";
    protected static final String CLOSING_REPORT_COLUMN_ACTUAL_TOTAL_VALUE = "actualTotalValue";
    protected static final String CLOSING_REPORT_COLUMN_EXPECTED_TOTAL_VALUE = "expectedTotalValue";
    protected static final String CLOSING_REPORT_COLUMN_DIFFERENT_TOTAL_VALUE = "differentTotalValue";
    protected static final String CLOSING_REPORT_COLUMN_CREATEDAT = "createdAt";
    protected static final String CLOSING_REPORT_COLUMN_OPINING_REPORT_ID = "opiningReportId";
    protected static final String CLOSING_REPORT_COLUMN_LAST_ORDER_ID = "lastOrderId";
    protected static final String CLOSING_REPORT_COLUMN_BY_USER = "byUser";


    public static final String DATABASE_CREATE = "CREATE TABLE closing_report ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`actualTotalValue` REAL,  " +
            "`expectedTotalValue` REAL, " +
            "`differentTotalValue` REAL," +
            "`createdAt` TIMESTAMP NOT NULL DEFAULT current_timestamp, " +
            "`lastOrderId` INTEGER , " +
            "`opiningReportId` INTEGER , " +
            "`byUser` INTEGER)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public ClosingReportDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public ClosingReportDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry(double actualTotalValue,double expectedTotalValue ,double differentTotalValue ,Timestamp createdAt , long  opiningReportId,long lastOrderId ,long byUser) {
        ClosingReport closingReport = new ClosingReport(Util.idHealth(this.db, CLOSING_REPORT_TABLE_NAME, CLOSING_REPORT_COLUMN_ID), actualTotalValue,expectedTotalValue,differentTotalValue, createdAt, opiningReportId,lastOrderId,byUser);
        sendToBroker(MessageType.ADD_CLOSING_REPORT, closingReport, this.context);

        try {
            return insertEntry(closingReport);
        } catch (SQLException ex) {
            Log.e("ClosingReport insert", "inserting Entry at " + CLOSING_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(ClosingReport closingReport) {
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(CLOSING_REPORT_COLUMN_ID, closingReport.getClosingReportId());
        val.put(CLOSING_REPORT_COLUMN_ACTUAL_TOTAL_VALUE, closingReport.getActualTotalValue());
        val.put(CLOSING_REPORT_COLUMN_EXPECTED_TOTAL_VALUE, closingReport.getExpectedTotalValue());
        val.put(CLOSING_REPORT_COLUMN_DIFFERENT_TOTAL_VALUE, closingReport.getDifferentTotalValue());
        val.put(CLOSING_REPORT_COLUMN_CREATEDAT, String.valueOf(closingReport.getCreatedAt()));
        val.put(CLOSING_REPORT_COLUMN_OPINING_REPORT_ID,closingReport.getOpiningReportId());
        val.put(CLOSING_REPORT_COLUMN_LAST_ORDER_ID,closingReport.getLastOrderId());
        val.put(CLOSING_REPORT_COLUMN_BY_USER,closingReport.getByUser());

        try {
            return db.insert(CLOSING_REPORT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("ClosingReport insert", "inserting Entry at " + CLOSING_REPORT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public ClosingReport getClosingReportByID(long id) {
        ClosingReport closingReport = null;
        Cursor cursor = db.rawQuery("select * from " + CLOSING_REPORT_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return closingReport;
        }
        cursor.moveToFirst();
        closingReport = new ClosingReport(id, Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_ACTUAL_TOTAL_VALUE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_EXPECTED_TOTAL_VALUE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_DIFFERENT_TOTAL_VALUE))),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_CREATEDAT))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_OPINING_REPORT_ID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_LAST_ORDER_ID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_BY_USER))));
        cursor.close();

        return closingReport;
    }
    public ClosingReport getClosingReportByOpiningReportId(long opiningReportId) {
        ClosingReport closingReport = null;
        Cursor cursor = db.rawQuery("select * from " + CLOSING_REPORT_TABLE_NAME + " where opiningReportId='" + opiningReportId + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return closingReport;
        }
        cursor.moveToFirst();
        closingReport = new ClosingReport(opiningReportId, Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_ACTUAL_TOTAL_VALUE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_EXPECTED_TOTAL_VALUE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_DIFFERENT_TOTAL_VALUE))),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_CREATEDAT))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_OPINING_REPORT_ID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_LAST_ORDER_ID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_BY_USER))));
        cursor.close();

        return closingReport;
    }
    public ClosingReport getLastRow() throws Exception {
        ClosingReport c = null;
        Cursor cursor = db.rawQuery("select * from " + CLOSING_REPORT_TABLE_NAME + " where id like '"+ SESSION.POS_ID_NUMBER+"%' order by id desc", null);
        if (cursor.getCount() < 1)
        {
            cursor.close();
            return c;
        }
        cursor.moveToFirst();
        c = makeCReport(cursor);
       // cursor.close();

        return c;
    }
    private ClosingReport makeCReport(Cursor cursor) {
        try {

                return new ClosingReport(Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_ID))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_ACTUAL_TOTAL_VALUE))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_EXPECTED_TOTAL_VALUE))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_DIFFERENT_TOTAL_VALUE))),
                    Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_CREATEDAT))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_OPINING_REPORT_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_LAST_ORDER_ID))),
                        Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_BY_USER))));
        } catch (Exception ex) {
            return new ClosingReport(
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_ID))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_ACTUAL_TOTAL_VALUE))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_EXPECTED_TOTAL_VALUE))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_DIFFERENT_TOTAL_VALUE))),
                    Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_CREATEDAT))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_OPINING_REPORT_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_LAST_ORDER_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_COLUMN_BY_USER))));

        }
    }
    public List<ClosingReport> getClosingReportByOpiningID(long opiningId) {
        List<ClosingReport> closingReportList = new ArrayList<ClosingReport>();
        try {
            open();
            Cursor cursor = db.rawQuery("select * from " + CLOSING_REPORT_TABLE_NAME +" where "+CLOSING_REPORT_COLUMN_OPINING_REPORT_ID+">="+opiningId, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                closingReportList.add(makeCReport(cursor));
                cursor.moveToNext();
            }
            close();

        } catch (Exception e) {

        }
        return closingReportList;
    }

}
