package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Invoice;
import com.pos.leaders.leaderspossystem.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 10/10/2018.
 */

public class OrderDocumentManagementListViewAdapter extends ArrayAdapter {
    private List<Invoice> invoicesList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> invoiceNumbers;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     * @param objects  The objects to represent in the ListView.
     */
    public OrderDocumentManagementListViewAdapter(Context context, int resource, List<Invoice> objects,ArrayList<String>invoiceNumbers) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.invoicesList = objects;
        this.invoiceNumbers=invoiceNumbers;

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        OrderDocumentManagementListViewAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new OrderDocumentManagementListViewAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvID = (TextView) convertView.findViewById(R.id.listOrderDocumentManagement_TVOrderDocumentID);
            holder.tvTotalAmount = (TextView) convertView.findViewById(R.id.listOrderDocumentManagement_TVTotalAmount);
            holder.tvTotalPaid = (TextView) convertView.findViewById(R.id.listOrderDocumentManagement_TVTotalPaid);
            convertView.setTag(holder);
        } else {
            holder = (OrderDocumentManagementListViewAdapter.ViewHolder) convertView.getTag();
        }

        try {
            holder.tvTotalAmount.setText(invoicesList.get(position).getDocumentsData().getDouble("total")+getContext().getString(R.string.ins));
          //  holder.tvTotalPaid.setText(invoicesList.get(position).getDocumentsData().getDouble("totalPaidAmount")+getContext().getString(R.string.ins));
            holder.tvID.setText(invoiceNumbers.get(position)+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
    class ViewHolder {
        private TextView tvID;
        private TextView tvTotalAmount;
        private TextView tvTotalPaid;

    }


}

