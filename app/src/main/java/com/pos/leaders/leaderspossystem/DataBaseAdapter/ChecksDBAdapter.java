package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 02/11/2016.
 */

public class ChecksDBAdapter {
	// Table Name
	protected static final String CHECKS_TABLE_NAME = "checks";
	// Column Names
	protected static final String CHECKS_COLUMN_ID = "id";
	protected static final String CHECKS_COLUMN_CHECKNUMBER = "checkNum";
	protected static final String CHECKS_COLUMN_BANKNUMBER = "bankNum";
	protected static final String CHECKS_COLUMN_BRANCHNUMBER = "branchNum";
	protected static final String CHECKS_COLUMN_ACCOUNTNUMBER = "accountNum";
	protected static final String CHECKS_COLUMN_AMOUNT = "amount";
	protected static final String CHECKS_COLUMN_DATE = "_date";
	protected static final String CHECKS_COLUMN_ISDELETED = "isDeleted";
	protected static final String CHECKS_COLUMN_SALEID = "saleId";

	public static final String DATABASE_CREATE = "CREATE TABLE `checks` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
			" `checkNum` INTEGER, `bankNum` INTEGER, `branchNum` INTEGER ," +
			" `accountNum` INTEGER, `amount` REAL, `_date` TEXT DEFAULT current_timestamp, `isDeleted` INTEGER DEFAULT 0 ," +
			" `saleId` INTEGER, FOREIGN KEY(`saleId`) REFERENCES `sales.id`)";
	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;


	public ChecksDBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
	}

	public ChecksDBAdapter open() throws SQLException {
		this.db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}



	public int insertEntry(int checkNum,int bankNum,int branchNum,int accountNum,double amount, long saleId) {
		ContentValues val = new ContentValues();

		val.put(CHECKS_COLUMN_ID, Util.idHealth(this.db,CHECKS_TABLE_NAME, CHECKS_COLUMN_ID) );


		//Assign values for each row.
		val.put(CHECKS_COLUMN_CHECKNUMBER, checkNum);
		val.put(CHECKS_COLUMN_BANKNUMBER, bankNum);
		val.put(CHECKS_COLUMN_BRANCHNUMBER, branchNum);
		val.put(CHECKS_COLUMN_ACCOUNTNUMBER, accountNum);
		val.put(CHECKS_COLUMN_AMOUNT,amount );
		val.put(CHECKS_COLUMN_SALEID, saleId);
		try {
			long i=db.insert(CHECKS_TABLE_NAME, null, val);
			return (int)i;

		} catch (SQLException ex) {
			Log.e("Checks DB insert", "inserting Entry at " + CHECKS_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public List<Check> getAllChecks() {
		List<Check> checksList = new ArrayList<Check>();

		Cursor cursor = db.rawQuery("select * from " + CHECKS_TABLE_NAME, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			checksList.add(newCheck(cursor));
			cursor.moveToNext();
		}

		return checksList;
	}

	public List<Check> getPaymentBySaleID(long saleID) {
		List<Check> checksList = new ArrayList<Check>();

		Cursor cursor = db.rawQuery("select * from " + CHECKS_TABLE_NAME + " where " + CHECKS_COLUMN_SALEID + "=" + saleID, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			checksList.add(newCheck(cursor));
			cursor.moveToNext();
		}
		return checksList;
	}

	private Check newCheck(Cursor cursor){
		////int id, int checkNum, int bankNum, int branchNum, int accountNum, double amount, Date date, boolean isDeleted, int saleId
		return new Check(Long.parseLong(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_ID))),
				Integer.parseInt(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_CHECKNUMBER))),
				Integer.parseInt(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_BANKNUMBER))),
				Integer.parseInt(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_BRANCHNUMBER))),
				Integer.parseInt(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_ACCOUNTNUMBER))),
				Double.parseDouble(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_AMOUNT))),
				DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_DATE))),
				Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_ISDELETED))),
				Long.parseLong(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_SALEID))));
	}

	public int deleteEntry(long id) {
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put(CHECKS_COLUMN_ISDELETED, 1);

		String where = CHECKS_COLUMN_ID + " = ?";
		try {
			db.update(CHECKS_TABLE_NAME, updatedValues, where, new String[]{id + ""});
			return 1;
		} catch (SQLException ex) {
			Log.e("Checks deleteEntry", "enable hide Entry at " + CHECKS_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}
}
