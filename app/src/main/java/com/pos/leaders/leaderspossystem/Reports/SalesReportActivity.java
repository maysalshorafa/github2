package com.pos.leaders.leaderspossystem.Reports;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.ProductCatalogActivity;
import com.pos.leaders.leaderspossystem.ProductsActivity;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SaleManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SalesReportListViweAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.CategoryGridViewAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class SalesReportActivity extends AppCompatActivity {

    TextView etFromDate, etToDate,amount,tvCountSale;
    Button btncancleReport,btnCategory,btnproduct;

    GridView gvCategory,gvProducts;
    CategoryDBAdapter categoryDBAdapter;
    List<Category> listCategory;
    List<Long> order;
    List<Long> orderProduct;
    List<Order> OrderList=new ArrayList<>();
    ListView lvReport;
    Date from, to;
    double amountReport=0;
    Long CategoryId;
    long productIdSelect;
    ViewGroup  headerCategory,headerProduct;
    LinearLayout LAmountRepot;
    boolean btnisclickedCategory = true,btnisclickedProduct=true;

    View prseedButtonDepartments;
    List<Product> filter_productsList;
    private static final int DIALOG_FROM_DATE = 825;
    private static final int DIALOG_TO_DATE = 324;
    CategoryGridViewAdapter adapterCategory;
    SaleManagementListViewAdapter adapterOrderList;
    List<Object>objectList = new ArrayList<Object>();
    List<Long>productID=new ArrayList<>();
    EditText etSearch;
    ProductDBAdapter productDBAdapter;
    int productLoadItemOffset=0;
    int productCountLoad=80;
    boolean userScrolled=false;
    List<Product> productsList;
    ProductCatalogGridViewAdapter adapterProduct;
    SaleManagementListViewAdapter adapterListProduct;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sales_report);
        TitleBar.setTitleBar(this);
        LayoutInflater inflater = getLayoutInflater();
         headerCategory = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_order, lvReport, false);
        LayoutInflater  inflaterProduct = getLayoutInflater();
        headerProduct = (ViewGroup)inflaterProduct.inflate(R.layout.list_adapter_head_row_order, lvReport, false);

        etFromDate = (TextView) findViewById(R.id.SaleReport_ETFrom);
        etToDate = (TextView) findViewById(R.id.SaleReport_ETTo);
        tvCountSale = (TextView) findViewById(R.id.SaleReport_Count);
        amount=(TextView) findViewById(R.id.SaleReport_amount);
        btncancleReport=(Button)findViewById(R.id.SaleReport_cancle);
        gvCategory = (GridView) findViewById(R.id.Category_Report);
        btnCategory=(Button) findViewById(R.id.btn_Category);
        btnproduct=(Button) findViewById(R.id.btn_product);
        lvReport = (ListView) findViewById(R.id.SaleReport_LVReport);
        LAmountRepot=(LinearLayout) findViewById(R.id.Linear_AmountReport);
        etSearch = (EditText) findViewById(R.id.productCatalog_ETSearch);
        gvProducts = (GridView) findViewById(R.id.productCatalog_GVProducts);

        etFromDate.setFocusable(false);
        etFromDate.setText(DateConverter.getBeforeMonth().split(" ")[0]);
        from = DateConverter.stringToDate(DateConverter.getBeforeMonth());
        etToDate.setFocusable(false);
        etToDate.setText(DateConverter.currentDateTime().split(" ")[0]);
        to = DateConverter.stringToDate(DateConverter.currentDateTime());

        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_FROM_DATE);
            }
        });
        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TO_DATE);
            }
        });
        btncancleReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

      btnCategory.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         if (btnisclickedCategory){
             makeList();
             etSearch.setVisibility(View.GONE);
             gvCategory.setVisibility(View.VISIBLE);
             lvReport.setVisibility(View.VISIBLE);
             LAmountRepot.setVisibility(View.GONE);
             btnisclickedCategory=false;
         }
         else {
             etSearch.setVisibility(View.GONE);
             gvProducts.setVisibility(View.GONE);
             gvCategory.setVisibility(View.GONE);
             lvReport.setVisibility(View.GONE);
             LAmountRepot.setVisibility(View.GONE);
             btnisclickedCategory=true;
         }
      }
       });

        btnproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btnisclickedProduct) {
                    gvCategory.setVisibility(View.GONE);
                    lvReport.setVisibility(View.GONE);
                    LAmountRepot.setVisibility(View.GONE);
                    productDBAdapter = new ProductDBAdapter(SalesReportActivity.this);
                    productDBAdapter.open();
                    productsList = productDBAdapter.getTopProducts(productLoadItemOffset, productCountLoad);
                    filter_productsList = productsList;
                    adapterProduct = new ProductCatalogGridViewAdapter(SalesReportActivity.this, productsList);
                    gvProducts.setAdapter(adapterProduct);
                    etSearch.setVisibility(View.VISIBLE);
                    gvProducts.setVisibility(View.VISIBLE);
                    btnisclickedProduct=false;
                }
                else {
                    gvCategory.setVisibility(View.GONE);
                    lvReport.setVisibility(View.GONE);
                    LAmountRepot.setVisibility(View.GONE);
                    etSearch.setVisibility(View.GONE);
                    gvProducts.setVisibility(View.GONE);
                    btnisclickedProduct=true;
                }

            }
        });
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                gvProducts.setVisibility(View.GONE);
                etSearch.setVisibility(View.GONE);
                gvCategory.setVisibility(View.GONE);
                lvReport.setVisibility(View.VISIBLE);
                LAmountRepot.setVisibility(View.GONE);
                CategoryId=listCategory.get(position).getCategoryId();
                Log.d("CategoryId",CategoryId+"cat");
                setOrder();
            }
        });



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
                            adapterProduct = new ProductCatalogGridViewAdapter(getApplicationContext(), filter_productsList);
                            gvProducts.setAdapter(adapterProduct);
                        }
                    }.execute(word);
                } else {
                    filter_productsList = productsList;
                    adapterProduct = new ProductCatalogGridViewAdapter(getApplicationContext(), filter_productsList);
                    gvProducts.setAdapter(adapterProduct);
                }

            }
        });
        gvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                productIdSelect= filter_productsList.get(position).getProductId();
                SetOrderProduct();

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
                  //  LoadMoreProducts();
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_FROM_DATE) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, onFromDateSetListener, Integer.parseInt(from.toString().split(" ")[5]), from.getMonth(), Integer.parseInt(from.toString().split(" ")[2]));
            //datePickerDialog.getDatePicker().setMaxDate(to.getTime());
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            return datePickerDialog;
        } else if (id == DIALOG_TO_DATE) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, onToDateSetListener, Integer.parseInt(to.toString().split(" ")[5]), to.getMonth(), Integer.parseInt(to.toString().split(" ")[2]));
            //datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            //datePickerDialog.getDatePicker().setMinDate(from.getTime());
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener onFromDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            etFromDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            from = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
            view.setMaxDate(from.getTime());
            setOrder();
            Log.d("rr","from");
        }
    };

    private DatePickerDialog.OnDateSetListener onToDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            etToDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            to = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
            view.setMinDate(to.getTime());
           setOrder();
        }
    };
    private void makeList() {
        // Category Data adapter
        categoryDBAdapter = new CategoryDBAdapter(this);
        categoryDBAdapter.open();
        listCategory = categoryDBAdapter.getAllDepartments();
        adapterCategory = new CategoryGridViewAdapter(SalesReportActivity.this, listCategory);
        gvCategory.setAdapter(adapterCategory);
    }

    private void setOrder() {
        objectList = new ArrayList<>();
        OrderList = new ArrayList<>();
        order = new ArrayList<>();
        productID = new ArrayList<>();
        ProductDBAdapter productDBAdapter = new ProductDBAdapter(SalesReportActivity.this);
        productDBAdapter.open();
        productID = productDBAdapter.getProductByCategory(CategoryId);
        OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(SalesReportActivity.this);
        orderDBAdapter.open();
        order = orderDBAdapter.getOrderDetailsByListIDproduct(productID);
        OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(SalesReportActivity.this);
        orderDBAdapter1.open();
        OrderList = orderDBAdapter1.getBetweenByOrder(from.getTime(), to.getTime(), order);
        if (OrderList.size() > 0) {
            lvReport.setVisibility(View.VISIBLE);
            LAmountRepot.setVisibility(View.VISIBLE);
            for (int i = 0; i < OrderList.size(); i++) {
                amountReport = amountReport + OrderList.get(i).getTotalPrice();
            }
            amount.setText(amountReport + "");
            tvCountSale.setText(OrderList.size() + "");
            lvReport.addHeaderView(headerCategory, null, false);
            objectList.addAll(OrderList);
            adapterOrderList = new SaleManagementListViewAdapter(SalesReportActivity.this, R.layout.list_adapter_row_sales_management, objectList);
            lvReport.setAdapter(adapterOrderList);
        } else {
            LAmountRepot.setVisibility(View.GONE);
            lvReport.setVisibility(View.GONE);
        }
    }
    private void SetOrderProduct() {
        objectList = new ArrayList<>();
        OrderList = new ArrayList<>();
        orderProduct = new ArrayList<>();
        Log.d("jjp",productIdSelect+"");
        OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(SalesReportActivity.this);
        orderDBAdapter.open();
        orderProduct = orderDBAdapter.getOrderDetailsByIDproduct(productIdSelect);
        Log.d("orderDetialsAdapter", String.valueOf(order));
        OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(SalesReportActivity.this);
        orderDBAdapter1.open();
        OrderList = orderDBAdapter1.getBetweenByOrder(from.getTime(), to.getTime(), orderProduct);
        Log.d("OrderList", OrderList.toString());
        if (OrderList.size() > 0) {
            gvCategory.setVisibility(View.GONE);
            gvProducts.setVisibility(View.GONE);
            etSearch.setVisibility(View.GONE);
            lvReport.setVisibility(View.VISIBLE);
            LAmountRepot.setVisibility(View.VISIBLE);


        for (int i=0; i<OrderList.size();i++){
            amountReport=amountReport+OrderList.get(i).getTotalPrice();
        }
        amount.setText( amountReport+"");
        tvCountSale.setText(OrderList.size()+"");
        lvReport.addHeaderView(headerProduct, null, false);
        objectList.addAll(OrderList);
        adapterListProduct = new SaleManagementListViewAdapter(SalesReportActivity.this, R.layout.list_adapter_row_sales_management, objectList);
        lvReport.setAdapter(adapterListProduct);
    }
        else {
            LAmountRepot.setVisibility(View.GONE);
            lvReport.setVisibility(View.GONE);
        }
    }
    protected void LoadMoreProducts(){
        final ProgressDialog dialog=new ProgressDialog(SalesReportActivity.this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                filter_productsList=productsList;
                adapterProduct.notifyDataSetChanged();
                dialog.cancel();
            }

            @Override
            protected Void doInBackground(Void... params) {

                    productsList.addAll(productDBAdapter.getTopProducts(productLoadItemOffset,productCountLoad));

                return null;
            }
        }.execute();



    }

}


