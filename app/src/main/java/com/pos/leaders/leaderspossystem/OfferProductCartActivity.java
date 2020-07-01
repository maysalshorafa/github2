package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 25/12/2016.
 */

public class OfferProductCartActivity extends Activity {

    private Button btDone, btDepartmentProduct,btRest;
    private TextView tvCount;
    EditText etSearch;
    private GridView gvProducts;
    ListView lvProducts;
    ProductDBAdapter productDBAdapter;
    CategoryDBAdapter departmentDBAdapter;
    List<Product> productsList;
    List<Product> filter_productsList;
    List<Product> selectedProductsList;
    View previouslySelectedItem = null;
    String selectedProduct = "";
    LinearLayout llDepartments;
    Product p;
    View prseedButtonDepartments;
    Button btAll;
    boolean userScrolled=false;
    int productLoadItemOffset=0;
    int productCountLoad=50;
    int departmentID=-1;
    ProductCatalogGridViewAdapter adapter,productSelectedAdapter;

    public void CancelClickButton(View v){
        Log.i("Sess",SESSION._TEMPOFFERPRODUCTS.toString());
        Log.i("CXXSess",selectedProductsList.toString());
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_offer_products_catalog);

        //region popUp

        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        //wlp.gravity = Gravity.CENTER_VERTICAL;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        wlp.dimAmount = (float) 0.6;
        window.setAttributes(wlp);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.9));

        //endregion


        btDone = (Button) findViewById(R.id.offerProductsCatalog_BTDone);
        btDepartmentProduct = (Button) findViewById(R.id.offerProductsCatalog_BTDepartmentProduct);
        btRest = (Button) findViewById(R.id.offerProductsCatalog_BTRest);
        tvCount = (TextView) findViewById(R.id.offerProductsCatalog_TVCount);
        gvProducts = (GridView) findViewById(R.id.offerProductsCatalog_GVProducts);
        lvProducts = (ListView) findViewById(R.id.offerProductsCatalog_LVProducts);
        etSearch = (EditText) findViewById(R.id.offerProductsCatalog_ETSearch);
        llDepartments=(LinearLayout)findViewById(R.id.offerProductsCatalog_LLDepartment);
        selectedProductsList=new ArrayList<Product>();
        if(SESSION._TEMPOFFERPRODUCTS!=null&&SESSION._TEMPOFFERPRODUCTS.size()>0)
            selectedProductsList.addAll(SESSION._TEMPOFFERPRODUCTS);

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
                        if (p.getDisplayName().toLowerCase().contains(word.toLowerCase()) || p.getDescription().toLowerCase().contains(word.toLowerCase())) {
                            filter_productsList.add(p);
                        }
                    }
                } else {
                    filter_productsList = productsList;
                }
                ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(getApplicationContext(), filter_productsList);
                gvProducts.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();
        productsList = productDBAdapter.getTopProducts(productLoadItemOffset,productCountLoad);
        productDBAdapter.close();
        filter_productsList = productsList;
        adapter = new ProductCatalogGridViewAdapter(this, productsList);
        productSelectedAdapter=new ProductCatalogGridViewAdapter(this, selectedProductsList);

        lvProducts.setAdapter(productSelectedAdapter);
        gvProducts.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        productSelectedAdapter.notifyDataSetChanged();

        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SESSION._TEMPOFFERPRODUCTS=new ArrayList<Product>();
                SESSION._TEMPOFFERPRODUCTS.addAll(selectedProductsList);
                finish();
            }
        });

        gvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               if(!ProductAtList(selectedProductsList,productsList.get(position))){
                   selectedProductsList.add(productsList.get(position));
                   refresh();
               }
            }
        });
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProductsList.remove(position);
                refresh();
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

        btRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedProductsList=new ArrayList<Product>();
                productSelectedAdapter=new ProductCatalogGridViewAdapter(OfferProductCartActivity.this,selectedProductsList);
                lvProducts.setAdapter(productSelectedAdapter);
                refresh();
            }
        });

        btDepartmentProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDBAdapter.open();
                //if all tap hs been selected
                if(prseedButtonDepartments.getId()<1)
                    return;//do nothing
                final ProgressDialog dialog=new ProgressDialog(OfferProductCartActivity.this);
                dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));
                final int depID=prseedButtonDepartments.getId();
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected void onPreExecute() {
                        dialog.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {

                        for (Product p : productDBAdapter.getAllProductsByCategory(depID)) {
                            if(!ProductAtList(selectedProductsList,p)){
                                selectedProductsList.add(p);
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        refresh();
                        dialog.cancel();
                    }
                }.execute();
                productDBAdapter.close();
            }
        });

        //region Departments



        departmentDBAdapter=new CategoryDBAdapter(this);
        departmentDBAdapter.open();
        for (Category d : departmentDBAdapter.getAllDepartments()) {
            Button bt = new Button(this);
            //bt.setId(d.getCashPaymentId());
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
                    productDBAdapter.open();
                    productsList = productDBAdapter.getAllProductsByCategory(departmentID,productLoadItemOffset,productCountLoad);
                    productDBAdapter.close();
                    Log.i("Ending time",new Date().toString());
                    filter_productsList = productsList;

                    adapter = new ProductCatalogGridViewAdapter(getBaseContext(), productsList);
                    gvProducts.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


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
                productDBAdapter.open();
                filter_productsList = productDBAdapter.getTopProducts(productLoadItemOffset,productCountLoad);
                productDBAdapter.close();
                productsList = filter_productsList;
                adapter = new ProductCatalogGridViewAdapter(getBaseContext(), productsList);
                gvProducts.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return true;
            }
        });


        //endregion

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        refresh();
    }

    protected void LoadMoreProducts(){
        productDBAdapter.open();
        productLoadItemOffset+=productCountLoad;
        final int id=prseedButtonDepartments.getId();
        final ProgressDialog dialog=new ProgressDialog(OfferProductCartActivity.this);
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

        productDBAdapter.close();

    }

    private void refresh(){
        productSelectedAdapter.notifyDataSetChanged();
        tvCount.setText(getString(R.string.selected)+" "+selectedProductsList.size()+" "+getString(R.string.products));
    }
    private boolean ProductAtList(List<Product> list,Product product){
        for (Product p : list) {
            if (p.getProductId() == product.getProductId())
                return true;
        }
        return false;
    }
}
