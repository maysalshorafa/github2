package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

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

	public long insertEntry(Offer offer){
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(OFFER_COLUMN_ID, offer.getId());

        val.put(OFFER_COLUMN_NAME, offer.getName());
        val.put(OFFER_COLUMN_STARTDATE, offer.getStartDate());
        val.put(OFFER_COLUMN_ENDDATE, offer.getEndDate());
        val.put(OFFER_COLUMN_CREATINGDATE, offer.getCreatingDate());
        val.put(OFFER_COLUMN_STATUS, offer.getStatus());
        val.put(OFFER_COLUMN_BYUSER, offer.getByUser());
        val.put(OFFER_COLUMN_RULENAME, offer.getRuleName());
        val.put(OFFER_COLUMN_RULEID, offer.getRuleID());

        try {
            return db.insert(OFFER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "inserting Entry in " + OFFER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
	}

	public long insertEntry(String name,long startDate,long endDate,long createDate,int status,long byUser,String ruleName,long ruleID){

        Offer offer = new Offer(Util.idHealth(this.db, OFFER_TABLE_NAME, OFFER_COLUMN_ID), name, startDate, endDate, createDate, status, byUser, ruleName, ruleID);
        sendToBroker(MessageType.ADD_OFFER, offer, this.context);

        try {
            return insertEntry(offer);
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "inserting Entry in " + OFFER_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
	}

	public Offer getOfferById(long id){
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
//+ OFFER_COLUMN_ENDDATE+"< '"+new Date().getTime()+"' order by id desc"
	public List<Offer> getAllOffersByStatus(int Status) {
		List<Offer> offerList = new ArrayList<Offer>();
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_STATUS + "=" + Status + " order by id desc", null);
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
			offerList.add(createOfferObject(cursor));
			cursor.moveToNext();
		}

		return offerList;
	}

    public List<Integer> getAllOffersIDsByStatus(int Status) {
        List<Integer> offerIDsList = new ArrayList<Integer>();
        Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_STATUS + "=" + Status+" order by id desc", null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            offerIDsList.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ID))));
            cursor.moveToNext();
        }

        return offerIDsList;
    }

	public Offer createOfferObject(Cursor cursor) {
		return new Offer(Long.parseLong(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ID))),
				cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_NAME)),
				cursor.getLong(cursor.getColumnIndex(OFFER_COLUMN_STARTDATE)),
				cursor.getLong(cursor.getColumnIndex(OFFER_COLUMN_ENDDATE)),
				cursor.getLong(cursor.getColumnIndex(OFFER_COLUMN_CREATINGDATE)),
				cursor.getInt(cursor.getColumnIndex(OFFER_COLUMN_STATUS)),
				cursor.getInt(cursor.getColumnIndex(OFFER_COLUMN_BYUSER)),
				cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_RULENAME)),
				cursor.getLong(cursor.getColumnIndex(OFFER_COLUMN_RULEID)));
	}
	// "=1 and "+OFFER_COLUMN_ENDDATE+"< '"+new Date().getTime()+"' order by id desc"

	public Offer getAllValidOffers() {
		Offer offer =null;
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_STATUS +"="+1, null);
		cursor.moveToFirst();
		if(cursor.getCount()<0){
			return  offer;
		}
		else	if( cursor != null && cursor.moveToFirst() ){
			offer= new Offer(Long.parseLong(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ID))),
					cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_NAME)),
					cursor.getLong(cursor.getColumnIndex(OFFER_COLUMN_STARTDATE)),
					cursor.getLong(cursor.getColumnIndex(OFFER_COLUMN_ENDDATE)),
					cursor.getLong(cursor.getColumnIndex(OFFER_COLUMN_CREATINGDATE)),
					Integer.parseInt(	cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_STATUS))),
					Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_BYUSER))),cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_RULENAME)),
					Long.parseLong(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_RULEID))));
			cursor.close();
		}

		return offer;
	}
}
