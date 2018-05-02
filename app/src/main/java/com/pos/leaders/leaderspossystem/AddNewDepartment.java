package com.pos.leaders.leaderspossystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.List;

public class AddNewDepartment extends AppCompatActivity {
    Button addNewDepartment, cancel;
    EditText departmentName;
    List<Department> listDepartment;
    DepartmentDBAdapter departmentDBAdapter;
    long departmentId;
    Department department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_new_department);
        TitleBar.setTitleBar(this);
        addNewDepartment = (Button) findViewById(R.id.addNewDepartment);
        cancel = (Button) findViewById(R.id.cancelAddDepartment);
        departmentName = (EditText) findViewById(R.id.etdepartmentName);
        departmentDBAdapter = new DepartmentDBAdapter(this);
        departmentDBAdapter.open();
        listDepartment = departmentDBAdapter.getAllDepartments();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //update case
            departmentId = (long) bundle.get("departmentID");
            department = departmentDBAdapter.getDepartmentByID(departmentId);
            addNewDepartment.setText(getString(R.string.update_department));
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addNewDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new department region
                if (department == null) {
                    //if empty input
                    if (departmentName.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), getString(R.string.please_insert_department_name), Toast.LENGTH_LONG).show();
                    } else {
                        //input with spaces
                        if (departmentName.getText().toString().contains(" ")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_department_name_with_out_spaces), Toast.LENGTH_LONG).show();

                        } else {
                            //test if unique name or not
                            boolean exist = false;
                            for (Department dep : listDepartment) {
                                if (dep.getName().equals(departmentName.getText().toString())) {
                                    exist = true;
                                    break;
                                }
                            }
                            //unique name
                            if (!exist) {
                                long check = departmentDBAdapter.insertEntry(departmentName.getText().toString(), SESSION._USER.getId());
                                if (check > 0) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.success_to_add_new_department), Toast.LENGTH_LONG).show();
                                    Log.i("success", "added department");
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.failed_to_add_new_department), Toast.LENGTH_LONG).show();
                                    Log.e("error", " adding department");
                                }
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(AddNewDepartment.this, DepartmentActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intent);
                            }
                            //not unique
                            else {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_another_department_name), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                //end

                //upDate region

                else {
                    //if empty input
                    if (departmentName.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), getString(R.string.please_insert_department_name), Toast.LENGTH_LONG).show();
                    } else {
                        //input with spaces
                        if (departmentName.getText().toString().contains(" ")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_department_name_with_out_spaces), Toast.LENGTH_LONG).show();

                        } else {
                            //test if unique name or not
                            boolean exist = false;
                            for (Department dep : listDepartment) {
                                if (dep.getName().equals(departmentName.getText().toString())) {
                                    exist = true;
                                    break;
                                }
                            }
                            if (!exist) {
                                //unique name
                                department.setName(departmentName.getText().toString());
                                departmentDBAdapter.updateEntry(department);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(AddNewDepartment.this, DepartmentActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                //not unique
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_another_department_name), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }
                //end
            }


        });

    }
}
