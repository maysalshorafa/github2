package com.pos.leaders.leaderspossystem.Offers;

import android.content.Context;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupsResourceDbAdapter;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfferController {
    public static List<Offer> getOffersForResource(Long resourceId, String sku,long productCategoryId, Context context) {
        //store the group and product ids with his type
        Map<Long, ResourceType> offersResourceAndId = new HashMap<>();
        Map<Long, ResourceType> offersResourceAndIdForCategory = new HashMap<>();


        //collect all offers to this product
        //PRODUCT
        offersResourceAndId.put(resourceId, ResourceType.PRODUCT);
        //GROUP
        GroupsResourceDbAdapter groupsResourceDbAdapter = new GroupsResourceDbAdapter(context);

        groupsResourceDbAdapter.open();
        List<Long> groupsIdsForProduct = groupsResourceDbAdapter.getGroupsIdByProductSku(sku);
        List<Long> groupsIdsForProductCategory = groupsResourceDbAdapter.getGroupsIdByProductCategory(productCategoryId);

        groupsResourceDbAdapter.close();


        if (groupsIdsForProduct!=null) {
            for (Long l : groupsIdsForProduct) {
                offersResourceAndId.put(l, ResourceType.MULTIPRODUCT);
            }
        }
        if (groupsIdsForProductCategory!=null) {
            for (Long l : groupsIdsForProductCategory) {
                offersResourceAndIdForCategory.put(l, ResourceType.CATEGORY);
            }
        }

        //search on database for offer for this resource id
        OfferDBAdapter offerDBAdapter = new OfferDBAdapter(context);
        List<Offer> offers = null;
        offers = new ArrayList<>();

        if(!offersResourceAndId.isEmpty()) {
            offerDBAdapter.open();
            for (Map.Entry<Long, ResourceType> entry : offersResourceAndId.entrySet()) {
                List<Offer> allActiveOffersByResourceIdResourceType = offerDBAdapter.getAllActiveOffersByResourceIdResourceType(entry.getKey(), entry.getValue());

                if(allActiveOffersByResourceIdResourceType!=null)
                    offers.addAll(allActiveOffersByResourceIdResourceType);
            }
            offerDBAdapter.close();
        }
        if(!offersResourceAndIdForCategory.isEmpty()) {
            offerDBAdapter.open();
            for (Map.Entry<Long, ResourceType> entry : offersResourceAndIdForCategory.entrySet()) {
                List<Offer> allActiveOffersByResourceIdResourceType = offerDBAdapter.getAllActiveOffersByResourceIdResourceType(entry.getKey(), entry.getValue());

                if(allActiveOffersByResourceIdResourceType!=null)
                    offers.addAll(allActiveOffersByResourceIdResourceType);
            }
            offerDBAdapter.close();
        }

        //sort the offer list, last created first
        Collections.sort(offers, new Comparator<Offer>() {
            @Override
            public int compare(Offer offer, Offer offer2) {
                return offer.getCreatedAt().compareTo(offer2.getCreatedAt());
            }
        });

        return offers;
    }    public static boolean check(Offer offer, OrderDetails orderDetails) throws JSONException {
        JSONObject rules = offer.getDataAsJsonObject().getJSONObject(Rules.RULES.getValue());
        if (rules.getInt(Rules.quantity.getValue())<1) {
            return false;
        }
        return (orderDetails.getQuantity() / rules.getInt(Rules.quantity.getValue())) > 0;
    }

    public static OrderDetails execute(Offer offer, OrderDetails orderDetails) throws JSONException {

        JSONObject action = offer.getDataAsJsonObject().getJSONObject(Action.ACTION.getValue());
        JSONObject rules = offer.getDataAsJsonObject().getJSONObject(Rules.RULES.getValue());
        String actionName = action.getString(Action.NAME.getValue());
        int quantity = rules.getInt(Rules.quantity.getValue());
        orderDetails.offer = offer;
        if(actionName.equalsIgnoreCase(Action.GET_GIFT_PRODUCT.getValue())){
            if (orderDetails.getQuantity()  >= quantity+ 1) {
                int productGroup = orderDetails.getQuantity() / (quantity + 1);

                double totalPriceBeforeDiscount = orderDetails.getQuantity() * orderDetails.getUnitPrice();
                double discount = (1 - (((orderDetails.getQuantity() - productGroup) * orderDetails.getUnitPrice()) / totalPriceBeforeDiscount)) * 100;

                orderDetails.setDiscount(discount);
            } else {
                orderDetails.setDiscount(0);
                orderDetails.offer = null;
            }
        } else if (actionName.equalsIgnoreCase(Action.PRICE_FOR_PRODUCT.getValue())) {
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
