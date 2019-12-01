package com.pos.leaders.leaderspossystem.syncposservice.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.syncposservice.Model.BrokerMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 25/07/2017.
 */

public class Broker {

    private static final String LOG_TAG = "Broker DB";

    // Table Name
    private static final String BROKER_TABLE_NAME = "a_report";
    // Column Names
    private static final String BROKER_COLUMN_ID = "id";
    private static final String BROKER_COLUMN_COMMAND = "command";
    private static final String BROKER_COLUMN_IS_SYNCED = "isSynced";
    private static final String BROKER_COLUMN_CREATE_DATE = "createDate";
    private static final String BROKER_COLUMN_BY_USER = "byUser";

    public static final String DATABASE_CREATE = "CREATE TABLE "+ BROKER_TABLE_NAME
            + " ( `"+ BROKER_COLUMN_ID +"` INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "`" + BROKER_COLUMN_COMMAND +"` TEXT, "
            + "`" + BROKER_COLUMN_IS_SYNCED +"` INTEGER DEFAULT 0, "
            + "`" + BROKER_COLUMN_CREATE_DATE +"` TEXT DEFAULT current_timestamp, "
            + "`" + BROKER_COLUMN_BY_USER +"` INTEGER)";

    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private BufferDbHelper dbHelper;

    public Broker(Context context) {
        this.context = context;
        this.dbHelper=new BufferDbHelper(context);
    }

    public Broker open() throws SQLException {
        try {
            this.db=dbHelper.getWritableDatabase();

            Log.i(LOG_TAG, "connection open");
            return this;

        } catch (SQLException s) {
            new Exception("Error with DB Open");
            return this;

        }

    }

    public void close(){
        Log.i(LOG_TAG, "connection close");
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry(BrokerMessage bm) {
        return insertEntry(bm.getCommand(), bm.isSynced());
    }


    public synchronized long insertEntry(String command,boolean isSynced) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        Log.i(LOG_TAG, command);
        Log.i(LOG_TAG, "is "+isSynced);

        val.put(BROKER_COLUMN_COMMAND, command);
        val.put(BROKER_COLUMN_IS_SYNCED, (isSynced ? 1 : 0));
        try {
            long index=db.insert(BROKER_TABLE_NAME, null, val);
            return index;
        } catch (SQLException ex) {
            Log.e("Broker insertEntry", "inserting Entry at " + BROKER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public synchronized List<BrokerMessage> getAllNotSyncedCommand() {
        List<BrokerMessage> bMessages = new ArrayList<BrokerMessage>();
        try {
            open();
        Cursor cursor = db.rawQuery("select * from " + BROKER_TABLE_NAME + " where "+BROKER_COLUMN_IS_SYNCED+"=0 order by " + BROKER_COLUMN_ID + " asc ", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            bMessages.add(makeBrokerMessage(cursor));
            cursor.moveToNext();
        }
            cursor.close();
            close();

        } catch (Exception e) {

        }
        return bMessages;
    }

    public synchronized int deleteEntry(int id) {

        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.

        try {
            db.delete(BROKER_TABLE_NAME,BROKER_COLUMN_IS_SYNCED+"=?",new String[]{"1"});
            return 1;
        } catch (SQLException ex) {
            Log.e("Broker deleteEntry", "Delete entry into " + BROKER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public synchronized void Synced(int id) {
        try {
            open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(BROKER_COLUMN_IS_SYNCED, 1);

        String where = BROKER_COLUMN_ID + " = ?";
        db.update(BROKER_TABLE_NAME, val, where, new String[]{id + ""});
            close();

        } catch (Exception e) {

        }
    }

    private BrokerMessage makeBrokerMessage(Cursor c){
        return new BrokerMessage(c.getInt(c.getColumnIndex(BROKER_COLUMN_ID)),
                c.getString(c.getColumnIndex(BROKER_COLUMN_COMMAND)),
                c.getInt(c.getColumnIndex(BROKER_COLUMN_IS_SYNCED)));
    }

}
