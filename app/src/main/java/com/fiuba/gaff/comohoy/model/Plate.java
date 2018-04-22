package com.fiuba.gaff.comohoy.model;

import android.graphics.Bitmap;
import android.support.design.internal.BottomNavigationItemView;

import java.util.List;

public class Plate {
    private Long mId;
    private String mName;
    private String mDescription;
    private double mPrice;
    private List<Category> mCategories;
    private List<Extra> mExtras;
    private Bitmap mPicture;

    public Plate(Long id) {
        mId = id;
    }

    public Plate (Long id, String name, String description, double price) {
        mId = id;
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

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public void setCategories(List<Category> categories) {
        this.mCategories = categories;
    }

    public Bitmap getPicture() {
        return mPicture;
    }

    public void setPicture(Bitmap picture) {
        this.mPicture = picture;
    }

    public List<Extra> getExtras() {
        return mExtras;
    }

    public void setExtras(List<Extra> extras) {
        this.mExtras = extras;
    }
}
