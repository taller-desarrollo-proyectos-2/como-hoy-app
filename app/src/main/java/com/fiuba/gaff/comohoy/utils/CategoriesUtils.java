package com.fiuba.gaff.comohoy.utils;

import android.content.Context;

import com.fiuba.gaff.comohoy.R;

import java.util.HashMap;
import java.util.Map;

public class CategoriesUtils {

    private static final Map<String, Integer> mCategoriesDrawablesIds;
    static {
        mCategoriesDrawablesIds = new HashMap<>();
        mCategoriesDrawablesIds.put("Milanesas", R.drawable.milanesas);
        mCategoriesDrawablesIds.put("Pizzas", R.drawable.cafeteria);
        mCategoriesDrawablesIds.put("Empanadas", R.drawable.calzon);
        mCategoriesDrawablesIds.put("Carnes", R.drawable.carnes);
        mCategoriesDrawablesIds.put("Celiacos", R.drawable.celiacos);
        mCategoriesDrawablesIds.put("Arepas",R.drawable.arepas);
        mCategoriesDrawablesIds.put("Bebidas",R.drawable.food);
        mCategoriesDrawablesIds.put("Cafetería",R.drawable.food);
        mCategoriesDrawablesIds.put("Calzones",R.drawable.food);
        mCategoriesDrawablesIds.put("Celíacos",R.drawable.food);
        mCategoriesDrawablesIds.put("Comida Árabe",R.drawable.food);
        mCategoriesDrawablesIds.put("Comida Armenia",R.drawable.food);
        mCategoriesDrawablesIds.put("Comida China",R.drawable.food);
        mCategoriesDrawablesIds.put("Comida Hindú",R.drawable.food);
        mCategoriesDrawablesIds.put("Comida Internacional",R.drawable.food);
        mCategoriesDrawablesIds.put("Comida Japonesa",R.drawable.food);
        mCategoriesDrawablesIds.put("Comida Mexicana",R.drawable.food);
        mCategoriesDrawablesIds.put("Comida Peruana",R.drawable.food);
        mCategoriesDrawablesIds.put("Comida Vegana",R.drawable.food);
        mCategoriesDrawablesIds.put("Comida Vegetariana",R.drawable.food);
        mCategoriesDrawablesIds.put("Crepes",R.drawable.food);
        mCategoriesDrawablesIds.put("Cupcakes",R.drawable.food);
        mCategoriesDrawablesIds.put("Desayunos",R.drawable.food);
        mCategoriesDrawablesIds.put("Ensaladas",R.drawable.food);
        mCategoriesDrawablesIds.put("Hamburguesas",R.drawable.food);
        mCategoriesDrawablesIds.put("Helados",R.drawable.food);
        mCategoriesDrawablesIds.put("Licuados y Jugos",R.drawable.food);
        mCategoriesDrawablesIds.put("Lomitos",R.drawable.food);
        mCategoriesDrawablesIds.put("Menú del día",R.drawable.food);
        mCategoriesDrawablesIds.put("Minimercado",R.drawable.food);
        mCategoriesDrawablesIds.put("Papas Fritas",R.drawable.food);
        mCategoriesDrawablesIds.put("Parrilla",R.drawable.food);
        mCategoriesDrawablesIds.put("Pastas",R.drawable.food);
        mCategoriesDrawablesIds.put("Pescados y Mariscos",R.drawable.food);
        mCategoriesDrawablesIds.put("Picadas",R.drawable.food);
        mCategoriesDrawablesIds.put("Pollo",R.drawable.food);
        mCategoriesDrawablesIds.put("Postres",R.drawable.food);
        mCategoriesDrawablesIds.put("Sándwiches",R.drawable.food);
        mCategoriesDrawablesIds.put("Sopas",R.drawable.food);
        mCategoriesDrawablesIds.put("Sushi",R.drawable.food);
        mCategoriesDrawablesIds.put("Tartas",R.drawable.food);
        mCategoriesDrawablesIds.put("Tortillas",R.drawable.food);
        mCategoriesDrawablesIds.put("Viandas y Congelados",R.drawable.food);
        mCategoriesDrawablesIds.put("Waffles",R.drawable.food);
        mCategoriesDrawablesIds.put("Woks",R.drawable.food);
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
