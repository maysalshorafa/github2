package com.pos.leaders.leaderspossystem.Reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.ReportZDetailsActivity;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.ZReportListViewAdapter;
import com.pos.leaders.leaderspossystem.ZReportActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NumberOfSaleProductReport extends AppCompatActivity {
    TextView etFromDate, etToDate;
    TextView tvCountSale;
    ListView lvReport;
    Button print;

    Date from, to;
    int mYear, mMonth, mDay;

    long productID;

    List<Long> order;
    private static final int DIALOG_FROM_DATE = 825;
    private static final int DIALOG_TO_DATE = 324;
    OrderDetailsDBAdapter orderDBAdapter = new OrderDetailsDBAdapter(NumberOfSaleProductReport.this);

    List<Order> OrderList=new ArrayList<>();
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
        print=(Button)findViewById(R.id.countOfSaleReport_print);

        //region Date
        Date d = new Date();
        Log.i("current date", d.toString());
        //Mon Oct 24 08:45:01 GMT 2016
        //("yyyy-mm-dd hh:mm:ss")
        mYear = Integer.parseInt(d.toString().split(" ")[5]);
        mMonth = d.getMonth();
        mDay = Integer.parseInt(d.toString().split(" ")[2]);


        etFromDate.setText(DateConverter.getBeforeMonth().split(" ")[0]);
        from = DateConverter.stringToDate(DateConverter.getBeforeMonth());
        etFromDate.setFocusable(false);
        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_FROM_DATE);
            }
        });
        etToDate.setText(DateConverter.currentDateTime().split(" ")[0]);
        to = DateConverter.stringToDate(DateConverter.currentDateTime());
        etToDate.setFocusable(false);
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
            OrderDBAdapter orderDBAdapter1=new OrderDBAdapter(NumberOfSaleProductReport.this);
            orderDBAdapter1.open();
            OrderList=orderDBAdapter1.getBetweenByOrder(from,to,order);
            Log.d("OrderList",OrderList.toString() );

        }


    }
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_FROM_DATE) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, onFromDateSetListener, Integer.parseInt(from.toString().split(" ")[5]), from.getMonth(),  Integer.parseInt(from.toString().split(" ")[2]));
            //datePickerDialog.getDatePicker().setMaxDate(to.getTime());
            datePickerDialog.getDatePicker().setCalendarViewShown(false);
            return datePickerDialog;
        } else if (id == DIALOG_TO_DATE) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, onToDateSetListener, Integer.parseInt(to.toString().split(" ")[5]), to.getMonth(),  Integer.parseInt(to.toString().split(" ")[2]));
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
            setDate();
        }
    };

    private DatePickerDialog.OnDateSetListener onToDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            etToDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            to = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
            view.setMinDate(from.getTime());
            setDate();
        }
    };

    private void setDate() {
        orderDBAdapter.open();
        zReportList = zReportDBAdapter.getBetweenTwoDates(from.getTime(), to.getTime()+ DAY_MINUS_ONE_SECOND);
        adapter = new ZReportListViewAdapter(this, R.layout.list_view_z_report, zReportList);
        lvReports.setAdapter(adapter);
        lvReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ZReportActivity.this, ReportZDetailsActivity.class);
//                                                            /* i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_ID, zReport.getzReportId());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FORM, zReport.getStartOrderId());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TO, zReport.getEndOrderId());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TOTAL_AMOUNT,zReport.getTotalSales());
//                                                             i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_AMOUNT,zReport.getTotalAmount());
//                                                            i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FROM_DASH_BOARD,true);*/
                i.putExtra("ObjectZReport",zReportList.get(position-1));
                i.putExtra(COM_LEADPOS_ZREPORT_HISTORY, true);
                startActivity(i);

                //finish();

            }
        });

    }
}
