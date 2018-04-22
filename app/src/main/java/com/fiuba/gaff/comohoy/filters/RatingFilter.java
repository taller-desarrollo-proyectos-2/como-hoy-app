package com.fiuba.gaff.comohoy.filters;

import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.ArrayList;
import java.util.List;

public class RatingFilter implements Filter {
    private double mPuntuation;

    public RatingFilter(double puntuation) {
        mPuntuation = puntuation;
    }

    @Override
    public List<Commerce> apply(List<Commerce> commerceList) {
        List<Commerce> filteredList = new ArrayList<>();

        for (int i = 0; i < commerceList.size(); i++) {
            Commerce commerce = commerceList.get(i);
            double puntuationConvert = commerce.getRating();
            if (puntuationConvert > mPuntuation) {
                filteredList.add(commerce);
            }
        }
        return filteredList;
    }
}