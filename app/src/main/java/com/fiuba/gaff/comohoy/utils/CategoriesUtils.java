package com.fiuba.gaff.comohoy.utils;

import android.content.Context;

import com.fiuba.gaff.comohoy.R;

import java.util.HashMap;
import java.util.Map;

public class CategoriesUtils {

    private static final Map<String, Integer> mCategoriesDrawablesIds;
    static {
        mCategoriesDrawablesIds = new HashMap<>();
        mCategoriesDrawablesIds.put("Milanesas", R.drawable.arepas);
        mCategoriesDrawablesIds.put("Pizzas", R.drawable.cafeteria);
        mCategoriesDrawablesIds.put("Empanadas", R.drawable.calzon);
        mCategoriesDrawablesIds.put("Carnes", R.drawable.carnes);
        mCategoriesDrawablesIds.put("Celiacos", R.drawable.celiacos);
    }

    public static int getCategoryDrawableIdByName(String categoryName, Context context) {
        if (mCategoriesDrawablesIds.containsKey(categoryName)) {
            int categoryDrawableId = mCategoriesDrawablesIds.get(categoryName);
            return categoryDrawableId;
        }
        // Si no encuentra una imagen que matchea devuelve la imagen de error
        return R.drawable.no_image;
    }
}
