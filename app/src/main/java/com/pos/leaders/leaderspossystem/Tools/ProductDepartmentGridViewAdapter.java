package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.R;

import java.util.List;

/**
 * Created by Win8.1 on 1/7/2018.
 */

public class ProductDepartmentGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Department> departments;
    private LayoutInflater inflater;

    public ProductDepartmentGridViewAdapter(Context context, List<Department> departments) {
        this.context = context;
        this.departments = departments;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return departments.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return departments.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return (long) departments.get(position).getDepartmentId();
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
        ProductDepartmentGridViewAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_product_department, parent, false);

            holder = new ProductDepartmentGridViewAdapter.ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.gridViewItemDepartment_TVName);

            convertView.setTag(holder);
        } else {
            holder = (ProductDepartmentGridViewAdapter.ViewHolder) convertView.getTag();
        }

        Department dep = departments.get(position);

        holder.tvName.setText(dep.getName());

        if (dep.isChecked()) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.pressed_color));
        } else {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.transparent));
        }

        return convertView;
    }

    public void updateRecords(List<Department> departments){
        this.departments = departments;

        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvName;
    }
}
