package com.fiuba.gaff.comohoy.model;

import java.util.ArrayList;
import java.util.List;

public class CommerceMenuItem {
    private String mCategory;
    private List<Plate> mPlates;

    public CommerceMenuItem() {
        mPlates = new ArrayList<>();
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        this.mCategory = category;
    }

    public List<Plate> getPlates() {
        return mPlates;
    }

    public void setPlates(List<Plate> plates) {
        this.mPlates = plates;
    }

    public void addPlate(Plate plate) {
        mPlates.add(plate);
    }
}
