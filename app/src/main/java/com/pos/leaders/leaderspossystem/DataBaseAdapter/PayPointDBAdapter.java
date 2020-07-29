package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.PayPoint;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 7/27/2020.
 */

public class PayPointDBAdapter {
    protected static final String PAY_POINT_TABLE_NAME = "PayPoint";
    // Column Names
    protected static final String PAY_POINT_COLUMN_ID = "id";
    protected static final String PAY_POINT_COLUMN_OrderID = "orderId";
    protected static final String PAY_POINT_COLUMN_AMOUNT = "amount";
    protected static final String PAY_POINT_COLUMN_CurrencyType = "currency_type";
    protected static final String PAY_POINT_COLUMN_CREATEDATE = "createDate";
    protected static final String PAY_POINT_COLUMN_CurrencyRATE = "currencyRate";
    protected static final String PAY_POINT_COLUMN_ActualCurrencyRATE = "actualCurrencyRate";

    public static final String DATABASE_CREATE = "CREATE TABLE `PayPoint` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `orderId` INTEGER, `amount` REAL NOT NULL, `currency_type` INTEGER,'createDate'  TIMESTAMP DEFAULT current_timestamp, `currencyRate` REAL NOT NULL DEFAULT 0.0, `actualCurrencyRate` REAL NOT NULL DEFAULT 0.0)";    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public PayPointDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public PayPointDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(long saleId, double amount, long currency_type, Timestamp createDate, double currencyRate, double actualCurrencyRate) {

        if (db.isOpen()) {

        } else {
            try {
                open();
            } catch (SQLException ex) {
                Log.d("Exception", ex.toString());
            }

        }
        PayPoint payment = new PayPoint(Util.idHealth(this.db, PAY_POINT_TABLE_NAME, PAY_POINT_COLUMN_ID), saleId, amount, currency_type, createDate, currencyRate, actualCurrencyRate);
        //    sendToBroker(MessageType.ADD_CASH_PAYMENT, payment, this.context);
        try {
            close();
            return insertEntry(payment);

        } catch (SQLException ex) {
            Log.e("PayPoint DB insert", "inserting Entry at " + PAY_POINT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(PayPoint payment) {
        if (db.isOpen()) {

        } else {
            try {
                open();
            } catch (SQLException ex) {
                Log.d("Exception", ex.toString());
            }
        }
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(PAY_POINT_COLUMN_ID, payment.getPayPointId());
        val.put(PAY_POINT_COLUMN_OrderID, payment.getOrderId());
        val.put(PAY_POINT_COLUMN_AMOUNT, payment.getAmount());
        val.put(PAY_POINT_COLUMN_CurrencyRATE, payment.getCurrency_type());
        val.put(PAY_POINT_COLUMN_CurrencyRATE, payment.getCurrencyRate());
        val.put(PAY_POINT_COLUMN_ActualCurrencyRATE, payment.getActualCurrencyRate());

        try {
            return db.insert(PAY_POINT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Payment DB insert", "inserting Entry at " + PAY_POINT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntryDuplicate(CashPayment payment) {
        if (db.isOpen()) {

        } else {
            try {
                open();
            } catch (SQLException ex) {
                Log.d("Exception", ex.toString());
            }
        }
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(PAY_POINT_COLUMN_ID, Util.idHealth(this.db, PAY_POINT_TABLE_NAME, PAY_POINT_COLUMN_ID));
        val.put(PAY_POINT_COLUMN_OrderID, payment.getOrderId());
        val.put(PAY_POINT_COLUMN_AMOUNT, payment.getAmount());
        val.put(PAY_POINT_COLUMN_CurrencyType, payment.getCurrency_type());
        val.put(PAY_POINT_COLUMN_CurrencyRATE, payment.getCurrencyRate());
        val.put(PAY_POINT_COLUMN_ActualCurrencyRATE, payment.getActualCurrencyRate());

        try {
            return db.insert(PAY_POINT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Payment DB insert", "inserting Entry at " + PAY_POINT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }


    public List<PayPoint> getPaymentBySaleID(long orderId) {
        List<PayPoint> salePaymentList = new ArrayList<PayPoint>();
        try {
            if (db.isOpen()) {

            } else {
                try {
                    open();
                } catch (SQLException ex) {
                    Log.d("Exception", ex.toString());
                }
            }

            Cursor cursor = db.rawQuery("select * from " + PAY_POINT_TABLE_NAME + " where " + PAY_POINT_COLUMN_OrderID + "=" + orderId, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                salePaymentList.add(make(cursor));
                cursor.moveToNext();
            }
            close();
        } catch (Exception e) {
            Log.d("exception", e.toString());

        }
        return salePaymentList;
    }

    private PayPoint make(Cursor cursor) {
        return new PayPoint(Long.parseLong(cursor.getString(cursor.getColumnIndex(PAY_POINT_COLUMN_ID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PAY_POINT_COLUMN_OrderID))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PAY_POINT_COLUMN_AMOUNT))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PAY_POINT_COLUMN_CurrencyType))),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(PAY_POINT_COLUMN_CREATEDATE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PAY_POINT_COLUMN_CurrencyRATE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PAY_POINT_COLUMN_ActualCurrencyRATE))));
    }
}