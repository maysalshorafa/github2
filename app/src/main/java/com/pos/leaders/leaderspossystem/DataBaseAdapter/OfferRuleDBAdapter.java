package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.OfferRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 23/10/2016.
 */

public class OfferRuleDBAdapter {
    // Table Name
    ////offer rule table
    protected static final String Rule_OFFER_TABLE_NAME = "offerRule";
    protected static final String Rule_OFFER_COLUMN_ID = "id";

    protected static final String OFFER_Rule_COLUMN_Rule_ID = "rule_id";
    protected static final String OFFER_Rule_COLUMN_Product = "product_id";


    public static final String DATABASE_CREATE= "CREATE TABLE IF NOT EXISTS offerRule ( `id` INTEGER ,"+" 'rule_id'  INTEGER  ,"+" 'product_id' INTEGER , FOREIGN KEY(`id`) REFERENCES `offers.id`)";
    // Variable to hold the database instance
    private SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public OfferRuleDBAdapter(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }

    public OfferRuleDBAdapter open() throws SQLException {
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

    public int insertEntry(int id, int rule,int product_id) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Rule_OFFER_COLUMN_ID, id);
        val.put(OFFER_Rule_COLUMN_Rule_ID, rule);
        val.put(OFFER_Rule_COLUMN_Product, product_id);

        try {
            db.insert(Rule_OFFER_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("RuleOffer insert", "insatring Entry at " + Rule_OFFER_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public OfferRule getRuleNo() {
        OfferDBAdapter offerDBAdapter=new OfferDBAdapter(context);
        offerDBAdapter.open();
        int offer_id=offerDBAdapter.getAllValidOffers();
        OfferRule offer =null;
        Cursor cursor = db.rawQuery("select * from " + Rule_OFFER_TABLE_NAME+ " where id='" + offer_id + "'" , null);
        cursor.moveToFirst();


        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return  offer;
        }
        cursor.moveToFirst();
        offer= new OfferRule(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Rule_OFFER_COLUMN_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFER_Rule_COLUMN_Rule_ID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFER_Rule_COLUMN_Product))));
        cursor.close();



        return offer;
    }
    public Boolean getProductStatus(int order) {
        OfferDBAdapter offerDBAdapter=new OfferDBAdapter(context);
        offerDBAdapter.open();
        int offer_id=offerDBAdapter.getAllValidOffers();
        OfferRule offer =getRuleNo();
        Cursor cursor = db.rawQuery("select * from " + Rule_OFFER_TABLE_NAME+ " where product_id='" + order + "'" , null);
        cursor.moveToFirst();


        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return  true;
        }
        cursor.moveToFirst();


        return false;
    }


    /**public OfferRule getOfferRuleByID(int id) {
        OfferRule offerRule = null;
        Cursor cursor = db.rawQuery("select * from " + OFFERROLL_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return offerRule;
        }
        cursor.moveToFirst();
        offerRule =new OfferRule(id,cursor.getString(cursor.getColumnIndex(OFFERROLL_COLUMN_NAME)));

        cursor.close();

        return offerRule;
    }*

    public void updateEntry(OfferRule offerRule) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(OFFERROLL_COLUMN_NAME, offerRule.getName());

        String where = OFFERROLL_COLUMN_ID + " = ?";
        db.update(OFFERROLL_TABLE_NAME, val, where, new String[]{offerRule.getId() + ""});
    }

    public List<OfferRule> getAllOfferRoll(){
        List<OfferRule> offerRuleList =new ArrayList<OfferRule>();

        Cursor cursor =  db.rawQuery( "select * from "+OFFERROLL_TABLE_NAME, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            offerRuleList.add(new OfferRule(Integer.parseInt(cursor.getString(cursor.getColumnIndex(OFFERROLL_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(OFFERROLL_COLUMN_NAME))));
            cursor.moveToNext();
        }

        return offerRuleList;
    }*/
}
