package com.pos.leaders.leaderspossystem;

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

import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Tools.DepartmentGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Karam on 18/10/2016.
 */

public class DepartmentActivity extends AppCompatActivity {

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

		TitleBar.setTitleBar(this);

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
					previousDepartmentView.setBackground(getResources().getDrawable(R.drawable.bt_normal));
				previousDepartmentView = v;
                v.setBackground(getResources().getDrawable(R.drawable.bt_normal_pressed));
                btAddDepartment.setText(getResources().getText(R.string.edit_department));
				addDepartmentOnClick(v);
			}
		});

        btAddDepartment.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						addDepartmentOnClick(v);
					}
				});
	}

	private void makeList(){
		// Department Data adapter
		departmentDBAdapter = new DepartmentDBAdapter(this);
		departmentDBAdapter.open();

		listDepartment = departmentDBAdapter.getAllDepartments();

		final DepartmentGridViewAdapter adapter = new DepartmentGridViewAdapter(this, listDepartment);
		gvDepartment.setAdapter(adapter);
	}

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
			if(etDepartmentName.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(), getString(R.string.please_insert_department_name), Toast.LENGTH_LONG).show();
			}
			else {
				boolean exist = false;
				for (Department dep : listDepartment) {
					if(dep.getName().equals(etDepartmentName.getText().toString())){
						exist = true;
						break;
					}
				}
				if(!exist) {
					long check = departmentDBAdapter.insertEntry(etDepartmentName.getText().toString(), SESSION._USER.getId());
					if (check > 0) {
						Log.i("success", "added department");
					} else {
						Log.e("error", " adding department");
					}
					rest();
					makeList();
				}
				else{
					Toast.makeText(getApplicationContext(), getString(R.string.please_insert_another_department_name), Toast.LENGTH_LONG).show();
				}
			}
		}
		else{
			if(etDepartmentName.getText().toString().equals("")) {
				Toast.makeText(getApplicationContext(), getString(R.string.please_insert_department_name), Toast.LENGTH_LONG).show();
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