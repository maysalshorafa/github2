package com.pos.leaders.leaderspossystem.Tools;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.InvoiceManagementActivity;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.ReciptObject;
import com.pos.leaders.leaderspossystem.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win8.1 on 6/21/2020.
 */

public class ReciptManagementListViewAdapter extends ArrayAdapter {
    private List<BoInvoice> invoicesList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> invoiceNumbers;
    public static List<ReciptObject> reciptObjectList;
     double amount =0;
    ReciptManagementListViewAdapter.ViewHolder holder = null;
    List<String> partiallyString;


    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     * @param objects  The objects to represent in the ListView.
     */
    public ReciptManagementListViewAdapter(Context context, int resource, List<BoInvoice> objects, ArrayList<String> invoiceNumbers, List<ReciptObject> reciptObjec,List<String>partialyCheckBoxString) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.invoicesList = objects;
        this.invoiceNumbers = invoiceNumbers;
       this.reciptObjectList = reciptObjec;
        this.partiallyString=partialyCheckBoxString;


        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ReciptManagementListViewAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvID = (TextView) convertView.findViewById(R.id.listInvoiceManagement_TVInvoiceID);
            holder.tvTotalAmount = (TextView) convertView.findViewById(R.id.listInvoiceManagement_TVTotalAmount);
            holder.tvTotalPaid = (TextView) convertView.findViewById(R.id.listInvoiceManagement_TVTotalPaid);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.paidCheckBox);
        //    holder.checkBox.setChecked(reciptObjectList.get(position).isAll());
            holder.checkBoxPartial = (CheckBox) convertView.findViewById(R.id.paidPartialCheckBox);
            holder.checkBoxPartial.setText(partiallyString.get(position));

            convertView.setTag(holder);
        } else {
            holder = (ReciptManagementListViewAdapter.ViewHolder) convertView.getTag();
        }

        try {
            holder.tvTotalAmount.setText(invoicesList.get(position).getDocumentsData().getDouble("total") + getContext().getString(R.string.ins));
           holder.tvID.setText(invoicesList.get(position).getDocNum()+"");
            holder.tvTotalPaid.setText(invoicesList.get(position).getDocumentsData().getDouble("totalPaid") + getContext().getString(R.string.ins));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.checkBoxPartial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    reciptObjectList.get(position).setPartially(true);
                    final Dialog customerDialog = new Dialog(context);
                    customerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    customerDialog.show();
                    customerDialog.setContentView(R.layout.recipt_dialog_layout);
                    final EditText   customer_amount = (EditText) customerDialog.findViewById(R.id.customer_email);
                    final  Button button = (Button)customerDialog.findViewById(R.id.done);
                  button.setOnClickListener(new View.OnClickListener() {

                                public void onClick(View arg0) {
                                    if(!customer_amount.getText().toString().isEmpty()){
                                        double amount =Double.parseDouble(customer_amount.getText().toString());
                                        reciptObjectList.get(position).setPartialAmount(amount);
                                   //    Log.d("reciptObjectList",reciptObjectList.toString());
                                      //  InvoiceManagementActivity.edit(position,String.valueOf(amount),reciptObjectList);

                                        customerDialog.dismiss();
                                    }
                                    else {
                                           customerDialog.dismiss();
                                    }


                                }
                            });



                } else {

                    reciptObjectList.get(position).setPartially(false);
                    reciptObjectList.get(position).setPartialAmount(0);


                }        }});

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    reciptObjectList.get(position).setAll(true);
                    try {
                        reciptObjectList.get(position).setAllAmount(invoicesList.get(position).getDocumentsData().getDouble("total"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("totalAmount111",position+"");

                    reciptObjectList.get(position).setAll(false);
                    reciptObjectList.get(position).setAllAmount(0);
                }     }});



        return convertView;
    }


    public static class ViewHolder {
        private TextView tvID;
        private TextView tvTotalAmount;
        private TextView tvTotalPaid;
        public CheckBox checkBox;
        public CheckBox checkBoxPartial;

    }


}
