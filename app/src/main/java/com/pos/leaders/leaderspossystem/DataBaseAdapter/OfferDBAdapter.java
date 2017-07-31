package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.OfferRule;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.Rule3;
import com.pos.leaders.leaderspossystem.Tools.Rule7;
import com.pos.leaders.leaderspossystem.Tools.Rule7DbAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 23/10/2016.
 */
public class OfferDBAdapter {
	// Table Name
	protected static final String OFFER_TABLE_NAME = "offers";

	// Column Names
	protected static final String OFFER_COLUMN_ID = "id";
	protected static final String OFFER_COLUMN_NAME = "name";
	protected static final String OFFER_COLUMN_STARTDATE = "startDate";
	protected static final String OFFER_COLUMN_ENDDATE = "endDate";
	protected static final String OFFER_COLUMN_CREATINGDATE = "creatingDate";
	protected static final String OFFER_COLUMN_ENABLE = "status";
	protected static final String OFFER_COLUMN_ClubId = "club_offer";






	public static final String DATABASE_CREATE = "CREATE TABLE offers ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"`name` TEXT NOT NULL, `startDate` TEXT DEFAULT current_timestamp, " +
			"`endDate` TEXT DEFAULT current_timestamp, `creatingDate` TEXT DEFAULT current_timestamp, enable INTEGER DEFAULT 1, " +
			"`x` REAL,`y` REAL,`z` REAL,`p` REAL,`a` REAL,`b` REAL,`c` REAL,`d` REAL,`e` REAL,`f` REAL,`g` REAL,`h` REAL, "+
			"`userId` INTEGER,`ruleId` INTEGER, FOREIGN KEY(`userId`) REFERENCES `users.id`, " +
			"FOREIGN KEY(`ruleId`) REFERENCES `offerRule.id` )";
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

	/**public int insertEntry(String name, Date startDate, Date endDate, Date creatingDate, boolean enable, int byUser, int ruleId) {
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(OFFER_COLUMN_NAME, name);
		val.put(OFFER_COLUMN_STARTDATE, startDate.getTime());
		val.put(OFFER_COLUMN_ENDDATE, endDate.getTime());
		val.put(OFFER_COLUMN_CREATINGDATE, creatingDate.getTime());
		val.put(OFFER_COLUMN_ENABLE, enable);
		val.put(OFFER_COLUMN_BYUSER, byUser);
		val.put(OFFER_COLUMN_RULEID, ruleId);
		try {
			db.insert(OFFER_TABLE_NAME, null, val);
			return 1;
		} catch (SQLException ex) {
			Log.e("OfferDB insert1", "inserting Entry at " + OFFER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

	public int insertEntry(String name, Date endDate, int byUser, int ruleId) {
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(OFFER_COLUMN_NAME, name);
		val.put(OFFER_COLUMN_ENDDATE, endDate.getTime());
		val.put(OFFER_COLUMN_BYUSER, byUser);
		val.put(OFFER_COLUMN_RULEID, ruleId);
		try {
			db.insert(OFFER_TABLE_NAME, null, val);
			return 1;
		} catch (SQLException ex) {
			Log.e("OfferDB insert2", "inserting Entry at " + OFFER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}
*/
	/**public int insertEntry(String name, Date endDate, int byUser, int ruleId,double x,double y,double z,double p) {
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(OFFER_COLUMN_NAME, name);
		val.put(OFFER_COLUMN_ENDDATE, endDate.getTime());
		val.put(OFFER_COLUMN_BYUSER, byUser);
		val.put(OFFER_COLUMN_RULEID, ruleId);
		val.put(OFFER_COLUMN_X, x);
		val.put(OFFER_COLUMN_Y, y);
		val.put(OFFER_COLUMN_Z, z);
		val.put(OFFER_COLUMN_P, p);
		try {
			db.insert(OFFER_TABLE_NAME, null, val);
			return 1;
		} catch (SQLException ex) {
			Log.e("OfferDB insert3", "inserting Entry at " + OFFER_TABLE_NAME + ": " + ex.getMessage());
			return 0;
		}
	}

    public int insertEntry(Offer o) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(OFFER_COLUMN_NAME,o.getName());
        val.put(OFFER_COLUMN_STARTDATE, o.getStartDate().toString());
        val.put(OFFER_COLUMN_ENDDATE, o.getEndDate().toString());
        val.put(OFFER_COLUMN_CREATINGDATE, o.getCreatingDate().getTime());
        val.put(OFFER_COLUMN_BYUSER, o.getByUser());
        val.put(OFFER_COLUMN_RULEID, o.getRuleId());
        val.put(OFFER_COLUMN_X, o.getX());
        val.put(OFFER_COLUMN_Y, o.getY());
        val.put(OFFER_COLUMN_Z, o.getZ());
        val.put(OFFER_COLUMN_P, o.getP());
        try {

            return (int)db.insert(OFFER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("OfferDB insert4", "inserting Entry at " + OFFER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
	 **/
	public void disableOffer(int id) {
		ContentValues val = new ContentValues();
		//Assign values for each row.
		val.put(OFFER_COLUMN_ENABLE, false);

		String where = OFFER_COLUMN_ID + " = ?";
		db.update(OFFER_TABLE_NAME, val, where, new String[]{id + ""});
	}

	/**public Offer getOfferById(int id){
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where id='" + id + "'", null);
		if (cursor.getCount() < 1) // Offer Not Exist
		{
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		return createOfferObject(cursor);
	}
**/
	/**public List<Offer> getAllOffers() {
		List<Offer> offerList = new ArrayList<Offer>();
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_ENABLE + "=1 order by id desc", null);
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
			offerList.add(createOfferObject(cursor));
			cursor.moveToNext();
		}
		return offerList;
	}
**/
	public int getAllValidOffers() {
		int offer_id=0;
		Offer offer =null;
		Cursor cursor = db.rawQuery("select * from " + OFFER_TABLE_NAME + " where " + OFFER_COLUMN_ENABLE +"="+1, null);
		cursor.moveToFirst();
if(cursor.getCount()<0){
    offer_id=0;
	return  offer_id;
}
	else	if( cursor != null && cursor.moveToFirst() ){
			offer= new Offer(Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ID))),
					cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_NAME)),
					DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_STARTDATE))),
					DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ENDDATE))),
					DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_CREATINGDATE))),
					Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ENABLE))),
					Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ClubId))));
			offer_id = offer.getId();
			cursor.close();
		}

return offer_id;
	}












	//int id, String name, Date startDate, Date endDate, Date creatingDate,boolean enable, int byUser,
	// int ruleId,double x,double y,double z,double p,double a, double b, double c, double d, double e, double f, double g, double h
/**	public Offer createOfferObject(Cursor cursor){
		return new Offer(Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ID))),
				cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_NAME)),
				DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_STARTDATE))),
				DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ENDDATE))),
				DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_CREATINGDATE))),
				Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(OFFER_COLUMN_ENABLE))),
				(cursor.getInt(cursor.getColumnIndex(OFFER_COLUMN_BYUSER))),
				(cursor.getInt(cursor.getColumnIndex(OFFER_COLUMN_RULEID))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_X))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_Y))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_Z))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_P))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_A))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_B))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_C))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_D))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_E))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_F))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_G))),
				(cursor.getDouble(cursor.getColumnIndex(OFFER_COLUMN_H))));
	}**/
}
