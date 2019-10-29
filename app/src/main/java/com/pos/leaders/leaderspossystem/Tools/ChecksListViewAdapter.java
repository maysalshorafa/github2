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
import android.widget.ImageView;

import com.pos.leaders.leaderspossystem.ChecksActivity;
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
	private  double amount=0;

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
			holder.btnDelete = (ImageView) convertView.findViewById(R.id.btn_delete_check);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (checks.get(position) != null) {
			if(position==0){
				holder.etAmount.setHint(checks.get(position).getAmount() + "");
				holder.etBankNum.setHint(checks.get(position).getBankNum() + "");
				holder.etBenchNum.setHint(checks.get(position).getBranchNum() + "");
				holder.etCheckNum.setHint(checks.get(position).getCheckNum() + "");
				holder.etAccountNum.setHint(checks.get(position).getAccountNum() + "");
				holder.etDate.setText(DateConverter.toDate(new Date(checks.get(position).getCreatedAt().getTime())));
				amount=checks.get(position).getAmount() ;
			}else {
				holder.etAccountNum.setText(checks.get(position).getAccountNum() + "");
				holder.etAmount.setText(checks.get(position).getAmount() + "");
				holder.etBankNum.setText(checks.get(position).getBankNum() + "");
				holder.etBenchNum.setText(checks.get(position).getBranchNum() + "");
				holder.etCheckNum.setText(checks.get(position).getCheckNum() + "");
				holder.etDate.setText(DateConverter.toDate(new Date(checks.get(position).getCreatedAt().getTime())));

			}
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
				updateDate(position,myCalendar.getTime().getTime());
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
		holder.btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("delete click", "onClick: "+position);
				ChecksActivity checksActivity = (ChecksActivity)getContext();
				checksActivity.delete(position);
			}
		});
		return convertView;
	}

	public void updateDate(int position, long date){
		Date date1 = new Date(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		cal.set(Calendar.MILLISECOND, 0);
		checks.get(position).setCreatedAt(new java.sql.Timestamp(date1.getTime()));
		this.notifyDataSetChanged();
	}

	public class ViewHolder {
		private EditText etAmount;
		private EditText etDate;
		private EditText etBankNum;
		private EditText etBenchNum;
		private EditText etAccountNum;
		private EditText etCheckNum;
		private ImageView btnDelete;

		public long getEtAccountNum() {
			if(etAccountNum.getText().toString().equals("")){
				String s= "0";
				etAccountNum.setText(s);
				return Long.parseLong(etAccountNum.getText().toString());
			}
			return Long.parseLong(etAccountNum.getText().toString());
		}

		public Double getEtAmount() {
			if(etAmount.getText().toString().equals("")){
				String s= "0";
				etAmount.setText(s);
				return Double.parseDouble(etAmount.getText().toString());
			}
			if(amount>0){
				etAmount.setHint(Util.makePrice(amount));
				return Double.parseDouble(etAmount.getText().toString());
			}
			return Double.parseDouble(etAmount.getText().toString());
		}
		public void setEtAmount(double amount) {
			etAmount.setText(Util.makePrice(amount));
		}

		public long getEtBankNum() {
			if(etBankNum.getText().toString().equals("")){
				String s= "0";
				etBankNum.setText(s);
				return Long.parseLong(etBankNum.getText().toString());
			}
			return Long.parseLong(etBankNum.getText().toString());
		}

		public long getEtBenchNum() {
			if(etBenchNum.getText().toString().equals("")){
				String s= "0";
				etBenchNum.setText(s);
				return Long.parseLong(etBenchNum.getText().toString());
			}
			return Long.parseLong(etBenchNum.getText().toString());
		}

		public long getEtCheckNum() {
			if(etCheckNum.getText().toString().equals("")){
				String s= "0";
				etCheckNum.setText(s);
				return Long.parseLong(etCheckNum.getText().toString());
			}
			return Long.parseLong(etCheckNum.getText().toString());
		}

		public String getEtDate() {
			if(etDate.getText().equals("")){
				String s= "0";
				etDate.setText(s);
				return etDate.getText().toString();
			}
			return etDate.getText().toString();
		}
        public Double getEtHint() {
            if(etAmount.getHint().toString().equals("")){
                String s= "0";
                etAmount.setHint(s);
                return Double.parseDouble(etAmount.getHint().toString());
            }
            if(amount>0){
                etAmount.setHint(Util.makePrice(amount));
                return Double.parseDouble(etAmount.getHint().toString());
            }
            return Double.parseDouble(etAmount.getHint().toString());
        }
        public long getEtBankNumHint() {
            if(etBankNum.getHint().toString().equals("")){
                String s= "0";
                etBankNum.setHint(s);
                return Long.parseLong(etBankNum.getHint().toString());
            }
            return Long.parseLong(etBankNum.getHint().toString());
        }

        public long getEtBenchNumHint() {
            if(etBenchNum.getHint().toString().equals("")){
                String s= "0";
                etBenchNum.setHint(s);
                return Integer.parseInt(etBenchNum.getHint().toString());
            }
            return Long.parseLong(etBenchNum.getHint().toString());
        }

        public long getEtCheckNumHint() {
            if(etCheckNum == null || etCheckNum.getHint().equals("")){
                String s= "0";
                etCheckNum.setHint(s);
                return Long.parseLong(etCheckNum.getHint().toString());
            }
            return Long.parseLong(etCheckNum.getHint().toString());
        }

        public String getEtDateHint() {
            if(etDate.equals("")){
                String s= "0";
                etDate.setHint(s);
                return etDate.getHint().toString();
            }
            return etDate.getHint().toString();
        }
        public long getEtAccountNumHint() {
            if(etAccountNum.getHint().toString().equals("")){
                String s= "0";
                etAccountNum.setHint(s);
                return Long.parseLong(etAccountNum.getHint().toString());
            }
            return Long.parseLong(etAccountNum.getHint().toString());
        }
	}
}