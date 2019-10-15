package com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 9/24/2017.
 */

public class CurrencyOperationDBAdapter {
    // Table Name
    protected static final String CurrencyOperation_TABLE_NAME = "currency_operation";
    // Column Names
    protected static final String CurrencyOperation_COLUMN_ID = "id";
    protected static final String CurrencyOperation_COLUMN_CREATEDATE = "createDate";

    protected static final String CurrencyOperation_COLUMN_Operation_ID = "order_id";
    protected static final String CurrencyOperation_COLUMN_Operation_Type = "operation_type";
    protected static final String CurrencyOperationCOLUMN_AMOUNT = "amount";
    protected static final String CurrencyOperation_COLUMN_Currency_Type = "currency_type";
    protected static final String CurrencyOperation_COLUMN_PAYMENT_WAY = "payment_way";



    public static final String DATABASE_CREATE = "CREATE TABLE "+ CurrencyOperation_TABLE_NAME
            +" ( `"+ CurrencyOperation_COLUMN_ID +"` INTEGER PRIMARY KEY AUTOINCREMENT, `"+ CurrencyOperation_COLUMN_CREATEDATE +"` TIMESTAMP DEFAULT current_timestamp,  `"
            + CurrencyOperation_COLUMN_Operation_ID +"` INTEGER, `" + CurrencyOperation_COLUMN_Operation_Type +"` TEXT, " +
            " `"+ CurrencyOperationCOLUMN_AMOUNT +"` REAL,  `"+ CurrencyOperation_COLUMN_PAYMENT_WAY +"` TEXT,  `"+ CurrencyOperation_COLUMN_Currency_Type +"` TEXT)";

    public static final String DATABASE_UPDATE_FROM_V1_TO_V2[] = {"alter table currency_operation rename to currency_operation_v1;", DATABASE_CREATE + "; ",
            "insert into currency_operation (id,createDate,order_id,operation_type,amount,currency_type) " +
                    "select id,createDate,operation_id,operation_type,amount,currency_type from currency_operation_v1;"};

    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public CurrencyOperationDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public CurrencyOperationDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(Timestamp createDate, long operation_id, String operation_type, double amount, String currency_type,String paymentWay) {

        CurrencyOperation currency = new CurrencyOperation(Util.idHealth(this.db, CurrencyOperation_TABLE_NAME, CurrencyOperation_COLUMN_ID), createDate, operation_id,operation_type, amount, currency_type,paymentWay);
        sendToBroker(MessageType.ADD_CURRENCY_OPERATION, currency, this.context);

        try {
            long insertResult = insertEntry(currency);
            return insertResult;
        } catch (SQLException ex) {
            Log.d(CurrencyOperation_TABLE_NAME, "inserting Entry at " + CurrencyOperation_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }
    public long insertEntry(CurrencyOperation currency) {

        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(CurrencyOperation_COLUMN_ID,currency.getCurrencyOperationId());
        val.put(CurrencyOperation_COLUMN_CREATEDATE, String.valueOf(currency.getCreatedAt()));
        val.put(CurrencyOperation_COLUMN_Operation_ID, currency.getOperationId());
        val.put(CurrencyOperation_COLUMN_Operation_Type,currency.getOperationType());
        val.put(CurrencyOperationCOLUMN_AMOUNT, currency.getAmount());
        val.put(CurrencyOperation_COLUMN_Currency_Type, currency.getCurrencyType());
        val.put(CurrencyOperation_COLUMN_PAYMENT_WAY, currency.getPaymentWay());

        try {
            return db.insert(CurrencyOperation_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(CurrencyOperation_TABLE_NAME, "inserting Entry at " + CurrencyOperation_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    public long insertEntryDuplicate(CurrencyOperation currency) {

        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(CurrencyOperation_COLUMN_ID, Util.idHealth(this.db, CurrencyOperation_TABLE_NAME, CurrencyOperation_COLUMN_ID));
        val.put(CurrencyOperation_COLUMN_CREATEDATE, String.valueOf(currency.getCreatedAt()));
        val.put(CurrencyOperation_COLUMN_Operation_ID, currency.getOperationId());
        val.put(CurrencyOperation_COLUMN_Operation_Type,currency.getOperationType());
        val.put(CurrencyOperationCOLUMN_AMOUNT, currency.getAmount());
        val.put(CurrencyOperation_COLUMN_Currency_Type, currency.getCurrencyType());
        val.put(CurrencyOperation_COLUMN_PAYMENT_WAY, currency.getPaymentWay());

        try {
            sendToBroker(MessageType.ADD_CURRENCY_OPERATION, currency, this.context);
            return db.insert(CurrencyOperation_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(CurrencyOperation_TABLE_NAME, "inserting Entry at " + CurrencyOperation_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    public List<CurrencyOperation> getCurrencyOperationByOrderID(long orderId) {
        List<CurrencyOperation> saleReturns = new ArrayList<CurrencyOperation>();
        try {
            open();
            Cursor cursor = db.rawQuery("select * from " + CurrencyOperation_TABLE_NAME +" where "+CurrencyOperation_COLUMN_Operation_ID+"="+orderId, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                saleReturns.add(make(cursor));
                cursor.moveToNext();
            }
            close();

        } catch (Exception e) {

        }
        return saleReturns;
    }

    private CurrencyOperation make(Cursor cursor){
        return new CurrencyOperation(Long.parseLong(cursor.getString(cursor.getColumnIndex(CurrencyOperation_COLUMN_ID))),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(CurrencyOperation_COLUMN_CREATEDATE))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CurrencyOperation_COLUMN_Operation_ID))),
                cursor.getString(cursor.getColumnIndex(CurrencyOperation_COLUMN_Operation_Type)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CurrencyOperationCOLUMN_AMOUNT))),cursor.getString(cursor.getColumnIndex(CurrencyOperation_COLUMN_PAYMENT_WAY)),
                cursor.getString(cursor.getColumnIndex(CurrencyOperation_COLUMN_Currency_Type)));
    }



}
