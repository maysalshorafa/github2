package com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 9/25/2017.
 */

public class CurrencyDBAdapter {

    public static final  String CURRENCY_TABLE_NAME = "Currency";

    // Column Names
    protected static final String CURRENCY_COLUMN_ID = "id";
    protected static final String CURRENCY_COLUMN_NAME = "name";

    protected static final String CURRENCY_COLUMN_CURRENCYCODE = "currency_code";
    protected static final String CURRENCY_COLUMN_COUNTRY = "country";
    protected static final String CURRENCYCOLUMN_RATE = "rate";
    protected static final String CURRENCYCOLUMN_CREATEDATE = "createDate";



    public static final String DATABASE_CREATE = "CREATE TABLE "+ CURRENCY_TABLE_NAME
            +" ( `"+ CURRENCY_COLUMN_ID +"` INTEGER PRIMARY KEY AUTOINCREMENT, `"+ CURRENCY_COLUMN_NAME +"` TEXT ,  `"
            + CURRENCY_COLUMN_CURRENCYCODE +"` TEXT, `" + CURRENCY_COLUMN_COUNTRY +"` TEXT, " +
            " `"+ CURRENCYCOLUMN_RATE +"` REAL,  `"  + CURRENCYCOLUMN_CREATEDATE +"` TIMESTAMP DEFAULT current_timestamp)";

    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public CurrencyDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public CurrencyDBAdapter open() throws SQLException {
        try {
            this.db = dbHelper.getWritableDatabase();
            return this;

        } catch (SQLException s) {
            new Exception("Error with DB Open");
            return this;

        }
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry(String name, String currency_code, String country, long rate,Timestamp createDate) {
        Currency currency = new Currency(Util.idHealth(this.db, CURRENCY_TABLE_NAME, CURRENCY_COLUMN_ID), name, currency_code, country,rate,createDate);
        sendToBroker(MessageType.ADD_CURRENCY, currency, this.context);

        try {
            return insertEntry(currency);
        } catch (SQLException ex) {
            Log.e("Currency DB insert", "inserting Entry at " + CURRENCY_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(Currency currency){
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(CURRENCY_COLUMN_ID, currency.getId());


        val.put(CURRENCY_COLUMN_NAME, currency.getName());
        val.put(CURRENCY_COLUMN_CURRENCYCODE, currency.getCurrencyCode() );
        val.put(CURRENCY_COLUMN_COUNTRY, currency.getCountry());
        val.put(CURRENCYCOLUMN_RATE, currency.getRate());

        try {
            return db.insert(CURRENCY_TABLE_NAME, null, val);
        } catch (Exception ex) {
            Log.e("Currency DB insert", "inserting Entry at " + CURRENCY_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    public int deleteEntry(long id) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.

        String where = CURRENCY_COLUMN_ID + " = ?";
        try {
            db.update(CURRENCY_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("Currency DB delete", "enable hide Entry at " + CURRENCY_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public void updateEntry(Currency currency) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CURRENCY_COLUMN_NAME, currency.getName());
        val.put(CURRENCY_COLUMN_COUNTRY, currency.getCountry());
        val.put(CURRENCY_COLUMN_CURRENCYCODE, currency.getCurrencyCode());
        val.put(CURRENCYCOLUMN_RATE, currency.getRate());

        String where = CURRENCY_COLUMN_ID + " = ?";
        db.update(CURRENCY_TABLE_NAME, val, where, new String[]{currency.getId() + ""});
    }

    public List<Currency> getAllCurrencyLastUpdate(List<CurrencyType> currency) {
        List<Currency> currencyList = new ArrayList<Currency>();
        Cursor cursor=null;
        String name="";
        for (int i=0;i<currency.size();i++) {
            name = currency.get(i).getType();
            cursor = db.rawQuery("select * from " + CURRENCY_TABLE_NAME + " where  "+ CURRENCY_COLUMN_CURRENCYCODE +"='" + name + "'" + " order by id desc LIMIT 1", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                currencyList.add(build(cursor));
                cursor.moveToNext();
            }
        }
        return currencyList;
    }

    private Currency build(Cursor cursor) {
        return new Currency(Long.parseLong(cursor.getString(cursor.getColumnIndex(CURRENCY_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CURRENCY_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(CURRENCY_COLUMN_CURRENCYCODE)),
                cursor.getString(cursor.getColumnIndex(CURRENCY_COLUMN_COUNTRY)),
                Double.parseDouble( cursor.getString(cursor.getColumnIndex(CURRENCYCOLUMN_RATE))),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(CURRENCYCOLUMN_CREATEDATE))));
    }
    public Currency getLastCurrency(){
        Currency currency = new Currency();
        Cursor cursor=null;
            cursor = db.rawQuery("SELECT * FROM "+CURRENCY_TABLE_NAME+" ORDER BY id DESC LIMIT 1", null);
        cursor.moveToFirst();
                currency=build(cursor);
        Log.d("Currency",currency.toString());
        return currency;
    }
    public void deleteCurrencyList(){
        db.execSQL("delete from "+ CURRENCY_TABLE_NAME);
        }

    public void deleteOldRate(List<CurrencyType> currency) {
        String name="";
        for (int i=0;i<currency.size();i++) {
            name = currency.get(i).getType();
            db.execSQL("delete from " + CURRENCY_TABLE_NAME + " where " + CURRENCY_COLUMN_ID + " not in ( select " + CURRENCY_COLUMN_ID + " from " + CURRENCY_TABLE_NAME + " where " + CURRENCY_COLUMN_CURRENCYCODE + "='" + name + "'" + " order by " + CURRENCY_COLUMN_ID + " desc LIMIT 1) AND currency_code ='"+name+"'");
        }

    }
    public Currency getCurrencyByCode(String code) {
        Currency currency = null;
        Cursor cursor = db.rawQuery("select * from " + CURRENCY_TABLE_NAME + " where currency_code='" + code + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return currency;
        }
        cursor.moveToFirst();
        currency =  new Currency(Long.parseLong(cursor.getString(cursor.getColumnIndex(CURRENCY_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CURRENCY_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(CURRENCY_COLUMN_CURRENCYCODE)),
                cursor.getString(cursor.getColumnIndex(CURRENCY_COLUMN_COUNTRY)),
                Double.parseDouble( cursor.getString(cursor.getColumnIndex(CURRENCYCOLUMN_RATE))),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(CURRENCYCOLUMN_CREATEDATE))));
        cursor.close();

        return currency;
    }

}
