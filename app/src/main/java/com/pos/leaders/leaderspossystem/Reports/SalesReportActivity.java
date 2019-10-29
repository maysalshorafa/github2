package com.pos.leaders.leaderspossystem.Reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
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
    Button btncancleReport;

    GridView gvCategory;
    CategoryDBAdapter categoryDBAdapter;
    List<Category> listCategory;
    List<Long> order;
    List<Order> OrderList=new ArrayList<>();
    ListView lvReport;
    Date from, to;
    double amountReport=0;
    Long CategoryId;
    ViewGroup  headerProduct;
    LinearLayout LAmountRepot;
    private static final int DIALOG_FROM_DATE = 825;
    private static final int DIALOG_TO_DATE = 324;
    CategoryGridViewAdapter adapter;
    SaleManagementListViewAdapter adapterOrderList;
    List<Object>objectList = new ArrayList<Object>();
    List<Long>productID=new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sales_report);
        TitleBar.setTitleBar(this);
        LayoutInflater inflater = getLayoutInflater();
         headerProduct = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_order, lvReport, false);

        etFromDate = (TextView) findViewById(R.id.SaleReport_ETFrom);
        etToDate = (TextView) findViewById(R.id.SaleReport_ETTo);
        tvCountSale = (TextView) findViewById(R.id.SaleReport_Count);
        amount=(TextView) findViewById(R.id.SaleReport_amount);
        btncancleReport=(Button)findViewById(R.id.SaleReport_cancle);
        gvCategory = (GridView) findViewById(R.id.Category_Report);
        lvReport = (ListView) findViewById(R.id.SaleReport_LVReport);
        LAmountRepot=(LinearLayout) findViewById(R.id.Linear_AmountReport);
        makeList();
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


        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                gvCategory.setVisibility(View.GONE);
                lvReport.setVisibility(View.VISIBLE);
                CategoryId=listCategory.get(position).getCategoryId();
                Log.d("CategoryId",CategoryId+"cat");
                setOrder();
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
    /*private void setProduct() {
        orderDBAdapter.open();
        OrderList.clear();
        objectList.clear();
        OrderList=new ArrayList<>();
        objectList=new ArrayList<>();
        amountReport=0;
        OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(SalesReportActivity.this);
        orderDBAdapter1.open();
        OrderList = orderDBAdapter1.getBetweenOrder(from.getTime(), to.getTime());
        Log.d("OrderList", OrderList.toString());
        for (int i=0; i<OrderList.size();i++){
            amountReport=amountReport+OrderList.get(i).getTotalPrice();
        }
        amount.setText( amountReport+"");
        tvCountSale.setText(OrderList.size()+"");

        objectList.addAll(OrderList);
        adapter = new SaleManagementListViewAdapter(SalesReportActivity.this, R.layout.list_adapter_row_sales_management, objectList);
    }*/

    /*private void setGategory() {
        CategoryList=new ArrayList<>();
        order=new ArrayList<>();
        OrderList=new ArrayList<>();
        objectList=new ArrayList<>();
        CategoryDBAdapter categoryDBAdapter = new CategoryDBAdapter(SalesReportActivity.this);
        categoryDBAdapter.open();
        CategoryList=categoryDBAdapter.getAllDepartments();
        for (int i=0;i<CategoryList.size();i++){
            CategoryID.add(CategoryList.get(i).getCategoryId());
            Log.d("CategoryID",CategoryList.get(i).getCategoryId()+"");
        }
        ProductDBAdapter productDBAdapter =new ProductDBAdapter(SalesReportActivity.this);
        productDBAdapter.open();
        productID=productDBAdapter.getProductByCategory(CategoryID);
        OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(SalesReportActivity.this);
        orderDBAdapter.open();
        order = orderDBAdapter.getOrderDetailsByListIDproduct(productID);
        OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(SalesReportActivity.this);
        orderDBAdapter1.open();
        OrderList = orderDBAdapter1.getBetweenByOrder(from.getTime(), to.getTime(), order);

        for (int i=0;i<OrderList.size();i++){
          if( OrderList.get(i).getOrders().get(i).getProductId()== OrderList.get(i+1).getOrders().get(i+1).getProductId()){
              OrderListFilter.add(OrderList.get(i));
              OrderListFilter.add(OrderList.get(i+1));
          }
        }

        adapterCategory = new SalesReportListViweAdapter(SalesReportActivity.this, R.layout.list_adapter_row_sales_report ,objectList);

}*/

    private void makeList() {
        // Category Data adapter
        categoryDBAdapter = new CategoryDBAdapter(this);
        categoryDBAdapter.open();

        listCategory = categoryDBAdapter.getAllDepartments();
        adapter = new CategoryGridViewAdapter(SalesReportActivity.this, listCategory);
        gvCategory.setAdapter(adapter);
    }

    private void setOrder() {
        objectList=new ArrayList<>();
        OrderList=new ArrayList<>();
        order=new ArrayList<>();
        productID=new ArrayList<>();
        ProductDBAdapter productDBAdapter =new ProductDBAdapter(SalesReportActivity.this);
        productDBAdapter.open();
        productID=productDBAdapter.getProductByCategory(CategoryId);
        OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(SalesReportActivity.this);
        orderDBAdapter.open();
        order = orderDBAdapter.getOrderDetailsByListIDproduct(productID);
        OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(SalesReportActivity.this);
        orderDBAdapter1.open();
        OrderList = orderDBAdapter1.getBetweenByOrder(from.getTime(), to.getTime(), order);
        if (OrderList.size()>0){
            lvReport.setVisibility(View.VISIBLE);
            LAmountRepot.setVisibility(View.VISIBLE);
            for (int i=0; i<OrderList.size();i++){
                amountReport=amountReport+OrderList.get(i).getTotalPrice();
            }
            amount.setText( amountReport+"");
            tvCountSale.setText(OrderList.size()+"");
        lvReport.addHeaderView( headerProduct, null, false);
        objectList.addAll(OrderList);
        adapterOrderList = new SaleManagementListViewAdapter(SalesReportActivity.this, R.layout.list_adapter_row_sales_management, objectList);
        lvReport.setAdapter(adapterOrderList);}
        else {
            LAmountRepot.setVisibility(View.GONE);
            lvReport.setVisibility(View.GONE);
        }
    }

}
