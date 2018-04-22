package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.content.Context;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.R;
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
        Commerce c1 = new Commerce("Banchero");
        c1.setDescription("Pizzas y Empanadas");
        c1.setDiscounts("20% off en empanadas de ensalada");
        c1.setRating("3.4");
        c1.setShippingCost("$50");
        c1.setShippingTime("30-45 min");
        c1.setPicture(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.luigi));

        Commerce c2 = new Commerce("La Esquina de Tito");
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
        return mCommerces.get(0);
    }
}
