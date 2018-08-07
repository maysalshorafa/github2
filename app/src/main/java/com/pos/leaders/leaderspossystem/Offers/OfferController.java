package com.pos.leaders.leaderspossystem.Offers;

import android.content.Context;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OfferController {
    public static List<Offer> getOffersForResourceId(ResourceType resourceType, long resourceId, Context context) {
        OfferDBAdapter offerDBAdapter = new OfferDBAdapter(context);
        List<Offer> offers = null;

        offerDBAdapter.open();
        offers = offerDBAdapter.getAllActiveOffersByResourceIdResourceType(resourceId, resourceType);
        offerDBAdapter.close();

        return offers;
    }

    public static boolean check(Offer offer, OrderDetails orderDetails) throws JSONException {
        JSONObject rules = offer.getDataAsJsonObject().getJSONObject("rules");

        return orderDetails.getQuantity() % rules.getInt("quantity") >= 0;
    }

    public static OrderDetails execute(Offer offer, OrderDetails orderDetails) throws JSONException {

        JSONObject action = offer.getDataAsJsonObject().getJSONObject("action");
        JSONObject rules = offer.getDataAsJsonObject().getJSONObject("rules");
        String actionName = action.getString("name");
        int quantity = rules.getInt("quantity");
        if(actionName.equals("Get gift product")){
            //todo edit excute this action
            if (orderDetails.getQuantity() >= quantity) {
                if (orderDetails.getDiscount()-orderDetails.rowDiscount != 0) {
                    double currentPrice = orderDetails.getItemTotalPrice();
                    double origanalPrice = orderDetails.getQuantity() * orderDetails.getUnitPrice();

                    int freeItem = (int) ((origanalPrice - currentPrice) / orderDetails.getUnitPrice());
                    orderDetails.setCount(orderDetails.getQuantity() - freeItem);
                    orderDetails.setDiscount(0);
                }

                int productGroup = orderDetails.getQuantity() / quantity;
                double totalPrice = orderDetails.getItemTotalPrice();

                orderDetails.setCount(orderDetails.getQuantity() + productGroup);
                double newTotalPrice = orderDetails.getItemTotalPrice();
                double discount = (1 - (totalPrice / newTotalPrice)) * 100;

                orderDetails.setDiscount(discount);
            }
        } else if (actionName.equals("Price for Product")) {
            double value = action.getDouble("value");
            orderDetails.setDiscount(0);
            if (orderDetails.getQuantity() >= quantity) {

                int productGroup = orderDetails.getQuantity() / quantity;
                int productCountWithOutProductIntoOffer = orderDetails.getQuantity() - (productGroup * quantity);
                double discount = (1 -
                        (((productGroup * value) + (productCountWithOutProductIntoOffer * orderDetails.getUnitPrice()))
                                / (orderDetails.getUnitPrice() * orderDetails.getQuantity()))) * 100;

                orderDetails.setDiscount(discount);
                Log.e("Offer Discount", "execute: " + discount);
            }
        }
        orderDetails.offer = offer;
        return orderDetails;
    }
}
