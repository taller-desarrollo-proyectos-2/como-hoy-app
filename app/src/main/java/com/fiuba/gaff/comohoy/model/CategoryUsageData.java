package com.fiuba.gaff.comohoy.model;

public class CategoryUsageData {
    private Category mCategory;
    private int mUsesAmount;

    public CategoryUsageData (Category category, int usesAmount) {
        mCategory = category;
        mUsesAmount = usesAmount;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category category) {
        mCategory = category;
    }

    public int getUsesAmount() {
        return mUsesAmount;
    }

    public void setUsesAmount(int usesAmount) {
        mUsesAmount = usesAmount;
    }
}
