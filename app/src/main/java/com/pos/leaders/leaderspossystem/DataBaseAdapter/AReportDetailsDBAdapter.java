package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.AReportDetails;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 12/3/2017.
 */

public class AReportDetailsDBAdapter {
    // Table Name
    protected static final String A_REPORT_DETAILS_TABLE_NAME = "a_report_details";
    // Column Names
    protected static final String A_REPORT_DETAILS_COLUMN_ID = "id";
    protected static final String A_REPORT_DETAILS_A_REPORT_COLUMN_ID = "a_report_id";
    protected static final String A_REPORT_DETAILS_COLUMN_AMOUNT = "amount";
    protected static final String A_REPORT_DETAILS_COLUMN_TYPE = "type";
    protected static final String A_REPORT_DETAILS_COLUMN_AMOUNT_IN_BASIC_CURRENCY = "amount_in_basic_currency";

    public static final String DATABASE_CREATE = "CREATE TABLE " + A_REPORT_DETAILS_TABLE_NAME
            + " ( `" + A_REPORT_DETAILS_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, `" + A_REPORT_DETAILS_A_REPORT_COLUMN_ID + "` INTEGER,  `"
            + A_REPORT_DETAILS_COLUMN_AMOUNT + "` REAL NOT NULL, " + " `" + A_REPORT_DETAILS_COLUMN_TYPE + "` INTEGER,  `" + A_REPORT_DETAILS_COLUMN_AMOUNT_IN_BASIC_CURRENCY + "` REAL NOT NULL , " +
            "FOREIGN KEY(`" + A_REPORT_DETAILS_A_REPORT_COLUMN_ID + "`) REFERENCES `a_report.id` )";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public AReportDetailsDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public AReportDetailsDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(long a_report_id, double amount, long type, double amount_in_basic_currency) {
        AReportDetails aReportDetails = new AReportDetails(Util.idHealth(this.db, A_REPORT_DETAILS_TABLE_NAME, A_REPORT_DETAILS_COLUMN_ID), a_report_id, amount, type, amount_in_basic_currency);
        sendToBroker(MessageType.ADD_A_REPORT_DETAILS, aReportDetails, this.context);
        try {
            return insertEntry(aReportDetails);
        } catch (SQLException ex) {
            Log.e(A_REPORT_DETAILS_TABLE_NAME + " DB insert", "inserting Entry at " + A_REPORT_DETAILS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public long insertEntry(AReportDetails aReportDetails) {
        ContentValues val = new ContentValues();

        val.put(A_REPORT_DETAILS_COLUMN_ID, aReportDetails.getId());
        val.put(A_REPORT_DETAILS_A_REPORT_COLUMN_ID, aReportDetails.getA_report_id());
        val.put(A_REPORT_DETAILS_COLUMN_AMOUNT, aReportDetails.getAmount());
        val.put(A_REPORT_DETAILS_COLUMN_TYPE, aReportDetails.getType());
        val.put(A_REPORT_DETAILS_COLUMN_AMOUNT_IN_BASIC_CURRENCY, aReportDetails.getAmount_in_basic_currency());

        try {
            return db.insert(A_REPORT_DETAILS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(A_REPORT_DETAILS_TABLE_NAME + " DB insert", "inserting Entry at " + A_REPORT_DETAILS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public AReportDetails getLastRow(int type,long aReportId) {
        AReportDetails aReportDetails = null;
        Cursor cursor = null;

        cursor = db.rawQuery("select * from " + A_REPORT_DETAILS_TABLE_NAME + " where  type='" + type +" and " + A_REPORT_DETAILS_A_REPORT_COLUMN_ID +" = " + aReportId + "'" + " order by id desc", null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return aReportDetails;
        }
        cursor.moveToFirst();
        aReportDetails = new AReportDetails(makeAReportDetails(cursor));

        cursor.close();
        return aReportDetails;
    }

    private AReportDetails makeAReportDetails(Cursor cursor) {
        try {
            return new AReportDetails(Long.parseLong(cursor.getString(cursor.getColumnIndex(A_REPORT_DETAILS_COLUMN_ID))), Long.parseLong(cursor.getString(cursor.getColumnIndex(A_REPORT_DETAILS_A_REPORT_COLUMN_ID))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(A_REPORT_DETAILS_COLUMN_AMOUNT))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(A_REPORT_DETAILS_COLUMN_TYPE))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(A_REPORT_DETAILS_COLUMN_AMOUNT_IN_BASIC_CURRENCY))));
        } catch (Exception ex) {
            return new AReportDetails(Long.parseLong(cursor.getString(cursor.getColumnIndex(A_REPORT_DETAILS_COLUMN_ID))), Long.parseLong(cursor.getString(cursor.getColumnIndex(A_REPORT_DETAILS_A_REPORT_COLUMN_ID))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(A_REPORT_DETAILS_COLUMN_AMOUNT))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(A_REPORT_DETAILS_COLUMN_TYPE))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(A_REPORT_DETAILS_COLUMN_AMOUNT_IN_BASIC_CURRENCY))));
        }

    }


}
