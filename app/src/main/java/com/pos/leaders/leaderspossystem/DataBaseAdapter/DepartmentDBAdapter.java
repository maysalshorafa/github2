package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

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

    public static final String DATABASE_CREATE = "CREATE TABLE departments ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`name` TEXT NOT NULL , `creatingDate` TEXT NOT NULL DEFAULT current_timestamp, " +
            "`byUser` INTEGER, `hide` INTEGER DEFAULT 0, FOREIGN KEY(`byUser`) REFERENCES `users.id` )";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;


    public DepartmentDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public DepartmentDBAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry(String name, long byUser) {
        Department department = new Department(Util.idHealth(this.db, DEPARTMENTS_TABLE_NAME, DEPARTMENTS_COLUMN_ID), name, new Date().getTime(), byUser, false);
        Department boDepartment = department;
        boDepartment.setName(Util.getString(boDepartment.getName()));
        Log.d("test", boDepartment.getName());
        sendToBroker(MessageType.ADD_DEPARTMENT, boDepartment, this.context);

        try {
            return insertEntry(department);
        } catch (SQLException ex) {
            Log.e("DepartmentDB insert", "inserting Entry at " + DEPARTMENTS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(Department department) {
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(DEPARTMENTS_COLUMN_ID, department.getId());
        val.put(DEPARTMENTS_COLUMN_NAME, department.getName());
        val.put(DEPARTMENTS_COLUMN_BYUSER, department.getByUser());
        val.put(DEPARTMENTS_COLUMN_CREATINGDATE, department.getCreatingDate());
        val.put(DEPARTMENTS_COLUMN_DISENABLED, department.isHide() ? 1 : 0);

        try {

            return db.insert(DEPARTMENTS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("DepartmentDB insert", "inserting Entry at " + DEPARTMENTS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }

    }

    public Department getDepartmentByID(long id) {
        Department department = null;
        Cursor cursor = db.rawQuery("select * from " + DEPARTMENTS_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return department;
        }
        cursor.moveToFirst();
        department = new Department(id, cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_NAME)),
                cursor.getLong(cursor.getColumnIndex(DEPARTMENTS_COLUMN_CREATINGDATE)),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_BYUSER))), Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_DISENABLED))));
        cursor.close();

        return department;
    }

    public int deleteEntry(long id) {
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
        val.put(DEPARTMENTS_COLUMN_CREATINGDATE, department.getCreatingDate());
        val.put(DEPARTMENTS_COLUMN_BYUSER, department.getByUser());
        val.put(DEPARTMENTS_COLUMN_DISENABLED, department.isHide());

        String where = DEPARTMENTS_COLUMN_ID + " = ?";
        db.update(DEPARTMENTS_TABLE_NAME, val, where, new String[]{department.getId() + ""});
    }

    public List<Department> getAllDepartments() {
        List<Department> departmentList = new ArrayList<Department>();

        Cursor cursor = db.rawQuery("select * from " + DEPARTMENTS_TABLE_NAME + " where " + DEPARTMENTS_COLUMN_DISENABLED + "=0 order by id desc", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            departmentList.add(new Department(Long.parseLong(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_NAME)),
                    cursor.getLong(cursor.getColumnIndex(DEPARTMENTS_COLUMN_CREATINGDATE)),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_BYUSER))),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_DISENABLED)))));
            cursor.moveToNext();
        }

        return departmentList;
    }

    public List<Department> getAllUserDepartments(long userId) {
        List<Department> userDepartmentList = new ArrayList<Department>();
        List<Department> departmentList = getAllDepartments();
        for (Department d : departmentList) {
            if (d.getByUser() == userId)
                userDepartmentList.add(d);
        }
        return userDepartmentList;
    }

    public List<Department> getAllDepartmentByHint(String hint) {
        List<Department> departmentList = new ArrayList<Department>();

        Cursor cursor = db.rawQuery("select * from " + DEPARTMENTS_TABLE_NAME + " where " + DEPARTMENTS_COLUMN_NAME + " like '%" +
                hint + "%' " + "and " + DEPARTMENTS_COLUMN_DISENABLED + "=0 order by id desc  ", null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            departmentList.add(makeDepartment(cursor));
            cursor.moveToNext();
        }

        return departmentList;
    }

    private Department makeDepartment(Cursor cursor) {
        try {
            Department d = new Department(new Department(Long.parseLong(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_NAME)),
                    cursor.getLong(cursor.getColumnIndex(DEPARTMENTS_COLUMN_CREATINGDATE)),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_BYUSER))),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_DISENABLED)))));

            return d;
        } catch (Exception ex) {
            Department d = new Department(new Department(Long.parseLong(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_NAME)),
                    cursor.getLong(cursor.getColumnIndex(DEPARTMENTS_COLUMN_CREATINGDATE)),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_BYUSER))),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(DEPARTMENTS_COLUMN_DISENABLED)))));

            return d;
        }
    }
    public boolean availableDepartmentName(String departmentName) {
        Cursor cursor = db.query(DEPARTMENTS_TABLE_NAME, null, DEPARTMENTS_COLUMN_NAME + "=?", new String[]{departmentName}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            // Department Name not available
            return false;
        }
        // Department Name available
        return true;
    }
}
