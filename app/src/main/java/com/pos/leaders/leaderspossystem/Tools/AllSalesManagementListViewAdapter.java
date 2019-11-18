package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DocumentType;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ProductSale;
import com.pos.leaders.leaderspossystem.Models.SumOfSalesPerProduct;
import com.pos.leaders.leaderspossystem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by LeadPos on 12/11/19.
 */

public class AllSalesManagementListViewAdapter  extends ArrayAdapter {
    private List<SumOfSalesPerProduct> salesList;
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
    public AllSalesManagementListViewAdapter(Context context, int resource, List<SumOfSalesPerProduct> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.salesList = objects;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AllSalesManagementListViewAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new AllSalesManagementListViewAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.listSaleManagement_TVPrice);
            holder.tvCount = (TextView) convertView.findViewById(R.id.listSaleManagement_TVCount);
            holder.tvProductId=(TextView) convertView.findViewById(R.id.listSaleManagement_TVProductId);


            convertView.setTag(holder);
        } else {
            holder = (AllSalesManagementListViewAdapter.ViewHolder) convertView.getTag();
        }
        if (salesList.get(position) != null) {
          //  ;
            ProductDBAdapter productDBAdapter=new ProductDBAdapter(context);
            productDBAdapter.open();
            Product product=productDBAdapter.getProductByID(Long.parseLong(salesList.get(position).get_id()));
            Log.d("product",product.toString());
            Log.d("productID",salesList.get(position).get_id()+"");
            holder.tvProductId.setText(product.getDisplayName());
            holder.tvCount.setText(salesList.get(position).getCount() + "");
            holder.tvPrice.setText(salesList.get(position).getTotal() + "");
        }
        return convertView;
    }
    class ViewHolder {
        private TextView tvPrice;
        private TextView tvProductId;
        private TextView tvCount;
    }
}
