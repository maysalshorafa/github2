package com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;

import java.util.ArrayList;
import java.util.List;

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
        List<CurrencyType> currencyTypes = new ArrayList<CurrencyType>();
        Cursor cursor = db.rawQuery("select * from " + CurrencyType_TABLE_NAME , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            currencyTypes.add(createNewUser(cursor));
            cursor.moveToNext();
        }
        return currencyTypes;
    }

    private CurrencyType createNewUser(Cursor cursor){
        return new CurrencyType(Long.parseLong(cursor.getString(cursor.getColumnIndex(CurrencyType_COLUMN_ID)))
                , cursor.getString(cursor.getColumnIndex(CurrencyType_COLUMN_Name)));
    }
}
