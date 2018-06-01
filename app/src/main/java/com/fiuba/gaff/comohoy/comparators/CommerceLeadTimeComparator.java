package com.fiuba.gaff.comohoy.comparators;

import com.fiuba.gaff.comohoy.model.Commerce;

import java.util.Comparator;

public class CommerceLeadTimeComparator implements Comparator<Commerce> {

    @Override
    public int compare(Commerce commerce1, Commerce commerce2) {
        double leadTime1 = commerce1.getAveragePrice();
        double leadTime2 = commerce2.getAveragePrice();

        if (leadTime1 <= 0) {
            return 1;
        }

        if (leadTime2 <= 0) {
            return -1;
        }

        if (leadTime1 < leadTime2) {
            return -1;
        } else if (leadTime1 > leadTime2) {
            return 1;
        }
        return 0;
    }
}