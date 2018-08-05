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
        String s=offer.getDataAsJson().toString();
        JSONObject jsonObject = new JSONObject(s);
        JSONObject rules = new JSONObject(jsonObject.get("rules").toString());

    return orderDetails.getQuantity()% rules.getInt("quantity") >= 0;
    }

    public static OrderDetails execute(Offer offer, OrderDetails orderDetails) throws JSONException {
        OrderDetails od = orderDetails;
        String s=offer.getDataAsJson().toString();
        JSONObject jsonObject = new JSONObject(s);
        JSONObject action = new JSONObject(jsonObject.get("action").toString());
        JSONObject rules = new JSONObject(jsonObject.get("rules").toString());
        String actionName = action.getString("name");
        int quantity = rules.getInt("quantity");
        if(actionName.equals("Get gift product")){
            od.offer = offer;

        } else if (actionName.equals("Price for Product")) {

                od.offer = offer;
             od.setDiscount(1/quantity);
            }


        return od;
    }
}
