package com.fiuba.gaff.comohoy.model;

import java.util.ArrayList;
import java.util.List;

public class CommerceMenuItem {
    private String mCategoryName;
    private List<Plate> mPlates;

    public CommerceMenuItem() {
        mPlates = new ArrayList<>();
    }

    public CommerceMenuItem(String categoryName, List<Plate> plates) {
        mCategoryName = categoryName;
        mPlates = (plates != null) ? plates : new ArrayList<Plate>();
    }

    public String getCategory() {
        return mCategoryName;
    }

    public void setCategory(String category) {
        this.mCategoryName = category;
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
