package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.SumPoint;
import com.pos.leaders.leaderspossystem.Models.UsedPoint;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 8/8/2017.
 */

public class UsedPointDBAdapter {
    public static final String UsedPoint_TabelName = "usedPoint";
    // Column Names
    protected static final String UsedPoint_COLUMN_Id = "id";
    protected static final String UsedPoint_COLUMN_Sale_Id = "sale_id";
    protected static final String UsedPoint_COLUMN_Point = "unUsedpoint_amount";
    protected static final String UsedPoint_COLUMN_Custmer= "custmer_id";


    public static final String DATABASE_CREATE = "CREATE TABLE usedPoint ( `id` INTEGER PRIMARY KEY AUTOINCREMENT  , `sale_id` INTEGER ,`unUsedpoint_amount` INTEGER ,`custmer_id` INTEGER )";
    private SQLiteDatabase db;
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;



    public int getUnusedPointInfo(long _custmer_id) {

        Cursor cur = db.rawQuery("SELECT SUM(unUsedpoint_amount) from " +  UsedPoint_TabelName + "  where custmer_id='" + _custmer_id + "'", null);

        if (cur.moveToFirst()) {
            return (int) cur.getLong(0);
        }
        return  0;
    }
    public UsedPointDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public UsedPointDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry( long saleId, int point,long custmerId) {
        UsedPoint usedPoint = new UsedPoint(Util.idHealth(this.db, UsedPoint_TabelName, UsedPoint_COLUMN_Id),saleId, point,custmerId);
        sendToBroker(MessageType.ADD_USEDPOINT, usedPoint, this.context);

        try {
            long insertResult = insertEntry(usedPoint);
            return insertResult;
        } catch (SQLException ex) {
            Log.e("UsedPoint insertEntry", "inserting Entry at " + UsedPoint_TabelName + ": " + ex.getMessage());
            return 0;
        }
    }
    public long insertEntry(UsedPoint usedPoint){
        ContentValues val = new ContentValues();
        val.put(UsedPoint_COLUMN_Id,usedPoint.getId());
        //Assign values for each row.
        val.put(UsedPoint_COLUMN_Sale_Id, usedPoint.getSaleId());
        val.put(UsedPoint_COLUMN_Point, usedPoint.getUnUsedpoint_amount());
        val.put(UsedPoint_COLUMN_Custmer,usedPoint.getCustmerId());

        try {
            return db.insert(UsedPoint_TabelName, null, val);
        } catch (SQLException ex) {
            Log.e("UsedPoint DB insert", "inserting Entry at " + UsedPoint_TabelName + ": " + ex.getMessage());
            return 0;
        }
    }
}
