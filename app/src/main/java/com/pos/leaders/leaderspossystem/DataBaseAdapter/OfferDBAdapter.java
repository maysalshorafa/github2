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

import org.json.JSONException;

import java.io.IOException;
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
	protected static final String OFFER_COLUMN_HIDE = "hide";

	public static final String DATABASE_CREATE = "CREATE TABLE "+OFFER_TABLE_NAME+" ( `" + OFFER_COLUMN_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"`" + OFFER_COLUMN_NAME + "` TEXT NOT NULL,`"+OFFER_COLUMN_RESOURCE_ID+"` INTEGER NOT NULL, `"+OFFER_COLUMN_RESOURCE_TYPE+"` TEXT NOT NULL,"+
			"`" + OFFER_COLUMN_START_DATE + "` TIMESTAMP NOT NULL DEFAULT current_timestamp, " +
			"`" + OFFER_COLUMN_END_DATE + "` TIMESTAMP NOT NULL DEFAULT current_timestamp, "+
			"`" + OFFER_COLUMN_CREATED_AT + "` TIMESTAMP NOT NULL DEFAULT current_timestamp , " +
			"`" + OFFER_COLUMN_UPDATED_AT + "` TIMESTAMP NOT NULL DEFAULT current_timestamp , " +
			"`" + OFFER_COLUMN_ACTIVE + "` TEXT  , `" + OFFER_COLUMN_BY_EMPLOYEE + "` INTEGER," + "`" +OFFER_COLUMN_HIDE + "` INTEGER DEFAULT 0, " +
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
		val.put(OFFER_COLUMN_ACTIVE, offer.getStatus());
		val.put(OFFER_COLUMN_RESOURCE_ID, offer.getResourceId());
		val.put(OFFER_COLUMN_RESOURCE_TYPE, offer.getResourceType().getValue());
		val.put(OFFER_COLUMN_START_DATE, String.valueOf(offer.getStartDate()));
		val.put(OFFER_COLUMN_END_DATE, String.valueOf(offer.getEndDate()));
		val.put(OFFER_COLUMN_DATA, offer.getOfferData());
		val.put(OFFER_COLUMN_BY_EMPLOYEE, offer.getByEmployee());
		if(offer.getCreatedAt()!=null) {
			val.put(OFFER_COLUMN_CREATED_AT, String.valueOf(offer.getCreatedAt()));
		}
		if(offer.getUpdatedAt()!=null) {
			val.put(OFFER_COLUMN_UPDATED_AT, String.valueOf(offer.getUpdatedAt()));
		}

        try {
            return db.insert(OFFER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "inserting Entry in " + OFFER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
	}

	public long insertEntry(String name, String active, long resourceId, ResourceType resourceType, Timestamp start, Timestamp end, String data, long byEmployee) throws JSONException, IOException {
		Offer offer = new Offer(Util.idHealth(this.db, OFFER_TABLE_NAME, OFFER_COLUMN_ID), name, active, resourceId, resourceType, start, end, data.toString(), byEmployee, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()));

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

	public List<Offer> getAllActiveOffersByResourceIdResourceType(long id,ResourceType resourceType ) {
		List<Offer> offerList = null;
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME +" where "+ OFFER_COLUMN_RESOURCE_TYPE+ " = '"+resourceType.getValue() +"' and "+OFFER_COLUMN_RESOURCE_ID+" = " + id +" and "+OFFER_COLUMN_ACTIVE+"=ACTIVE and "+OFFER_COLUMN_HIDE+"=0  order by " + OFFER_COLUMN_ID + " desc", null);
		cursor.moveToFirst();
		if(cursor.getCount()>0) {
			offerList = new ArrayList<Offer>();
			while (!cursor.isAfterLast()) {
				offerList.add(createOfferObject(cursor));
				cursor.moveToNext();
			}
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
				cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ACTIVE)),
				cursor.getLong(cursor.getColumnIndex(OFFER_COLUMN_RESOURCE_ID)),
				ResourceType.valueFor(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_RESOURCE_TYPE))),
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
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_ACTIVE +"="+ "ACTIVE", null);
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
	public List<Offer> getBetweenTwoDates(long from, long to){
		List<Offer> offerList = new ArrayList<Offer>();
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_START_DATE + " > datetime("+from+"/1000, 'unixepoch') and "+  OFFER_COLUMN_END_DATE +" < datetime("+to+"/1000, 'unixepoch')"+ " and "+OFFER_COLUMN_ACTIVE + " = " + "'ACTIVE' "+ " and "+OFFER_COLUMN_HIDE + " = " + 0 , null);
		cursor.moveToFirst();
		Log.d("offerList","select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_START_DATE + " > datetime("+from+"/1000, 'unixepoch') and "+  OFFER_COLUMN_END_DATE +" < datetime("+to+"/1000, 'unixepoch')"+ " and "+OFFER_COLUMN_ACTIVE + " = " + "'ACTIVE' "+ " and "+OFFER_COLUMN_HIDE + " = " + 0 );
		while (!cursor.isAfterLast()) {
				offerList.add(createOfferObject(cursor));
			cursor.moveToNext();
		}
		Log.d("offerList",offerList.toString());
		return offerList;
	}

	public int deleteEntry(long id) {
		OfferDBAdapter offerDBAdapter=new OfferDBAdapter(context);
		offerDBAdapter.open();
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put(OFFER_COLUMN_HIDE, 1);

		String where = OFFER_COLUMN_ID + " = ?";
		try {
			db.update(OFFER_TABLE_NAME, updatedValues, where, new String[]{id + ""});
			Offer offer=offerDBAdapter.getOfferById(id);
			sendToBroker(MessageType.DELETE_OFFER, offer, this.context);
			return 1;
		} catch (SQLException ex) {
			Log.e("Offer deleteEntry", "enable hide Entry at " + OFFER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}
	public long deleteEntryBo(Offer offer) {
		// Define the updated row content.
		ContentValues updatedValues = new ContentValues();
		// Assign values for each row.
		updatedValues.put(OFFER_COLUMN_HIDE, 1);

		String where = OFFER_COLUMN_ID + " = ?";
		try {
			db.update(OFFER_TABLE_NAME, updatedValues, where, new String[]{offer.getOfferId() + ""});
			return 1;
		} catch (SQLException ex) {
			Log.e("Offer deleteEntry", "enable hide Entry at " + OFFER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public void updateEntry(Offer offer) {
		OfferDBAdapter offerDBAdapter = new OfferDBAdapter(context);
		offerDBAdapter.open();
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(OFFER_COLUMN_ID, offer.getOfferId());
		val.put(OFFER_COLUMN_NAME, offer.getName());
		val.put(OFFER_COLUMN_ACTIVE, offer.getStatus());
		val.put(OFFER_COLUMN_RESOURCE_ID, offer.getResourceId());
		val.put(OFFER_COLUMN_RESOURCE_TYPE, offer.getResourceType().getValue());
		val.put(OFFER_COLUMN_START_DATE, String.valueOf(offer.getStartDate()));
		val.put(OFFER_COLUMN_END_DATE, String.valueOf(offer.getEndDate()));
		val.put(OFFER_COLUMN_DATA, offer.getOfferData());
		val.put(OFFER_COLUMN_BY_EMPLOYEE, offer.getByEmployee());
		if(offer.getCreatedAt()!=null) {
			val.put(OFFER_COLUMN_CREATED_AT, String.valueOf(offer.getCreatedAt()));
		}
		if(offer.getUpdatedAt()!=null) {
			val.put(OFFER_COLUMN_UPDATED_AT, String.valueOf(offer.getUpdatedAt()));
		}


		String where = OFFER_COLUMN_ID + " = ?";
		db.update(OFFER_TABLE_NAME, val, where, new String[]{offer.getOfferId() + ""});
		Offer p=offerDBAdapter.getOfferById(offer.getOfferId());
		Log.d("Update Object",p.toString());
		sendToBroker(MessageType.UPDATE_OFFER, p, this.context);
		offerDBAdapter.close();
	}

	public long updateEntryBo(Offer offer) {
		OfferDBAdapter offerDBAdapter = new OfferDBAdapter(context);
		offerDBAdapter.open();
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(OFFER_COLUMN_ID, offer.getOfferId());
		val.put(OFFER_COLUMN_NAME, offer.getName());
		val.put(OFFER_COLUMN_ACTIVE, offer.getStatus());
		val.put(OFFER_COLUMN_RESOURCE_ID, offer.getResourceId());
		val.put(OFFER_COLUMN_RESOURCE_TYPE, offer.getResourceType().getValue());
		val.put(OFFER_COLUMN_START_DATE, String.valueOf(offer.getStartDate()));
		val.put(OFFER_COLUMN_END_DATE, String.valueOf(offer.getEndDate()));
		val.put(OFFER_COLUMN_DATA, offer.getOfferData());
		val.put(OFFER_COLUMN_BY_EMPLOYEE, offer.getByEmployee());
		if(offer.getCreatedAt()!=null) {
			val.put(OFFER_COLUMN_CREATED_AT, String.valueOf(offer.getCreatedAt()));
		}
		if(offer.getUpdatedAt()!=null) {
			val.put(OFFER_COLUMN_UPDATED_AT, String.valueOf(offer.getUpdatedAt()));
		}


		try {
			String where = OFFER_COLUMN_ID + " = ?";
			db.update(OFFER_TABLE_NAME, val, where, new String[]{offer.getOfferId() + ""});
			Offer p=offerDBAdapter.getOfferById(offer.getOfferId());
			Log.d("Update Object",p.toString());
			offerDBAdapter.close();
			return 1;
		} catch (SQLException ex) {
			return 0;
		}
	}

}
