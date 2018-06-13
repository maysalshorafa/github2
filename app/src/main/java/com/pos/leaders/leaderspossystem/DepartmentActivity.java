package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Tools.DepartmentGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karam on 18/10/2016.
 */

public class DepartmentActivity extends AppCompatActivity {

    DepartmentDBAdapter departmentDBAdapter;
    EditText etDepartmentName;
    Button btAddDepartment, btnCancel;
    List<Department> listDepartment, filter_departmentList;
    GridView gvDepartment;

    DepartmentGridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.department_mangment);

        TitleBar.setTitleBar(this);

        // Get Refferences of Views
        gvDepartment = (GridView) findViewById(R.id.workerManagement_GVDEpartment);
        etDepartmentName = (EditText) findViewById(R.id.ETdepartmentName);
        btAddDepartment = (Button) findViewById(R.id.BTAddDepartment);
        btnCancel = (Button) findViewById(R.id.departmentActivity_btnCancel);
        makeList();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        gvDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String[] items = {
                        getString(R.string.edit),
                        getString(R.string.delete)};
                AlertDialog.Builder builder = new AlertDialog.Builder(DepartmentActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                intent = new Intent(DepartmentActivity.this, AddNewDepartment.class);
                                intent.putExtra("departmentID", listDepartment.get(position).getDepartmentId());
                                startActivity(intent);
                                break;
                            case 1:
                                new AlertDialog.Builder(DepartmentActivity.this)
                                        .setMessage(getString(R.string.delete))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                departmentDBAdapter.deleteEntry(listDepartment.get(position).getDepartmentId());
                                                listDepartment.remove(listDepartment.get(position));
                                                gvDepartment.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btAddDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepartmentActivity.this, AddNewDepartment.class);
                startActivity(intent);
            }
        });
        etDepartmentName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etDepartmentName.setFocusable(true);
            }
        });
        etDepartmentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gvDepartment.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter_departmentList = new ArrayList<Department>();
                String word = etDepartmentName.getText().toString();

                if (!word.equals("")) {
                    for (Department d : listDepartment) {

                        if (d.getName().toLowerCase().contains(word.toLowerCase())) {
                            filter_departmentList.add(d);

                        }
                    }
                } else {
                    filter_departmentList = listDepartment;
                }
                adapter = new DepartmentGridViewAdapter(getApplicationContext(), filter_departmentList);

                gvDepartment.setAdapter(adapter);
            }
        });
    }

    private void makeList() {
        // Department Data adapter
        departmentDBAdapter = new DepartmentDBAdapter(this);
        departmentDBAdapter.open();

        filter_departmentList = departmentDBAdapter.getAllDepartments();
        listDepartment = filter_departmentList;
        adapter = new DepartmentGridViewAdapter(this, listDepartment);
        gvDepartment.setAdapter(adapter);
    }
}