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

    protected static final String SETTINGS_COLUMN_CURRENCY_CODE="currency_code";
    protected static final String SETTINGS_COLUMN_CURRENCY_SYMBOL="currency_symbol";
    protected static final String SETTINGS_COLUMN_COUNTRY="country";

    public static final String DATABASE_CREATE= "CREATE TABLE tbl_settings ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,"+"`company_id` TEXT NOT NULL,"+"`company_name` TEXT NOT NULL,"+" `pos_number` TEXT,"+"`tax` REAL NOT NULL,"+" `return_note` TEXT NOT NULL,"+" `eorn` TEXT NOT NULL,"+" `cc_un` TEXT NOT NULL,"+"`cc_pw` TEXT NOT NULL,"+"`branchId` INTEGER NOT NULL,"+" `currency_code` TEXT NOT NULL,"+" `currency_symbol` TEXT NOT NULL,"+" `country` TEXT NOT NULL)";


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
        try {
            this.db = dbHelper.getWritableDatabase();
            return this;

        } catch (SQLException s) {
            new Exception("Error with DB Open");
            return this;

        }
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public boolean GetSettings() {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        Cursor cursor = db.rawQuery("select * from " + SETTINGS_TABLE_NAME , null);
        if (cursor.getCount() < 1) // NO DATA HAS BEEN SET
        {
            cursor.close();
            return false;
        }
        read(cursor);
        cursor.close();
       close();
        return true;
    }
    public int getRowCount(){
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        Cursor cursor = db.rawQuery("select * from " + SETTINGS_TABLE_NAME , null);
        close();
        return cursor.getCount();
    }

    public int insertEntry(String companyID,String companyName,String posNumber,float tax,String returnNote,int eorn,String ccUserName,String ccPass,int branchId,String currencyCode,
                           String currencySymbol,String country){
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
        val.put(SETTINGS_COLUMN_CURRENCY_CODE,currencyCode);
        val.put(SETTINGS_COLUMN_CURRENCY_SYMBOL,currencySymbol);
        val.put(SETTINGS_COLUMN_COUNTRY,country);

        try {

                    db.insert(SETTINGS_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("Settings insertEntry", "inserting Entry at " + SETTINGS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public int updateEntry(String companyID,String companyName,String posNumber,float tax,String returnNote,int eorn,String ccUserName,String ccPass,int branchId,String currencyCode
    ,String currencySymbol,String country){
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
        val.put(SETTINGS_COLUMN_CURRENCY_CODE,currencyCode);
        val.put(SETTINGS_COLUMN_CURRENCY_SYMBOL,currencySymbol);
        val.put(SETTINGS_COLUMN_COUNTRY,country);

        try {
            db.update(SETTINGS_TABLE_NAME,val,null,null);
            return 1;
        } catch (SQLException ex) {
            Log.e("Settings insertEntry", "inserting Entry at " + SETTINGS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }

    }

    public void read(Cursor cursor) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
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
        SETTINGS.currencyCode=cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_CURRENCY_CODE));
        SETTINGS.currencySymbol=cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_CURRENCY_SYMBOL));
        SETTINGS.country=cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_COUNTRY));
        cursor.close();
        close();
    }
    public void readSetting() {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
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
        SETTINGS.currencyCode=cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_CURRENCY_CODE));
        SETTINGS.currencySymbol=cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_CURRENCY_SYMBOL));
        SETTINGS.country=cursor.getString(cursor.getColumnIndex(SETTINGS_COLUMN_COUNTRY));
        cursor.close();
        close();
    }
    public static String addColumnInteger(String columnName) {
        String dbc = "ALTER TABLE " + SETTINGS_TABLE_NAME
                + " add column " + columnName + " INTEGER  DEFAULT 0 ;";
        return dbc;
    }

    public static String addColumnText(String columnName) {
        String dbc = "ALTER TABLE " + SETTINGS_TABLE_NAME
                + " add column " + columnName + " TEXT  DEFAULT '' ;";
        return dbc;
    }
}
