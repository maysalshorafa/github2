package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Tools.DepartmentGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.WorkerGridViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Karam on 18/10/2016.
 */

public class DepartmentActivity extends AppCompatActivity {
	android.support.v7.app.ActionBar actionBar;

	DepartmentDBAdapter departmentDBAdapter;
	EditText etDepartmentName;
	Button btAddDepartment,btnCancel;
	//ListView lv;
	//ArrayAdapter<String> LAdapter;
	List<Department> listDepartment;
	GridView gvDepartment;
//	List<String> departmentsName;

	Department editableDepartment = null;
	View previousDepartmentView=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.department_mangment);


		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
				R.layout.title_bar,
				null);

		// Set up your ActionBar
		actionBar = getSupportActionBar();
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
		setContentView(R.layout.department_mangment);

		// Get Refferences of Views
		gvDepartment = (GridView) findViewById(R.id.workerManagement_GVDEpartment);
		etDepartmentName = (EditText) findViewById(R.id.ETdepartmentName);
		btAddDepartment = (Button) findViewById(R.id.BTAddDepartment);
	//	btNewDepartment=(Button)findViewById(R.id.BTNewDepartment);
		btnCancel=(Button)findViewById(R.id.departmentActivity_btnCancel);
		//lv = (ListView) findViewById(R.id.LVDepartment);

		makeList();

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		gvDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent,
									View v, int position, long id) {
				editableDepartment = listDepartment.get(position);
				if (previousDepartmentView != null)
					previousDepartmentView.setBackgroundColor(getResources().getColor(R.color.transparent));
				previousDepartmentView = v;
				v.setBackgroundColor(getResources().getColor(R.color.pressed_color));
				btAddDepartment.setText(getResources().getText(R.string.edit_department));
				addDepartmentOnClick(v);
			}
		});

		//// TODO: 22/10/2016  on select item on department list view
                /*
                btAddDepartment.setText("Edit Department");
                etDepartmentName.setText((String)parent.getItemAtPosition(position));


			}
		});

/**		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				String selectedItem = (String) parent.getItemAtPosition(position);

				editableDepartment=listDepartment.get(position);
				if(previousDepartmentView!=null)
					previousDepartmentView.setBackgroundColor(getResources().getColor(R.color.transparent));
				previousDepartmentView=v;
				v.setBackgroundColor(getResources().getColor(R.color.pressed_color));
				etDepartmentName.setText(selectedItem);
				btAddDepartment.setText(getResources().getText(R.string.edit_department));
				//// TODO: 22/10/2016  on select item on department list view
                /*
                btAddDepartment.setText("Edit Department");
                etDepartmentName.setText((String)parent.getItemAtPosition(position));

			}
		});**/

				btAddDepartment.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						addDepartmentOnClick(v);
					}
				});
				/**    btNewDepartment.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
				rest();
				}
				});**/
			}

	private void makeList(){

		// Department Data adapter
		departmentDBAdapter = new DepartmentDBAdapter(this);
		departmentDBAdapter.open();


	//	departmentsName = new ArrayList<String>();
		listDepartment = departmentDBAdapter.getAllDepartments();

		final DepartmentGridViewAdapter adapter = new DepartmentGridViewAdapter(this, listDepartment);
		gvDepartment.setAdapter(adapter);}

	/**	for (Department d : listDepartment) {
			departmentsName.add(d.getName());
			Log.i("departments", d.getName());
		}

		LAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, departmentsName);
		lv.setAdapter(LAdapter);
	}**/

	private void rest(){
		if(previousDepartmentView!=null)
			previousDepartmentView.setBackgroundColor(getResources().getColor(R.color.transparent));
		previousDepartmentView=null;
		editableDepartment=null;
		etDepartmentName.setText("");
		btAddDepartment.setText(getResources().getText(R.string.add_department));
	}

	private void addDepartmentOnClick(View v) {
		if (editableDepartment == null) {
			long check = departmentDBAdapter.insertEntry(etDepartmentName.getText().toString(), SESSION._USER.getId());
			if (check > 0) {
				Log.i("seccess", "added department");
			} else {
				Log.e("error", " addeing department");
			}
			rest();
			makeList();
		}
		else{
			if(etDepartmentName.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(),"Please insert department name.",Toast.LENGTH_LONG).show();
			}
			else {
				editableDepartment.setName(etDepartmentName.getText().toString());
				departmentDBAdapter.updateEntry(editableDepartment);
				rest();
				makeList();
			}
		}
	}

}