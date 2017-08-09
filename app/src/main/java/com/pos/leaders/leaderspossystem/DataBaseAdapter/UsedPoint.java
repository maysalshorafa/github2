package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import com.pos.leaders.leaderspossystem.DbHelper;

/**
 * Created by Win8.1 on 8/8/2017.
 */

public class UsedPoint {
    public static final String UsedPoint_TabelName = "usedPoint";
    // Column Names
    protected static final String UsedPoint_COLUMN_Sale_Id = "sale_id";
    protected static final String UsedPoint_COLUMN_Point = "unUsedpoint_amount";
    protected static final String UsedPoint_COLUMN_Custmer= "custmer_id";

    public static final String DATABASE_CREATE = "CREATE TABLE `"+UsedPoint_TabelName+"` ( `"+UsedPoint_COLUMN_Sale_Id+"` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`"+UsedPoint_COLUMN_Point+"` INTEGER NOT NULL, `"+UsedPoint_COLUMN_Custmer+"` INTEGER )";
    private SQLiteDatabase db;
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;



    public int getUnusedPointInfo(int _custmer_id) {

        Cursor cur = db.rawQuery("SELECT SUM(unUsedpoint_amount) from " +  UsedPoint_TabelName + "  where custmer_id='" + _custmer_id + "'", null);

        if (cur.moveToFirst()) {
            return cur.getInt(0);
        }
        return  0;
    }
    public UsedPoint(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public UsedPoint open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }
    public int insertEntry(int  sale_id, int point,int custmer_id){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(UsedPoint_COLUMN_Sale_Id,sale_id);
        val.put(UsedPoint_COLUMN_Point,point);

        val.put(UsedPoint_COLUMN_Custmer,custmer_id);


        try {

            db.insert( UsedPoint_TabelName, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("UsedPoint insertEntry", "inserting Entry at " +  UsedPoint_TabelName + ": " + ex.getMessage());
            return 0;
        }
    }
}
