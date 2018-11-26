package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by KARAM on 26/10/2016.
 */

public class SaleManagementListViewAdapter extends ArrayAdapter {
	private List<Order> salesList;
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
	public SaleManagementListViewAdapter(Context context, int resource, List<Order> objects) {
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
			holder.tvNumberOfItems = (TextView) convertView.findViewById(R.id.listSaleManagement_TVNumberOfItems);
			holder.tvCustomerName = (TextView) convertView.findViewById(R.id.listSaleManagement_TVCustomerName);
			holder.tvDate = (TextView) convertView.findViewById(R.id.listSaleManagement_TVDate);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.listSaleManagement_TVPrice);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.listSaleManagement_TVStatus);
			holder.FL = (LinearLayout) convertView.findViewById(R.id.listSaleManagement_FLMore);
			holder.btCancel = (Button) convertView.findViewById(R.id.listSaleManagement_BTCancel);
			holder.btReturn = (Button) convertView.findViewById(R.id.listSaleManagement_BTReturn);
			holder.btView = (Button) convertView.findViewById(R.id.listSaleManagement_BTView);
			holder.tvDiscount = (Button) convertView.findViewById(R.id.listSaleManagement_TVDiscount);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		double price;
		Order o = salesList.get(position);
		price = o.getTotalPrice();
		holder.tvID.setText(o.getOrderId() + "");
		holder.tvDiscount.setText(o.getCartDiscount()+"");
		holder.tvNumberOfItems.setText(o.getNumberOfItems()+"");

		if (o.getCustomer_name() != null && !o.getCustomer_name().equals("")) {
			holder.tvCustomerName.setText(o.getCustomer_name());
		} else{
			holder.tvCustomerName.setText(context.getString(R.string.general_customer));
		}

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		holder.tvDate.setText(String.format(new Locale("en"),format.format(salesList.get(position).getCreatedAt())));

		holder.tvPrice.setText(Util.makePrice(price) + " " + context.getString(R.string.ins));

		holder.tvStatus.setText(R.string.invoice_with_tax);

		holder.FL.setVisibility(View.GONE);

		try {
			if (salesList.get(position).getPayment().getPaymentWay().equals(CONSTANT.CREDIT_CARD)) {
				convertView.setBackground(context.getResources().getDrawable(R.color.credit_card_bg));
			} else if (salesList.get(position).getPayment().getPaymentWay().equals(CONSTANT.CHECKS)) {
				convertView.setBackground(context.getResources().getDrawable(R.color.check_bg));
			} else {
				convertView.setBackground(context.getResources().getDrawable(R.color.sale_bg));
			}
		} catch (Exception e) {
			//e.printStackTrace();
			convertView.setBackground(context.getResources().getDrawable(R.color.sale_bg));
		}

		return convertView;
	}
	class ViewHolder {
		private TextView tvID;
		private TextView tvNumberOfItems;
		private TextView tvCustomerName;
		private TextView tvDate;
		private TextView tvPrice;
		private TextView tvStatus;
		private LinearLayout FL;
		private Button btCancel;
		private Button btReturn;
		private Button btView;
		private TextView tvDiscount;
	}
}
