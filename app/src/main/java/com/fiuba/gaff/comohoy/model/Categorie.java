package com.fiuba.gaff.comohoy.model;

import android.graphics.Bitmap;

/**
 * Created by FernandoN on 30/04/2018.
 */

public class Categorie {
    private Bitmap mPicture;
    private String mName = "";


    public Categorie(String name) {
        mName = name;
    }

    public String getName () {return mName;}
    public Bitmap getPicture() {
        return mPicture;
    }
    public void setPicture(Bitmap picture) {
        this.mPicture = picture;
    }

}
