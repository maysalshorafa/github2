package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.Models.CustomerAssistant;
import com.pos.leaders.leaderspossystem.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Win8.1 on 10/26/2017.
 */

public class SalesManDetailsGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<CustomerAssistant> customerAssests;
    private LayoutInflater inflater;
    private int resource;
    int bgColor =0;
    public SalesManDetailsGridViewAdapter(Context context, int resource, List<CustomerAssistant> customerAssests) {
        this.context = context;
        this.resource = resource;
        this.context = context;
        bgColor=0;
        this.customerAssests = customerAssests;
    }
    public SalesManDetailsGridViewAdapter(Context context, List<CustomerAssistant> customerAssests) {
        this.context = context;
        this.resource = resource;
        this.context = context;
        bgColor=0;
        this.customerAssests = customerAssests;
    }




    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return customerAssests.size();
    }



    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public CustomerAssistant getItem(int position) {
        return customerAssests.get(position);
    }



    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return (long)customerAssests.get(position).getCustAssistantId();
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
            gridView=inflater.inflate(R.layout.grid_view_item_sales_man,null);
        }
       // TextView saleManId=(TextView)gridView.findViewById(R.id.salesManGridView_TVSaleManId);
        TextView tvSalesAmount=(TextView)gridView.findViewById(R.id.salesManGridView_TVAmount);
        TextView salesCase=(TextView)gridView.findViewById(R.id.salesManGridView_TVSaleCase);
        TextView salesDate=(TextView)gridView.findViewById(R.id.salesManGridView_TVSaleDate);
        TextView saleId=(TextView)gridView.findViewById(R.id.salesManGridView_TVSaleId);
        EmployeeDBAdapter userDBAdapter=new EmployeeDBAdapter(context);
        userDBAdapter.open();
        String userName=userDBAdapter.getEmployeesName(customerAssests.get(position).getCustomerAssistantID());
      //  saleManId.setText(userName);
        salesCase.setText(""+customerAssests.get(position).getSalesCase());
        tvSalesAmount.setText(Util.makePrice(customerAssests.get(position).getAmount()));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        salesDate.setText(format.format(customerAssests.get(position).getCreatedAt()));
        saleId.setText(customerAssests.get(position).getOrderId()+"");
        if(bgColor%2==0){
            gridView.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor));
        }
        bgColor++;
        return gridView;
    }}