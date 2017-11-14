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
import com.pos.leaders.leaderspossystem.Models.User;
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
	final List<View> selectedViews=new ArrayList<View>();
List<String> selectedFromList=new  ArrayList<String>();;
	ArrayList<Integer> permissions_name;
	ArrayList<Integer> userPermissions;
	List<String> permissionName;
	Map<String,Long> permissionsMap=new HashMap<String,Long>();
	ArrayAdapter<String> LAdapter;
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
		permissions_name = getIntent().getIntegerArrayListExtra("permissions_name");
		permissionName=new ArrayList<String>();
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
		UserPermissionsDBAdapter userPermissionsDBAdapter=new UserPermissionsDBAdapter(this);
		userPermissionsDBAdapter.open();
		user = null;
		PermissionsDBAdapter permissionsDBAdapter = new PermissionsDBAdapter(this);
		permissionsDBAdapter.open();

		List<Permissions> permissionsList = new ArrayList<Permissions>();
		permissionsList = permissionsDBAdapter.getAllPermissions();

		for (Permissions d : permissionsList) {
			permissionName.add(d.getName());
			permissionsMap.put(d.getName(),d.getId());
		}

		LAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,permissionName);
		lvPermissions.setAdapter(LAdapter);
		lvPermissions.setChoiceMode(CHOICE_MODE_MULTIPLE);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			long i = (long) extras.get("userId");
			String btnName=extras.getString(WorkerManagementActivity.LEAD_POS_RESULT_INTENT_CODE_ADD_USER_ACTIVITY_BUTTON_ADD_USER_NAME);
			btAdd.setText(btnName);
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
			userPermissions	= userPermissionsDBAdapter.getPermissions(i);
				for (int permissionsNo = 0; permissionsNo <= lvPermissions.getAdapter().getCount(); permissionsNo++){
				lvPermissions.setSelection(userPermissions.get(permissionsNo));
					lvPermissions.getChildAt(userPermissions.get(permissionsNo)).setBackgroundColor(Color.BLUE);

				}
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
								long i = userDBAdapter.insertEntry(etUserName.getText().toString(), etPassword.getText().toString(), etFirstName.getText().toString()
										, etLastName.getText().toString(), etPhoneNumber.getText().toString()
										, Double.parseDouble(etPresent.getText().toString()), Double.parseDouble(etHourlyWage.getText().toString()) );
								if (i > 0) {
									UserPermissionsDBAdapter userPermissionAdapter=new UserPermissionsDBAdapter(AddUserActivity.this);
									userPermissionAdapter.open();
									for(int permissionNo=0;permissionNo<selectedFromList.size();permissionNo++) {
										userPermissionAdapter.insertEntry(permissionsMap.get(selectedFromList.get(permissionNo)), i);
									}
									Toast.makeText(getApplicationContext(), "success dding new user", Toast.LENGTH_LONG).show();

									Log.i("success", "adding new user");
									intent = new Intent(AddUserActivity.this, WorkerManagementActivity.class);
								//	intent.putExtra("permissions_name",selectedFromList);
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
									//user.setPermissionsName(selectedFromList);

									userDBAdapter.updateEntry(user);
									Log.i("success Edit", user.toString());
									intent = new Intent(AddUserActivity.this, WorkerManagementActivity.class);
									intent.putIntegerArrayListExtra("permissions_name",  permissions_name);
									startActivity(intent);
								} catch (Exception ex) {
									Log.e("error can`t edit 0user", ex.getMessage().toString());
									Toast.makeText(getApplicationContext(), "Can`t edit user please try again", Toast.LENGTH_SHORT).show();
								}
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
			//	intent.putExtra("permissions_name",user.getPermtionName());

				//userDBAdapter.close();
				startActivity(intent);
			}
		});

		lvPermissions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
				view.setSelected(true);
				selectedFromList.add(lvPermissions.getItemAtPosition(position).toString());

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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//  if(keyCode==KeyEvent.KEYCODE_BACK)
		//   Toast.makeText(getContext(), "back press", Toast.LENGTH_LONG).show();

		return false;
		// Disable back button..............
	}
}