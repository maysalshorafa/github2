package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.City;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 10/12/2017.
 */

public class CityDbAdapter {
    private static final String LOG_TAG = "City1DB";

    public static final String City_TABLE_NAME = "City";
    // Column Names
    protected static final String City_COLUMN_Id = "id";
    protected static final String City_COLUMN_Name = "name";
    public static final String DATABASE_CREATE = "CREATE TABLE City ( `id` INTEGER PRIMARY KEY AUTOINCREMENT ," + " `name` TEXT )";
    private DbHelper dbHelper;
    Context context;
    private SQLiteDatabase db;

    public CityDbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public CityDbAdapter open() throws SQLException {
        try {
            this.db = dbHelper.getWritableDatabase();
            return this;

        } catch (SQLException s) {
            new Exception("Error with DB Open");
            return this;

        }
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public int insertEntry(String name) {
        ContentValues val = new ContentValues();

        //Assign values for each row.
        val.put(City_COLUMN_Id, Util.idHealth(this.db,City_TABLE_NAME,City_COLUMN_Id));
        val.put(City_COLUMN_Name, name);
        City city = new City(Util.idHealth(this.db, City_TABLE_NAME, City_COLUMN_Id), name);
                City boCity = city;
                sendToBroker(MessageType.ADD_CITY, boCity, this.context);

        try {
            db.insert(City_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e( LOG_TAG,"inserting Entry at " + City_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long insertEntry(City city){
        ContentValues val = new ContentValues();
        val.put(City_COLUMN_Id,city.getCityId());
        //Assign values for each row.
        val.put(City_COLUMN_Name, city.getName());
        try {
            return db.insert(City_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("City DB insert", "inserting Entry at " + City_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }


    public List<City> getAllCity() {
        List<City> city = new ArrayList<City>();
        Cursor cursor = db.rawQuery("select * from " + City_TABLE_NAME , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            city.add(createNewCity(cursor));
            cursor.moveToNext();
        }
        return city;
    }

    private City createNewCity(Cursor cursor){
        return new City(Long.parseLong(cursor.getString(cursor.getColumnIndex(City_COLUMN_Id)))
                , cursor.getString(cursor.getColumnIndex(City_COLUMN_Name)));
    }
}
