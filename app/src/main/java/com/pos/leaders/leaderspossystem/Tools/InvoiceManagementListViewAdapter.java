package com.pos.leaders.leaderspossystem.Tools;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CreditCardPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DocumentType;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Invoice;
import com.pos.leaders.leaderspossystem.Models.InvoiceStatus;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.ReceiptDocuments;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.pos.leaders.leaderspossystem.Tools.CONSTANT.CASH;

/**
 * Created by Win8.1 on 9/5/2018.
 */

public class InvoiceManagementListViewAdapter  extends ArrayAdapter {
    private List<Invoice> invoicesList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private List<String>invoiceNumbers;
    double amount =0.0;
    private  List<String>invoiceOrderIds;
    private long customerId;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     * @param objects  The objects to represent in the ListView.
     */
    public InvoiceManagementListViewAdapter(Context context, int resource, List<Invoice> objects,List<String>invoiceNumbers,List<String>invoiceOrderIds,long customerId) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.invoicesList = objects;
        this.invoiceNumbers=invoiceNumbers;
        this.invoiceOrderIds=invoiceOrderIds;
        this.customerId=customerId;
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
                Spinner currencySp = (Spinner)cashReceiptDialog.findViewById(R.id.spinnerCurrencyTypeForTotalPrice);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etAmount.getText().toString()!=null){
                            amount=Double.parseDouble(etAmount.getText().toString());
                            PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(getContext());
                            paymentDBAdapter.open();
                            ordersIds.add(invoiceNumbers.get(position));
                            try {
                                long paymentID = paymentDBAdapter.receiptInsertEntry(CASH,Double.parseDouble(String.valueOf(invoicesList.get(position).getDocumentsData().getDouble("total"))), Long.parseLong(invoiceOrderIds.get(position)));
                                final Payment payment = paymentDBAdapter.getPaymentByID( Long.parseLong(invoiceOrderIds.get(position)));
                                final JSONObject newJsonObject = new JSONObject(payment.toString());
                                String paymentWay = newJsonObject.getString("paymentWay");
                                long orderId = newJsonObject.getLong("orderId");
                                List<CashPayment> cashPaymentList = new ArrayList<CashPayment>();
                                List<Payment> paymentList = new ArrayList<Payment>();
                                List<CreditCardPayment> creditCardPaymentList = new ArrayList<CreditCardPayment>();
                                List<Check> checkList = new ArrayList<Check>();
                                if(paymentWay.equalsIgnoreCase(CONSTANT.CASH)){
                                    //get cash payment detail by order id
                                    CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(getContext());
                                    cashPaymentDBAdapter.open();
                                    cashPaymentDBAdapter.insertEntry(Long.parseLong(invoiceOrderIds.get(position)), Double.parseDouble(String.valueOf(invoicesList.get(position).getDocumentsData().getDouble("total"))), 0, new Timestamp(System.currentTimeMillis()));
                                    cashPaymentList = cashPaymentDBAdapter.getPaymentBySaleID(orderId);
                                    JSONArray jsonArray = new JSONArray(cashPaymentList.toString());
                                    newJsonObject.put("paymentDetails",jsonArray);
                                }

                                if(paymentWay.equalsIgnoreCase(CONSTANT.CREDIT_CARD)){
                                    //get credit payment detail by order id
                                    CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(getContext());
                                    creditCardPaymentDBAdapter.open();
                                    creditCardPaymentList = creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
                                    JSONArray jsonArray = new JSONArray(creditCardPaymentList.toString());
                                    newJsonObject.put("paymentDetails",jsonArray);
                                }
                                if(paymentWay.equalsIgnoreCase(CONSTANT.CHECKS)){
                                    //get check payment detail by order id
                                    ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(getContext());
                                    checksDBAdapter.open();
                                    checkList = checksDBAdapter.getPaymentBySaleID(orderId);
                                    JSONArray jsonArray = new JSONArray(checkList.toString());
                                    newJsonObject.put("paymentDetails",jsonArray);
                                }
                                new AsyncTask<Void, Void, Void>(){
                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                    }
                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        cashReceiptDialog.dismiss();
                                   //     print(invoiceImg.Invoice( SESSION._ORDER_DETAILES, SESSION._ORDERS, false, SESSION._EMPLOYEE,invoiceNum));

                                        //clearCart();

                                    }
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                                        try {
                                            ObjectMapper mapper = new ObjectMapper();
                                            Log.i("Payment", newJsonObject.toString());
                                            String payRes=transmit.authPost(ApiURL.Payment, newJsonObject.toString(), SESSION.token);
                                            Log.i("Payment log", payRes);
                                            JSONObject customerData = new JSONObject();
                                            customerData.put("customerId",customerId);
                                                ReceiptDocuments documents = new ReceiptDocuments("Receipt",new Timestamp(System.currentTimeMillis()), ordersIds,amount,"ILS");
                                                String doc = mapper.writeValueAsString(documents);
                                                JSONObject docJson= new JSONObject(doc);
                                                String type = docJson.getString("type");
                                                docJson.remove("type");
                                                docJson.put("@type",type);
                                                docJson.put("customer",customerData);
                                                Log.d("Document vale", docJson.toString());
                                                com.pos.leaders.leaderspossystem.Models.Invoice invoice = new Invoice(DocumentType.RECEIPT,docJson);
                                                Log.d("Receipt log",invoice.toString());
                                                String res=transmit.authPost(ApiURL.Documents,invoice.toString(), SESSION.token);
                                                JSONObject jsonObject = new JSONObject(res);
                                               String msgData = jsonObject.getString(MessageKey.responseBody);
                                                Log.d("receiptResult",res);
                                            Invoice invoice1 = invoicesList.get(position);
                                           JSONObject updataInvoice =invoice1.getDocumentsData();
                                            updataInvoice.remove("invoiceStatus");
                                            updataInvoice.put("invoiceStatus", InvoiceStatus.PAID);
                                            updataInvoice.put("type",DocumentType.INVOICE);
                                            Log.d("invoiceRes123",updataInvoice.toString());

                                            String upDataInvoiceRes=transmit.authPutInvoice(ApiURL.Documents,updataInvoice.toString(), SESSION.token,invoiceNumbers.get(position));
                                            Log.d("invoiceRes",upDataInvoiceRes);
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        return null;
                                    }
                                }.execute();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            paymentDBAdapter.close();


                        }

                    }
                });

            }
        });
        try {
            holder.tvTotalAmount.setText(invoicesList.get(position).getDocumentsData().getDouble("total")+getContext().getString(R.string.ins));
            holder.tvTotalPaid.setText(invoicesList.get(position).getDocumentsData().getDouble("totalPaid")+getContext().getString(R.string.ins));
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
        private Button cashReceipt;
        private Button creditReceipt;
        private Button checkReceipt;
        private LinearLayout FL;

    }
}
