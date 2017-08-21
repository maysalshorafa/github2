package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.ValueOfPoint;

/**
 * Created by Win8.1 on 7/26/2017.
 */

public class ValueOfPointDB {
    public static final String ValueOfPoint_TABLE_NAME = "value_ofPoint";
    // Column Names
    protected static final String Value_COLUMN_Id = "id";
    protected static final String Value_COLUMN = "value";
    protected static final String CreateDate_Value_COLUMN_Point = "createDate";
    public static final String DATABASE_CREATE = "CREATE TABLE value_ofPoint ( `id` INTEGER ," + " `value` INTEGER ,`createDate` TEXT DEFAULT current_timestamp )";
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

    public int insertEntry(long id,int value,String create_date){
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(Value_COLUMN_Id,id);
        val.put(Value_COLUMN,value);

        val.put(CreateDate_Value_COLUMN_Point,create_date);
        try {

            db.insert(ValueOfPoint_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("Value_point insertEntry", "inserting Entry at " + ValueOfPoint_TABLE_NAME + ": " + ex.getMessage());
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
                cursor.getString(cursor.getColumnIndex(CreateDate_Value_COLUMN_Point)));
        return valueOfPoint;
    }
}
