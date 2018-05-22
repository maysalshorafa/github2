package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.R;

import java.util.List;

/**
 * Created by Win8.1 on 5/21/2018.
 */

public class DiscountProductInMainActivityGridViewAdapter extends BaseAdapter {

    Context context;
    List<Order> orders;
    private LayoutInflater inflater;

    private static final int MINCHARNUMBER = 12;

    public DiscountProductInMainActivityGridViewAdapter(Context context,List<Order> orders) {
        this.context=context;
        this.orders=orders;

    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return orders.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Order getItem(int position) {
        return orders.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return orders.get(position).getId();
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView=convertView;
        if(convertView==null){
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView=inflater.inflate(R.layout.discount_product_grid_view_item,null);
        }

        TextView tvName=(TextView)gridView.findViewById(R.id.itemProductCatalog_TVName);
        TextView tvTotalPrice=(TextView)gridView.findViewById(R.id.itemProductCatalog_TVTotalPrice);
        TextView tvPrice=(TextView)gridView.findViewById(R.id.itemProductCatalog_TVPrice);
        TextView tvCount=(TextView)gridView.findViewById(R.id.itemProductCatalog_TVCount);

        tvName.setText(_Substring(orders.get(position).getProduct().getName()));
        tvTotalPrice.setText(orders.get(position).getProduct().getPrice()+"");
        tvPrice.setText(orders.get(position).getProduct().getPrice()+ " " +  context.getString(R.string.ins));
        tvCount.setText(orders.get(position).getCount()+"");
        tvTotalPrice.setText(Util.makePrice(_totalPrice())+context.getString(R.string.ins));
        return gridView;
    }
    private String _Substring(String str){
        if(str.length()>MINCHARNUMBER)
            return str.substring(0,MINCHARNUMBER);
        return str;
    }
    private double _totalPrice(){

        double totalPrice = 0.0;
        for (int i= 0; i<orders.size();i++){
            totalPrice+=orders.get(i).getPrice();
        }
        return totalPrice;
    }

}
