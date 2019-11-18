package com.pos.leaders.leaderspossystem.Reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ProductSale;
import com.pos.leaders.leaderspossystem.Models.SumOfSalesPerProduct;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.AllSalesManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.CategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.SaleManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SalesReportActivity extends AppCompatActivity {

    TextView etFromDate, etToDate,amount,tvCountSale;
    Button btncancleReport,btnCategory,btnproduct,btnAllSales;

    GridView gvCategory,gvProducts;
    CategoryDBAdapter categoryDBAdapter;
    List<Category> listCategory;
    List<Long> order;
    List<Long> orderProduct;
    List<Order> OrderList=new ArrayList<>();
    ListView lvReport;
    public static ListView lvAllSallesList;
    public  static Date from, to;
    double amountReport=0;
    Long CategoryId;
    long productIdSelect;
    ViewGroup  headerCategory,headerAllSales;
    LinearLayout LAmountRepot;
    boolean btnisclickedCategory = true,btnisclickedProduct=true,btnisclickedAll=true;
    List<Product> filter_productsList;
    private static final int DIALOG_FROM_DATE = 825;
    private static final int DIALOG_TO_DATE = 324;
    CategoryGridViewAdapter adapterCategory;
    SaleManagementListViewAdapter adapterOrderList;
    List<Object>objectList = new ArrayList<Object>();
    public static  List<Object>objectListSalesAllProduct = new ArrayList<Object>();
    List<Long>productID=new ArrayList<>();
    EditText etSearch;
    ProductDBAdapter productDBAdapter;
    int productLoadItemOffset=0;
    int productCountLoad=80;
    public static   List<SumOfSalesPerProduct>finalListProduct=new ArrayList<>();
    boolean userScrolled=false;
    List<Product> productsList;
    ProductCatalogGridViewAdapter adapterProduct;
    SaleManagementListViewAdapter adapterListProduct;
    String flageGridOnClick="";
    AsyncTask<String, String, String> sendTask;
    public static Context context = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sales_report);
        TitleBar.setTitleBar(this);



        etFromDate = (TextView) findViewById(R.id.SaleReport_ETFrom);
        etToDate = (TextView) findViewById(R.id.SaleReport_ETTo);
        tvCountSale = (TextView) findViewById(R.id.SaleReport_Count);
        amount=(TextView) findViewById(R.id.SaleReport_amount);
        btncancleReport=(Button)findViewById(R.id.SaleReport_cancle);
        gvCategory = (GridView) findViewById(R.id.Category_Report);
        btnCategory=(Button) findViewById(R.id.btn_Category);
        btnproduct=(Button) findViewById(R.id.btn_product);
        lvReport = (ListView) findViewById(R.id.SaleReport_LVReport);
        lvAllSallesList=(ListView) findViewById(R.id.AllSaleReport_LVReport);
        LAmountRepot=(LinearLayout) findViewById(R.id.Linear_AmountReport);
        etSearch = (EditText) findViewById(R.id.productCatalog_ETSearch);
        gvProducts = (GridView) findViewById(R.id.productCatalog_GVProducts);
        btnAllSales=(Button) findViewById(R.id.btn_ALL);

        etFromDate.setFocusable(false);
        etFromDate.setText(DateConverter.getBeforeMonth().split(" ")[0]);
        from = DateConverter.stringToDate(DateConverter.getBeforeMonth());
        etToDate.setFocusable(false);
        etToDate.setText(DateConverter.currentDateTime().split(" ")[0]);
        to = DateConverter.stringToDate(DateConverter.currentDateTime());
        LayoutInflater inflater = getLayoutInflater();
        headerCategory = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_order, lvReport, false);
        lvReport.addHeaderView(headerCategory, null, false);

        headerAllSales=(ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_all_sales, lvReport, false);
        lvAllSallesList.addHeaderView(headerAllSales,null,false);

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
        context=this;

        btnAllSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnisclickedAll){
                    btnisclickedAll=false;
                    flageGridOnClick="onCreate";
                    setOrder();
                }
                else{
                    btnisclickedAll=true;
                }

            }
        });

      btnCategory.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         if (btnisclickedCategory){
             makeList();
             etSearch.setVisibility(View.GONE);
             LAmountRepot.setVisibility(View.GONE);
             lvAllSallesList.setVisibility(View.GONE);
             btnisclickedCategory=false;
         }
         else {
             etSearch.setVisibility(View.GONE);
             gvProducts.setVisibility(View.GONE);
             gvCategory.setVisibility(View.GONE);
             lvReport.setVisibility(View.GONE);
             LAmountRepot.setVisibility(View.GONE);
             lvAllSallesList.setVisibility(View.GONE);
             btnisclickedCategory=true;
         }
      }
       });

        btnproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btnisclickedProduct) {
                    flageGridOnClick="onclickProduct";
                    gvCategory.setVisibility(View.GONE);
                    lvReport.setVisibility(View.GONE);
                    lvAllSallesList.setVisibility(View.GONE);
                    lvAllSallesList.setVisibility(View.GONE);
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
                    setOrder();
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
                flageGridOnClick="gvCategory";
                gvProducts.setVisibility(View.GONE);
                etSearch.setVisibility(View.GONE);
                gvCategory.setVisibility(View.GONE);
                LAmountRepot.setVisibility(View.GONE);
                lvAllSallesList.setVisibility(View.GONE);
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
                flageGridOnClick="gvProducts";
                setOrder();

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

       /*lvAllSallesList.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    LoadMoreProductsAllSales();
                }
            }
        });*/
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_FROM_DATE) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, onFromDateSetListener, Integer.parseInt(from.toString().split(" ")[5]), from.getMonth(), Integer.parseInt(from.toString().split(" ")[2]));
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            return datePickerDialog;
        } else if (id == DIALOG_TO_DATE) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, onToDateSetListener, Integer.parseInt(to.toString().split(" ")[5]), to.getMonth(), Integer.parseInt(to.toString().split(" ")[2]));

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
            setOrder();
        }
    };

    private DatePickerDialog.OnDateSetListener onToDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            etToDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            to = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
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
        gvCategory.setVisibility(View.VISIBLE);
    }

    private void setOrder() {
        amountReport=0;
        if (flageGridOnClick.equals("onclickProduct")){
            amountReport=0;
            OrderList=new ArrayList<Order>();
            sendTask = new AsyncTask<String, String, String>() {

                   protected String doInBackground(String... title) {
                    OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(SalesReportActivity.this);
                    orderDBAdapter1.open();
                    Log.d("OrderListProduct", OrderList.size() + "");
                    OrderList = orderDBAdapter1.getBetweenOrder(from.getTime(), to.getTime());

                return "Sent message.";
            }
                protected void onPostExecute(String result) {
               for (int i2 = 0; i2 < OrderList.size(); i2++) {
                amountReport = amountReport + OrderList.get(i2).getTotalPrice();
               }
                    LAmountRepot.setVisibility(View.VISIBLE);
                   tvCountSale.setText(OrderList.size() + "");
                    amount.setText(Util.makePrice(amountReport));}
            };

            sendTask.execute(null, null, null);}



      else  if (flageGridOnClick.equals("onCreate")){
            finalListProduct.clear();
            amountReport=0;
            OrderList=new ArrayList<Order>();
            objectList=new ArrayList<>();
            gvProducts.setVisibility(View.GONE);
            etSearch.setVisibility(View.GONE);
            gvCategory.setVisibility(View.GONE);
            lvReport.setVisibility(View.GONE);
            LAmountRepot.setVisibility(View.GONE);
            GetALLProductSalesConnection getALLProductSalesConnection = new GetALLProductSalesConnection();
            getALLProductSalesConnection.execute(String.valueOf(from),String.valueOf(to));
         /*   sendTask = new AsyncTask<String, String, String>() {

                @Override
                protected  void onPreExecute()
                {
                    progressDialog = ProgressDialog.show(SalesReportActivity.this, getString(R.string.plzwait), getString(R.string.loadingdata), true);

                }
                protected String doInBackground(String... title) {

                    OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(SalesReportActivity.this);
                    orderDBAdapter1.open();
                    OrderList = orderDBAdapter1.getBetweenOrder(from.getTime(),to.getTime());
                    Set<Order> set = new HashSet<>(OrderList);
                    OrderList.clear();
                    OrderList.addAll(set);
                    if (OrderList.size()!= 0) {
                        List<OrderDetails> orderDetailsList = new ArrayList<>();
                        OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(getApplicationContext());
                        for (int x = 0; x < OrderList.size(); x++) {
                            orderDetailsDBAdapter.open();
                            orderDetailsList.addAll(orderDetailsDBAdapter.getOrderBySaleID(OrderList.get(x).getOrderId()));
                            orderDetailsDBAdapter.close();
                        }

                        productDBAdapterALlSale = new ProductDBAdapter(SalesReportActivity.this);
                        productDBAdapterALlSale.open();
                        productList = productDBAdapterALlSale.getTopProducts(productLoadItemOffsetAllSales,productCountLoadAllSales);
                        if(productList.size()!= 0){
                            Log.d("productListSize",productList.size()+"size");
                            Log.d("orderDetailsListsize",orderDetailsList.size()+"size");
                        for(int a=0;a<productList.size();a++){
                            Product p =productList.get(a);
                            ProductSale productSale=new ProductSale();
                            productSale.setProduct(p);
                            for(int x=0;x<orderDetailsList.size();x++){
                                if(p.getProductId()==orderDetailsList.get(x).getProductId()) {
                                    int i = orderDetailsList.get(x).getQuantity();
                                    productSale.setCount(productSale.getCount() + i);
                                    productSale.setPrice(productSale.getPrice() + (p.getPrice() * i));

                                }}
                            finalListProduct.add(productSale);
                            }
                            Log.d("finalListProductSize",finalListProduct.size()+"Size");}}

                        return "Sent message.";
                    }
                protected void onPostExecute(String result) {
                    if (progressDialog.isShowing()) progressDialog.dismiss();
                    sendTask = null;
                        if (finalListProduct.size()!=0){
                            Log.d("finalListProduct",finalListProduct.toString());

                        lvAllSallesList.setVisibility(View.VISIBLE);
                        LAmountRepot.setVisibility(View.VISIBLE);
                        Log.d("OrderListOnCreate",OrderList.size()+"");
                        for (int i2 = 0; i2 < OrderList.size(); i2++) {
                            amountReport = amountReport + OrderList.get(i2).getTotalPrice();
                            Log.d("i2",i2+"");
                        }
                        tvCountSale.setText(OrderList.size() + "");
                        amount.setText(Util.makePrice(amountReport));
                            Set<ProductSale> set2 = new HashSet<>(finalListProduct);
                            finalListProduct.clear();
                            finalListProduct.addAll(set2);
                            Collections.sort(finalListProduct, new CustomComparator());
                          objectList.addAll(finalListProduct);

                        adapterAllOrderList = new AllSalesManagementListViewAdapter(SalesReportActivity.this, R.layout.list_adapter_row_all_sales, objectList);
                        lvAllSallesList.setAdapter(adapterAllOrderList);

                    }
                    else {
                        LAmountRepot.setVisibility(View.GONE);
                        lvReport.setVisibility(View.GONE);
                        Toast.makeText(SalesReportActivity.this, R.string.there_are_no_sales,
                                Toast.LENGTH_LONG).show();
                    }
                }
            };

            sendTask.execute(null, null, null);*/
      }
       else if (flageGridOnClick.equals("gvCategory")){
        objectList = new ArrayList<>();
        OrderList = new ArrayList<>();
        order = new ArrayList<>();
        productID = new ArrayList<>();
            sendTask = new AsyncTask<String, String, String>() {
                protected String doInBackground(String... title) {

        ProductDBAdapter productDBAdapter = new ProductDBAdapter(SalesReportActivity.this);
        productDBAdapter.open();
        productID = productDBAdapter.getProductByCategory(CategoryId);
        OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(SalesReportActivity.this);
        orderDBAdapter.open();
        order = orderDBAdapter.getOrderDetailsByListIDproduct(productID);
            Set<Long> set = new HashSet<>(order);
            order.clear();
            order.addAll(set);
            OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(SalesReportActivity.this);
            orderDBAdapter1.open();

            for(int i=0;i<order.size();i++) {
                Order o = orderDBAdapter1.getOrderById(order.get(i));
                Date date = new Date(o.getCreatedAt().getTime());
                String date1 = new SimpleDateFormat("dd/MM/yyyy").format(date);

                Date date2 = new Date(from.getTime());
                String date3 = new SimpleDateFormat("dd/MM/yyyy").format(date2);

                Date date4 = new Date(to.getTime());
                String date5 = new SimpleDateFormat("dd/MM/yyyy").format(date4);
                try {
                    Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse(date1);
                    Date d2 = new SimpleDateFormat("dd/MM/yyyy").parse(date3);
                    Date d3 = new SimpleDateFormat("dd/MM/yyyy").parse(date5);
                    if ((d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0) && (d1.compareTo(d3) < 0 || d1.compareTo(d3) == 0)) {
                        OrderList.add(o);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
                    return "Sent message.";
                }
                protected void onPostExecute(String result) {

              if (OrderList.size() > 0) {
                lvReport.setVisibility(View.VISIBLE);
                LAmountRepot.setVisibility(View.VISIBLE);
                for (int i2 = 0; i2 < OrderList.size(); i2++) {
                    amountReport = amountReport + OrderList.get(i2).getTotalPrice();
                }

            } else {
                LAmountRepot.setVisibility(View.GONE);
                lvReport.setVisibility(View.GONE);
                Toast.makeText(SalesReportActivity.this, R.string.there_are_no_sales,
                        Toast.LENGTH_LONG).show();
            }
            amount.setText(Util.makePrice(amountReport));
            tvCountSale.setText(OrderList.size() + "");
            objectList.addAll(OrderList);
            adapterOrderList = new SaleManagementListViewAdapter(SalesReportActivity.this, R.layout.list_adapter_row_sales_management, objectList);
            lvReport.setAdapter(adapterOrderList);
                }
            };

            sendTask.execute(null, null, null);}



        else if (flageGridOnClick.equals("gvProducts")){
            objectList = new ArrayList<>();
            OrderList = new ArrayList<>();
            orderProduct = new ArrayList<>();
            OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(SalesReportActivity.this);
            orderDBAdapter.open();
            orderProduct = orderDBAdapter.getOrderDetailsByIDproduct(productIdSelect);
            Set<Long> set = new HashSet<>(orderProduct);
            orderProduct.clear();
            orderProduct.addAll(set);
            OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(SalesReportActivity.this);
            orderDBAdapter1.open();

            for(int i=0;i<orderProduct.size();i++) {
                Order o = orderDBAdapter1.getOrderById(orderProduct.get(i));
                Date date = new Date(o.getCreatedAt().getTime());
               String date1 = new SimpleDateFormat("dd/MM/yyyy").format(date);

                Date date2 = new Date(from.getTime());
                String date3 = new SimpleDateFormat("dd/MM/yyyy").format(date2);

                Date date4 = new Date(to.getTime());
                String date5 = new SimpleDateFormat("dd/MM/yyyy").format(date4);
                try {
                    Date d1=new SimpleDateFormat("dd/MM/yyyy").parse(date1);
                    Date d2=new SimpleDateFormat("dd/MM/yyyy").parse(date3);
                    Date d3=new SimpleDateFormat("dd/MM/yyyy").parse(date5);
                    if((d1.compareTo(d2)>0 || d1.compareTo(d2)==0)&&(d1.compareTo(d3)<0 || d1.compareTo(d3)==0) ){
                        OrderList.add(o);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }



        }
            List<OrderDetails>orderDetailsListForProduct=new ArrayList<>();
            for (int i1=0; i1<OrderList.size();i1++){
                orderDetailsListForProduct=orderDBAdapter.getOrderBySaleIDAndProductId(OrderList.get(i1).getOrderId(),productIdSelect);
                for (int a=0;a<orderDetailsListForProduct.size();a++){
                    amountReport=amountReport+orderDetailsListForProduct.get(a).getItemTotalPrice();

                }
            }

            if (orderProduct.size() > 0) {
                gvCategory.setVisibility(View.GONE);
                gvProducts.setVisibility(View.GONE);
                etSearch.setVisibility(View.GONE);
                lvReport.setVisibility(View.VISIBLE);
                LAmountRepot.setVisibility(View.VISIBLE);


            }
            else {
                LAmountRepot.setVisibility(View.GONE);
                lvReport.setVisibility(View.GONE);
                Toast.makeText(SalesReportActivity.this, R.string.there_are_no_sales,
                        Toast.LENGTH_LONG).show();
            }
            amount.setText(Util.makePrice(amountReport));
            tvCountSale.setText(OrderList.size()+"");
            objectList.addAll(OrderList);

            adapterListProduct = new SaleManagementListViewAdapter(SalesReportActivity.this, R.layout.list_adapter_row_sales_management, objectList);
            lvReport.setAdapter(adapterListProduct);
        }
        }

    protected void LoadMoreProducts(){
        productLoadItemOffset+=productCountLoad;
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

 /*   protected void LoadMoreProductsAllSales(){
        productLoadItemOffsetAllSales+=productCountLoadAllSales;

        final ProgressDialog dialog=new ProgressDialog(SalesReportActivity.this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (finalListProduct.size()!=0){
                    lvAllSallesList.setVisibility(View.VISIBLE);
                    LAmountRepot.setVisibility(View.VISIBLE);
                    Log.d("OrderListOnCreate",OrderList.size()+"");
                    for (int i2 = 0; i2 < OrderList.size(); i2++) {
                        amountReport = amountReport + OrderList.get(i2).getTotalPrice();
                        Log.d("i2",i2+"");
                    }
                    tvCountSale.setText(OrderList.size() + "");
                    amount.setText(Util.makePrice(amountReport));
                    Set<ProductSale> set2 = new HashSet<>(finalListProduct);
                    finalListProduct.clear();
                    finalListProduct.addAll(set2);
                    Collections.sort(finalListProduct, new CustomComparator());
                    objectList.addAll(finalListProduct);

                    adapterAllOrderList = new AllSalesManagementListViewAdapter(SalesReportActivity.this, R.layout.list_adapter_row_all_sales, objectList);
                    lvAllSallesList.setAdapter(adapterAllOrderList);
                    Log.d("finalListProduct",finalListProduct.toString());
                    Log.d("finalListProductSize2",finalListProduct.size()+"Size2");
                }
               *//* else {
                    LAmountRepot.setVisibility(View.GONE);
                    lvReport.setVisibility(View.GONE);
                    Toast.makeText(SalesReportActivity.this, R.string.there_are_no_sales,
                            Toast.LENGTH_LONG).show();
                }*//*
//                adapterAllOrderList.notifyDataSetChanged();
                dialog.cancel();
            }

            @Override
            protected Void doInBackground(Void... params) {
                OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(SalesReportActivity.this);
                orderDBAdapter1.open();
                OrderList = orderDBAdapter1.getBetweenOrder(from.getTime(),to.getTime());
                Set<Order> set = new HashSet<>(OrderList);
                OrderList.clear();
                OrderList.addAll(set);
                if (OrderList.size()!= 0) {
                    List<OrderDetails> orderDetailsList = new ArrayList<>();
                    OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(getApplicationContext());
                    for (int x = 0; x < OrderList.size(); x++) {
                        orderDetailsDBAdapter.open();
                        orderDetailsList.addAll(orderDetailsDBAdapter.getOrderBySaleID(OrderList.get(x).getOrderId()));
                        orderDetailsDBAdapter.close();
                    }
                    Log.d("testOrderList2", orderDetailsList.toString());


                    productDBAdapterALlSale = new ProductDBAdapter(SalesReportActivity.this);
                    productDBAdapterALlSale.open();
                    productList = productDBAdapterALlSale.getTopProducts(productLoadItemOffsetAllSales,productCountLoadAllSales);
                    if(productList.size()!= 0){
                        Log.d("productListSize2",productList.size()+"size");
                        for(int a=0;a<productList.size();a++){
                            Product p =productList.get(a);
                            ProductSale productSale=new ProductSale();
                            productSale.setProduct(p);
                            for(int x=0;x<orderDetailsList.size();x++){
                                if(p.getProductId()==orderDetailsList.get(x).getProductId()){
                                    int i = orderDetailsList.get(x).getQuantity();
                                    productSale.setCount(productSale.getCount()+i);
                                    productSale.setPrice(productSale.getPrice()+(p.getPrice()*i));

                                }
                            }
                            finalListProduct.add(productSale);
                        }}}

                return null;
            }
        }.execute();



    }*/
}
class GetALLProductSalesConnection extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;
    ProductSale productSale;
    JSONObject jsonObject;
    JSONArray jsonArray;
    JSONArray jsonArray1;
    SumOfSalesPerProduct sumOfSalesPerProduct;
    GetALLProductSalesConnection() {
        Date date2 = new Date(SalesReportActivity.from.getTime());

        String date3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000", Locale.ENGLISH).format(date2);
        Log.d("date2",date2+"");

        Date date4 = new Date(SalesReportActivity.to.getTime());
        Log.d("date3",date4+"");
        String date5 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000", Locale.ENGLISH).format(date4);
        Log.d("date5",date5);
        Log.d("date3",date3);

        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL,date3,date5);
    }

    final ProgressDialog progressDialog = new ProgressDialog(SalesReportActivity.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(SalesReportActivity.context);

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Please Wait");
        progressDialog2.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {//args{key,uuid}

            try {

                String url = "DashBoard/perProductForPos";
                String AllSalesProduct = messageTransmit.getALLSalesProduct(url,SESSION.token);
                Log.d("AllSalesProduct",AllSalesProduct);
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(AllSalesProduct);

            String idProduct;
                double countSalesProduct,total;
            try {
                String msgData = jsonObject.getString(MessageKey.responseBody);
                jsonObject = new JSONObject(msgData);
                jsonArray = jsonObject.getJSONArray("sumOfSalesPerProduct");
                jsonArray1 = jsonObject.getJSONArray("countOfSalesPerProduct");

                Log.d("jsonArray",jsonArray.toString()+"djjj");
                Log.d("jsonArray1",jsonArray1.toString()+"djjj");

                for (int i = 0; i <= jsonArray.length() ; i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                    idProduct = jsonObject1.getString("_id");
                    Log.d("idProduct",jsonObject1.getLong("_id")+"");
                    total = jsonObject1.getDouble("total");
                    countSalesProduct = jsonObject2.getDouble("count");
                    sumOfSalesPerProduct=new SumOfSalesPerProduct(idProduct,total,countSalesProduct);
                    Log.d("finalListProductfinal",sumOfSalesPerProduct.toString()+"final");
                    SalesReportActivity.finalListProduct.add(sumOfSalesPerProduct);
                }


            } catch (Exception e) {
                Log.d("exception1", e.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }
    @Override
    protected void onPostExecute(final String s) {
        Log.d("s1",s);
        if (s != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog2.setTitle("Success.");
                    progressDialog2.show();
                    Log.d("final",  SalesReportActivity.finalListProduct.toString());
                    if (SalesReportActivity.finalListProduct.size()>0){
                        SalesReportActivity.lvAllSallesList.setVisibility(View.VISIBLE);
                  AllSalesManagementListViewAdapter  adapterAllOrderList = new AllSalesManagementListViewAdapter(SalesReportActivity.context, R.layout.list_adapter_row_all_sales, SalesReportActivity.finalListProduct);
                   SalesReportActivity.lvAllSallesList.setAdapter(adapterAllOrderList);
                    }
                    else {
                        SalesReportActivity.lvAllSallesList.setVisibility(View.GONE);
                        Toast.makeText(SalesReportActivity.context, R.string.there_are_no_sales,
                                Toast.LENGTH_LONG).show();}
                }


                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    progressDialog2.cancel();
                    super.onPostExecute(aVoid);
                }
            }.execute();
        } else {
            //fail
            Toast.makeText(SalesReportActivity.context, "Try Again.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }

}


