package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Customer_M;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 7/3/2017.
 */

public class CustomerDBAdapter {
    //Table name
    public static final String CUSTOMER_TABLE_NAME = "customer";
    //column names
    protected static final String CUSTOMER_COLUMN_ID = "id";
    protected static final String CUSTOMER_COLUMN_FIRST_NAME = "firstName";
    protected static final String CUSTOMER_COLUMN_LAST_NAME = "lastName";
    protected static final String CUSTOMER_COLUMN_GENDER = "gender";
    protected static final String CUSTOMER_COLUMN_EMAIL = "email";
    protected static final String CUSTOMER_COLUMN_JOB = "job";
    protected static final String CUSTOMER_COLUMN_PHONENUMBER = "phoneNumber";
    protected static final String CUSTOMER_COLUMN_STREET = "street";
    protected static final String CUSTOMER_COLUMN_DISENABLED = "hide";
    protected static final String CUSTOMER_COLUMN_CITY= "cityId";
    protected static final String CUSTOMER_COLUMN_CLUB = "clubId";
    protected static final String CUSTOMER_COLUMN_HOUSE_NUMBER = "houseNumber";
    protected static final String CUSTOMER_COLUMN_POSTAL_CODE = "postalCode";
    protected static final String CUSTOMER_COLUMN_COUNTRY = "country";
    protected static final String CUSTOMER_COLUMN_COUNTRY_CODE = "countryCode";
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    public static final String DATABASE_CREATE="CREATE TABLE customer ( `id` INTEGER PRIMARY KEY AUTOINCREMENT , "+ "`firstName` TEXT NOT NULL,"+ " `lastName` TEXT NOT NULL,"+" `gender` TEXT,"+ "`email` TEXT,"+" `job` TEXT NOT NULL, "+
            "`phoneNumber` TEXT,"+" `street` TEXT NOT NULL,"+"`hide` INTEGER DEFAULT 0 ,`cityId` INTEGER,"+" `clubId` INTEGER,`houseNumber` TEXT,"+ "`postalCode` TEXT,"+ " 'country' TEXT,"+ " 'countryCode' TEXT )";

    public  SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;
    public CustomerDBAdapter(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }
    public CustomerDBAdapter open() throws SQLException {
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

    public Customer_M getCustmerByname(String name){
        Customer_M customer_m = null;
        Cursor cursor = db.rawQuery("select * from " + CUSTOMER_TABLE_NAME + " where firstName='" + name + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return customer_m;
        }
        cursor.moveToFirst();
        customer_m =new Customer_M(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONENUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                Integer.parseInt( cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE)));
        cursor.close();

        return customer_m;
    }

    public long insertEntry( String firstName, String lastName, String gender, String email, String job, String phoneNumber, String street  , int cityId,long clubId,String houseNumber,String postalCode,String country, String countryCode) {
        Customer_M customer_m = new Customer_M(Util.idHealth(this.db, CUSTOMER_TABLE_NAME, CUSTOMER_COLUMN_ID), firstName, lastName, gender, email, job, phoneNumber, street, false , cityId, clubId,houseNumber,postalCode,country,countryCode);
        sendToBroker(MessageType.ADD_CUSTOMER, customer_m, this.context);

        try {
            long insertResult = insertEntry(customer_m);
            return insertResult;
        } catch (SQLException ex) {
            Log.e("Custmer insertEntry", "inserting Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public long insertEntry(Customer_M customer) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CUSTOMER_COLUMN_ID, customer.getId());
        val.put(CUSTOMER_COLUMN_FIRST_NAME, customer.getFirstName());
        val.put(CUSTOMER_COLUMN_LAST_NAME, customer.getLastName());
        val.put(CUSTOMER_COLUMN_GENDER, customer.getGender());
        val.put(CUSTOMER_COLUMN_EMAIL, customer.getEmail());
        val.put(CUSTOMER_COLUMN_JOB, customer.getJob());
        val.put(CUSTOMER_COLUMN_DISENABLED, customer.isHide()?1:0);
        val.put(CUSTOMER_COLUMN_PHONENUMBER, customer.getPhoneNumber());
        val.put(CUSTOMER_COLUMN_STREET, customer.getStreet());
        val.put(CUSTOMER_COLUMN_CITY, customer.getCity());
        val.put(CUSTOMER_COLUMN_CLUB, customer.getClub());
        val.put(CUSTOMER_COLUMN_HOUSE_NUMBER,customer.getHouseNumber());
        val.put(CUSTOMER_COLUMN_POSTAL_CODE,customer.getPostalCode());
        val.put(CUSTOMER_COLUMN_COUNTRY,customer.getCountry());
        val.put(CUSTOMER_COLUMN_COUNTRY_CODE,customer.getCountryCode());

        try {
            return db.insert(CUSTOMER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("CustmerDB insertEntry", "inserting Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public Customer_M getCustmerByID(long id) {
        Customer_M customer = null;
        Cursor cursor = db.query(CUSTOMER_TABLE_NAME, null, CUSTOMER_COLUMN_ID + "=? ", new String[]{id + ""}, null, null, null);
        //Cursor cursor = db.rawQuery("select * from " + USERS_TABLE_NAME + " where id='" + id + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) // UserName Exist
        {
            customer = createNewCustmer(cursor);
            cursor.close();
            return customer;
        }
        cursor.close();
        return customer;
    }


    public int deleteCustomer(int id) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(CUSTOMER_COLUMN_DISENABLED, 1);

        String where = CUSTOMER_COLUMN_ID + " = ?";
        try {
            db.update(CUSTOMER_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("Customer deleteEntry", "enable hide Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }


    public void updateEntry(Customer_M customer) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CUSTOMER_COLUMN_ID, customer.getId());
        val.put(CUSTOMER_COLUMN_FIRST_NAME, customer.getFirstName());
        val.put(CUSTOMER_COLUMN_LAST_NAME, customer.getLastName());
        val.put(CUSTOMER_COLUMN_GENDER, customer.getGender());
        val.put(CUSTOMER_COLUMN_EMAIL, customer.getEmail());
        val.put(CUSTOMER_COLUMN_JOB, customer.getJob());
        val.put(CUSTOMER_COLUMN_DISENABLED, customer.isHide()?1:0);
        val.put(CUSTOMER_COLUMN_PHONENUMBER, customer.getPhoneNumber());
        val.put(CUSTOMER_COLUMN_STREET, customer.getStreet());
        val.put(CUSTOMER_COLUMN_CITY, customer.getCity());
        val.put(CUSTOMER_COLUMN_CLUB, customer.getClub());
        val.put(CUSTOMER_COLUMN_HOUSE_NUMBER,customer.getHouseNumber());
        val.put(CUSTOMER_COLUMN_POSTAL_CODE,customer.getPostalCode());
        val.put(CUSTOMER_COLUMN_COUNTRY,customer.getCountry());
        val.put(CUSTOMER_COLUMN_COUNTRY_CODE,customer.getCountryCode());
        String where = CUSTOMER_COLUMN_ID + " = ?";
        db.update(CUSTOMER_TABLE_NAME, val, where, new String[]{customer.getId() + ""});
    }

    public List<Customer_M> getTopCustmer(int from ,int count){
        List<Customer_M> custmerlist =new ArrayList<Customer_M>();
//SELECT * FROM table limit 100, 200
        Cursor cursor =  db.rawQuery( "select * from "+CUSTOMER_TABLE_NAME , null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            custmerlist.add(makecustmer(cursor));
            cursor.moveToNext();
        }

        return custmerlist;
    }
    private Customer_M makecustmer(Cursor cursor){
        Customer_M c= new Customer_M(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONENUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                Long.parseLong( cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE)));
        if(c.getCustmerName()==null){
            c.setFirstName("");
        }

        return c;
    }
    public List<Customer_M> getAllCustmer(){
        List<Customer_M> customerMs =new ArrayList<Customer_M>();

        Cursor cursor =  db.rawQuery( "select * from "+CUSTOMER_TABLE_NAME , null );
        cursor.moveToFirst();


        while(!cursor.isAfterLast()){
            customerMs.add( new Customer_M(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONENUMBER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                    Long.parseLong( cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE))));
            cursor.moveToNext();
        }
        return customerMs;
    }
    public List<Customer_M> getAllCustmerInClub(long id){
        List<Customer_M> customerMs =new ArrayList<Customer_M>();

        Cursor cursor =  db.rawQuery("select * from " + CUSTOMER_TABLE_NAME+ " where id='" + id + "'", null );
        cursor.moveToFirst();


        while(!cursor.isAfterLast()){
            customerMs.add( new Customer_M(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONENUMBER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                    Long.parseLong( cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE))));
            cursor.moveToNext();
        }
        return customerMs;
    }

    public int deleteEntry(long id) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(CUSTOMER_COLUMN_DISENABLED, 1);

        String where = CUSTOMER_COLUMN_ID + " = ?";
        try {
            db.update(CUSTOMER_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("CustmerDB deleteEntry", "enable hide Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public boolean availableCustomerrName(String custmerName){
        Cursor cursor=db.query(CUSTOMER_TABLE_NAME,null,CUSTOMER_COLUMN_FIRST_NAME+"=?",new String[]{custmerName},null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            // Customer Name not available
            return false;
        }
        //customer Name available
        return true;
    }
    private Customer_M createNewCustmer(Cursor cursor){
        return new Customer_M(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONENUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))), Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                Long.parseLong( cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)), cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE)));
    }


}
