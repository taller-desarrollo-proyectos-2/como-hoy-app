package com.fiuba.gaff.comohoy.model;

import android.graphics.Bitmap;

public class Commerce {

    private String mName = "";
    private String mDescription = "";
    private String mRating = "";
    private String mOrdersAmount = "";
    private String mShippingTime = "";
    private String mShippingCost = "";
    private String mDiscounts = "";
    private float mUbicationLong = 0;
    private float mUbicationLat = 0;

    private Bitmap mPicture;

    public Commerce(String name) {
        mName = name;
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

    public String getRating() {
        return mRating;
    }

    public void setRating(String rating) {
        this.mRating = rating;
    }

    public String getOrdersAmount() {
        return mOrdersAmount;
    }

    public void setOrdersAmount(String ordersAmount) {
        this.mOrdersAmount = ordersAmount;
    }

    public String getShippingTime() {
        return mShippingTime;
    }

    public void setShippingTime(String shippingTime) {
        this.mShippingTime = shippingTime;
    }

    public String getShippingCost() {
        return mShippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.mShippingCost = shippingCost;
    }

    public String getDiscounts() {
        return mDiscounts;
    }

    public void setDiscounts(String discounts) {
        this.mDiscounts = discounts;
    }

    public Bitmap getPicture() {
        return mPicture;
    }

    public void setPicture(Bitmap picture) {
        this.mPicture = picture;
    }

    public void setUbicationLong(float longi ) { mUbicationLong = longi;}

    public float getUbicationLong(){ return mUbicationLong; }

    public void setUbicationLat(float lat) { mUbicationLat = lat;}

    public float getUbicationLat() { return mUbicationLat; }

}
