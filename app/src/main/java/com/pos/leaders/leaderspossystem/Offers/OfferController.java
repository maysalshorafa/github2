package com.pos.leaders.leaderspossystem.Offers;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupsResourceDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class OfferController {

    public static List<Offer> activeOffers = new ArrayList<>();

    public static boolean executeCategoryOffers(List<OrderDetails> ods,List<Offer> offers) throws JSONException, IOException {

        boolean changes = false;
        for(Offer offer:offers) {
          //  if (!offer.isActionSameResource()) {
                JSONObject rules = offer.getRules();
                int quantity = offer.getRuleQuantity();

                int productCount = 0;

                offer.conditionList = new ArrayList<>();

                List<Long> categoryIds = new ArrayList<>();
                if (offer.getResourceType() == ResourceType.CATEGORY) {
                    categoryIds = Arrays.asList(new ObjectMapper().readValue(rules.getString(Rules.offerCategoryList.getValue()), Long[].class));
                    Log.d("teeestOffer11",categoryIds.toString());

                }
                if(offer.getActionName().equals(Action.GET_GIFT_PRODUCT.getValue())){

                //COLLECT ALL PRODUCT ON IMPLEMENT OFFER
                for (OrderDetails orderDetails : ods) {

                    if(orderDetails.giftProduct) {
                        continue;
                    }

                    if(!orderDetails.scannable) {
                        continue;
                    }


                    Log.i("orderDetails", orderDetails.toString());
                    if (offer.getResourceType() == ResourceType.CATEGORY) {
                        if (categoryIds.contains(orderDetails.getOfferCategory())) {

                            productCount += orderDetails.getQuantity();
                            offer.conditionList.add(orderDetails);
                        }
                    }
                }

                if (productCount >= quantity) {

                    //save the highest quantity price

                    //sort offer condition list

                    List<OrderDetails> tempCondition = offer.conditionList;

                    Collections.sort(tempCondition, new Comparator<OrderDetails>() {
                        @Override
                        public int compare(OrderDetails t1, OrderDetails t2) {
                            return Double.compare(t1.getUnitPrice(), t2.getUnitPrice());
                        }
                    });
                    Collections.reverse(tempCondition);

                    //reset offer condition list
                    offer.conditionList = new ArrayList<>();
                    // select first highest quantity
                    int targetQuantity = 0;
                    while (targetQuantity < quantity) {
                        OrderDetails od = tempCondition.remove(0);

                        if (targetQuantity + od.getQuantity() > quantity) {
                            //must split this order details
                            List<OrderDetails> spitedOrderDetails = splitOrderDetails(od, quantity - targetQuantity);
                            //reset on the main orders list
                            ods.remove(od);


                            OrderDetails cond = spitedOrderDetails.get(0);

                            cond.scannable = false;
                            ods.add(cond);
                           // spitedOrderDetails.get(1).setUnitPrice(0);
                            ods.add(spitedOrderDetails.get(1));

                            //rejoin the two item to the first of the tem list
                            tempCondition.add(0, spitedOrderDetails.get(1));

                            tempCondition.add(0, cond);

                            continue;
                        }

                        targetQuantity += od.getQuantity();
                        ods.remove(od);
                        od.scannable = false;
                        ods.add(od);

                        offer.conditionList.add(0, od);
                    }
                    //search the gift product
                    List<Long> giftProductsIds = new ArrayList<>();
                    giftProductsIds = Arrays.asList(offer.getActionResourceList());

                    int giftQuantity = offer.getActionQuantity();
                    int targetResourceCount = 0;

                    String resourceType = offer.getAction().getString(Action.RESOURCE_TYPE.getValue());

                    for (OrderDetails od : ods) {
                        if(od.giftProduct) continue;
                        if(!od.scannable) continue;
                        if (resourceType.equalsIgnoreCase(ResourceType.CATEGORY.getValue())) {
                            if (giftProductsIds.contains(od.getProduct().getCategoryId())&&!(offer.conditionList.contains(od))) {
                                targetResourceCount += od.getQuantity();
                            }
                        }
                    }

                    if (targetResourceCount >= giftQuantity) {

                        //search the first X product and implement the offer
                        for (OrderDetails orderDetails : ods) {
                            if (giftQuantity > 0) {
                                if(orderDetails.giftProduct) continue;
                                if(!orderDetails.scannable) continue;

                                if (resourceType.equalsIgnoreCase(ResourceType.CATEGORY.getValue())) {

                                    if (giftProductsIds.contains(orderDetails.getProduct().getCategoryId())) {
                                        changes = true;
                                        if (giftQuantity - orderDetails.getQuantity() > 0) {
                                            giftQuantity -= orderDetails.getQuantity();
                                            orderDetails.setDiscount(100);
                                            orderDetails.giftProduct = true;
                                            orderDetails.scannable = false;

                                        } else if(giftQuantity - orderDetails.getQuantity() == 0){
                                            giftQuantity -= orderDetails.getQuantity();
                                            orderDetails.setDiscount(100);
                                            orderDetails.giftProduct = true;
                                            orderDetails.scannable = false;

                                            break;
                                        } else {
                                            //split this order details
                                            OrderDetails od = ods.remove(ods.indexOf(orderDetails));

                                            List<OrderDetails> orderDetails1 = splitOrderDetails(od, giftQuantity);
                                            orderDetails1.get(0).setDiscount(100);
                                            orderDetails1.get(0).giftProduct = true;
                                            orderDetails1.get(0).scannable = false;

                                            ods.addAll(orderDetails1);

                                            break;
                                        }
                                    }
                                }
                            } else break;
                        }
                    } else {
                        for (OrderDetails od : offer.conditionList) {
                            od.scannable = true;
                        }}
                        //not reach the rule condition

                }}
            else  if(offer.getActionName().equals(Action.PRICE_FOR_PRODUCT.getValue())){
                    double value = offer.getAction().getDouble(Action.VALUE.getValue());
                //COLLECT ALL PRODUCT ON IMPLEMENT OFFER
                for (OrderDetails orderDetails : ods) {

                    if(orderDetails.giftProduct) {
                        continue;
                    }

                    if(!orderDetails.scannable) {
                        continue;
                    }


                    Log.i("orderDetails", orderDetails.toString());
                    if (offer.getResourceType() == ResourceType.CATEGORY) {
                        if (categoryIds.contains(orderDetails.getOfferCategory())) {

                            productCount += orderDetails.getQuantity();
                            offer.conditionList.add(orderDetails);
                            Log.i("orderDetailsCount", productCount+"");

                        }
                    }
                }

                if (productCount >= quantity) {
                    //save the highest quantity price

                    //sort offer condition list

                    List<OrderDetails> tempCondition = offer.conditionList;

                    Collections.sort(tempCondition, new Comparator<OrderDetails>() {
                        @Override
                        public int compare(OrderDetails t1, OrderDetails t2) {
                            return Double.compare(t1.getUnitPrice(), t2.getUnitPrice());
                        }
                    });

                    Collections.reverse(tempCondition);
                    Log.d("tempCondition",tempCondition.toString());
                    //reset offer condition list
                    offer.conditionList = new ArrayList<>();
                    // select first highest quantity
                    int targetQuantity = 0;
                    while (targetQuantity < quantity) {
                        Log.d("CountTarget", targetQuantity+"");

                        OrderDetails od = tempCondition.remove(0);

                        if (targetQuantity + od.getQuantity() > quantity) {
                            //must split this order details
                            List<OrderDetails> spitedOrderDetails = splitOrderDetailsToPriceForProductOffer(od, quantity - targetQuantity,value);
                            Log.d("CountTarget1", spitedOrderDetails.toString()+"");

                            //reset on the main orders list
                            ods.remove(od);


                            OrderDetails cond = spitedOrderDetails.get(0);

                            cond.scannable = false;
                            ods.add(cond);
                            // spitedOrderDetails.get(1).setUnitPrice(0);
                            ods.add(spitedOrderDetails.get(1));

                            /*rejoin the two item to the first of the tem list
                            tempCondition.add(0, spitedOrderDetails.get(1));

                            tempCondition.add(0, cond);*/
                            Log.d("odss",ods.toString());

                            continue;
                        }

                        targetQuantity += od.getQuantity();
                        offer.conditionList.add(0, od);
                    }

                }}
            }
      //  }
        return changes;
    }

    /*
        first item on th returned list is equal to slice size
     */
    private static List<OrderDetails> splitOrderDetails(OrderDetails orderDetails,int sliceSize) {
        if (orderDetails.getQuantity() > sliceSize) {

            OrderDetails orderDetails1 = new OrderDetails(orderDetails);
            orderDetails1.setCount(sliceSize);

            OrderDetails orderDetails2 =new OrderDetails(orderDetails);
            orderDetails2.setDiscount(0);
            orderDetails2.giftProduct = false;
            orderDetails2.scannable = true;
            orderDetails2.setCount(orderDetails.getQuantity() - sliceSize);

            List<OrderDetails> odList = new ArrayList<>();
            odList.add(orderDetails1);
            odList.add(orderDetails2);

            return odList;
        }
        return null;
    }
    private static List<OrderDetails> splitOrderDetailsToPriceForProductOffer(OrderDetails orderDetails,int sliceSize,double offerPrice) {
        if (orderDetails.getQuantity() > sliceSize) {

            OrderDetails orderDetails1 = new OrderDetails(orderDetails);
            orderDetails1.setCount(sliceSize);
            orderDetails1.setPaidAmount(0);

            OrderDetails orderDetails2 =new OrderDetails(orderDetails);
            orderDetails2.setDiscount(0);
            orderDetails2.setCount(orderDetails.getQuantity() - sliceSize);
            orderDetails2.setPaidAmount(offerPrice*sliceSize);

            List<OrderDetails> odList = new ArrayList<>();
            odList.add(orderDetails1);
            odList.add(orderDetails2);

            return odList;
        }
        return null;
    }

    public static Map<Long, Offer> catOfferMap = new HashMap<Long, Offer>();

    public static void addOfferCat(Long catID, Offer offer,OrderDetails orderDetails) throws IOException, JSONException {
        if (catOfferMap.containsKey(catID)) {
            if (catOfferMap.get(catID).getOfferId() == offer.getOfferId()) {
                //offer category found add to conditionList
                catOfferMap.get(catID).addToConditionsList(orderDetails);
            }
        }
        // offer or category id not found add to map
        catOfferMap.put(catID, offer);
        recheckOffers();
    }

    public static void recheckOffers(){
        for (Map.Entry<Long, Offer> entry : catOfferMap.entrySet()) {
            if (entry.getValue().getRuleQuantity() == entry.getValue().getConditionQuantity()) {
                //search if have result item on other category
                if(entry.getValue().resourceList!=null) {
                    long catID = entry.getValue().resourceList.get(0);
                    if (entry.getKey() != catID) {
                        //this offer not the same category

                        if (catOfferMap.containsKey(catID)) {
                            //have this category

                            int pickItems = catOfferMap.get(catID).getActionQuantity();
                            if (catOfferMap.get(catID).getConditionQuantity() >= pickItems) {

                                //sort the Condition list by lowest price
                                Collections.sort(catOfferMap.get(catID).conditionList, new Comparator<OrderDetails>() {
                                    @Override
                                    public int compare(OrderDetails o1, OrderDetails o2) {
                                        return Double.compare(o1.getUnitPrice(), o2.getUnitPrice());
                                    }
                                });
                                Collections.reverse(catOfferMap.get(catID).conditionList);

                                while (pickItems > 0) {
                                    //pop first item and adding to result list on the offer object
                                    OrderDetails temp = catOfferMap.get(catID).conditionList.remove(0);
                                    entry.getValue().addToResultList(temp);
                                    //todo split this order details on quantity large this the pickItems
                                    pickItems -= temp.getQuantity();
                                }

                                //offer ready to apply
                                //call apply method
                            }

                        }

                    } else {
                        //the action and rule the same category
                    }


                }
            }
        }
    }


    public static void removeOfferCat(Long catID,Offer offer,OrderDetails orderDetails) {
        if (catOfferMap.containsKey(catID)) {
            if (catOfferMap.get(catID).getOfferId() == offer.getOfferId()) {
                //offer category found add to conditionList
                catOfferMap.get(catID).removeFromConditionsList(orderDetails);
            }
        }
    }

    public static boolean checkOnResultList(OrderDetails orderDetails) throws JSONException, IOException {
        for (Map.Entry<Long, Offer> entry : catOfferMap.entrySet()) {
            if (entry.getValue().resourceList.contains(orderDetails.getProduct().getCategoryId())) {
                entry.getValue().addToResultList(orderDetails);
                return true;
            }
        }
        return false;
    }















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

    public static OrderDetails execute(Offer offer, OrderDetails orderDetails, Context context, List<OrderDetails>orderDetailsList) throws JSONException {
        List<OrderDetails>newOrderDetails=new ArrayList<>();
        List<Integer>newOrderDetailsPosition=new ArrayList<>();

        int qty =orderDetails.getQuantity();
        for(int i=0;i<orderDetailsList.size();i++){
            if(String.valueOf(orderDetails.getProduct().getOfferId()).equals(String.valueOf(orderDetailsList.get(i).getProduct().getOfferId()))&&orderDetails.getProduct().getProductId()!=orderDetailsList.get(i).getProduct().getProductId()){
                newOrderDetails.add(orderDetailsList.get(i));
                qty+=orderDetailsList.get(i).getQuantity();
                newOrderDetailsPosition.add(i);
            }
        }


        JSONObject action = offer.getDataAsJsonObject().getJSONObject(Action.ACTION.getValue());
        JSONObject rules = offer.getDataAsJsonObject().getJSONObject(Rules.RULES.getValue());
        String actionName = action.getString(Action.NAME.getValue());
        int quantity = rules.getInt(Rules.quantity.getValue());
        orderDetails.offer = offer;
        if (actionName.equalsIgnoreCase(Action.GET_GIFT_PRODUCT.getValue())) {
                if (qty >= quantity+1) {
                    int productGroup =qty / (quantity + 1);

                    double totalPriceBeforeDiscount = orderDetails.getQuantity() * orderDetails.getUnitPrice();
                    double discount = (1 - (((orderDetails.getQuantity() - productGroup) * orderDetails.getUnitPrice()) / totalPriceBeforeDiscount)) * 100;

                    orderDetails.setDiscount(discount);
                    orderDetails.setOfferId(offer.getOfferId());
                } else {
                    orderDetails.setDiscount(0);
                    orderDetails.offer = null;
                }
            } else if (actionName.equalsIgnoreCase(Action.PRICE_FOR_PRODUCT.getValue())) {
                double value = action.getDouble(Action.VALUE.getValue());
                orderDetails.setDiscount(0);

            if (qty >= quantity) {
                    int productGroup = qty / quantity;
                    int productCountWithOutProductIntoOffer = qty - (productGroup * quantity);
                double discount =0;
                if(newOrderDetails.size()==0) {
                     discount = (1 -
                            (((productGroup * value) + (productCountWithOutProductIntoOffer * orderDetails.getUnitPrice()))
                                    / (orderDetails.getUnitPrice() * orderDetails.getQuantity()))) * 100;
                    orderDetails.setDiscount(discount);
                    orderDetails.setOfferId(offer.getOfferId());

                }
                else {
                    double totalSum=0;
                    for(int i=0;i<newOrderDetails.size();i++){
                        totalSum+=(newOrderDetails.get(i).getUnitPrice() * newOrderDetails.get(i).getQuantity());

                    }
                    totalSum+=orderDetails.getUnitPrice()*orderDetails.getQuantity();
                    discount = (1 -
                            (((productGroup * value) + (productCountWithOutProductIntoOffer * orderDetails.getUnitPrice()))
                                    / totalSum)) * 100;
                    orderDetails.setDiscount(discount);
                    for(int i=0 ;i<newOrderDetailsPosition.size();i++){
                        SESSION._ORDER_DETAILES.get(newOrderDetailsPosition.get(i)).setDiscount(discount);
                        SESSION._ORDER_DETAILES.get(newOrderDetailsPosition.get(i)).setOfferId(offer.getOfferId());
                    }


                }

                } else {
                    orderDetails.setDiscount(0);
                    orderDetails.offer = null;
                }
            } else if (actionName.equalsIgnoreCase(Action.GET_DISCOUNT.getValue())) {
                String value = action.getString(Action.VALUE.getValue());
                if (value.contains("%")) {
                    //percent value
                    //remove the percent char
                    value = value.replace("%", "");

                    double val = 0;
                    //convert the string value to double
                    val = Double.parseDouble(value);
                    if (orderDetails.getQuantity() >= quantity) {
                        int productCollection = orderDetails.getQuantity() / quantity;
                        int productOutTheCollection = orderDetails.getQuantity() - (productCollection * quantity);
                        double discount = (1 - (((productCollection * orderDetails.getUnitPrice() * (1 - (val / 100))) +
                                (productOutTheCollection * orderDetails.getUnitPrice())) /
                                (orderDetails.getQuantity() * orderDetails.getUnitPrice()))) * 100;
                        orderDetails.setDiscount(discount);
                        orderDetails.setOfferId(offer.getOfferId());
                    }

                } else {
                    double val = 0;
                    //convert string value to double
                    val = Double.parseDouble(value);
                    if (orderDetails.getQuantity() >= quantity) {
                        int productCollection = orderDetails.getQuantity() / quantity;
                        int productOutTheCollection = orderDetails.getQuantity() - (productCollection * quantity);
                        double discount = (1 - (((productCollection * orderDetails.getUnitPrice() - (val * productCollection)) +
                                (productOutTheCollection * orderDetails.getUnitPrice())) /
                                (orderDetails.getQuantity() * orderDetails.getUnitPrice()))) * 100;
                        orderDetails.setDiscount(discount);
                        orderDetails.setOfferId(offer.getOfferId());
                    }

                }

            }

        return orderDetails;
    }
}
