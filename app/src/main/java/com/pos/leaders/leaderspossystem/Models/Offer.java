package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.Offers.ResourceType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by KARAM on 30/07/2018.
 */

public class Offer extends JSONObject {
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

	//endregion

	//region Constructors

	public Offer() {
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