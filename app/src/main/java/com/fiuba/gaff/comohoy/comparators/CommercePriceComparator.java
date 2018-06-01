package com.fiuba.gaff.comohoy.comparators;

import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.Comparator;

public class CommercePriceComparator implements Comparator<Commerce> {

    @Override
    public int compare(Commerce commerce1, Commerce commerce2) {
        double price1 = commerce1.getAveragePrice();
        double price2 = commerce2.getAveragePrice();

        if (price1 <= 0) {
            return 1;
        }

        if (price2 <= 0) {
            return -1;
        }

        if (price1 < price2) {
            return -1;
        } else if (price1 > price2) {
            return 1;
        }
        return 0;
    }
}