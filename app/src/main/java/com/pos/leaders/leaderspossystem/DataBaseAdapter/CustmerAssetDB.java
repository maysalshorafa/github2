package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;

/**
 * Created by Win8.1 on 8/27/2017.
 */

public class CustmerAssetDB {

    public static final String CustmerAsset_TabelName = "custmerAssest";
    // Column Names
    protected static final String CustmerAssest_COLUMN_Sale_Id = "sale_id";
    protected static final String CustmerAssest_COLUMN_ID = "id";
    protected static final String CustmerAssest_COLUMN_amount= "amount";
    protected static final String CustmerAssest_COLUMN_type= "type";

    public static final String DATABASE_CREATE = "CREATE TABLE `"+CustmerAsset_TabelName+"` ( `"+CustmerAssest_COLUMN_Sale_Id+"` INTEGER , " +
            "`"+CustmerAssest_COLUMN_ID+"` INTEGER NOT NULL, `"+CustmerAssest_COLUMN_amount+"` INTEGER, `"+CustmerAssest_COLUMN_type+"` INTEGER DEFAULT 1 )";
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
    public int insertEntry(long sale_id,long user_id,int amount,int type){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CustmerAssest_COLUMN_Sale_Id,sale_id);
        val.put(CustmerAssest_COLUMN_ID,user_id);
        val.put(CustmerAssest_COLUMN_amount,amount);
        val.put(CustmerAssest_COLUMN_type,type);

        try {
            db.insert( CustmerAsset_TabelName, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("CustmerAssest  insertEntry", "inserting Entry at " +  CustmerAsset_TabelName + ": " + ex.getMessage());
            return 0;
        }
    }
}
