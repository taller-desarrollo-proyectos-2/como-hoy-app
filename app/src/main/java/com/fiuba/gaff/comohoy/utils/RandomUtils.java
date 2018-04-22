package com.fiuba.gaff.comohoy.utils;

import java.util.Random;

public class RandomUtils {
    public static int getIntBetween(int min, int max) {
        Random rand = new Random();
        int result = rand.nextInt(max - min + 1) + min;
        return result;
    }

    public static double getDoubleBetween(double min, double max) {
        Random rand = new Random();
        double result = min + rand.nextDouble() * (max - min);
        return result;
    }
}
