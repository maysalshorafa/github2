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

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;

import java.util.ArrayList;
import java.util.List;

import POSSDK.POSSDK;

public class ReportZDetailsActivity extends Activity {
    Button btCancel,btPrint;
    POSSDK pos;
    Bitmap p;
    PrintTools pt;
    long from=0,to=0,id=0;
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
        }
        pt=new PrintTools(ReportZDetailsActivity.this);
        p=pt.createZReport(id,from,to,true);
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
    private void goHome(){
        Intent intent = new Intent(ReportZDetailsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(LogInActivity.LEADPOS_MAKE_A_REPORT, LogInActivity.LEADPOS_MAKE_A_REPORT);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goHome();
    }

    private List<Order> orderList(List<Sale> sales){
        List<Order> ol=new ArrayList<Order>();
        OrderDBAdapter orderDBAdapter=new OrderDBAdapter(this);
        orderDBAdapter.open();
        for (Sale s : sales) {
            ol.addAll(orderDBAdapter.getOrderBySaleID(s.getId()));
        }
        orderDBAdapter.close();
        return ol;
    }


    private void printAndOpenCashBox() {

    }


}
