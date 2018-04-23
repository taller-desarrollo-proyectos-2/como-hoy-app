package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.utils.RandomUtils;

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
        final int commercesAmount = 5;
        mCommerces = new ArrayList<>();
        for (int i = 0; i < commercesAmount; ++i) {
            mCommerces.add(createCommerce(i));
            createPlatesAndCategories(mCommerces.get(i));
        }

        callback.onCommercesUpdated();
    }

    private Commerce createCommerce(int id) {
        Commerce commerce = new Commerce(id);
        commerce.setName("Comercio " + id);
        commerce.setDescription("Comercio autogenerado " + id);
        boolean hasDiscount = (RandomUtils.getIntBetween(0, 1) == 1);
        int discount = getRandomDiscount();
        if (hasDiscount) {
            commerce.setDiscounts(String.format("%%d off en empanadas de ensalada", discount));
        }
        commerce.setRating(RandomUtils.getDoubleBetween(1.0, 5.0));
        commerce.setShippingCost(String.format("$%d", RandomUtils.getIntBetween(10, 250)));

        int minShippingTime = RandomUtils.getIntBetween(10, 45);
        int maxShippingTime = RandomUtils.getIntBetween(minShippingTime, minShippingTime * 2);
        commerce.setShippingTime(String.format("%d-%d min", minShippingTime, maxShippingTime));

        commerce.setPicture(BitmapFactory.decodeResource(mContext.getResources(), getRandomDrawableId()));

        Location loc = new Location(-34.617290, -58.367846);
        commerce.setmLocation(loc);

        return commerce;
    }

    //TODO refactor
    private void createPlatesAndCategories (Commerce commerce) {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Entradas"));
        categories.add(new Category(2L, "Plato Principal"));
        commerce.setCategories(categories);

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

        commerce.setPlates(plates);
    }

    private int getRandomDiscount() {
        int discounts[] = {5, 10, 15, 20, 25};
        int randomIndex = RandomUtils.getIntBetween(0, discounts.length - 1);
        return discounts[randomIndex];
    }

    private int getRandomDrawableId() {
        int ids[] = {R.drawable.luigi, R.drawable.pizzeria};
        int randomIndex = RandomUtils.getIntBetween(0, ids.length - 1);
        return  ids[randomIndex];
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
