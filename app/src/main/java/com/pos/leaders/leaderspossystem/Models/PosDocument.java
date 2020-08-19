package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by Win8.1 on 8/17/2020.
 */

public class PosDocument {
        private long posDocumentId;
        private String type;
        private String boID;
        private String documentData;

        public PosDocument() {
        }

    public PosDocument(long posDocumentId, String type, String boID, String documentData) {
        this.posDocumentId = posDocumentId;
        this.type = type;
        this.boID = boID;
        this.documentData = documentData;
    }


    public long getPosDocumentId() {
        return posDocumentId;
    }

    public void setPosDocumentId(long posDocumentId) {
        this.posDocumentId = posDocumentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBoID() {
        return boID;
    }

    public void setBoID(String boID) {
        this.boID = boID;
    }

    public String getDocumentData() {
        return documentData;
    }

    public void setDocumentData(String documentData) {
        this.documentData = documentData;
    }

    @Override
    public String toString() {
        return "PosDocument{" +
                "posDocumentId=" + posDocumentId +
                ", type='" + type + '\'' +
                ", boID='" + boID + '\'' +
                ", documentData='" + documentData + '\'' +
                '}';
    }
}
