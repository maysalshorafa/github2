package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.OfferRule;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule7;

/**
 * Created by Win8.1 on 7/31/2017.
 */

public class Rule7DbAdapter {

    public static final String DATABASE_CREATE= "CREATE TABLE IF NOT EXISTS Rule7 ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,"+" 'price'  REAL  ,"+"  'product_id' INTEGER"+"  'contain_club' INTEGER  )";
    ////rule7 tabel
    protected static final String Rule7_TABLE_NAME = "Rule7";
    protected static final String Rule7_COLUMN_ID = "id";
    protected static final String Rule7_COLUMN_Price = "price";
    protected static final String Rule7_Product_id = "product_id";
    protected static final String Rule7_Product_Contain_club = "contain_club";
    // Context of the application using the database.
    private final Context context;
    private SQLiteDatabase db;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public Rule7DbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public Rule7DbAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }
    public long insertEntry(Rule7 rule7) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Rule7_COLUMN_ID, rule7.getId());
        val.put(Rule7_COLUMN_Price, rule7.getPrice());

        val.put(Rule7_Product_id, rule7.getProduct_id());
        val.put(Rule7_Product_Contain_club, rule7.getContain_club());

        try {
            return db.insert(Rule7_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Rule7 insertEntry", "inserting Entry at " + Rule7_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public Rule7 getPriceForRule7(long rule_id) {
        Rule7 rule7=null;
        Cursor cursor1 = db.rawQuery("select * from " + Rule7_TABLE_NAME+ " where id='" + rule_id + "'" , null);
        cursor1.moveToFirst();


        if (cursor1.getCount() < 1) // UserName Not Exist
        {
            cursor1.close();
            return rule7;
        }
        cursor1.moveToFirst();
        rule7= new Rule7(Long.parseLong(cursor1.getString(cursor1.getColumnIndex(Rule7_COLUMN_ID))),
                Double.parseDouble(cursor1.getString(cursor1.getColumnIndex(Rule7_COLUMN_Price))),
                Long.parseLong(cursor1.getString(cursor1.getColumnIndex(Rule7_Product_id))),
                Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(Rule7_Product_Contain_club))));
        cursor1.close();
        return  rule7;
    }


}
