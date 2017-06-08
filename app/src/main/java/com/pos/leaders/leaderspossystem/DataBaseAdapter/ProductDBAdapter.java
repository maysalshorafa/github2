package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 18/10/2016.
 */

public class ProductDBAdapter {
    //Table name
    protected static final String PRODUCTS_TABLE_NAME = "products";
    //column names
    protected static final String PRODUCTS_COLUMN_ID = "id";
    protected static final String PRODUCTS_COLUMN_NAME = "name";
    protected static final String PRODUCTS_COLUMN_BARCODE = "barcode";
    protected static final String PRODUCTS_COLUMN_DESCRIPTION = "description";
    protected static final String PRODUCTS_COLUMN_PRICE = "price";
    protected static final String PRODUCTS_COLUMN_COSTPRICE = "costPrice";
    protected static final String PRODUCTS_COLUMN_WITHTAX = "withTax";
    protected static final String PRODUCTS_COLUMN_WEIGHABLE = "weighable";
    protected static final String PRODUCTS_COLUMN_CREATINGDATE = "creatingDate";
    protected static final String PRODUCTS_COLUMN_DISENABLED = "hide";
    protected static final String PRODUCTS_COLUMN_DEPARTMENTID = "depId";
    protected static final String PRODUCTS_COLUMN_BYUSER = "byUser";

    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    public static final String DATABASE_CREATE="CREATE TABLE products ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "`name` TEXT NOT NULL, `barcode` INTEGER NOT NULL, `description` TEXT,"+
            "`price` REAL NOT NULL, `costPrice` REAL, `withTax` INTEGER NOT NULL DEFAULT 1, "+
            "`weighable` INTEGER NOT NULL DEFAULT 0, `creatingDate` TEXT NOT NULL DEFAULT current_timestamp, "+
            "`hide` INTEGER DEFAULT 0, `depId` INTEGER NOT NULL, `byUser` INTEGER NOT NULL, "+
            "FOREIGN KEY(`depId`) REFERENCES `departments.id`, FOREIGN KEY(`byUser`) REFERENCES `users.id` )";
    // Variable to hold the database instance
    public SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private DbHelper dbHelper;

    public ProductDBAdapter(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }

    public ProductDBAdapter open() throws SQLException {
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

    public int insertEntry(String name,String barCode,String description,double price,double costPrice,
                           boolean withTax,boolean weighable,int depId,int byUser) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCTS_COLUMN_NAME, name);
        val.put(PRODUCTS_COLUMN_BARCODE, barCode);
        val.put(PRODUCTS_COLUMN_DESCRIPTION, description);
        val.put(PRODUCTS_COLUMN_PRICE, price);
        val.put(PRODUCTS_COLUMN_COSTPRICE, costPrice);
        val.put(PRODUCTS_COLUMN_WITHTAX, withTax);
        val.put(PRODUCTS_COLUMN_WEIGHABLE, weighable);
        val.put(PRODUCTS_COLUMN_DEPARTMENTID, depId);
        val.put(PRODUCTS_COLUMN_BYUSER, byUser);
        try {
            db.insert(PRODUCTS_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("UserDB insertEntry", "inserting Entry at " + PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public int insertEntry(Product p) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCTS_COLUMN_ID, p.getId());
        val.put(PRODUCTS_COLUMN_NAME, p.getName());
        val.put(PRODUCTS_COLUMN_BARCODE, p.getBarCode());
        val.put(PRODUCTS_COLUMN_DESCRIPTION, p.getDescription());
        val.put(PRODUCTS_COLUMN_PRICE, p.getPrice());
        val.put(PRODUCTS_COLUMN_COSTPRICE, p.getCostPrice());
        val.put(PRODUCTS_COLUMN_WITHTAX, p.isWithTax());
        val.put(PRODUCTS_COLUMN_WEIGHABLE, p.isWeighable());
        val.put(PRODUCTS_COLUMN_DEPARTMENTID, p.getDepartmentId());
        val.put(PRODUCTS_COLUMN_BYUSER, p.getByUser());
        try {
            db.insert(PRODUCTS_TABLE_NAME, null, val);
            return 1;
        } catch (SQLException ex) {
            Log.e("UserDB insertEntry", "inserting Entry at " + PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public double getProductPrice(int id){
        Cursor cursor = db.rawQuery("select * from " + PRODUCTS_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return -1.0;
        }
        cursor.moveToFirst();
        return Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_PRICE)));
    }


    public Product getProductByID(int id) {
        if(id==-1){
            return new Product(-1, context.getResources().getString(R.string.general));
        }
        Product product = null;
        Cursor cursor = db.rawQuery("select * from " + PRODUCTS_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return product;
        }
        cursor.moveToFirst();
        product = new Product(id,cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BARCODE)),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DESCRIPTION)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_PRICE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_COSTPRICE))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WITHTAX))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WEIGHABLE))),
                DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_CREATINGDATE))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DISENABLED))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DEPARTMENTID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BYUSER))));
        cursor.close();

        return product;
    }

    public Product getProductByBarCode(String barcode){
        Product product = null;
        Cursor cursor = db.rawQuery("select * from " + PRODUCTS_TABLE_NAME + " where barcode='" + barcode + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return product;
        }
        cursor.moveToFirst();
        product =new Product(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BARCODE)),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DESCRIPTION)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_PRICE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_COSTPRICE))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WITHTAX))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WEIGHABLE))),
                DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_CREATINGDATE))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DISENABLED))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DEPARTMENTID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BYUSER))));
        cursor.close();

        return product;
    }

    public int deleteEntry(int id) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(PRODUCTS_COLUMN_DISENABLED, 1);

        String where = PRODUCTS_COLUMN_ID + " = ?";
        try {
            db.update(PRODUCTS_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("Product deleteEntry", "enable hide Entry at " + PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public void updateEntry(Product product) {
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCTS_COLUMN_NAME, product.getName());
        val.put(PRODUCTS_COLUMN_BARCODE, product.getBarCode());
        val.put(PRODUCTS_COLUMN_DESCRIPTION, product.getDescription());
        val.put(PRODUCTS_COLUMN_PRICE, product.getPrice());
        val.put(PRODUCTS_COLUMN_COSTPRICE, product.getCostPrice());
        val.put(PRODUCTS_COLUMN_WITHTAX, product.isWithTax());
        val.put(PRODUCTS_COLUMN_WEIGHABLE, product.isWeighable());
        val.put(PRODUCTS_COLUMN_DEPARTMENTID, product.getDepartmentId());
        val.put(PRODUCTS_COLUMN_BYUSER, product.getByUser());

        String where = PRODUCTS_COLUMN_ID + " = ?";
        db.update(PRODUCTS_TABLE_NAME, val, where, new String[]{product.getId() + ""});
    }

    public List<Product> getAllProducts(){
        List<Product> productsList =new ArrayList<Product>();

        Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME +" where "+PRODUCTS_COLUMN_DISENABLED+"=0 order by id desc", null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            productsList.add(new Product(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BARCODE)),
                    cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DESCRIPTION)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_PRICE))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_COSTPRICE))),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WITHTAX))),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WEIGHABLE))),
                    DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_CREATINGDATE))),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DISENABLED))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DEPARTMENTID))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BYUSER)))));
            cursor.moveToNext();
        }

        return productsList;
    }

    public List<Product> getAllUserProducts(int  userId){
        List<Product> userProductList=new ArrayList<Product>();
        List<Product> productsList=getAllProducts();
        for (Product d:productsList) {
            if(d.getByUser()==userId)
                userProductList.add(d);
        }
        return productsList;
    }

	public List<Product> getAllProductsByDepartment(int departmentId){
		List<Product> productsList =new ArrayList<Product>();

		Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME+" where "+PRODUCTS_COLUMN_DEPARTMENTID+"="+departmentId+" and "+PRODUCTS_COLUMN_DISENABLED+"=0 order by id desc", null );
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
			productsList.add(new Product(Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_ID))),
					cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_NAME)),
					cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BARCODE)),
					cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DESCRIPTION)),
					Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_PRICE))),
					Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_COSTPRICE))),
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WITHTAX))),
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WEIGHABLE))),
					DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_CREATINGDATE))),
					Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DISENABLED))),
					Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DEPARTMENTID))),
					Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BYUSER)))));
			cursor.moveToNext();
		}

		return productsList;
	}

    public List<Product> getAllProductsByDepartment(int departmentId,int from ,int count){
        List<Product> productsList =new ArrayList<Product>();

        Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME+" where "+PRODUCTS_COLUMN_DEPARTMENTID+"="+departmentId+" and "+PRODUCTS_COLUMN_DISENABLED+"=0 order by id desc limit "+from+","+count, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
        }

        return productsList;
    }

    public List<Product> getTopProducts(int from ,int count){
        List<Product> productsList =new ArrayList<Product>();
//SELECT * FROM table limit 100, 200
        Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME +" where "+PRODUCTS_COLUMN_DISENABLED+"=0 order by id desc limit "+from+","+count, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
        }

        return productsList;
    }

    private Product makeProduct(Cursor cursor){
        Product p=new Product(
        Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BARCODE)),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DESCRIPTION)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_PRICE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_COSTPRICE))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WITHTAX))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_WEIGHABLE))),
                DateConverter.stringToDate(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_CREATINGDATE))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DISENABLED))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DEPARTMENTID))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BYUSER))));
        if(p.getDescription()==null){
            p.setDescription("");
        }
        if(Double.isNaN(p.getCostPrice())){
            p.setCostPrice(0.0f);
        }
        return p;
    }
}
