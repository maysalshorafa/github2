package com.pos.leaders.leaderspossystem.Models;

import java.sql.Timestamp;

/**
 * Created by Win8.1 on 4/28/2020.
 */

public class LincessPos {
    private long id;
    private String companyId;
    private String Note;
    private String branchId;
    private Timestamp activationDate;
    private Timestamp dueDate;
    private String statusLincess;

   public LincessPos(){}


    public LincessPos(String companyId,String Note,String branchId,Timestamp activationDate,Timestamp dueDate){
     //   this.id=id;
        this.companyId=companyId;
        this.Note=Note;
        this.branchId=branchId;
        this.activationDate=activationDate;
        this.dueDate=dueDate;
    }
    public LincessPos(long id,String companyId,String Note,String branchId,Timestamp activationDate,Timestamp dueDate,String statusLincess){
        this.id=id;
        this.companyId=companyId;
        this.Note=Note;
        this.branchId=branchId;
        this.activationDate=activationDate;
        this.dueDate=dueDate;
        this.statusLincess=statusLincess;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Timestamp getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Timestamp activationDate) {
        this.activationDate = activationDate;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getStatusLincess() {
        return statusLincess;
    }

    public void setStatusLincess(String statusLincess) {
        this.statusLincess = statusLincess;
    }

    @Override
    public String toString() {
        return "LincessPos{" +
                "id=" + id +
                ", companyId='" + companyId + '\'' +
                ", Note='" + Note + '\'' +
                ", branchId='" + branchId + '\'' +
                ", activationDate=" + activationDate +
                ", dueDate=" + dueDate +
                ", statusLincess='" + statusLincess + '\'' +
                '}';
    }
}
