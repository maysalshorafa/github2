package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 17/08/2017.
 */

public class IdsCounterDBAdapter {

    // Table Name
    public static final String IDS_COUNTER_TABLE_NAME = "idsCounter";

    public static String DATABASE_CREATE(List<String> tables) {
        String dbc = "CREATE TABLE IF NOT EXISTS " + IDS_COUNTER_TABLE_NAME
                + " ( ";
        for (String s : tables)
            dbc += " `" + s + "` INTEGER,";
        dbc = dbc.substring(0, dbc.length() - 1);
        dbc += " );";

        return dbc;
    }
    public static String INIT(List<String> tables){
        String values = "",dbc="";
        for(int i=0;i<tables.size();i++){
            values += "0,";
        }
        values = values.substring(0, values.length() - 1);
        dbc+=" insert into "+IDS_COUNTER_TABLE_NAME+" values("+values+");";
        return dbc;
    }
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    private static boolean isEmpty = true;

    public IdsCounterDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public IdsCounterDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public long getID(String tableName) {
        long id = 0L;
        Cursor cursor = db.rawQuery("select " + tableName + " from " + IDS_COUNTER_TABLE_NAME, null);
        if (cursor.getCount() < 1) // aReport Not Exist
        {
            cursor.close();
            return id;
        }
        cursor.moveToFirst();
        id = cursor.getLong(0);
        cursor.close();

        return id;
    }


}
