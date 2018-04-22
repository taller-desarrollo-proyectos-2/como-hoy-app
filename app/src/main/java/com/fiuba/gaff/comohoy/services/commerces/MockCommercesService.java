package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.content.Context;

import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Plate;

import java.util.ArrayList;
import java.util.List;

public class MockCommercesService implements CommercesService {

    private Context mContext;
    private List<Commerce> mCommerces;

    public MockCommercesService(Context context){
        mContext = context;
    }

    @Override
    public void updateCommercesData(Activity activity, UpdateCommercesCallback callback) {
        mCommerces = new ArrayList<>();
        Commerce c1 = new Commerce(0, "Banchero");
        c1.setDescription("Pizzas y Empanadas");
        c1.setDiscounts("20% off en empanadas de ensalada");
        c1.setRating("3.4");
        c1.setShippingCost("$50");
        c1.setShippingTime("30-45 min");
        c1.setPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.luigi));

        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Entradas"));
        categories.add(new Category(2L, "Plato Principal"));
        c1.setCategories(categories);

        List<Plate> plates = new ArrayList<>();
        Plate p1 = new Plate(0L, "Papas", "Las mejores papas", 50);
        List<Category> p1Cat = new ArrayList<>();
        p1Cat.add(new Category(0L, "Entradas"));
        p1.setCategories(p1Cat);

        Plate p2 = new Plate(0L, "Papas con Panceta", "Entrada y Cena", 500);
        List<Category> p2Cat = new ArrayList<>();
        p2Cat.add(new Category(0L, "Entradas"));
        p2Cat.add(new Category(1L, "Plato Principal"));
        p2.setCategories(p2Cat);

        plates.add(p1);;
        plates.add(p2);

        c1.setmPlates(plates);

        Commerce c2 = new Commerce(1, "La Esquina de Tito");
        c2.setDescription("Chori y Bondioletas");
        c2.setRating("4.5");
        c2.setShippingCost("$15");
        c2.setShippingTime("15-30 min");
        c2.setPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pizzeria2));

        mCommerces.add(c1);
        mCommerces.add(c2);

        callback.onCommercesUpdated();
    }

    @Override
    public List<Commerce> getCommerces() {
        return mCommerces;
    }

    @Override
    public Commerce getCommerce(int id) {
        return mCommerces.get(id);
    }
}
