package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.Offers.Action;
import com.pos.leaders.leaderspossystem.Offers.ResourceType;
import com.pos.leaders.leaderspossystem.Offers.Rules;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by KARAM on 30/07/2018.
 */

public class Offer {
	//region Attribute
	private long offerId;
	private String name;
	private String status;
	private long resourceId;
	private ResourceType resourceType;
	private Timestamp startDate;
	private Timestamp endDate;
	private String offerData;
	private long byEmployee;
	private Timestamp createdAt;
	private Timestamp updatedAt;


	@JsonIgnore
	public List<Long> resourceList = null;


	@JsonIgnore
	private int conditionItemQuantity = 0;
	@JsonIgnore
	private int resultItemQuantity = 0;
	@JsonIgnore
	public List<OrderDetails> conditionList = new ArrayList<>();

	public void addToConditionsList(OrderDetails orderDetails) {
		for (OrderDetails orderDetails1 : conditionList) {
			if (orderDetails1.getObjectID() == orderDetails.getObjectID()) {
				conditionItemQuantity -= orderDetails1.getQuantity();
				conditionList.remove(orderDetails1);
				break;
			}
		}
		conditionList.add(orderDetails);
		conditionItemQuantity += orderDetails.getQuantity();
	}

	public int getConditionQuantity() {
		return this.conditionItemQuantity;
	}

	public void removeFromConditionsList(OrderDetails orderDetails) {
		conditionList.remove(orderDetails);
		conditionItemQuantity -= orderDetails.getQuantity();
	}

	@JsonIgnore
	public List<OrderDetails> suggestedList = new ArrayList<>();


	@JsonIgnore
	public List<OrderDetails> resultList = new ArrayList<>();


	public void addToResultList(OrderDetails orderDetails) {
		for (OrderDetails orderDetails1 : conditionList) {
			if (orderDetails1.getObjectID() == orderDetails.getObjectID()) {
				resultItemQuantity -= orderDetails1.getQuantity();
				resultList.remove(orderDetails1);
				break;
			}
		}
		resultList.add(orderDetails);
		resultItemQuantity += orderDetails.getQuantity();
	}

	public int getResultQuantity() {
		return this.resultItemQuantity;
	}


	@JsonIgnore
	public boolean isImplemented = false;


	//endregion


	//region Constructors

	public Offer() {
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}



	public Offer(long offerId, String name, String status, long resourceId, ResourceType resourceType, Timestamp startDate, Timestamp endDate, String offerData, long byEmployee, Timestamp createdAt, Timestamp updatedAt) {
		this.offerId = offerId;
		this.name = name;
		this.status = status;
		this.resourceId = resourceId;
		this.resourceType = resourceType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.offerData = offerData;
		this.byEmployee = byEmployee;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;

		if (getActionResourceList() != null) {
			resourceList = Arrays.asList(getActionResourceList());
		}
	}
	//endregion

	//region Method
	@JsonIgnore
	public JsonNode getDataAsJson() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readTree(this.offerData);
		} catch (IOException e) {
			return null;
		}
	}

	@JsonIgnore
	public JSONObject getDataAsJsonObject() {
		try {
			return new JSONObject(this.offerData);
		} catch (JSONException e) {
			return null;
		}
	}

	@JsonIgnore
	public JSONObject getAction() {
		try {
			return getDataAsJsonObject().getJSONObject(Action.ACTION.getValue());
		} catch (JSONException e) {
			return null;
		}
	}


	@JsonIgnore
	public int getActionQuantity() {
		try {
			return getAction().getInt(Action.QUANTITY.getValue());
		} catch (JSONException e) {
			return -1;
		}
	}

	@JsonIgnore
	public boolean isActionSameResource() {
		try {
			return getAction().getBoolean(Action.SAME_RESOURCE.getValue());
		} catch (JSONException e) {
			return true;
		}
	}

	@JsonIgnore
	public Long[] getActionResourceList() {
		if (getAction().has(Action.RESOURCES_LIST.getValue())) {
			try {
				return new ObjectMapper().readValue(getAction().getString(Action.RESOURCES_LIST.getValue()), Long[].class);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@JsonIgnore
	public String getActionName() {
		try {
			return getAction().getString(Action.NAME.getValue());
		} catch (JSONException e) {
			return "";
		}
	}

	@JsonIgnore
	public JSONObject getRules() {
		try {
			return getDataAsJsonObject().getJSONObject(Rules.RULES.getValue());
		} catch (JSONException e) {
			return null;
		}
	}

	@JsonIgnore
	public int getRuleQuantity() {
		try {
			return getRules().getInt(Rules.quantity.getValue());
		} catch (JSONException e) {
			return -1;
		}
	}

	//endregion

	//region Getters

	public String getName() {
		return name;
	}

	public long getOfferId() {
		return offerId;
	}

	public String getStatus() {
		return status;
	}

	public long getResourceId() {
		return resourceId;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public String getOfferData() {
		return offerData;
	}

	public long getByEmployee() {
		return byEmployee;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	//endregion

	//region Setters


	public void setName(String name) {
		this.name = name;
	}

	public void setOfferId(long offerId) {
		this.offerId = offerId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public void setOfferData(String offerData) {
		this.offerData = offerData;
	}

	public void setByEmployee(long byEmployee) {
		this.byEmployee = byEmployee;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	//endregion

	@Override
	public String toString() {
		return "Offer{" +
				"offerId=" + offerId +
				", name='" + name + '\'' +
				", status=" + status +
				", resourceId=" + resourceId +
				", resourceType=" + resourceType +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", offerData='" + offerData + '\'' +
				", byEmployee=" + byEmployee +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				'}';
	}

}