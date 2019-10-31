package com.pos.leaders.leaderspossystem.Reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SaleManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    String flageGridOnClick="";


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
        LAmountRepot=(LinearLayout) findViewById(R.id.Linear_AmountReport);
        etSearch = (EditText) findViewById(R.id.productCatalog_ETSearch);
        gvProducts = (GridView) findViewById(R.id.productCatalog_GVProducts);

        etFromDate.setFocusable(false);
        etFromDate.setText(DateConverter.getBeforeMonth().split(" ")[0]);
        from = DateConverter.stringToDate(DateConverter.getBeforeMonth());
        etToDate.setFocusable(false);
        etToDate.setText(DateConverter.currentDateTime().split(" ")[0]);
        to = DateConverter.stringToDate(DateConverter.currentDateTime());
        LayoutInflater inflater = getLayoutInflater();
        headerCategory = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_order, lvReport, false);
        lvReport.addHeaderView(headerCategory, null, false);
        /*LayoutInflater  inflaterProduct = getLayoutInflater();
        headerProduct = (ViewGroup)inflaterProduct.inflate(R.layout.list_adapter_head_row_order, lvReport, false);
        lvReport.addHeaderView(headerProduct, null, false);*/
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
                flageGridOnClick="gvCategory";
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
    }

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
            view.setMaxDate(to.getTime());
            setOrder();
        }
    };

    private DatePickerDialog.OnDateSetListener onToDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            etToDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            to = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
            view.setMinDate(from.getTime());
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
        amountReport=0;


        if (flageGridOnClick.equals("gvCategory")){
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
            Log.d("fromProduct",from.toString()+to.toString()+" "+CategoryId);
            Log.d("order",order.toString());
        OrderList = orderDBAdapter1.getBetweenByOrder(from.getTime(), to.getTime(), order);
            Log.d("OrderList",OrderList.toString());
        if (OrderList.size() > 0) {
            lvReport.setVisibility(View.VISIBLE);
            LAmountRepot.setVisibility(View.VISIBLE);
            for (int i = 0; i < OrderList.size(); i++) {
                amountReport = amountReport + OrderList.get(i).getTotalPrice();
            }
            amount.setText(amountReport + "");
            tvCountSale.setText(OrderList.size() + "");
            objectList.addAll(OrderList);
            adapterOrderList = new SaleManagementListViewAdapter(SalesReportActivity.this, R.layout.list_adapter_row_sales_management, objectList);
            lvReport.setAdapter(adapterOrderList);
        } else {
            LAmountRepot.setVisibility(View.GONE);
            lvReport.setVisibility(View.GONE);
            Toast.makeText(SalesReportActivity.this, "لا يوجد بيعات!",
                    Toast.LENGTH_LONG).show();
        }}
        else if (flageGridOnClick.equals("gvProducts")){
            objectList = new ArrayList<>();
            OrderList = new ArrayList<>();
            orderProduct = new ArrayList<>();
            Log.d("jjp",productIdSelect+"");
            OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(SalesReportActivity.this);
            orderDBAdapter.open();
            orderProduct = orderDBAdapter.getOrderDetailsByIDproduct(productIdSelect);
            Log.d("fromCa",from.toString()+to.toString()+" "+orderProduct.toString());
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
                    Log.d("testdate",d1+ ""+d2+"  "+d3);
                    if(d1.before(d2)&&d1.after(d3)){
                        //&&new Date(DateConverter.toDate(o.getCreatedAt())).compareTo(new Date(DateConverter.toDate(to.getDate())))<0
                        OrderList.add(o);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }



          //  OrderList = orderDBAdapter1.getBetweenByOrder(from.getTime(), to.getTime(), orderProduct);
            Log.d("OrderList", OrderList.toString());
            if (OrderList.size() > 0) {
                gvCategory.setVisibility(View.GONE);
                gvProducts.setVisibility(View.GONE);
                etSearch.setVisibility(View.GONE);
                lvReport.setVisibility(View.VISIBLE);
                LAmountRepot.setVisibility(View.VISIBLE);


                for (int i1=0; i1<OrderList.size();i1++){
                    amountReport=amountReport+OrderList.get(i1).getTotalPrice();
                }
                amount.setText( amountReport+"");
                tvCountSale.setText(OrderList.size()+"");

                objectList.addAll(OrderList);
                adapterListProduct = new SaleManagementListViewAdapter(SalesReportActivity.this, R.layout.list_adapter_row_sales_management, objectList);
                lvReport.setAdapter(adapterListProduct);
            }
            else {
                LAmountRepot.setVisibility(View.GONE);
                lvReport.setVisibility(View.GONE);
                Toast.makeText(SalesReportActivity.this, "لا يوجد بيعات!",
                        Toast.LENGTH_LONG).show();
            }
        }}}

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

