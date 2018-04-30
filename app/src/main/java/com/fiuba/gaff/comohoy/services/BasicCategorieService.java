package com.fiuba.gaff.comohoy.services;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Categorie;

import java.util.ArrayList;
import java.util.List;

public class BasicCategorieService implements CategorieService {

    private List<Categorie> mCategories;
    private Context mContext;

    public BasicCategorieService(Context context){ mContext = context;}

    @Override
    public void updateFoodData() {
        mCategories = new ArrayList<>();
        Categorie f1 = new Categorie("Arepas");
        Categorie f2 = new Categorie("Cafetería");
        Categorie f3 = new Categorie("Calzones");
        Categorie f4 = new Categorie("Carnes");
        Categorie f5 = new Categorie("Celíacos");

        f1.setPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.prueba1));
        f2.setPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.prueba1));
        f3.setPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.prueba1));
        f4.setPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.prueba1));
        f5.setPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.prueba1));

    }

    @Override
    public List<Categorie> getCategories() {
        if (mCategories == null) {
            updateFoodData();
        }
        return mCategories;
    }

    @Override
    public Categorie getCategorie(int index) {
        return mCategories.get(index);
    }
}
