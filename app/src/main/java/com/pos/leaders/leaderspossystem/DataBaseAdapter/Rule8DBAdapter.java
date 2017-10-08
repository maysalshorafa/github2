package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule8;
import com.pos.leaders.leaderspossystem.Tools.Util;

/**
 * Created by Win8.1 on 8/2/2017.
 */

public class Rule8DBAdapter {
    protected static final String Rule8_TABLE_NAME = "Rule8";
    protected static final String Rule8_COLUMN_ID = "id";
    protected static final String Rule8_COLUMN_Parcent = "parcent";
    protected static final String Rule8_Product_id = "product_id";
    protected static final String Rule8_Contain_club = "contain_club";

    public static final String DATABASE_CREATE= "CREATE TABLE IF NOT EXISTS Rule8 ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,"+" 'parcent'  REAL  ,"+" 'offer_id' INTEGER ,"+" 'product_id' INTEGER ,"+" 'contain_club' INTEGER  , FOREIGN KEY(`offer_id`) REFERENCES `offers.id`)";
    private SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public Rule8DBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public Rule8DBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }
    public long insertEntry(Rule8 rule8){
        ContentValues val = new ContentValues();
        //Assign values for each row.
<<<<<<< HEAD
        val.put(Rule8_COLUMN_ID,rule8.getId());
        val.put(Rule8_COLUMN_Parcent,rule8.getPercent());
=======
        val.put(Rule8_COLUMN_ID, Util.idHealth(this.db, Rule8_TABLE_NAME, Rule8_COLUMN_ID));
        val.put(Rule8_COLUMN_Parcent,parcent);
>>>>>>> mays-sameer

        val.put(Rule8_Product_id,rule8.getProductID());
        val.put(Rule8_Contain_club,rule8.getContainClub());

        try {
            return db.insert(Rule8_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Rule8 insertEntry", "inserting Entry at " + Rule8_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public Rule8 getParcentForRule8(long rule_id) {
        Rule8 rule8=null;
        Cursor cursor1 = db.rawQuery("select * from " + Rule8_TABLE_NAME+ " where id='" + rule_id + "'" , null);
        cursor1.moveToFirst();


        if (cursor1.getCount() < 1) // UserName Not Exist
        {
            cursor1.close();
            return rule8;
        }
        cursor1.moveToFirst();
        rule8= new Rule8(Long.parseLong(cursor1.getString(cursor1.getColumnIndex(Rule8_COLUMN_ID))),
                Double.parseDouble(cursor1.getString(cursor1.getColumnIndex(Rule8_COLUMN_Parcent))),
                Long.parseLong(cursor1.getString(cursor1.getColumnIndex(Rule8_Product_id))),
                Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(Rule8_Contain_club))));
        cursor1.close();
        return  rule8;
    }
}
