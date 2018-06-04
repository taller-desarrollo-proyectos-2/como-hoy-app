package com.fiuba.gaff.comohoy.filters;

import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.ArrayList;
import java.util.List;

public class PriceFilter implements Filter {
    public static float INFINITE = 999999;
    public float minPrice;
    public float maxPrice;

    public PriceFilter( float min, float max) {
        minPrice = min;
        maxPrice = max;
    }

    @Override
    public List<Commerce> apply(List<Commerce> commerceList) {
        List<Commerce> filteredList = new ArrayList<>();
        for (int i = 0; i < commerceList.size(); i++) {
            Commerce commerce = commerceList.get(i);
            double priceConvert = commerce.getAveragePrice();
            if ((priceConvert >= minPrice) && (priceConvert <= maxPrice)){
                filteredList.add(commerce);
            }
        }
        return filteredList;
    }
}
