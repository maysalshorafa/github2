package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.ClosingReportDetails;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 11/21/2018.
 */

public class ClosingReportDetailsDBAdapter {
    public static final String CLOSING_REPORT_DETAILS_TABLE_NAME = "closing_report_details";
    // Column Names
    protected static final String CLOSING_REPORT_DETAILS_COLUMN_ID = "id";
    protected static final String CLOSING_REPORT_DETAILS_COLUMN_CLOSING_REPORT_ID = "closingReportId";
    protected static final String CLOSING_REPORT_DETAILS_COLUMN_ACTUAL_VALUE = "actualValue";
    protected static final String CLOSING_REPORT_DETAILS_COLUMN_EXPECTED_VALUE = "expectedValue";
    protected static final String CLOSING_REPORT_DETAILS_COLUMN_DIFFERENT_VALUE = "differentValue";
    protected static final String CLOSING_REPORT_DETAILS_COLUMN_TYPE = "type";
    protected static final String CLOSING_REPORT_DETAILS_COLUMN_CURRENCY_TYPE = "currencyType";

    public static final String DATABASE_CREATE = "CREATE TABLE closing_report_details ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`actualValue` REAL,  " +
            "`expectedValue` REAL, " +
            "`differentValue` REAL," +
            "`type` TEXT , " +
            "`currencyType` TEXT , " +
            "`closingReportId` INTEGER)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public ClosingReportDetailsDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public ClosingReportDetailsDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry(long closingReportId ,double actualValue, double expectedValue , double differentValue , String type  ,String currencyType  ) {
        ClosingReportDetails closingReportDetails = new ClosingReportDetails(Util.idHealth(this.db, CLOSING_REPORT_DETAILS_TABLE_NAME, CLOSING_REPORT_DETAILS_COLUMN_ID),closingReportId, actualValue,expectedValue,differentValue, type, currencyType);
        if(Util.idHealth(this.db, CLOSING_REPORT_DETAILS_TABLE_NAME, CLOSING_REPORT_DETAILS_COLUMN_ID)>0){
            Log.d("ClosingReportDetails",closingReportDetails.toString());
            sendToBroker(MessageType.ADD_CLOSING_REPORT_DETAILS, closingReportDetails, this.context);
        }

        try {
            return insertEntry(closingReportDetails);
        } catch (SQLException ex) {
            Log.e("ClosingReportDetails", "inserting Entry at " + CLOSING_REPORT_DETAILS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(ClosingReportDetails closingReportDetails) {
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(CLOSING_REPORT_DETAILS_COLUMN_ID, closingReportDetails.getClosingReportDetailsId());
        val.put(CLOSING_REPORT_DETAILS_COLUMN_ACTUAL_VALUE, closingReportDetails.getActualValue());
        val.put(CLOSING_REPORT_DETAILS_COLUMN_EXPECTED_VALUE, closingReportDetails.getExpectedValue());
        val.put(CLOSING_REPORT_DETAILS_COLUMN_DIFFERENT_VALUE, closingReportDetails.getDifferentValue());
        val.put(CLOSING_REPORT_DETAILS_COLUMN_CLOSING_REPORT_ID,closingReportDetails.getClosingReportId());
        val.put(CLOSING_REPORT_DETAILS_COLUMN_TYPE,closingReportDetails.getType());
        val.put(CLOSING_REPORT_DETAILS_COLUMN_CURRENCY_TYPE,closingReportDetails.getCurrencyType());

        try {
            return db.insert(CLOSING_REPORT_DETAILS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("ClosingReportDetails ", "inserting Entry at " + CLOSING_REPORT_DETAILS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public ClosingReportDetails getClosingReporDetailsByID(long id) {
        ClosingReportDetails closingReportDetails = null;
        Cursor cursor = db.rawQuery("select * from " + CLOSING_REPORT_DETAILS_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return closingReportDetails;
        }
        cursor.moveToFirst();
        closingReportDetails = new ClosingReportDetails(id,  cursor.getLong(cursor.getColumnIndex(CLOSING_REPORT_DETAILS_COLUMN_CLOSING_REPORT_ID)), Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_DETAILS_COLUMN_ACTUAL_VALUE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_DETAILS_COLUMN_EXPECTED_VALUE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_DETAILS_COLUMN_DIFFERENT_VALUE))),
              cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_DETAILS_COLUMN_TYPE)),
              cursor.getString(cursor.getColumnIndex(CLOSING_REPORT_DETAILS_COLUMN_CURRENCY_TYPE)));
        cursor.close();

        return closingReportDetails;
    }
}
