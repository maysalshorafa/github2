package com.pos.leaders.leaderspossystem.Offers;

import android.content.Context;

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
        JSONObject rules = offer.getDataAsJsonObject().getJSONObject(Rules.RULES.getValue());

        return (orderDetails.getQuantity() / rules.getInt(Rules.quantity.getValue())) > 0;
    }

    public static OrderDetails execute(Offer offer, OrderDetails orderDetails) throws JSONException {

        JSONObject action = offer.getDataAsJsonObject().getJSONObject(Action.ACTION.getValue());
        JSONObject rules = offer.getDataAsJsonObject().getJSONObject(Rules.RULES.getValue());
        String actionName = action.getString(Action.NAME.getValue());
        int quantity = rules.getInt(Rules.quantity.getValue());
        orderDetails.offer = offer;
        if(actionName.equals(Action.GET_GIFT_PRODUCT.getValue())){
            if (orderDetails.getQuantity()  >= quantity+ 1) {
                int productGroup = orderDetails.getQuantity() / (quantity + 1);

                double totalPriceBeforeDiscount = orderDetails.getQuantity() * orderDetails.getUnitPrice();
                double discount = (1 - (((orderDetails.getQuantity() - productGroup) * orderDetails.getUnitPrice()) / totalPriceBeforeDiscount)) * 100;

                orderDetails.setDiscount(discount);
            } else {
                orderDetails.setDiscount(0);
                orderDetails.offer = null;
            }
        } else if (actionName.equals(Action.PRICE_FOR_PRODUCT.getValue())) {
            double value = action.getDouble(Action.VALUE.getValue());
            orderDetails.setDiscount(0);
            if (orderDetails.getQuantity() >= quantity) {

                int productGroup = orderDetails.getQuantity() / quantity;
                int productCountWithOutProductIntoOffer = orderDetails.getQuantity() - (productGroup * quantity);
                double discount = (1 -
                        (((productGroup * value) + (productCountWithOutProductIntoOffer * orderDetails.getUnitPrice()))
                                / (orderDetails.getUnitPrice() * orderDetails.getQuantity()))) * 100;

                orderDetails.setDiscount(discount);
            } else {
                orderDetails.setDiscount(0);
                orderDetails.offer = null;
            }
        }

        return orderDetails;
    }
}
