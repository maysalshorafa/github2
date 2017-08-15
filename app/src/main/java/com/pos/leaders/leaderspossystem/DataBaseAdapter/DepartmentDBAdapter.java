package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Models.Department;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 18/10/2016.
 */

public class DepartmentDBAdapter {
    // Table Name
    public static final String DEPARTMENTS_TABLE_NAME = "departments";
    // Column Names
    protected static final String DEPARTMENTS_COLUMN_ID = "id";
    protected static final String DEPARTMENTS_COLUMN_NAME = "name";
    protected static final String DEPARTMENTS_COLUMN_CREATINGDATE = "creatingDate";
    protected static final String DEPARTMENTS_COLUMN_BYUSER = "byUser";
    protected static final String DEPARTMENTS_COLUMN_DISENABLED = "hide";

    public static final String DATABASE_CREATE="CREATE TABLE departments ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "`name` TEXT NOT NULL UNIQUE, `creatingDate` TEXT NOT NULL DEFAULT current_timestamp, "+
            "`byUser` INTEGER, `hide` INTEGER DEFAULT 0, FOREIGN KEY(`byUser`) REFERENCES `users.id` )";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public DepartmentDBAdapter(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }

    public DepartmentDBAdapter open() throws SQLException {
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

    public int insertEntry(String name,int byUser) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(DEPARTMENTS_COLUMN_NAME, name);
        val.put(DEPARTMENTS_COLUMN_BYUSER, byUser);
        try {
            db.insert(DEPARTMENTS_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("DepartmentDB insert", "insatring Entry at " + DEPARTMENTS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public Department getDepartmentByID(int id) {
        Department department = null;
        Cursor cursor = db.rawQuery("select * from " + DEPARTMENTS_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return department;
        }
        cursor.moveToFirst();
        department =new Department(id,cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_NAME)),
                DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_CREATINGDATE))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_BYUSER))),Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_DISENABLED))));
        cursor.close();

        return department;
    }

    public int deleteEntry(int id) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(DEPARTMENTS_COLUMN_DISENABLED, 1);

        String where = DEPARTMENTS_COLUMN_ID + " = ?";
        try {
            db.update(DEPARTMENTS_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("Department DB delete", "enable hide Entry at " + DEPARTMENTS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public void updateEntry(Department department) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(DEPARTMENTS_COLUMN_NAME, department.getName());
        val.put(DEPARTMENTS_COLUMN_CREATINGDATE, department.getCreatingDate().toString());
        val.put(DEPARTMENTS_COLUMN_BYUSER, department.getByUser());
        val.put(DEPARTMENTS_COLUMN_DISENABLED, department.isHide());

        String where = DEPARTMENTS_COLUMN_ID + " = ?";
        db.update(DEPARTMENTS_TABLE_NAME, val, where, new String[]{department.getId() + ""});
    }

    public List<Department> getAllDepartments(){
        List<Department> departmentList =new ArrayList<Department>();

        Cursor cursor =  db.rawQuery( "select * from "+DEPARTMENTS_TABLE_NAME, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            departmentList.add(new Department(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_NAME)),
                    DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_CREATINGDATE))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_BYUSER))),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_DISENABLED)))));
            cursor.moveToNext();
        }

        return departmentList;
    }
    public List<Department> getAllUserDepartments(int  userId){
        List<Department> userDepartmentList=new ArrayList<Department>();
        List<Department> departmentList=getAllDepartments();
        for (Department d:departmentList) {
            if(d.getByUser()==userId)
                userDepartmentList.add(d);
        }
        return userDepartmentList;
    }
}
