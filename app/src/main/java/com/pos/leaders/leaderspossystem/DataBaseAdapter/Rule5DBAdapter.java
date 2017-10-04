package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule5;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule7;
import com.pos.leaders.leaderspossystem.Tools.Util;

/**
 * Created by Win8.1 on 8/3/2017.
 */

public class Rule5DBAdapter {
    protected static final String Rule5_TABLE_NAME = "Rule5";
    protected static final String Rule5_COLUMN_ID = "id";
    protected static final String Rule5_COLUMN_Gift = "giftID";
    protected static final String Rule5_Product_id = "product_id";
    protected static final String Rule5_Product_Price = "price";

    public static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS Rule5 ( `id` INTEGER PRIMARY KEY AUTOINCREMENT," + " 'giftID'   INTEGER  ," + " 'product_id' INTEGER ," + " 'price' INTEGER )";
    private SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public Rule5DBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public Rule5DBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public int insertEntry(long id, long giftID,  long product_id,int price){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Rule5_COLUMN_ID, Util.idHealth(this.db, Rule5_TABLE_NAME, Rule5_COLUMN_ID));
        val.put(Rule5_COLUMN_Gift,giftID);

        val.put(Rule5_Product_id,product_id);
        val.put(Rule5_Product_Price,price);


        try {

            db.insert(Rule5_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("Rule5 insertEntry", "inserting Entry at " + Rule5_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public Rule5 getGiftForRule5(long rule_id) {
        Rule5 rule5=null;
        Cursor cursor1 = db.rawQuery("select * from " + Rule5_TABLE_NAME+ " where id='" + rule_id + "'" , null);
        cursor1.moveToFirst();

        if (cursor1.getCount() < 1) // UserName Not Exist
        {
            cursor1.close();
            return rule5;
        }
        cursor1.moveToFirst();
        rule5= new Rule5(Long.parseLong(cursor1.getString(cursor1.getColumnIndex(Rule5_COLUMN_ID))),
                Long.parseLong(cursor1.getString(cursor1.getColumnIndex(Rule5_COLUMN_Gift))),
                Long.parseLong(cursor1.getString(cursor1.getColumnIndex(Rule5_Product_id))),
                Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(Rule5_Product_Price))));
        cursor1.close();
        return  rule5;
    }

}
