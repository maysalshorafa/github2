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

public class EmployeeAttendanceReportListViewAdapter extends ArrayAdapter {
	private List<ScheduleWorkers> scheduleWorkersesList;
	private int resource;
	private LayoutInflater inflater;
	private Context context;
	int bgColor =0;

	public EmployeeAttendanceReportListViewAdapter(Context context, int resource, List<ScheduleWorkers> scheduleWorkersesList) {
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

		Date d1=null, d2=null ;
		long diff;
		long val = scheduleWorkersesList.get(position).getDate();
		Date date=new Date(val);
		SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
		if(scheduleWorkersesList.get(position).getExitTime()>0){
			d2 = new Date(scheduleWorkersesList.get(position).getExitTime());
			holder.tvEnd.setText(DateConverter.getTime(d2));
		}else {
			holder.tvEnd.setText("");
		}
		if(scheduleWorkersesList.get(position).getStartTime()>0){
			d1 = new Date(scheduleWorkersesList.get(position).getStartTime());
			holder.tvStart.setText(DateConverter.getTime(d1));
		}else {
			holder.tvStart.setText("");
		}
		Log.d("Date",date+"");
		holder.tvDate.setText(DateConverter.geDate(date));

		if(d2!=null&& d1!=null){
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
		holder.tvH.setText(String.format("%02d:%02d:%02d", h, m, s));
		}else {
			holder.tvH.setText(String.format(""));

		}
		if(bgColor%2==0){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
		}
		bgColor++;
		return convertView;
	}

	class ViewHolder {
		private TextView tvDate;
		private TextView tvStart;
		private TextView tvEnd;
		private TextView tvH;
	}
}
