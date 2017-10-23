package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Reports.UserAttendanceReport;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.WorkerGridViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by KARAM on 25/10/2016.
 */

public class WorkerManagementActivity  extends AppCompatActivity {
	List<User> users;
	UserDBAdapter userDBAdapter;
	GridView gvUsers;
	Button btAddUser,btCancel;
	private static final int CHANGE_PASSWORD_DIALOG = 656;
	User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_worker_management);

		TitleBar.setTitleBar(this);

		gvUsers = (GridView) findViewById(R.id.workerManagement_GVWorkers);
		btAddUser = (Button) findViewById(R.id.workerManagement_BTNewUser);
		//btCancel = (Button) findViewById(R.id.workerManagement_BTCancel);

		//region Buttons

		btAddUser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WorkerManagementActivity.this, AddUserActivity.class);
				startActivity(intent);
			}
		});
/**
		btCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WorkerManagementActivity.this, MainActivity.class);
			//	intent.putExtra("permissions_name",permissions_name);
				startActivity(intent);
			}
		});
**/

		//endregion


		userDBAdapter = new UserDBAdapter(this);
		userDBAdapter.open();
		users = userDBAdapter.getAllUsers();
		user = null;

		final WorkerGridViewAdapter adapter = new WorkerGridViewAdapter(this, users);
		gvUsers.setAdapter(adapter);
		gvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				final String[] items = {
                        getString(R.string.view),
						getString(R.string.edit),
						getString(R.string.change_password),
						getString(R.string.delete),
						getString(R.string.report)};
				AlertDialog.Builder builder = new AlertDialog.Builder(WorkerManagementActivity.this);
				builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						Intent intent;
						switch (item) {
                            case 0:
                                intent = new Intent(WorkerManagementActivity.this, AddUserActivity.class);
                                intent.putExtra("ViewUserId", users.get(position).getId());
                                startActivity(intent);
							case 1:
								intent = new Intent(WorkerManagementActivity.this, AddUserActivity.class);
								intent.putExtra("userId", users.get(position).getId());
								startActivity(intent);
								break;
							case 2:
								user = users.get(position);
								_showDialog(CHANGE_PASSWORD_DIALOG);
								break;
							case 3:
								new AlertDialog.Builder(WorkerManagementActivity.this)
										.setTitle("Delete User")
										.setMessage("Are you want to delete this user?")
										.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												userDBAdapter.deleteEntry(users.get(position).getId());
												users.remove(users.get(position));
												gvUsers.setAdapter(adapter);
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
                            case 4:
								intent = new Intent(WorkerManagementActivity.this, UserAttendanceReport.class);
								intent.putExtra("userID", users.get(position).getId());
								startActivity(intent);
								//Toast.makeText(getApplicationContext(),"this function not available.",Toast.LENGTH_LONG).show();
								break;
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	private void _showDialog(int dialogID) {
		switch (dialogID) {
			case CHANGE_PASSWORD_DIALOG:
				//cash
				final Dialog changePasswordDialog = new Dialog(WorkerManagementActivity.this);
				changePasswordDialog.setTitle(R.string.change_password);
				changePasswordDialog.setContentView(R.layout.dialog_change_password);
				changePasswordDialog.show();
				final EditText etPassword = (EditText) changePasswordDialog.findViewById(R.id.dialogChangePassword_ETPassword);
				final EditText etRePassword = (EditText) changePasswordDialog.findViewById(R.id.dialogChangePassword_ETREPassword);
				Button btSave = (Button) changePasswordDialog.findViewById(R.id.dialogChangePassword_BTSave);
				btSave.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (etPassword.getText().toString().equals("")) {
							Toast.makeText(WorkerManagementActivity.this, "Please type new password.", Toast.LENGTH_LONG);
                            //Log.i("dialog_ETPassword", "empty");
                        } else if (!(etPassword.getText().toString().equals(etRePassword.getText().toString()))) {
							Toast.makeText(WorkerManagementActivity.this, "Password does`t match.", Toast.LENGTH_LONG);
                            //Log.i("dialog_ETPassword", "Password does`t match");
                        } else {
							user.setPassword(etPassword.getText().toString());
							userDBAdapter.updateEntry(user);
							Toast.makeText(WorkerManagementActivity.this, "Success, setting new password.", Toast.LENGTH_LONG);
							changePasswordDialog.cancel();
						}
					}
				});
				Button btCancel = (Button) changePasswordDialog.findViewById(R.id.dialogChangePassword_BTCancel);
				btCancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						changePasswordDialog.cancel();
					}
				});
				break;
		}
	}
}