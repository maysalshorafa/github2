package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DocumentType;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ProductSale;
import com.pos.leaders.leaderspossystem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by LeadPos on 12/11/19.
 */

public class AllSalesManagementListViewAdapter  extends ArrayAdapter {
    private List<Object> salesList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     * @param objects  The objects to represent in the ListView.
     */
    public AllSalesManagementListViewAdapter(Context context, int resource, List<Object> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.salesList = objects;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AllSalesManagementListViewAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new AllSalesManagementListViewAdapter.ViewHolder();
            convertView = inflater.inflate(resource, null);
           // holder.tvID = (TextView) convertView.findViewById(R.id.listSaleManagement_TVSaleID);
            //	holder.tvNumberOfItems = (TextView) convertView.findViewById(R.id.listSaleManagement_TVNumberOfItems);
           // holder.tvCustomerName = (TextView) convertView.findViewById(R.id.listSaleManagement_TVCustomerName);
           // holder.tvDate = (TextView) convertView.findViewById(R.id.listSaleManagement_TVDate);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.listSaleManagement_TVPrice);
            //holder.tvStatus = (TextView) convertView.findViewById(R.id.listSaleManagement_TVStatus);
          //  holder.FL = (LinearLayout) convertView.findViewById(R.id.listSaleManagement_FLMore);
           holder.tvCount = (TextView) convertView.findViewById(R.id.listSaleManagement_TVCount);
          //  holder.cancelingOrderId = (TextView) convertView.findViewById(R.id.listSaleManagement_TVCancelOrderId);
            holder.tvProductId=(TextView) convertView.findViewById(R.id.listSaleManagement_TVProductId);


            convertView.setTag(holder);
        } else {
            holder = (AllSalesManagementListViewAdapter.ViewHolder) convertView.getTag();
        }
        //	Order order = (Order)salesList.get(position);


        int count=0;
        double priceproduct = 0;
        if(salesList.get(position)instanceof ProductSale){
            ProductSale productSale=(ProductSale) salesList.get(position);
            /*double price;
            List<OrderDetails> productId,prductId2;

            Product product,product1;

            ProductDBAdapter productDBAdapter1=new ProductDBAdapter(context);
            productDBAdapter1.open();
            Order order = (Order)salesList.get(position);
            price = order.getTotalPrice();
            *//*holder.tvID.setText(order.getOrderId() + "");
            holder.tvDiscount.setText(order.getCartDiscount()+"");*//*
            OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(context);
            orderDetailsDBAdapter.open();

               order.setOrders(orderDetailsDBAdapter.getOrderBySaleID(order.getOrderId()));
                productId= orderDetailsDBAdapter.getOrderBySaleID(order.getOrderId());
            for(int i=0;i<productId.size();i++){
                Product p = productId.get(i).getProduct();

            }
            //    product=productDBAdapter1.getProductByID(productId);
                 Log.d("productId",productId+"");
                for (int j=position+1;j<salesList.size();j=j+1){
                    Order order2 = (Order)salesList.get(j);
                    Log.d("Order2",order2.toString()+"");
                  //  prductId2= orderDetailsDBAdapter.getIdProductByIDOrder(order.getOrderId());
                    product1=productDBAdapter1.getProductByID(prductId2);
                    Log.d("productId2",prductId2+"");
                    Log.d("product1",product1.toString());
                    if (prductId2==productId){
                        count=count+1;
                        Log.d("productId2Equal",prductId2+"");
                        priceproduct+=productDBAdapter1.getProductPrice(prductId2);
                        Log.d("count",count+"");
                        Log.d("price",priceproduct+"price");
                    }



                }*/

                 holder.tvProductId.setText(productSale.getProduct().getDisplayName());

                  holder.tvCount.setText(productSale.getCount()+"");
                  holder.tvPrice.setText(productSale.getPrice()+"");
             /*    if (product.getDisplayName()!=null&& !product.getDisplayName().equals("") ){
                     holder.tvProductId.setText(product.getDisplayName());
                 }
                 else {
                     holder.tvProductId.setText("");
                 }
                Log.d("position",position+""+count+"count"+priceproduct+"priceProduct");
*/


            //holder.tvNumberOfItems.setText(order.getNumberOfItems()+"");

          /*  if (order.getCustomer_name() != null && !order.getCustomer_name().equals("")) {
                holder.tvCustomerName.setText(order.getCustomer_name());
            } else{
                holder.tvCustomerName.setText(context.getString(R.string.general_customer));
            }
            if(order.getCancellingOrderId()>0){
                holder.cancelingOrderId.setText(order.getCancellingOrderId()+"");
            }
            else {
                holder.cancelingOrderId.setText("");
            }

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            holder.tvDate.setText(String.format(new Locale("en"),format.format(order.getCreatedAt())));
            holder.tvPrice.setText(Util.makePrice(price) + " " + context.getString(R.string.ins));

            holder.tvStatus.setText("INRC");

           // holder.FL.setVisibility(View.GONE);

            try {
                convertView.setBackground(context.getResources().getDrawable(R.color.sale_bg));

		*//*	if (order.getPayment().getPaymentWay().equals(CONSTANT.CREDIT_CARD)) {
				convertView.setBackground(context.getResources().getDrawable(R.color.credit_card_bg));
			} else if (order.getPayment().getPaymentWay().equals(CONSTANT.CHECKS)) {
				convertView.setBackground(context.getResources().getDrawable(R.color.check_bg));
			} else {
				convertView.setBackground(context.getResources().getDrawable(R.color.sale_bg));
			}*//*
            } catch (Exception e) {
                //e.printStackTrace();
                convertView.setBackground(context.getResources().getDrawable(R.color.sale_bg));
            }*/
        }else {
           /* try {
                holder.cancelingOrderId.setText("");
                double price;
                Log.d("teeest",position+"  " +salesList.get(position).toString());
                Log.d("teeest",salesList.get(position).toString());

                BoInvoice boInvoice = (BoInvoice)salesList.get(position);;
                JSONObject doc = boInvoice.getDocumentsData();
                holder.tvID.setText(boInvoice.getDocNum() );

                if(boInvoice.getType().equals(DocumentType.INVOICE)) {
                    holder.tvStatus.setText("IN");
                }else {
                    holder.tvStatus.setText("CIN");
                }
                price = doc.getDouble("total");
                holder.tvDiscount.setText(doc.getDouble("cartDiscount")+"");
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                Date date = DateConverter.stringToDate(doc.getString("date"));

                holder.tvDate.setText(DateConverter.geDate(date));
                holder.tvPrice.setText(Util.makePrice(price) + " " + context.getString(R.string.ins));
                JSONObject customerJson= doc.getJSONObject("customer");
                holder.tvCustomerName.setText(customerJson.get("firstName")+" "+customerJson.get("lastName"));
                int count =0;
                JSONArray jsonArray = doc.getJSONArray("cartDetailsList");
                for (int c = 0;c<jsonArray.length();c++){
                    JSONObject j = jsonArray.getJSONObject(c);
                    count+=j.getInt("quantity");
                }
                //	holder.tvNumberOfItems.setText(count+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
         //   holder.FL.setVisibility(View.GONE);
        }

        return convertView;
    }
    class ViewHolder {
        private TextView tvID;
        //private TextView tvNumberOfItems;
        private TextView tvCustomerName;
        private TextView tvDate;
        private TextView tvPrice;
        private TextView tvStatus;
        private TextView tvProductId;
       // private LinearLayout FL;
        private Button btCancel;
        private Button btReturn;
        private Button btView;
        private Button btDublicte;
        private TextView tvCount;
        private TextView cancelingOrderId;
    }
}
