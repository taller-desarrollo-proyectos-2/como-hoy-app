package com.fiuba.gaff.comohoy.comparators;

import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.Comparator;

public class CommerceRatingComparator implements Comparator<Commerce> {

    @Override
    public int compare(Commerce commerce1, Commerce commerce2) {
        double rating1 = commerce1.getRating();
        double rating2 = commerce2.getRating();
        if (rating1 < rating2) {
            return 1;
        } else if (rating1 > rating2) {
            return -1;
        }
        return 0;
    }
}