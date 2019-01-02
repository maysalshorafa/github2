package com.pos.leaders.leaderspossystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthZReportView extends AppCompatActivity {
    private List<ZReport> zReportList;
    private ZReportDBAdapter zReportDBAdapter;
    private EmployeeDBAdapter userDBAdapter;
    Button btCancel,btPrint;
    Bitmap p;
    PrintTools pt;
    String str;
    Date from, to;
    EditText etFrom, etTo;
    private static final int DIALOG_FROM_DATE = 825;
    private static final int DIALOG_TO_DATE = 324;
    private final static int DAY_MINUS_ONE_SECOND = 86399999;
    ImageView imageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_month_zreport_view);

        TitleBar.setTitleBar(this);
        btCancel=(Button)findViewById(R.id.reportZDetails_btCancel);
        btPrint=(Button)findViewById(R.id.reportZDetails_btPrint);
        etFrom = (EditText) findViewById(R.id.zReportManagement_ETFrom);
        etTo = (EditText) findViewById(R.id.zReportManagement_ETTo);
        imageView=(ImageView)findViewById(R.id.reportZDetails_ivInvoice);

        etFrom.setFocusable(false);
        etFrom.setText(DateConverter.getBeforeMonth().split(" ")[0]);
        from = DateConverter.stringToDate(DateConverter.getBeforeMonth());
        etTo.setFocusable(false);
        etTo.setText(DateConverter.currentDateTime().split(" ")[0]);
        to = DateConverter.stringToDate(DateConverter.currentDateTime());

        etFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_FROM_DATE);
            }
        });

        etTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TO_DATE);
            }
        });

        //region Init
        zReportDBAdapter = new ZReportDBAdapter(this);
        userDBAdapter = new EmployeeDBAdapter(this);
        userDBAdapter.open();
        zReportDBAdapter.open();
        zReportList = new ArrayList<ZReport>();
        setDate();
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pt.PrintReport(p);
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        zReportDBAdapter.close();
        userDBAdapter.close();
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    private void setDate() {
        double totalAmount=0;
        double totalSales=0;
        double cashTotal=0;
        double checkTotal=0;
        double creditTotal=0;
        double totalPosSales=0;
        double invoiceAmount=0;
        double creditInvoiceAmount=0;
        double shekelAmount=0;
        double usdAmount=0;
        double eurAmount=0;
        double gbpAmount=0;
        double invoiceReceiptAmount=0;
        zReportDBAdapter.open();
        zReportList=new ArrayList<>();
        zReportList = zReportDBAdapter.getBetweenTwoDates(from.getTime(), to.getTime()+ DAY_MINUS_ONE_SECOND);
        if(zReportList.size()>0){
            imageView.setVisibility(View.VISIBLE);
            ZReport zReport = null;
            for(int i = 0 ; i<zReportList.size();i++){
            totalAmount+=zReportList.get(i).getTotalAmount();
            totalSales+=zReportList.get(i).getTotalSales();
            cashTotal+=zReportList.get(i).getCashTotal();
            checkTotal+=zReportList.get(i).getCheckTotal();
            creditTotal+=zReportList.get(i).getCreditTotal();
            totalPosSales+=zReportList.get(i).getTotalPosSales();
            invoiceAmount+=zReportList.get(i).getInvoiceAmount();
            creditInvoiceAmount+=zReportList.get(i).getCreditInvoiceAmount();
            shekelAmount+=zReportList.get(i).getShekelAmount();
            usdAmount+=zReportList.get(i).getUsdAmount();
            eurAmount+=zReportList.get(i).getEurAmount();
            gbpAmount+=zReportList.get(i).getGbpAmount();
            invoiceReceiptAmount+=zReportList.get(i).getInvoiceReceiptAmount();

            }

        zReport=new ZReport(0,new Timestamp(System.currentTimeMillis()),zReportList.get(0).getByUser(),0,0,totalAmount,totalSales,cashTotal,checkTotal,creditTotal,totalPosSales,zReportList.get(0).getTax(),invoiceAmount,creditInvoiceAmount,shekelAmount,usdAmount,eurAmount,gbpAmount,invoiceReceiptAmount);
        pt=new PrintTools(MonthZReportView.this);
            p=pt.createMonthZReport(zReport,false,from,to);
       imageView.setImageBitmap(p);
        }else {
            imageView.setVisibility(View.INVISIBLE);
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
            etFrom.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            from = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
            view.setMaxDate(to.getTime());
            setDate();
        }
    };

    private DatePickerDialog.OnDateSetListener onToDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            etTo.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            to = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
            view.setMinDate(from.getTime());
            setDate();
        }
    };

}
