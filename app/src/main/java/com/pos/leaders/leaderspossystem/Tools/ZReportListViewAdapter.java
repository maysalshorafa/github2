package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by TOSHIBA on 05/01/2017.
 */

public class ZReportListViewAdapter extends ArrayAdapter {
    private List<ZReport> ZReportList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    public ZReportListViewAdapter(Context context, int resource, List<ZReport> ZReportList) {
        super(context, resource, ZReportList);
        this.context = context;
        this.ZReportList = ZReportList;
        this.resource = resource;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvZID = (TextView) convertView.findViewById(R.id.listViewItemZReport_tvZID);
            holder.tvZCreateDate = (TextView) convertView.findViewById(R.id.listViewItemZReport_tvZCreateDate);
            holder.tvZUserName = (TextView) convertView.findViewById(R.id.listViewItemZReport_tvZUserName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvZID.setText(context.getString(R.string.z_number)+" "+ZReportList.get(position).getId());
        holder.tvZCreateDate.setText(DateConverter.DateToString(ZReportList.get(position).getCreationDate()));
        holder.tvZUserName.setText("");
        if(ZReportList.get(position).getUser()!=null)
            holder.tvZUserName.setText(ZReportList.get(position).getUser().getFullName());

        return convertView;
    }

    class ViewHolder {
        private TextView tvZID;
        private TextView tvZUserName;
        private TextView tvZCreateDate;
    }
}
