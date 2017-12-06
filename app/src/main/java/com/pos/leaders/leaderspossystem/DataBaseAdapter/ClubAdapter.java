package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by Win8.1 on 6/21/2017.
 */

public class ClubAdapter {
    public static final String Group_TABLE_NAME = "club";
    // Column Names
    protected static final String Group_COLUMN_Name = "name";
    protected static final String Group_COLUMN__ID = "id";

    protected static final String Group_COLUMN__Descrption = "description";
    protected static final String Group_COLUMN_Type = "type";
    protected static final String Group_COLUMN_Parcent = "parcent";
    protected static final String Group_COLUMN_Amount = "amount";
    protected static final String Group_COLUMN_Point = "point";
    protected static final String Group_COLUMN_DISENABLED = "hide";



    public static final String DATABASE_CREATE= "CREATE TABLE IF NOT EXISTS club ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,"+"`name` TEXT NOT NULL,"+"'description' Text ,"+"'type' INTEGER ,"+" 'parcent'  REAL DEFAULT 0 ,"+" 'amount' REAL DEFAULT 0,"+" 'point' REAL DEFAULT 0 ,"+"`hide` INTEGER DEFAULT 0 )";
    private SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public ClubAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public ClubAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public int getRowCount(){
        Cursor cursor = db.rawQuery("select * from " + Group_TABLE_NAME , null);
        return cursor.getCount();
    }

    public long insertEntry( String name,String description, int type, float parcent, int amount, int point) {
        Club group = new Club(Util.idHealth(this.db, Group_TABLE_NAME, Group_COLUMN__ID), name, description, type, parcent, amount, point, false );
        sendToBroker(MessageType.ADD_CLUB, group, this.context);

        try {
            long insertResult = insertEntry(group);
            return insertResult;
        } catch (SQLException ex) {
            Log.e("Club insertEntry", "inserting Entry at " + Group_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public long insertEntry(Club group) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Group_COLUMN__ID, group.getId());
        val.put(Group_COLUMN_Name, group.getname());
        val.put(Group_COLUMN__Descrption, group.getDescription());
        val.put(Group_COLUMN_Type, group.getType());
        val.put(Group_COLUMN_Parcent, group.getParcent());
        val.put(Group_COLUMN_Amount, group.getAmount());
        val.put(Group_COLUMN_Point, group.getPoint());
        val.put(Group_COLUMN_DISENABLED, group.isHide()?1:0);
        try {
            return db.insert(Group_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("GroupDB insertEntry", "inserting Entry at " + Group_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public int updateEntry(String name,String description, String type, String parcent, String amount, String point){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Group_COLUMN_Name,name);
        val.put(Group_COLUMN__Descrption,description);
        val.put(Group_COLUMN_Type,type);
        val.put(Group_COLUMN_Parcent,parcent);
        val.put(Group_COLUMN_Amount,amount);
        val.put(Group_COLUMN_Point,point);
        try {

            db.update(Group_TABLE_NAME,val,null,null);
            return 1;
        } catch (SQLException ex) {
            Log.e("Club insertEntry", "inserting Entry at " + Group_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }

    }
    public boolean availableGrouprName(String groupName){
        Cursor cursor=db.query(Group_TABLE_NAME,null,Group_COLUMN_Name+"=?",new String[]{groupName},null,null,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            // group Name not available
            return false;
        }
        //group Name available
        return true;
    }

    /*
    public void read(Cursor cursor) {
        cursor.moveToFirst();
        Club.name= cursor.getString(cursor.getColumnIndex(Group_COLUMN_Name));
        Club.description= cursor.getString(cursor.getColumnIndex(Group_COLUMN__Descrption));
        Club.type= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Type)));
        Club.amount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Amount)));
        Club.parcent=Integer.parseInt( cursor.getString(cursor.getColumnIndex(Group_COLUMN_Parcent)));


        cursor.close();
    }*/

    public Club getGroupInfo(long club_id){
            Club group = null;
            Cursor cursor = db.rawQuery("select * from " + Group_TABLE_NAME + " where id='" + club_id + "'", null);
            if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return group;
        }
            cursor.moveToFirst();
            group = new Club(Long.parseLong(cursor.getString(cursor.getColumnIndex(Group_COLUMN__ID))),
                    cursor.getString(cursor.getColumnIndex(Group_COLUMN_Name)),
                    cursor.getString(cursor.getColumnIndex(Group_COLUMN__Descrption)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Type))),
                    (float) Double.parseDouble(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Parcent))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Amount))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Point))), Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Group_COLUMN_DISENABLED))));
            cursor.close();

            return group;

    }
    public Club getGroupByID(long id) {
        Club group = null;
        Cursor cursor = db.query(Group_TABLE_NAME, null, Group_COLUMN__ID + "=? ", new String[]{id + ""}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
        {
            group = createNewGroup(cursor);
            cursor.close();
            return group;
        }
        cursor.close();
        return group;
    }
    public List<Club> getAllGroup() {
        List<Club> groups = new ArrayList<Club>();
        Cursor cursor = db.rawQuery("select * from " + Group_TABLE_NAME + " where " + Group_COLUMN_DISENABLED + "=0 order by id desc", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            groups.add(createNewGroup(cursor));
            cursor.moveToNext();
        }
        return groups;
    }

    private Club createNewGroup(Cursor cursor){
        return  new Club(Long.parseLong(cursor.getString(cursor.getColumnIndex(Group_COLUMN__ID))),
                cursor.getString(cursor.getColumnIndex(Group_COLUMN_Name)),
                cursor.getString(cursor.getColumnIndex(Group_COLUMN__Descrption)),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Type))),
                (float) Double.parseDouble(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Parcent))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Amount))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Point))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Group_COLUMN_DISENABLED))));
    }
    public int deleteEntry(long id) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(Group_COLUMN_DISENABLED, 1);

        String where = Group_COLUMN__ID + " = ?";
        try {
            db.update(Group_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("Club deleteEntry", "enable hide Entry at " + Group_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }


}
