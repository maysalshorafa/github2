package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule3;

/**
 * Created by Win8.1 on 7/31/2017.
 */

public class Rule3DbAdapter {
    //////rule3 tabel
    protected static final String Rule3_TABLE_NAME = "Rule3";
    protected static final String Rule3_COLUMN_ID = "id";
    protected static final String Rule3_COLUMN_Parcent = "parcent";
    public static final String DATABASE_CREATE= "CREATE TABLE IF NOT EXISTS Rule3 ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,"+" 'parcent'  REAL  )";
    private SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public Rule3DbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public Rule3DbAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }
    public int insertEntry(int id,double parcent){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Rule3_COLUMN_ID,id);

        val.put(Rule3_COLUMN_Parcent,parcent);




        try {

            db.insert(Rule3_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("Rule3 insertEntry", "inserting Entry at " + Rule3_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }


    public double getParcentForRule3(int rule_id) {
        OfferDBAdapter offerDBAdapter=new OfferDBAdapter(context);
        offerDBAdapter.open();
        Rule3 rule3=null;
        Cursor cursor1 = db.rawQuery("select * from " + Rule3_TABLE_NAME+ " where id='" + rule_id + "'" , null);
        cursor1.moveToFirst();


        if (cursor1.getCount() < 1) // UserName Not Exist
        {
            cursor1.close();
            return 0;
        }
        cursor1.moveToFirst();
        rule3= new Rule3(Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(Rule3_COLUMN_ID))),Double.parseDouble(cursor1.getString(cursor1.getColumnIndex(Rule3_COLUMN_Parcent))));
        cursor1.close();
        return  rule3.getParcent();}

}
