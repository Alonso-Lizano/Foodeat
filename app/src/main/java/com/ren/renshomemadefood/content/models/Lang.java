package com.ren.renshomemadefood.content.models;

import java.io.Serializable;

public class Lang implements Serializable {

    private String name;
    private int image;

    public Lang() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
