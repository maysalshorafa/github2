package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 23/10/2016.
 * Updated by KARAM on 01/08/2017.
 */
public class OfferDBAdapter {

	private static final String LOG_TAG = "Offer Table";

	// Table Name
	protected static final String OFFER_TABLE_NAME = "offers";

	// Column Names
	protected static final String OFFER_COLUMN_ID = "id";
	protected static final String OFFER_COLUMN_NAME = "name";
	protected static final String OFFER_COLUMN_STARTDATE = "startDate";
	protected static final String OFFER_COLUMN_ENDDATE = "endDate";
	protected static final String OFFER_COLUMN_CREATINGDATE = "creatingDate";
	protected static final String OFFER_COLUMN_STATUS = "status";
	protected static final String OFFER_COLUMN_BYUSER = "byUser";
	protected static final String OFFER_COLUMN_RULENAME = "ruleName";
	protected static final String OFFER_COLUMN_RULEID = "ruleID";

	public static final String DATABASE_CREATE = "CREATE TABLE "+OFFER_TABLE_NAME+" ( `" + OFFER_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"`" + OFFER_COLUMN_NAME + "` TEXT NOT NULL, `" + OFFER_COLUMN_STARTDATE + "` TEXT DEFAULT current_timestamp, " +
			"`" + OFFER_COLUMN_ENDDATE + "` TEXT DEFAULT current_timestamp, `" + OFFER_COLUMN_CREATINGDATE + "` TEXT DEFAULT current_timestamp, " +
			"`" + OFFER_COLUMN_STATUS + "` INTEGER DEFAULT 1, `" + OFFER_COLUMN_BYUSER + "` INTEGER," +
			"`" + OFFER_COLUMN_RULENAME + "` TEXT NOT NULL, `" + OFFER_COLUMN_RULEID + "` INTEGER NOT NULL)";

	// Variable to hold the database instance
	private SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DbHelper dbHelper;

	public OfferDBAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
	}

	public OfferDBAdapter open() throws SQLException {
		this.db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}

	public int insertEntry(Offer offer){
		return insertEntry(offer.getName(), offer.getStartDate().getTime(), offer.getEndDate().getTime(), offer.getCreatingDate().getTime(), offer.getStatus(), offer.getByUser(), offer.getRuleName(), offer.getRuleID());
	}

	public int insertEntry(String name,long startDate,long endDate,long createDate,int status,int byUser,String ruleName,int ruleID){
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(OFFER_COLUMN_NAME, name);
		val.put(OFFER_COLUMN_STARTDATE, startDate);
		val.put(OFFER_COLUMN_ENDDATE, endDate);
		val.put(OFFER_COLUMN_CREATINGDATE, createDate);
		val.put(OFFER_COLUMN_STATUS, status);
		val.put(OFFER_COLUMN_BYUSER, byUser);
		val.put(OFFER_COLUMN_RULENAME, ruleName);
		val.put(OFFER_COLUMN_RULEID, ruleID);
		try {
			return (int)db.insert(OFFER_TABLE_NAME, null, val);
		} catch (SQLException ex) {
			Log.e(LOG_TAG, "inserting Entry in " + OFFER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public Offer getOfferById(int id){
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where id='" + id + "'", null);
		if (cursor.getCount() < 1) // Offer Not Exist
		{
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		return createOfferObject(cursor);
	}

	public List<Offer> getAllOffers() {
		List<Offer> offerList = new ArrayList<Offer>();
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " order by id desc", null);
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
			offerList.add(createOfferObject(cursor));
			cursor.moveToNext();
		}
		return offerList;
	}

	public List<Offer> getAllOffersByStatus(int Status) {
		List<Offer> offerList = new ArrayList<Offer>();
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_STATUS + "=" + Status, null);
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
			offerList.add(createOfferObject(cursor));
			cursor.moveToNext();
		}

		return offerList;
	}

	public Offer createOfferObject(Cursor cursor) {
		return new Offer(Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ID))),
				cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_NAME)),
				DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_STARTDATE))),
				DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ENDDATE))),
				DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_CREATINGDATE))),
				cursor.getInt(cursor.getColumnIndex(OFFER_COLUMN_STATUS)),
				cursor.getInt(cursor.getColumnIndex(OFFER_COLUMN_BYUSER)),
				cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_RULENAME)),
				cursor.getInt(cursor.getColumnIndex(OFFER_COLUMN_RULEID)));
	}
}
