package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.BoScheduleWorkers;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 19/10/2016.
 */

public class ScheduleWorkersDBAdapter {
    protected static final String SCHEDULEWORKERS_TABLE_NAME = "scheduleWorkers";
    // Column Names
    protected static final String SCHEDULEWORKERS_COLUMN_ID = "id";
    protected static final String SCHEDULEWORKERS_COLUMN_USERID = "employeeId";
    protected static final String SCHEDULEWORKERS_COLUMN_DATE = "date";
    protected static final String SCHEDULEWORKERS_COLUMN_STARTTIME = "startTime";
    protected static final String SCHEDULEWORKERS_COLUMN_EXITTIME = "exitTime";

    public static final String DATABASE_CREATE="CREATE TABLE scheduleWorkers ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "`employeeId` INTEGER, `date` TEXT DEFAULT current_timestamp, `startTime` TEXT, `exitTime` TEXT, "+
            "FOREIGN KEY(`employeeId`) REFERENCES `employees.id` )";
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
            this.db = dbHelper.getWritableDatabase();
            return this;
    }

    public void close(){
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }
    //insert region
    // normal insert region with start time
    public long insertEntry(long userId) {
        ScheduleWorkers scheduleWorkers = new ScheduleWorkers(Util.idHealth(this.db, SCHEDULEWORKERS_TABLE_NAME, SCHEDULEWORKERS_COLUMN_ID), userId,new Date().getTime(),  new Date().getTime(),0);
            BoScheduleWorkers boScheduleWorkers =new BoScheduleWorkers(Util.idHealth(this.db, SCHEDULEWORKERS_TABLE_NAME, SCHEDULEWORKERS_COLUMN_ID), userId,new Date().getTime(),  new Date().getTime());
        sendToBroker(MessageType.ADD_SCHEDULE_WORKERS, boScheduleWorkers, this.context);
        try {
            return insertEntry(scheduleWorkers);
        } catch (SQLException ex) {
            Log.e(SCHEDULEWORKERS_TABLE_NAME +" DB insert", "inserting Entry at " + SCHEDULEWORKERS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }
    // end
    // insert exit time and last row have start and exit time first step insert new row with exit time then send it
    public long insertEntryExitTime(long userId) {
        ScheduleWorkers scheduleWorkers = new ScheduleWorkers(Util.idHealth(this.db, SCHEDULEWORKERS_TABLE_NAME, SCHEDULEWORKERS_COLUMN_ID), userId,new Date().getTime(),  0,new Date().getTime());
        BoScheduleWorkers boScheduleWorkers =new BoScheduleWorkers(Util.idHealth(this.db, SCHEDULEWORKERS_TABLE_NAME, SCHEDULEWORKERS_COLUMN_ID), userId,new Date().getTime(),  new Date().getTime());
        sendToBroker(MessageType.ADD_SCHEDULE_WORKERS, boScheduleWorkers, this.context);
        try {
            return insertEntry(scheduleWorkers);
        } catch (SQLException ex) {
            Log.e(SCHEDULEWORKERS_TABLE_NAME +" DB insert", "inserting Entry at " + SCHEDULEWORKERS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }
    //end

    public long insertEntry(ScheduleWorkers scheduleWorkers) {
        ContentValues val = new ContentValues();
        val.put(SCHEDULEWORKERS_COLUMN_ID,scheduleWorkers.getScheduleWorkersId());
        val.put(SCHEDULEWORKERS_COLUMN_USERID, scheduleWorkers.getUserId());
        val.put(SCHEDULEWORKERS_COLUMN_DATE, scheduleWorkers.getDate());
        val.put(SCHEDULEWORKERS_COLUMN_STARTTIME, scheduleWorkers.getStartTime());
        val.put(SCHEDULEWORKERS_COLUMN_EXITTIME, scheduleWorkers.getExitTime());

        try {
            return db.insert(SCHEDULEWORKERS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("SCHEDULEWORKERS DB insert", "inserting Entry at " + SCHEDULEWORKERS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    //end insert region for two cases
    //update region
    public void updateEntry(long userId,Date exitTime) {
        ScheduleWorkersDBAdapter scheduleWorkersDBAdapter =new ScheduleWorkersDBAdapter(context);
        scheduleWorkersDBAdapter.open();
        ScheduleWorkers scheduleWorkers=scheduleWorkersDBAdapter.getLastScheduleWorkersByUserID(userId); //get last row insert for this user
        ContentValues val = new ContentValues();
        if(scheduleWorkers.getExitTime()>0){
            //if row have exit and start time then insert new row
            long scheduleID = scheduleWorkersDBAdapter.insertEntryExitTime(userId);
            if(scheduleID>0){
                ScheduleWorkers s=scheduleWorkersDBAdapter.getLastScheduleWorkersByUserID(userId);
                Log.d("last row not empty for exit time",s.toString());
                sendToBroker(MessageType.ADD_SCHEDULE_WORKERS, s, this.context);
            }

        }else {
            //normal update case when exit time didnt have value
            val.put(SCHEDULEWORKERS_COLUMN_EXITTIME, exitTime.getTime());
            String where = SCHEDULEWORKERS_COLUMN_ID + " = ?";
            db.update(SCHEDULEWORKERS_TABLE_NAME, val, where, new String[]{scheduleWorkers.getScheduleWorkersId() + ""} );
            ScheduleWorkers s=scheduleWorkersDBAdapter.getLastScheduleWorkersByUserID(userId);
            Log.d("last row  empty for exit time",s.toString());
            sendToBroker(MessageType.UPDATE_SCHEDULE_WORKERS, s, this.context);
        }
        scheduleWorkersDBAdapter.close();

    }
    //end


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
        db.update(SCHEDULEWORKERS_TABLE_NAME, val, where, new String[]{scheduleWorkers.getScheduleWorkersId() + ""});
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
        try {
            if(dbHelper==null) {
                open();
            }
        for (ScheduleWorkers item:scheduleWorkersList) {
            if(item.getUserId()==userId && item.getExitTime()<=to.getTime()&& item.getStartTime()>=from.getTime() )
                userScheduleWorkerstList.add(item);
        }
        } catch (Exception e) {
            Log.d("exception",e.toString());

        }
        return userScheduleWorkerstList;
    }
    public List<ScheduleWorkers> getAllUserScheduleWork(long userId){
        List<ScheduleWorkers> userScheduleWorkerstList=new ArrayList<ScheduleWorkers>();
        List<ScheduleWorkers> scheduleWorkersList=getAllScheduleWorkers();
        try {
            if(dbHelper==null) {
                open();
            }
        for (ScheduleWorkers item:scheduleWorkersList) {
            if(item.getUserId()==userId)
                userScheduleWorkerstList.add(item);
        }
        } catch (Exception e) {
            Log.d("exception",e.toString());

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
    //get last row in table
    public ScheduleWorkers getLastScheduleWorkersByUserID(long userId) {
        ScheduleWorkers scheduleWorkers=null;
        Cursor cursor =  db.rawQuery( "select * from "+SCHEDULEWORKERS_TABLE_NAME +" where "+SCHEDULEWORKERS_COLUMN_USERID+" = "+ userId+ " order by id desc", null );
        cursor.moveToFirst();
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return scheduleWorkers;
        }
        cursor.moveToFirst();
        scheduleWorkers=  new ScheduleWorkers(Long.parseLong(cursor.getString(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_ID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_USERID))),
                cursor.getLong(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_DATE)),
                cursor.getLong(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_STARTTIME)),
                cursor.getLong(cursor.getColumnIndex(SCHEDULEWORKERS_COLUMN_EXITTIME)));

        cursor.close();
        return scheduleWorkers;

    }

}
