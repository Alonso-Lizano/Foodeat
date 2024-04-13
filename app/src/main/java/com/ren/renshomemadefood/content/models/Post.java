package com.ren.renshomemadefood.content.models;

import com.google.firebase.database.ServerValue;

public class Post {

    private String id;
    private String title;
    private String description;
    private String image;
    private String userId;
    private String userImg;
    private Object timeStamp;
    private int reportCount;

    public Post(String title, String description, String image, String userId, String userImg) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.userId = userId;
        this.userImg = userImg;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Post() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }
}
