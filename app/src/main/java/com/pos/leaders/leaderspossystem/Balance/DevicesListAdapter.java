package com.pos.leaders.leaderspossystem.Balance;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.R;

import java.util.ArrayList;

public class DevicesListAdapter extends ArrayAdapter<DeviceSchema> {
    private ArrayList<DeviceSchema> deviceList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    int bgColor=0;

    public DevicesListAdapter(Context context, int resource, ArrayList<DeviceSchema> deviceList){
        super(context, resource, deviceList);
        this.context = context;
        this.resource = resource;
        this.deviceList = deviceList;

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @Nullable ViewGroup parent){
        ViewHolder holder = null;

        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);

            holder.tvName = (TextView) convertView.findViewById(R.id.tvDeviceName_listViewRowDevices);
            holder.tvPort = (TextView) convertView.findViewById(R.id.tvDevicePort_listViewRowDevices);
            holder.tvManufacture = (TextView) convertView.findViewById(R.id.tvDeviceManufacture_listViewRowDevices);
            holder.tvManufacture2 = (TextView) convertView.findViewById(R.id.tvDeviceManufacture2_listViewRowDevices);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DeviceSchema device = getItem(position);


        holder.tvName.setText(device != null ? device.getName() : "");
        holder.tvPort.setText(device != null ? "Port : "+device.getPort() : "");
        holder.tvManufacture.setText(device != null ? device.getManufacture() : "");
        holder.tvManufacture2.setText(device != null ? device.getManufacture() : "");


        if(bgColor%2==0){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
        }

        bgColor++;

        return convertView;
    }

    class ViewHolder{
        TextView tvName;
        TextView tvPort;
        TextView tvManufacture;
        TextView tvManufacture2;
    }
}
