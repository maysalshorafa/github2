package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Provider;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 7/2/2019.
 */

public class ProviderDbAdapter {
    //Table name
    public static final String PROVIDER_TABLE_NAME = "Provider";
    //column names
    protected static final String PROVIDER_COLUMN_ID = "id";
    protected static final String PROVIDER_COLUMN_FIRST_NAME = "firstName";
    protected static final String PROVIDER_COLUMN_LAST_NAME = "lastName";
    protected static final String PROVIDER_COLUMN_GENDER = "gender";
    protected static final String PROVIDER_COLUMN_EMAIL = "email";
    protected static final String PROVIDER_COLUMN_JOB = "job";
    public static final String PROVIDER_COLUMN_PHONE_NUMBER = "phoneNumber";
    protected static final String PROVIDER_COLUMN_STREET = "street";
    protected static final String PROVIDER_COLUMN_DISENABLED = "hide";
    protected static final String PROVIDER_COLUMN_CITY = "cityId";
    protected static final String PROVIDER_COLUMN_CLUB = "clubId";
    protected static final String PROVIDER_COLUMN_HOUSE_NUMBER = "houseNumber";
    protected static final String PROVIDER_COLUMN_POSTAL_CODE = "postalCode";
    protected static final String PROVIDER_COLUMN_COUNTRY = "country";
    protected static final String PROVIDER_COLUMN_COUNTRY_CODE = "countryCode";
    protected static final String PROVIDER_COLUMN_BALANCE = "balance";
    protected static final String PROVIDER_COLUMN_TYPE = "providerType";
    protected static final String PROVIDER_CODE= "providerCode";
    protected static final String PROVIDER_IDENTITY= "providerIdentity";
    protected static final String PROVIDER_BRANCH_ID= "branchId";



    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    public static final String DATABASE_CREATE = "CREATE TABLE Provider ( `id` INTEGER PRIMARY KEY AUTOINCREMENT , " + "`firstName` TEXT NOT NULL," + " `lastName` TEXT NOT NULL," + " `gender` TEXT," + "`email` TEXT," + " `job` TEXT , " +
            "`phoneNumber` TEXT," + " `street` TEXT ," + "`hide` INTEGER DEFAULT 0 ,`cityId` INTEGER," + " `clubId` INTEGER DEFAULT 0,`houseNumber` TEXT," + "`postalCode` TEXT," + " `country` TEXT," + " `countryCode` TEXT,"+   " `providerCode` TEXT,"+   "`providerIdentity` TEXT a,"
            +" `balance` Double DEFAULT 0," +" `branchId` INTEGER DEFAULT 0)";

    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public ProviderDbAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }

    public ProviderDbAdapter open() throws SQLException {
            this.db = dbHelper.getWritableDatabase();
            return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public long insertEntry(String firstName, String lastName, String gender, String email, String job, String phoneNumber, String street, int cityId, String houseNumber, String postalCode, String country, String countryCode,double balance,String providerCode,String providerIdentity,int branchId) throws JSONException {
        if (db.isOpen()){

        }
        else {
            open();
        }
        Provider provider = new Provider(Util.idHealth(this.db, PROVIDER_TABLE_NAME, PROVIDER_COLUMN_ID), firstName, lastName, gender, email, job, phoneNumber, street, false, cityId, houseNumber, postalCode, country, countryCode,balance,providerCode,providerIdentity,branchId);

                sendToBroker(MessageType.ADD_PROVIDER,provider,context);

        try {
            long insertResult = insertEntry(provider);
            close();
            return insertResult;
        } catch (SQLException ex) {
            Log.e("Provider insertEntry", "inserting Entry at " + PROVIDER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public long insertEntry(Provider provider) {
        if (db.isOpen()){

        }
        else {
            open();
        }
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PROVIDER_COLUMN_ID, provider.getProviderId());
        val.put(PROVIDER_COLUMN_FIRST_NAME, provider.getFirstName());
        val.put(PROVIDER_COLUMN_LAST_NAME, provider.getLastName());
        val.put(PROVIDER_COLUMN_GENDER, provider.getGender());
        val.put(PROVIDER_COLUMN_EMAIL, provider.getEmail());
        val.put(PROVIDER_COLUMN_JOB, provider.getJob());
        val.put(PROVIDER_COLUMN_DISENABLED, provider.isHide() ? 1 : 0);
        val.put(PROVIDER_COLUMN_PHONE_NUMBER, provider.getPhoneNumber());
        val.put(PROVIDER_COLUMN_STREET, provider.getStreet());
        val.put(PROVIDER_COLUMN_CITY, provider.getCity());
        val.put(PROVIDER_COLUMN_HOUSE_NUMBER, provider.getHouseNumber());
        val.put(PROVIDER_COLUMN_POSTAL_CODE, provider.getPostalCode());
        val.put(PROVIDER_COLUMN_COUNTRY, provider.getCountry());
        val.put(PROVIDER_COLUMN_COUNTRY_CODE, provider.getCountryCode());
        val.put(PROVIDER_COLUMN_BALANCE,provider.getBalance());
        val.put(PROVIDER_CODE,provider.getCountryCode());
        val.put(PROVIDER_IDENTITY,provider.getProviderIdentity());
        val.put(PROVIDER_BRANCH_ID,provider.getBranchId());

        try {
            return db.insert(PROVIDER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("ProviderDB insertEntry", "inserting Entry at " + PROVIDER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long insertEntryFromBo(Provider provider) {
        if (db.isOpen()){

        }
        else {
            open();
        }
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PROVIDER_COLUMN_ID, provider.getProviderId());
        val.put(PROVIDER_COLUMN_FIRST_NAME, provider.getFirstName());
        val.put(PROVIDER_COLUMN_LAST_NAME, provider.getLastName());
        val.put(PROVIDER_COLUMN_GENDER, provider.getGender());
        val.put(PROVIDER_COLUMN_EMAIL, provider.getEmail());
        val.put(PROVIDER_COLUMN_JOB, provider.getJob());
        val.put(PROVIDER_COLUMN_DISENABLED, provider.isHide() ? 1 : 0);
        val.put(PROVIDER_COLUMN_PHONE_NUMBER, provider.getPhoneNumber());
        val.put(PROVIDER_COLUMN_STREET, provider.getStreet());
        val.put(PROVIDER_COLUMN_CITY, provider.getCity());
        val.put(PROVIDER_COLUMN_HOUSE_NUMBER, provider.getHouseNumber());
        val.put(PROVIDER_COLUMN_POSTAL_CODE, provider.getPostalCode());
        val.put(PROVIDER_COLUMN_COUNTRY, provider.getCountry());
        val.put(PROVIDER_COLUMN_COUNTRY_CODE, provider.getCountryCode());
        val.put(PROVIDER_COLUMN_BALANCE,provider.getBalance());
        val.put(PROVIDER_CODE,provider.getCountryCode());
        val.put(PROVIDER_IDENTITY,provider.getProviderIdentity());
        val.put(PROVIDER_BRANCH_ID,provider.getBranchId());

        try {
            return db.insert(PROVIDER_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("CustomerDB insertEntry", "inserting Entry at " + PROVIDER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public Provider getProviderByID(long id) {
        if (db.isOpen()){

        }
        else {
            open();
        }
        Provider provider = null;
        Cursor cursor = db.query(PROVIDER_TABLE_NAME, null, PROVIDER_COLUMN_ID + "=? ", new String[]{id + ""}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
        {
            provider = makeProvider(cursor);
            cursor.close();
            return provider;
        }
        cursor.close();
        close();
        return provider;
    }


    public int deleteProvider(int id) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(PROVIDER_COLUMN_DISENABLED, 1);
        String where = PROVIDER_COLUMN_ID + " = ?";
        try {
            db.update(PROVIDER_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("Provider deleteEntry", "enable hide Entry at " + PROVIDER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }


    public void updateEntry(Provider provider) {
        if (db.isOpen()){

        }
        else {
            open();
        }
        ProviderDbAdapter providerDbAdapter=new ProviderDbAdapter(context);
        providerDbAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PROVIDER_COLUMN_FIRST_NAME, provider.getFirstName());
        val.put(PROVIDER_COLUMN_LAST_NAME, provider.getLastName());
        val.put(PROVIDER_COLUMN_GENDER, provider.getGender());
        val.put(PROVIDER_COLUMN_EMAIL, provider.getEmail());
        val.put(PROVIDER_COLUMN_JOB, provider.getJob());
        val.put(PROVIDER_COLUMN_DISENABLED, provider.isHide() ? 1 : 0);
        val.put(PROVIDER_COLUMN_PHONE_NUMBER, provider.getPhoneNumber());
        val.put(PROVIDER_COLUMN_STREET, provider.getStreet());
        val.put(PROVIDER_COLUMN_CITY, provider.getCity());
        val.put(PROVIDER_COLUMN_HOUSE_NUMBER, provider.getHouseNumber());
        val.put(PROVIDER_COLUMN_POSTAL_CODE, provider.getPostalCode());
        val.put(PROVIDER_COLUMN_COUNTRY, provider.getCountry());
        val.put(PROVIDER_COLUMN_COUNTRY_CODE, provider.getCountryCode());
        val.put(PROVIDER_COLUMN_BALANCE,provider.getBalance());
        val.put(PROVIDER_CODE,provider.getCountryCode());
        val.put(PROVIDER_IDENTITY,provider.getProviderIdentity());
        val.put(PROVIDER_BRANCH_ID,provider.getBranchId());


        String where = PROVIDER_COLUMN_ID + " = ?";
        db.update(PROVIDER_TABLE_NAME, val, where, new String[]{provider.getProviderId() + ""});
        Provider p=providerDbAdapter.getProviderByID(provider.getProviderId());
        Log.d("Update Object",p.toString());
        sendToBroker(MessageType.UPDATE_PROVIDER, p, this.context);
        providerDbAdapter.close();
        close();

    }
    public long updateEntryBo(Provider provider) {
        if (db.isOpen()){

        }
        else {
            open();
        }
        ProviderDbAdapter providerDbAdapter=new ProviderDbAdapter(context);
        providerDbAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PROVIDER_COLUMN_FIRST_NAME, provider.getFirstName());
        val.put(PROVIDER_COLUMN_LAST_NAME, provider.getLastName());
        val.put(PROVIDER_COLUMN_GENDER, provider.getGender());
        val.put(PROVIDER_COLUMN_EMAIL, provider.getEmail());
        val.put(PROVIDER_COLUMN_JOB, provider.getJob());
        val.put(PROVIDER_COLUMN_DISENABLED, provider.isHide() ? 1 : 0);
        val.put(PROVIDER_COLUMN_PHONE_NUMBER, provider.getPhoneNumber());
        val.put(PROVIDER_COLUMN_STREET, provider.getStreet());
        val.put(PROVIDER_COLUMN_CITY, provider.getCity());
        val.put(PROVIDER_COLUMN_HOUSE_NUMBER, provider.getHouseNumber());
        val.put(PROVIDER_COLUMN_POSTAL_CODE, provider.getPostalCode());
        val.put(PROVIDER_COLUMN_COUNTRY, provider.getCountry());
        val.put(PROVIDER_COLUMN_COUNTRY_CODE, provider.getCountryCode());
        val.put(PROVIDER_COLUMN_BALANCE,provider.getBalance());
        val.put(PROVIDER_CODE,provider.getCountryCode());
        val.put(PROVIDER_IDENTITY,provider.getProviderIdentity());
        val.put(PROVIDER_BRANCH_ID,provider.getBranchId());


        try {
            String where = PROVIDER_COLUMN_ID + " = ?";
            db.update(PROVIDER_TABLE_NAME, val, where, new String[]{provider.getProviderId() + ""});
            Provider p=providerDbAdapter.getProviderByID(provider.getProviderId());
            Log.d("Update Object",p.toString());
            providerDbAdapter.close();
            close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
    }



    private Provider makeProvider(Cursor cursor) {
        if (db.isOpen()){

        }
        else {
            open();
        }
        Provider c = new Provider(Long.parseLong(cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_GENDER)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_STREET)),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_DISENABLED))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_CITY))),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_HOUSE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_POSTAL_CODE)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_COUNTRY)),
                cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_COUNTRY_CODE)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_BALANCE))),
                cursor.getString(cursor.getColumnIndex(PROVIDER_CODE)), cursor.getString(cursor.getColumnIndex(PROVIDER_IDENTITY)),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PROVIDER_BRANCH_ID))));
        if (c.getFirstName() == null) {
            c.setFirstName("");
        }
        close();
        return c;
    }

    public List<Provider> getAllCustomer() {
        if (db.isOpen()){

        }
        else {
            open();
        }
        List<Provider> providers = new ArrayList<Provider>();
        Cursor cursor=null;
        if(SETTINGS.enableAllBranch) {
            cursor =  db.rawQuery( "select * from "+PROVIDER_TABLE_NAME+ " where " + PROVIDER_COLUMN_DISENABLED +" = 0 order by id desc", null );
        }else {
            cursor = db.rawQuery("select * from " + PROVIDER_TABLE_NAME + " where " + PROVIDER_BRANCH_ID + " = "+ SETTINGS.branchId+ " and " + PROVIDER_COLUMN_DISENABLED + "=0 order by id desc", null);

        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            providers.add(new Provider(Long.parseLong(cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_GENDER)),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_JOB)),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_PHONE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_STREET)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_DISENABLED))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_CITY))),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_HOUSE_NUMBER)),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_POSTAL_CODE)),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_COUNTRY)),
                    cursor.getString(cursor.getColumnIndex(PROVIDER_COLUMN_COUNTRY_CODE)),
                    0,
                    cursor.getString(cursor.getColumnIndex(PROVIDER_CODE)), cursor.getString(cursor.getColumnIndex(PROVIDER_IDENTITY)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(PROVIDER_BRANCH_ID)))));
            cursor.moveToNext();
        }
        close();
        return providers;
    }


    public int deleteEntry(long id) {
        if (db.isOpen()){

        }
        else {
            open();
        }
        ContentValues updatedValues = new ContentValues();
        ProviderDbAdapter providerDbAdapter =new ProviderDbAdapter(context);
        providerDbAdapter.open();
        updatedValues.put(PROVIDER_COLUMN_DISENABLED, 1);
        String where = PROVIDER_COLUMN_ID + " = ?";
        try {
            db.update(PROVIDER_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            Provider provider=providerDbAdapter.getProviderByID(id);
            sendToBroker(MessageType.DELETE_PROVIDER, provider, this.context);
            close();
            return 1;
        } catch (SQLException ex) {
            Log.e("CustomerDB deleteEntry", "enable hide Entry at " + PROVIDER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long deleteEntryBo(Provider provider) {
        if (db.isOpen()){

        }
        else {
            open();
        }
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(PROVIDER_COLUMN_DISENABLED, 1);
        String where = PROVIDER_COLUMN_ID + " = ?";
        try {
            db.update(PROVIDER_TABLE_NAME, updatedValues, where, new String[]{provider.getProviderId() + ""});
            close();
            return 1;
        } catch (SQLException ex) {
            Log.e("ProviderDB deleteEntry", "enable hide Entry at " + PROVIDER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public boolean availableProviderName(String providerName) {
        Cursor cursor = db.query(PROVIDER_TABLE_NAME, null, PROVIDER_COLUMN_FIRST_NAME + "=?", new String[]{providerName}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return false;
        }
        return true;
    }

    public boolean availableProviderId(String customerPhone) {
        if (db.isOpen()){

        }
        else {
            open();
        }
        Cursor cursor = db.query(PROVIDER_TABLE_NAME, null, PROVIDER_IDENTITY + "=?", new String[]{customerPhone}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            close();
            return false;
        }
        close();
        return true;
    }
    public static String addColumn(String columnName) {
        String dbc = "ALTER TABLE " + PROVIDER_TABLE_NAME
                + " add column " + columnName + " TEXT default normal;";
        return dbc;
    }

}
