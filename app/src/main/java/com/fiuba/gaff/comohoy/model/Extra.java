package com.fiuba.gaff.comohoy.model;

/***
 *  Can be added to a plate, e.g., bacon
 */
public class Extra {

    private Long mId;
    private String mName;
    private double mPrice;

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
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }
}
