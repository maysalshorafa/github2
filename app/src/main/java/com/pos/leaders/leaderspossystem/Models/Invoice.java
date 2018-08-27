package com.pos.leaders.leaderspossystem.Models;

import org.json.JSONObject;

/**
 * Created by Win8.1 on 8/27/2018.
 */

public class Invoice {
    private String type;
    private JSONObject documentsData;

    public Invoice() {
    }

    public Invoice(String type, JSONObject documentsData) {
        this.type = type;
        this.documentsData = documentsData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getDocumentsData() {
        return documentsData;
    }

    public void setDocumentsData(JSONObject documentsData) {
        this.documentsData = documentsData;
    }
}
