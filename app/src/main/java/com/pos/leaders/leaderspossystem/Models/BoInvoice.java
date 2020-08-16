package com.pos.leaders.leaderspossystem.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.pos.leaders.leaderspossystem.DocumentType;

import org.json.JSONObject;

/**
 * Created by Win8.1 on 8/27/2018.
 */

public class BoInvoice implements Parcelable {
    private DocumentType type;
    private JSONObject documentsData;
    private String docNum;
    public BoInvoice() {
    }

    public BoInvoice(DocumentType type, JSONObject documentsData, String docNum) {
        this.type = type;
        this.documentsData = documentsData;
        this.docNum=docNum;

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

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    @Override
    public String toString() {
        return "{" +
                "\""+"type\":" +"\""+type +"\""+
                "," +"\""+"documentsData\":" +documentsData  +
                "," +"\""+"docNum\":" +"\""+docNum +"\""  +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
