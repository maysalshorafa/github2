package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import java.util.ArrayList;
import java.util.Date;


public class HomeActivity extends Activity {

	SharedPreferences prefs = null;

	EditText etUserName, etPassword;
	UserDBAdapter userDBAdapter;
	ScheduleWorkersDBAdapter scheduleWorkersDBAdapter;
	String str ;
	@Override
	protected void onResume() {
		super.onResume();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		 str = extras.getString(LogInActivity.LEADPOS_MAKE_A_REPORT);

		}
		if (prefs.getBoolean("firstrun", true)) {
			// Do first run stuff here then set 'firstrun' as false
			Toast.makeText(getApplicationContext(),"this is customerName first run",Toast.LENGTH_LONG);
			// using the following line to edit/commit prefs
			prefs.edit().putBoolean("firstrun", false).commit();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_home);
		prefs = getSharedPreferences("com.pos.leaders.leaderspossystem", MODE_PRIVATE);

		SESSION._ORDERS = new ArrayList<OrderDetails>();
		userDBAdapter = new UserDBAdapter(this);
		userDBAdapter.open();

		scheduleWorkersDBAdapter = new ScheduleWorkersDBAdapter(this);
		scheduleWorkersDBAdapter.open();

		// Get References of Views
		Button btLogIn = (Button) findViewById(R.id.button);
		Button btReg=(Button)findViewById(R.id.btReg);
		btReg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(HomeActivity.this,AddUserActivity.class);
				startActivity(intent);
			}
		});
		etUserName = (EditText) findViewById(R.id.ETUserName);
		etPassword = (EditText) findViewById(R.id.ETPassword);

		btLogIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				User u = userDBAdapter.logIn(etUserName.getText().toString(), etPassword.getText().toString());
				if (u != null) {
					SESSION._USER = new User(u);
					Log.i("HomeActivity", u.toString());
					Toast.makeText(getApplicationContext(), "Hello " + u.getFullName() + " !!", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(getApplicationContext(), OldDashBoard.class);

					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(LogInActivity.LEADPOS_MAKE_A_REPORT, str);
			//		intent.putExtra("permissions_name",u.getPermtionName());
					long scheduleID = scheduleWorkersDBAdapter.insertEntry(u.getId());
					SESSION._SCHEDULEWORKERS = new ScheduleWorkers(scheduleID, u.getId(), new Date().getTime(), new Date().getTime());
					/*
					String message = "hello";
                    intent.putExtra("customerName", message);
					*/
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "User name or password does`t exist!! try again.", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}