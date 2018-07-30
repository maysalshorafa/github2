package com.pos.leaders.leaderspossystem.Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.Offers.ResourceType;

import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * Created by KARAM on 30/07/2018.
 */

public class Offer {
	//region Attribute
	private long offerId;
	private String name;
	private boolean active;
	private long resourceId;
	private ResourceType resourceType;
	private Timestamp start;
	private Timestamp end;
	private String data;

	private long byEmployee;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	//endregion

	//region Constructors

	public Offer() {
	}


	public Offer(long offerId, String name, boolean active, long resourceId, ResourceType resourceType, Timestamp start, Timestamp end, String data, long byEmployee, Timestamp createdAt, Timestamp updatedAt) {
		this.offerId = offerId;
		this.name = name;
		this.active = active;
		this.resourceId = resourceId;
		this.resourceType = resourceType;
		this.start = start;
		this.end = end;
		this.data = data;
		this.byEmployee = byEmployee;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	//endregion

	//region Method

	public JsonNode getDataAsJson() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readTree(this.data);
		} catch (IOException e) {
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

	public boolean isActive() {
		return active;
	}

	public long getResourceId() {
		return resourceId;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public Timestamp getStart() {
		return start;
	}

	public Timestamp getEnd() {
		return end;
	}

	public String getData() {
		return data;
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

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public void setStart(Timestamp start) {
		this.start = start;
	}

	public void setEnd(Timestamp end) {
		this.end = end;
	}

	public void setData(String data) {
		this.data = data;
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

}