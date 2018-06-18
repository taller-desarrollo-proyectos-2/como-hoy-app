package com.fiuba.gaff.comohoy.filters;

import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.ArrayList;
import java.util.List;

public class RatingFilter implements Filter {
    private float mPuntuationMin;
    private float mPuntuationMax;

    public RatingFilter(float min, float max ) {
        mPuntuationMin = min;
        mPuntuationMax = max;
    }

    @Override
    public List<Commerce> apply(List<Commerce> commerceList) {
        List<Commerce> filteredList = new ArrayList<>();

        for (int i = 0; i < commerceList.size(); i++) {
            Commerce commerce = commerceList.get(i);
            double puntuationConvert = commerce.getRating();
            if (puntuationConvert > 5){
                puntuationConvert = 5;
            }
            if ((puntuationConvert >= mPuntuationMin) && (puntuationConvert <= mPuntuationMax)){
                filteredList.add(commerce);
            }
            if (puntuationConvert == 0){
                filteredList.add(commerce);
            }
        }
        return filteredList;
    }
}