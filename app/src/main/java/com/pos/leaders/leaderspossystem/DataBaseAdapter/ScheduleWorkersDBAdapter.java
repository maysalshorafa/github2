package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 19/10/2016.
 */

public class ScheduleWorkersDBAdapter {
    protected static final String SCHEDULEWORKERS_TABLE_NAME = "scheduleWorkers";
    // Column Names
    protected static final String SCHEDULEWORKERS_COLUMN_ID = "id";
    protected static final String SCHEDULEWORKERS_COLUMN_USERID = "userId";
    protected static final String SCHEDULEWORKERS_COLUMN_DATE = "date";
    protected static final String SCHEDULEWORKERS_COLUMN_STARTTIME = "startTime";
    protected static final String SCHEDULEWORKERS_COLUMN_EXITTIME = "exitTime";

    public static final String DATABASE_CREATE="CREATE TABLE scheduleWorkers ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "`userId` INTEGER, `date` TEXT DEFAULT current_timestamp, `startTime` TEXT DEFAULT current_timestamp, `exitTime` TEXT, "+
            "FOREIGN KEY(`userId`) REFERENCES `users.id` )";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public ScheduleWorkersDBAdapter(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }

    public ScheduleWorkersDBAdapter open() throws SQLException {
        this.db=dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public long insertEntry(long userId) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(SCHEDULEWORKERS_COLUMN_ID, Util.idHealth(this.db, SCHEDULEWORKERS_TABLE_NAME, SCHEDULEWORKERS_COLUMN_ID));
        val.put(SCHEDULEWORKERS_COLUMN_USERID, userId);
        val.put(SCHEDULEWORKERS_COLUMN_DATE,new Date().getTime());
        val.put(SCHEDULEWORKERS_COLUMN_STARTTIME, new Date().getTime());

        try {
            long lastID = db.insert(SCHEDULEWORKERS_TABLE_NAME, null, val);
            return lastID;
        } catch (SQLException ex) {
            Log.e("SchWorkersDB insert", "inserting Entry at " + SCHEDULEWORKERS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public ScheduleWorkers getScheduleWorkersByID(long id) {
        ScheduleWorkers scheduleWorkers = null;
        Cursor cursor = db.rawQuery("select * from " + SCHEDULEWORKERS_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return scheduleWorkers;
        }
        cursor.moveToFirst();
        scheduleWorkers = new ScheduleWorkers(id,Long.parseLong(cursor.getString(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_USERID))),
                cursor.getLong(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_DATE)),
                cursor.getLong(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_STARTTIME)),
                cursor.getLong(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_EXITTIME)));

        cursor.close();

        return scheduleWorkers;
    }

    public void updateEntry(ScheduleWorkers scheduleWorkers) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(SCHEDULEWORKERS_COLUMN_USERID, scheduleWorkers.getUserId());
        val.put(SCHEDULEWORKERS_COLUMN_DATE, scheduleWorkers.getDate());
        val.put(SCHEDULEWORKERS_COLUMN_STARTTIME, scheduleWorkers.getStartTime());
        val.put(SCHEDULEWORKERS_COLUMN_EXITTIME, scheduleWorkers.getExitTime());

        String where = SCHEDULEWORKERS_COLUMN_ID + " = ?";
        db.update(SCHEDULEWORKERS_TABLE_NAME, val, where, new String[]{scheduleWorkers.getId() + ""});
    }

    public void updateEntry(long id,Date exitTime) {
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(SCHEDULEWORKERS_COLUMN_EXITTIME, exitTime.getTime());

        String where = SCHEDULEWORKERS_COLUMN_ID + " = ?";
        db.update(SCHEDULEWORKERS_TABLE_NAME, val, where, new String[]{id + ""});
    }

    public List<ScheduleWorkers> getAllScheduleWorkers(){
        List<ScheduleWorkers> scheduleWorkersList =new ArrayList<ScheduleWorkers>();

        Cursor cursor =  db.rawQuery( "select * from "+SCHEDULEWORKERS_TABLE_NAME, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            scheduleWorkersList.add(new ScheduleWorkers(Long.parseLong(cursor.getString(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_ID))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_USERID))),
                    cursor.getLong(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_DATE)),
                    cursor.getLong(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_STARTTIME)),
                    cursor.getLong(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_EXITTIME))));
            cursor.moveToNext();
        }

        return scheduleWorkersList;
    }

    public List<ScheduleWorkers> getAllUserScheduleWorkBtweenToDate(long userId , Date from , Date to){
        List<ScheduleWorkers> userScheduleWorkerstList=new ArrayList<ScheduleWorkers>();
        List<ScheduleWorkers> scheduleWorkersList=getAllScheduleWorkers();
        for (ScheduleWorkers item:scheduleWorkersList) {
            if(item.getUserId()==userId && item.getStartTime() >= from.getTime() && item.getExitTime()<=to.getTime() )
				userScheduleWorkerstList.add(item);
        }
        return userScheduleWorkerstList;
    }
    public List<ScheduleWorkers> getAllUserScheduleWork(long userId){
        List<ScheduleWorkers> userScheduleWorkerstList=new ArrayList<ScheduleWorkers>();
        List<ScheduleWorkers> scheduleWorkersList=getAllScheduleWorkers();
        for (ScheduleWorkers item:scheduleWorkersList) {
            if(item.getUserId()==userId)
                userScheduleWorkerstList.add(item);
        }
        return userScheduleWorkerstList;
    }

    public ScheduleWorkers getScheduleWorkersByUserID(long userId) {
        ScheduleWorkers scheduleWorkers = null;
        List<ScheduleWorkers> scheduleWorkersList=getAllUserScheduleWork(userId);
        for (ScheduleWorkers item:scheduleWorkersList) {
            long val = item.getDate();
            Date date=new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            if(df2.format(date).equals(df2.format(new Date())))
                return item;
        }
        return scheduleWorkers;
    }

}
