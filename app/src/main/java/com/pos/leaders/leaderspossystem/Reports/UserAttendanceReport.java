package com.pos.leaders.leaderspossystem.Reports;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.UserAttendanceReportListViewAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by KARAM on 30/10/2016.
 */

public class UserAttendanceReport extends Activity {

	EditText etFromDate, etToDate;
	TextView tvTotalHour;
	ListView lvReport;
	ScheduleWorkersDBAdapter scheduleWorkersDBAdapter;
	List<ScheduleWorkers> scheduleWorkersList, _scheduleWorkersList;
	Date from, to;
	int mYear, mMonth, mDay;
	int userID;
	private static final int DIALOG_FROM_DATE = 825;
	private static final int DIALOG_TO_DATE = 324;
	UserAttendanceReportListViewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendance_report_user);


		etFromDate = (EditText) findViewById(R.id.userAttendanceReport_ETFrom);
		etToDate = (EditText) findViewById(R.id.userAttendanceReport_ETTo);

		tvTotalHour = (TextView) findViewById(R.id.userAttendanceReport_TVTotalHourlyWork);
		lvReport = (ListView) findViewById(R.id.userAttendanceReport_LVReport);


		//region Date
		Date d = new Date();
		Log.i("current date", d.toString());
		//Mon Oct 24 08:45:01 GMT 2016
		//("yyyy-mm-dd hh:mm:ss")
		mYear = Integer.parseInt(d.toString().split(" ")[5]);
		mMonth = d.getMonth();
		mDay = Integer.parseInt(d.toString().split(" ")[2]);
/*
		etStartDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_ID);
			}
		});

		etEndDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_ID2);
			}
		});
		//endregion
*/

		etFromDate.setText(DateConverter.getBeforeMonth().split(" ")[0]);
		from = DateConverter.stringToDate(DateConverter.getBeforeMonth());
		etFromDate.setFocusable(false);
		etFromDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_FROM_DATE);
			}
		});
		etToDate.setText(DateConverter.currentDateTime().split(" ")[0]);
		to = DateConverter.stringToDate(DateConverter.currentDateTime());
		etToDate.setFocusable(false);
		etToDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_TO_DATE);
			}
		});


		scheduleWorkersDBAdapter = new ScheduleWorkersDBAdapter(this);
		scheduleWorkersDBAdapter.open();

		userID = 1;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			userID = extras.getInt("userID");
		}
		setReportDate();
	}

	private void setReportDate() {
		scheduleWorkersList = new ArrayList<ScheduleWorkers>();

		_scheduleWorkersList = scheduleWorkersDBAdapter.getAllUserScheduleWork(userID);


		long r=0,h=0,m=0,s=0;
		for (ScheduleWorkers sw : _scheduleWorkersList) {
			if (DateConverter.dateBetweenTwoDates(from, to, new Date(sw.getDate()))) {
				scheduleWorkersList.add(sw);
				r+=DateConverter.getDateDiff(new Date(sw.getStartTime()),new Date(sw.getExitTime()), TimeUnit.MILLISECONDS);
			}
		}
		h=r/(1000*60*60);
		m=((r-(h*1000*60*60))/(1000*60));
		s=(r-(m*1000*60)-(h*1000*60*60))/(1000);

		tvTotalHour.setText(String.format("%02d:%02d:%02d",h,m,s));


		adapter = new UserAttendanceReportListViewAdapter(this, R.layout.list_adapter_row_attendance, scheduleWorkersList);
		lvReport.setAdapter(adapter);
	}


	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == DIALOG_FROM_DATE) {
			DatePickerDialog datePickerDialog = new DatePickerDialog(this, onFromDateSetListener, Integer.parseInt(from.toString().split(" ")[5]), from.getMonth(),  Integer.parseInt(from.toString().split(" ")[2]));
			//datePickerDialog.getDatePicker().setMaxDate(to.getTime());
			datePickerDialog.getDatePicker().setCalendarViewShown(false);
			return datePickerDialog;
		} else if (id == DIALOG_TO_DATE) {
			DatePickerDialog datePickerDialog = new DatePickerDialog(this, onToDateSetListener, Integer.parseInt(to.toString().split(" ")[5]), to.getMonth(),  Integer.parseInt(to.toString().split(" ")[2]));
			//datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
			//datePickerDialog.getDatePicker().setMinDate(from.getTime());
			datePickerDialog.getDatePicker().setCalendarViewShown(false);
			return datePickerDialog;
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener onFromDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
			mYear = year;
			mMonth = month;
			mDay = dayOfMonth;
			etFromDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
			//yyyy-MM-dd HH:mm:ss
			from = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
			view.setMaxDate(to.getTime());
			setReportDate();
		}
	};
	private DatePickerDialog.OnDateSetListener onToDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
			mYear = year;
			mMonth = month;
			mDay = dayOfMonth;
			etToDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
			view.setMinDate(from.getTime());
			to = DateConverter.stringToDate(year + "-" + (month + 1) + "-" + dayOfMonth + " 00:00:00");
			setReportDate();
		}
	};
}
