package com.fiuba.gaff.comohoy.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Plate {
    private Long mId;
    private String mName;
    private String mDescription;
    private double mPrice;
    private List<Category> mCategories;
    private Map<Long, Extra> mExtrasMap;
    private boolean mSuitableForCeliac;
    private Bitmap mPicture;

    public Plate(Long id) {
        mId = id;
        mExtrasMap = new HashMap<>();
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
        return new ArrayList<>(mExtrasMap.values());
    }

    public boolean containsExtra(Long extraId) {
        return mExtrasMap.containsKey(extraId);
    }

    public Extra getExtraWithId (Long extraId) {
        if (mExtrasMap.containsKey(extraId)) {
            return mExtrasMap.get(extraId);
        }
        return null;
    }


    public void setExtras(List<Extra> extras) {
        if (extras == null) return;
        for (Extra extra : extras) {
            mExtrasMap.put(extra.getId(), extra);
        }
    }

    public boolean isSuitableForCeliac() {
        return mSuitableForCeliac;
    }

    public void setSuitableForCeliac(boolean suitableForCeliac) {
        this.mSuitableForCeliac = suitableForCeliac;
    }
}
