package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 19/10/2016.
 */
public class PaymentDBAdapter {
	// Table Name
	protected static final String PAYMENT_TABLE_NAME = "payment";
	// Column Names
	protected static final String PAYMENT_COLUMN_ID = "id";
	protected static final String PAYMENT_COLUMN_PAYMENTWAY = "paymentWay";
	protected static final String PAYMENT_COLUMN_ORDERID = "orderId";
	protected static final String PAYMENT_COLUMN_AMOUNT = "amount";
	protected static final String PAYMENT_COLUMN_KEY = "key";

	public static final String DATABASE_CREATE = "CREATE TABLE `payment` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `paymentWay` TEXT NOT NULL, `amount` REAL NOT NULL,`orderId` INTEGER ,`key` TEXT, FOREIGN KEY(`orderId`) REFERENCES `_Order.id`)";
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

	public long insertEntry(String paymentWay,double amount, long saleId, String orderDetailsKey) {
		Payment payment = new Payment(Util.idHealth(this.db, PAYMENT_TABLE_NAME, PAYMENT_COLUMN_ID), paymentWay, amount, saleId,orderDetailsKey);
		sendToBroker(MessageType.ADD_PAYMENT, payment, this.context);

		try {
			return insertEntry(payment);
		} catch (SQLException ex) {
			Log.e("Payment DB insert", "inserting Entry at " + PAYMENT_TABLE_NAME + ": " + ex.getMessage());
			return -1;
		}
	}
	public long receiptInsertEntry(String paymentWay,double amount, long saleId) {
		Payment payment = new Payment(Util.idHealth(this.db, PAYMENT_TABLE_NAME, PAYMENT_COLUMN_ID), paymentWay, amount, saleId);

		try {
			return insertEntry(payment);
		} catch (SQLException ex) {
			Log.e("Payment DB insert", "inserting Entry at " + PAYMENT_TABLE_NAME + ": " + ex.getMessage());
			return -1;
		}
	}


	public long insertEntry(Payment payment){
		ContentValues val = new ContentValues();
		//Assign values for each row.

		val.put(PAYMENT_COLUMN_ID, payment.getPaymentId());


		val.put(PAYMENT_COLUMN_PAYMENTWAY, payment.getPaymentWay());
		val.put(PAYMENT_COLUMN_AMOUNT,payment.getAmount() );
		val.put(PAYMENT_COLUMN_ORDERID, payment.getOrderId());
		val.put(PAYMENT_COLUMN_KEY,payment.getOrderKey());

		try {
			return db.insert(PAYMENT_TABLE_NAME, null, val);
		} catch (SQLException ex) {
			Log.e("Payment DB insert", "inserting Entry at " + PAYMENT_TABLE_NAME + ": " + ex.getMessage());
			return -1;
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

	public List<Payment> getPaymentBySaleID(long saleID) {
		List<Payment> salePaymentList = new ArrayList<Payment>();

		Cursor cursor = db.rawQuery("select * from " + PAYMENT_TABLE_NAME +" where "+PAYMENT_COLUMN_ORDERID+"="+saleID, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			salePaymentList.add(make(cursor));
			cursor.moveToNext();
		}

		return salePaymentList;
	}
	public Payment getPaymentByID(long saleID) {
	Payment payment = null;

		Cursor cursor = db.rawQuery("select * from " + PAYMENT_TABLE_NAME +" where "+PAYMENT_COLUMN_ORDERID+"="+saleID, null);
		cursor.moveToFirst();
		payment = make(cursor);
		return payment;
	}

    private Payment make(Cursor cursor){
        return new Payment(Long.parseLong(cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_PAYMENTWAY)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_AMOUNT))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_ORDERID))),cursor.getString(cursor.getColumnIndex(PAYMENT_COLUMN_KEY)));
    }
	public static String addColumn(String columnName) {
		String dbc = "ALTER TABLE " + PAYMENT_TABLE_NAME
				+ " add column " + columnName + " TEXT  DEFAULT '' ;";
		return dbc;
	}

}
