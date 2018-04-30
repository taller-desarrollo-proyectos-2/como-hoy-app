package com.fiuba.gaff.comohoy.model;

import android.graphics.Bitmap;

public class Category {

    private Long mId;
    private String mName;
    private Bitmap mPicture;

    public Category(Long id, String name) {
        mId = id;
        mName = name;
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

    public Bitmap getPicture () {return mPicture;}

    public void setPicture (Bitmap picture) {this.mPicture = picture;}
}
