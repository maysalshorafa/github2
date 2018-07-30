package com.pos.leaders.leaderspossystem.Offers;

import android.content.Context;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;

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

    public static boolean check(Offer offer, OrderDetails orderDetails) {
        //supporting offers
        /*
        {\"rules\":{\"product_sku\":hh, \"quantity\":55} ,\"action\":{\"Get gift product\"}}
        {"rules":{"product_sku":hh, "quantity":55} ,"action":{"Price for Product":66.0}}
         */
        return offer.getDataAsJson().get("rules").get("quantity").intValue() >= orderDetails.getQuantity();
    }

    public static OrderDetails execute(Offer offer, OrderDetails orderDetails) {
        if(!check(offer,orderDetails)) return orderDetails;
        OrderDetails od = orderDetails;
        String actionName = offer.getDataAsJson().get("action").get("name").textValue();
        int quantity = offer.getDataAsJson().get("rules").get("quantity").intValue();
        if(actionName.equals("Get gift product")){
            od.setDiscount(1 / (quantity + 1));
            od.increaseCount();
            od.offer = offer;
        } else if (actionName.equals("Price for Product")) {
            od.setDiscount(1 - (100 / (od.getUnitPrice() * quantity)));
            od.offer = offer;
        }
        return od;
    }
}
