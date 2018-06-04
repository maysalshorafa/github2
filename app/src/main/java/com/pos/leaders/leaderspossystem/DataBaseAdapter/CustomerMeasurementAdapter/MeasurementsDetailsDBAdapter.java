package com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.CustomerMeasurement;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.MeasurementsDetails;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 12/14/2017.
 */

public class MeasurementsDetailsDBAdapter {
    // TABLE NAME
    public static final String MEASUREMENTS_DETAILS_TABLE_NAME = "MeasurementsDetails";

    // Column Names
    protected static final String MEASUREMENTS_DETAILS_COLUMN_ID = "id";
    protected static final String MEASUREMENTS_DETAILS_COLUMN_MEASUREMENTS_ID = "measurementId";
    protected static final String MEASUREMENTS_DETAILS_COLUMN_DYNAMIC_VAR_ID = "dynamicVarId";
    protected static final String MEASUREMENTS_DETAILS_COLUMN_VALUE = "value";
    //end
    // Create Table
    public static final String DATABASE_CREATE = "CREATE TABLE " + MEASUREMENTS_DETAILS_TABLE_NAME
            + " ( `" + MEASUREMENTS_DETAILS_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, `" + MEASUREMENTS_DETAILS_COLUMN_MEASUREMENTS_ID + "` INTEGER, `" + MEASUREMENTS_DETAILS_COLUMN_DYNAMIC_VAR_ID + "` INTEGER, " + " `" + MEASUREMENTS_DETAILS_COLUMN_VALUE + "` TEXT)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public MeasurementsDetailsDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public MeasurementsDetailsDBAdapter open() throws SQLException {
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
    public long insertEntry(long measurementId, long dynamicVarId, String value) {
        MeasurementsDetails measurementsDetails = new MeasurementsDetails(Util.idHealth(this.db, MEASUREMENTS_DETAILS_TABLE_NAME, MEASUREMENTS_DETAILS_COLUMN_ID), measurementId, dynamicVarId, value);
        sendToBroker(MessageType.ADD_MEASUREMENTS_DETAILS, measurementsDetails, this.context);
        try {
            return insertEntry(measurementsDetails);
        } catch (SQLException ex) {
            Log.e(MEASUREMENTS_DETAILS_TABLE_NAME +" DB insert", "inserting Entry at " + MEASUREMENTS_DETAILS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public long insertEntry(MeasurementsDetails measurementsDetails) {
        ContentValues val = new ContentValues();
        val.put(MEASUREMENTS_DETAILS_COLUMN_ID, measurementsDetails.getMeasurementsDetailsId());
        val.put(MEASUREMENTS_DETAILS_COLUMN_MEASUREMENTS_ID, measurementsDetails.getMeasurementId());
        val.put(MEASUREMENTS_DETAILS_COLUMN_DYNAMIC_VAR_ID,measurementsDetails.getDynamicVarId());
        val.put(MEASUREMENTS_DETAILS_COLUMN_VALUE, measurementsDetails.getValue());

        try {
            return db.insert(MEASUREMENTS_DETAILS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(MEASUREMENTS_DETAILS_TABLE_NAME +" DB insert", "inserting Entry at " + MEASUREMENTS_DETAILS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    // end insert methods
    //Delete Method
    public boolean deleteCustomerMeasurement(long id)
    {
        return db.delete(MEASUREMENTS_DETAILS_TABLE_NAME, MEASUREMENTS_DETAILS_COLUMN_ID + "=" + id, null) > 0;
    }
    // end
    // get MeasurementsDetails by usedPointId
    public MeasurementsDetails getMeasurementDetailsByID(long id) {
        MeasurementsDetails measurementsDetails = null;
        Cursor cursor = db.rawQuery("select * from " + MEASUREMENTS_DETAILS_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1)
        {
            cursor.close();
            return measurementsDetails;
        }
        cursor.moveToFirst();
        measurementsDetails = new MeasurementsDetails(makeMeasurementDetails(cursor));
        cursor.close();

        return measurementsDetails;
    }
    // end
    // method to make MeasurementsDetails
    private MeasurementsDetails makeMeasurementDetails(Cursor cursor){
        try {
            return new MeasurementsDetails(Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_MEASUREMENTS_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_DYNAMIC_VAR_ID))),
                   cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_VALUE)));
        }catch (Exception ex){
            return new MeasurementsDetails(Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_MEASUREMENTS_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_DYNAMIC_VAR_ID))),
                    cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_VALUE)));
        }
    }
    //end
    // get All MeasurementsDetails To Customer
    public List<MeasurementsDetails> getAllMeasurementsDetails(long CustomerId) {
        CustomerMeasurementDBAdapter customerMeasurementDBAdapter = new CustomerMeasurementDBAdapter(context);
        customerMeasurementDBAdapter.open();

        List<MeasurementsDetails> measurementsDetailsList = new ArrayList<MeasurementsDetails>();
        Cursor cursor = db.rawQuery("select * from " + MEASUREMENTS_DETAILS_TABLE_NAME , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MeasurementsDetails measurementsDetails =new MeasurementsDetails(Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_MEASUREMENTS_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_DYNAMIC_VAR_ID))),
                    cursor.getString(cursor.getColumnIndex(MEASUREMENTS_DETAILS_COLUMN_VALUE)));
            CustomerMeasurement customerMeasurement =customerMeasurementDBAdapter.getCustomerMeasurementByID(measurementsDetails.getMeasurementId());
            if(customerMeasurement.getCustomerId()==CustomerId){
                measurementsDetailsList.add(measurementsDetails);

            }

            cursor.moveToNext();
        }
        return measurementsDetailsList;
    }
    // end

    // get MeasurementsDetails by measurementsId
    public int getMeasurementDetailsByMeasurementsId(long measurementId) {
        MeasurementsDetails measurementsDetails = null;
        Cursor cursor = db.rawQuery("select * from " + MEASUREMENTS_DETAILS_TABLE_NAME + " where measurementId='" + measurementId + "'", null);
            int count = cursor.getCount();
            return  count;

    }
    // end
    // UPDate MeasurementsDetails
    public void updateEntry(MeasurementsDetails measurementsDetails) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(MEASUREMENTS_DETAILS_COLUMN_MEASUREMENTS_ID, measurementsDetails.getMeasurementId());
        val.put(MEASUREMENTS_DETAILS_COLUMN_DYNAMIC_VAR_ID, measurementsDetails.getDynamicVarId());
        val.put(MEASUREMENTS_DETAILS_COLUMN_VALUE, measurementsDetails.getValue());
        String where = MEASUREMENTS_DETAILS_COLUMN_ID + " = ?";
        db.update(MEASUREMENTS_DETAILS_TABLE_NAME, val, where, new String[]{measurementsDetails.getMeasurementsDetailsId() + ""});
    }
    //end
    // get All MeasurementsDetails bu measurementId
    public ArrayList<HashMap<String, String>> getAllMeasurementDetail(long measurementId) {
        ArrayList<HashMap<String, String>> storeList = new ArrayList<HashMap<String, String>>();

        Cursor c = db.rawQuery("SELECT * FROM MeasurementsDetails"+ " where " + MEASUREMENTS_DETAILS_COLUMN_MEASUREMENTS_ID + "="+measurementId, null);

        c.moveToFirst();

        while (c.isAfterLast() == false) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(MEASUREMENTS_DETAILS_COLUMN_DYNAMIC_VAR_ID,  c.getString(c.getColumnIndex("dynamicVarId")));
            map.put(MEASUREMENTS_DETAILS_COLUMN_VALUE,  c.getString(c.getColumnIndex("value")));
            storeList.add(map);

            c.moveToNext();

        }
        return storeList;
    }

    // end
}
