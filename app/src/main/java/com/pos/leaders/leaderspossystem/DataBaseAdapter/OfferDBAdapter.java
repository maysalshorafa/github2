package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Offers.ResourceType;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 23/10/2016.
 * Updated by KARAM on 01/08/2017.
 */
public class OfferDBAdapter {

	private static final String LOG_TAG = "Offer Table";

	// Table Name
	protected static final String OFFER_TABLE_NAME = "offer";

	// Column Names
	protected static final String OFFER_COLUMN_ID = "offerId";
	protected static final String OFFER_COLUMN_NAME = "name";
	protected static final String OFFER_COLUMN_ACTIVE = "active";
	protected static final String OFFER_COLUMN_RESOURCE_ID = "resourceId";
	protected static final String OFFER_COLUMN_RESOURCE_TYPE = "resourceType";
	protected static final String OFFER_COLUMN_START_DATE = "start";
	protected static final String OFFER_COLUMN_END_DATE = "end";
	protected static final String OFFER_COLUMN_DATA = "data";
	protected static final String OFFER_COLUMN_BY_EMPLOYEE = "byEmployee";
	protected static final String OFFER_COLUMN_CREATED_AT = "createdAt";
	protected static final String OFFER_COLUMN_UPDATED_AT = "updatedAt";

	public static final String DATABASE_CREATE = "CREATE TABLE "+OFFER_TABLE_NAME+" ( `" + OFFER_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"`" + OFFER_COLUMN_NAME + "` TEXT NOT NULL,`"+OFFER_COLUMN_RESOURCE_ID+"` INTEGER NOT NULL, `"+OFFER_COLUMN_RESOURCE_TYPE+"` TEXT NOT NULL,"+
			"`" + OFFER_COLUMN_START_DATE + "` TIMESTAMP NOT NULL DEFAULT current_timestamp, " +
			"`" + OFFER_COLUMN_END_DATE + "` TIMESTAMP NOT NULL DEFAULT current_timestamp, "+
			"`" + OFFER_COLUMN_CREATED_AT + "` TIMESTAMP NOT NULL DEFAULT current_timestamp , " +
			"`" + OFFER_COLUMN_UPDATED_AT + "` TIMESTAMP NOT NULL DEFAULT current_timestamp , " +
			"`" + OFFER_COLUMN_ACTIVE + "` INTEGER DEFAULT 1, `" + OFFER_COLUMN_BY_EMPLOYEE + "` INTEGER," +
			"`" + OFFER_COLUMN_DATA + "` TEXT NOT NULL)";

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
        val.put(OFFER_COLUMN_ID, offer.getOfferId());
        val.put(OFFER_COLUMN_NAME, offer.getName());
		val.put(OFFER_COLUMN_ACTIVE, offer.isActive());
		val.put(OFFER_COLUMN_RESOURCE_ID, offer.getResourceId());
		val.put(OFFER_COLUMN_RESOURCE_TYPE, offer.getResourceType().getValue());
		val.put(OFFER_COLUMN_START_DATE, offer.getStart().toString());
		val.put(OFFER_COLUMN_END_DATE, offer.getEnd().toString());
		val.put(OFFER_COLUMN_DATA, offer.getData());
		val.put(OFFER_COLUMN_BY_EMPLOYEE, offer.getByEmployee());
		val.put(OFFER_COLUMN_CREATED_AT, offer.getCreatedAt().toString());
		val.put(OFFER_COLUMN_UPDATED_AT, offer.getUpdatedAt().toString());

        try {
            return db.insert(OFFER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "inserting Entry in " + OFFER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
	}

	public long insertEntry(String name, boolean active, long resourceId, ResourceType resourceType, Timestamp start, Timestamp end, String data, long byEmployee){

		Offer offer = new Offer(Util.idHealth(this.db, OFFER_TABLE_NAME, OFFER_COLUMN_ID), name, active, resourceId, resourceType, start, end, data, byEmployee, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()));
        sendToBroker(MessageType.ADD_OFFER, offer, this.context);

        try {
            return insertEntry(offer);
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "inserting Entry in " + OFFER_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
	}

	public Offer getOfferById(long id){
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where "+OFFER_COLUMN_ID+"='" + id + "'", null);
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
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " order by " + OFFER_COLUMN_ID + " desc", null);
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
			offerList.add(createOfferObject(cursor));
			cursor.moveToNext();
		}
		return offerList;
	}
//+ OFFER_COLUMN_ENDDATE+"< '"+new Date().getTime()+"' order by id desc"
	public List<Offer> getAllOffersByStatus(boolean Status) {
		List<Offer> offerList = new ArrayList<Offer>();
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_ACTIVE + "=" + (Status ? 1 : 0) + " order by " + OFFER_COLUMN_ID + " desc", null);
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
			offerList.add(createOfferObject(cursor));
			cursor.moveToNext();
		}

		return offerList;
	}

    public List<Integer> getAllOffersIDsByStatus(boolean Status) {
        List<Integer> offerIDsList = new ArrayList<Integer>();
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_ACTIVE + "=" + Status + " order by " + OFFER_COLUMN_ID + " desc", null);
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
				Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ACTIVE))),
				cursor.getLong(cursor.getColumnIndex(OFFER_COLUMN_RESOURCE_ID)),
				ResourceType.valueOf(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_RESOURCE_TYPE))),
				Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_START_DATE))),
				Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_END_DATE))),
				cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_DATA)),
				cursor.getLong(cursor.getColumnIndex(OFFER_COLUMN_BY_EMPLOYEE)),
				Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_CREATED_AT))),
				Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_UPDATED_AT))));
	}
	// "=1 and "+OFFER_COLUMN_ENDDATE+"< '"+new Date().getTime()+"' order by id desc"

	public Offer getAllValidOffers() {
		Offer offer =null;
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_ACTIVE +"="+ 1, null);
		cursor.moveToFirst();
		if(cursor.getCount()<0){
			return offer;
		}
		else {
			if (cursor != null && cursor.moveToFirst()) {
				offer = createOfferObject(cursor);
				cursor.close();
			}
		}
		return offer;
	}
}
