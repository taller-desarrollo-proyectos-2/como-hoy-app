package com.fiuba.gaff.comohoy.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;
import android.widget.LinearLayout;

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
        Category f6 = new Category(Long.valueOf(6),"Asd");

        f1.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.arepas));
        f2.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.cafeteria));
        f3.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.calzon));
        f4.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.carnes));
        f5.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.celiacos));
        f6.setPicture(BitmapFactory.decodeResource(context.getResources(), R.drawable.prueba1));

        mCategories.add(f1);
        mCategories.add(f2);
        mCategories.add(f3);
        mCategories.add(f4);
        mCategories.add(f5);
        mCategories.add(f6);
    }

    public List<Category> getListCategories() {return mCategories;}

    public List<List<Category>> getListOfList(){
        List listaDeListas = new ArrayList<>();
        int i = 0;
        List listaDeADos = new ArrayList<>();
        for (Category category : mCategories) {
            if (i == 0){
                listaDeADos.add(category);
                i = i + 1;
            }
            else{
                listaDeADos.add(category);
                listaDeListas.add(listaDeADos);
                listaDeADos = new ArrayList<>();
                i = 0;
            }
        }

        return listaDeListas;
    }
}
