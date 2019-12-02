package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.DepositAndPullDetailsReport;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 9/8/2019.
 */

public class DepositAndPullReportDetailsDbAdapter {
    // Table Name
    public static final String DEPOSIT_AND_PULL_DETAILS_REPORT_TABLE_NAME = "depositAndPullDetails";
    // Column Names
    protected static final String DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_ID = "id";
    protected static final String DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_DEPOSIT_AND_PULL_ID = "depositAndPullId";
    protected static final String DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_AMOUNT = "amount";
    protected static final String DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_CURRENCY_TYPE = "currency_type";

    public static final String DATABASE_CREATE = "CREATE TABLE "+ DEPOSIT_AND_PULL_DETAILS_REPORT_TABLE_NAME
            +" ( `"+ DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_ID +"` INTEGER PRIMARY KEY AUTOINCREMENT, `"+ DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_DEPOSIT_AND_PULL_ID +"` INTEGER , `"+
            DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_AMOUNT +"` REAL,  `"+ DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_CURRENCY_TYPE +"` TEXT " + ")";
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

    public DepositAndPullReportDetailsDbAdapter open() {

            this.db = dbHelper.getWritableDatabase();
            return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(long depositAndPullId, double amount, String currencyType) {
        if(db.isOpen()){

        }else {
            open();
        }
        DepositAndPullDetailsReport depositAndPullReport = new DepositAndPullDetailsReport(Util.idHealth(this.db,DEPOSIT_AND_PULL_DETAILS_REPORT_TABLE_NAME, DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_ID), depositAndPullId, amount,currencyType);
        sendToBroker(MessageType.ADD_DEPOSIT_AND_PULL_DETAILS_REPORT, depositAndPullReport, this.context);
   close();
        return insertEntry(depositAndPullReport);

    }

    public long insertEntry(DepositAndPullDetailsReport depositAndPullReport) {
        if(db.isOpen()){

        }else {
            open();
        }
        ContentValues val = new ContentValues();

        val.put(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_ID, depositAndPullReport.getDepositAndPullDetailsId());
        val.put(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_DEPOSIT_AND_PULL_ID, depositAndPullReport.getDepositAndPullId());
        val.put(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_AMOUNT, depositAndPullReport.getAmount());
        val.put(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_CURRENCY_TYPE,depositAndPullReport.getCurrencyType());
        return db.insert(DEPOSIT_AND_PULL_DETAILS_REPORT_TABLE_NAME, null, val);
    }

    public DepositAndPullDetailsReport getDepositAndPullDetailsReportByID(long id) {
        DepositAndPullDetailsReport depositAndPullDetailsReport = null;
        Cursor cursor = db.rawQuery("select * from " + DEPOSIT_AND_PULL_DETAILS_REPORT_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return depositAndPullDetailsReport;
        }
        cursor.moveToFirst();
        depositAndPullDetailsReport = new DepositAndPullDetailsReport(id,  cursor.getLong(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_DEPOSIT_AND_PULL_ID)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_AMOUNT))),
                cursor.getString(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_CURRENCY_TYPE))
                );
        cursor.close();

        return depositAndPullDetailsReport;
    }
    public List<DepositAndPullDetailsReport> getListDepositAndPullReportReport(long aReportId){
        if(db.isOpen()){

        }else {
            open();
        }
        List<DepositAndPullDetailsReport> depositAndPullReportDetailsList =new ArrayList<DepositAndPullDetailsReport>();
        try {
            if(dbHelper==null) {
                open();
            }
        Cursor cursor =  db.rawQuery( "select * from "+DEPOSIT_AND_PULL_DETAILS_REPORT_TABLE_NAME +" where "+ DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_DEPOSIT_AND_PULL_ID +" = "+aReportId, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            depositAndPullReportDetailsList.add(makeDepositAndPullDetailsReportDetails(cursor));
            cursor.moveToNext();
        }
        close();
        } catch (Exception e) {
            Log.d("exxx",e.toString());
        }
        return depositAndPullReportDetailsList;
    }
    private DepositAndPullDetailsReport makeDepositAndPullDetailsReportDetails(Cursor cursor) {
        try {
            return new DepositAndPullDetailsReport( cursor.getLong(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_ID)),  cursor.getLong(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_DEPOSIT_AND_PULL_ID)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_AMOUNT))),
                    cursor.getString(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_CURRENCY_TYPE))
            );
        } catch (Exception ex) {
            return new DepositAndPullDetailsReport( cursor.getLong(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_ID)),  cursor.getLong(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_DEPOSIT_AND_PULL_ID)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_AMOUNT))),
                    cursor.getString(cursor.getColumnIndex(DEPOSIT_AND_PULL_DETAILS_REPORT_COLUMN_CURRENCY_TYPE))
            );
        }

    }
}
