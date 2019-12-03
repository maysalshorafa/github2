package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 31/10/2017.
 */

public class CreditCardPaymentDBAdapter {
    private static final String TABLE_NAME = "CreditCardPayment";
    // Column Names
    private static final String ID = "id";
    private static final String ORDERID = "orderId";
    private static final String AMOUNT = "amount";
    private static final String CCC_NAME = "cccName";
    private static final String TRANSACTION_TYPE = "transactionType";
    private static final String LAST_4DIGITS = "last4Digits";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String ANSWER = "answer";

    private static final String PAYMENTS_NUMBER = "paymentsNumber";
    private static final String FIRST_PAYMENT_AMOUNT = "firstPaymentAmount";
    private static final String OTHER_PAYMENT_AMOUNT = "otherPaymentAmount";
    private static final String CARDHOLDER = "cardholder";

    private static final String CREATEDATE = "createDate";


    public static final String DATABASE_CREATE = "CREATE TABLE `" + TABLE_NAME + "` ( `" + ID + "` INTEGER PRIMARY KEY AUTOINCREMENT," +
            " `" + ORDERID + "` INTEGER, `" + AMOUNT + "` REAL NOT NULL," +
            " `" + CCC_NAME + "` TEXT, `" + TRANSACTION_TYPE + "` INTEGER," +
            " `" + LAST_4DIGITS + "` TEXT,`" + TRANSACTION_ID + "` TEXT, `" + ANSWER + "` TEXT," +
            " `" + PAYMENTS_NUMBER + "` INTEGER,`" + FIRST_PAYMENT_AMOUNT + "` REAL, `" + OTHER_PAYMENT_AMOUNT + "` REAL," +
            " `" + CARDHOLDER + "` TEXT,`" + CREATEDATE + "`  TIMESTAMP DEFAULT current_timestamp)";

    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public CreditCardPaymentDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public CreditCardPaymentDBAdapter open() throws SQLException {
            this.db = dbHelper.getWritableDatabase();
            return this;

    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(long saleId, double amount, String cccName,int transactionType,
                            String last4Digit,String transactionId,String answer,int paymentsNumber,
                            double firstPaymentAmount,double otherPaymentAmount,String cardHolder) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        CreditCardPayment payment = new CreditCardPayment(Util.idHealth(this.db, TABLE_NAME, ID), saleId, amount, cccName,transactionType,
                last4Digit,transactionId,answer,paymentsNumber,firstPaymentAmount,otherPaymentAmount,cardHolder,new Timestamp(System.currentTimeMillis()));
        //sendToBroker(MessageType.ADD_CREDIT_CARD_PAYMENT, payment, this.context);

        try {
            close();
            return insertEntry(payment);
        } catch (SQLException ex) {
            Log.e(TABLE_NAME + " DB insert", "inserting Entry at " + TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(CreditCardPayment p){
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(ID, p.getCreditCardPaymentId());


        val.put(ORDERID, p.getOrderId());
        val.put(AMOUNT, p.getAmount());
        val.put(CCC_NAME, p.getCreditCardCompanyName());
        val.put(TRANSACTION_TYPE, p.getTransactionType());
        val.put(LAST_4DIGITS, p.getLast4Digits());
        val.put(TRANSACTION_ID, p.getTransactionId());
        val.put(ANSWER, p.getAnswer());

        val.put(PAYMENTS_NUMBER, p.getPaymentsNumber());
        val.put(FIRST_PAYMENT_AMOUNT, p.getFirstPaymentAmount());
        val.put(OTHER_PAYMENT_AMOUNT, p.getOtherPaymentAmount());
        val.put(CARDHOLDER, p.getCardholder());


        try {
            return db.insert(TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("DB insert", "inserting Entry at " + TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public CreditCardPayment getCreditCardPaymentByID(long id){
        CreditCardPayment creditCardPayment = null;
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + ID + "='" + id + "'", null);
        if (cursor.getCount() < 1)
        {
            cursor.close();
            return creditCardPayment;
        }
        cursor.moveToFirst();
        creditCardPayment = make(cursor);
        cursor.close();

        return creditCardPayment;
    }

    public CreditCardPayment getCreditCardPaymentBySaleID(long saleId){
        CreditCardPayment creditCardPayment = null;
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + ORDERID + "='" + saleId + "'", null);
        if (cursor.getCount() < 1)
        {
            cursor.close();
            return creditCardPayment;
        }
        cursor.moveToFirst();
        creditCardPayment = make(cursor);
        cursor.close();

        return creditCardPayment;
    }

    private CreditCardPayment make(Cursor cursor){
        return new CreditCardPayment(cursor.getLong(cursor.getColumnIndex(ID)), cursor.getLong(cursor.getColumnIndex(ORDERID)),
                cursor.getDouble(cursor.getColumnIndex(AMOUNT)), cursor.getString(cursor.getColumnIndex(CCC_NAME)), cursor.getInt(cursor.getColumnIndex(TRANSACTION_TYPE)),
                cursor.getString(cursor.getColumnIndex(LAST_4DIGITS)), cursor.getString(cursor.getColumnIndex(TRANSACTION_ID)), cursor.getString(cursor.getColumnIndex(ANSWER)),
                cursor.getInt(cursor.getColumnIndex(PAYMENTS_NUMBER)), cursor.getDouble(cursor.getColumnIndex(FIRST_PAYMENT_AMOUNT)), cursor.getDouble(cursor.getColumnIndex(OTHER_PAYMENT_AMOUNT)),
                cursor.getString(cursor.getColumnIndex(CARDHOLDER)),Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(CREATEDATE))));
    }
    public List<CreditCardPayment> getPaymentByOrderID(long orderId) {
        List<CreditCardPayment> orderPaymentList = new ArrayList<CreditCardPayment>();
        try {
            if(db.isOpen()){

            }else {
                try {
                    open();
                }
                catch (SQLException ex) {
                    Log.d("Exception",ex.toString());
                }
            }
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME +" where "+ORDERID+"="+orderId, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            orderPaymentList.add(make(cursor));
            cursor.moveToNext();
        }
close();
        } catch (Exception e) {
            Log.d("exception",e.toString());

        }
        return orderPaymentList;
    }

}
