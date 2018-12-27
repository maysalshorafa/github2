package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.OpiningReportDetails;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 12/3/2017.
 */

public class OpiningReportDetailsDBAdapter {
    // Table Name
    protected static final String OPINING_REPORT_DETAILS_TABLE_NAME = "opining_report_details";
    // Column Names
    protected static final String OPINING_REPORT_DETAILS_COLUMN_ID = "id";
    protected static final String OPINING_REPORT_DETAILS_OPINING_REPORT_COLUMN_ID = "opining_report_id";
    protected static final String OPINING_REPORT_DETAILS_COLUMN_AMOUNT = "amount";
    protected static final String OPINING_REPORT_DETAILS_COLUMN_TYPE = "type";
    protected static final String OPINING_REPORT_DETAILS_COLUMN_AMOUNT_IN_BASIC_CURRENCY = "amount_in_basic_currency";

    public static final String DATABASE_CREATE = "CREATE TABLE " + OPINING_REPORT_DETAILS_TABLE_NAME
            + " ( `" + OPINING_REPORT_DETAILS_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, `" + OPINING_REPORT_DETAILS_OPINING_REPORT_COLUMN_ID + "` INTEGER,  `"
            + OPINING_REPORT_DETAILS_COLUMN_AMOUNT + "` REAL NOT NULL, " + " `" + OPINING_REPORT_DETAILS_COLUMN_TYPE + "` INTEGER,  `" + OPINING_REPORT_DETAILS_COLUMN_AMOUNT_IN_BASIC_CURRENCY + "` REAL NOT NULL , " +
            "FOREIGN KEY(`" + OPINING_REPORT_DETAILS_OPINING_REPORT_COLUMN_ID + "`) REFERENCES `a_report.id` )";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public OpiningReportDetailsDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public OpiningReportDetailsDBAdapter open() throws SQLException {
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
        OpiningReportDetails aReportDetails = new OpiningReportDetails(Util.idHealth(this.db, OPINING_REPORT_DETAILS_TABLE_NAME, OPINING_REPORT_DETAILS_COLUMN_ID), a_report_id, amount, type, amount_in_basic_currency);
        sendToBroker(MessageType.ADD_OPINING_REPORT_DETAILS, aReportDetails, this.context);
        try {
            return insertEntry(aReportDetails);
        } catch (SQLException ex) {
            Log.e(" DB insert", "inserting Entry at " + OPINING_REPORT_DETAILS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public long insertEntry(OpiningReportDetails aReportDetails) {
        ContentValues val = new ContentValues();

        val.put(OPINING_REPORT_DETAILS_COLUMN_ID, aReportDetails.getOpiningReportDetailsId());
        val.put(OPINING_REPORT_DETAILS_OPINING_REPORT_COLUMN_ID, aReportDetails.getOpiningReportId());
        val.put(OPINING_REPORT_DETAILS_COLUMN_AMOUNT, aReportDetails.getAmount());
        val.put(OPINING_REPORT_DETAILS_COLUMN_TYPE, aReportDetails.getType());
        val.put(OPINING_REPORT_DETAILS_COLUMN_AMOUNT_IN_BASIC_CURRENCY, aReportDetails.getAmount_in_basic_currency());

        try {
            return db.insert(OPINING_REPORT_DETAILS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e( " DB insert", "inserting Entry at " + OPINING_REPORT_DETAILS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public Double getLastRow(int type,long aReportId) {
        OpiningReportDetails aReportDetails = null;
        Cursor cursor = db.rawQuery("SELECT * from " + OPINING_REPORT_DETAILS_TABLE_NAME + "  where "+ OPINING_REPORT_DETAILS_COLUMN_TYPE +"=" + type +" and " + OPINING_REPORT_DETAILS_OPINING_REPORT_COLUMN_ID +" = " + aReportId , null);
        if (cursor.getCount() < 1) {
            cursor.close();
            return 0.0;
        }
        cursor.moveToFirst();
        aReportDetails = new OpiningReportDetails(makeAReportDetails(cursor));

        cursor.close();
        return aReportDetails.getAmount();
    }

    private OpiningReportDetails makeAReportDetails(Cursor cursor) {
        try {
            return new OpiningReportDetails(Long.parseLong(cursor.getString(cursor.getColumnIndex(OPINING_REPORT_DETAILS_COLUMN_ID))), Long.parseLong(cursor.getString(cursor.getColumnIndex(OPINING_REPORT_DETAILS_OPINING_REPORT_COLUMN_ID))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(OPINING_REPORT_DETAILS_COLUMN_AMOUNT))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(OPINING_REPORT_DETAILS_COLUMN_TYPE))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(OPINING_REPORT_DETAILS_COLUMN_AMOUNT_IN_BASIC_CURRENCY))));
        } catch (Exception ex) {
            return new OpiningReportDetails(Long.parseLong(cursor.getString(cursor.getColumnIndex(OPINING_REPORT_DETAILS_COLUMN_ID))), Long.parseLong(cursor.getString(cursor.getColumnIndex(OPINING_REPORT_DETAILS_OPINING_REPORT_COLUMN_ID))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(OPINING_REPORT_DETAILS_COLUMN_AMOUNT))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(OPINING_REPORT_DETAILS_COLUMN_TYPE))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(OPINING_REPORT_DETAILS_COLUMN_AMOUNT_IN_BASIC_CURRENCY))));
        }

    }
    public List<OpiningReportDetails> getListOpiningReport(long aReportId){
        List<OpiningReportDetails> opiningReportDetailsList =new ArrayList<OpiningReportDetails>();
        Cursor cursor =  db.rawQuery( "select * from "+OPINING_REPORT_DETAILS_TABLE_NAME +" where "+ OPINING_REPORT_DETAILS_OPINING_REPORT_COLUMN_ID +" = "+aReportId, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            opiningReportDetailsList.add(makeAReportDetails(cursor));
            cursor.moveToNext();
        }

        return opiningReportDetailsList;
    }


}
