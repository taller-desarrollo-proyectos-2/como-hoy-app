package com.fiuba.gaff.comohoy.model;

import android.graphics.Bitmap;
import android.support.design.internal.BottomNavigationItemView;

public class Plate {
    private Long mId;
    private String mName;
    private String mDescription;
    private int mPrice;
    private Bitmap mPicture;

    public Plate() {}

    public Plate (String name, String description, int price) {
        mName = name;
        mDescription = description;
        mPrice = price;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        this.mPrice = price;
    }

    public Bitmap getPicture() {
        return mPicture;
    }

    public void setPicture(Bitmap picture) {
        this.mPicture = picture;
    }
}
