package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.CreditCard.CreditCardActivity;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by KARAM on 20/10/2016.
 */

public class ProductCatalogActivity extends AppCompatActivity {

    private Button btCreate, btImport;
    EditText etSearch;
    private GridView gvProducts;
    ProductDBAdapter productDBAdapter;
    DepartmentDBAdapter departmentDBAdapter;
    List<Product> productsList;
    List<Product> filter_productsList;
    View previouslySelectedItem = null;
    String selectedProduct = "";
    LinearLayout llDepartments;
    Product p;
    View prseedButtonDepartments;
    Button btAll;
    boolean userScrolled=false;
    int productLoadItemOffset=0;
    int productCountLoad=80;
    int departmentID=-1;
    ProductCatalogGridViewAdapter adapter;

    public void CancelClickButton(View v){
        onBackPressed();
    }


    @Override
    protected void onResume() {
        try{
            productLoadItemOffset=0;
        departmentDBAdapter.open();
        productDBAdapter.open();}
        catch (Exception ex){}
        prseedButtonDepartments.setPressed(false);
        btAll.setPressed(true);

        prseedButtonDepartments = btAll;

        filter_productsList = productDBAdapter.getTopProducts(productLoadItemOffset,productCountLoad);
        productsList = filter_productsList;
        adapter = new ProductCatalogGridViewAdapter(getBaseContext(), productsList);
        gvProducts.setAdapter(adapter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        try{
            departmentDBAdapter.close();
            productDBAdapter.close();}
        catch (Exception ex){}
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_product_catalog);

        btCreate = (Button) findViewById(R.id.productCatalog_BTCreateNewProduct);
        btImport = (Button) findViewById(R.id.productCatalog_BTImport);
        gvProducts = (GridView) findViewById(R.id.productCatalog_GVProducts);
        etSearch = (EditText) findViewById(R.id.productCatalog_ETSearch);
        llDepartments=(LinearLayout)findViewById(R.id.productCatalog_LLDepartment);




        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter_productsList = new ArrayList<Product>();
                String word = etSearch.getText().toString();
                if (!word.equals("")) {
                    for (Product p : productsList) {
                        if (p.getName().toLowerCase().contains(word.toLowerCase()) || p.getDescription().toLowerCase().contains(word.toLowerCase())) {
                            filter_productsList.add(p);
                        }
                    }
                } else {
                    filter_productsList = productsList;
                }
                ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(getApplicationContext(), filter_productsList);
                gvProducts.setAdapter(adapter);
            }
        });

        productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();

        productsList = productDBAdapter.getTopProducts(productLoadItemOffset,productCountLoad);
        filter_productsList = productsList;
        adapter = new ProductCatalogGridViewAdapter(this, productsList);

        gvProducts.setAdapter(adapter);
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductCatalogActivity.this,ProductsActivity.class);
                startActivity(intent);
            }
        });

        btImport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i=new Intent(ProductCatalogActivity.this,ImportProductsActivity.class);
                startActivity(i);

                // 1. on Upload click call ACTION_GET_CONTENT intent
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                // 2. pick image only

                //intent.setType("file/*");
              //  intent.addCategory(Intent.CATEGORY_OPENABLE);
                // 3. start activity
               // startActivityForResult(intent, CONSTANT.COM_POS_LEADERS_LEADPOS_CHOSEFILE);



                //// TODO: 15/12/2016 Import Product Dialog

                /*
                * new AlertDialog.Builder(ProductsActivity.this)
							.setTitle("Clear Cart")
							.setMessage("Are you sure to delete this item?")
							.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									int check;
									check = productDBAdapter.deleteEntry(editableProduct.getId());
									if (check == 1) {
										Log.i("entry deleted", editableProduct.toString());
										productsList.remove(editableProduct);
										editableProduct = null;
										previouslySelectedProductItem=null;
										gvProduct.setAdapter(adapter);
										Toast.makeText(getApplicationContext(), "item successfuly deleted", Toast.LENGTH_SHORT);
									} else {
										Toast.makeText(getApplicationContext(), "can`t delete this entry", Toast.LENGTH_SHORT);
									}
								}
							})
							.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// do nothing
								}
							})
							.setIcon(android.R.drawable.ic_dialog_alert)
							.show();
                * */
                //Intent i = new Intent(ProductCatalogActivity.this, OfferActivity.class);
                //startActivity(i);
            }
        });

        p = null;
        gvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String[] items = {
                        getString(R.string.edit),
                        getString(R.string.delete)};
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductCatalogActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                intent = new Intent(ProductCatalogActivity.this, ProductsActivity.class);
                                intent.putExtra("productID", filter_productsList.get(position).getId());
                                startActivity(intent);
                                break;
                            case 1:
                                //productDBAdapter.deleteEntry(filter_productsList.get(position).getId());
                                //// TODO: 15/12/2016 Delete Product 
                                break;
                             
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();



            }
        });

        gvProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userScrolled && firstVisibleItem + visibleItemCount == totalItemCount) {

                    userScrolled = false;
                    LoadMoreProducts();
                }
            }
        });

        //region Departments



        departmentDBAdapter=new DepartmentDBAdapter(this);
        departmentDBAdapter.open();
        for (Department d : departmentDBAdapter.getAllDepartments()) {
            Button bt = new Button(this);
            //bt.setId(d.getId());
            bt.setText(d.getName());
            bt.setTextAppearance(this, R.style.TextAppearance);
            bt.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productLoadItemOffset=0;
                    prseedButtonDepartments.setPressed(false);
                    v.setPressed(true);
                    final int departmentID=v.getId();
                    prseedButtonDepartments = v;

                    Log.i("starting time",new Date().toString());
                    productsList = productDBAdapter.getAllProductsByDepartment(departmentID,productLoadItemOffset,productCountLoad);
                    Log.i("Ending time",new Date().toString());
                    filter_productsList = productsList;

                    adapter = new ProductCatalogGridViewAdapter(getBaseContext(), productsList);
                    gvProducts.setAdapter(adapter);

                }
            });

            llDepartments.addView(bt);
        }

        btAll = new Button(this);
        btAll.setId(0);
        btAll.setText(getResources().getText(R.string.all));
        btAll.setTextAppearance(this, R.style.TextAppearance);
        btAll.setPressed(true);
        btAll.setBackground(getResources().getDrawable(R.drawable.btn_secondary));
        llDepartments.addView(btAll);
        prseedButtonDepartments = btAll;

        btAll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                productLoadItemOffset=0;
                prseedButtonDepartments.setPressed(false);
                v.setPressed(true);

                prseedButtonDepartments = v;

                filter_productsList = productDBAdapter.getTopProducts(productLoadItemOffset,productCountLoad);
                productsList = filter_productsList;
                adapter = new ProductCatalogGridViewAdapter(getBaseContext(), productsList);
                gvProducts.setAdapter(adapter);
                return true;
            }
        });

        //endregion

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    protected void LoadMoreProducts(){
        productLoadItemOffset+=productCountLoad;
        final int id=prseedButtonDepartments.getId();
        final ProgressDialog dialog=new ProgressDialog(ProductCatalogActivity.this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                filter_productsList=productsList;
                adapter.notifyDataSetChanged();
                dialog.cancel();
            }

            @Override
            protected Void doInBackground(Void... params) {
                if(id==0){
                    productsList.addAll(productDBAdapter.getTopProducts(productLoadItemOffset,productCountLoad));
                }
                else{
                    productsList.addAll(productDBAdapter.getAllProductsByDepartment(id,productLoadItemOffset,productCountLoad));
                }
                return null;
            }
        }.execute();



    }








}
