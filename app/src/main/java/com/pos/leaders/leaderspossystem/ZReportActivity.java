package com.pos.leaders.leaderspossystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Printer.PrintTools;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.ZReportListViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZReportActivity extends AppCompatActivity {
    public static final String COM_LEADPOS_ZREPORT_FORM = "COM_LEADPOS_ZREPORT_FORM";
    public static final String COM_LEADPOS_ZREPORT_TO = "COM_LEADPOS_ZREPORT_TO";
    public static final String COM_LEADPOS_ZREPORT_ID = "COM_LEADPOS_ZREPORT_ID";
    public static final String COM_LEADPOS_ZREPORT_HISTORY = "COM_LEADPOS_ZREPORT_HISTORY";
    public static final String COM_LEADPOS_ZREPORT_TOTAL_AMOUNT = "COM_LEADPOS_ZREPORT_TOTAL_AMOUNT";
    public static final String COM_LEADPOS_ZREPORT_AMOUNT = "COM_LEADPOS_ZREPORT_AMOUNT";
    public static final String COM_LEADPOS_ZREPORT_FROM_DASH_BOARD = "COM_LEADPOS_ZREPORT_FROM_DASH_BOARD";

    private ZReportListViewAdapter adapter;
    private List<ZReport> zReportList;
    private ZReportDBAdapter zReportDBAdapter;
    private EmployeeDBAdapter userDBAdapter;

    ListView lvReports;
    String str;
    Date from, to;
    EditText etFrom, etTo;
    private static final int DIALOG_FROM_DATE = 825;
    private static final int DIALOG_TO_DATE = 324;
    private final static int DAY_MINUS_ONE_SECOND = 86399999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zreport);

        TitleBar.setTitleBar(this);
        etFrom = (EditText) findViewById(R.id.zReportManagement_ETFrom);
        etTo = (EditText) findViewById(R.id.zReportManagement_ETTo);

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
        lvReports = (ListView) findViewById(R.id.ZReportActivity_lvReports);
        zReportDBAdapter = new ZReportDBAdapter(this);
        userDBAdapter = new EmployeeDBAdapter(this);
        userDBAdapter.open();
        zReportDBAdapter.open();
        zReportList = new ArrayList<ZReport>();
        zReportList = zReportDBAdapter.getAll();
        zReportDBAdapter.close();
        for (ZReport z:zReportList){
            z.setUser(userDBAdapter.getEmployeeByID((int)z.getByUser()));
        }
        userDBAdapter.close();
        adapter = new ZReportListViewAdapter(this, R.layout.list_view_z_report, zReportList);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_z_reports, lvReports, false);
        lvReports.addHeaderView(header, null, false);
        lvReports.setAdapter(adapter);//endregion init
        setDate();

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            str = extras.getString("permissions_name");
        }

    }
    private void setDate() {
        zReportDBAdapter.open();
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

    private void print(Bitmap bitmap) {
        PrintTools printTools = new PrintTools(this);
        printTools.PrintReport(bitmap);
        /*POSInterfaceAPI posInterfaceAPI = new POSUSBAPI(SalesManagementActivity.this);

        int i = posInterfaceAPI.OpenDevice();
        POSSDK pos = new POSSDK(posInterfaceAPI);

        pos.textSelectCharSetAndCodePage(POSSDK.CharacterSetUSA, 15);
        pos.systemSelectPrintMode(0);
        pos.systemFeedLine(1);
        pos.imageStandardModeRasterPrint(bitmap, CONSTANT.PRINTER_PAGE_WIDTH);
        pos.systemFeedLine(3);
        pos.systemCutPaper(66, 0);

        posInterfaceAPI.CloseDevice();*/
    }
}