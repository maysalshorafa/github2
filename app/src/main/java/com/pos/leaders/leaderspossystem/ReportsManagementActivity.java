package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
<<<<<<< HEAD
import android.graphics.Bitmap;
=======
import android.graphics.drawable.ColorDrawable;
>>>>>>> mays-sameer
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.OpenFormat.BKMVDATA;
import com.pos.leaders.leaderspossystem.OpenFormat.INI;
import com.pos.leaders.leaderspossystem.OpenFormat.OpenFrmt;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import org.joda.time.DateTime;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportsManagementActivity  extends AppCompatActivity {
    Button btnZ, btnZView,btnX, btnSales,btnExFiles;
    android.support.v7.app.ActionBar actionBar;

    ZReportDBAdapter zReportDBAdapter;
    SaleDBAdapter saleDBAdapter;

    ZReport lastZReport=null;
    Sale lastSale;
String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.report_mangment);


        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.title_bar,
                null);

        // Set up your ActionBar
        actionBar = getSupportActionBar();
        // TODO: Remove the redundant calls to getSupportActionBar()
        //       and use variable actionBar instead
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        // You customization
        final int actionBarColor = getResources().getColor(R.color.primaryColor);
        actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));

        final TextView actionBarTitle = (TextView) findViewById(R.id.editText8);
        actionBarTitle.setText(format.format(ca.getTime()));
        final TextView actionBarSent = (TextView) findViewById(R.id.editText9);
        actionBarSent.setText("POSID  "+ SESSION.POS_ID_NUMBER);


        final TextView actionBarStaff = (TextView) findViewById(R.id.editText10);
        actionBarStaff.setText(SESSION._USER.getFullName());
        //region Init

        final TextView actionBarLocations = (TextView) findViewById(R.id.editText11);
        actionBarLocations.setText(" "+SESSION._USER.getPermtionName());
        btnZ = (Button) findViewById(R.id.reportManagementActivity_btnZ);
        btnZ.setText("Z " + getString(R.string.report));
        btnZView = (Button) findViewById(R.id.reportManagementActivity_btnZView);
        btnZView.setText("Z " + getString(R.string.report) + " " + getString(R.string.view));
        btnX=(Button)findViewById(R.id.reportManagementActivity_btnX);
        btnX.setText("X " + getString(R.string.report));

        btnSales =(Button)findViewById(R.id.reportManagementActivity_btnSaleManagement);
        btnExFiles = (Button) findViewById(R.id.reportManagementActivity_btnExtractingFiles);

        zReportDBAdapter = new ZReportDBAdapter(this);
        saleDBAdapter = new SaleDBAdapter(this);

        //endregion init

        zReportDBAdapter.open();
        saleDBAdapter.open();
        lastSale = saleDBAdapter.getLast();

        try {
            lastZReport = zReportDBAdapter.getLastRow();

            if (lastZReport.getEndSaleId() == lastSale.getId()){
                btnZ.setEnabled(false);
            }

            else{
                btnZ.setEnabled(true);
            }
        } catch (Exception ex) {
            Log.e(ex.getLocalizedMessage(), ex.getMessage());
        }
        btnZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //(long id, Date creationDate, User user, Sale startSale, Sale endSale)
                if(lastZReport == null) {
                    lastZReport = new ZReport();
                    lastZReport.setEndSaleId(0);
                }

                ZReport z=new ZReport(0,DateConverter.stringToDate(DateConverter.currentDateTime()) , SESSION._USER,lastZReport.getEndSaleId()+1,lastSale);
                z.setByUser(SESSION._USER.getId());
                long zID=zReportDBAdapter.insertEntry(z);
                z.setId(zID);
                lastZReport=new ZReport(z);

                PrintTools pt=new PrintTools(ReportsManagementActivity.this);

                //create and print z report
                Bitmap bmap = pt.createZReport(lastZReport.getId(), lastZReport.getStartSaleId(), lastZReport.getEndSaleId(), false);
                if(bmap!=null)
                    pt.PrintReport(bmap);

                Intent i=new Intent(ReportsManagementActivity.this,ReportZDetailsActivity.class);
                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_ID,lastZReport.getId());
                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_FORM,lastZReport.getStartSaleId());
                i.putExtra(ZReportActivity.COM_LEADPOS_ZREPORT_TO,lastZReport.getEndSaleId());
                startActivity(i);
                btnZ.setEnabled(false);

                //// TODO: 30/03/2017 check error
                //Backup backup = new Backup(ReportsManagementActivity.this, String.format(new Locale("en"), new Date().getTime() + ""));
                //backup.encBackupDB();

            }
        });
        btnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintTools pt=new PrintTools(ReportsManagementActivity.this);

                //create and print x report
                pt.PrintReport(pt.createXReport(lastZReport.getEndSaleId()+1,lastSale.getId(),SESSION._USER,new Date()));

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
                Intent intent=new Intent(ReportsManagementActivity.this,SalesManagementActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnExFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsManagementActivity.this, OpenFormatActivity.class);
                startActivity(intent);
            }
        });
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
    protected void onDestroy() {
        zReportDBAdapter.open();
        saleDBAdapter.open();
        super.onDestroy();
    }
}
