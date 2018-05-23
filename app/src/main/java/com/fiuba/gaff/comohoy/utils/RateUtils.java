package com.fiuba.gaff.comohoy.utils;


public class RateUtils {
    public static double cleanRate(double rate) {
        double intPart = new Double(rate).intValue();
        double decimalPart = 0.0;
        double remainder = rate - intPart;
        if ((remainder < 0.25) && (remainder <= 0.75)) {
            decimalPart = 0.5;
        } else if (remainder > 0.75) {
            decimalPart = 1.0;
        }
        return (intPart + decimalPart);
    }
}
