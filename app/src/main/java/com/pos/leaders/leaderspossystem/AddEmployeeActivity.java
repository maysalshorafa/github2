package com.pos.leaders.leaderspossystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.PermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeePermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Permission.Permissions;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Tools.PermissionsGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;

/**
 * Created by KARAM on 22/10/2016.
 */

public class AddEmployeeActivity extends AppCompatActivity {

    EditText etUserName, etPassword, etREPassword, etFirstName, etLastName, etPhoneNumber, etPresent, etHourlyWage;
    Button btAdd, btCancel;
    ListView lvPermissions;
    EmployeeDBAdapter userDBAdapter;
    Employee user;
    final List<View> selectedViews = new ArrayList<View>();
    List<String> selectedFromList = new ArrayList<String>();
    ;
    ArrayList<Integer> permissions_name;
    ArrayList<Integer> userPermissions;
    List<Permissions> userPermissionses;
    List<String> permissionName;
    Map<String, Long> permissionsMap = new HashMap<String, Long>();
    Spinner SpEmployeeBranch;
    List<Permissions> permissionsList = new ArrayList<Permissions>();
    boolean addPerm = true;
    int branchId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_users);


        TitleBar.setTitleBar(this);

        // Get Refferences of Views
        permissionName = new ArrayList<String>();
        etUserName = (EditText) findViewById(R.id.addUser_ETUserName);
        etUserName.setText("");
        etPassword = (EditText) findViewById(R.id.addUser_ETPassword);
        etREPassword = (EditText) findViewById(R.id.addUser_ETREPassword);
        etFirstName = (EditText) findViewById(R.id.addUser_ETFirstName);
        etLastName = (EditText) findViewById(R.id.addUser_ETLastName);
        etPhoneNumber = (EditText) findViewById(R.id.addUser_ETPhoneNumber);
        etPresent = (EditText) findViewById(R.id.addUser_ETPresent);
        etHourlyWage = (EditText) findViewById(R.id.addUser_ETHourlyWage);
        btAdd = (Button) findViewById(R.id.addUser_BTAdd);
        btCancel = (Button) findViewById(R.id.addUser_BTCancel);
        lvPermissions = (ListView) findViewById(R.id.addUser_LVPermissions);
        SpEmployeeBranch = (Spinner)findViewById(R.id.SpEmployeeBranch);
        final List<String> employeeBranch = new ArrayList<String>();
        employeeBranch.add(getString(R.string.all));
        employeeBranch.add(getString(R.string.pos_branch));
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, employeeBranch);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpEmployeeBranch.setAdapter(dataAdapter);
        userDBAdapter = new EmployeeDBAdapter(this);

        final EmployeePermissionsDBAdapter userPermissionsDBAdapter = new EmployeePermissionsDBAdapter(this);
        user = null;
        PermissionsDBAdapter permissionsDBAdapter = new PermissionsDBAdapter(this);
        permissionsDBAdapter.open();


        permissionsList = permissionsDBAdapter.getAllPermissions();
        permissionsDBAdapter.close();

        for (Permissions d : permissionsList) {
            permissionName.add(d.getName());
            permissionsMap.put(d.getName(), d.getId());
        }

        final PermissionsGridViewAdapter permissionsGridViewAdapter = new PermissionsGridViewAdapter(this, permissionsList);

        lvPermissions.setAdapter(permissionsGridViewAdapter);

        lvPermissions.setChoiceMode(CHOICE_MODE_MULTIPLE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long i = (long) extras.get("employeeId");
            if (EmployeeManagementActivity.User_Management_Edit == 8) {
                btAdd.setText(R.string.edit);
                EmployeeManagementActivity.User_Management_Edit =0;
            }
            if (EmployeeManagementActivity.User_Management_View == 7) {
                btAdd.setVisibility(View.GONE);
                etPassword.setEnabled(false);
                etREPassword.setEnabled(false);
                etFirstName.setEnabled(false);
                etUserName.setEnabled(false);
                etLastName.setEnabled(false);
                etPhoneNumber.setEnabled(false);
                etHourlyWage.setEnabled(false);
                etPresent.setEnabled(false);
                EmployeeManagementActivity.User_Management_View =0;
            }
            userDBAdapter.open();
            user = userDBAdapter.getEmployeeByID(i);
            userDBAdapter.close();
            if(user.getBranchId()==0){
                SpEmployeeBranch.setSelection(0);
            }else {SpEmployeeBranch.setSelection(1);}
            etUserName.setText(user.getEmployeeName());
            etPassword.setText(user.getPassword());
            etREPassword.setText(user.getPassword());
            etFirstName.setText(user.getFirstName());
            etLastName.setText(user.getLastName());
            etPhoneNumber.setText(user.getPhoneNumber());
            etPresent.setText(user.getPresent() + "");
            etHourlyWage.setText(user.getHourlyWage() + "");

            userPermissionsDBAdapter.open();
            userPermissions = userPermissionsDBAdapter.getPermissions(i);
            userPermissionsDBAdapter.close();
            userPermissionses = new ArrayList<>();
            permissionsDBAdapter.open();
            for (int p : userPermissions) {
                Permissions _p = permissionsDBAdapter.getPermission(p);
                userPermissionses.add(_p);
                for (Permissions per : permissionsList) {
                    if (per.getName().equals(_p.getName())) {
                        per.setChecked(true);
                    }
                }
            }
            permissionsDBAdapter.close();

            permissionsGridViewAdapter.updateRecords(permissionsList);
        }

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDBAdapter.open();
                String _userName = etUserName.getText().toString();
                String pass = etPassword.getText().toString();
                boolean availablePass =  userDBAdapter.availablePassWord(pass);
                boolean availableUserName =userDBAdapter.availableEmployeeName(_userName);
                Intent intent;
                if (user == null) {
                    if(SpEmployeeBranch.getSelectedItem().toString().equals(getString(R.string.all))){
                        branchId=0;
                    }else {
                        branchId= SETTINGS.branchId;
                    }
                    if (!_userName.equals("")&& !(etPassword.getText().toString().equals(""))) {
                        if (availablePass&& availableUserName) {
                             if (!(etPassword.getText().toString().equals(etREPassword.getText().toString()))) {
                                Toast.makeText(getApplicationContext(), getString(R.string.password_does_not_match), Toast.LENGTH_LONG).show();
                            } else if (etFirstName.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_first_name), Toast.LENGTH_LONG).show();
                            } else if (etPresent.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_present), Toast.LENGTH_LONG).show();
                            } else if (etHourlyWage.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_hourly_wage), Toast.LENGTH_LONG).show();
                            } else {
                                long i = userDBAdapter.insertEntry(etUserName.getText().toString(), etPassword.getText().toString(), etFirstName.getText().toString()
                                        , etLastName.getText().toString(), etPhoneNumber.getText().toString()
                                        , Double.parseDouble(etPresent.getText().toString()), Double.parseDouble(etHourlyWage.getText().toString()),branchId);
                                if (i > 0) {
                                    EmployeePermissionsDBAdapter userPermissionAdapter = new EmployeePermissionsDBAdapter(AddEmployeeActivity.this);
                                    userPermissionAdapter.open();
                                    for (Permissions p : permissionsList) {
                                        if (p.isChecked()) {
                                            userPermissionAdapter.insertEntry(p.getId(), i);
                                        }
                                    }
                                    userPermissionAdapter.close();

                                    Toast.makeText(getApplicationContext(), getString(R.string.success_adding_new_user), Toast.LENGTH_LONG).show();
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    onBackPressed();
                                } else {
                                    Log.e("error", "can`t add user");
                                    Toast.makeText(getApplicationContext(), getString(R.string.can_not_add_user_please_try_again), Toast.LENGTH_SHORT).show();
                                    //// TODO: 17/10/2016 error with adding entity
                                }
                            }
                        } else {
                            if(!availableUserName){
                                etUserName.setText("");
                                etUserName.setBackgroundResource(R.drawable.backtext);
                                Toast.makeText(getApplicationContext(), getString(R.string.user_name_is_not_available_try_to_use_another_user_name), Toast.LENGTH_LONG).show();

                            }
                          if(!availablePass) {
                              etPassword.setText("");
                              etREPassword.setText("");
                            Toast.makeText(getApplicationContext(), getString(R.string.user_password_is_not_available_try_to_use_another_user_password), Toast.LENGTH_LONG).show();
                              etPassword.setBackgroundResource(R.drawable.backtext);
                              etREPassword.setBackgroundResource(R.drawable.backtext);

                          }
                        }
                    } else {
                        if (etPassword.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_set_password), Toast.LENGTH_LONG).show();
                            etPassword.setBackgroundResource(R.drawable.backtext);

                        }else if(_userName.equals("")){
                        Toast.makeText(getApplicationContext(), getString(R.string.user_name_is_empty), Toast.LENGTH_LONG).show();
                            etUserName.setBackgroundResource(R.drawable.backtext);

                        }
                    }
                } else {
                    // Edit mode
                    if (_userName != "") {
                        if (etFirstName.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_first_name), Toast.LENGTH_LONG).show();
                        } else if (etPresent.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_present), Toast.LENGTH_LONG).show();
                        } else if (etHourlyWage.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_hourly_wage), Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                if(SpEmployeeBranch.getSelectedItem().toString().equals(getString(R.string.all))){
                                    branchId=0;
                                }else {
                                    branchId= SETTINGS.branchId;
                                }

                                user.setEmployeeName(etUserName.getText().toString());
                                user.setFirstName(etFirstName.getText().toString());
                                user.setLastName(etLastName.getText().toString());
                                user.setPhoneNumber(etPhoneNumber.getText().toString());
                                user.setHourlyWage(Double.parseDouble(etHourlyWage.getText().toString()));
                                user.setPresent(Double.parseDouble(etPresent.getText().toString()));
                                user.setBranchId(branchId);
                                EmployeePermissionsDBAdapter userPermissionAdapter = new EmployeePermissionsDBAdapter(AddEmployeeActivity.this);
                                userPermissionAdapter.open();
                                for (int i = 0; i <= userPermissions.size() - 1; i++) {
                                    for (Permissions p : permissionsList) {
                                        if (p.getId() == userPermissions.get(i)) {
                                            if (!p.isChecked()) {
                                                userPermissionAdapter.deletePermissions(user.getEmployeeId(), (int) p.getId());

                                            }
                                        }
                                    }
                                }
                                for (int a = 0; a <= permissionsList.size() - 1; a++) {
                                    addPerm = true;
                                    Permissions p = permissionsList.get(a);
                                    if (p.isChecked()) {
                                        for (Permissions p1 : userPermissionses) {

                                            if (p1.getName().equals(p.getName())) {
                                                addPerm = false;
                                                break;

                                            }

                                        }
                                        if (addPerm) {
                                            userPermissionAdapter.insertEntry(p.getId(), user.getEmployeeId());
                                        }
                                    }

                                }
                                userPermissionAdapter.close();
                                userDBAdapter.updateEntry(user);
                                Log.i("success Edit", user.toString());
                                onBackPressed();
                            } catch (Exception ex) {
                                Log.e("error can`t edit 0user", ex.getMessage().toString());
                                Toast.makeText(getApplicationContext(), getString(R.string.can_not_edit_user_please_try_again), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEmployeeActivity.this, EmployeeManagementActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);             }
        });

        lvPermissions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                Permissions model = permissionsList.get(position);
                model.setChecked(!model.isChecked());
                permissionsList.set(position, model);

                permissionsGridViewAdapter.updateRecords(permissionsList);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //  if(keyCode==KeyEvent.KEYCODE_BACK)
        //   Toast.makeText(getContext(), "back press", Toast.LENGTH_LONG).show();

        return false;
        // Disable back button..............
    }
}