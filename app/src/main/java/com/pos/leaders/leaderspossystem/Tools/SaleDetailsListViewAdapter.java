package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by KARAM on 20/10/2016.
 */

public class SaleDetailsListViewAdapter extends ArrayAdapter {


    private static final int MINCHARNUMBER = 7;
    private List<Order> orderList;
	private int resource;
	private LayoutInflater inflater;
	private Context context;
    private int selected=-1;
	/**
	 * Constructor
	 *
	 * @param context  The current context.
	 * @param resource The resource ID for a layout file containing a TextView to use when
	 *                 instantiating views.
	 * @param objects  The objects to represent in the ListView.
	 */
	public SaleDetailsListViewAdapter(Context context, int resource, List<Order> objects) {
		super(context, resource, objects);
		this.context=context;
		orderList=objects;
		this.resource=resource;
		inflater=(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;

		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(resource,null);
			holder.tvName=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVProductName);
			holder.tvPrice=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVPrice);
			holder.tvCount=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVCount);
			holder.tvTotal=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVTotalPrice);
			holder.llMethods=(RelativeLayout)convertView.findViewById(R.id.rowSaleDetails_LLMethods);
			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		int count;
		double price;
		price=orderList.get(position).getPrice();
		count=orderList.get(position).getCount();
		holder.tvName.setText(_Substring(orderList.get(position).getProduct().getName()));
		holder.tvPrice.setText(String.format(new Locale("en"),"%.2f",price)+" "+ context.getString(R.string.ins));
		holder.tvCount.setText(count+"");
		holder.tvTotal.setText(String.format(new Locale("en"),"%.2f",(price*count))+ " " + context.getString(R.string.ins));
        if(selected==position&&selected!=-1){
            holder.llMethods.setVisibility(View.VISIBLE);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.list_background_color));
        }
        else {
            holder.llMethods.setVisibility(View.GONE);
            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
		return convertView;
	}

	class ViewHolder{
		private TextView tvName;
		private TextView tvCount;
		private TextView tvPrice;
		private TextView tvTotal;
		private RelativeLayout llMethods;

	}

    public void setSelected(int selected) {
        this.selected = selected;
    }

	private String _Substring(String str){
		if(str.length()>MINCHARNUMBER)
			return str.substring(0,MINCHARNUMBER);
		return str;
	}
}
