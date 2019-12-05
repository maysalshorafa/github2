package com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency;

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
                try {
                    this.db = dbHelper.getWritableDatabase();
                }catch (Exception e){
                    Log.d("eeee",e.toString());
                }

            return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public List<CurrencyType> getAllCurrencyType() {
        List<CurrencyType> currencyTypes = new ArrayList<CurrencyType>();
        try {
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
        Cursor cursor = db.rawQuery("select * from " + CurrencyType_TABLE_NAME , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            currencyTypes.add(createNewCurrency(cursor));
            cursor.moveToNext();
        }
            close();
        }
          catch (Exception e) {
              Log.d("CurrencyTypeEx",e.toString());
            }

        return currencyTypes;
    }

    private CurrencyType createNewCurrency(Cursor cursor){
        return new CurrencyType(Long.parseLong(cursor.getString(cursor.getColumnIndex(CurrencyType_COLUMN_ID)))
                , cursor.getString(cursor.getColumnIndex(CurrencyType_COLUMN_Name)));
    }
    public long insertEntry(String name) {
                CurrencyType currencyType = new CurrencyType(Util.idHealth(this.db, CurrencyType_TABLE_NAME, CurrencyType_COLUMN_ID), name);
              CurrencyType boCurrencyType = currencyType;
               sendToBroker(MessageType.ADD_CURRENCY_TYPE, boCurrencyType, this.context);

                       return 1;
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
        close();
        return  currencyType.getCurrencyTypeId();
    }


}
