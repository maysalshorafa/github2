package com.pos.leaders.leaderspossystem.Reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SaleManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NumberOfSaleProductReport extends AppCompatActivity {
    TextView etFromDate, etToDate,amount,tvCountSale;
    ListView lvReport;
    Button cancleReport;

    Date from, to;
    int mYear, mMonth, mDay;

    long productID;

    List<Long> order;
    LayoutInflater inflater;

    SaleManagementListViewAdapter adapter;
    private static final int DIALOG_FROM_DATE = 825;
    private static final int DIALOG_TO_DATE = 324;
    OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(NumberOfSaleProductReport.this);

    List<Order> OrderList=new ArrayList<>();
    List<Object>objectList = new ArrayList<Object>();
    double amountReport=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_number_of_sale_product_report);
        TitleBar.setTitleBar(this);

        etFromDate = (TextView) findViewById(R.id.countOfSaleReport_ETFrom);
        etToDate = (TextView) findViewById(R.id.ucountOfSaleReport_ETTo);
        tvCountSale = (TextView) findViewById(R.id.countOfSaleReport_Count);
        lvReport = (ListView) findViewById(R.id.countOfSaleReport_LVReport);
        amount=(TextView) findViewById(R.id.amountOfSaleReport_amount);
        cancleReport=(Button)findViewById(R.id.countOfSaleReport_cancle);
        cancleReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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





        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productID = extras.getLong("productID");
            Log.d("productID", String.valueOf(productID));
            orderDBAdapter.open();
            order = orderDBAdapter.getOrderDetailsByIDproduct(productID);
            Log.d("orderDetialsAdapter", String.valueOf(order));
            OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(NumberOfSaleProductReport.this);
            orderDBAdapter1.open();
            OrderList = orderDBAdapter1.getBetweenByOrder(from.getTime(), to.getTime(), order);
            Log.d("OrderList", OrderList.toString());
            for (int i=0; i<OrderList.size();i++){
                amountReport=amountReport+OrderList.get(i).getTotalPrice();
            }
            amount.setText( amountReport+"");
            tvCountSale.setText(OrderList.size()+"");

            inflater = getLayoutInflater();
            ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_order, lvReport, false);
            lvReport.addHeaderView(header, null, false);
            objectList.addAll(OrderList);

            adapter = new SaleManagementListViewAdapter(this, R.layout.list_adapter_row_sales_management, objectList);
            lvReport.setAdapter(adapter);

        }


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
            setDate();
        }
    };

    private DatePickerDialog.OnDateSetListener onToDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            etToDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            to = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
            view.setMinDate(to.getTime());
            setDate();
        }
    };


    private void setDate() {
            orderDBAdapter.open();
             OrderList=new ArrayList<>();
             order=new ArrayList<>();
            objectList=new ArrayList<>();
            order = orderDBAdapter.getOrderDetailsByIDproduct(productID);
            Log.d("orderDetialsAdapter", String.valueOf(order));
            OrderDBAdapter orderDBAdapter1 = new OrderDBAdapter(NumberOfSaleProductReport.this);
            orderDBAdapter1.open();
        Log.d("OrderList",from.toString());
        Log.d("OrderList", to.toString());

        OrderList = orderDBAdapter1.getBetweenByOrder(from.getTime(), to.getTime(), order);
            Log.d("OrderList", OrderList.toString());
        for (int i=0; i<OrderList.size();i++){
            amountReport=amountReport+OrderList.get(i).getTotalPrice();
        }
        amount.setText( amountReport+"");
        tvCountSale.setText(OrderList.size()+"");

          /* LayoutInflater inflater = getLayoutInflater();
           ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_order, lvReport, false);
           lvReport.addHeaderView(header, null, false);*/

           objectList.addAll(OrderList);
        adapter = new SaleManagementListViewAdapter(this, R.layout.list_adapter_row_sales_management, objectList);
           lvReport.setAdapter(adapter);
    }
}
