package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Payment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 19/10/2016.
 */
public class PaymentDBAdapter {
	// Table Name
	protected static final String PAYMENT_TABLE_NAME = "payment";
	// Column Names
	protected static final String PAYMENT_COLUMN_ID = "id";
	protected static final String PAYMENT_COLUMN_PAYMENTWAY = "paymentWay";
	protected static final String PAYMENT_COLUMN_SALEID = "saleId";
	protected static final String PAYMENT_COLUMN_AMOUNT = "amount";
	private static final String PAYMENT_CHECKS_ID = "checkId";

	public static final String DATABASE_CREATE = "CREATE TABLE `payment` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `paymentWay` TEXT NOT NULL, `amount` REAL NOT NULL, `checkId` INTEGER , `saleId` INTEGER, FOREIGN KEY(`saleId`) REFERENCES `sales.id` ,FOREIGN KEY(`checkId`) REFERENCES `checks.id`)";
	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;

	public PaymentDBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
	}

	public PaymentDBAdapter open() throws SQLException {
		this.db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}

	public int insertEntry(String paymentWay,double amount, int saleId) {
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(PAYMENT_COLUMN_PAYMENTWAY, paymentWay);
		val.put(PAYMENT_COLUMN_AMOUNT,amount );
		val.put(PAYMENT_COLUMN_SALEID, saleId);
		try {
			db.insert(PAYMENT_TABLE_NAME, null, val);
			return 1;
		} catch (SQLException ex) {
			Log.e("Payment DB insert", "inserting Entry at " + PAYMENT_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public List<Payment> getAllPayments() {
		List<Payment> paymentsList = new ArrayList<Payment>();

		Cursor cursor = db.rawQuery("select * from " + PAYMENT_TABLE_NAME, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			paymentsList.add(make(cursor));
			cursor.moveToNext();
		}

		return paymentsList;
	}

	public List<Payment> getPaymentBySaleID(int saleID) {
		List<Payment> salePaymentList = new ArrayList<Payment>();

		Cursor cursor = db.rawQuery("select * from " + PAYMENT_TABLE_NAME +" where "+PAYMENT_COLUMN_SALEID+"="+saleID, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			salePaymentList.add(make(cursor));
			cursor.moveToNext();
		}

		return salePaymentList;
	}

    private Payment make(Cursor cursor){
        return new Payment(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_PAYMENTWAY)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_AMOUNT))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_SALEID))));
    }

}
