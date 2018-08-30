package com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 9/27/2017.
 */

public class CashPaymentDBAdapter {
    protected static final String CashPAYMENT_TABLE_NAME = "CashPayment";
    // Column Names
    protected static final String CashPAYMENT_COLUMN_ID = "id";
    protected static final String CashPAYMENT_COLUMN_OrderID = "orderId";
    protected static final String CashPAYMENT_COLUMN_AMOUNT = "amount";
    protected static final String CashPAYMENT_COLUMN_CurrencyType = "currency_type";
    protected static final String CashPAYMENT_COLUMN_CREATEDATE = "createDate";


    public static final String DATABASE_CREATE = "CREATE TABLE `CashPayment` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `orderId` INTEGER, `amount` REAL NOT NULL, `currency_type` INTEGER,'createDate'  TIMESTAMP DEFAULT current_timestamp)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public CashPaymentDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }
    public CashPaymentDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(long saleId, double amount, long currency_type, Timestamp createDate) {
        CashPayment payment = new CashPayment(Util.idHealth(this.db, CashPAYMENT_TABLE_NAME, CashPAYMENT_COLUMN_ID), saleId, amount, currency_type,createDate);
    //    sendToBroker(MessageType.ADD_CASH_PAYMENT, payment, this.context);

        try {
            return insertEntry(payment);
        } catch (SQLException ex) {
            Log.e("Cash Payment DB insert", "inserting Entry at " + CashPAYMENT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    public long insertEntry(CashPayment payment){
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(CashPAYMENT_COLUMN_ID, payment.getCashPaymentId());
        val.put(CashPAYMENT_COLUMN_OrderID, payment.getOrderId());
        val.put(CashPAYMENT_COLUMN_AMOUNT,payment.getAmount() );
        val.put(CashPAYMENT_COLUMN_CurrencyType, payment.getCurrency_type());
        try {
            return db.insert(CashPAYMENT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Payment DB insert", "inserting Entry at " + CashPAYMENT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }



    public List<CashPayment> getAllPayments() {
        List<CashPayment> paymentsList = new ArrayList<CashPayment>();

        Cursor cursor = db.rawQuery("select * from " + CashPAYMENT_TABLE_NAME, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            paymentsList.add(make(cursor));
            cursor.moveToNext();
        }

        return paymentsList;
    }

    public List<CashPayment> getPaymentBySaleID(long orderId) {
        List<CashPayment> salePaymentList = new ArrayList<CashPayment>();

        Cursor cursor = db.rawQuery("select * from " + CashPAYMENT_TABLE_NAME +" where "+CashPAYMENT_COLUMN_OrderID+"="+orderId, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            salePaymentList.add(make(cursor));
            cursor.moveToNext();
        }

        return salePaymentList;
    }

    private CashPayment make(Cursor cursor){
        return new CashPayment(Long.parseLong(cursor.getString(cursor.getColumnIndex(CashPAYMENT_COLUMN_ID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CashPAYMENT_COLUMN_OrderID))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CashPAYMENT_COLUMN_AMOUNT))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CashPAYMENT_COLUMN_CurrencyType))),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(CashPAYMENT_COLUMN_CREATEDATE))));
    }
    public double getSumOfType(int currencyType, long from, long to) {
        double total=0;
        Cursor cur = db.rawQuery("SELECT SUM(amount) from " +  CashPAYMENT_TABLE_NAME + "  where "+ CashPAYMENT_COLUMN_CurrencyType +"=" + currencyType +" and " + CashPAYMENT_COLUMN_OrderID +" <= " + to + " and " + CashPAYMENT_COLUMN_OrderID +" >= "+from, null);
      if(cur.moveToFirst()){
          cur.getString(cur.getColumnIndex("amount"));
            // total = cur.getDouble(cur.getColumnIndex(0));// get final total
           }
            return total;

    }



}
