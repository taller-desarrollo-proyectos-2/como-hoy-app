package com.fiuba.gaff.comohoy.model;

import java.util.ArrayList;
import java.util.List;

public class CommerceMenu {
    private List<CommerceMenuItem> mMenuItems;

    public CommerceMenu() {
        mMenuItems = new ArrayList<>();
    }

    public List<CommerceMenuItem> getMenuItems() {
        return mMenuItems;
    }

    public void setMenuItems(List<CommerceMenuItem> menuItems) {
        this.mMenuItems = menuItems;
    }

    public void addMenuItem(CommerceMenuItem item) {
        mMenuItems.add(item);
    }
}
