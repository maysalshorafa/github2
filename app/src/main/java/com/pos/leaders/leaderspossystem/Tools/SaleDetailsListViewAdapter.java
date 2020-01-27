package com.pos.leaders.leaderspossystem.Tools;

        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
        import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
        import com.pos.leaders.leaderspossystem.Models.Offer;
        import com.pos.leaders.leaderspossystem.Models.OrderDetails;
        import com.pos.leaders.leaderspossystem.Models.Product;
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
    final ArrayList<String>offerNames= new ArrayList<>();
    final ArrayList<Double>offerDiscount= new ArrayList<>();

    int a=1;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.rowSaleDetails_TVProductName);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.rowSaleDetails_TVPrice);
            holder.tvCount = (TextView) convertView.findViewById(R.id.rowSaleDetails_TVCount);
            holder.tvTotal = (TextView) convertView.findViewById(R.id.rowSaleDetails_TVTotalPrice);
            holder.OfferName = (ImageView) convertView.findViewById(R.id.imOfferName);
            holder.tvOriginalPrice = (TextView) convertView.findViewById(R.id.tvOrderListOriginalPriceValue);

            holder.llMethods = (LinearLayout) convertView.findViewById(R.id.rowSaleDetails_LLMethods);
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
        ProductDBAdapter productDBAdapter =new  ProductDBAdapter(context);
        productDBAdapter.open();
        Product product =productDBAdapter.getProductByID(orderList.get(position).getProductId());
        productDBAdapter.close();
        String currencyType = "";
        try {

            holder.tvName.setText(_Substring(product.getDisplayName()));
            if (product.getCurrencyType() == 0) {
                currencyType = context.getString(R.string.ins);
            }
            if (product.getCurrencyType() == 1) {
                currencyType = context.getString(R.string.dolor_sign);
            }
            if (product.getCurrencyType() == 2) {
                currencyType = context.getString(R.string.gbp);
            }
            if (product.getCurrencyType() == 3) {
                currencyType = context.getString(R.string.eur);
            }

        }
        catch(NullPointerException e)
        {
            Log.d("NullPointerException",e.toString());
        }

        holder.tvPrice.setText(String.format(new Locale("en"), "%.2f", orderList.get(position).getUnitPrice()) + " " + currencyType);
        holder.tvCount.setText(count + "");
        holder.tvTotal.setText(String.format(new Locale("en"), "%.2f", (price )) + " " + currencyType);
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
                    boolean status = true;
                    for (int i = 0; i < offerNames.size(); i++) {
                        if (offerNames.get(i).equals(orderList.get(position).getOffer().getName() + " _ " + action.getString("name"))) {
                            status=false;
                        }
                    }
                    if(status){
                        OfferDBAdapter offerDBAdapter = new OfferDBAdapter(context);
                        offerDBAdapter.open();
                        Offer offer = offerDBAdapter.getOfferById(orderList.get(position).getProduct().getOfferId());
                        offerDBAdapter.close();
                        offerNames.add(offer.getName() + " _ " + offer.getActionName());
                        offerDiscount.add(orderList.get(position).getDiscount());
                    }
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
        final View finalConvertView = convertView;
        holder.OfferName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a=1;
                OfferDBAdapter offerDBAdapter = new OfferDBAdapter(context);
                offerDBAdapter.open();
                Offer offer = offerDBAdapter.getOfferById(orderList.get(position).getProduct().getOfferId());
                offerDBAdapter.close();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(finalConvertView.getRootView().getContext());
                alertDialogBuilder .setTitle(getContext().getString(R.string.offer_name));
                alertDialogBuilder.setMessage(offer.getName());
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

                alertDialogBuilder.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
        private LinearLayout llMethods;
        private LinearLayout llSalesMan;
        private LinearLayout discountLayout;
        private TextView tvPercentage;
        private TextView tvPercentageAmount;
        private LinearLayout llOfferName;
        private LinearLayout llRowDiscount;
        private TextView tvRowDiscountValue;
    }
}
