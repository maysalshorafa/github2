package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 01/08/2017.
 */

public class Rule1DBAdapter {
    private static final String LOG_TAG = "Rule1DB";
    // Table Name
    public static final String RULE1_TABLE_NAME = "Rule1";
    // Column Names
    protected static final String RULE1_COLUMN_ID = "id";
    protected static final String RULE1_COLUMN_COUNT = "count";
    protected static final String RULE1_COLUMN_PRICE = "price";

    public static final String DATABASE_CREATE = "CREATE TABLE `"+RULE1_TABLE_NAME+"` ( `"+RULE1_COLUMN_ID+"` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`"+RULE1_COLUMN_COUNT+"` INTEGER NOT NULL, `"+RULE1_COLUMN_PRICE+"` REAL NOT NULL)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public Rule1DBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public Rule1DBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public int insertEntry(int count,double price) {
        ContentValues val = new ContentValues();

        //Assign values for each row.
        val.put(RULE1_COLUMN_COUNT, count);
        val.put(RULE1_COLUMN_PRICE, price);
        try {
            db.insert(RULE1_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "inserting Entry at " + RULE1_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public Rule1 getByID(int id) {
        Rule1 rule=null;

        Cursor cursor = db.rawQuery("select * from " + RULE1_TABLE_NAME+" where id="+id, null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        rule = new Rule1(id, Integer.parseInt(cursor.getString(cursor.getColumnIndex(RULE1_COLUMN_COUNT))),
                cursor.getDouble(cursor.getColumnIndex(RULE1_COLUMN_PRICE)));
        cursor.close();

        return rule;
    }

}
