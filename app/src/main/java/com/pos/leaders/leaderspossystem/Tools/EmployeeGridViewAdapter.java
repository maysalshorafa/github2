package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.R;

import java.util.List;

/**
 * Created by KARAM on 25/10/2016.
 */

public class EmployeeGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<Employee> workers;
	private LayoutInflater inflater;

	public EmployeeGridViewAdapter(Context context, List<Employee> workers) {
		this.context = context;
		this.workers = workers;
	}


	/**
	 * How many items are in the data set represented by this Adapter.
	 *
	 * @return Count of items.
	 */
	@Override
	public int getCount() {
		return workers.size();
	}



	/**
	 * Get the data item associated with the specified position in the data set.
	 *
	 * @param position Position of the item whose data we want within the adapter's
	 *                 data set.
	 * @return The data at the specified position.
	 */
	@Override
	public Employee getItem(int position) {
		return workers.get(position);
	}



	/**
	 * Get the row usedPointId associated with the specified position in the list.
	 *
	 * @param position The position of the item within the adapter's data set whose row usedPointId we want.
	 * @return The usedPointId of the item at the specified position.
	 */
	@Override
	public long getItemId(int position) {
		return (long)workers.get(position).getEmployeeId();
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
			gridView=inflater.inflate(R.layout.grid_view_user,null);
		}
		TextView tvFullName=(TextView)gridView.findViewById(R.id.workerGridView_TVFullName);
		tvFullName.setText(workers.get(position).getFullName());
/**
		TextView tvUserName=(TextView)gridView.findViewById(R.usedPointId.workerGridView_TVUserName);
		TextView tvPhoneNumber=(TextView)gridView.findViewById(R.usedPointId.workerGridView_TVPhoneNumber);
		TextView tvPresent=(TextView)gridView.findViewById(R.usedPointId.workerGridView_TVPresent);

		tvUserName.setText(workers.get(position).getEmployeeName());
		tvPhoneNumber.setText(workers.get(position).getPhoneNumber());
		tvPresent.setText(workers.get(position).getPresent()+"");
**/
		return gridView;
	}
}
