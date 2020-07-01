package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 9/5/2018.
 */

public class InvoiceManagementListViewAdapter  extends ArrayAdapter {
    private List<BoInvoice> invoicesList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String>invoiceNumbers;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     * @param objects  The objects to represent in the ListView.
     */
    public InvoiceManagementListViewAdapter(Context context, int resource, List<BoInvoice> objects, ArrayList<String>invoiceNumbers) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.invoicesList = objects;
        this.invoiceNumbers=invoiceNumbers;

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        InvoiceManagementListViewAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new InvoiceManagementListViewAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvID = (TextView) convertView.findViewById(R.id.listInvoiceManagement_TVInvoiceID);
            holder.tvTotalAmount = (TextView) convertView.findViewById(R.id.listInvoiceManagement_TVTotalAmount);
            holder.tvTotalPaid = (TextView) convertView.findViewById(R.id.listInvoiceManagement_TVTotalPaid);
        }
        try {
            holder.tvTotalAmount.setText(invoicesList.get(position).getDocumentsData().getDouble("total")+SETTINGS.currencySymbol);
            holder.tvID.setText(invoiceNumbers.get(position)+"");
            holder.tvTotalPaid.setText(invoicesList.get(position).getDocumentsData().getDouble("totalPaid")+SETTINGS.currencySymbol);
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
