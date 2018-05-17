package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.ValueOfPoint;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 7/26/2017.
 */

public class ValueOfPointDB {
    public static final String ValueOfPoint_TABLE_NAME = "value_ofPoint";
    // Column Names
    protected static final String Value_COLUMN_Id = "id";
    protected static final String Value_COLUMN = "value";
    protected static final String CreateDate_Value_COLUMN_CreateDate = "createDate";
    public static final String DATABASE_CREATE = "CREATE TABLE value_ofPoint ( `id` INTEGER PRIMARY KEY AUTOINCREMENT  ," + " `value` INTEGER ,`createDate` TEXT DEFAULT current_timestamp )";
    private DbHelper dbHelper;
    Context context;
    private SQLiteDatabase db;

    public ValueOfPointDB(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public ValueOfPointDB open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

      public long insertEntry( int value,long create_date) {
        ValueOfPoint valueOfPoint = new ValueOfPoint(Util.idHealth(this.db, ValueOfPoint_TABLE_NAME, Value_COLUMN_Id),value, create_date);
        sendToBroker(MessageType.ADD_VALUE_OF_POINT, valueOfPoint, this.context);

        try {
            long insertResult = insertEntry(valueOfPoint);
            return insertResult;
        } catch (SQLException ex) {
            Log.e("Value Of Point insertEntry", "inserting Entry at " + ValueOfPoint_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long insertEntry(ValueOfPoint valueOfPoint){
        ContentValues val = new ContentValues();
        val.put(Value_COLUMN_Id,valueOfPoint.getId());
        val.put(Value_COLUMN, valueOfPoint.getValue());
        val.put(CreateDate_Value_COLUMN_CreateDate, valueOfPoint.getCreate_Date());

        try {
            return db.insert(ValueOfPoint_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Value Of Point  DB insert", "inserting Entry at " + ValueOfPoint_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public ValueOfPoint getValue(){
        ValueOfPoint valueOfPoint=null;
        Cursor cursor = db.rawQuery("SELECT  * FROM " + ValueOfPoint_TABLE_NAME, null);
        if (cursor.getCount() < 1) // Entry Not Exist
        {
            cursor.close();
            return valueOfPoint;
        }        cursor.moveToLast();
        valueOfPoint =new ValueOfPoint(Long.parseLong(cursor.getString(cursor.getColumnIndex(Value_COLUMN_Id))),Double.parseDouble(
                cursor.getString(cursor.getColumnIndex(Value_COLUMN))),
                cursor.getLong(cursor.getColumnIndex(CreateDate_Value_COLUMN_CreateDate)));
        return valueOfPoint;
    }
}
