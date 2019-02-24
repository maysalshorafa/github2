package com.pos.leaders.leaderspossystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClosingReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.XReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ClosingReport;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.XReport;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.sql.Timestamp;

public class ReportsManagementActivity  extends AppCompatActivity {
    public static final String COM_LEADPOS_XREPORT_FORM = "COM_LEADPOS_XREPORT_FORM";
    public static final String COM_LEADPOS_XREPORT_TO = "COM_LEADPOS_XREPORT_TO";
    public static final String COM_LEADPOS_XREPORT_ID = "COM_LEADPOS_XREPORT_ID";
    public static final String COM_LEADPOS_XREPORT_FLAG = "COM_LEADPOS_XREPORT_FLAG";
    public static final String COM_LEADPOS_XREPORT_TOTAL_AMOUNT = "COM_LEADPOS_XREPORT_TOTAL_AMOUNT";
    public static final String COM_LEADPOS_XREPORT_AMOUNT = "COM_LEADPOS_XREPORT_AMOUNT";

    Button btnZ, btnZView,btnX, btnOrder,btnExFiles ,btnSalesMan , btnInvoice , btnClosingReport , btnMonthZReportView;

    ZReportDBAdapter zReportDBAdapter;
    OrderDBAdapter saleDBAdapter;

    ZReport lastZReport=null;
    Order lastSale;
    String str;
    double totalZReportAmount =0;
    OpiningReportDBAdapter opiningReportDBAdapter = new OpiningReportDBAdapter(ReportsManagementActivity.this);
    ClosingReportDBAdapter closingReportDBAdapter = new ClosingReportDBAdapter(ReportsManagementActivity.this);
    OpiningReport lastOpiningReport =null;
    ClosingReport closingReport=null;
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

        btnOrder =(Button)findViewById(R.id.reportManagementActivity_btnSaleManagement);
        btnExFiles = (Button) findViewById(R.id.reportManagementActivity_btnExtractingFiles);
        btnInvoice = (Button) findViewById(R.id.reportManagementActivity_btnInvoice);
        btnClosingReport = (Button) findViewById(R.id.reportManagementActivity_btnClosingReport);
        btnMonthZReportView = (Button)findViewById(R.id.reportManagementActivity_btnMonthZReportView);
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
        if(lastZReport==null) {btnX.setEnabled(false);}
        try {
            opiningReportDBAdapter.open();
            closingReportDBAdapter.open();
            lastOpiningReport = opiningReportDBAdapter.getLastRow();
            closingReport=closingReportDBAdapter.getLastRow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(closingReport!=null) {
            if (lastOpiningReport.getOpiningReportId() == closingReport.getOpiningReportId()) {
                btnClosingReport.setEnabled(false);
            } else {
                btnClosingReport.setEnabled(true);
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
                                OpiningReport opiningReport = Util.getLastAReport(getApplicationContext());
                                ClosingReportDBAdapter closingReportDBAdapter =new ClosingReportDBAdapter(getApplicationContext());
                                closingReportDBAdapter.open();
                                ClosingReport closingReport = closingReportDBAdapter.getClosingReportByOpiningReportId(opiningReport.getOpiningReportId());
                                if(closingReport!=null){
                                ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(ReportsManagementActivity.this);
                                zReportDBAdapter.open();
                                ZReport lastZReport = Util.getLastZReport(getApplicationContext());

                                if (lastZReport == null) {
                                    lastZReport = new ZReport();
                                    lastZReport.setEndOrderId(0);
                                }
                                ZReport z = new ZReport(0,  new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE, lastZReport.getEndOrderId() + 1, lastSale);
                                z.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                                double amount = zReportDBAdapter.getZReportAmount(z.getStartOrderId(), z.getEndOrderId());
                                    try {
                                        totalZReportAmount=zReportDBAdapter.getLastRow().getTotalPosSales()+amount;
                                    } catch (Exception e) {
                                        totalZReportAmount=amount;

                                        e.printStackTrace();
                                    }
                                    z.setInvoiceReceiptAmount(amount);
                                    z.setTotalAmount(amount);
                                    z.setTotalSales(amount);
                                    z.setTotalPosSales(totalZReportAmount);
                                ZReport zReport= Util.insertZReport(z,getApplicationContext());
                                Intent i = new Intent(ReportsManagementActivity.this, ReportZDetailsActivity.class);
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_ID, zReport.getzReportId());
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FORM, zReport.getStartOrderId());
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TO, zReport.getEndOrderId());
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TOTAL_AMOUNT,totalZReportAmount);
                                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_AMOUNT,amount);


                                startActivity(i);
                                btnZ.setEnabled(false);
                                }else {
                                    new AlertDialog.Builder(ReportsManagementActivity.this)
                                            .setTitle(getString(R.string.create_closing_report))
                                            .setMessage(getString(R.string.you_must_create_closing_report_before_z_report))
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent(ReportsManagementActivity.this, ClosingReportActivity.class);
                                                    startActivity(i);
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
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
                if (lastZReport!=null) {
                    double totalXreportAmount = 0;
                    XReportDBAdapter xReportDBAdapter = new XReportDBAdapter(ReportsManagementActivity.this);
                    xReportDBAdapter.open();
                    XReport x = new XReport(0, new Timestamp(System.currentTimeMillis()), SESSION._EMPLOYEE, lastZReport.getEndOrderId() + 1, lastSale);
                    x.setByUser(SESSION._EMPLOYEE.getEmployeeId());
                    double amount = xReportDBAdapter.getXReportAmount(x.getStartOrderId(), x.getEndOrderId());
                    try {
                        totalXreportAmount = xReportDBAdapter.getLastRow().getTotalPosSales() + amount;
                    } catch (Exception e) {
                        totalXreportAmount = amount;

                        e.printStackTrace();
                    }
                    x.setInvoiceReceiptAmount(amount);
                    x.setTotalAmount(amount);
                    x.setTotalSales(amount);
                    x.setTotalPosSales(totalXreportAmount);
                    XReport xReport = Util.insertXReport(x, getApplicationContext());
                    Intent i = new Intent(ReportsManagementActivity.this, ReportZDetailsActivity.class);
                    i.putExtra(COM_LEADPOS_XREPORT_ID, xReport.getxReportId());
                    i.putExtra(COM_LEADPOS_XREPORT_FORM, xReport.getStartOrderId());
                    i.putExtra(COM_LEADPOS_XREPORT_TO, xReport.getEndOrderId());
                    i.putExtra(COM_LEADPOS_XREPORT_TOTAL_AMOUNT, totalXreportAmount);
                    i.putExtra(COM_LEADPOS_XREPORT_AMOUNT, amount);
                    i.putExtra(COM_LEADPOS_XREPORT_FLAG, true);

                    startActivity(i);
                }

            }
        });
        btnMonthZReportView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsManagementActivity.this,MonthZReportView.class);
                startActivity(intent);
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
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, OrdersManagementActivity.class);
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
        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = {
                        getString(R.string.invoice),
                        getString(R.string.receipt),getString(R.string.order_document),getString(R.string.create_credit_invoice_doc),getString(R.string.view_credit_invoice_doc)

                };
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReportsManagementActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent intent = new Intent(ReportsManagementActivity.this, SalesCartActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(ReportsManagementActivity.this, InvoiceManagementActivity.class);
                                startActivity(intent1);
                                break;
                            case 2:

                                Intent intent2 = new Intent(ReportsManagementActivity.this, OrderDocumentManagementActivity.class);
                                startActivity(intent2);
                                break;
                            case 3:
                                Intent intent3 = new Intent(ReportsManagementActivity.this, CreateCreditInvoiceActivity.class);
                                startActivity(intent3);
                                break;
                            case 4:
                                Intent intent4= new Intent(ReportsManagementActivity.this, ViewCreditInvoiceActivity.class);
                                startActivity(intent4);
                                break;


                        }
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
        btnClosingReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, ClosingReportActivity.class);
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
