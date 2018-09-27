package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Permission.EmployeesPermissions;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 29/10/2016.
 */

public class EmployeePermissionsDBAdapter {
	// Table Name
	public static final String USERPERMISSIONS_TABLE_NAME = "EmployeePermissions";
	// Column Names
	protected static final String USERPERMISSIONS_COLUMN_USERID = "employeeId";
	protected static final String USERPERMISSIONS_COLUMN_PERMISSIONSID = "permissionId";

	protected static final String USERPERMISSIONS_COLUMN_ID = "id";

	public static final String DATABASE_CREATE = "CREATE TABLE `EmployeePermissions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `employeeId` INTEGER , `permissionId` INTEGER,"+
			"FOREIGN KEY(`employeeId`) REFERENCES `employees.id`)";
	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;

	public EmployeePermissionsDBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
	}

	public EmployeePermissionsDBAdapter open() throws SQLException {
		this.db = dbHelper.getWritableDatabase();
		return this;
	}



		public long insertEntry( long permissionsId, long userId) {
			EmployeesPermissions userPermissions = new EmployeesPermissions(Util.idHealth(this.db, USERPERMISSIONS_TABLE_NAME, USERPERMISSIONS_COLUMN_ID),userId,permissionsId);
			sendToBroker(MessageType.ADD_EMPLOYEE_PERMISSION, userPermissions, this.context);

			try {
				long insertResult = insertEntry(userPermissions);
				return insertResult;
			} catch (SQLException ex) {
				Log.e("UserPermissions insert", "inserting Entry at " + USERPERMISSIONS_TABLE_NAME + ": " + ex.getMessage());
				return 0;
			}
		}
	public long insertEntry(EmployeesPermissions userPermissions){
		ContentValues val = new ContentValues();
		val.put(USERPERMISSIONS_COLUMN_ID,userPermissions.getEmployeePermissionId());
		//Assign values for each row.
		val.put(USERPERMISSIONS_COLUMN_USERID, userPermissions.getEmployeeId());
		val.put(USERPERMISSIONS_COLUMN_PERMISSIONSID, userPermissions.getPermissionId());

		try {
			return db.insert(USERPERMISSIONS_TABLE_NAME, null, val);
		} catch (SQLException ex) {
			Log.e("employeePer insert", "inserting Entry at " + USERPERMISSIONS_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}
	public ArrayList<Integer> getPermissions(long user_id) {
		ArrayList<Integer> permissions = new ArrayList<Integer>();
		Cursor cursor1 = db.rawQuery("select * from " + USERPERMISSIONS_TABLE_NAME+ " where employeeId='" + user_id + "'" , null);
		cursor1.moveToFirst();

		if (cursor1.getCount() < 1) // UserName Not Exist
		{
			cursor1.close();
			return permissions;
		}
		while(!cursor1.isAfterLast()){
			permissions.add(cursor1.getInt(cursor1.getColumnIndex(USERPERMISSIONS_COLUMN_PERMISSIONSID)));
			cursor1.moveToNext();
		}
		return permissions;
	}
    public ArrayList<Long> getSalesManId() {
        ArrayList<Long> salesManId = new ArrayList<Long>();
        EmployeesPermissions userPermissions=null;
        Cursor cursor1 = db.rawQuery("select * from " + USERPERMISSIONS_TABLE_NAME+ " where permissionId='" + 10 + "'" , null);
        cursor1.moveToFirst();


        if (cursor1.getCount() < 1) // UserName Not Exist
        {
            cursor1.close();
            return  salesManId;
        }
        while(!cursor1.isAfterLast()){
            userPermissions= new EmployeesPermissions(Long.parseLong(cursor1.getString(cursor1.getColumnIndex(USERPERMISSIONS_COLUMN_ID))),Long.parseLong(cursor1.getString(cursor1.getColumnIndex(USERPERMISSIONS_COLUMN_USERID))),Integer.parseInt(cursor1.getString(cursor1.getColumnIndex(USERPERMISSIONS_COLUMN_PERMISSIONSID))));
            salesManId.add( userPermissions.getEmployeeId());
            cursor1.moveToNext();
        }
        return salesManId;
    }

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}
	public boolean deletePermissions(long userId ,int permissions)
	{
		return db.delete(USERPERMISSIONS_TABLE_NAME, USERPERMISSIONS_COLUMN_PERMISSIONSID + "=" + permissions+ " and " + USERPERMISSIONS_COLUMN_USERID + "="+userId, null) > 0;
	}

}
