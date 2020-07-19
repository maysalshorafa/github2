package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.LincessPos;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 4/9/2020.
 */

public class LincessDBAdapter {

    public static final String POS_LINCESS_TABLE_NAME = "PosLincess";

    public static final String POS_LINCESS_COLUMN_ID = "id";
    public static final String POS_LINCESS_COLUMN_COMPANY_ID = "companyId";
    public static final String POS_LINCESS_COLUMN_BRANCH_ID = "branchId";
    public static final String POS_LINCESS_COLUMN_ACTIVATION_DATE = "activationDate";
    public static final String POS_LINCESS_COLUMN_DUE_DATE = "dueDate";
    public static final String POS_LINCESS_COLUMN_NOTE = "note";
    public static final String POS_LINCESS_COLUMN_STATUS = "status";


    public static final String DATABASE_CREATE = "CREATE TABLE " + POS_LINCESS_TABLE_NAME
            + " ( `" + POS_LINCESS_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, `" + POS_LINCESS_COLUMN_COMPANY_ID + "` TEXT ,  `"
            + POS_LINCESS_COLUMN_BRANCH_ID + "` TEXT, `" + POS_LINCESS_COLUMN_NOTE + "` TEXT, `" + POS_LINCESS_COLUMN_ACTIVATION_DATE + "` TIMESTAMP DEFAULT current_timestamp , `"
            + POS_LINCESS_COLUMN_DUE_DATE + "` TIMESTAMP DEFAULT current_timestamp , `" + POS_LINCESS_COLUMN_STATUS + "` TEXT)";


    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public LincessDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public LincessDBAdapter open() throws SQLException {
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


    public long insertEntry(String companyId, String Note, String branchId, Timestamp activationDate, Timestamp dueDate, String statusLincess) {

        if (db.isOpen()) {

        } else {
            try {
                open();
            } catch (SQLException ex) {
                Log.d("Exception", ex.toString());
            }
        }

        LincessPos lincessPos = new LincessPos(Util.idHealth(this.db, POS_LINCESS_TABLE_NAME, POS_LINCESS_COLUMN_ID), companyId, Note, branchId, activationDate, dueDate, statusLincess);
        if (db.isOpen()) {

        } else {
            open();
        }
        try {
            close();
            return insertEntry(lincessPos);
        } catch (SQLException ex) {
            Log.d("SQLException", ex.toString());

            Log.e(POS_LINCESS_TABLE_NAME + " DB insert", "inserting Entry at " + POS_LINCESS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }


    public long insertEntry(LincessPos lincessPos) {
        if (db.isOpen()) {

        } else {
            try {
                open();
            } catch (SQLException ex) {
                Log.d("Exception", ex.toString());
            }
        }
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(POS_LINCESS_COLUMN_ID, lincessPos.getId());
        val.put(POS_LINCESS_COLUMN_ACTIVATION_DATE, String.valueOf(lincessPos.getActivationDate()));
        val.put(POS_LINCESS_COLUMN_COMPANY_ID, lincessPos.getCompanyId());
        val.put(POS_LINCESS_COLUMN_BRANCH_ID, lincessPos.getBranchId());
        val.put(POS_LINCESS_COLUMN_DUE_DATE, String.valueOf(lincessPos.getDueDate()));
        val.put(POS_LINCESS_COLUMN_NOTE, lincessPos.getNote());
        val.put(POS_LINCESS_COLUMN_STATUS, lincessPos.getStatusLincess());
        Log.d("testLincesPOs", lincessPos.toString());


        try {
            return db.insert(POS_LINCESS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.d("SQLException", ex.toString());
            Log.e(POS_LINCESS_TABLE_NAME + " DB insert", "inserting Entry at " + POS_LINCESS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }


    public int updateEntry(String statusLincess,long idLincess) {
        if (db.isOpen()) {

        } else {
            try {
                open();
            } catch (SQLException ex) {
                Log.d("Exception", ex.toString());
            }
        }
        ContentValues updatedValues = new ContentValues();
        //Assign values for each row.
        String where = POS_LINCESS_COLUMN_ID + " = ?";
        updatedValues.put(POS_LINCESS_COLUMN_STATUS, statusLincess);

        try {
            db.update(POS_LINCESS_TABLE_NAME, updatedValues, where, new String[]{idLincess + ""});
         //   db.update(POS_LINCESS_TABLE_NAME, val, null, null);
            return 1;
        } catch (SQLException ex) {
            Log.e("Lincess update", "update Entry at " + POS_LINCESS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }

    }

    public int updateEntry(long idLincess,String statusLincess, Timestamp activationDate, Timestamp dueDate, String branchId, String companyId, String note) {
        if (db.isOpen()) {

        } else {
            try {
                open();
            } catch (SQLException ex) {
                Log.d("Exception", ex.toString());
            }
        }
        ContentValues updatedValues = new ContentValues();
        //Assign values for each row.
        String where = POS_LINCESS_COLUMN_ID + " = ?";
        updatedValues.put(POS_LINCESS_COLUMN_STATUS, statusLincess);
        updatedValues.put(POS_LINCESS_COLUMN_ACTIVATION_DATE, String.valueOf(activationDate));
        updatedValues.put(POS_LINCESS_COLUMN_DUE_DATE, String.valueOf(dueDate));
        updatedValues.put(POS_LINCESS_COLUMN_BRANCH_ID, branchId);
        updatedValues.put(POS_LINCESS_COLUMN_COMPANY_ID, companyId);
        updatedValues.put(POS_LINCESS_COLUMN_NOTE, note);
        Log.d("dueDateUPDate",dueDate +"  "+activationDate+statusLincess);
        try {
            db.update(POS_LINCESS_TABLE_NAME, updatedValues, where, new String[]{idLincess + ""});
           // db.update(POS_LINCESS_TABLE_NAME, val, null, null);
            return 1;
        } catch (SQLException ex) {
            Log.e("Lincess update", "update Entry at " + POS_LINCESS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }

    }

    public boolean GetLincess() {
        if (db.isOpen()) {

        } else {
            try {
                open();
            } catch (SQLException ex) {
                Log.d("Exception", ex.toString());
            }
        }
       Cursor cursor = db.rawQuery("SELECT * FROM " + POS_LINCESS_TABLE_NAME + " ORDER BY id DESC LIMIT 1", null);
       // Cursor cursor = db.rawQuery("select * from " + POS_LINCESS_TABLE_NAME, null);
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

    public LincessPos GetLincessID() {
        if (db.isOpen()) {

        } else {
            try {
                open();
            } catch (SQLException ex) {
                Log.d("Exception", ex.toString());
            }
        }
        LincessPos lincessPos = new LincessPos();
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM " + POS_LINCESS_TABLE_NAME + " ORDER BY id DESC LIMIT 1", null);
        cursor.moveToFirst();
        if (cursor != null && cursor.getCount() != 0) {
            lincessPos = build(cursor);
            Log.d("lincessPos", lincessPos.toString());
        }
        return lincessPos;
    }

    public void read(Cursor cursor) {
        if (db.isOpen()) {

        } else {
            try {
                open();
            } catch (SQLException ex) {
                Log.d("Exception", ex.toString());
            }
        }
        cursor.moveToFirst();
        SETTINGS.statusLincess = cursor.getString(cursor.getColumnIndex(POS_LINCESS_COLUMN_STATUS));
        SETTINGS.dueDate = cursor.getString(cursor.getColumnIndex(POS_LINCESS_COLUMN_DUE_DATE));
        Log.d("dueDateDB",SETTINGS.dueDate);
        cursor.close();
        close();
    }


    private LincessPos build(Cursor cursor) {
        return new LincessPos(Long.parseLong(cursor.getString(cursor.getColumnIndex(POS_LINCESS_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(POS_LINCESS_COLUMN_COMPANY_ID)),
                cursor.getString(cursor.getColumnIndex(POS_LINCESS_COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(POS_LINCESS_COLUMN_BRANCH_ID)),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(POS_LINCESS_COLUMN_ACTIVATION_DATE))),
                Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(POS_LINCESS_COLUMN_DUE_DATE))),
                cursor.getString(cursor.getColumnIndex(POS_LINCESS_COLUMN_STATUS)));
    }
}