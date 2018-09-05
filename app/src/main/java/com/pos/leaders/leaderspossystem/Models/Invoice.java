package com.pos.leaders.leaderspossystem.Models;

import com.pos.leaders.leaderspossystem.DocumentType;

import org.json.JSONObject;

/**
 * Created by Win8.1 on 8/27/2018.
 */

public class Invoice {
    private DocumentType type;
    private JSONObject documentsData;

    public Invoice() {
    }

    public Invoice(DocumentType type, JSONObject documentsData) {
        this.type = type;
        this.documentsData = documentsData;

    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }
    public JSONObject getDocumentsData() {
        return documentsData;
    }
    public void setDocumentsData(JSONObject documentsData) {
        this.documentsData = documentsData;
    }

    @Override
    public String toString() {
        return "{" +
                "\""+"type\":" +"\""+type +"\""+
                "," +"\""+"documentsData\":" +documentsData  +
                '}';
    }
}
