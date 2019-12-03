package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Group;
import com.pos.leaders.leaderspossystem.Tools.Util;

/**
 * Created by Karam on 8/12/2018.
 */

public class GroupDbAdapter {
    public static final String GROUP_TABLE_NAME ="groups";

    private static final String GROUP_COLUMN_ID ="id";
    private static final String GROUP_COLUMN_NAME ="name";

    public static final String DATABASE_CREATE = "CREATE TABLE " + GROUP_TABLE_NAME + " ( `" + GROUP_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`" + GROUP_COLUMN_NAME + "` TEXT NOT NULL)";
    // Variable to hold the database instance
    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public GroupDbAdapter(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }

    public GroupDbAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public long insertEntry(String name) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        Group group = new Group(Util.idHealth(this.db, GROUP_TABLE_NAME, GROUP_COLUMN_ID),name);
close();
        return insertEntry(group);
    }

    public long insertEntry(long Id,String name) {
        if(db.isOpen()){

        }else {
            try {
                open();
            }
            catch (SQLException ex) {
                Log.d("Exception",ex.toString());
            }
        }
        Group group = new Group(Id, name);
close();
        return insertEntry(group);
    }

    public long insertEntry(Group group) {
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

        val.put(GROUP_COLUMN_ID, group.getId());
        val.put(GROUP_COLUMN_NAME, group.getName());
        try {
            return db.insert(GROUP_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("Groups insert", "inserting Entry at " + GROUP_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public Group groupById (long groupId) {
        Group group = null;
        Cursor cursor = db.rawQuery("select * from " + GROUP_TABLE_NAME + " where " + GROUP_COLUMN_ID + "='" + groupId + "' ;", null);
        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            group = makeGroup(cursor);
        }
        cursor.close();
        return group;
    }

    public boolean deleteGroup(long groupId) {
        return db.delete(GROUP_TABLE_NAME, GROUP_COLUMN_ID + "=" + groupId, null) > 0;
    }

    private Group makeGroup(Cursor cursor) {
        try {
            return new Group(Long.parseLong(cursor.getString(cursor.getColumnIndex(GROUP_COLUMN_ID))), cursor.getString(cursor.getColumnIndex(GROUP_COLUMN_NAME)));
        } catch (Exception ex) {
            return null;

        }

    }
}
