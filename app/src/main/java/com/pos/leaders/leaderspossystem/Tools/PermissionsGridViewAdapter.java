package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;

import com.pos.leaders.leaderspossystem.Models.Permission.Permissions;
import com.pos.leaders.leaderspossystem.R;

import java.util.List;

/**
 * Created by KARAM on 29/10/2016.
 */

public class PermissionsGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<Permissions> permissionses;
	private LayoutInflater inflater;
	///Permissions permissionsObject;

	public PermissionsGridViewAdapter(Context context, List<Permissions> permissionses) {
		this.context = context;
		this.permissionses = permissionses;
	//	permissionsObject = new Permissions();

	}

	/**
	 * How many items are in the data set represented by this Adapter.
	 *
	 * @return Count of items.
	 */
	@Override
	public int getCount() {
		return permissionses.size();
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
		return permissionses.get(position);
	}

	/**
	 * Get the row id associated with the specified position in the list.
	 *
	 * @param position The position of the item within the adapter's data set whose row id we want.
	 * @return The id of the item at the specified position.
	 */
	@Override
	public long getItemId(int position) {
		return (long)permissionses.get(position).getId();
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
			gridView=inflater.inflate(R.layout.grid_view_permissions,null);
		}
		Switch sw=(Switch)gridView.findViewById(R.id.gridViewPermissions_Switch);

		sw.setText(permissionses.get(position).getName());
		sw.setChecked(permissionses.get(position).isChecked());

		//set id for the adapter row here.
	//	sw.setId(permissionsObject.getId());

			return gridView;
	}
}
