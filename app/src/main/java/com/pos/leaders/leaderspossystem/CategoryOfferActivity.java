package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferCategoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.OfferCategory;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Tools.CategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.FilterCategoryOfferProductDialog;
import com.pos.leaders.leaderspossystem.Tools.OfferCategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.OfferCategoryProductGridViewAdapter;
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
    List<Product>final_filter_productList = new ArrayList<>();
    List<Category>final_filter_categoryList = new ArrayList<>();
     EditText productName ;
    Product product =new Product();
    List<Product>productList = new ArrayList<>();
    List<String>productListName = new ArrayList<>();
    boolean flagProduct=false;

    Spinner SpProductBranch;
    GridView offerCategoryGridView;
    List<OfferCategory>offerCategoryList =new ArrayList<>();
    boolean editFlag = false;
    ArrayList<String>productIdListForEdit=new ArrayList<String>();
     GridView gvFilterProduct;
    FilterCategoryOfferProductDialog filterCategoryOfferProductDialog;
    Dialog addProductFromProductDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category_offer);
        TitleBar.setTitleBar(this);

        addProductFromProductDialog = new Dialog(CategoryOfferActivity.this);
        addProductFromProductDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        categoryOfferName = (EditText)findViewById(R.id.etCategoryOfferName);
        cancelAddCategoryOffer = (Button)findViewById(R.id.cancelAddCategoryOffer);
        addCategoryProduct = (Button)findViewById(R.id.addCategoryProduct);
        addProduct=(Button)findViewById(R.id.addProduct);
        SpProductBranch = (Spinner)findViewById(R.id.SpCategoryBranch);
        offerCategoryGridView =(GridView)findViewById(R.id.category_offer_list);
        final List<String> productBranch = new ArrayList<String>();
        productBranch.add(getString(R.string.all));
        productBranch.add(getString(R.string.pos_branch));
        final ArrayAdapter<String> dataAdapterBranch = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productBranch);
        dataAdapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpProductBranch.setAdapter(dataAdapterBranch);
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
                addProductFromProductDialog(false);
            }
        });


        cancelAddCategoryOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });
        final OfferCategoryDbAdapter offerCategoryDbAdapter = new OfferCategoryDbAdapter(CategoryOfferActivity.this);
        offerCategoryDbAdapter.open();
        offerCategoryList=offerCategoryDbAdapter.getAllCategoryOffer();
        Log.d("offerCategoryList",offerCategoryList.toString());
        final OfferCategoryGridViewAdapter offerCategoryGridViewAdapter = new OfferCategoryGridViewAdapter(getApplicationContext(),offerCategoryList);
        offerCategoryGridView.setAdapter(offerCategoryGridViewAdapter);
        offerCategoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final String[] items = {
                        getString(R.string.view),
                        getString(R.string.edit),
                        getString(R.string.delete)};
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryOfferActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                final Dialog viewCategoryOfferProduct = new Dialog(CategoryOfferActivity.this);
                                viewCategoryOfferProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                viewCategoryOfferProduct.show();
                                viewCategoryOfferProduct.setContentView(R.layout.category_offer_products);
                                final EditText categoryOfferName = (EditText) viewCategoryOfferProduct.findViewById(R.id.categoryOfferName);
                                final GridView gvCategory = (GridView) viewCategoryOfferProduct.findViewById(R.id.gvCategory);
                                gvCategory.setNumColumns(3);
                                Button btn_cancel = (Button) viewCategoryOfferProduct.findViewById(R.id.btn_cancel);
                                Button btn_add = (Button) viewCategoryOfferProduct.findViewById(R.id.btn_add);
                                categoryOfferName.setText(offerCategoryList.get(position).getName());
                                OfferCategoryProductGridViewAdapter offerCategoryProductGridViewAdapter = new OfferCategoryProductGridViewAdapter(CategoryOfferActivity.this,offerCategoryList.get(position).getProductsIdList());
                                gvCategory.setAdapter(offerCategoryProductGridViewAdapter);
                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        viewCategoryOfferProduct.dismiss();
                                    }
                                });
                                btn_add.setVisibility(View.GONE);
                                break;
                            case 1:
                                List<String> newCategoryOfferProductListEdit = new ArrayList<String>();
                                final Dialog editCategoryOfferProduct = new Dialog(CategoryOfferActivity.this);
                                editCategoryOfferProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                editCategoryOfferProduct.show();
                                editCategoryOfferProduct.setContentView(R.layout.category_offer_products);
                                final EditText editCategoryOfferName = (EditText) editCategoryOfferProduct.findViewById(R.id.categoryOfferName);
                                final GridView gvEditCategory = (GridView) editCategoryOfferProduct.findViewById(R.id.gvCategory);
                                gvEditCategory.setNumColumns(3);
                                Button btn_cancel_edit = (Button) editCategoryOfferProduct.findViewById(R.id.btn_cancel);
                                Button btn_edit = (Button) editCategoryOfferProduct.findViewById(R.id.btn_add);
                                editCategoryOfferName.setText(offerCategoryList.get(position).getName());
                                TextView addProductToCategoryOffer = (TextView)editCategoryOfferProduct.findViewById(R.id.addProductToCategoryOffer);
                                final OfferCategoryProductGridViewAdapter offerCategoryProductGridViewAdapterEdit = new OfferCategoryProductGridViewAdapter(CategoryOfferActivity.this,offerCategoryList.get(position).getProductsIdList());
                                gvEditCategory.setAdapter(offerCategoryProductGridViewAdapterEdit);
                                final List<String> list = new ArrayList<String>(offerCategoryList.get(position).getProductsIdList());

                                gvEditCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                                        String s =list.get(position);
                                        for (int i = 0; i <list.size(); i++) {
                                            if(s.equals(list.get(i))){
                                              list.remove(i);

                                            }
                                        }

                                        final OfferCategoryProductGridViewAdapter offerCategoryProductGridViewAdapterEdit1 = new OfferCategoryProductGridViewAdapter(CategoryOfferActivity.this,list);
                                        gvEditCategory.setAdapter(offerCategoryProductGridViewAdapterEdit1);

                                    }
                                });
                                addProductToCategoryOffer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                     addProductFromProductDialog(true);
                                    }
                                });
                                btn_cancel_edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editCategoryOfferProduct.dismiss();
                                    }
                                });
                                btn_edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String categoryName = editCategoryOfferName.getText().toString();
                                        if(categoryName.equals("")){
                                            Toast.makeText(CategoryOfferActivity.this,"Please insert offer category name",Toast.LENGTH_LONG).show();
                                        }else {
                                        OfferCategory offerCategory = offerCategoryList.get(position);
                                        offerCategory.setName(categoryName);
                                            List<String>test=new ArrayList<String>();
                                            List<Long>test1=new ArrayList<Long>();
                                            list.addAll(productIdListForEdit);

                                            for (int a=0;a<list.size();a++){
                                                test1.add(Long.parseLong(list.get(a).trim()));
                                            }

                                            for (int i=0;i<test1.size();i++){
                                                test.add(String.valueOf(test1.get(i)).replaceAll("\\s+",""));

                                            }
                                            Log.d("lissst",test.toString());

                                            offerCategory.setProductsIdList(test);
                                            offerCategoryDbAdapter.updateEntry(offerCategory);
                                        editCategoryOfferProduct.dismiss();
                                    }
                                    }
                                });

                                break;
                            case 2:
                                new AlertDialog.Builder(CategoryOfferActivity.this)
                                        .setMessage(getString(R.string.delete))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                offerCategoryDbAdapter.deleteEntry(offerCategoryList.get(position).getOfferCategoryId());
                                                offerCategoryList.remove(offerCategoryList.get(position));
                                                offerCategoryGridView.setAdapter(offerCategoryGridViewAdapter);
                                                offerCategoryGridViewAdapter.notifyDataSetChanged();
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
                    }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    @Override
    protected void onDestroy(){
        if (addProductFromProductDialog!= null && addProductFromProductDialog.isShowing()){
            addProductFromProductDialog.dismiss();
            addProductFromProductDialog=null;
        }
        super.onDestroy();
    }
    private void addProductFromCategory() {
        final Dialog addProductFromCategoryDialog = new Dialog(CategoryOfferActivity.this);
        addProductFromCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addProductFromCategoryDialog.show();
        addProductFromCategoryDialog.setContentView(R.layout.add_category_offer_product_from_category);
       final EditText categoryOfferName = (EditText) addProductFromCategoryDialog.findViewById(R.id.categoryOfferName);
        final EditText categoryNameSearch = (EditText) addProductFromCategoryDialog.findViewById(R.id.categoryNameSearch);

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
        final CategoryDBAdapter  categoryDBAdapter = new CategoryDBAdapter(this);
        categoryDBAdapter.open();

        filter_categoryList = categoryDBAdapter.getAllDepartments();
        CategoryGridViewAdapter adapter = new CategoryGridViewAdapter(this, filter_categoryList);
        gvCategory.setAdapter(adapter);
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                category=filter_categoryList.get(position);
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

                if(categoryOfferName.getText().toString().equals("")){
                    Toast.makeText(CategoryOfferActivity.this,"please insert category offer name",Toast.LENGTH_LONG).show();
                }else {
                addProductFromCategoryDialog.dismiss();
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
                    int branchId=0;
                    if(SpProductBranch.getSelectedItem().toString().equals(getString(R.string.all))){
                        branchId=0;
                    }else {
                        branchId= SETTINGS.branchId;

                    }
                offerCategoryDbAdapter.insertEntry(categoryOfferName.getText().toString(), SESSION._EMPLOYEE.getEmployeeId(),productIdList, branchId);
                }

            }
        });
        categoryNameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String word = categoryNameSearch.getText().toString();
                if (!word.equals("")) {
                    // Database query can be a time consuming task ..
                    // so its safe to call database query in another thread
                    // Handler, will handle this stuff
                    new AsyncTask<String, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            final_filter_categoryList = categoryDBAdapter.getAllDepartmentByHint(params[0]);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            CategoryGridViewAdapter adapter = new CategoryGridViewAdapter(getApplicationContext(), final_filter_categoryList);
                            gvCategory.setAdapter(adapter);
                        }
                    }.execute(word);
                } else {
                    final_filter_categoryList = filter_categoryList;
                    CategoryGridViewAdapter adapter = new CategoryGridViewAdapter(getApplicationContext(), final_filter_categoryList);
                    gvCategory.setAdapter(adapter);
                }

            }
        });

    }
    private void addProductFromProductDialog(final boolean editOrAdd) {

        final_filter_productList =new ArrayList<>();
        filter_productList=new ArrayList<>();
        productListName=new ArrayList<>();
        productList=new ArrayList<>();
        addProductFromProductDialog.show();
        addProductFromProductDialog.setContentView(R.layout.add_category_offer_product_from_product_catalog);
       productName = (EditText) addProductFromProductDialog.findViewById(R.id.productName);
        /**productName.setFocusable(true);
        productName.requestFocus();
        productName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                productName.setFocusable(true);
            }
        });*/
        final EditText categoryName = (EditText) addProductFromProductDialog.findViewById(R.id.categoryName);
        if(editOrAdd){
            categoryName.setVisibility(View.GONE);
        }
        final GridView gvProduct = (GridView) addProductFromProductDialog.findViewById(R.id.gvProduct);
        gvProduct.setNumColumns(3);
      gvFilterProduct = (GridView) addProductFromProductDialog.findViewById(R.id.gvFilterProductInCategory);
        gvFilterProduct.setNumColumns(3);
        gvFilterProduct.setVisibility(View.GONE);
        Button btn_cancel_add_product = (Button) addProductFromProductDialog.findViewById(R.id.btn_cancel_add_product);
        Button btn_add_product = (Button) addProductFromProductDialog.findViewById(R.id.btn_add_product);

        btn_cancel_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (addProductFromProductDialog!= null && addProductFromProductDialog.isShowing()) {
                        addProductFromProductDialog.dismiss();
                    }
            }
        });
        final ProductDBAdapter  productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();

        filter_productList = productDBAdapter.getAllProducts();
        ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(this, filter_productList);
        gvProduct.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("testProduct",productList.toString());
                flagProduct=false;
                if(final_filter_productList.size()>0){
                product=final_filter_productList.get(position);
                }
                else {
                    product=filter_productList.get(position);

                }
                for(int i=0;i<productList.size();i++){
                    if(product.getProductId()==productList.get(i).getProductId()){
                        flagProduct=true;
                    }
                }
                if(!flagProduct){
                productList.add(product);
                    productListName.add(product.getDisplayName());

                }
                 filterCategoryOfferProductDialog = new FilterCategoryOfferProductDialog(CategoryOfferActivity.this,R.layout.grid_view_filter_category_offer_product,productListName);
                gvFilterProduct.setVisibility(View.VISIBLE);
                gvFilterProduct.setAdapter(filterCategoryOfferProductDialog);
                  productName.setText("");

            }
        });
        gvFilterProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                productList.remove(position);
                productListName.remove(position);
                filterCategoryOfferProductDialog.remove(position);
                filterCategoryOfferProductDialog = new FilterCategoryOfferProductDialog(CategoryOfferActivity.this, R.layout.list_adapter_row_checks, productListName);
                gvFilterProduct.setAdapter(filterCategoryOfferProductDialog);
                filterCategoryOfferProductDialog.notifyDataSetChanged();
            }
        });
        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editOrAdd){
                if(categoryName.getText().toString().equals("")){
                    Toast.makeText(CategoryOfferActivity.this,"please insert category offer name First then select Product",Toast.LENGTH_LONG).show();
                }else {
                    if (addProductFromProductDialog!= null && addProductFromProductDialog.isShowing()) {
                        addProductFromProductDialog.dismiss();
                    }
                    ArrayList<String>productIdList=new ArrayList<String>();
                    ArrayList<Product>products=new ArrayList<Product>();

                    for (int i=0;i<productList.size();i++){
                        productIdList.add(String.valueOf(productList.get(i).getProductId()).replaceAll("\\s+",""));

                    }
                    int branchId=0;
                    if(SpProductBranch.getSelectedItem().toString().equals(getString(R.string.all))){
                        branchId=0;
                    }else {
                        branchId= SETTINGS.branchId;

                    }
                    OfferCategoryDbAdapter offerCategoryDbAdapter = new OfferCategoryDbAdapter(CategoryOfferActivity.this);
                    offerCategoryDbAdapter.open();
                    offerCategoryDbAdapter.insertEntry(categoryName.getText().toString(), SESSION._EMPLOYEE.getEmployeeId(),productIdList, branchId);
                }
                }else {
                    for (int i=0;i<productList.size();i++){
                        productIdListForEdit.add(String.valueOf(productList.get(i).getProductId()).replaceAll("\\s+",""));

                    }
                    if (addProductFromProductDialog!= null && addProductFromProductDialog.isShowing()) {
                        addProductFromProductDialog.dismiss();
                    }

                }

            }
        });
        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String word = productName.getText().toString();
                String newBarCode="";

                if (word.contains("\r\n")) {
                    newBarCode =word.replace("\r\n", "");
                } else if (word.contains("\r")) {
                    newBarCode = word.replace("\r", "");
                } else if (word.contains("\n")) {
                    newBarCode =word.replace("\n", "");
                }else {
                    newBarCode=word;
                }
                if (!newBarCode.equals("")) {
                    // Database query can be a time consuming task ..
                    // so its safe to call database query in another thread
                    // Handler, will handle this stuff
                    new AsyncTask<String, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            final_filter_productList = productDBAdapter.getAllProductsByHint(params[0], 0, 80);


                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(getApplicationContext(), final_filter_productList);
                            gvProduct.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            //suggestionAdapter.notifyDataSetChanged();
                        }
                    }.execute(word);
                } else {
                    final_filter_productList = filter_productList;
                    ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(getApplicationContext(), final_filter_productList);
                    gvProduct.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });


    }


}
