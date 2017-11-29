package com.pos.leaders.leaderspossystem.Tools;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by KARAM on 03/11/2016.
 */

public class ChecksListViewAdapter extends ArrayAdapter {

	private Context context;
	private int resource;
	private List<Check> checks;
	private LayoutInflater inflater;
	private int bgColor = 0;

	public ChecksListViewAdapter(Context context, int resource, List<Check> checks) {
		super(context, resource, checks);
		this.context = context;
		this.resource = resource;
		this.checks = checks;
		this.inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bgColor = 0;
	}

	@NonNull
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(resource, null);

			holder.etAccountNum = (EditText) convertView.findViewById(R.id.listChecks_ETAccountNum);
			holder.etAmount = (EditText) convertView.findViewById(R.id.listChecks_ETAmount);
			holder.etBankNum = (EditText) convertView.findViewById(R.id.listChecks_ETBankNum);
			holder.etBenchNum = (EditText) convertView.findViewById(R.id.listChecks_ETBranchNum);
			holder.etCheckNum = (EditText) convertView.findViewById(R.id.listChecks_ETCheckNumber);
			holder.etDate = (EditText) convertView.findViewById(R.id.listChecks_ETDate);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (checks.get(position) != null) {
			holder.etAccountNum.setText(checks.get(position).getAccountNum()+"");
			holder.etAmount.setText(checks.get(position).getAmount() + "");
			holder.etBankNum.setText(checks.get(position).getBankNum()+"");
			holder.etBenchNum.setText(checks.get(position).getBranchNum()+"");
			holder.etCheckNum.setText(checks.get(position).getCheckNum()+"");
			holder.etDate.setText(DateConverter.toDate(checks.get(position).getDate()));
		}
		if(bgColor%2==0){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
		}
		bgColor++;
		final Calendar myCalendar = Calendar.getInstance();

		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateDate(position,myCalendar.getTime());
			}

		};

		holder.etDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(context, date, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		return convertView;
	}

	public void updateDate(int position, Date date){
		checks.get(position).setDate(date);
		this.notifyDataSetChanged();
	}

	public class ViewHolder {
		private EditText etAmount;
		private EditText etDate;
		private EditText etBankNum;
		private EditText etBenchNum;
		private EditText etAccountNum;
		private EditText etCheckNum;

		public int getEtAccountNum() {
			return Integer.parseInt(etAccountNum.getText().toString());
		}

		public Double getEtAmount() {
			return Double.parseDouble(etAmount.getText().toString());
		}

		public int getEtBankNum() {
			return Integer.parseInt(etBankNum.getText().toString());
		}

		public int getEtBenchNum() {
			return Integer.parseInt(etBenchNum.getText().toString());
		}

		public int getEtCheckNum() {
			return Integer.parseInt(etCheckNum.getText().toString());
		}

		public String getEtDate() {
			return etDate.getText().toString();
		}
	}
}