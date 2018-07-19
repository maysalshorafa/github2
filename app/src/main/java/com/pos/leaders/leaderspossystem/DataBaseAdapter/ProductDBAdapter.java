package com.pos.leaders.leaderspossystem.DataBaseAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DbHelper;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

/**
 * Created by KARAM on 18/10/2016.
 */

public class ProductDBAdapter {

      //Table name
    public static final String PRODUCTS_TABLE_NAME = "products";
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
    protected static final String PRODUCTS_COLUMN_CATEGORYID = "categoryId";
    protected static final String PRODUCTS_COLUMN_BYUSER = "byEmployee";
    protected static final String PRODUCTS_COLUMN_status = "status";
    protected static final String PRODUCTS_COLUMN_with_pos = "with_pos";
    protected static final String PRODUCTS_COLUMN_with_point_system = "with_point_system";


    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    public static final String DATABASE_CREATE="CREATE TABLE products ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "`name` TEXT NOT NULL, `barcode` TEXT UNIQUE , `description` TEXT,"+
            "`price` REAL NOT NULL, `costPrice` REAL, `withTax` INTEGER NOT NULL DEFAULT 1, "+
            "`weighable` INTEGER NOT NULL DEFAULT 0, `creatingDate` TIMESTAMP NOT NULL DEFAULT current_timestamp, "+
            "`hide` INTEGER DEFAULT 0, `categoryId` INTEGER NOT NULL, `byEmployee` INTEGER NOT NULL, `status` INTEGER NOT NULL DEFAULT 0 ,  `with_pos` INTEGER NOT NULL DEFAULT 1, `with_point_system` INTEGER NOT NULL DEFAULT 1,"+
            "FOREIGN KEY(`categoryId`) REFERENCES `Category.id`, FOREIGN KEY(`byEmployee`) REFERENCES `employees.id` )";
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

    public long insertEntry(String name,String barCode,String description,double price,double costPrice,
                           boolean withTax,boolean weighable,long categoryId,long byUser ,int pos,int point_system) {
        Product p = new Product(Util.idHealth(this.db, PRODUCTS_TABLE_NAME, PRODUCTS_COLUMN_ID), name, barCode, description, price, costPrice, withTax, weighable, new Timestamp(System.currentTimeMillis()), categoryId, byUser, pos, point_system);

        long id = insertEntry(p);
        if (id > 0) {
            Product boProduct = p;
            boProduct.setName(Util.getString(boProduct.getName()));
            boProduct.setDescription(Util.getString(boProduct.getDescription()));
            boProduct.setBarCode(Util.getString(boProduct.getBarCode()));
            sendToBroker(MessageType.ADD_PRODUCT, boProduct, this.context);
        }
        return id;
    }

    public long insertEntry(Product p) {
        Product product = getProductByBarCode(p.getBarCode());
        if (product != null) {
            Log.i("Inserting product", "barcode is exist");
            return product.getProductId();
        }
        Product productCheckName = null;
        productCheckName = getByProductName(p.getName());
        if (productCheckName != null) {
            Log.i("Inserting product", "name is busy");
            return productCheckName.getProductId();
        }

        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCTS_COLUMN_ID, p.getProductId());
        val.put(PRODUCTS_COLUMN_NAME, p.getName());
        val.put(PRODUCTS_COLUMN_BARCODE, p.getBarCode());
        val.put(PRODUCTS_COLUMN_DESCRIPTION, p.getDescription());
        val.put(PRODUCTS_COLUMN_PRICE, p.getPrice());
        val.put(PRODUCTS_COLUMN_COSTPRICE, p.getCostPrice());
        val.put(PRODUCTS_COLUMN_WITHTAX, p.isWithTax());
        val.put(PRODUCTS_COLUMN_WEIGHABLE, p.isWeighable());
        val.put(PRODUCTS_COLUMN_CATEGORYID, p.getCategoryId());
        val.put(PRODUCTS_COLUMN_BYUSER, p.getByUser());
        val.put(PRODUCTS_COLUMN_with_pos,p.getWithPos());
        val.put(PRODUCTS_COLUMN_with_point_system,p.getWithPointSystem());
        try {
            return db.insert(PRODUCTS_TABLE_NAME, null, val);
        } catch (SQLException ex) {
            Log.e("ProductDB insertEntry", "inserting Entry at " + PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return -1;
        }
    }

    public double getProductPrice(long id){
        Cursor cursor = db.rawQuery("select * from " + PRODUCTS_TABLE_NAME + " where id='" + id + "'", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return -1.0;
        }
        cursor.moveToFirst();
        return Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_PRICE)));
    }


    public Product getProductByID(long id) {
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
        product = makeProduct(cursor);
        cursor.close();

        return product;
    }

    public int getProductsCount() {
        String countQuery = "SELECT  * FROM " + PRODUCTS_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
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
        product = makeProduct(cursor);
        cursor.close();

        return product;
    }

    public int deleteEntry(long id) {
        ProductDBAdapter productDBAdapter=new ProductDBAdapter(context);
        productDBAdapter.open();
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(PRODUCTS_COLUMN_DISENABLED, 1);

        String where = PRODUCTS_COLUMN_ID + " = ?";
        try {
            db.update(PRODUCTS_TABLE_NAME, updatedValues, where, new String[]{id + ""});
            Product product=productDBAdapter.getProductByID(id);
            sendToBroker(MessageType.DELETE_PRODUCT, product, this.context);
            return 1;
        } catch (SQLException ex) {
            Log.e("Product deleteEntry", "enable hide Entry at " + PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }
    public long deleteEntryBo(Product product) {
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(PRODUCTS_COLUMN_DISENABLED, 1);

        String where = PRODUCTS_COLUMN_ID + " = ?";
        try {
            db.update(PRODUCTS_TABLE_NAME, updatedValues, where, new String[]{product.getProductId() + ""});
            return 1;
        } catch (SQLException ex) {
            Log.e("Product deleteEntry", "enable hide Entry at " + PRODUCTS_TABLE_NAME + ": " + ex.getMessage());
            return 0;
        }
    }

    public void updateEntry(Product product) {
        ProductDBAdapter productDBAdapter = new ProductDBAdapter(context);
        productDBAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCTS_COLUMN_NAME, product.getName());
        val.put(PRODUCTS_COLUMN_BARCODE, product.getBarCode());
        val.put(PRODUCTS_COLUMN_DESCRIPTION, product.getDescription());
        val.put(PRODUCTS_COLUMN_PRICE, product.getPrice());
        val.put(PRODUCTS_COLUMN_COSTPRICE, product.getCostPrice());
        val.put(PRODUCTS_COLUMN_WITHTAX, product.isWithTax());
        val.put(PRODUCTS_COLUMN_WEIGHABLE, product.isWeighable());
        val.put(PRODUCTS_COLUMN_CATEGORYID, product.getCategoryId());
        val.put(PRODUCTS_COLUMN_BYUSER, product.getByUser());

        String where = PRODUCTS_COLUMN_ID + " = ?";
        db.update(PRODUCTS_TABLE_NAME, val, where, new String[]{product.getProductId() + ""});
        Product p=productDBAdapter.getProductByID(product.getProductId());
        Log.d("Update Object",p.toString());
        sendToBroker(MessageType.UPDATE_PRODUCT, p, this.context);
        productDBAdapter.close();
    }
    public long updateEntryBo(Product product) {
        ProductDBAdapter productDBAdapter = new ProductDBAdapter(context);
        productDBAdapter.open();
        ContentValues val = new ContentValues();
        //Assign values for each row.
        val.put(PRODUCTS_COLUMN_NAME, product.getName());
        val.put(PRODUCTS_COLUMN_BARCODE, product.getBarCode());
        val.put(PRODUCTS_COLUMN_DESCRIPTION, product.getDescription());
        val.put(PRODUCTS_COLUMN_PRICE, product.getPrice());
        val.put(PRODUCTS_COLUMN_COSTPRICE, product.getCostPrice());
        val.put(PRODUCTS_COLUMN_WITHTAX, product.isWithTax());
        val.put(PRODUCTS_COLUMN_WEIGHABLE, product.isWeighable());
        val.put(PRODUCTS_COLUMN_CATEGORYID, product.getCategoryId());
        val.put(PRODUCTS_COLUMN_BYUSER, product.getByUser());

        try {
            String where = PRODUCTS_COLUMN_ID + " = ?";
            db.update(PRODUCTS_TABLE_NAME, val, where, new String[]{product.getProductId() + ""});
            Product p=productDBAdapter.getProductByID(product.getProductId());
            Log.d("Update Object",p.toString());
            productDBAdapter.close();
            return 1;
        } catch (SQLException ex) {
            return 0;
        }
    }

    public List<Product> getAllProducts(){
        List<Product> productsList =new ArrayList<Product>();

        Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME +" where "+PRODUCTS_COLUMN_DISENABLED+"=0 order by id desc", null );
        cursor.moveToFirst();


        while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
        }

        return productsList;
    }

	public List<Product> getAllProductsByCategory(long categoryId){
		List<Product> productsList =new ArrayList<Product>();

		Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME+" where "+ PRODUCTS_COLUMN_CATEGORYID +"="+categoryId+" and "+PRODUCTS_COLUMN_DISENABLED+"=0 order by id desc", null );
		cursor.moveToFirst();

		while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
		}

		return productsList;
	}

    public List<Product> getAllProductsByCategory(long categoryId, int from , int count){
        List<Product> productsList =new ArrayList<Product>();

        Cursor cursor =  db.rawQuery( "select * from "+PRODUCTS_TABLE_NAME+" where "+ PRODUCTS_COLUMN_CATEGORYID +"="+categoryId+" and "+PRODUCTS_COLUMN_DISENABLED+"=0 order by id desc limit "+from+","+count, null );
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
        int withTaxValue = cursor.getInt(cursor.getColumnIndex(PRODUCTS_COLUMN_WITHTAX));
        int weighableValue = cursor.getInt(cursor.getColumnIndex(PRODUCTS_COLUMN_WEIGHABLE));

        boolean withTaxStatus ,weighableStatus =false ;
        if(withTaxValue==1){
            withTaxStatus=true;
        }else {
            withTaxStatus=false;
        }
        if(weighableValue==1){
            weighableStatus=true;
        }else {
            weighableStatus=false;
        }

        Product p=new Product(
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_ID))),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BARCODE)),
                cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DESCRIPTION)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_PRICE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_COSTPRICE))),
                withTaxStatus, weighableStatus, Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_CREATINGDATE))),
                Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_DISENABLED))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_CATEGORYID))),
                Long.parseLong(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_BYUSER))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_with_pos))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRODUCTS_COLUMN_with_point_system))));
        if(p.getDescription()==null){
            p.setDescription("");
        }
        if(Double.isNaN(p.getCostPrice())){
            p.setCostPrice(0.0f);
        }
        return p;
    }
    public boolean availableProductName(String productName) {
        Cursor cursor = db.query(PRODUCTS_TABLE_NAME, null, PRODUCTS_COLUMN_NAME + "=?", new String[]{productName}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            //Product Name not available
            return false;
        }
        // Product Name available
        return true;
    }
    public Product getByProductName(String productName) {
        Cursor cursor = db.query(PRODUCTS_TABLE_NAME, null, PRODUCTS_COLUMN_NAME + "=?", new String[]{productName}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            //Product Name not available
            return makeProduct(cursor);
        }
        // Product Name available
        return null;
    }

    public List<Product> getAllProductsByHint(String hint , int from , int count ){
        List<Product> productsList =new ArrayList<Product>();

        Cursor cursor =  db.rawQuery("select * from " + PRODUCTS_TABLE_NAME +" where "+ PRODUCTS_COLUMN_BARCODE +" like '%"+
                hint+"%' OR " + PRODUCTS_COLUMN_DESCRIPTION+" like '%"+ hint +"%' OR "+PRODUCTS_COLUMN_NAME+" like '%"+ hint+"%'" +" and "+PRODUCTS_COLUMN_DISENABLED+"=0 order by id desc limit "+from+","+count, null );

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            productsList.add(makeProduct(cursor));
            cursor.moveToNext();
        }

        return productsList;
    }
    // methode to test if barcode is UNIQUE
    public Boolean isValidBarcode(String barcode) {
        Cursor cursor = db.query(PRODUCTS_TABLE_NAME, null, PRODUCTS_COLUMN_BARCODE + "=?", new String[]{barcode}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return false;
        }
        return true;
    }
}
