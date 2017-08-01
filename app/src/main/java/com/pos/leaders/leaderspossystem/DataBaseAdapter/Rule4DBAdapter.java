package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule4;

/**
 * Created by KARAM on 01/08/2017.
 */

public class Rule4DBAdapter {
    private static final String LOG_TAG = "Rule2DB";
    // Table Name
    public static final String RULE2_TABLE_NAME = "Rule2";
    // Column Names
    protected static final String RULE2_COLUMN_ID = "id";
    protected static final String RULE2_COLUMN_QUANTITY = "quantity";
    protected static final String RULE2_COLUMN_DISCOUNTPRICE = "discountPrice";

    public static final String DATABASE_CREATE = "CREATE TABLE `"+ RULE2_TABLE_NAME +"` ( `"+ RULE2_COLUMN_ID +"` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`"+ RULE2_COLUMN_QUANTITY +"` INTEGER NOT NULL, `"+ RULE2_COLUMN_DISCOUNTPRICE +"` REAL NOT NULL)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public Rule4DBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public Rule4DBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public int insertEntry(int quantity,double discountPrice) {
        ContentValues val = new ContentValues();

        //Assign values for each row.
        val.put(RULE2_COLUMN_QUANTITY, quantity);
        val.put(RULE2_COLUMN_DISCOUNTPRICE, discountPrice);
        try {
            db.insert(RULE2_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "inserting Entry at " + RULE2_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public Rule4 getByID(int id) {
        Rule4 rule=null;

        Cursor cursor = db.rawQuery("select * from " + RULE2_TABLE_NAME +" where id="+id, null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        rule = new Rule4(id, Integer.parseInt(cursor.getString(cursor.getColumnIndex(RULE2_COLUMN_QUANTITY))),
                cursor.getFloat(cursor.getColumnIndex(RULE2_COLUMN_DISCOUNTPRICE)));
        cursor.close();

        return rule;
    }
}
