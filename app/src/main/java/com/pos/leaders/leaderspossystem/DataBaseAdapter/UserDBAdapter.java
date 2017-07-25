package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karam on 17/10/2016.
 */

public class UserDBAdapter {
    //Table name
    public static final String USERS_TABLE_NAME = "users";
    //column names
    protected static final String USERS_COLUMN_ID = "id";
    protected static final String USERS_COLUMN_USERNAME = "userName";
    protected static final String USERS_COLUMN_PASSWORD = "pwd";
    protected static final String USERS_COLUMN_FIRSTNAME = "firstName";
    protected static final String USERS_COLUMN_LASTNAME = "lastName";
    protected static final String USERS_COLUMN_CREATINGDATE = "visitDate";
    protected static final String USERS_COLUMN_DISENABLED = "hide";
    protected static final String USERS_COLUMN_PHONENUMBER = "phoneNumber";
    protected static final String USERS_COLUMN_DISCOUNTINPERCENTAGE = "present";
    protected static final String USERS_COLUMN_HOURLYWAGE = "hourlyWage";
    protected static final String USERS_COLUMN_PermmionName = "permissions_name";


    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    public static final String DATABASE_CREATE="CREATE TABLE IF NOT EXISTS users "+
            "( `id` INTEGER PRIMARY KEY AUTOINCREMENT,`userName` TEXT UNIQUE, "+
            "`firstName` TEXT NOT NULL, `lastName` TEXT, `visitDate` TEXT NOT NULL DEFAULT current_timestamp, "+
            "`pwd` TEXT UNIQUE, `hide` INTEGER DEFAULT 0, `phoneNumber` TEXT, `present` REAL NOT NULL DEFAULT 0, 'permissions_name' TEXT, "+
            "`hourlyWage` REAL DEFAULT 0.0 )";
    // Variable to hold the database instance
    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public UserDBAdapter(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }

    public UserDBAdapter open() throws SQLException {
        this.db=dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public int insertEntry(String userName,String password,String firstName,String lastName,String phoneNumber,Double persent,Double hourlyWag,String permissions_name ) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(USERS_COLUMN_USERNAME, userName);
        val.put(USERS_COLUMN_PASSWORD, password);
        val.put(USERS_COLUMN_FIRSTNAME, firstName);
        val.put(USERS_COLUMN_LASTNAME, lastName);
        val.put(USERS_COLUMN_PHONENUMBER, phoneNumber);
        val.put(USERS_COLUMN_DISCOUNTINPERCENTAGE, persent);
        val.put(USERS_COLUMN_HOURLYWAGE, hourlyWag);
        val.put(USERS_COLUMN_PermmionName, permissions_name);

        try {
            db.insert(USERS_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("UserDB insertEntry", "insatring Entry at " + USERS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public int insertEntry(User user) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(USERS_COLUMN_USERNAME, user.getUserName());
        val.put(USERS_COLUMN_PASSWORD, user.getPassword());
        val.put(USERS_COLUMN_FIRSTNAME, user.getFirstName());
        val.put(USERS_COLUMN_LASTNAME, user.getLastName());
        val.put(USERS_COLUMN_PHONENUMBER, user.getPhoneNumber());
        val.put(USERS_COLUMN_DISCOUNTINPERCENTAGE, user.getPresent());
        val.put(USERS_COLUMN_HOURLYWAGE, user.getHourlyWage());
        val.put(USERS_COLUMN_PermmionName,user.getPermtionName());
        try {
            db.insert(USERS_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("UserDB insertEntry", "insatring Entry at " + USERS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public User getUserByID(int id) {
		User user = null;
		Cursor cursor = db.query(USERS_TABLE_NAME, null, USERS_COLUMN_ID + "=? ", new String[]{id + ""}, null, null, null);
		//Cursor cursor = db.rawQuery("select * from " + USERS_TABLE_NAME + " where id='" + id + "'", null);
		cursor.moveToFirst();
		if (cursor.getCount() > 0) // UserName Exist
		{
			user = createNewUser(cursor);
			cursor.close();
			return user;
		}
		cursor.close();
		return user;
	}

    public User logIn(String userName,String Passowrd) {
        int userID = 0;
        Cursor cursor = db.query(USERS_TABLE_NAME, null, USERS_COLUMN_USERNAME + "=? and " + USERS_COLUMN_PASSWORD + "=?", new String[]{userName, Passowrd}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 ) // UserName Not Exist
        {
			String ID = cursor.getString(cursor.getColumnIndex(USERS_COLUMN_ID));

			User u=createNewUser(cursor);

			cursor.close();
			Log.i("Log in",u.toString());
			return u;
        }
		cursor.close();
		return null;
    }
    public User logIn(String Passowrd) {
        int userID = 0;
        Cursor cursor = db.query(USERS_TABLE_NAME, null, USERS_COLUMN_PASSWORD + "=?", new String[]{Passowrd}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 ) // UserName Not Exist
        {
            String ID = cursor.getString(cursor.getColumnIndex(USERS_COLUMN_ID));

            User u=createNewUser(cursor);

            cursor.close();
            Log.i("Log in",u.toString());
            return u;
        }
        cursor.close();
        return null;
    }

    public int deleteEntry(int id) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(USERS_COLUMN_DISENABLED, 1);

        String where = USERS_COLUMN_ID + " = ?";
        try {
            db.update(USERS_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("UserDB deleteEntry", "enable hide Entry at " + USERS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public void updateEntry(User user) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(USERS_COLUMN_USERNAME, user.getUserName());
        val.put(USERS_COLUMN_PASSWORD, user.getPassword());
        val.put(USERS_COLUMN_FIRSTNAME, user.getFirstName());
        val.put(USERS_COLUMN_LASTNAME, user.getLastName());
        val.put(USERS_COLUMN_PHONENUMBER, user.getPhoneNumber());
        val.put(USERS_COLUMN_DISCOUNTINPERCENTAGE, user.getPresent());
        val.put(USERS_COLUMN_HOURLYWAGE, user.getHourlyWage());
        val.put(USERS_COLUMN_PermmionName,user.getPermtionName());

        String where = USERS_COLUMN_ID + " = ?";
        db.update(USERS_TABLE_NAME, val, where, new String[]{user.getId() + ""});
    }

	public boolean availableUserName(String userName){
		Cursor cursor=db.query(USERS_TABLE_NAME,null,USERS_COLUMN_USERNAME+"=?",new String[]{userName},null,null,null);
		cursor.moveToFirst();
		if(cursor.getCount()>0){
			// User Name not available
			return false;
		}
		// User Name available
		return true;
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		Cursor cursor = db.rawQuery("select * from " + USERS_TABLE_NAME + " where " + USERS_COLUMN_DISENABLED + "=0 order by id desc", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			users.add(createNewUser(cursor));
			cursor.moveToNext();
		}
		return users;
	}

	private User createNewUser(Cursor cursor){
		return new User(Integer.parseInt(cursor.getString(cursor.getColumnIndex(USERS_COLUMN_ID)))
				, cursor.getString(cursor.getColumnIndex(USERS_COLUMN_USERNAME)), cursor.getString(cursor.getColumnIndex(USERS_COLUMN_PASSWORD))
				, cursor.getString(cursor.getColumnIndex(USERS_COLUMN_FIRSTNAME)), cursor.getString(cursor.getColumnIndex(USERS_COLUMN_LASTNAME))
				, DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(USERS_COLUMN_CREATINGDATE)))
				, Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(USERS_COLUMN_DISENABLED)))
				, cursor.getString(cursor.getColumnIndex(USERS_COLUMN_PHONENUMBER))
				, Double.parseDouble(cursor.getString(cursor.getColumnIndex(USERS_COLUMN_DISCOUNTINPERCENTAGE)))
				, Double.parseDouble(cursor.getString(cursor.getColumnIndex(USERS_COLUMN_HOURLYWAGE))),cursor.getString(cursor.getColumnIndex(USERS_COLUMN_PermmionName)));
	}
}