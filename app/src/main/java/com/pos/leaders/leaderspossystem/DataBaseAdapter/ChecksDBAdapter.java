package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;
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
	protected static final String CHECKS_COLUMN_ORDERID = "orderId";

	public static final String DATABASE_CREATE = "CREATE TABLE `checks` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
			" `checkNum` INTEGER, `bankNum` INTEGER, `branchNum` INTEGER ," +
			" `accountNum` INTEGER, `amount` REAL, `_date` TIMESTAMP DEFAULT current_timestamp, `isDeleted` INTEGER DEFAULT 0 ," +
			" `orderId` INTEGER, FOREIGN KEY(`orderId`) REFERENCES `_Order.id`)";
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



	public long insertEntry(long checkNum, long bankNum, long branchNum, long accountNum, double amount, Timestamp date, long saleId) {
		if(db.isOpen()){

		}else {
			open();
		}
        Check check = new Check(Util.idHealth(this.db, CHECKS_TABLE_NAME, CHECKS_COLUMN_ID), checkNum, bankNum, branchNum, accountNum, amount, date, false, saleId);
        //sendToBroker(MessageType.ADD_CHECK, check, this.context);

        try {

            return insertEntry(check);
        } catch (SQLException ex) {
            Log.e("Checks DB insert", "inserting Entry at " + CHECKS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
	}

	public long insertEntry(Check check){
		if(db.isOpen()){

		}else {
			open();
		}
		ContentValues val = new ContentValues();
		val.put(CHECKS_COLUMN_ID,check.getCheckId());
		//Assign values for each row.
		val.put(CHECKS_COLUMN_CHECKNUMBER, check.getCheckNum());
		val.put(CHECKS_COLUMN_BANKNUMBER, check.getBankNum());
		val.put(CHECKS_COLUMN_BRANCHNUMBER, check.getBranchNum());
		val.put(CHECKS_COLUMN_ACCOUNTNUMBER, check.getAccountNum());
		val.put(CHECKS_COLUMN_AMOUNT,check.getAmount() );
		val.put(CHECKS_COLUMN_DATE,String.valueOf(check.getCreatedAt()));
        val.put(CHECKS_COLUMN_ISDELETED, check.isDeleted() ? 1 : 0);
        val.put(CHECKS_COLUMN_ORDERID, check.getOrderId());
		try {
			return db.insert(CHECKS_TABLE_NAME, null, val);

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
		try {
			if(db.isOpen()){

			}else {
				open();
			}
		Cursor cursor = db.rawQuery("select * from " + CHECKS_TABLE_NAME + " where " + CHECKS_COLUMN_ORDERID + "=" + saleID, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			checksList.add(newCheck(cursor));
			cursor.moveToNext();
		}
close();

		} catch (Exception e) {
			Log.d("exception",e.toString());

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
				Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_DATE))),
				Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_ISDELETED))),
				Long.parseLong(cursor.getString(cursor.getColumnIndex(CHECKS_COLUMN_ORDERID))));
	}

	public int deleteEntry(long id) {
		if(db.isOpen()){

		}else {
			open();
		}
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put(CHECKS_COLUMN_ISDELETED, 1);

		String where = CHECKS_COLUMN_ID + " = ?";
		try {
			db.update(CHECKS_TABLE_NAME, updatedValues, where, new String[]{id + ""});
			close();
			return 1;
		} catch (SQLException ex) {
			Log.e("Checks deleteEntry", "enable hide Entry at " + CHECKS_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}
}
