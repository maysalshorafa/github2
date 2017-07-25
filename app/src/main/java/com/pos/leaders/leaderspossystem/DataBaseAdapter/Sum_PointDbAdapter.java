package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
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
    public static final String DATABASE_CREATE= "CREATE TABLE sumPoint ( `sale_id` INTEGER ,"+" `point_amount` INTEGER , FOREIGN KEY(`sale_id`) REFERENCES `sales.id` )";
    private SQLiteDatabase db;
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

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
    public int insertEntry(int  sale_id, int point){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Sum_PointDbAdapter_COLUMN_Sale_Id,sale_id);
        val.put(Sum_PointDbAdapter_COLUMN_Point,point);



        try {

            db.insert(Sum_PointDbAdapter_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("Sum_point insertEntry", "inserting Entry at " + Sum_PointDbAdapter_COLUMN_Point + ": " + ex.getMessage());
            return 0;
        }
    }


}
