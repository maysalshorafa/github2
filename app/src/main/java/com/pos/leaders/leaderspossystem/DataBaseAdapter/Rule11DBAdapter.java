package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule11;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule3;

/**
 * Created by Win8.1 on 8/2/2017.
 */

public class Rule11DBAdapter {
    protected static final String Rule11_TABLE_NAME = "Rule11";
    protected static final String Rule11_COLUMN_ID = "id";
    protected static final String Rule11_COLUMN_Amount = "amount";
    protected static final String Rule11_COLUMN_DiscountAmount = "discountAmount";

    public static final String DATABASE_CREATE= "CREATE TABLE IF NOT EXISTS Rule11 ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,"+" 'amount'  INTEGER  ,"+" 'discountAmount'  INTEGER)";
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
    public int insertEntry(int id,int amount, int discountAmount){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Rule11_COLUMN_ID,id);

        val.put(Rule11_COLUMN_Amount,amount);
        val.put(Rule11_COLUMN_DiscountAmount,discountAmount);





        try {

            db.insert(Rule11_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("Rule11 insertEntry", "inserting Entry at " + Rule11_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }


    public Rule11 getAmountForRule11(int rule_id) {
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
        rule11= new Rule11(Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(Rule11_COLUMN_Amount))),Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(Rule11_COLUMN_DiscountAmount))));
        cursor1.close();
        return  rule11;}

}
