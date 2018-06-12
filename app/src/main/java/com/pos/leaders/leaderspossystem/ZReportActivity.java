package com.pos.leaders.leaderspossystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.ZReportListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ZReportActivity extends AppCompatActivity {
    public static final String COM_LEADPOS_ZREPORT_FORM = "COM_LEADPOS_ZREPORT_FORM";
    public static final String COM_LEADPOS_ZREPORT_TO = "COM_LEADPOS_ZREPORT_TO";
    public static final String COM_LEADPOS_ZREPORT_ID = "COM_LEADPOS_ZREPORT_ID";
    public static final String COM_LEADPOS_ZREPORT_HISTORY = "COM_LEADPOS_ZREPORT_HISTORY";
    public static final String COM_LEADPOS_ZREPORT_TOTAL_AMOUNT = "COM_LEADPOS_ZREPORT_TOTAL_AMOUNT";

    private ZReportListViewAdapter adapter;
    private List<ZReport> zReportList;
    private ZReportDBAdapter zReportDBAdapter;
    private EmployeeDBAdapter userDBAdapter;

    ListView lvReports;
    Button btCancel,btOk;
    DatePicker dpFrom,dpTo;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_zreport);

        TitleBar.setTitleBar(this);

        //region Init
        dpFrom = (DatePicker) findViewById(R.id.ZReportActivity_dpFrom);
        dpTo = (DatePicker) findViewById(R.id.ZReportActivity_dpTo);
        btCancel = (Button) findViewById(R.id.ZReportActivity_btnCancel);
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
        lvReports.setAdapter(adapter);

        //endregion init

        lvReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(ZReportActivity.this,ReportZDetailsActivity.class);
                i.putExtra("permissions_name",str);

                i.putExtra(COM_LEADPOS_ZREPORT_ID,zReportList.get(position).getzReportId());
                i.putExtra(COM_LEADPOS_ZREPORT_FORM,zReportList.get(position).getStartOrderId());
                i.putExtra(COM_LEADPOS_ZREPORT_TO,zReportList.get(position).getEndOrderId());
                i.putExtra(COM_LEADPOS_ZREPORT_TOTAL_AMOUNT,zReportList.get(position).getTotalAmount());

                i.putExtra(COM_LEADPOS_ZREPORT_HISTORY, true);
                startActivity(i);

                //finish();

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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            str = extras.getString("permissions_name");
        }

    }
}