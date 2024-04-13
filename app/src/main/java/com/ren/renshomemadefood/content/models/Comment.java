package com.ren.renshomemadefood.content.models;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String comment;
    private String useId;
    private String userImg;
    private String userName;
    private Object timestamp;

    public Comment() {

    }

    public Comment(String comment, String useId, String userImg, String userName) {
        this.comment = comment;
        this.useId = useId;
        this.userImg = userImg;
        this.userName = userName;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Comment(String comment, String useId, String userImg, String userName, Object timestamp) {
        this.comment = comment;
        this.useId = useId;
        this.userImg = userImg;
        this.userName = userName;
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
