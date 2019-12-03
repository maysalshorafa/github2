package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Permission.Permissions;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 29/10/2016.
 */

public class PermissionsDBAdapter {
	// Table Name
	public static final String PERMISSIONS_TABLE_NAME = "permissions";
	// Column Names
	protected static final String PERMISSIONS_COLUMN_ID = "id";
	protected static final String PERMISSIONS_COLUMN_NAME = "name";

	public static final String DATABASE_CREATE = "CREATE TABLE `permissions` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT NOT NULL)";
	public static final String FIX_DEPARTMENT_CATEGORY_V2 = "update " + PERMISSIONS_TABLE_NAME + " set " + PERMISSIONS_COLUMN_NAME + "='" + Permissions.PERMISSIONS_CATEGORY + "' where " + PERMISSIONS_COLUMN_NAME + "='department' and " + PERMISSIONS_COLUMN_ID + "=4;";
	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;

	public PermissionsDBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
	}

	public PermissionsDBAdapter open() throws SQLException {
		this.db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
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
		ContentValues val = new ContentValues();
		val.put(PERMISSIONS_COLUMN_ID, Util.idHealth(this.db, PERMISSIONS_TABLE_NAME, PERMISSIONS_COLUMN_ID));

		//Assign values for each row.
		val.put(PERMISSIONS_TABLE_NAME, name);
		try {
			db.insert(PERMISSIONS_TABLE_NAME, null, val);
			close();
			return 1;
		} catch (SQLException ex) {
			Log.e("Permissions DB insert", "inserting Entry at " + PERMISSIONS_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public Permissions getPermission(long id){
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
		Cursor cursor = db.rawQuery("select * from " + PERMISSIONS_TABLE_NAME + " where id=" + id, null);
		if (cursor.getCount() < 1) // UserName Not Exist
		{
			cursor.close();
			close();
			return null;
		}
		cursor.moveToFirst();
		close();
		return new Permissions(cursor.getLong(cursor.getColumnIndex(PERMISSIONS_COLUMN_ID)),
				cursor.getString(cursor.getColumnIndex(PERMISSIONS_COLUMN_NAME)));
	}

	public List<Permissions> getAllPermissions() {
		if(db.isOpen()){

		}else {
			try {
				open();
			}
			catch (SQLException ex) {
				Log.d("Exception",ex.toString());
			}
		}
		List<Permissions> permissionsList = new ArrayList<Permissions>();

		Cursor cursor = db.rawQuery("select * from " + PERMISSIONS_TABLE_NAME, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			permissionsList.add(new Permissions(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PERMISSIONS_COLUMN_ID))),
					cursor.getString(cursor.getColumnIndex(PERMISSIONS_COLUMN_NAME))));
			cursor.moveToNext();
		} close();

		return permissionsList;
	}

}
