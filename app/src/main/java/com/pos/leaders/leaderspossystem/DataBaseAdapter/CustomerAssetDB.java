package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.CustomerAssest;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 8/27/2017.
 */

public class CustomerAssetDB {

    public static final String CustmerAsset_TabelName = "custmerAssest";
    // Column Names
    protected static final String CustmerAssest_Id = "id";

    protected static final String CustmerAssest_COLUMN_Order_Id = "order_id";
    protected static final String CustmerAssest_COLUMN_ID = "custmerAssestID";
    protected static final String CustmerAssest_COLUMN_amount= "amount";
    protected static final String CustmerAssest_COLUMN_type= "type";
    protected static final String CustmerAssest_COLUMN_Case= "salescase";
    public static final String DATABASE_CREATE = "CREATE TABLE `"+CustmerAsset_TabelName+"` ( `"+ CustmerAssest_Id+"` INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "`"+CustmerAssest_COLUMN_Order_Id+"` INTEGER ,"+
            "`"+CustmerAssest_COLUMN_ID+"` INTEGER NOT NULL, `"+CustmerAssest_COLUMN_amount+"` REAL, `"+CustmerAssest_COLUMN_type+"` INTEGER DEFAULT 0, `"+CustmerAssest_COLUMN_Case+"` TEXT );";
    private SQLiteDatabase db;
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;




    public CustomerAssetDB(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public CustomerAssetDB open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }



   
    public long insertEntry(long order_id,long user_id,double amount,int type,String salescase) {
        CustomerAssest assest = new CustomerAssest(Util.idHealth(this.db, CustmerAsset_TabelName, CustmerAssest_Id), order_id, user_id, amount,type,salescase);
        sendToBroker(MessageType.ADD_CUSTMER_ASSEST, assest, this.context);

        try {
            return insertEntry(assest);
        } catch (SQLException ex) {
            Log.e("Assest DB insert", "inserting Entry at " + CustmerAsset_TabelName + ": " + ex.getMessage());
            return -1;
        }
    }

    public long insertEntry(CustomerAssest assest){
        ContentValues val = new ContentValues();
        //Assign values for each row.

        val.put(CustmerAssest_Id, assest.getId());


        val.put(CustmerAssest_COLUMN_Order_Id, assest.getOrder_id());
        val.put(CustmerAssest_COLUMN_ID,assest.getCustmerAssestID() );
        val.put(CustmerAssest_COLUMN_amount, assest.getAmount());
        val.put(CustmerAssest_COLUMN_type, assest.getType());
        val.put(CustmerAssest_COLUMN_Case, assest.getSalescase());

        try {
            return db.insert(CustmerAsset_TabelName, null, val);
        } catch (SQLException ex) {
            Log.e("CustomerAssest DB insert", "inserting Entry at " + CustmerAsset_TabelName + ": " + ex.getMessage());
            return -1;
        }
    }

}
