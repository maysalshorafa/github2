package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;

import java.util.ArrayList;
import java.util.List;

import POSSDK.POSSDK;

public class ReportZDetailsActivity extends Activity {
    Button btCancel,btPrint;
    POSSDK pos;
    Bitmap p;
    PrintTools pt;
    String str;
    long from=0,to=0,id=0;
    boolean goBack = false;
    double totalZReportAmount =0.0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_report_zdetails);


        btCancel=(Button)findViewById(R.id.reportZDetails_btCancel);
        btPrint=(Button)findViewById(R.id.reportZDetails_btPrint);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id=(long)extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_ID);
            from = (long) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_FORM);
            to = (long) extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_TO);
            totalZReportAmount =(double)extras.get(ZReportActivity.COM_LEADPOS_ZREPORT_TOTAL_AMOUNT);
            if (extras.containsKey(ZReportActivity.COM_LEADPOS_ZREPORT_HISTORY)) {
                goBack = extras.getBoolean(ZReportActivity.COM_LEADPOS_ZREPORT_HISTORY, false);

            }
        }
        pt=new PrintTools(ReportZDetailsActivity.this);
        p=pt.createZReport(id,from,to,true,totalZReportAmount);
        // p=pt.createXReport(id,from, SESSION._EMPLOYEE,new java.util.Date()); // testing xReport
        ((ImageView)findViewById(R.id.reportZDetails_ivInvoice)).setImageBitmap(p);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pt.PrintReport(p);
                goHome();
            }
        });
    }
    private void goHome() {
        if (!goBack) {
            Intent intent = new Intent(ReportZDetailsActivity.this, LogInActivity.class);
            intent.putExtra("permissions_name", str);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(LogInActivity.LEADPOS_MAKE_A_REPORT, LogInActivity.LEADPOS_MAKE_A_REPORT);
            startActivity(intent);
        }
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            str = extras.getString("permissions_name");


        }

    }

    @Override
    public void onBackPressed() {
        goHome();
    }

    private List<OrderDetails> orderList(List<Order> sales){
        List<OrderDetails> ol=new ArrayList<OrderDetails>();
        OrderDetailsDBAdapter orderDBAdapter=new OrderDetailsDBAdapter(this);
        orderDBAdapter.open();
        for (Order s : sales) {
            ol.addAll(orderDBAdapter.getOrderBySaleID(s.getOrderId()));
        }
        orderDBAdapter.close();
        return ol;
    }


    private void printAndOpenCashBox() {

    }


}