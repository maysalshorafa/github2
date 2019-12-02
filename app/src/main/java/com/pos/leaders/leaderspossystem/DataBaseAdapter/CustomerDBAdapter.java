package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.CustomerType;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 7/3/2017.
 */

public class CustomerDBAdapter {
    Bitmap page=null ;
    public static final String SAMPLE_FILE = "randompdf.pdf";
    ArrayList<Bitmap> bitmapList=new ArrayList<Bitmap>();
    //Table name
    public static final String CUSTOMER_TABLE_NAME = "customer";
    //column names
    protected static final String CUSTOMER_COLUMN_ID = "id";
    protected static final String CUSTOMER_COLUMN_FIRST_NAME = "firstName";
    protected static final String CUSTOMER_COLUMN_LAST_NAME = "lastName";
    protected static final String CUSTOMER_COLUMN_GENDER = "gender";
    protected static final String CUSTOMER_COLUMN_EMAIL = "email";
    protected static final String CUSTOMER_COLUMN_JOB = "job";
    public static final String CUSTOMER_COLUMN_PHONE_NUMBER = "phoneNumber";
    protected static final String CUSTOMER_COLUMN_STREET = "street";
    protected static final String CUSTOMER_COLUMN_DISENABLED = "hide";
    protected static final String CUSTOMER_COLUMN_CITY = "cityId";
    protected static final String CUSTOMER_COLUMN_CLUB = "clubId";
    protected static final String CUSTOMER_COLUMN_HOUSE_NUMBER = "houseNumber";
    protected static final String CUSTOMER_COLUMN_POSTAL_CODE = "postalCode";
    protected static final String CUSTOMER_COLUMN_COUNTRY = "country";
    protected static final String CUSTOMER_COLUMN_COUNTRY_CODE = "countryCode";
    protected static final String CUSTOMER_COLUMN_BALANCE = "balance";
    protected static final String CUSTOMER_COLUMN_TYPE = "customerType";
    protected static final String CUSTOMER_CODE= "customerCode";
    protected static final String CUSTOMER_IDENTITY= "customerIdentity";
    protected static final String CUSTOMER_BRANCH_ID= "branchId";



    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    public static final String DATABASE_CREATE = "CREATE TABLE customer ( `id` INTEGER PRIMARY KEY AUTOINCREMENT , " + "`firstName` TEXT NOT NULL," + " `lastName` TEXT NOT NULL," + " `gender` TEXT," + "`email` TEXT," + " `job` TEXT , " +
            "`phoneNumber` TEXT," + " `street` TEXT ," + "`hide` INTEGER DEFAULT 0 ,`cityId` INTEGER," + " `clubId` INTEGER DEFAULT 0,`houseNumber` TEXT," + "`postalCode` TEXT," + " 'country' TEXT," + " 'countryCode' TEXT,"+   " 'customerCode' TEXT,"+   "`customerType` TEXT," +  "`customerIdentity` TEXT a," +" 'balance' Double DEFAULT 0," +" 'branchId' INTEGER DEFAULT 0)";

    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public CustomerDBAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public CustomerDBAdapter open() throws SQLException {
            this.db = dbHelper.getWritableDatabase();
            return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public Customer getCustomerByName(String name) {
        Customer customer_m = null;
        try {

            if(db.isOpen()){

            }else {
                open();
            }

        Cursor cursor = db.rawQuery("select * from " + CUSTOMER_TABLE_NAME + " where firstName='" + name + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return customer_m;
        }
        cursor.moveToFirst();
        customer_m = new Customer(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE)),Double.parseDouble(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_BALANCE))), CustomerType.valueOf(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_TYPE)).toUpperCase()),cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)),cursor.getString(cursor.getColumnIndex(CUSTOMER_IDENTITY)), Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_BRANCH_ID))));
        cursor.close();
            close();

        } catch (Exception e) {
            Log.d("exception",e.toString());
        }
        return customer_m;
    }

    public long insertEntry(String firstName, String lastName, String gender, String email, String job, String phoneNumber, String street, int cityId, long clubId, String houseNumber, String postalCode, String country, String countryCode,double balance,CustomerType customerType,String customerCode,String customerIdentity,int branchId) throws JSONException {
        if(db.isOpen()){

        }else {
            open();
        }
        Customer customer_m = new Customer(Util.idHealth(this.db, CUSTOMER_TABLE_NAME, CUSTOMER_COLUMN_ID), firstName, lastName, gender, email, job, phoneNumber, street, false, cityId, clubId, houseNumber, postalCode, country, countryCode,balance,customerType,customerCode,customerIdentity,branchId);
        Customer boCustomer = customer_m;
        boCustomer.setFirstName(Util.getString(boCustomer.getFirstName()));
        boCustomer.setLastName(Util.getString(boCustomer.getLastName()));
        boCustomer.setGender(Util.getString(boCustomer.getGender()));
        boCustomer.setEmail(Util.getString(boCustomer.getEmail()));
        boCustomer.setJob(Util.getString(boCustomer.getJob()));
        boCustomer.setPhoneNumber(Util.getString(boCustomer.getPhoneNumber()));
        boCustomer.setStreet(Util.getString(boCustomer.getStreet()));
        boCustomer.setHouseNumber(Util.getString(boCustomer.getHouseNumber()));
        boCustomer.setPostalCode(Util.getString(boCustomer.getPostalCode()));
        boCustomer.setCountry(Util.getString(boCustomer.getCountry()));
        boCustomer.setCountryCode(Util.getString(boCustomer.getCountryCode()));
        boCustomer.setCustomerCode(Util.getString(boCustomer.getCustomerCode()));
        boCustomer.setCustomerIdentity(Util.getString(boCustomer.getCustomerIdentity()));

        sendToBroker(MessageType.ADD_CUSTOMER,boCustomer,context);

        try {
            long insertResult = insertEntry(customer_m);
            close();
            return insertResult;
        } catch (SQLException ex) {
            Log.e("OldCustomer insertEntry", "inserting Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public long insertEntry(Customer customer) {
        if(db.isOpen()){

        }else {
            open();
        }
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CUSTOMER_COLUMN_ID, customer.getCustomerId());
        val.put(CUSTOMER_COLUMN_FIRST_NAME, customer.getFirstName());
        val.put(CUSTOMER_COLUMN_LAST_NAME, customer.getLastName());
        val.put(CUSTOMER_COLUMN_GENDER, customer.getGender());
        val.put(CUSTOMER_COLUMN_EMAIL, customer.getEmail());
        val.put(CUSTOMER_COLUMN_JOB, customer.getJob());
        val.put(CUSTOMER_COLUMN_DISENABLED, customer.isHide() ? 1 : 0);
        val.put(CUSTOMER_COLUMN_PHONE_NUMBER, customer.getPhoneNumber());
        val.put(CUSTOMER_COLUMN_STREET, customer.getStreet());
        val.put(CUSTOMER_COLUMN_CITY, customer.getCity());
        val.put(CUSTOMER_COLUMN_CLUB, customer.getClub());
        val.put(CUSTOMER_COLUMN_HOUSE_NUMBER, customer.getHouseNumber());
        val.put(CUSTOMER_COLUMN_POSTAL_CODE, customer.getPostalCode());
        val.put(CUSTOMER_COLUMN_COUNTRY, customer.getCountry());
        val.put(CUSTOMER_COLUMN_COUNTRY_CODE, customer.getCountryCode());
        val.put(CUSTOMER_COLUMN_BALANCE,customer.getBalance());
        val.put(CUSTOMER_COLUMN_TYPE,customer.getCustomerType().getValue());
        val.put(CUSTOMER_CODE,customer.getCountryCode());
        val.put(CUSTOMER_IDENTITY,customer.getCustomerIdentity());
        val.put(CUSTOMER_BRANCH_ID,customer.getBranchId());

        try {
            return db.insert(CUSTOMER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("CustomerDB insertEntry", "inserting Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long insertEntryFromBo(Customer customer) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CUSTOMER_COLUMN_ID, customer.getCustomerId());
        val.put(CUSTOMER_COLUMN_FIRST_NAME, customer.getFirstName());
        val.put(CUSTOMER_COLUMN_LAST_NAME, customer.getLastName());
        val.put(CUSTOMER_COLUMN_GENDER, customer.getGender());
        val.put(CUSTOMER_COLUMN_EMAIL, customer.getEmail());
        val.put(CUSTOMER_COLUMN_JOB, customer.getJob());
        val.put(CUSTOMER_COLUMN_DISENABLED, customer.isHide() ? 1 : 0);
        val.put(CUSTOMER_COLUMN_PHONE_NUMBER, customer.getPhoneNumber());
        val.put(CUSTOMER_COLUMN_STREET, customer.getStreet());
        val.put(CUSTOMER_COLUMN_CITY, customer.getCity());
        val.put(CUSTOMER_COLUMN_CLUB, customer.getClub());
        val.put(CUSTOMER_COLUMN_HOUSE_NUMBER, customer.getHouseNumber());
        val.put(CUSTOMER_COLUMN_POSTAL_CODE, customer.getPostalCode());
        val.put(CUSTOMER_COLUMN_COUNTRY, customer.getCountry());
        val.put(CUSTOMER_COLUMN_COUNTRY_CODE, customer.getCountryCode());
        val.put(CUSTOMER_COLUMN_BALANCE,customer.getBalance());
        val.put(CUSTOMER_COLUMN_TYPE,customer.getCustomerType().getValue());
        val.put(CUSTOMER_CODE,customer.getCustomerCode());
        val.put(CUSTOMER_IDENTITY,customer.getCustomerIdentity());
        val.put(CUSTOMER_BRANCH_ID,customer.getBranchId());

        try {
            return db.insert(CUSTOMER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("CustomerDB insertEntry", "inserting Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public Customer getCustomerByID(long id) {
        if(db.isOpen()){

        }else {
            open();
        }
        Customer customer = null;
        Cursor cursor = db.query(CUSTOMER_TABLE_NAME, null, CUSTOMER_COLUMN_ID + "=? ", new String[]{id + ""}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
        {
            customer = createNewCustomer(cursor);
            cursor.close();
            return customer;
        }
        cursor.close();
        close();
        return customer;
    }


    public int deleteCustomer(int id) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(CUSTOMER_COLUMN_DISENABLED, 1);
        String where = CUSTOMER_COLUMN_ID + " = ?";
        try {
            db.update(CUSTOMER_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("OldCustomer deleteEntry", "enable hide Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }


    public void updateEntry(Customer customer) {
        if(db.isOpen()){

        }else {
            open();
        }
        CustomerDBAdapter customerDBAdapter=new CustomerDBAdapter(context);
        customerDBAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CUSTOMER_COLUMN_FIRST_NAME, customer.getFirstName());
        val.put(CUSTOMER_COLUMN_LAST_NAME, customer.getLastName());
        val.put(CUSTOMER_COLUMN_GENDER, customer.getGender());
        val.put(CUSTOMER_COLUMN_EMAIL, customer.getEmail());
        val.put(CUSTOMER_COLUMN_JOB, customer.getJob());
        val.put(CUSTOMER_COLUMN_DISENABLED, customer.isHide() ? 1 : 0);
        val.put(CUSTOMER_COLUMN_PHONE_NUMBER, customer.getPhoneNumber());
        val.put(CUSTOMER_COLUMN_STREET, customer.getStreet());
        val.put(CUSTOMER_COLUMN_CITY, customer.getCity());
        val.put(CUSTOMER_COLUMN_CLUB, customer.getClub());
        val.put(CUSTOMER_COLUMN_HOUSE_NUMBER, customer.getHouseNumber());
        val.put(CUSTOMER_COLUMN_POSTAL_CODE, customer.getPostalCode());
        val.put(CUSTOMER_COLUMN_COUNTRY, customer.getCountry());
        val.put(CUSTOMER_COLUMN_COUNTRY_CODE, customer.getCountryCode());
        val.put(CUSTOMER_COLUMN_BALANCE,customer.getBalance());
        val.put(CUSTOMER_COLUMN_TYPE,customer.getCustomerType().getValue());
        val.put(CUSTOMER_CODE,customer.getCustomerCode());
        val.put(CUSTOMER_IDENTITY,customer.getCustomerIdentity());
        val.put(CUSTOMER_BRANCH_ID,customer.getBranchId());

        String where = CUSTOMER_COLUMN_ID + " = ?";
        db.update(CUSTOMER_TABLE_NAME, val, where, new String[]{customer.getCustomerId() + ""});
        Customer c=customerDBAdapter.getCustomerByID(customer.getCustomerId());
        Log.d("Update Object",c.toString());
        sendToBroker(MessageType.UPDATE_CUSTOMER, c, this.context);
        customerDBAdapter.close();
        close();

    }
    public long updateEntryBo(Customer customer) {
        if(db.isOpen()){

        }else {
            open();
        }
        CustomerDBAdapter customerDBAdapter=new CustomerDBAdapter(context);
        customerDBAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(CUSTOMER_COLUMN_FIRST_NAME, customer.getFirstName());
        val.put(CUSTOMER_COLUMN_LAST_NAME, customer.getLastName());
        val.put(CUSTOMER_COLUMN_GENDER, customer.getGender());
        val.put(CUSTOMER_COLUMN_EMAIL, customer.getEmail());
        val.put(CUSTOMER_COLUMN_JOB, customer.getJob());
        val.put(CUSTOMER_COLUMN_DISENABLED, customer.isHide() ? 1 : 0);
        val.put(CUSTOMER_COLUMN_PHONE_NUMBER, customer.getPhoneNumber());
        val.put(CUSTOMER_COLUMN_STREET, customer.getStreet());
        val.put(CUSTOMER_COLUMN_CITY, customer.getCity());
        val.put(CUSTOMER_COLUMN_CLUB, customer.getClub());
        val.put(CUSTOMER_COLUMN_HOUSE_NUMBER, customer.getHouseNumber());
        val.put(CUSTOMER_COLUMN_POSTAL_CODE, customer.getPostalCode());
        val.put(CUSTOMER_COLUMN_COUNTRY, customer.getCountry());
        val.put(CUSTOMER_COLUMN_COUNTRY_CODE, customer.getCountryCode());
        val.put(CUSTOMER_COLUMN_BALANCE,customer.getBalance());
        val.put(CUSTOMER_COLUMN_TYPE,customer.getCustomerType().getValue());
        val.put(CUSTOMER_CODE,customer.getCustomerCode());
        val.put(CUSTOMER_IDENTITY,customer.getCustomerIdentity());
        val.put(CUSTOMER_BRANCH_ID,customer.getBranchId());

        try {
            String where = CUSTOMER_COLUMN_ID + " = ?";
            db.update(CUSTOMER_TABLE_NAME, val, where, new String[]{customer.getCustomerId() + ""});
            Customer c=customerDBAdapter.getCustomerByID(customer.getCustomerId());
            Log.d("Update Object",c.toString());
            customerDBAdapter.close();
            close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
    }

    public List<Customer> getTopCustomer(int from, int count) {
        List<Customer> customerList = new ArrayList<Customer>();
        Cursor cursor=null;
        try {
            if(db.isOpen()){

            }else {
                open();
            }
            if (SETTINGS.enableAllBranch) {
                cursor = db.rawQuery("select * from " + CUSTOMER_TABLE_NAME + " where " + CUSTOMER_COLUMN_DISENABLED + " = 0 order by id desc", null);
            } else {
                cursor = db.rawQuery("select * from " + CUSTOMER_TABLE_NAME + " where " + CUSTOMER_BRANCH_ID + " = " + SETTINGS.branchId + " and " + CUSTOMER_COLUMN_DISENABLED + "=0 order by id desc", null);

            }
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                customerList.add(makeCustomer(cursor));
                cursor.moveToNext();
            }
        close();
        } catch (Exception e) {
            Log.d("exception",e.toString());

        }
        return customerList;
    }

    private Customer makeCustomer(Cursor cursor) {
        Customer c = new Customer(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_BALANCE))),CustomerType.valueOf(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_TYPE)).toUpperCase()),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)), cursor.getString(cursor.getColumnIndex(CUSTOMER_IDENTITY)), Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_BRANCH_ID))));
        if (c.getFirstName() == null) {
            c.setFirstName("");
        }
        return c;
    }

    public List<Customer> getAllCustomer() {
        List<Customer> customerMs = new ArrayList<Customer>();
        try {
            if(db.isOpen()){

            }else {
                open();
            }
        Cursor cursor=null;
        if(SETTINGS.enableAllBranch) {
            cursor =  db.rawQuery( "select * from "+CUSTOMER_TABLE_NAME+ " where " + CUSTOMER_COLUMN_DISENABLED +" = 0 order by id desc", null );
        }else {
            cursor = db.rawQuery("select * from " + CUSTOMER_TABLE_NAME + " where " + CUSTOMER_BRANCH_ID + " = "+ SETTINGS.branchId+ " and " + CUSTOMER_COLUMN_DISENABLED + "=0 order by id desc", null);

        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            customerMs.add(new Customer(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_BALANCE))),CustomerType.valueOf(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_TYPE)).toUpperCase()), cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)), cursor.getString(cursor.getColumnIndex(CUSTOMER_IDENTITY)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_BRANCH_ID)))));
            cursor.moveToNext();
        }  close();
        } catch (Exception e) {
            Log.d("exception",e.toString());

        }
        return customerMs;
    }

    public List<Customer> getAllCustomerInClub(long id) {
        List<Customer> customerMs = new ArrayList<Customer>();
        try {
            if(db.isOpen()){

            }else {
                open();
            }
        Cursor cursor=null;
        if(SETTINGS.enableAllBranch) {
            cursor =  db.rawQuery( "select * from "+CUSTOMER_TABLE_NAME+ " where "+ CUSTOMER_COLUMN_CLUB +"=" +id  +" and "  + CUSTOMER_COLUMN_DISENABLED +" = 0 order by id desc", null );
        }else {
            cursor = db.rawQuery("select * from " + CUSTOMER_TABLE_NAME + " where "+ CUSTOMER_COLUMN_CLUB +"=" +id  +" and " + CUSTOMER_BRANCH_ID + " = "+ SETTINGS.branchId+ " and " + CUSTOMER_COLUMN_DISENABLED + "=0 order by id desc", null);

        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            customerMs.add(new Customer(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_BALANCE))),CustomerType.valueOf(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_TYPE)).toUpperCase()), cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)), cursor.getString(cursor.getColumnIndex(CUSTOMER_IDENTITY)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_BRANCH_ID)))));
            cursor.moveToNext();
        }
        close();
        } catch (Exception e) {
            Log.d("exception",e.toString());

        }
        return customerMs;
    }

    public int deleteEntry(long id) {
        if(db.isOpen()){

        }else {
            open();
        }
        ContentValues updatedValues = new ContentValues();
        CustomerDBAdapter customerDBAdapter =new CustomerDBAdapter(context);
        customerDBAdapter.open();
        updatedValues.put(CUSTOMER_COLUMN_DISENABLED, 1);
        String where = CUSTOMER_COLUMN_ID + " = ?";
        try {
            db.update(CUSTOMER_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            Customer customer=customerDBAdapter.getCustomerByID(id);
            sendToBroker(MessageType.DELETE_CUSTOMER, customer, this.context);
            close();
            return 1;
        } catch (SQLException ex) {
            Log.e("CustomerDB deleteEntry", "enable hide Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long deleteEntryBo(Customer customer) {
        if(db.isOpen()){

        }else {
            open();
        }
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(CUSTOMER_COLUMN_DISENABLED, 1);
        String where = CUSTOMER_COLUMN_ID + " = ?";
        try {
            db.update(CUSTOMER_TABLE_NAME, updatedValues, where, new String[]{customer.getCustomerId() + ""});
            close();
            return 1;
        } catch (SQLException ex) {
            Log.e("CustomerDB deleteEntry", "enable hide Entry at " + CUSTOMER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public boolean availableCustomerName(String customerName) {
        Cursor cursor = db.query(CUSTOMER_TABLE_NAME, null, CUSTOMER_COLUMN_FIRST_NAME + "=?", new String[]{customerName}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            // OldCustomer Name not available
            return false;
        }
        //customer Name available
        return true;
    }

    private Customer createNewCustomer(Cursor cursor) {
        return new Customer(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)),
                cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_BALANCE))),CustomerType.valueOf(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_TYPE)).toUpperCase()), cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)), cursor.getString(cursor.getColumnIndex(CUSTOMER_IDENTITY)),  Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_BRANCH_ID))));
    }
    public boolean availableCustomerPhoneNo(String customerPhone) {
        if(db.isOpen()){

        }else {
            open();
        }
        Cursor cursor = db.query(CUSTOMER_TABLE_NAME, null, CUSTOMER_COLUMN_PHONE_NUMBER + "=?", new String[]{customerPhone}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return false;
        }
        close();
        return true;
    }
    public static String addColumn(String columnName) {
        String dbc = "ALTER TABLE " + CUSTOMER_TABLE_NAME
                + " add column " + columnName + " TEXT default normal;";
        return dbc;
    }
    public List<Customer> getAllNormalCustomer() {
        List<Customer> customerMs = new ArrayList<Customer>();
        try {
            if(db.isOpen()){

            }else {
                open();
            }
        Cursor cursor = db.rawQuery("select * from " + CUSTOMER_TABLE_NAME + " where " + CUSTOMER_COLUMN_DISENABLED + "=0"+" and " + CUSTOMER_COLUMN_TYPE + " = '"+ CustomerType.CREDIT.getValue()+"' order by id desc", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            customerMs.add(new Customer(Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_JOB)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_STREET)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_DISENABLED))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CITY))),
                    Long.parseLong(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_CLUB))),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_HOUSE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_POSTAL_CODE)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY)),
                    cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_COUNTRY_CODE)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_BALANCE))),CustomerType.valueOf(cursor.getString(cursor.getColumnIndex(CUSTOMER_COLUMN_TYPE)).toUpperCase()), cursor.getString(cursor.getColumnIndex(CUSTOMER_CODE)), cursor.getString(cursor.getColumnIndex(CUSTOMER_IDENTITY)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(CUSTOMER_BRANCH_ID)))));
            cursor.moveToNext();
        }
            close();
        } catch (Exception e) {
            Log.d("exception",e.toString());

        }
        return customerMs;
    }
    public static String addColumnInteger(String columnName) {
        String dbc = "ALTER TABLE " + CUSTOMER_TABLE_NAME
                + " add column " + columnName + " INTEGER  DEFAULT 0 ;";
        return dbc;
    }
}