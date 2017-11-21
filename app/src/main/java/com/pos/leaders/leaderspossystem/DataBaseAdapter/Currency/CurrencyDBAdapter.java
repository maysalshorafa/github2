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
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 9/25/2017.
 */

public class CurrencyDBAdapter {

    public static final  String Currency_TABLE_NAME = "Currency";

    // Column Names
    protected static final String Currency_COLUMN_ID = "id";
    protected static final String Currency_COLUMN_Name = "name";

    protected static final String Currency_COLUMN_CurrencyCode = "currency_code";
    protected static final String Currency_COLUMN_Country = "country";
    protected static final String CurrencyCOLUMN_Rate = "rate";
    protected static final String CurrencyCOLUMN_createDate = "createDate";



    public static final String DATABASE_CREATE = "CREATE TABLE "+ Currency_TABLE_NAME
            +" ( `"+ Currency_COLUMN_ID +"` INTEGER PRIMARY KEY AUTOINCREMENT, `"+ Currency_COLUMN_Name +"` TEXT ,  `"
            + Currency_COLUMN_CurrencyCode +"` TEXT, `" + Currency_COLUMN_Country +"` TEXT, " +
            " `"+ CurrencyCOLUMN_Rate +"` INTEGER,  `"  + CurrencyCOLUMN_createDate +"` TEXT DEFAULT current_timestamp)";

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
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry(String name, String currency_code, String country, long rate,Date createDate) {
        Currency currency = new Currency(Util.idHealth(this.db, Currency_TABLE_NAME, Currency_COLUMN_ID), name, currency_code, country,rate,createDate);
        sendToBroker(MessageType.ADD_CURRENCY, currency, this.context);

        try {
            return insertEntry(currency);
        } catch (SQLException ex) {
            Log.e("Currency DB insert", "inserting Entry at " + Currency_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(Currency currency){
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(Currency_COLUMN_ID, currency.getId());


        val.put(Currency_COLUMN_Name, currency.getName());
        val.put(Currency_COLUMN_CurrencyCode, currency.getCurrency_code() );
        val.put(Currency_COLUMN_Country, currency.getCountry());
        val.put(CurrencyCOLUMN_Rate, currency.getRate());

        val.put(CurrencyCOLUMN_createDate, String.valueOf(currency.getCreateDate()));

        try {
            return db.insert(Currency_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Currency DB insert", "inserting Entry at " + Currency_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    public int deleteEntry(long id) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.

        String where = Currency_COLUMN_ID + " = ?";
        try {
            db.update(Currency_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("Currency DB delete", "enable hide Entry at " + Currency_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public void updateEntry(Currency currency) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Currency_COLUMN_Name, currency.getName());
        val.put(CurrencyCOLUMN_createDate, currency.getCreateDate().toString());
        val.put(Currency_COLUMN_Country, currency.getCountry());
        val.put(Currency_COLUMN_CurrencyCode, currency.getCurrency_code());
        val.put(CurrencyCOLUMN_Rate, currency.getRate());

        String where = Currency_COLUMN_ID + " = ?";
        db.update(Currency_TABLE_NAME, val, where, new String[]{currency.getId() + ""});
    }

    public List<Currency> getAllCurrency(List<CurrencyType> currency) {
        List<Currency> currencyList = new ArrayList<Currency>();
        Cursor cursor=null;
        String name="";
        for (int i=0; i<=currency.size()-1;i++) {
           name=currency.get(i).getType();
           cursor = db.rawQuery("select * from " + Currency_TABLE_NAME + " where  name='" + name + "'"+ " order by id desc", null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                currencyList.add(createNewCurrency(cursor));
                cursor.moveToNext();
            } }


        return currencyList;
    }
    private Currency createNewCurrency(Cursor cursor) {
        return new Currency(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Currency_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(Currency_COLUMN_Name)),
                cursor.getString(cursor.getColumnIndex(Currency_COLUMN_CurrencyCode)),
                cursor.getString(cursor.getColumnIndex(Currency_COLUMN_Country)),
                Double.parseDouble( cursor.getString(cursor.getColumnIndex(CurrencyCOLUMN_Rate))),  DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(CurrencyCOLUMN_createDate))));
    }

}
