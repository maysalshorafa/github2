package com.pos.leaders.leaderspossystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
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

import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepositAndPullReportDetailsDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DrawerDepositAndPullReportDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.Models.DepositAndPullDetailsReport;
import com.pos.leaders.leaderspossystem.Models.DepositAndPullReport;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.DepositAndPullListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DepositAndPullReportView  extends AppCompatActivity {
    private DepositAndPullListViewAdapter adapter;
    private List<DepositAndPullReport> depositAndPullReportList;
    private DrawerDepositAndPullReportDbAdapter depositAndPullReportDbAdapter;
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
        setContentView(R.layout.activity_deposit_and_pull_report_view);

        TitleBar.setTitleBar(this);

        TitleBar.setTitleBar(this);
        etFrom = (EditText) findViewById(R.id.depositAndPullReportManagement_ETFrom);
        etTo = (EditText) findViewById(R.id.depositAndPullManagement_ETTo);

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
        lvReports = (ListView) findViewById(R.id.depositAndPullActivity_lvReports);
        depositAndPullReportDbAdapter = new DrawerDepositAndPullReportDbAdapter(this);
        userDBAdapter = new EmployeeDBAdapter(this);
        userDBAdapter.open();
        depositAndPullReportDbAdapter.open();
        depositAndPullReportList = new ArrayList<DepositAndPullReport>();
        depositAndPullReportList = depositAndPullReportDbAdapter.getAll();
        depositAndPullReportDbAdapter.close();
      /*  for (DepositAndPullReport z:depositAndPullReportList){
            z.setByUserID(userDBAdapter.getEmployeeByID((int)z.getByUserID()));
        }*/
        userDBAdapter.close();
        adapter = new DepositAndPullListViewAdapter(this, R.layout.list_view_pull_and_deposit_report, depositAndPullReportList);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_head_row_deposit_and_pull_reports, lvReports, false);
        lvReports.addHeaderView(header, null, false);
        lvReports.setAdapter(adapter);//endregion init
       // setDate();

    }

    @Override
    protected void onDestroy() {
        depositAndPullReportDbAdapter.close();
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
        depositAndPullReportDbAdapter.open();
        depositAndPullReportList = depositAndPullReportDbAdapter.getBetweenTwoDates(from.getTime(), to.getTime()+ DAY_MINUS_ONE_SECOND);
        adapter = new DepositAndPullListViewAdapter(this, R.layout.list_view_pull_and_deposit_report, depositAndPullReportList);
        lvReports.setAdapter(adapter);
        lvReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ArrayList<String> hintForCurrencyType = new ArrayList<String>();
                final ArrayList<Double> hintForCurrencyAmount = new ArrayList<Double>();
                DepositAndPullReportDetailsDbAdapter depositAndPullReportDetailsDbAdapter = new DepositAndPullReportDetailsDbAdapter(DepositAndPullReportView.this);
                depositAndPullReportDetailsDbAdapter.open();
                List<DepositAndPullDetailsReport>depositAndPullDetailsReports=depositAndPullReportDetailsDbAdapter.getListDepositAndPullReportReport(depositAndPullReportList.get(position).getDepositAndPullReportId());
                for (int i=0;i<depositAndPullDetailsReports.size();i++){
                    hintForCurrencyAmount.add(depositAndPullDetailsReports.get(i).getAmount());
                    hintForCurrencyType.add(depositAndPullDetailsReports.get(i).getCurrencyType());
                }
                Util.pullAndDepositReport(DepositAndPullReportView.this,depositAndPullReportList.get(position),hintForCurrencyType,hintForCurrencyAmount,depositAndPullReportList.get(position).getType());
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

    }

