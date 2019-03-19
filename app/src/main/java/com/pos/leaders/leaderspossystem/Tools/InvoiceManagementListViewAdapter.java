package com.pos.leaders.leaderspossystem.Tools;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.ChecksActivity;
import com.pos.leaders.leaderspossystem.CreditCard.MainCreditCardActivity;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Pinpad.PinpadActivity;
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
            holder.cashReceipt = (Button)convertView.findViewById(R.id.listInvoiceManagement_BTCreateCashReceipt);
            holder.checkReceipt = (Button)convertView.findViewById(R.id.listInvoiceManagement_BTCreateCheckReceipt);
            holder.creditReceipt = (Button)convertView.findViewById(R.id.listInvoiceManagement_BTCreateCreditReceipt);
            holder.FL = (LinearLayout) convertView.findViewById(R.id.listInvoiceManagement_FLMore);

            convertView.setTag(holder);
        } else {
            holder = (InvoiceManagementListViewAdapter.ViewHolder) convertView.getTag();
        }
        holder.FL.setVisibility(View.GONE);
        holder.cashReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<String> ordersIds = new ArrayList<>();
                final Dialog cashReceiptDialog = new Dialog(getContext());
                cashReceiptDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                cashReceiptDialog.show();
                cashReceiptDialog.setContentView(R.layout.cash_receipt_dialog);
                final EditText etAmount = (EditText) cashReceiptDialog.findViewById(R.id.cashReceiptDialog_TECash);
                Button btnOk = (Button)cashReceiptDialog.findViewById(R.id.cashReceiptDialog_BTOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etAmount.getText().toString()!=null){
                            try {
                                if(Double.parseDouble(etAmount.getText().toString())>=Double.parseDouble(String.valueOf(invoicesList.get(position).getDocumentsData().getDouble("total")))) {
                                    DocumentControl.sendDoc(getContext(), invoicesList.get(position), CONSTANT.CASH,Double.parseDouble(etAmount.getText().toString()));
                                    cashReceiptDialog.dismiss();
                                }else {
                                    Toast.makeText(getContext(),"Inserted amount not equal to required amount",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

            }
        });


        holder.checkReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ChecksActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("checksReceipt", "checksReceipt");
                try {
                    intent.putExtra("_Price",Double.parseDouble(String.valueOf(invoicesList.get(position).getDocumentsData().getDouble("total"))));
                    intent.putExtra("_custmer", "");
                    intent.putExtra("invoice",invoicesList.get(position).toString());
                    v.getContext().startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        holder.creditReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(SETTINGS.creditCardEnable) {
                    if (SETTINGS.pinpadEnable) {//pinpad is active
                        intent=new Intent(getContext(),PinpadActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("creditReceipt", "creditsReceipt");
                        try {
                            intent.putExtra("_Price",Double.parseDouble(String.valueOf(invoicesList.get(position).getDocumentsData().getDouble("total"))));
                            intent.putExtra("_custmer", "");
                            intent.putExtra("invoice",invoicesList.get(position).toString());
                            v.getContext().startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        intent=new Intent(getContext(),MainCreditCardActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("creditReceipt", "creditsReceipt");
                        try {
                            intent.putExtra("_Price",Double.parseDouble(String.valueOf(invoicesList.get(position).getDocumentsData().getDouble("total"))));
                            intent.putExtra("_custmer", "");
                            intent.putExtra("invoice",invoicesList.get(position).toString());
                            v.getContext().startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        try {
            holder.tvTotalAmount.setText(invoicesList.get(position).getDocumentsData().getDouble("total")+getContext().getString(R.string.ins));
            holder.tvID.setText(invoiceNumbers.get(position)+"");
            holder.tvTotalPaid.setText(invoicesList.get(position).getDocumentsData().getDouble("totalPaid")+getContext().getString(R.string.ins));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }
    class ViewHolder {
        private TextView tvID;
        private TextView tvTotalAmount;
        private TextView tvTotalPaid;
        private Button cashReceipt;
        private Button creditReceipt;
        private Button checkReceipt;
        private LinearLayout FL;

    }


}
