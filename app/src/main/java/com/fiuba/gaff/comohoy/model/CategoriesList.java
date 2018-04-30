package com.fiuba.gaff.comohoy.model;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.fiuba.gaff.comohoy.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesList {
    private List<Category> mCategories;

    public CategoriesList(Context context){
        mCategories = new ArrayList<>();
        Category f1 = new Category(Long.valueOf(1),"Arepas");
        Category f2 = new Category(Long.valueOf(2),"Cafetería");
        Category f3 = new Category(Long.valueOf(3),"Calzones");
        Category f4 = new Category(Long.valueOf(4),"Carnes");
        Category f5 = new Category(Long.valueOf(5),"Celíacos");

        f1.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.prueba1));
        f2.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.prueba1));
        f3.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.prueba1));
        f4.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.prueba1));
        f5.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.prueba1));

        mCategories.add(f1);
        mCategories.add(f2);
        mCategories.add(f3);
        mCategories.add(f4);
        mCategories.add(f5);
    }

    public List<Category> getListCategories() {return mCategories;}
}
