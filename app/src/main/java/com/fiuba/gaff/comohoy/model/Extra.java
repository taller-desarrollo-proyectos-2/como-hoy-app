package com.fiuba.gaff.comohoy.model;

/***
 *  Can be added to a plate, e.g., bacon
 */
public class Extra {

    private Long mId;
    private String mName;
    private double mPrice;
    private boolean mOnDiscount = false;
    private int mDiscountAmount = 0;

    public Extra(Long id) {
        mId = id;
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

    public double getPrice() {
        double price = mPrice;
        if (mOnDiscount) {
            price = (1.0 - (mDiscountAmount / 100.0)) * price;
        }
        return price;
    }

    public double getFullPrice() {
        return mPrice;
    }

    public boolean isOnDiscount() {
        return mOnDiscount;
    }

    public void setOnDiscount(boolean onDiscount) {
        mOnDiscount = onDiscount;
    }

    public int getDiscountAmount() {
        return mDiscountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        mDiscountAmount = discountAmount;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }
}
