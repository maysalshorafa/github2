package com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 9/25/2017.
 */

public class CurrencyTypeDBAdapter {
    private static final String LOG_TAG = "CurrencyType";
    // Table Name
    public static final String CurrencyType_TABLE_NAME = "CurrencyType";
    // Column Names
    protected static final String CurrencyType_COLUMN_ID = "id";
    protected static final String CurrencyType_COLUMN_Name= "type";

    public static final String DATABASE_CREATE = "CREATE TABLE `"+ CurrencyType_TABLE_NAME +"` ( `"+ CurrencyType_COLUMN_ID +"` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`"+ CurrencyType_COLUMN_Name +"` TEXT)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public CurrencyTypeDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public CurrencyTypeDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public List<CurrencyType> getAllCurrencyType() {
        if (db.isOpen()){

        }
        else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        List<CurrencyType> currencyTypes = new ArrayList<CurrencyType>();
        Cursor cursor = db.rawQuery("select * from " + CurrencyType_TABLE_NAME , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            currencyTypes.add(createNewCurrency(cursor));
            cursor.moveToNext();
        }
        return currencyTypes;
    }

    private CurrencyType createNewCurrency(Cursor cursor){
        if (db.isOpen()){

        }
        else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        return new CurrencyType(Long.parseLong(cursor.getString(cursor.getColumnIndex(CurrencyType_COLUMN_ID)))
                , cursor.getString(cursor.getColumnIndex(CurrencyType_COLUMN_Name)));
    }
    public void delete() {
        if (db.isOpen()){

        }
        else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        db.execSQL("delete from "+ CurrencyType_TABLE_NAME);
    }
    public long insertEntry(String name) {
        if (db.isOpen()){

        }
        else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        CurrencyType currencyType = new CurrencyType(Util.idHealth(this.db, CurrencyType_TABLE_NAME, CurrencyType_COLUMN_ID), name);
        CurrencyType boCurrencyType = currencyType;
        sendToBroker(MessageType.ADD_CURRENCY_TYPE, boCurrencyType, this.context);

        return 1;
    }

    public long insertEntry(CurrencyType currency){
        if (db.isOpen()){

        }
        else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(CurrencyType_COLUMN_ID, currency.getCurrencyTypeId());
        val.put(CurrencyType_COLUMN_Name, currency.getType());
        try {
            return db.insert(CurrencyType_TABLE_NAME, null, val);
        } catch (Exception ex) {
            Log.e("Currency DB insert", "inserting Entry at " + CurrencyType_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    public  long getCurrencyIdByType(String type){
        if (db.isOpen()){

        }
        else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        Cursor cursor=null;
        cursor = db.rawQuery("select * from " + CurrencyType_TABLE_NAME + " where  "+ CurrencyType_COLUMN_Name +"='" + type + "'" , null);
        cursor.moveToFirst();
        CurrencyType currencyType = createNewCurrency(cursor);
        return  currencyType.getCurrencyTypeId();
    }

    public void deleteOldRate(List<CurrencyType> currency) {
        if (db.isOpen()){

        }
        else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        String name="";
        for (int i=0;i<currency.size();i++) {
            name = currency.get(i).getType();
            db.execSQL("delete from " + CurrencyType_TABLE_NAME + " where " + CurrencyType_COLUMN_ID + " not in ( select " + CurrencyType_COLUMN_ID + " from " + CurrencyType_TABLE_NAME + " where " + CurrencyType_COLUMN_Name + "='" + name + "'" + " order by " + CurrencyType_COLUMN_ID + " desc LIMIT 1) AND type ='"+name+"'");
        }

    }
}

