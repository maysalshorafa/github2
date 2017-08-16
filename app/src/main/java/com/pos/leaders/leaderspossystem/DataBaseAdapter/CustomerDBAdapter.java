package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.AdapterView;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.Customer_M;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 7/3/2017.
 */

public class CustomerDBAdapter {

    //Table name
    public static final String CUSTOMER_TABLE_NAME = "customer";
    //column names

    protected static final String CUSTOMER_COLUMN_ID = "id";
    protected static final String CUSTOMER_COLUMN_NAME = "name";
    protected static final String CUSTOMER_COLUMN_BIRTHDAY = "birthday";
    protected static final String CUSTOMER_COLUMN_GENDER = "gender";
    protected static final String CUSTOMER_COLUMN_EMAIL = "email";
    protected static final String CUSTOMER_COLUMN_JOB = "job";
    protected static final String CUSTOMER_COLUMN_PHONENUMBER = "phoneNumber";
    protected static final String CUSTOMER_COLUMN_ADDRESS = "address";
    protected static final String CUSTOMER_COLUMN_DISENABLED = "hide";
    protected static final String CUSTOMER_COLUMN_CITY= "city_id";
    protected static final String CUSTOMER_COLUMN_Club= "club_id";






    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    public static final String DATABASE_CREATE="CREATE TABLE customer ( `id` INTEGER PRIMARY KEY , "+
            "`name` TEXT NOT NULL, `birthday` TEXT NOT NULL, `gender` TEXT,"+ "`email` TEXT, `job` TEXT NOT NULL, "+
            "`phoneNumber` TEXT, `address` TEXT NOT NULL, `city_id` INTEGER, `club_id` INTEGER, `hide` INTEGER DEFAULT 0)";
    // Variable to hold the database instance



    public  SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    private static boolean isEmpty = true;

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
        Cursor cursor = db.rawQuery("select * from " + CUSTOMER_TABLE_NAME + " where name='" + name + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return customer_m;
        }
        cursor.moveToFirst();
        customer_m =new Customer_M(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_BIRTHDAY)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),

                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),

                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONENUMBER)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))),    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY)),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_Club))));
        cursor.close();

        return customer_m;
    }

    public int insertEntry(String id , String name, String birthday, String gender, String email, String job,
                            String phoneNumber, String address  , int select_city_id , int select_club_id) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CUSTOMER_COLUMN_NAME, name);
        val.put(CUSTOMER_COLUMN_BIRTHDAY, birthday);
        val.put(CUSTOMER_COLUMN_GENDER, gender);
        val.put(CUSTOMER_COLUMN_EMAIL, email);
        val.put(CUSTOMER_COLUMN_JOB, job);
        val.put(CUSTOMER_COLUMN_PHONENUMBER, phoneNumber);

        val.put(CUSTOMER_COLUMN_ADDRESS, address);
        val.put(CUSTOMER_COLUMN_ID, id);
        val.put(CUSTOMER_COLUMN_CITY, select_city_id);

        val.put(CUSTOMER_COLUMN_Club, select_club_id);


        try {
            db.insert(CUSTOMER_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("UserDB insertEntry", "inserting Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
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

    private Customer_M createNewCustmer(Cursor cursor){
        return new Customer_M(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_BIRTHDAY)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),

                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),

                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONENUMBER)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))),    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY)),
                Integer.parseInt( cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_Club))));
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
        val.put(CUSTOMER_COLUMN_NAME, customer.getName());
        val.put(CUSTOMER_COLUMN_BIRTHDAY, customer.getBirthday());
        val.put(CUSTOMER_COLUMN_GENDER, customer.getGender());
        val.put(CUSTOMER_COLUMN_EMAIL, customer.getEmail());
        val.put(CUSTOMER_COLUMN_ADDRESS, customer.getAddress());
        val.put(CUSTOMER_COLUMN_DISENABLED, customer.getHide());
        val.put(CUSTOMER_COLUMN_JOB, customer.getJob());
        val.put(CUSTOMER_COLUMN_PHONENUMBER, customer.getPhoneNumber());
        val.put(CUSTOMER_COLUMN_CITY, customer.getCity());
        val.put(CUSTOMER_COLUMN_Club, customer.getClub());



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
        Customer_M c=new Customer_M(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_BIRTHDAY)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),

                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),

                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONENUMBER)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))),  cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY)),
                Integer.parseInt( cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_Club))));
        if(c.getCustmerName()==null){
            c.setName("");
        }

        return c;
    }
    public List<Customer_M> getAllCustmer(){
        List<Customer_M> customerMs =new ArrayList<Customer_M>();

        Cursor cursor =  db.rawQuery( "select * from "+CUSTOMER_TABLE_NAME , null );
        cursor.moveToFirst();


        while(!cursor.isAfterLast()){
            customerMs.add(new Customer_M(Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_BIRTHDAY)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),

                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),

                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONENUMBER)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))),  cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY)),
                    Integer.parseInt(  cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_Club)))));

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
    public boolean availableCustmerName(String userName){
        Cursor cursor=db.query(CUSTOMER_TABLE_NAME,null,CUSTOMER_COLUMN_NAME+"=?",new String[]{userName},null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            //custmer Name not available
            return false;
        }
        // custmer Name available
        return true;
    }


}
