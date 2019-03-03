package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferCategoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Tools.CategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class CategoryOfferActivity extends AppCompatActivity {
    EditText categoryOfferName;
    Button saveCategory , cancelAddCategoryOffer , addCategoryProduct , addProduct;
    List<Category>filter_categoryList = new ArrayList<>();
     Category category =new Category();
    List<Category>categoryList = new ArrayList<>();
    List<Product>filter_productList = new ArrayList<>();
    Product product =new Product();
    List<Product>productList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category_offer);
        TitleBar.setTitleBar(this);
        categoryOfferName = (EditText)findViewById(R.id.etCategoryOfferName);
        saveCategory = (Button)findViewById(R.id.addNewCategoryOffer);
        cancelAddCategoryOffer = (Button)findViewById(R.id.cancelAddCategoryOffer);
        addCategoryProduct = (Button)findViewById(R.id.addCategoryProduct);
        addProduct=(Button)findViewById(R.id.addProduct);
        addCategoryProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //addProductFromCategory
                addProductFromCategory();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductFromProductDialog();
            }
        });

        saveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelAddCategoryOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void addProductFromCategory() {
        final Dialog addProductFromCategoryDialog = new Dialog(CategoryOfferActivity.this);
        addProductFromCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addProductFromCategoryDialog.show();
        addProductFromCategoryDialog.setContentView(R.layout.add_category_offer_product_from_category);
       final EditText categoryName = (EditText) addProductFromCategoryDialog.findViewById(R.id.categoryName);
        final GridView gvCategory = (GridView) addProductFromCategoryDialog.findViewById(R.id.gvCategory);
        gvCategory.setNumColumns(3);
       Button btn_cancel = (Button) addProductFromCategoryDialog.findViewById(R.id.btn_cancel);
        Button btn_add = (Button) addProductFromCategoryDialog.findViewById(R.id.btn_add);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductFromCategoryDialog.dismiss();
            }
        });
        CategoryDBAdapter  categoryDBAdapter = new CategoryDBAdapter(this);
        categoryDBAdapter.open();

        filter_categoryList = categoryDBAdapter.getAllDepartments();
        CategoryGridViewAdapter adapter = new CategoryGridViewAdapter(this, filter_categoryList);
        gvCategory.setAdapter(adapter);
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                category=filter_categoryList.get(position);
                Log.d("testCategory",category.toString());
                categoryList.add(category);
                for (int i = 0; i < gvCategory.getChildCount(); i++) {
                    if(position == i ){
                        gvCategory.getChildAt(i).setBackgroundColor(Color.BLACK);
                    }
                }
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(categoryName.getText().toString().equals("")){
                    Toast.makeText(CategoryOfferActivity.this,"please insert category offer name",Toast.LENGTH_LONG).show();
                }else {
                addProductFromCategoryDialog.dismiss();
                Log.d("testCategory",categoryList.toString());
                ArrayList<String>productIdList=new ArrayList<String>();
                ArrayList<Product>products=new ArrayList<Product>();

                for (int i=0;i<categoryList.size();i++){
                    ProductDBAdapter productDBAdapter = new ProductDBAdapter(CategoryOfferActivity.this);
                    productDBAdapter.open();
                    products.addAll(productDBAdapter.getAllProductsByCategory(categoryList.get(i).getCategoryId()));
                }
                for(int p=0;p<products.size();p++){
                    productIdList.add(String.valueOf(products.get(p).getProductId()));
                }
                OfferCategoryDbAdapter offerCategoryDbAdapter = new OfferCategoryDbAdapter(CategoryOfferActivity.this);
                offerCategoryDbAdapter.open();
                offerCategoryDbAdapter.insertEntry(categoryName.getText().toString(), SESSION._EMPLOYEE.getEmployeeId(),productIdList, SETTINGS.branchId);
                }

            }
        });

    }
    private void addProductFromProductDialog() {
        final Dialog addProductFromProductDialog = new Dialog(CategoryOfferActivity.this);
        addProductFromProductDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addProductFromProductDialog.show();
        addProductFromProductDialog.setContentView(R.layout.add_category_offer_product_from_product_catalog);
        final EditText categoryName = (EditText) addProductFromProductDialog.findViewById(R.id.categoryName);
        final GridView gvProduct = (GridView) addProductFromProductDialog.findViewById(R.id.gvProduct);
        gvProduct.setNumColumns(3);
        Button btn_cancel_add_product = (Button) addProductFromProductDialog.findViewById(R.id.btn_cancel_add_product);
        Button btn_add_product = (Button) addProductFromProductDialog.findViewById(R.id.btn_add_product);

        btn_cancel_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductFromProductDialog.dismiss();
            }
        });
        ProductDBAdapter  productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();

        filter_productList = productDBAdapter.getAllProducts();
        ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(this, filter_productList);
        gvProduct.setAdapter(adapter);
        gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                product=filter_productList.get(position);
                Log.d("testProduct",product.toString());
                productList.add(product);
                for (int i = 0; i < gvProduct.getChildCount(); i++) {
                    if(position == i ){
                        gvProduct.getChildAt(i).setBackgroundColor(Color.BLACK);
                    }
                }
            }
        });
        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(categoryName.getText().toString().equals("")){
                    Toast.makeText(CategoryOfferActivity.this,"please insert category offer name",Toast.LENGTH_LONG).show();
                }else {
                    addProductFromProductDialog.dismiss();
                    Log.d("testProductList",productList.toString());
                    ArrayList<String>productIdList=new ArrayList<String>();
                    ArrayList<Product>products=new ArrayList<Product>();

                    for (int i=0;i<productList.size();i++){
                        productIdList.add(String.valueOf(productList.get(i).getProductId()));

                    }

                    OfferCategoryDbAdapter offerCategoryDbAdapter = new OfferCategoryDbAdapter(CategoryOfferActivity.this);
                    offerCategoryDbAdapter.open();
                    offerCategoryDbAdapter.insertEntry(categoryName.getText().toString(), SESSION._EMPLOYEE.getEmployeeId(),productIdList, SETTINGS.branchId);
                }

            }
        });

    }

}
