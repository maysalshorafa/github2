package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by DELTA on 30/10/2016.
 */

public class UserAttendanceReportListViewAdapter extends ArrayAdapter {
	private List<ScheduleWorkers> scheduleWorkersesList;
	private int resource;
	private LayoutInflater inflater;
	private Context context;


	public UserAttendanceReportListViewAdapter(Context context, int resource, List<ScheduleWorkers> scheduleWorkersesList) {
		super(context, resource, scheduleWorkersesList);
		this.context = context;
		this.scheduleWorkersesList = scheduleWorkersesList;
		this.resource = resource;
		inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@NonNull
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(resource, null);
			holder.tvDate = (TextView) convertView.findViewById(R.id.attendanceReportUser_TVDate);
			holder.tvStart = (TextView) convertView.findViewById(R.id.attendanceReportUser_TVStartTime);
			holder.tvEnd = (TextView) convertView.findViewById(R.id.attendanceReportUser_TVEndTime);
			holder.tvH = (TextView) convertView.findViewById(R.id.attendanceReportUser_TVH);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Date d1, d2 ;
		long diff;
		long val = scheduleWorkersesList.get(position).getDate();
		Date date=new Date(val);
		SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
		d2 = new Date(scheduleWorkersesList.get(position).getExitTime());
		d1 = new Date(scheduleWorkersesList.get(position).getStartTime());
		Log.d("Date",date+"");
		holder.tvDate.setText(DateConverter.geDate(date));
		holder.tvStart.setText(DateConverter.getTime(d1));
		holder.tvEnd.setText(DateConverter.getTime(d2));

		//holder.tvH.setText(i+":"+String.format("%.0f",ii)+" Hour");
		long h, m, s, ms, d;
		d = DateConverter.getDateDiff(d1, d2, TimeUnit.MILLISECONDS);
		h = DateConverter.getDateDiff(d1, d2, TimeUnit.MILLISECONDS);
		m = DateConverter.getDateDiff(d1, d2, TimeUnit.MILLISECONDS);
		s = DateConverter.getDateDiff(d1, d2, TimeUnit.MILLISECONDS);
		ms = DateConverter.getDateDiff(d1, d2, TimeUnit.MILLISECONDS);
		d=d/(1000*60*60*24);
		h=h/(1000*60*60);
		m=((m-(h*1000*60*60))/(1000*60));
		s=(s-(m*1000*60)-(h*1000*60*60))/(1000);

		/*
		h = h - (d * 24);
		m = m - (h * 60) - (d * 24);
		s = s - (m * 60) - (h * 60) - (d * 24);
		ms = ms - (s * 1000);
		*/

		holder.tvH.setText(String.format("%02d:%02d:%02d", h, m, s));
		return convertView;
	}

	class ViewHolder {
		private TextView tvDate;
		private TextView tvStart;
		private TextView tvEnd;
		private TextView tvH;
	}
}
