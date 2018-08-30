package com.pos.leaders.leaderspossystem.Tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by KARAM on 20/10/2016.
 */

public class SaleDetailsListViewAdapter extends ArrayAdapter implements OnClickListener {
    private static final int MINCHARNUMBER = 50;
    private List<OrderDetails> orderList;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private int selected = -1;
    ViewHolder holder = null;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public SaleDetailsListViewAdapter(Context context, int resource, List<OrderDetails> objects) {
        super(context, resource, objects);
        this.context = context;
        orderList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ArrayList<String>offerNames= new ArrayList<>();

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.rowSaleDetails_TVProductName);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.rowSaleDetails_TVPrice);
            holder.tvCount = (TextView) convertView.findViewById(R.id.rowSaleDetails_TVCount);
            holder.tvTotal = (TextView) convertView.findViewById(R.id.rowSaleDetails_TVTotalPrice);
            holder.OfferName = (ImageView) convertView.findViewById(R.id.imOfferName);
            holder.tvOriginalPrice = (TextView) convertView.findViewById(R.id.tvOrderListOriginalPriceValue);

            holder.llMethods = (RelativeLayout) convertView.findViewById(R.id.rowSaleDetails_LLMethods);
            holder.llSalesMan = (LinearLayout) convertView.findViewById(R.id.saleManLayout);
            holder.llOfferName = (LinearLayout) convertView.findViewById(R.id.offerLayout);
            holder.discountLayout = (LinearLayout) convertView.findViewById(R.id.discountLayout);
            holder.llRowDiscount = (LinearLayout) convertView.findViewById(R.id.cartRow_llRowDiscount);
            holder.tvPercentage = (TextView) convertView.findViewById(R.id.tvDiscountPercentageAmount);
            holder.tvPercentageAmount = (TextView) convertView.findViewById(R.id.discountPercentage);
            holder.tvRowDiscountValue = (TextView) convertView.findViewById(R.id.cartRow_tvRowDiscountValue);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int count;
        double price;
        double discount = orderList.get(position).getDiscount();
        price = orderList.get(position).getPaidAmount();
        count = orderList.get(position).getQuantity();
        holder.tvName.setText(_Substring(orderList.get(position).getProduct().getDisplayName()));
        holder.tvPrice.setText(String.format(new Locale("en"), "%.2f", price) + " " + context.getString(R.string.ins));
        holder.tvCount.setText(count + "");
        holder.tvTotal.setText(String.format(new Locale("en"), "%.2f", (price * count)) + " " + context.getString(R.string.ins));
        holder.tvPercentage.setText(R.string.discount_percentage);
        holder.tvPercentageAmount.setText(String.format(new Locale("en"), "%.2f", (discount)) + " %");

        //	callPopup();
        if (selected == position && selected != -1) {
            holder.llMethods.setVisibility(View.VISIBLE);
            holder.llSalesMan.setVisibility(View.VISIBLE);
            holder.discountLayout.setVisibility(View.VISIBLE);

            convertView.setBackgroundColor(context.getResources().getColor(R.color.list_background_color));
        } else {
            holder.llMethods.setVisibility(View.GONE);
            holder.llSalesMan.setVisibility(View.GONE);
            holder.discountLayout.setVisibility(View.GONE);

            convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.llOfferName.setVisibility(View.GONE);
        //Offer
        if (orderList.get(position).getOffer() != null) {
            if (orderList.get(position).getDiscount() > 0) {
                //show offer name
                holder.llOfferName.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = orderList.get(position).getOffer().getDataAsJsonObject();
                    JSONObject action = new JSONObject(jsonObject.get("action").toString());
                    offerNames.add(orderList.get(position).getOffer().getName() + " _ " + action.getString("name"));
                    holder.tvOriginalPrice.setText(Util.makePrice(orderList.get(position).getUnitPrice() * orderList.get(position).getQuantity()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        holder.llRowDiscount.setVisibility(View.GONE);
        if (orderList.get(position).rowDiscount != 0) {
            holder.llRowDiscount.setVisibility(View.VISIBLE);
            holder.tvRowDiscountValue.setText(Util.makePrice(orderList.get(position).rowDiscount) + " %");
        }
        holder.OfferName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle(getContext().getString(R.string.offer_name))
                        .setMessage( "\n" + offerNames.toString())
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return convertView;
    }


    public void setSelected(int selected) {
        this.selected = selected;
    }

    private String _Substring(String str) {
        if (str.length() > MINCHARNUMBER)
            return str.substring(0, MINCHARNUMBER);
        return str;
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder {
        private TextView tvName;
        private TextView tvCount;
        private TextView tvPrice;
        private TextView tvTotal;
        private ImageView OfferName;
        private TextView tvOriginalPrice;
        private RelativeLayout llMethods;
        private LinearLayout llSalesMan;
        private LinearLayout discountLayout;
        private TextView tvPercentage;
        private TextView tvPercentageAmount;
        private LinearLayout llOfferName;
        private LinearLayout llRowDiscount;
        private TextView tvRowDiscountValue;
    }
}




