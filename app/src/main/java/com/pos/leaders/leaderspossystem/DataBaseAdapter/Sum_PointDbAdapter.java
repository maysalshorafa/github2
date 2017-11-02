package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.SumPoint;
import com.pos.leaders.leaderspossystem.Models.UserPermissions;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 7/19/2017.
 */

public class Sum_PointDbAdapter {
    public static final String SUM_POINT_TABLE_NAME = "SumPoint";
    // Column Names
    protected static final String SUM_POINT_COLUMN_ID = "id";
    protected static final String SUM_POINT_COLUMN_SALE_ID = "saleId";
    protected static final String SUM_POINT_COLUMN_POINT = "pointAmount";
    protected static final String SUM_POINT_COLUMN_CUSTOMER = "customer_id";


    public static final String DATABASE_CREATE= "CREATE TABLE sumPoint ( `id` INTEGER PRIMARY KEY AUTOINCREMENT  , `saleId` INTEGER ,"+" `pointAmount` INTEGER , `"+ SUM_POINT_COLUMN_CUSTOMER +"` INTEGER, FOREIGN KEY(`saleId`) REFERENCES `sales.id` )";
    private SQLiteDatabase db;

    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;
    public int getPointInfo(long _custmer_id) {

        Cursor cur = db.rawQuery("SELECT SUM(pointAmount) from " + SUM_POINT_TABLE_NAME + "  where "+ SUM_POINT_COLUMN_CUSTOMER +"='" + _custmer_id + "'", null);
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

    public long insertEntry( long  saleId, int point,long custmerId) {
        SumPoint sumPoint = new SumPoint(Util.idHealth(this.db, SUM_POINT_TABLE_NAME, SUM_POINT_COLUMN_ID),saleId, point,custmerId);
        sendToBroker(MessageType.ADD_SUMPOINT, sumPoint, this.context);

        try {
            long insertResult = insertEntry(sumPoint);
            return insertResult;
        } catch (SQLException ex) {
            Log.e("SumPoint insertEntry", "inserting Entry at " + SUM_POINT_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long insertEntry(SumPoint sumPoint){
        ContentValues val = new ContentValues();
        val.put(SUM_POINT_COLUMN_ID,sumPoint.getId());
        //Assign values for each row.
        val.put(SUM_POINT_COLUMN_SALE_ID, sumPoint.getSaleId());
        val.put(SUM_POINT_COLUMN_POINT, sumPoint.getPointAmount());
        val.put(SUM_POINT_COLUMN_CUSTOMER,sumPoint.getCustomer_id());

        try {
            return db.insert(SUM_POINT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("SumPoint DB insert", "inserting Entry at " + SUM_POINT_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }


}
