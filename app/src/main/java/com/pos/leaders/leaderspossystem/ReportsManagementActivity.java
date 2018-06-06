package com.pos.leaders.leaderspossystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.Date;

public class ReportsManagementActivity  extends AppCompatActivity {
    Button btnZ, btnZView,btnX, btnSales,btnExFiles ,btnSalesMan;

    ZReportDBAdapter zReportDBAdapter;
    OrderDBAdapter saleDBAdapter;

    ZReport lastZReport=null;
    Order lastSale;
    String str;
    double totalZReportAmount =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.report_mangment);

        TitleBar.setTitleBar(this);

        btnZ = (Button) findViewById(R.id.reportManagementActivity_btnZ);
        btnSalesMan=(Button)findViewById(R.id.reportManagementActivity_btnSalesManReport);
        btnZ.setText("Z " + getString(R.string.report));
        btnZView = (Button) findViewById(R.id.reportManagementActivity_btnZView);
        btnZView.setText("Z " + getString(R.string.report) + " " + getString(R.string.view));
        btnX=(Button)findViewById(R.id.reportManagementActivity_btnX);
        btnX.setText("X " + getString(R.string.report));

        btnSales =(Button)findViewById(R.id.reportManagementActivity_btnSaleManagement);
        btnExFiles = (Button) findViewById(R.id.reportManagementActivity_btnExtractingFiles);

        zReportDBAdapter = new ZReportDBAdapter(this);
        saleDBAdapter = new OrderDBAdapter(this);

        //endregion init

        zReportDBAdapter.open();
        saleDBAdapter.open();
        lastSale = saleDBAdapter.getLast();
        if (lastSale == null) {
            btnZ.setEnabled(false);
            btnX.setEnabled(false);
        }
        else {

            try {
                lastZReport = zReportDBAdapter.getLastRow();

                if (lastZReport.getEndOrderId() == lastSale.getOrderId()) {
                    btnZ.setEnabled(false);
                } else {
                    btnZ.setEnabled(true);
                }
                // dis enable X Report if no row insert in ZReport Table
                if (lastZReport == null) {
                    btnX.setEnabled(false);
                }

            } catch (Exception ex) {
                Log.e(ex.getLocalizedMessage(), ex.getMessage());
            }
        }
        btnZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ReportsManagementActivity.this)
                        .setTitle(getString(R.string.create_z_report))
                        .setMessage(getString(R.string.create_z_report_message))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //(long id, Date creationDate, User user, Sale startSale, Sale endSale)
                                if(lastZReport == null) {
                                    lastZReport = new ZReport();
                                    lastZReport.setEndOrderId(0);
                                }

                                ZReport z=new ZReport(0,new Date().getTime() , SESSION._USER,lastZReport.getEndOrderId()+1,lastSale);
                                z.setByUser(SESSION._USER.getUserId());
                                double amount = zReportDBAdapter.getZReportAmount(z.getStartOrderId(), z.getEndOrderId());
                                totalZReportAmount+=LogInActivity.LEADPOS_MAKE_Z_REPORT_TOTAL_AMOUNT+amount;
                                long zID = zReportDBAdapter.insertEntry(z.getCreatedAt(), z.getByUser(), z.getStartOrderId(), z.getEndOrderId(),amount,totalZReportAmount);
                                z.setzReportId(zID);
                                lastZReport = new ZReport(z);

                                PrintTools pt = new PrintTools(ReportsManagementActivity.this);

                                //create and print z report
                                Bitmap bmap = pt.createZReport(lastZReport.getzReportId(), lastZReport.getStartOrderId(), lastZReport.getEndOrderId(), false,totalZReportAmount);
                                if(bmap!=null)
                                    pt.PrintReport(bmap);

                                Intent i=new Intent(ReportsManagementActivity.this,ReportZDetailsActivity.class);
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_ID,lastZReport.getzReportId());
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FORM,lastZReport.getStartOrderId());
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TO,lastZReport.getEndOrderId());
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TOTAL_AMOUNT,totalZReportAmount);
                                startActivity(i);
                                btnZ.setEnabled(false);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


                //// TODO: 30/03/2017 check error
                //Backup backup = new Backup(ReportsManagementActivity.this, String.format(new Locale("en"), new Date().getTime() + ""));
                //backup.encBackupDB();

            }
        });
        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintTools pt=new PrintTools(ReportsManagementActivity.this);
                if (lastZReport!=null){ // test if have at least i row in ZReportTable Then make XReport
                Bitmap bmap = pt.createXReport(lastZReport.getEndOrderId()+1,lastSale.getOrderId(),SESSION._USER,new Date());

                    pt.PrintReport(bmap);
                    //create and print x report
/** test xReport use zActivity
                    Intent i=new Intent(ReportsManagementActivity.this,ReportZDetailsActivity.class);
                    i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_ID,lastZReport.getCashPaymentId());
                    i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FORM,lastZReport.getStartOrderId());
                    i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TO,lastZReport.getEndOrderId());
                    startActivity(i);**/}


            }
        });
        btnZView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsManagementActivity.this,ZReportActivity.class);
                intent.putExtra("permissions_name",str);

                startActivity(intent);
            }
        });
        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, SalesManagementActivity.class);
                startActivity(intent);
            }
        });

        btnExFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, OpenFormatActivity.class);
                startActivity(intent);
            }
        });
        btnSalesMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, SalesManManagementActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
      /**  if (extras != null) {
            str = extras.getString("permissions_name");


        }**/

    }

    @Override
    protected void onDestroy() {
        zReportDBAdapter.open();
        saleDBAdapter.open();
        super.onDestroy();
    }
}
