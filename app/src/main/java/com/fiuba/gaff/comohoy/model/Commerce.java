package com.fiuba.gaff.comohoy.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Commerce {

    private int mId;
    private String mBusinessName = "";
    private String mName = "";
    private String mDescription = "";
    private String mRating = "";
    private String mOrdersAmount = "";
    private String mShippingTime = "";
    private String mShippingCost = "";
    private String mDiscounts = "";
    private Location mLocation;

    private Bitmap mPicture;

    private List<Category> mCategories;
    private List<Plate> mPlates;

    public Commerce(int id) {
        mId = id;
        mCategories = new ArrayList<>();
        mPlates = new ArrayList<>();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getBusinessName() {
        return mBusinessName;
    }

    public void setBusinessName(String businessName) {
        this.mBusinessName = businessName;
    }

    public String getShowableName() {
        String name = getName().equals("") ? getBusinessName() : getName();
        return name;
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

    public Location getLocation() {
        return mLocation;
    }

    public void setmLocation(Location location) {
        this.mLocation = location;
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public void setCategories(List<Category> categories) {
        this.mCategories = categories;
    }

    public List<Plate> getPlates() {
        return mPlates;
    }

    public void setPlates(List<Plate> plates) {
        this.mPlates = plates;
    }
}
