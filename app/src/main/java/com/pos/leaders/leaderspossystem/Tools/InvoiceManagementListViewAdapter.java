package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Invoice;
import com.pos.leaders.leaderspossystem.R;

import java.util.List;

/**
 * Created by Win8.1 on 9/5/2018.
 */

public class InvoiceManagementListViewAdapter  extends ArrayAdapter {
    private List<Invoice> invoicesList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     * @param objects  The objects to represent in the ListView.
     */
    public InvoiceManagementListViewAdapter(Context context, int resource, List<Invoice> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.invoicesList = objects;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InvoiceManagementListViewAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new InvoiceManagementListViewAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvID = (TextView) convertView.findViewById(R.id.listInvoiceManagement_TVInvoiceID);
            holder.tvTotalAmount = (TextView) convertView.findViewById(R.id.listInvoiceManagement_TVTotalAmount);
            holder.tvTotalPaid = (TextView) convertView.findViewById(R.id.listInvoiceManagement_TVTotalPaid);
            convertView.setTag(holder);
        } else {
            holder = (InvoiceManagementListViewAdapter.ViewHolder) convertView.getTag();
        }


        return convertView;
    }
    class ViewHolder {
        private TextView tvID;
        private TextView tvTotalAmount;
        private TextView tvTotalPaid;

    }
}
