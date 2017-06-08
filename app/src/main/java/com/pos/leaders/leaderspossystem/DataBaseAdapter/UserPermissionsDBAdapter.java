package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.pos.leaders.leaderspossystem.DbHelper;

/**
 * Created by KARAM on 29/10/2016.
 */

public class UserPermissionsDBAdapter {
	// Table Name
	protected static final String USERPERMISSIONS_TABLE_NAME = "userPermissions";
	// Column Names
	protected static final String USERPERMISSIONS_COLUMN_USERID = "userId";
	protected static final String USERPERMISSIONS_COLUMN_PERMISSIONSID = "permissionId";

	public static final String DATABASE_CREATE = "CREATE TABLE `userPermissions` ( `userId` INTEGER , `permissionId` INTEGER,"+
			"FOREIGN KEY(`userId`) REFERENCES `users.id`,FOREIGN KEY(`permissionId`) REFERENCES `permissions.id`)";
	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;

	public UserPermissionsDBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
	}

	public UserPermissionsDBAdapter open() throws SQLException {
		this.db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}
}
