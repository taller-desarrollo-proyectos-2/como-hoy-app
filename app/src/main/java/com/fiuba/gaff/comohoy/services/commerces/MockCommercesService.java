package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.content.Context;

import com.fiuba.gaff.comohoy.comparators.CommerceLocationComparator;
import com.fiuba.gaff.comohoy.filters.Filter;
import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.CategoryUsageData;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.model.Opinion;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MockCommercesService implements CommercesService {

    private Context mContext;
    private List<Commerce> mCommerces;

    public MockCommercesService(Context context){
        mContext = context;
    }

    @Override
    public void addFilter(Filter filter) {

    }

    @Override
    public void clearFilters() {

    }

    @Override
    public void addToFavourites(Activity activity, int commerceId, AddToFavouriteCallback callback) {

    }

    @Override
    public void removeFromFavourites(Activity activity, int commerceId, RemoveFromFavouritesCallback callback) {

    }

    @Override
    public void updateCommercesData(Activity activity, UpdateCommercesCallback callback) {
        final int commercesAmount = 3;
        mCommerces = new ArrayList<>();
        for (int i = 0; i < commercesAmount; ++i) {
            mCommerces.add(createCommerce(i));
            createPlatesAndCategories(mCommerces.get(i));
        }

        callback.onCommercesUpdated();
    }

    @Override
    public List<Commerce> getCommercesSortedBy(Context context, SortCriteria sortCriteria) {
        switch (sortCriteria) {
            case Closeness:
                Collections.sort(mCommerces, new CommerceLocationComparator(context));
                break;
            default:
                Collections.sort(mCommerces, new CommerceLocationComparator(context));
        }
        return mCommerces;
    }

    @Override
    public List<CategoryUsageData> getUsedCategoriesUsageData() {
        HashMap<String, CategoryUsageData> categoriesUsageMap = new HashMap<>();
        List<Commerce> commerces = getCommerces();
        for (Commerce commerce : commerces) {
            List<Category> categories = commerce.getCategories();

            for (Category category : categories) {
                String categoryName = category.getName();
                // Si existe suma una a la cantidad de uso, si no lo crea e inicializa en 1
                if (categoriesUsageMap.containsKey(categoryName)) {
                    CategoryUsageData categoryData = categoriesUsageMap.get(categoryName);
                    int uses = categoryData.getUsesAmount() + 1;
                    categoryData.setUsesAmount(uses);
                } else {
                    CategoryUsageData categoryData = new CategoryUsageData(category, 1);
                    categoriesUsageMap.put(category.getName(), categoryData);
                }
            }
        }
        return new ArrayList<>(categoriesUsageMap.values());
    }

    private Commerce createCommerce(int id) {
        Commerce commerce = new Commerce(id);
        commerce.setName("Comercio " + id);
        commerce.setDescription("Comercio autogenerado " + id);
        boolean hasDiscount = (RandomUtils.getIntBetween(0, 1) == 1);
        int discount = getRandomDiscount();
        if (hasDiscount) {
            commerce.setDiscounts(String.format("%%%d off en empanadas de ensalada", discount));
        }
        commerce.setRating(RandomUtils.getDoubleBetween(1.0, 5.0));
        commerce.setShippingCost(String.format("$%d", RandomUtils.getIntBetween(10, 250)));

        int minShippingTime = RandomUtils.getIntBetween(10, 45);
        int maxShippingTime = RandomUtils.getIntBetween(minShippingTime, minShippingTime * 2);

        commerce.setPicture(BitmapFactory.decodeResource(mContext.getResources(), getRandomDrawableId()));

        Location loc = new Location(-34.617290, -58.367846);
        commerce.setLocation(loc);

        return commerce;
    }

    //TODO refactor
    private void createPlatesAndCategories (Commerce commerce) {
        List<Category> categories = new ArrayList<>();

        Category entrada =  new Category(1L, "Arepas");
        Category platoPpal =  new Category(2L, "Empanadas");
        Category postre =  new Category(3L, "Postres");

        categories.add(entrada);
        categories.add(platoPpal);
        commerce.setCategories(categories);

        HashMap<Long, Plate> plates = new HashMap<>();
        Plate p1 = new Plate(0L);
        p1.setName("Papas");
        p1.setDescription("Las mejores papas");
        p1.setPrice(50);
        p1.setPicture(BitmapFactory.decodeResource(mContext.getResources(), getRandomDrawableId()));
        List<Category> p1Cat = new ArrayList<>();
        p1Cat.add(entrada);
        p1.setSuitableForCeliac((RandomUtils.getIntBetween(0, 1) == 1));
        p1.setCategories(p1Cat);

        Plate p2 = new Plate(1L);
        p2.setName("Papas con Panceta");
        p2.setDescription("Entrada y Cena");
        p2.setPrice(500);
        Plate p3 = new Plate(2L);
        p3.setName("Helado");
        p3.setDescription("Postre helado");
        p3.setPrice(200);

        List<Category> p2Cat = new ArrayList<>();
        p2Cat.add(entrada);
        p2Cat.add(platoPpal);
        p2Cat.add(postre);
        p2.setCategories(p2Cat);
        p2.setSuitableForCeliac((RandomUtils.getIntBetween(0, 1) == 1));
        p2.setPicture(BitmapFactory.decodeResource(mContext.getResources(), getRandomDrawableId()));
        p3.setCategories(p2Cat);
        p3.setSuitableForCeliac((RandomUtils.getIntBetween(0, 1) == 1));
        p3.setPicture(BitmapFactory.decodeResource(mContext.getResources(), getRandomDrawableId()));

        plates.put(p1.getId(), p1);
        plates.put(p2.getId(), p2);
        plates.put(p3.getId(), p3);

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
    public boolean isDownloading() {
        return false;
    }

    @Override
    public List<Commerce> getFavouritesCommerces() {
        return null;
    }

    @Override
    public List<Commerce> getCommerces() {
        return mCommerces;
    }

    @Override
    public Commerce getCommerce(int id) {
        return mCommerces.get(id);
    }

    @Override
    public void updateCommercesWithLocation(Activity activity, UpdateCommercesCallback callback, Location location) {
        final int commercesAmount = 3;
        mCommerces = new ArrayList<>();
        for (int i = 0; i < commercesAmount; ++i) {
            mCommerces.add(createCommerce(i));
            createPlatesAndCategories(mCommerces.get(i));
        }

        callback.onCommercesUpdated();
    }
}
