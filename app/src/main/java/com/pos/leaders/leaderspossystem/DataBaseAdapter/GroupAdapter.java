package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 6/21/2017.
 */

public class GroupAdapter {
    public static final String Group_TABLE_NAME = "club";
    // Column Names
    protected static final String Group_COLUMN_Name = "name";
    protected static final String Group_COLUMN__ID = "id";

    protected static final String Group_COLUMN__Descrption = "description";
    protected static final String Group_COLUMN_Type = "type";
    protected static final String Group_COLUMN_Parcent = "parcent";
    protected static final String Group_COLUMN_Amount = "amount";
    protected static final String Group_COLUMN_Point = "point";



    public static final String DATABASE_CREATE= "CREATE TABLE IF NOT EXISTS club ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,"+"`name` TEXT NOT NULL,"+"'description' Text ,"+"'type' INTEGER ,"+" 'parcent'  REAL DEFAULT 0 ,"+" 'amount' REAL DEFAULT 0,"+" 'point' REAL DEFAULT 0  )";
    private SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public GroupAdapter(Context context) {
        this.context = context;
        this.dbHelper = new DbHelper(context);
    }


    public GroupAdapter open() throws SQLException {
        this.db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public List<Group> getAllGroup(){
        List<Group> groupList =new ArrayList<>();

        Cursor cursor =  db.rawQuery( "select * from "+Group_TABLE_NAME,null );

        cursor.moveToFirst();


        while(!cursor.isAfterLast()){
            groupList.add(new Group(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN__ID))),
                    cursor.getString(cursor.getColumnIndex(Group_COLUMN_Name)),
                    cursor.getString(cursor.getColumnIndex(Group_COLUMN__Descrption)),

                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Type)))
                    , (float) Double.parseDouble(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Parcent))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Amount)))
                    ,Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Point)))));

            cursor.moveToNext();

        }

        return groupList;
    }
    public int getRowCount(){
        Cursor cursor = db.rawQuery("select * from " + Group_TABLE_NAME , null);
        return cursor.getCount();
    }
    public int insertEntry(String name,String description, String type, String parcent, String amount, String point){
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(Group_COLUMN_Name,name);
        val.put(Group_COLUMN__Descrption,description);
        val.put(Group_COLUMN_Type,type);
        val.put(Group_COLUMN_Parcent,parcent);
        val.put(Group_COLUMN_Amount,amount);
        val.put(Group_COLUMN_Point,point);



        try {

        db.insert(Group_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("Group insertEntry", "inserting Entry at " + Group_TABLE_NAME + ": " + ex.getMessage());
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
            Log.e("Group insertEntry", "inserting Entry at " + Group_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }

    }

    public void read(Cursor cursor) {
        cursor.moveToFirst();
        Group.name= cursor.getString(cursor.getColumnIndex(Group_COLUMN_Name));
        Group.description= cursor.getString(cursor.getColumnIndex(Group_COLUMN__Descrption));
        Group.type= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Type)));
        Group.amount= Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Amount)));
        Group.parcent=Integer.parseInt( cursor.getString(cursor.getColumnIndex(Group_COLUMN_Parcent)));


        cursor.close();
    }
    public  Group getGroupInfo(int club_id){
            Group group = null;
            Cursor cursor = db.rawQuery("select * from " + Group_TABLE_NAME + " where id='" + club_id + "'", null);
            if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return group;
        }
            cursor.moveToFirst();
            group =new Group(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN__ID))),
                    cursor.getString(cursor.getColumnIndex(Group_COLUMN_Name)),
                    cursor.getString(cursor.getColumnIndex(Group_COLUMN__Descrption)),

                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Type)))
             , (float) Double.parseDouble(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Parcent))),       Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Amount))),Integer.parseInt(cursor.getString(cursor.getColumnIndex(Group_COLUMN_Point))));
            cursor.close();

            return group;

    }


}
