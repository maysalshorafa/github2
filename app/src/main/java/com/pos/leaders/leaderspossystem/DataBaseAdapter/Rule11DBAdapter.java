package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule11;

/**
 * Created by Win8.1 on 8/2/2017.
 */

public class Rule11DBAdapter {
    protected static final String Rule11_TABLE_NAME = "Rule11";
    protected static final String Rule11_COLUMN_ID = "id";
    protected static final String Rule11_COLUMN_Amount = "amount";
    protected static final String Rule11_COLUMN_DiscountAmount = "discountAmount";
    protected static final String Rule11_COLUMN_Contain = "contain";
    protected static final String Rule11_COLUMN_Club_Contain = "club_contain";


    public static final String DATABASE_CREATE= "CREATE TABLE IF NOT EXISTS Rule11 ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,"+" 'amount'  REAL  ,"+" 'discountAmount'  REAL ,"+" 'contain'  INTEGER"+" 'club_contain'  INTEGER)";
    private SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public Rule11DBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public Rule11DBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long insertEntry(Rule11 rule11){

        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(Rule11_COLUMN_ID,rule11.getId());

        val.put(Rule11_COLUMN_Amount,rule11.getAmount());
        val.put(Rule11_COLUMN_DiscountAmount, rule11.getDiscountAmount());
        val.put(Rule11_COLUMN_Contain,rule11.getContain());
        val.put(Rule11_COLUMN_Club_Contain,rule11.getClubContain());

        try {
            return db.insert(Rule11_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Rule11 insertEntry", "inserting Entry at " + Rule11_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public Rule11 getAmountForRule11(long rule_id) {
        OfferDBAdapter offerDBAdapter=new OfferDBAdapter(context);
        offerDBAdapter.open();
        Rule11 rule11=null;
        Cursor cursor1 = db.rawQuery("select * from " + Rule11_TABLE_NAME+ " where id='" + rule_id + "'" , null);
        cursor1.moveToFirst();


        if (cursor1.getCount() < 1) // UserName Not Exist
        {
            cursor1.close();
            return rule11;
        }
        cursor1.moveToFirst();
        rule11= new Rule11(Double.parseDouble(cursor1.getString(cursor1.getColumnIndex(Rule11_COLUMN_Amount))),
                Double.parseDouble(cursor1.getString(cursor1.getColumnIndex(Rule11_COLUMN_DiscountAmount))),
                Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(Rule11_COLUMN_Contain))),
                Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(Rule11_COLUMN_Club_Contain))));
        cursor1.close();
        return  rule11;}

}
