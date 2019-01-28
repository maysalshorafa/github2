package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DocumentType;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by KARAM on 26/10/2016.
 */

public class SaleManagementListViewAdapter extends ArrayAdapter {
	private List<Object> salesList;
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
	public SaleManagementListViewAdapter(Context context, int resource, List<Object> objects) {
		super(context, resource, objects);
		this.context = context;
		this.resource = resource;
		this.salesList = objects;
		inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(resource, null);
			holder.tvID = (TextView) convertView.findViewById(R.id.listSaleManagement_TVSaleID);
		//	holder.tvNumberOfItems = (TextView) convertView.findViewById(R.id.listSaleManagement_TVNumberOfItems);
			holder.tvCustomerName = (TextView) convertView.findViewById(R.id.listSaleManagement_TVCustomerName);
			holder.tvDate = (TextView) convertView.findViewById(R.id.listSaleManagement_TVDate);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.listSaleManagement_TVPrice);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.listSaleManagement_TVStatus);
			holder.FL = (LinearLayout) convertView.findViewById(R.id.listSaleManagement_FLMore);
			holder.btCancel = (Button) convertView.findViewById(R.id.listSaleManagement_BTCancel);
			holder.btReturn = (Button) convertView.findViewById(R.id.listSaleManagement_BTReturn);
			holder.btView = (Button) convertView.findViewById(R.id.listSaleManagement_BTView);
			holder.tvDiscount = (TextView) convertView.findViewById(R.id.listSaleManagement_TVDiscount);
			holder.cancelingOrderId = (TextView) convertView.findViewById(R.id.listSaleManagement_TVCancelOrderId);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
	//	Order order = (Order)salesList.get(position);
		if(salesList.get(position)instanceof Order){
		double price;
		Order order = (Order)salesList.get(position);
		price = order.getTotalPrice();
		holder.tvID.setText(order.getOrderId() + "");
		holder.tvDiscount.setText(order.getCartDiscount()+"");
		OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(context);
		orderDetailsDBAdapter.open();
		order.setOrders(orderDetailsDBAdapter.getOrderBySaleID(order.getOrderId()));
		//holder.tvNumberOfItems.setText(order.getNumberOfItems()+"");

		if (order.getCustomer_name() != null && !order.getCustomer_name().equals("")) {
			holder.tvCustomerName.setText(order.getCustomer_name());
		} else{
			holder.tvCustomerName.setText(context.getString(R.string.general_customer));
		}
		if(order.getCancellingOrderId()>0){
			holder.cancelingOrderId.setText(order.getCancellingOrderId()+"");
		}
		else {
			holder.cancelingOrderId.setText("");
		}
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		holder.tvDate.setText(String.format(new Locale("en"),format.format(order.getCreatedAt())));

		holder.tvPrice.setText(Util.makePrice(price) + " " + context.getString(R.string.ins));

		holder.tvStatus.setText("INRC");

		holder.FL.setVisibility(View.GONE);

		try {
			if (order.getPayment().getPaymentWay().equals(CONSTANT.CREDIT_CARD)) {
				convertView.setBackground(context.getResources().getDrawable(R.color.credit_card_bg));
			} else if (order.getPayment().getPaymentWay().equals(CONSTANT.CHECKS)) {
				convertView.setBackground(context.getResources().getDrawable(R.color.check_bg));
			} else {
				convertView.setBackground(context.getResources().getDrawable(R.color.sale_bg));
			}
		} catch (Exception e) {
			//e.printStackTrace();
			convertView.setBackground(context.getResources().getDrawable(R.color.sale_bg));
		}
		}else {
            holder.cancelingOrderId.setText("");
            double price;
				BoInvoice boInvoice = (BoInvoice)salesList.get(position);;
				JSONObject doc = boInvoice.getDocumentsData();
				holder.tvID.setText(boInvoice.getDocNum() );
				try {
					if(boInvoice.getType().equals(DocumentType.INVOICE)) {
						holder.tvStatus.setText("IN");
					}else {
						holder.tvStatus.setText("CIN");
					}
					price = doc.getDouble("total");
					holder.tvDiscount.setText(doc.getDouble("cartDiscount")+"");
					SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					Date date = DateConverter.stringToDate(doc.getString("date"));

					holder.tvDate.setText(DateConverter.geDate(date));
					holder.tvPrice.setText(Util.makePrice(price) + " " + context.getString(R.string.ins));
					JSONObject customerJson= doc.getJSONObject("customer");
					holder.tvCustomerName.setText(customerJson.get("firstName")+" "+customerJson.get("lastName"));
					int count =0;
					JSONArray jsonArray = doc.getJSONArray("cartDetailsList");
					for (int c = 0;c<jsonArray.length();c++){
						JSONObject j = jsonArray.getJSONObject(c);
						count+=j.getInt("quantity");
					}
				//	holder.tvNumberOfItems.setText(count+"");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				holder.FL.setVisibility(View.GONE);
		}

		return convertView;
	}
	class ViewHolder {
		private TextView tvID;
		//private TextView tvNumberOfItems;
		private TextView tvCustomerName;
		private TextView tvDate;
		private TextView tvPrice;
		private TextView tvStatus;
		private LinearLayout FL;
		private Button btCancel;
		private Button btReturn;
		private Button btView;
		private TextView tvDiscount;
		private TextView cancelingOrderId;
	}
}
