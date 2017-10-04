package com.pos.leaders.leaderspossystem.Tools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.AddUserActivity;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerAssetDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by KARAM on 20/10/2016.
 */

public class SaleDetailsListViewAdapter extends ArrayAdapter implements OnClickListener {


	private  PopupWindow popupWindow;
	public UserDBAdapter userDB;

	private static final int MINCHARNUMBER = 7;
	private List<Order> orderList;
	private List<User> custmerAssestList;
	private List<User> AllCustmerAssestList;

	private int resource;
	private LayoutInflater inflater;
	private Context context;
	private int selected=-1;
	private  ListView lvcustmerAssest;
	private  EditText custmerAssest;
	private  Button btn_cancel;
	boolean userScrolled =false;
	public CustomerAssetDB custmerAssetDB ;
	public CustmerAssestCatlogGridViewAdapter custmerCatalogGridViewAdapter;
	ViewHolder holder=null;
	/**
	 * Constructor
	 *
	 * @param context  The current context.
	 * @param resource The resource ID for a layout file containing a TextView to use when
	 *                 instantiating views.
	 * @param objects  The objects to represent in the ListView.
	 */
	public SaleDetailsListViewAdapter(Context context, int resource, List<Order> objects) {
		super(context, resource, objects);
		this.context=context;
		orderList=objects;
		this.resource=resource;
		inflater=(LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(resource,null);
			holder.tvName=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVProductName);
			holder.tvPrice=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVPrice);
			holder.tvCount=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVCount);
			holder.tvTotal=(TextView)convertView.findViewById(R.id.rowSaleDetails_TVTotalPrice);
			holder.saleMan=(TextView)convertView.findViewById(R.id.saleMan);
			holder.llMethods=(RelativeLayout)convertView.findViewById(R.id.rowSaleDetails_LLMethods);
			userDB=new UserDBAdapter(context);
custmerAssetDB=new CustomerAssetDB(context);
			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder)convertView.getTag();
		}
		int count;
		double price;
		price=orderList.get(position).getPrice();
		count=orderList.get(position).getCount();
		holder.tvName.setText(_Substring(orderList.get(position).getProduct().getName()));
		holder.tvPrice.setText(String.format(new Locale("en"),"%.2f",price)+" "+ context.getString(R.string.ins));
		holder.tvCount.setText(count+"");
		holder.tvTotal.setText(String.format(new Locale("en"),"%.2f",(price*count))+ " " + context.getString(R.string.ins));
		holder . saleMan.setOnClickListener(this);

		//	callPopup();

		userDB.open();
		custmerAssetDB.open();
		if(selected==position&&selected!=-1){
			holder.llMethods.setVisibility(View.VISIBLE);
			convertView.setBackgroundColor(context.getResources().getColor(R.color.list_background_color));
		}
		else {
			holder.llMethods.setVisibility(View.GONE);
			convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
		}
		return convertView;
	}



public 	class ViewHolder{
		private TextView tvName;
		private TextView tvCount;
		private TextView tvPrice;
		private TextView tvTotal;
		public TextView saleMan;
		private RelativeLayout llMethods;


	}


	public void setSelected(int selected) {
		this.selected = selected;
	}

	private String _Substring(String str){
		if(str.length()>MINCHARNUMBER)
			return str.substring(0,MINCHARNUMBER);
		return str;
	}

	private void callPopup() {


		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = layoutInflater.inflate(R.layout.custmer_assest_popup, null);
		popupWindow = new PopupWindow(popupView, 1000, ActionBar.LayoutParams.WRAP_CONTENT,
				true);

		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
		custmerAssest = (EditText) popupView.findViewById(R.id.customerAssest_name);

		lvcustmerAssest=(ListView) popupView.findViewById(R.id.custmerAssest_list_view);

		btn_cancel=(Button) popupView.findViewById(R.id.btn_cancel) ;


		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

		((Button) popupView.findViewById(R.id.btn_add))
				.setOnClickListener(new View.OnClickListener() {

					@TargetApi(Build.VERSION_CODES.GINGERBREAD)
					public void onClick(View arg0) {
						Intent intent = new Intent(getContext(), AddUserActivity.class);
						context.startActivity(intent);

						popupWindow.dismiss();


					} });



		custmerAssest.setText("");
		custmerAssest.setHint("Search..");

		custmerAssest.setFocusable(true);
		custmerAssest.requestFocus();
		custmerAssest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				custmerAssest.setFocusable(true);
			}
		});

		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		lvcustmerAssest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
SESSION._ORDER.setCustmerAssestId(custmerAssestList.get(position).getId());
			//	custmerAssetDB.insertEntry(SESSION._ORDER.getId(),custmerAssestList.get(position).getId(),SESSION._ORDER.getItemTotalPrice(),0);

			}
		});
		lvcustmerAssest.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
					userScrolled = true;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (userScrolled && firstVisibleItem + visibleItemCount == totalItemCount) {

					userScrolled = false;
					//  loadMoreProduct();
				}
			}
		});

		custmerAssestList = userDB.getAllSalesMAn();
		AllCustmerAssestList = custmerAssestList;

		custmerCatalogGridViewAdapter = new CustmerAssestCatlogGridViewAdapter(context, custmerAssestList);

		lvcustmerAssest.setAdapter(custmerCatalogGridViewAdapter);




	}




	@Override
	public void onClick(View v) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		custmerAssestList = userDB.getAllSalesMAn();
			List<String> user = new ArrayList<String>();
			for (int i = 0, size = custmerAssestList.size(); i < size; i++) {
				user.add(
						custmerAssestList.get(i).getFullName());
			}

			//Create sequence of items
			final CharSequence[] User = user.toArray(new String[user.size()]);

			for (final Order o : SESSION._ORDERS) {
				builder.setItems(User, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						final long finalI = custmerAssestList.get(item).getId();
						o.setCustmerAssestId(finalI);

					}
				});
			}




			builder.setTitle("Choose Custmer Assest");

			AlertDialog alert = builder.create();
			alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);

			alert.show();

		}}




