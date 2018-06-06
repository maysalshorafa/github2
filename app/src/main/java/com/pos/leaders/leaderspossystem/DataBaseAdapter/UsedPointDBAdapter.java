package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.UsedPoint;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 8/8/2017.
 */

public class UsedPointDBAdapter {
    public static final String USED_POINT_TABLE_NAME = "UsedPoint";
    // Column Names
    protected static final String USED_POINT_COLUMN_ID = "usedPointId";
    protected static final String USED_POINT_COLUMN_Order_ID = "orderId";
    protected static final String USED_POINT_COLUMN_POINT = "unUsedPointAmount";
    protected static final String USED_POINT_COLUMN_CUSTOMER = "customerId";


    public static final String DATABASE_CREATE = "CREATE TABLE UsedPoint ( `usedPointId` INTEGER PRIMARY KEY AUTOINCREMENT  , `orderId` INTEGER ,`unUsedPointAmount` INTEGER ,`customerId` INTEGER ," +"FOREIGN KEY(`orderId`) REFERENCES `_Order.id`)";
    private SQLiteDatabase db;
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


// Sum Of UnUsed Point
    public int getUnusedPointInfo(long customerId) {

        Cursor cur = db.rawQuery("SELECT SUM(unUsedPointAmount) from " + USED_POINT_TABLE_NAME + "  where customerId='" + customerId + "'", null);

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
        UsedPoint usedPoint = new UsedPoint(Util.idHealth(this.db, USED_POINT_TABLE_NAME, USED_POINT_COLUMN_ID),saleId, point,custmerId);
        sendToBroker(MessageType.ADD_USED_POINT, usedPoint, this.context);

        try {
            long insertResult = insertEntry(usedPoint);
            return insertResult;
        } catch (SQLException ex) {
            Log.e("UsedPoint insertEntry", "inserting Entry at " + USED_POINT_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long insertEntry(UsedPoint usedPoint){
        ContentValues val = new ContentValues();
        val.put(USED_POINT_COLUMN_ID,usedPoint.getUsedPointId());
        //Assign values for each row.
        val.put(USED_POINT_COLUMN_Order_ID, usedPoint.getSaleId());
        val.put(USED_POINT_COLUMN_POINT, usedPoint.getUnUsed_point_amount());
        val.put(USED_POINT_COLUMN_CUSTOMER,usedPoint.getCustomerId());

        try {
            return db.insert(USED_POINT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("UsedPoint DB insert", "inserting Entry at " + USED_POINT_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
}
