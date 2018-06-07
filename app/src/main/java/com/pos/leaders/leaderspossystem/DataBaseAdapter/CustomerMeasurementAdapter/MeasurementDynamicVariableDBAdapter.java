package com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.MeasurementDynamicVariable;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.HashMap;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 12/14/2017.
 */

public class MeasurementDynamicVariableDBAdapter {
    // TABLE NAME
    public static final String MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME = "MeasurementDynamicVariable";

    // Column Names
    protected static final String MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_ID = "id";
    protected static final String MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_NAME = "name";
    protected static final String MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_DYNAMIC_TYPE = "type";
    protected static final String MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_UNIT = "unit";
    protected static final String MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_HIDE = "hide";

    //end
    // Create Table
    public static final String DATABASE_CREATE = "CREATE TABLE " + MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME
            + " ( `" + MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, `" + MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_NAME + "` TEXT, `" + MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_DYNAMIC_TYPE + "` TEXT, " + " `" + MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_UNIT + "` TEXT, " + " `" + MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_HIDE + "` INTEGER DEFAULT 0)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public MeasurementDynamicVariableDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public MeasurementDynamicVariableDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }
    // Insert Method
    public long insertEntry(String name, String type, String unit) {
        MeasurementDynamicVariable measurementDynamicVariable = new MeasurementDynamicVariable(Util.idHealth(this.db, MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME, MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_ID), name, type, unit);
        sendToBroker(MessageType.ADD_MEASUREMENTS_DYNAMIC_VARIABLE, measurementDynamicVariable, this.context);
        try {
            return insertEntry(measurementDynamicVariable);
        } catch (SQLException ex) {
            Log.e(MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME +" DB insert", "inserting Entry at " + MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public long insertEntry(MeasurementDynamicVariable measurementDynamicVariable) {
        ContentValues val = new ContentValues();
        val.put(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_ID, measurementDynamicVariable.getMeasurementDynamicVariableId());
        val.put(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_NAME, measurementDynamicVariable.getName());
        val.put(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_DYNAMIC_TYPE,measurementDynamicVariable.getType());
        val.put(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_UNIT, measurementDynamicVariable.getUnit());
        val.put(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_HIDE, measurementDynamicVariable.isHide() ? 1 : 0);

        try {
            return db.insert(MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME +" DB insert", "inserting Entry at " + MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    // end insert methods
    //Delete Method
    public boolean deleteCustomerMeasurement(long id)
    {
        return db.delete(MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME, MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_ID + "=" + id, null) > 0;
    }
    // end
    // get MeasurementDynamicVariable by id
    public MeasurementDynamicVariable getMeasurementDynamicVariableByID(long id) {
        MeasurementDynamicVariable measurementDynamicVariable = null;
        Cursor cursor = db.rawQuery("select * from " + MEASUREMENT_DYNAMIC_VARIABLE_TABLE_NAME + " where " + MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_ID + " = "+ id, null);
        if (cursor.getCount() < 1)
        {
            cursor.close();
            return measurementDynamicVariable;
        }
        cursor.moveToFirst();
        measurementDynamicVariable = new MeasurementDynamicVariable(makeMeasurementDynamicVariable(cursor));
        cursor.close();

        return measurementDynamicVariable;
    }
    // end
    // method to make MeasurementDynamicVariable
    private MeasurementDynamicVariable makeMeasurementDynamicVariable(Cursor cursor){
        try {
            return new MeasurementDynamicVariable(Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_DYNAMIC_TYPE)),
                    cursor.getString(cursor.getColumnIndex(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_UNIT)), Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_HIDE))));
        }catch (Exception ex){
             return new MeasurementDynamicVariable(Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_DYNAMIC_TYPE)),
                    cursor.getString(cursor.getColumnIndex(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_UNIT)), Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_HIDE))));
        }
    }
    //end
    // get All MeasurementDynamicVariable
    public ArrayList<HashMap<String, String>> getAllMeasurementDynamicVariable() {
        ArrayList<HashMap<String, String>> storeList = new ArrayList<HashMap<String, String>>();

        Cursor c = db.rawQuery("SELECT * FROM MeasurementDynamicVariable"+ " where " + MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_HIDE + "=0 ", null);

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_ID,  c.getString(c.getColumnIndex("id")));
            map.put(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_NAME,  c.getString(c.getColumnIndex("name")));
            map.put(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_DYNAMIC_TYPE, c.getString(c.getColumnIndex("type")));
            map.put(MEASUREMENT_DYNAMIC_VARIABLE_COLUMN_COLUMN_UNIT, c.getString(c.getColumnIndex("unit")));
            storeList.add(map);

            c.moveToNext();

        }
  return storeList;
    }

    // end
}
