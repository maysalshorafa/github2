/**
 * Created by Win8.1 on 12/14/2017.
 */package com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.util.Log;
        import com.pos.leaders.leaderspossystem.DbHelper;
        import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.CustomerMeasurement;
        import com.pos.leaders.leaderspossystem.Tools.Util;
        import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

        import java.util.ArrayList;
        import java.util.List;

        import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;


public class CustomerMeasurementDBAdapter {
    // TABLE NAME
    public static final String CUSTOMER_MEASUREMENT_TABLE_NAME = "CustomerMeasurement";

    // Column Names
    protected static final String CUSTOMER_MEASUREMENT_COLUMN_ID = "id";
    protected static final String CUSTOMER_MEASUREMENT_COLUMN_CUSTOMER_ID = "customerId";
    protected static final String CUSTOMER_MEASUREMENT_COLUMN_USER_ID = "userId";
    protected static final String CUSTOMER_MEASUREMENT_COLUMN_VISIT_DATE = "visitDate";
    //end
    // Create Table
    public static final String DATABASE_CREATE = "CREATE TABLE " + CUSTOMER_MEASUREMENT_TABLE_NAME
            + " ( `" + CUSTOMER_MEASUREMENT_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, `" + CUSTOMER_MEASUREMENT_COLUMN_CUSTOMER_ID + "` INTEGER, `" + CUSTOMER_MEASUREMENT_COLUMN_USER_ID + "` INTEGER, " + " `" + CUSTOMER_MEASUREMENT_COLUMN_VISIT_DATE + "` TEXT DEFAULT current_timestamp)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public CustomerMeasurementDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public CustomerMeasurementDBAdapter open() throws SQLException {
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
    public long insertEntry(long customerId, long userId, long visitDate) {
        CustomerMeasurement customerMeasurement = new CustomerMeasurement(Util.idHealth(this.db, CUSTOMER_MEASUREMENT_TABLE_NAME, CUSTOMER_MEASUREMENT_COLUMN_ID), customerId, userId, visitDate);
        sendToBroker(MessageType.ADD_CUSTOMER_MEASUREMENT, customerMeasurement, this.context);
        try {
            return insertEntry(customerMeasurement);
        } catch (SQLException ex) {
            Log.e(CUSTOMER_MEASUREMENT_TABLE_NAME +" DB insert", "inserting Entry at " + CUSTOMER_MEASUREMENT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public long insertEntry(CustomerMeasurement customerMeasurement) {
        ContentValues val = new ContentValues();
        val.put(CUSTOMER_MEASUREMENT_COLUMN_ID, customerMeasurement.getId());
        val.put(CUSTOMER_MEASUREMENT_COLUMN_CUSTOMER_ID, customerMeasurement.getCustomerId());
        val.put(CUSTOMER_MEASUREMENT_COLUMN_USER_ID,customerMeasurement.getUserId());
        val.put(CUSTOMER_MEASUREMENT_COLUMN_VISIT_DATE, customerMeasurement.getVisitDate());

        try {
            return db.insert(CUSTOMER_MEASUREMENT_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(CUSTOMER_MEASUREMENT_TABLE_NAME +" DB insert", "inserting Entry at " + CUSTOMER_MEASUREMENT_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }
    // end insert methods

    //Delete Method
    public boolean deleteCustomerMeasurement(long id)
    {
        return db.delete(CUSTOMER_MEASUREMENT_TABLE_NAME, CUSTOMER_MEASUREMENT_COLUMN_ID + "=" + id, null) > 0;
    }
    // end

    // get CustomerMeasurement by id
    public CustomerMeasurement getCustomerMeasurementByID(long id) {
        CustomerMeasurement customerMeasurement = null;
        Cursor cursor = db.rawQuery("select * from " + CUSTOMER_MEASUREMENT_TABLE_NAME + " where " + CUSTOMER_MEASUREMENT_COLUMN_ID + " = "+ id, null);
        if (cursor.getCount() < 1)
        {
            cursor.close();
            return customerMeasurement;
        }
        cursor.moveToFirst();
        customerMeasurement = new CustomerMeasurement(makeCustomerMeasurement(cursor));
        cursor.close();

        return customerMeasurement;
    }
    // end
    // method to make CustomerMeasurement
    private CustomerMeasurement makeCustomerMeasurement(Cursor cursor){
        try {
            return new CustomerMeasurement(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_MEASUREMENT_COLUMN_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_MEASUREMENT_COLUMN_CUSTOMER_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_MEASUREMENT_COLUMN_USER_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_MEASUREMENT_COLUMN_VISIT_DATE))));
        }catch (Exception ex){
            return new CustomerMeasurement(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_MEASUREMENT_COLUMN_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_MEASUREMENT_COLUMN_CUSTOMER_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_MEASUREMENT_COLUMN_USER_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_MEASUREMENT_COLUMN_VISIT_DATE))));
        }
    }
    //end
    // get CustomerMeasurement between to id
    public List<CustomerMeasurement> getTopCustomerMeasurement(long count, long offset) {
                     List<CustomerMeasurement> customerMeasurementList = new ArrayList<CustomerMeasurement>();
                    Cursor cursor = db.rawQuery("select * from " + CUSTOMER_MEASUREMENT_TABLE_NAME + " where " + CUSTOMER_MEASUREMENT_COLUMN_ID + "<='" + offset + "' and " + CUSTOMER_MEASUREMENT_COLUMN_ID +
                        ">='" + count + "'" + " order by " + CUSTOMER_MEASUREMENT_COLUMN_ID + " desc", null);

                    cursor.moveToFirst();

                        while (!cursor.isAfterLast()) {
                            customerMeasurementList.add(makeCustomerMeasurement(cursor));
                        cursor.moveToNext();
                   }

                      return customerMeasurementList;
           }
    //end

}

