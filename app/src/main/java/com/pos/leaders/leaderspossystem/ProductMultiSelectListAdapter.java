package com.pos.leaders.leaderspossystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.List;

public class ProductMultiSelectListAdapter extends RecyclerView.Adapter<ProductMultiSelectListAdapter.ViewHolder>{

    public List<Product> products = new ArrayList<>();
    public List<Product> selectedProducts = new ArrayList<>();

    private static final int MINCHARNUMBER = 12;

    Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,barcode,price;
        LinearLayout llItem;
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.itemProductCatalog_TVName);
            barcode = (TextView) view.findViewById(R.id.itemProductCatalog_TVBarCode);
            price = (TextView) view.findViewById(R.id.itemProductCatalog_TVPrice);
        //    llItem = (LinearLayout) view.findViewById(R.id.itemProductCatalog_llBackground);
        }
    }

    public ProductMultiSelectListAdapter(List<Product> products, List<Product> selectedProducts, Context context) {
        this.products = products;
        this.selectedProducts = selectedProducts;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_item_product_catalog, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product p = products.get(position);
        holder.name.setText(_Substring(p.getDisplayName()));
        holder.price.setText(Util.makePrice(p.getPriceWithTax()));
        holder.barcode.setText(p.getSku());

        if (selectedProducts.contains(products.get(position))) {
            holder.llItem.setPressed(true);
            //selected item
        } else {
            //not selected item
            holder.llItem.setPressed(false);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    private String _Substring(String str){
        if(str.length()>MINCHARNUMBER)
            return str.substring(0,MINCHARNUMBER);
        return str;
    }
}
