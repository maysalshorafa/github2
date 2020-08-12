package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.SumPoint;
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
    protected static final String SUM_POINT_COLUMN_SALE_ID = "orderId";
    protected static final String SUM_POINT_COLUMN_POINT = "pointAmount";
    protected static final String SUM_POINT_COLUMN_TOTAL_POINT = "totalPoint";

    protected static final String SUM_POINT_COLUMN_CUSTOMER = "customerId";


    public static final String DATABASE_CREATE= "CREATE TABLE sumPoint ( `id` INTEGER PRIMARY KEY AUTOINCREMENT  , `orderId` INTEGER ,"+" `pointAmount` INTEGER , "+" `totalPoint` INTEGER , `"+ SUM_POINT_COLUMN_CUSTOMER +"` INTEGER, FOREIGN KEY(`orderId`) REFERENCES `_Order.id` )";
    private SQLiteDatabase db;

    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;
    public int getPointInfo(long customerId) {

        Cursor cur = db.rawQuery("SELECT SUM(pointAmount) from " + SUM_POINT_TABLE_NAME + "  where "+ SUM_POINT_COLUMN_CUSTOMER +"='" + customerId + "'", null);
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

    public long insertEntry( long  saleId, int point,long custmerId,int totalPoint) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        SumPoint sumPoint = new SumPoint(Util.idHealth(this.db, SUM_POINT_TABLE_NAME, SUM_POINT_COLUMN_ID),saleId, point,custmerId,totalPoint);
        sendToBroker(MessageType.ADD_SUM_POINT, sumPoint, this.context);

        try {
            long insertResult = insertEntry(sumPoint);
            close();
            return insertResult;
        } catch (SQLException ex) {
            Log.e("SumPoint insertEntry", "inserting Entry at " + SUM_POINT_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long insertEntry(SumPoint sumPoint){
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        ContentValues val = new ContentValues();
        val.put(SUM_POINT_COLUMN_ID,sumPoint.getSumPointId());
        //Assign values for each row.
        val.put(SUM_POINT_COLUMN_SALE_ID, sumPoint.getOrderId());
        val.put(SUM_POINT_COLUMN_POINT, sumPoint.getPointAmount());
        val.put(SUM_POINT_COLUMN_TOTAL_POINT, sumPoint.getTotalPoint());

        val.put(SUM_POINT_COLUMN_CUSTOMER,sumPoint.getCustomerId());

        try {
            return db.insert(SUM_POINT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("SumPoint DB insert", "inserting Entry at " + SUM_POINT_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public SumPoint getLastRow(long customerId) throws Exception {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        SumPoint sumPoint = null;
        Cursor cursor = db.rawQuery("select * from " + SUM_POINT_TABLE_NAME + " where customerId='" + customerId + "'"+ " order by " + SUM_POINT_COLUMN_ID + " desc", null);
        if (cursor.getCount() < 1) // zReport Not Exist
        {
            sumPoint=new SumPoint();
            return sumPoint;

        }
        cursor.moveToFirst();
        sumPoint = makeSumPoint(cursor);
        cursor.close();
        close();
        return sumPoint;
    }
    public void updateEntry(SumPoint sumPoint) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        Sum_PointDbAdapter sum_pointDbAdapter = new Sum_PointDbAdapter(context);
        sum_pointDbAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        Log.d("Updat5555",sumPoint.toString());

        val.put(SUM_POINT_COLUMN_TOTAL_POINT,sumPoint.getTotalPoint());

        String where = SUM_POINT_COLUMN_ID + " = ?";
        db.update(SUM_POINT_TABLE_NAME, val, where, new String[]{sumPoint.getSumPointId() + ""});
        SumPoint d=sum_pointDbAdapter.getSumPointByID(sumPoint.getSumPointId());
        sendToBroker(MessageType.UPDATE_CATEGORY, d, this.context);
        sum_pointDbAdapter.close();
        close();
    }
    public SumPoint getSumPointByID(long id) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        SumPoint sumPoint = null;
        Cursor cursor = db.rawQuery("select * from " + SUM_POINT_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return sumPoint;
        }
        cursor.moveToFirst();
        sumPoint =new SumPoint(Long.parseLong(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_ID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_SALE_ID))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_POINT))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_CUSTOMER))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_TOTAL_POINT))));
        cursor.close();
        close();
        return sumPoint;
    }


    private SumPoint makeSumPoint(Cursor cursor) {
        try {
            SumPoint d = new SumPoint(Long.parseLong(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_SALE_ID))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_POINT))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_CUSTOMER))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_TOTAL_POINT))));

            return d;
        } catch (Exception ex) {
            SumPoint d = new SumPoint(Long.parseLong(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_SALE_ID))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_POINT))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_CUSTOMER))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(SUM_POINT_COLUMN_TOTAL_POINT))));

            return d;
        }
    }

}
