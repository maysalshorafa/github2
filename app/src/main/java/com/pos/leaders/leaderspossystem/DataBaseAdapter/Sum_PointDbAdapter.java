package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;

/**
 * Created by Win8.1 on 7/19/2017.
 */

public class Sum_PointDbAdapter {
    public static final String Sum_PointDbAdapter_TABLE_NAME = "sumPoint";
    // Column Names
    protected static final String Sum_PointDbAdapter_COLUMN_Sale_Id = "sale_id";
    protected static final String Sum_PointDbAdapter_COLUMN_Point = "point_amount";
    protected static final String Sum_PointDbAdapter_COLUMN_Custmer= "custmer_id";

    public static final String DATABASE_CREATE= "CREATE TABLE sumPoint ( `sale_id` INTEGER ,"+" `point_amount` INTEGER ,"+" `custmer_id` INTEGER , FOREIGN KEY(`sale_id`) REFERENCES `sales.id` )";    private SQLiteDatabase db;
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;
    public int getPointInfo(long _custmer_id) {

        Cursor cur = db.rawQuery("SELECT SUM(point_amount) from " + Sum_PointDbAdapter_TABLE_NAME + "  where custmer_id='" + _custmer_id + "'", null);
        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
return  0;
    }
    public Sum_PointDbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public Sum_PointDbAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }
    public long insertEntry(long  sale_id, int point,long custmer_id){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Sum_PointDbAdapter_COLUMN_Sale_Id,sale_id);
        val.put(Sum_PointDbAdapter_COLUMN_Point,point);

        val.put(Sum_PointDbAdapter_COLUMN_Custmer,custmer_id);


        try {
            db.insert(Sum_PointDbAdapter_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("Sum_point insertEntry", "inserting Entry at " + Sum_PointDbAdapter_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }


}
