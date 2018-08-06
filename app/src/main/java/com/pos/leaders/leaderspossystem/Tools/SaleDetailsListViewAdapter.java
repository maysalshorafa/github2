package com.pos.leaders.leaderspossystem.Tools;

import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by KARAM on 20/10/2016.
 */

public class SaleDetailsListViewAdapter extends ArrayAdapter implements OnClickListener {
	private static final int MINCHARNUMBER = 50;
	private List<OrderDetails> orderList;
	private int resource;
	private LayoutInflater inflater;
	private Context context;
	private int selected=-1;
	ViewHolder holder=null;
	/**
	 * Constructor
	 *
	 * @param context  The current context.
	 * @param resource The resource ID for a layout file containing a TextView to use when
	 *                 instantiating views.
	 * @param objects  The objects to represent in the ListView.
	 */
	public SaleDetailsListViewAdapter(Context context, int resource, List<OrderDetails> objects) {
		super(context, resource, objects);
		this.context=context;
		orderList=objects;
		this.resource=resource;
		inflater=(LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(resource,null);
			holder.tvName=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVProductName);
			holder.tvPrice=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVPrice);
			holder.tvCount=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVCount);
			holder.tvTotal=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVTotalPrice);
			holder.tvOfferName=(TextView)convertView.findViewById(R.id.tvOfferName);
			holder.tvOriginalPrice=(TextView)convertView.findViewById(R.id.tvOrderListOriginalPriceValue);

			holder.llMethods=(RelativeLayout)convertView.findViewById(R.id.rowSaleDetails_LLMethods);
			holder.llSalesMan=(LinearLayout) convertView.findViewById(R.id.saleManLayout);
			holder.llOfferName=(LinearLayout) convertView.findViewById(R.id.offerLayout);
			holder.discountLayout=(LinearLayout) convertView.findViewById(R.id.discountLayout);
			holder.tvPercentage=(TextView)convertView.findViewById(R.id.tvDiscountPercentageAmount);
			holder.tvPercentageAmount=(TextView)convertView.findViewById(R.id.discountPercentage);

			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		int count;
		double price;
		double discount = orderList.get(position).getDiscount();
		price=orderList.get(position).getPaidAmount();
		count=orderList.get(position).getQuantity();
		holder.tvName.setText(_Substring(orderList.get(position).getProduct().getDisplayName()));
		holder.tvPrice.setText(String.format(new Locale("en"),"%.2f",price)+" "+ context.getString(R.string.ins));
		holder.tvCount.setText(count+"");
		holder.tvTotal.setText(String.format(new Locale("en"),"%.2f",(price*count))+ " " + context.getString(R.string.ins));
		holder.tvPercentage.setText(R.string.discount_percentage);
		holder.tvPercentageAmount.setText(String.format(new Locale("en"),"%.2f",(discount))+ " %");

		//	callPopup();
		if(selected==position&&selected!=-1){
			holder.llMethods.setVisibility(View.VISIBLE);
			holder.llSalesMan.setVisibility(View.VISIBLE);
            holder.discountLayout.setVisibility(View.VISIBLE);

            convertView.setBackgroundColor(context.getResources().getColor(R.color.list_background_color));
		}
		else {
			holder.llMethods.setVisibility(View.GONE);
			holder.llSalesMan.setVisibility(View.GONE);
            holder.discountLayout.setVisibility(View.GONE);

            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
		}

		//Offer
		if ( orderList.get(position).getOffer()!= null) {
			if (orderList.get(position).getDiscount() > 0) {
				//show offer name
				holder.llOfferName.setVisibility(View.VISIBLE);

				try {
					JSONObject jsonObject = orderList.get(position).getOffer().getDataAsJsonObject();
					JSONObject action = new JSONObject(jsonObject.get("action").toString());
                    holder.tvOfferName.setText(orderList.get(position).getOffer().getName() + " _ " + action.getString("name"));

					holder.tvOriginalPrice.setText(Util.makePrice(orderList.get(position).getUnitPrice() * orderList.get(position).getQuantity()));
					return convertView;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		holder.llOfferName.setVisibility(View.GONE);

		return convertView;
	}



public 	class ViewHolder{
		private TextView tvName;
		private TextView tvCount;
		private TextView tvPrice;
		private TextView tvTotal;
		private TextView tvOfferName;
		private TextView tvOriginalPrice;
		private RelativeLayout llMethods;
		private LinearLayout llSalesMan;
		private LinearLayout discountLayout;
		private TextView tvPercentage;
		private TextView tvPercentageAmount;
		private LinearLayout llOfferName;
}


	public void setSelected(int selected) {
		this.selected = selected;
	}

	private String _Substring(String str){
		if(str.length()>MINCHARNUMBER)
			return str.substring(0,MINCHARNUMBER);
		return str;
	}
	@Override
	public void onClick(View v) {

	}}




