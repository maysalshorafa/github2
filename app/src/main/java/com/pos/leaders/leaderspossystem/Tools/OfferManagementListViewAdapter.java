package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Win8.1 on 8/1/2018.
 */

public class OfferManagementListViewAdapter extends ArrayAdapter {
    private List<Offer> offerList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    int bgColor =0;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     * @param objects  The objects to represent in the ListView.
     */
    public OfferManagementListViewAdapter(Context context, int resource, List<Offer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.offerList = objects;
        bgColor=0;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OfferManagementListViewAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new OfferManagementListViewAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvOfferName = (TextView) convertView.findViewById(R.id.listOfferManagement_TVOfferName);
            holder.tvOfferAction = (TextView) convertView.findViewById(R.id.listOfferManagement_TVOfferAction);
            holder.tvOfferEndDate = (TextView) convertView.findViewById(R.id.listOfferManagement_TVOfferEndDate);
            holder.tvOfferResourceType = (TextView) convertView.findViewById(R.id.listOfferManagement_TVResourceType);
            holder.tvOfferStartAtDate = (TextView) convertView.findViewById(R.id.listOfferManagement_TVStartAt);
            convertView.setTag(holder);
        } else {
            holder = (OfferManagementListViewAdapter.ViewHolder) convertView.getTag();
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        holder.tvOfferName.setText(offerList.get(position).getName() + "");
        holder.tvOfferEndDate.setText(String.format(new Locale("en"),format.format(offerList.get(position).getEndDate())));
        holder.tvOfferResourceType.setText(offerList.get(position).getResourceType().getValue());
        holder.tvOfferStartAtDate.setText(String.format(new Locale("en"),format.format(offerList.get(position).getStartDate())));
        String s=offerList.get(position).getDataAsJson().toString();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
            JSONObject action = new JSONObject(jsonObject.get("action").toString());
            holder.tvOfferAction.setText(action.getString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(bgColor%2==0){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
        }else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));

        }
        bgColor++;
        return convertView;
    }
    class ViewHolder {
        private TextView tvOfferName;
        private TextView tvOfferResourceType;
        private TextView tvOfferAction;
        private TextView tvOfferStartAtDate;
        private TextView tvOfferEndDate;

    }
}