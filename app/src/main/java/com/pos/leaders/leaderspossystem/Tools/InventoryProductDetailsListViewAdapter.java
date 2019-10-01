package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductInventoryDbAdapter;
import com.pos.leaders.leaderspossystem.Models.ProductInventory;
import com.pos.leaders.leaderspossystem.R;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Win8.1 on 7/8/2019.
 */

public class InventoryProductDetailsListViewAdapter extends ArrayAdapter implements View.OnClickListener {
    private static final int MINCHARNUMBER = 50;
    private List<ProductInventory> productList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private int selected = -1;
    InventoryProductDetailsListViewAdapter.ViewHolder holder = null;

    int a=1;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public InventoryProductDetailsListViewAdapter(Context context, int resource, List<ProductInventory> objects) {
        super(context, resource, objects);
        this.context = context;
        productList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new InventoryProductDetailsListViewAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.rowInventoryDetails_TVProductName);
            holder.tvCount = (TextView) convertView.findViewById(R.id.rowInventoryDetails_TVCount);
            holder.llMethods = (LinearLayout) convertView.findViewById(R.id.rowInventoryDetails_LLMethods);

            convertView.setTag(holder);
        } else {
            holder = (InventoryProductDetailsListViewAdapter.ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(_Substring(productList.get(position).getName()));
        String currencyType="";
        ProductInventoryDbAdapter productInventoryDbAdapter = new ProductInventoryDbAdapter(context);
        productInventoryDbAdapter.open();
        holder.tvCount.setText(1+ "");
        if (selected == position && selected != -1) {
            holder.llMethods.setVisibility(View.VISIBLE);

            convertView.setBackgroundColor(context.getResources().getColor(R.color.list_background_color));
        } else {
            holder.llMethods.setVisibility(View.GONE);


            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        return convertView;
    }


    public void setSelected(int selected) {
        this.selected = selected;
    }

    private String _Substring(String str) {
        if (str.length() > MINCHARNUMBER)
            return str.substring(0, MINCHARNUMBER);
        return str;
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder {
        private TextView tvName;
        private TextView tvCount;
        private LinearLayout llMethods;
    }
}