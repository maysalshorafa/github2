package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.Backup.Backup;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BackupActivity extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;

    private Button btnCancel,btnDepartments,btnSystem;
    private ListView lv;
    private Backup backup;

    ProductDBAdapter productDBAdapter;
    DepartmentDBAdapter departmentDBAdapter;

    Map<String,Long> departmentMap=new HashMap<String,Long>();
    List<String> departments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_backup);


        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                R.layout.title_bar,
                null);

        // Set up your ActionBar
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_LIST);
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

        final TextView actionBarLocations = (TextView) findViewById(R.id.editText11);
        actionBarLocations.setText(" "+SESSION._USER.getPermtionName());


        btnCancel = (Button) findViewById(R.id.backupActivity_btnCancel);
        btnDepartments = (Button) findViewById(R.id.backupActivity_btnDepartment);
        btnSystem = (Button) findViewById(R.id.backupActivity_btnSystem);
        lv = (ListView) findViewById(R.id.backupActivity_lvDepartments);
        final String folderName=String.format(new Locale("en"),Long.toString(new Date().getTime()));
        departments=new ArrayList<String>();



        departmentMap.put(getBaseContext().getString(R.string.all),0L);

        departments.add(getBaseContext().getString(R.string.all));
        productDBAdapter=new ProductDBAdapter(this);
        departmentDBAdapter=new DepartmentDBAdapter(this);
        departmentDBAdapter.open();
        for (Department d :
                departmentDBAdapter.getAllDepartments()) {
            departmentMap.put(d.getName(),d.getId());
            departments.add(d.getName());
        }
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,departments);
        lv.setAdapter(adapter);




        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backup=new Backup(BackupActivity.this,folderName);
                backup.encBackupDB();
                Toast.makeText(BackupActivity.this, getBaseContext().getString(R.string.done)+" ", Toast.LENGTH_SHORT).show();
            }
        });
        btnDepartments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backup=new Backup(BackupActivity.this,folderName);
                backup.backupDepartment();
                Toast.makeText(BackupActivity.this, getBaseContext().getString(R.string.done)+" ", Toast.LENGTH_SHORT).show();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ProgressDialog progressDialog=new ProgressDialog(BackupActivity.this);
                progressDialog.setTitle(getBaseContext().getString(R.string.backup));
                progressDialog.setMessage(getBaseContext().getString(R.string.wait_for_finish));
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected void onPreExecute() {
                        progressDialog.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        if(position==0){
                            backup.backupProducts();
                        }
                        else{
                            departmentDBAdapter.open();
                            Department d=departmentDBAdapter.getDepartmentByID(departmentMap.get(departments.get(position)));
                            departmentDBAdapter.close();
                            backup=new Backup(BackupActivity.this,folderName);
                            backup.backupProductOnDepartment(d);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        progressDialog.dismiss();
                        Toast.makeText(BackupActivity.this, getBaseContext().getString(R.string.done)+" ", Toast.LENGTH_SHORT).show();
                    }
                }.execute();
                //// TODO: 16/01/2017 flag
            }
        });








    }


}
