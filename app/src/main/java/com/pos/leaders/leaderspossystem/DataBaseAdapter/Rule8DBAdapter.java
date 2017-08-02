package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule7;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule8;

/**
 * Created by Win8.1 on 8/2/2017.
 */

public class Rule8DBAdapter {
    protected static final String Rule8_TABLE_NAME = "Rule8";
    protected static final String Rule8_COLUMN_ID = "id";
    protected static final String Rule8_COLUMN_Parcent = "parcent";
    protected static final String Rule8_Product_id = "product_id";
    public static final String DATABASE_CREATE= "CREATE TABLE IF NOT EXISTS Rule8 ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,"+" 'parcent'  REAL  ,"+" 'offer_id' INTEGER ,"+" 'product_id' INTEGER , FOREIGN KEY(`offer_id`) REFERENCES `offers.id`)";
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
    public int insertEntry(int id, double parcent,  int product_id){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Rule8_COLUMN_ID,id);
        val.put(Rule8_COLUMN_Parcent,parcent);

        val.put(Rule8_Product_id,product_id);

        try {

            db.insert(Rule8_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("Rule8 insertEntry", "inserting Entry at " + Rule8_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public Rule8 getParcentForRule8(int rule_id) {
        Rule8 rule8=null;
        Cursor cursor1 = db.rawQuery("select * from " + Rule8_TABLE_NAME+ " where id='" + rule_id + "'" , null);
        cursor1.moveToFirst();


        if (cursor1.getCount() < 1) // UserName Not Exist
        {
            cursor1.close();
            return rule8;
        }
        cursor1.moveToFirst();
        rule8= new Rule8(Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(Rule8_COLUMN_ID))),
                Double.parseDouble(cursor1.getString(cursor1.getColumnIndex(Rule8_COLUMN_Parcent))),
                Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(Rule8_Product_id))));
        cursor1.close();
        return  rule8;
    }
}
