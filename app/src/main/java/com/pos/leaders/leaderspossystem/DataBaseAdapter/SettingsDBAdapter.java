package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

/**
 * Created by KARAM on 19/01/2017.
 */

public class SettingsDBAdapter {
    // Table Name
    public static final String SETTINGS_TABLE_NAME = "tbl_settings";
    // Column Names
    protected static final String SETTINGS_COLUMN_COMPANY_ID = "company_id";
    protected static final String SETTINGS_COLUMN_COMPANY_NAME = "company_name";
    protected static final String SETTINGS_COLUMN_POS_NUMBER = "pos_number";
    protected static final String SETTINGS_COLUMN_TAX = "tax";

    protected static final String SETTINGS_COLUMN_RETURN_NOTE = "return_note";
    protected static final String SETTINGS_COLUMN_END_OF_REPLACEMENT_NOTE = "eorn";
    protected static final String SETTINGS_COLUMN_CREDIT_CARD_USERNAME = "cc_un";
    protected static final String SETTINGS_COLUMN_CREDIT_CARD_PASSWORD = "cc_pw";
    protected static final String SETTINGS_COLUMN_BRANCH_ID = "branchId";

    public static final String DATABASE_CREATE= "CREATE TABLE tbl_settings ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,"+"`company_id` TEXT NOT NULL,"+"`company_name` TEXT NOT NULL,"+" `pos_number` TEXT,"+"`tax` REAL NOT NULL,"+" `return_note` TEXT NOT NULL,"+" `eorn` TEXT NOT NULL,"+" `cc_un` TEXT NOT NULL,"+"`cc_pw` TEXT NOT NULL,"+"`branchId` INTEGER NOT NULL  )";


    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public SettingsDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public SettingsDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public boolean GetSettings() {
        Cursor cursor = db.rawQuery("select * from " + SETTINGS_TABLE_NAME , null);
        if (cursor.getCount() < 1) // NO DATA HAS BEEN SET
        {
            cursor.close();
            return false;
        }
        read(cursor);
        cursor.close();

        return true;
    }
    public int getRowCount(){
        Cursor cursor = db.rawQuery("select * from " + SETTINGS_TABLE_NAME , null);
        return cursor.getCount();
    }

    public int insertEntry(String companyID,String companyName,String posNumber,float tax,String returnNote,int eorn,String ccUserName,String ccPass,int branchId){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(SETTINGS_COLUMN_COMPANY_ID,companyID);
        val.put(SETTINGS_COLUMN_COMPANY_NAME, companyName);
        val.put(SETTINGS_COLUMN_POS_NUMBER, posNumber);
        val.put(SETTINGS_COLUMN_TAX, tax);
        val.put(SETTINGS_COLUMN_RETURN_NOTE, returnNote);
        val.put(SETTINGS_COLUMN_END_OF_REPLACEMENT_NOTE, eorn);
        val.put(SETTINGS_COLUMN_CREDIT_CARD_USERNAME, ccUserName);
        val.put(SETTINGS_COLUMN_CREDIT_CARD_PASSWORD, ccPass);
        val.put(SETTINGS_COLUMN_BRANCH_ID, branchId);

        try {

                    db.insert(SETTINGS_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("Settings insertEntry", "inserting Entry at " + SETTINGS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public int updateEntry(String companyID,String companyName,String posNumber,float tax,String returnNote,int eorn,String ccUserName,String ccPass,int branchId){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(SETTINGS_COLUMN_COMPANY_ID,companyID);
        val.put(SETTINGS_COLUMN_COMPANY_NAME, companyName);
        val.put(SETTINGS_COLUMN_POS_NUMBER, posNumber);
        val.put(SETTINGS_COLUMN_TAX, tax);
        val.put(SETTINGS_COLUMN_RETURN_NOTE, returnNote);
        val.put(SETTINGS_COLUMN_END_OF_REPLACEMENT_NOTE, eorn);
        val.put(SETTINGS_COLUMN_CREDIT_CARD_USERNAME, ccUserName);
        val.put(SETTINGS_COLUMN_CREDIT_CARD_PASSWORD, ccPass);
        val.put(SETTINGS_COLUMN_BRANCH_ID, branchId);

        try {
            db.update(SETTINGS_TABLE_NAME,val,null,null);
            return 1;
        } catch (SQLException ex) {
            Log.e("Settings insertEntry", "inserting Entry at " + SETTINGS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }

    }

    public void read(Cursor cursor) {
        cursor.moveToFirst();
        SETTINGS.companyID = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_COMPANY_ID));
        SETTINGS.companyName = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_COMPANY_NAME));
        SETTINGS.posID = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_POS_NUMBER));
        SETTINGS.tax = cursor.getFloat(cursor.getColumnIndex(SETTINGS_COLUMN_TAX));

        SETTINGS.returnNote = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_RETURN_NOTE));
        SETTINGS.endOfInvoice = cursor.getInt(cursor.getColumnIndex(SETTINGS_COLUMN_END_OF_REPLACEMENT_NOTE));
        SETTINGS.ccNumber = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_CREDIT_CARD_USERNAME));
        SETTINGS.ccPassword = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_CREDIT_CARD_PASSWORD));
        SETTINGS.branchId=cursor.getInt(cursor.getColumnIndex(SETTINGS_COLUMN_BRANCH_ID));
        cursor.close();
    }
    public void readSetting() {
        Cursor cursor = db.rawQuery("select * from " + SETTINGS_TABLE_NAME , null);
        if (cursor.getCount() < 1) // NO DATA HAS BEEN SET
        {
            cursor.close();
        }
        cursor.moveToFirst();
        SETTINGS.companyID = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_COMPANY_ID));
        SETTINGS.companyName = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_COMPANY_NAME));
        SETTINGS.posID = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_POS_NUMBER));
        SETTINGS.tax = cursor.getFloat(cursor.getColumnIndex(SETTINGS_COLUMN_TAX));

        SETTINGS.returnNote = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_RETURN_NOTE));
        SETTINGS.endOfInvoice = cursor.getInt(cursor.getColumnIndex(SETTINGS_COLUMN_END_OF_REPLACEMENT_NOTE));
        SETTINGS.ccNumber = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_CREDIT_CARD_USERNAME));
        SETTINGS.ccPassword = cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_CREDIT_CARD_PASSWORD));
        SETTINGS.branchId=cursor.getInt(cursor.getColumnIndex(SETTINGS_COLUMN_BRANCH_ID));
        cursor.close();
    }


}
