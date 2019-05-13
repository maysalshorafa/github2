package com.pos.leaders.leaderspossystem.Tools;

        import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.R;

import java.util.List;

/**
 * Created by Win8.1 on 5/9/2019.
 */

public class FilterCategoryOfferProductDialog extends ArrayAdapter {
    private Context context;
    private List<String> offerCategoryProductList;
    private LayoutInflater inflater;
    int pos;

    public FilterCategoryOfferProductDialog(Context context, int resource, List<String> offerCategoryProductList) {
        super(context, resource,offerCategoryProductList);
        this.context = context;
        this.offerCategoryProductList = offerCategoryProductList;
    }


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return offerCategoryProductList.size();
    }



    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public String getItem(int position) {
        return offerCategoryProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */



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
        pos=position;
        View gridView=convertView;
        if(convertView==null){
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView=inflater.inflate(R.layout.grid_view_filter_category_offer_product,null);
        }
        final TextView tvName=(TextView)gridView.findViewById(R.id.gridViewItemDepartment_TVName);
        final ImageView deleteButton =(ImageView)gridView.findViewById(R.id.deleteProductFromList);
        //TextView tvCount=(TextView)gridView.findViewById(R.id.gridViewItemDepartment_TVCount);
        Log.d("teeest",offerCategoryProductList.get(position));
        //  Long productId = Long.parseLong(offerCategoryProductList.get(position));
        tvName.setText(offerCategoryProductList.get(position));
        return gridView;
    }
}
