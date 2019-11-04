package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Reports.NumberOfSaleProductReport;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KARAM on 20/10/2016.
 */

public class ProductCatalogActivity extends AppCompatActivity {
    private Button btCreate, btImport;
    EditText etSearch;
    TextView tvCount;
    private GridView gvProducts;
    ProductDBAdapter productDBAdapter;
    CategoryDBAdapter departmentDBAdapter;
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
    public static int Product_Management_View ;
    public  static int  Product_Management_Edit;
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

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_catalog);

        TitleBar.setTitleBar(this);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        btCreate = (Button) findViewById(R.id.productCatalog_BTCreateNewProduct);
        btImport = (Button) findViewById(R.id.productCatalog_BTImport);
        gvProducts = (GridView) findViewById(R.id.productCatalog_GVProducts);
        etSearch = (EditText) findViewById(R.id.productCatalog_ETSearch);
        llDepartments=(LinearLayout)findViewById(R.id.productCatalog_LLDepartment);
        tvCount = (TextView) findViewById(R.id.productCatalog_etProductCount);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //layoutParams.setMargins(0, 10, 10, 40);
        layoutParams.setMargins(0, 10, 10, 0);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String word = etSearch.getText().toString();
                if (!word.equals("")) {
                    // Database query can be a time consuming task ..
                    // so its safe to call database query in another thread
                    // Handler, will handle this stuff
                    new AsyncTask<String, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            filter_productsList = new ArrayList<Product>();
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            filter_productsList = productDBAdapter.getAllProductsByHint(params[0], productLoadItemOffset, productCountLoad);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(getApplicationContext(), filter_productsList);
                            gvProducts.setAdapter(adapter);
                        }
                    }.execute(word);
                } else {
                    filter_productsList = productsList;
                    ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(getApplicationContext(), filter_productsList);
                    gvProducts.setAdapter(adapter);
                }

            }
        });

        productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();

        tvCount.setText(getString(R.string.product_count) + productDBAdapter.getProductsCount());

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
									check = productDBAdapter.deleteEntry(editableProduct.getCashPaymentId());
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
                Product_Management_Edit=0;
                Product_Management_View=0;
                final String[] items = {
                        getString(R.string.edit_product),
                        getString(R.string.delete),
                        getString(R.string.view) ,
                        getString(R.string.report)
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductCatalogActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                Product_Management_Edit=8;
                                intent = new Intent(ProductCatalogActivity.this, ProductsActivity.class);
                                intent.putExtra("productID", filter_productsList.get(position).getProductId());
                                startActivity(intent);
                                break;
                            case 1:
                                  new AlertDialog.Builder(ProductCatalogActivity.this)
                                    .setTitle(getString(R.string.delete)+" "+getString(R.string.product))
                                    .setMessage(getString(R.string.delete))
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            productDBAdapter.deleteEntry(filter_productsList.get(position).getProductId());
                                            productsList.remove(filter_productsList.get(position));
                                            gvProducts.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                                break;
                            case 2:
                                Product_Management_View=9;
                                intent = new Intent(ProductCatalogActivity.this, ProductsActivity.class);
                                intent.putExtra("productID", filter_productsList.get(position).getProductId());
                                startActivity(intent);
                                break;


                            case 3:
                                intent = new Intent(ProductCatalogActivity.this, NumberOfSaleProductReport.class);
                                intent.putExtra("productID", filter_productsList.get(position).getProductId());
                                startActivity(intent);
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        params.setMargins(2,6, 6, 2);

        departmentDBAdapter=new CategoryDBAdapter(this);
        departmentDBAdapter.open();
        for (Category d : departmentDBAdapter.getAllDepartments()) {
            Button bt = new Button(this);
            bt.setLayoutParams(params);
            bt.setTag(d);

            //bt.setId(d.getCashPaymentId());
            bt.setText(d.getName());
            bt.setTextAppearance(this, R.style.TextAppearance);
            bt.setBackground(getResources().getDrawable(R.drawable.bt_normal));



            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productLoadItemOffset=0;
                    prseedButtonDepartments.setPressed(false);
                    prseedButtonDepartments.setBackground(getResources().getDrawable(R.drawable.bt_normal));
                    v.setPressed(true);
                    v.setBackground(getResources().getDrawable(R.drawable.bt_normal_pressed));
                    final long departmentID = ((Category) v.getTag()).getCategoryId();
                    prseedButtonDepartments = v;
                    productsList = productDBAdapter.getAllProductsByCategory(departmentID, productLoadItemOffset, productCountLoad);
                    filter_productsList = productsList;
                    adapter = new ProductCatalogGridViewAdapter(getBaseContext(), productsList);
                    gvProducts.setAdapter(adapter);

                }
            });

          //  llDepartments.addView(bt);
            llDepartments.addView(bt, layoutParams);
        }

        btAll = new Button(this);
        btAll.setId(0);
        btAll.setText(getResources().getText(R.string.all));
        btAll.setTextAppearance(this, R.style.TextAppearance);
        btAll.setPressed(true);
        btAll.setBackground(getResources().getDrawable(R.drawable.bt_normal));
        llDepartments.addView(btAll,layoutParams);
        prseedButtonDepartments = btAll;
        btAll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                productLoadItemOffset=0;
                prseedButtonDepartments.setPressed(false);
                prseedButtonDepartments.setBackground(getResources().getDrawable(R.drawable.bt_normal));
                v.setPressed(true);
                v.setBackground(getResources().getDrawable(R.drawable.bt_normal_pressed));

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
                    productsList.addAll(productDBAdapter.getAllProductsByCategory(id,productLoadItemOffset,productCountLoad));
                }
                return null;
            }
        }.execute();



    }








}
