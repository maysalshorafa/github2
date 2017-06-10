package com.pos.leaders.leaderspossystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.*;
import com.pos.leaders.leaderspossystem.Models.*;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

/**
 * Created by Karam on 16/10/2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "POSDB.db";
    protected static final String CONTACTS_TABLE_NAME = "contacts";
    protected static final String CONTACTS_COLUMN_ID = "id";
    protected static final String CONTACTS_COLUMN_NAME = "name";
    protected static final String CONTACTS_COLUMN_EMAIL = "email";
    protected static final String CONTACTS_COLUMN_STREET = "street";
    protected static final String CONTACTS_COLUMN_CITY = "place";
    protected static final String CONTACTS_COLUMN_PHONE = "phone";
    private HashMap hp;

    //19
    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME ,null,19);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub


//        db.execSQL(AReportDBAdapter.DATABASE_CREATE);
/*
        db.execSQL(SettingsDBAdapter.DATABASE_CREATE);
        db.execSQL("insert into " + SettingsDBAdapter.SETTINGS_TABLE_NAME + "  values ('','','',0,'',0,'','');");
*/
/*
        db.execSQL("delete from z_report where id=1");
        db.execSQL("delete from z_report where id=2");
        db.execSQL("delete from z_report where id=3");
        db.execSQL("delete from z_report where id=4");
        db.execSQL("delete from z_report where id=4");
        */
        //db.execSQL("DROP TABLE IF EXISTS z_report");
        //db.execSQL(ZReportDBAdapter.DATABASE_CREATE);
        /*
        db.execSQL("ALTER TABLE `offers` ADD COLUMN y REAL");
        db.execSQL("ALTER TABLE `offers` ADD COLUMN z REAL");
        db.execSQL("ALTER TABLE `offers` ADD COLUMN p REAL");
        db.execSQL("ALTER TABLE `offers` ADD COLUMN a REAL");
        db.execSQL("ALTER TABLE `offers` ADD COLUMN b REAL");
        db.execSQL("ALTER TABLE `offers` ADD COLUMN c REAL");
        db.execSQL("ALTER TABLE `offers` ADD COLUMN d REAL");
        db.execSQL("ALTER TABLE `offers` ADD COLUMN e REAL");
        db.execSQL("ALTER TABLE `offers` ADD COLUMN f REAL");
        db.execSQL("ALTER TABLE `offers` ADD COLUMN g REAL");
        db.execSQL("ALTER TABLE `offers` ADD COLUMN h REAL");
       // db.execSQL("ALTER TABLE `sales` ADD COLUMN totalPaid REAL");
*/
        //db.execSQL("insert into "+UserDBAdapter.USERS_TABLE_NAME+"  values (1,'User','User','User','"+new Date().getTime()+"','1234',0,100,0,35);");

       // db.execSQL(ChecksDBAdapter.DATABASE_CREATE);

/*
        db.execSQL(SaleDBAdapter.DATABASE_CREATE);

        db.execSQL(ChecksDBAdapter.DATABASE_CREATE);
        db.execSQL(DepartmentDBAdapter.DATABASE_CREATE);
        db.execSQL(OfferRuleDBAdapter.DATABASE_CREATE);
        db.execSQL(OfferDBAdapter.DATABASE_CREATE);
        db.execSQL(OrderDBAdapter.DATABASE_CREATE);
        db.execSQL(PaymentDBAdapter.DATABASE_CREATE);
        db.execSQL(PermissionsDBAdapter.DATABASE_CREATE);
        db.execSQL(ProductDBAdapter.DATABASE_CREATE);
        db.execSQL(ProductOfferDBAdapter.DATABASE_CREATE);

        db.execSQL(ScheduleWorkersDBAdapter.DATABASE_CREATE);
        db.execSQL(SettingsDBAdapter.DATABASE_CREATE);
        db.execSQL("insert into " + SettingsDBAdapter.SETTINGS_TABLE_NAME + "  values ('','','',0,'',0,'','');");
        db.execSQL(UserDBAdapter.DATABASE_CREATE);
        db.execSQL("insert into "+UserDBAdapter.USERS_TABLE_NAME+"  values (1,'Dev','Ops','tec','"+new Date().getTime()+"','1234',0,046316969,20,35);");
        db.execSQL(UserPermissionsDBAdapter.DATABASE_CREATE);
        db.execSQL(ZReportDBAdapter.DATABASE_CREATE);
        db.execSQL(AReportDBAdapter.DATABASE_CREATE);

        /*
        db.execSQL("ALTER TABLE `_order` ADD COLUMN price REAL");
        db.execSQL("ALTER TABLE `_order` ADD COLUMN original_price REAL");
        db.execSQL("ALTER TABLE `_order` ADD COLUMN discount REAL");
*/
		//db.execSQL("ALTER TABLE `order` ADD COLUMN userOffer REAL");









		/*for (String s : Permissions.PERMISSIONS_ROLE) {
			db.execSQL("insert into "+PermissionsDBAdapter.PERMISSIONS_TABLE_NAME+" (name) values ('"+s+"');");
		}*/


        Log.w("Create DBAdapter ", "");
        /*db.execSQL(
                "CREATE TABLE IF NOT EXISTS users ( `id` INTEGER PRIMARY KEY AUTOINCREMENT,`userName` TEXT UNIQUE, `firstName` TEXT NOT NULL, `lastName` TEXT, `visitDate` TEXT NOT NULL DEFAULT current_timestamp, `pwd` TEXT NOT NULL, `hide` INTEGER DEFAULT 0, `phoneNumber` TEXT, `present` REAL NOT NULL DEFAULT 0, `hourlyWage` REAL DEFAULT 0.0 )"
        );/*
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS workLog ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `userId` INTEGER, `date` TEXT DEFAULT current_timestamp, `startTime` TEXT, `exitTime` TEXT, FOREIGN KEY(`userId`) REFERENCES users.id"
        );*//*
        db.execSQL(
                "INSERT INTO users (`userName`,`pwd`,`firstName`,`lastName`,`phoneNumber`) VALUES ('root','1234','karam','abed','0527068607')"
        );
        db.execSQL(
                "INSERT INTO users (`userName`,`pwd`,`firstName`,`lastName`,`phoneNumber`) VALUES ('karam','12','ard','abed','0527068607')"
        );*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " +oldVersion + " to " +newVersion + ", which will destroy all old data");
        //db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    /*
    * users table
    * insert to table
    * get user by id
    * get all users
    * get by User Name and Password
     */
    public boolean insertUser (String userName, String firstName, String lastName, String creatingDate, String pwd,
                               Integer hide,String phoneNumber,double present,double hourlyWage)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", userName);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("visitDate", creatingDate);
        contentValues.put("pwd", pwd);
        contentValues.put("hide", hide);
        contentValues.put("phoneNumber", phoneNumber);
        contentValues.put("present", present);
        contentValues.put("hourlyWage", hourlyWage);
        db.insert("users", null, contentValues);
        return true;
    }

    public User getUserByID(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from users where id='"+id+"'", null );
        res.moveToFirst();
        User user=null;
        user=new User(getColumnValueInt("id",res),getColumnValue("userName",res),getColumnValue("pwd",res)
                ,getColumnValue("firstName",res),getColumnValue("lastName",res), DateConverter.stringToDate(getColumnValue("visitDate",res))
                ,getColumnValueBoolean("hide",res),getColumnValue("phoneNumber",res)
                ,Double.parseDouble(getColumnValue("present",res)),Double.parseDouble(getColumnValue("hourlyWage",res)));
        return user;
    }

    private boolean getColumnValueBoolean(String Column, Cursor cr) {
        int b=cr.getInt(cr.getColumnIndex(Column));
        if(b==0)
            return false;
        return true;
    }

    private String getColumnValue(String Column,Cursor cr){
        return cr.getString(cr.getColumnIndex(Column));
    }

    private int getColumnValueInt(String Column, Cursor cr){
        return cr.getInt(cr.getColumnIndex(Column));
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateUser (Integer id, String userName, String firstName, String lastName, Date creatingDate, String pwd,
                               Integer hide, String phoneNumber, double present, double hourlyWage)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", userName);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("visitDate", creatingDate.getTime());
        contentValues.put("pwd", pwd);
        contentValues.put("hide", hide);
        contentValues.put("phoneNumber", phoneNumber);
        contentValues.put("present", present);
        contentValues.put("hourlyWage", hourlyWage);
        db.update("users", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean deleteUser (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hide", 1);
        db.update("users",contentValues,"id = ? ",new String[]{Integer.toString(id)});
        return true;
    }
    public int login(String userName,String Password){
        SQLiteDatabase db=this.getReadableDatabase();
        int id=0;

        Cursor res;
        try {
            Log.w("user name is:",userName);
            Log.w("password is :",Password);
            res = db.rawQuery("select * from users where userName='" + userName + "' and pwd='" + Password+"'", null);
            // res=db.query("users", new String[]{"id","userName","pwd"},"userName=? and pwd=?",new String[]{userName,Password},null,null,null);
            res.moveToFirst();
            Log.e("count of rows :",res.getCount()+"");

            if(res.getCount()>0){
                id=res.getInt(res.getColumnIndex("id"));
                Log.i("user id",id+"");
                res.close();
                return id;
            }
            res.close();
            return id;
        }
        catch (Exception sqlEX){

            Log.e("error",sqlEX.getMessage());
            ///// TODO: 17/10/2016 log error
            return id;
        }
    }

    public ArrayList<String> getAllUsers()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex("firstName")));
            res.moveToNext();
        }
        return array_list;
    }
}