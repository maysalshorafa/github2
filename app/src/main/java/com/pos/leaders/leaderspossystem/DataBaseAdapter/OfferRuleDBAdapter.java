package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.OfferRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 23/10/2016.
 */

public class OfferRuleDBAdapter {
    // Table Name
    protected static final String OFFERROLL_TABLE_NAME = "offerRule";
    // Column Names
    protected static final String OFFERROLL_COLUMN_ID = "id";
    protected static final String OFFERROLL_COLUMN_NAME = "name";

    public static final String DATABASE_CREATE="CREATE TABLE offerRule ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT )";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public OfferRuleDBAdapter(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }

    public OfferRuleDBAdapter open() throws SQLException {
        this.db=dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public int insertEntry(String name) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(OFFERROLL_COLUMN_NAME, name);
        try {
            db.insert(OFFERROLL_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("OfferRuleDB insert", "insatring Entry at " + OFFERROLL_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    /**public OfferRule getOfferRuleByID(int id) {
        OfferRule offerRule = null;
        Cursor cursor = db.rawQuery("select * from " + OFFERROLL_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return offerRule;
        }
        cursor.moveToFirst();
        offerRule =new OfferRule(id,cursor.getString(cursor.getColumnIndex(OFFERROLL_COLUMN_NAME)));

        cursor.close();

        return offerRule;
    }*

    public void updateEntry(OfferRule offerRule) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(OFFERROLL_COLUMN_NAME, offerRule.getName());

        String where = OFFERROLL_COLUMN_ID + " = ?";
        db.update(OFFERROLL_TABLE_NAME, val, where, new String[]{offerRule.getId() + ""});
    }

    public List<OfferRule> getAllOfferRoll(){
        List<OfferRule> offerRuleList =new ArrayList<OfferRule>();

        Cursor cursor =  db.rawQuery( "select * from "+OFFERROLL_TABLE_NAME, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            offerRuleList.add(new OfferRule(Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFERROLL_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(OFFERROLL_COLUMN_NAME))));
            cursor.moveToNext();
        }

        return offerRuleList;
    }*/
}
