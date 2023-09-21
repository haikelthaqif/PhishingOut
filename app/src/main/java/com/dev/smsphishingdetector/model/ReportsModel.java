package com.dev.smsphishingdetector.model;

import java.io.Serializable;

public class ReportsModel implements Serializable {
    String id,organisationName,phoneNumber,Message,imageUri,userId;
    Long time;
    int count=0;

    public ReportsModel(String id, String organisationName, String phoneNumber, String message, String imageUri, String userId, Long time) {
        this.id = id;
        this.organisationName = organisationName;
        this.phoneNumber = phoneNumber;
        Message = message;
        this.imageUri = imageUri;
        this.userId = userId;
        this.time = time;
    }
    public ReportsModel(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
