package com.ren.renshomemadefood.content.models;

import com.google.firebase.database.ServerValue;

public class FavoriteRecipe {

    private String id;
    private String title;
    private String image;
    private String readyInMinutes;
    private Object timeStamp;

    public FavoriteRecipe(String id, String title, String image, String readyInMinutes) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.readyInMinutes = readyInMinutes;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public FavoriteRecipe() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(String readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
