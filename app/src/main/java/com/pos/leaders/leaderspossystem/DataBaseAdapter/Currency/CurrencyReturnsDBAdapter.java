package com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 9/28/2017.
 */

public class CurrencyReturnsDBAdapter {
    protected static final String CurrencyReturnsDBAdapterTabelName = "CurrencyReturns";
    // Column Names
    protected static final String CurrencyReturns_COLUMN_ID = "id";
    protected static final String CurrencyReturns_COLUMN_SALEID = "saleId";
    protected static final String CurrencyReturns_COLUMN_AMOUNT = "amount";
    protected static final String CurrencyReturns_COLUMN_CREATEDATE = "createDate";

    protected static final String CurrencyReturns_COLUMN_CurencyType = "currency_type";

    public static final String DATABASE_CREATE = "CREATE TABLE `CurrencyReturns` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `saleId` INTEGER, `amount` REAL NOT NULL,'createDate'  TEXT DEFAULT current_timestamp, `currency_type` INTEGER)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public CurrencyReturnsDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }
    public CurrencyReturnsDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(long saleId, double amount, Date createDate , long currency_type) {
        CurrencyReturns returns = new CurrencyReturns(Util.idHealth(this.db, CurrencyReturnsDBAdapterTabelName, CurrencyReturns_COLUMN_ID), saleId, amount,createDate, currency_type);
        sendToBroker(MessageType.ADD_CURRENCY_RETURN, returns, this.context);

        try {
            return insertEntry(returns);
        } catch (SQLException ex) {
            Log.e("CurrencyReturn DB insert", "inserting Entry at " + CurrencyReturnsDBAdapterTabelName + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(CurrencyReturns returns){
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(CurrencyReturns_COLUMN_ID, returns.getId());


        val.put(CurrencyReturns_COLUMN_SALEID, returns.getSaleId());
        val.put(CurrencyReturns_COLUMN_AMOUNT,returns.getAmount() );
        val.put(CurrencyReturns_COLUMN_CREATEDATE, String.valueOf(returns.getCreateDate()));

        val.put(CurrencyReturns_COLUMN_CurencyType, returns.getCurrency_type());
        try {
            return db.insert(CurrencyReturnsDBAdapterTabelName, null, val);
        } catch (SQLException ex) {
            Log.e("CurrencyREturns DB insert", "inserting Entry at " + CurrencyReturnsDBAdapterTabelName + ": " + ex.getMessage());
            return -1;
        }
    }

    public List<CurrencyReturns> getAllReturns() {
        List<CurrencyReturns> Returns = new ArrayList<CurrencyReturns>();

        Cursor cursor = db.rawQuery("select * from " + CurrencyReturnsDBAdapterTabelName, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Returns.add(make(cursor));
            cursor.moveToNext();
        }

        return Returns;
    }

    public List<CurrencyReturns> getCurencyReturnBySaleID(long saleID) {
        List<CurrencyReturns> saleReturns = new ArrayList<CurrencyReturns>();

        Cursor cursor = db.rawQuery("select * from " + CurrencyReturnsDBAdapterTabelName +" where "+CurrencyReturns_COLUMN_SALEID+"="+saleID, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            saleReturns.add(make(cursor));
            cursor.moveToNext();
        }

        return saleReturns;
    }

    private CurrencyReturns make(Cursor cursor){
        return new CurrencyReturns(Long.parseLong(cursor.getString(cursor.getColumnIndex(CurrencyReturns_COLUMN_ID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CurrencyReturns_COLUMN_SALEID))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CurrencyReturns_COLUMN_AMOUNT))),
                DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(CurrencyReturns_COLUMN_CREATEDATE))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CurrencyReturns_COLUMN_CurencyType))));
    }

    public double getSumOftype(long currencyType, long from,long to) {
        double total=0;
        Cursor cur = db.rawQuery("SELECT SUM(amount) from " +  CurrencyReturnsDBAdapterTabelName + "  where "+ CurrencyReturns_COLUMN_CurencyType+"=" + currencyType +" and " + CurrencyReturns_COLUMN_SALEID +" <= " + to + " and " + CurrencyReturns_COLUMN_SALEID +" >= "+from, null);
        if(cur.moveToFirst()){
            total =  cur.getDouble(0);// get final total
        }
        return total;
    }
}
