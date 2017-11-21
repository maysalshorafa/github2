package com.pos.leaders.leaderspossystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.PermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserPermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Permission.Permissions;
import com.pos.leaders.leaderspossystem.Models.Permission.UserPermissions;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Tools.PermissionsGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;

/**
 * Created by KARAM on 22/10/2016.
 */

public class AddUserActivity extends AppCompatActivity {

	EditText etUserName, etPassword, etREPassword, etFirstName, etLastName, etPhoneNumber, etPresent, etHourlyWage;
	Button btAdd, btCancel;
	ListView lvPermissions;
	UserDBAdapter userDBAdapter;
	User user;
	final List<View> selectedViews = new ArrayList<View>();
	List<String> selectedFromList = new  ArrayList<String>();;
	ArrayList<Integer> permissions_name;
	ArrayList<Integer> userPermissions;
	List<String> permissionName;
	Map<String,Long> permissionsMap=new HashMap<String,Long>();

    List<Permissions> permissionsList = new ArrayList<Permissions>();

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

		userDBAdapter = new UserDBAdapter(this);

		final UserPermissionsDBAdapter userPermissionsDBAdapter = new UserPermissionsDBAdapter(this);
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
			long i = (long) extras.get("userId");
			String btnName = extras.getString(WorkerManagementActivity.LEAD_POS_RESULT_INTENT_CODE_ADD_USER_ACTIVITY_BUTTON_ADD_USER_NAME);
			btAdd.setText(btnName);

			userDBAdapter.open();
			user = userDBAdapter.getUserByID(i);
			userDBAdapter.close();

			etUserName.setText(user.getUserName());
			etPassword.setText(user.getPassword());
			etPassword.setEnabled(false);
			etREPassword.setText(user.getPassword());
			etREPassword.setEnabled(false);
			etFirstName.setText(user.getFirstName());
			etLastName.setText(user.getLastName());
			etPhoneNumber.setText(user.getPhoneNumber());
			etPresent.setText(user.getPresent() + "");
			etHourlyWage.setText(user.getHourlyWage() + "");

			userPermissionsDBAdapter.open();
			userPermissions = userPermissionsDBAdapter.getPermissions(i);
			userPermissionsDBAdapter.close();
			List<Permissions> userPermissionses = new ArrayList<>();
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
				String _userName = etUserName.getText().toString();
				Intent intent;
                userDBAdapter.open();
				if (user == null) {
					if (!_userName.equals("")) {
						if (userDBAdapter.availableUserName(_userName)) {
							if (etPassword.getText().toString().equals("")) {
								Toast.makeText(getApplicationContext(), getString(R.string.please_set_password), Toast.LENGTH_LONG).show();
							} else if (!(etPassword.getText().toString().equals(etREPassword.getText().toString()))) {
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
										, Double.parseDouble(etPresent.getText().toString()), Double.parseDouble(etHourlyWage.getText().toString()));
								if (i > 0) {
									UserPermissionsDBAdapter userPermissionAdapter = new UserPermissionsDBAdapter(AddUserActivity.this);
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
							Toast.makeText(getApplicationContext(), getString(R.string.user_name_is_not_available_try_to_use_another_user_name), Toast.LENGTH_LONG).show();
						}
					}
					else {
                        Toast.makeText(getApplicationContext(), getString(R.string.user_name_is_empty), Toast.LENGTH_LONG).show();
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

								user.setUserName(etUserName.getText().toString());
								user.setFirstName(etFirstName.getText().toString());
								user.setLastName(etLastName.getText().toString());
								user.setPhoneNumber(etPhoneNumber.getText().toString());
								user.setHourlyWage(Double.parseDouble(etHourlyWage.getText().toString()));
								user.setPresent(Double.parseDouble(etPresent.getText().toString()));
								UserPermissionsDBAdapter userPermissionAdapter = new UserPermissionsDBAdapter(AddUserActivity.this);
								userPermissionAdapter.open();
								for (int i=0;i<=userPermissions.size()-1;i++){
									for (Permissions p : permissionsList) {
										if(p.getId()==userPermissions.get(i)){
											userPermissionAdapter.deletePermissions((int) p.getId());
										}
									}
								}
								for (Permissions p : permissionsList) {
									if (p.isChecked()) {
										userPermissionAdapter.insertEntry(p.getId(), user.getId());
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
				onBackPressed();
			}
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