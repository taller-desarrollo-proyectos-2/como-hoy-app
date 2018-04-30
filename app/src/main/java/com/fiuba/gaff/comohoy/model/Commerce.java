package com.fiuba.gaff.comohoy.model;

import android.graphics.Bitmap;

import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.location.LocationService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Commerce {

    private int mId;
    private String mBusinessName = "";
    private String mName = "";
    private String mDescription = "";
    private String mOrdersAmount = "";
    private String mShippingTime = "";
    private String mShippingCost = "";
    private String mDiscounts = "";
    private Location mLocation;
    private HashMap<Day, OpeningTime> mOpeningTimes;
    private double mRating;

    private Bitmap mPicture;

    private List<Category> mCategories;
    private HashMap<Long, Plate> mPlates;

    public Commerce(int id) {
        mId = id;
        mCategories = new ArrayList<>();
        mPlates = new HashMap<>();
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
        String name = (mName.isEmpty() || mName.equals("null")) ? mBusinessName : mName;
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

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
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

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public void setCategories(List<Category> categories) {
        this.mCategories = categories;
    }

    public List<Plate> getPlates() {
        return new ArrayList<>(mPlates.values());
    }

    public Plate getPlate(Long id) {
        return mPlates.get(id);
    }

    public void setPlates(HashMap<Long, Plate> plates) {
        mPlates = plates;
    }

    public void setPlates(List<Plate> plates) {
        for (Plate plate : plates) {
            mPlates.put(plate.getId(), plate);
        }
    }

    public HashMap<Day, OpeningTime> getOpeningTimes() {
        return mOpeningTimes;
    }

    public void setOpeningTimes(HashMap<Day, OpeningTime> openingTimes) {
        mOpeningTimes = openingTimes;
    }
}
