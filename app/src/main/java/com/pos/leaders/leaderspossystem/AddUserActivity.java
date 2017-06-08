package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.PermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Permissions;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Tools.PermissionsGridViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KARAM on 22/10/2016.
 */

public class AddUserActivity extends Activity {

	EditText etUserName, etPassword, etREPassword, etFirstName, etLastName, etPhoneNumber, etPresent, etHourlyWage;
	Button btAdd, btCancel;
	ListView lvPermissions;
	UserDBAdapter userDBAdapter;
	User user;
	final List<View> selectedViews=new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_add_users);

		// Get Refferences of Views
		etUserName = (EditText) findViewById(R.id.addUser_ETUserName);
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
		userDBAdapter.open();

		user = null;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int i = (int) extras.get("userId");
			user = userDBAdapter.getUserByID(i);
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
			btAdd.setText(getResources().getText(R.string.edit));
			//The key argument here must match that used in the other activity
		}

		btAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String _userName = etUserName.getText().toString();
				Intent intent;
				if (user == null) {
					if (_userName != "") {
						if (userDBAdapter.availableUserName(_userName)) {
							if (etPassword.getText().toString().equals("")) {
								Toast.makeText(getApplicationContext(), "Please set password", Toast.LENGTH_LONG).show();
							} else if (!(etPassword.getText().toString().equals(etREPassword.getText().toString()))) {
								Toast.makeText(getApplicationContext(), "Password does`t match", Toast.LENGTH_LONG).show();
							} else if (etFirstName.getText().toString().equals("")) {
								Toast.makeText(getApplicationContext(), "Please insert first name", Toast.LENGTH_LONG).show();
							} else if (etPresent.getText().toString().equals("")) {
								Toast.makeText(getApplicationContext(), "Please insert present", Toast.LENGTH_LONG).show();
							} else if (etHourlyWage.getText().toString().equals("")) {
								Toast.makeText(getApplicationContext(), "Please insert hourly wage", Toast.LENGTH_LONG).show();
							} else {
								int i = userDBAdapter.insertEntry(etUserName.getText().toString(), etPassword.getText().toString(), etFirstName.getText().toString()
										, etLastName.getText().toString(), etPhoneNumber.getText().toString()
										, Double.parseDouble(etPresent.getText().toString()), Double.parseDouble(etHourlyWage.getText().toString()));
								if (i == 1) {
									Log.i("success", "adding new user");
									intent = new Intent(AddUserActivity.this, WorkerManagementActivity.class);
									startActivity(intent);
									//// TODO: 17/10/2016 sucess to add entity
								} else {
									Log.e("error", "can`t add user");
									Toast.makeText(getApplicationContext(), "Can`t add user please try again", Toast.LENGTH_SHORT).show();
									//// TODO: 17/10/2016 error with adding entity
								}
							}
						} else {
							Toast.makeText(getApplicationContext(), "User name is not available, try to use another user name", Toast.LENGTH_LONG).show();
						}
					}
				} else {
					// Edit mode
					if (_userName != "") {
						if ((userDBAdapter.availableUserName(_userName)) || _userName == user.getUserName()) {
							if (etFirstName.getText().toString().equals("")) {
								Toast.makeText(getApplicationContext(), "Please insert first name", Toast.LENGTH_LONG).show();
							} else if (etPresent.getText().toString().equals("")) {
								Toast.makeText(getApplicationContext(), "Please insert present", Toast.LENGTH_LONG).show();
							} else if (etHourlyWage.getText().toString().equals("")) {
								Toast.makeText(getApplicationContext(), "Please insert hourly wage", Toast.LENGTH_LONG).show();
							} else {
								try {
									user.setUserName(etUserName.getText().toString());
									user.setFirstName(etFirstName.getText().toString());
									user.setLastName(etLastName.getText().toString());
									user.setPhoneNumber(etPhoneNumber.getText().toString());
									user.setHourlyWage(Double.parseDouble(etHourlyWage.getText().toString()));
									user.setPresent(Double.parseDouble(etPresent.getText().toString()));
									userDBAdapter.updateEntry(user);
									Log.i("success Edit", user.toString());
									intent = new Intent(AddUserActivity.this, WorkerManagementActivity.class);
									startActivity(intent);
								} catch (Exception ex) {
									Log.e("error can`t edit 0user", ex.getMessage().toString());
									Toast.makeText(getApplicationContext(), "Can`t edit user please try again", Toast.LENGTH_SHORT).show();
								}
							}
						} else {
							Toast.makeText(getApplicationContext(), "User name is not available, try to use another user name", Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		});

		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//// TODO: 22/10/2016 cancel and return to previous activity
				Intent intent = new Intent(AddUserActivity.this, WorkerManagementActivity.class);
				//userDBAdapter.close();
				startActivity(intent);
			}
		});

		PermissionsDBAdapter permissionsDBAdapter = new PermissionsDBAdapter(this);
		permissionsDBAdapter.open();

		List<Permissions> permissionsList = new ArrayList<Permissions>();
		//permissionsList = permissionsDBAdapter.getAllPermissions();
		final String perList[]=getResources().getStringArray(R.array.permissions_list);
		for (String s : perList) {
			permissionsList.add(new Permissions(s));
		}


		ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,perList);
		PermissionsGridViewAdapter adapter = new PermissionsGridViewAdapter(this, permissionsList);
		lvPermissions.setAdapter(arrayAdapter);
		lvPermissions.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		lvPermissions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(selectedViews.contains(view)){
					selectedViews.remove(view);
					view.setBackgroundColor(getResources().getColor(R.color.transparent));
				}
				else
				{
					selectedViews.add(view);
					view.setBackgroundColor(getResources().getColor(R.color.pressed_color));
				}
				for (int i = 0; i < lvPermissions.getChildCount(); i++) {
					if(selectedViews.contains(lvPermissions.getChildAt(i))){
						lvPermissions.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.pressed_color));
					}
					else
					{
						lvPermissions.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.transparent));
					}
				}
			}
		});
	}
}