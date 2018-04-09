package com.fiuba.gaff.comohoy.services.commerces;

import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.ArrayList;
import java.util.List;

public class MockCommercesService implements CommercesService {

    private List<Commerce> mCommerces;

    @Override
    public void syncFromServer() {
        mCommerces = new ArrayList<>();
        Commerce c1 = new Commerce("Banchero");
        c1.setDescription("Pizzas y Empanadas");
        c1.setDiscounts("20% off en empanadas de ensalada");
        c1.setRating("3.4");
        c1.setShippingCost("$50");
        c1.setShippingTime("30-45 min");

        Commerce c2 = new Commerce("La Esquina de Tito");
        c1.setDescription("Chori y Bondioletas");
        c1.setRating("4.5");
        c1.setShippingCost("$15");
        c1.setShippingTime("15-30 min");

        mCommerces.add(c1);
        mCommerces.add(c2);
    }

    @Override
    public List<Commerce> getCommerces() {
        if (mCommerces == null) {
            syncFromServer();
        }
        return mCommerces;
    }

    @Override
    public Commerce getCommerce(int index) {
        return mCommerces.get(index);
    }
}
