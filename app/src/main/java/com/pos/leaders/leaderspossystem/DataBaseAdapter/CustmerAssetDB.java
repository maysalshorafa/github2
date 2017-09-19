package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Order;

/**
 * Created by Win8.1 on 8/27/2017.
 */

public class CustmerAssetDB {

    public static final String CustmerAsset_TabelName = "custmerAssest";
    // Column Names
    protected static final String CustmerAssest_COLUMN_Order_Id = "order_id";
    protected static final String CustmerAssest_COLUMN_ID = "custmerAssestID";
    protected static final String CustmerAssest_COLUMN_amount= "amount";
    protected static final String CustmerAssest_COLUMN_type= "type";
    protected static final String CustmerAssest_COLUMN_Case= "salescase";
    public static final String DATABASE_CREATE = "CREATE TABLE `"+CustmerAsset_TabelName+"` ( `"+CustmerAssest_COLUMN_Order_Id+"` INTEGER , " +
            "`"+CustmerAssest_COLUMN_ID+"` INTEGER NOT NULL, `"+CustmerAssest_COLUMN_amount+"` REAL, `"+CustmerAssest_COLUMN_type+"` INTEGER DEFAULT 0 "+CustmerAssest_COLUMN_Case+"` TEXT ";
    private SQLiteDatabase db;
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;




    public CustmerAssetDB(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public CustmerAssetDB open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }



    public long insertEntry(long order_id,long user_id,double amount,int type,String salescase){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CustmerAssest_COLUMN_Order_Id,order_id);
        val.put(CustmerAssest_COLUMN_ID,user_id);
        val.put(CustmerAssest_COLUMN_amount,amount);
        val.put(CustmerAssest_COLUMN_type,type);
        val.put(CustmerAssest_COLUMN_Case,salescase);

        try {
            return db.insert(CustmerAsset_TabelName, null, val);

        } catch (SQLException ex) {
            Log.e("ASsest DB insert", "inserting Entry at " + CustmerAsset_TabelName + ": " + ex.getMessage());
            return 0;
        }
    }

}
