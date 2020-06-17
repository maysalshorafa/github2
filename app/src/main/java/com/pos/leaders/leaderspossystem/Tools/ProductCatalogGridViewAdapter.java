package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.R;

import java.util.List;

/**
 * Created by KARAM on 20/10/2016.
 */

public class ProductCatalogGridViewAdapter extends BaseAdapter {

	Context context;
	List<Product> products;
	private LayoutInflater inflater;

    private static final int MINCHARNUMBER = 12;

	public ProductCatalogGridViewAdapter(Context context,List<Product> products) {
		this.context=context;
		this.products=products;

	}

	/**
	 * How many items are in the data set represented by this Adapter.
	 *
	 * @return Count of items.
	 */
	@Override
	public int getCount() {
		return products.size();
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 *
	 * @param position Position of the item whose data we want within the adapter's
	 *                 data set.
	 * @return The data at the specified position.
	 */
	@Override
	public Product getItem(int position) {
		return products.get(position);
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 *
	 * @param position The position of the item within the adapter's data set whose row id we want.
	 * @return The id of the item at the specified position.
	 */
	@Override
	public long getItemId(int position) {
		return products.get(position).getProductId();
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
		String currencyTypeSymbol;
		if(convertView==null){
			inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			gridView=inflater.inflate(R.layout.grid_view_item_product_catalog,null);
		}
		TextView tvName=(TextView)gridView.findViewById(R.id.itemProductCatalog_TVName);
		TextView tvBarcode=(TextView)gridView.findViewById(R.id.itemProductCatalog_TVBarCode);
		TextView tvPrice=(TextView)gridView.findViewById(R.id.itemProductCatalog_TVPrice);
         if (products!=null){
		tvName.setText(products.get(position).getDisplayName());
		tvBarcode.setText(products.get(position).getSku());
		 String currencyType=products.get(position).getCurrencyType();
			 if (currencyType.equals("0")){
				 currencyTypeSymbol = "₪";
			 }
			 else {
		 currencyTypeSymbol = String.valueOf(symbolWithCodeHashMap.valueOf(products.get(position).getCurrencyType()).getValue());}
		tvPrice.setText(products.get(position).getPrice() + " " + currencyTypeSymbol);}

		return gridView;
	}
    private String _Substring(String str){
        if(str.length()>MINCHARNUMBER)
            return str.substring(0,MINCHARNUMBER);
        return str;
    }

}
