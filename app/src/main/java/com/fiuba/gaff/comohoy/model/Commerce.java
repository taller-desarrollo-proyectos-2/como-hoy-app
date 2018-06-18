package com.fiuba.gaff.comohoy.model;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Commerce {

    private int mId;
    private String mBusinessName = "";
    private String mName = "";
    private String mDescription = "";
    private String mOrdersAmount = "";
    private String mShippingCost = "";
    private Location mLocation;
    private List<OpeningTime> mOpeningTimes;
    private boolean mIsFavourite = false;
    private int mLeadTime = 0;
    private double mRating;
    private double mAveragePrice;
    private Bitmap mPicture;
    private int mMaxDiscount = 0;

    private List<Opinion> mOpiniones;
    private List<Category> mCategories;
    private HashMap<Long, Plate> mPlates;

    public Commerce(int id) {
        mId = id;
        mCategories = new ArrayList<>();
        mOpeningTimes = new ArrayList<>();
        mPlates = new HashMap<>();
    }

    public void setIsFavourite(boolean like) { mIsFavourite = like; }
    public boolean isFavourite() { return mIsFavourite; }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getBusinessName() {
        return mBusinessName;
    }

    public void setFavourite(boolean favourite) {
        mIsFavourite = favourite;
    }

    public void setBusinessName(String businessName) {
        this.mBusinessName = businessName;
    }

    public String getShowableName() {
        String name = (mName.isEmpty() || mName.equals("null")) ? mBusinessName : mName;
        return name;
    }

    public double getAveragePrice() {
        return mAveragePrice;
    }

    public int getMaxDiscount() {
        return mMaxDiscount;
    }

    public void setMaxDiscount(int maxDiscount) {
        mMaxDiscount = maxDiscount;
    }

    public void setAveragePrice(double averagePrice) {
        mAveragePrice = averagePrice;
    }

    public int getLeadTime() {
        return mLeadTime;
    }

    public void setLeadTime(int leadTime) {
        mLeadTime = leadTime;
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

    public String getShippingCost() {
        return mShippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.mShippingCost = shippingCost;
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

    public void setOpiniones(List<Opinion> opiniones) {
        this.mOpiniones = opiniones;
    }

    public List<Plate> getPlates() {
        return new ArrayList<>(mPlates.values());
    }

    public List<Opinion> getOpiniones() { return mOpiniones; }

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

    public List<OpeningTime> getOpeningTimes() {
        return mOpeningTimes;
    }

    public void setOpeningTimes(List<OpeningTime> openingTimes) {
        mOpeningTimes = openingTimes;
    }
}
