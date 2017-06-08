package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.Backup.Backup;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BackupActivity extends Activity {

    private Button btnCancel,btnDepartments,btnSystem;
    private ListView lv;
    private Backup backup;

    ProductDBAdapter productDBAdapter;
    DepartmentDBAdapter departmentDBAdapter;

    Map<String,Integer> departmentMap=new HashMap<String,Integer>();
    List<String> departments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_backup);

        btnCancel = (Button) findViewById(R.id.backupActivity_btnCancel);
        btnDepartments = (Button) findViewById(R.id.backupActivity_btnDepartment);
        btnSystem = (Button) findViewById(R.id.backupActivity_btnSystem);
        lv = (ListView) findViewById(R.id.backupActivity_lvDepartments);
        final String folderName=String.format(new Locale("en"),Long.toString(new Date().getTime()));
        departments=new ArrayList<String>();



        departmentMap.put(getBaseContext().getString(R.string.all),0);

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
