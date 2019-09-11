package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.Models.DepositAndPullReport;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Win8.1 on 9/10/2019.
 */

public class DepositAndPullListViewAdapter extends ArrayAdapter {
    private List<DepositAndPullReport> depositAndPullReportList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    int bgColor =0;
    public DepositAndPullListViewAdapter(Context context, int resource, List<DepositAndPullReport> depositAndPullReportList) {
        super(context, resource, depositAndPullReportList);
        Log.d("tttttttttttt",depositAndPullReportList.toString());
        this.context = context;
        this.depositAndPullReportList = depositAndPullReportList;
        this.resource = resource;
        bgColor=0;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DepositAndPullListViewAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new DepositAndPullListViewAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvID = (TextView) convertView.findViewById(R.id.listViewItemDepositAndPullReport_tvID);
            holder.tvCreateDate = (TextView) convertView.findViewById(R.id.listViewItemDepositAndPullReport_tvCreateDate);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.listViewItemDepositAndPullReport_tvUserName);
            holder.tvAmount = (TextView) convertView.findViewById(R.id.listViewItemDepositAndPullReport_tvAmount);
            holder.tvType = (TextView) convertView.findViewById(R.id.listViewItemDepositAndPullReport_tvType);

            convertView.setTag(holder);
        } else {
            holder = (DepositAndPullListViewAdapter.ViewHolder) convertView.getTag();
        }
        Log.d("tttttttttttt",depositAndPullReportList.get(position).toString());
        holder.tvID.setText(" "+depositAndPullReportList.get(position).getDepositAndPullReportId());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        holder.tvCreateDate.setText(String.format(new Locale("en"),format.format(depositAndPullReportList.get(position).getCreatedAt())));
        holder.tvUserName.setText(SESSION._EMPLOYEE.getEmployeeName());
        EmployeeDBAdapter employeeDBAdapter = new EmployeeDBAdapter(context);
        employeeDBAdapter.open();
        Employee employee = employeeDBAdapter.getEmployeeByID(depositAndPullReportList.get(position).getByUserID());
            holder.tvUserName.setText(employee.getFullName());
        holder.tvAmount.setText(depositAndPullReportList.get(position).getAmount()+"");
        holder.tvType.setText(" "+depositAndPullReportList.get(position).getType());

        if(bgColor%2==0){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
        }
        bgColor++;
        return convertView;
    }

    class ViewHolder {
        private TextView tvID;
        private TextView tvUserName;
        private TextView tvCreateDate;
        private TextView tvAmount;
        private TextView tvType;


    }
}
