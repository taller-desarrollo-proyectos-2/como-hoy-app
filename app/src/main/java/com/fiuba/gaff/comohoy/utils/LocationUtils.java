package com.fiuba.gaff.comohoy.utils;

import com.fiuba.gaff.comohoy.model.Location;

public class LocationUtils {

    private static final double EARTH_RADIUS_KM = 6371;
    private static final double EARTH_RADIUS_MILES = 3958.5;

    public enum DistanceUnit { KILOMETERS, MILES }

    public static double GetDistanceBetween(Location loc1, Location loc2, DistanceUnit unit) {

        final double earthRadius = getEarthRadius(unit); // in miles, change to 6371 for kilometer output

        final double lat1 = loc1.getLatitud();
        final double lng1 = loc1.getLongitud();
        final double lat2 = loc2.getLatitud();
        final double lng2 = loc2.getLongitud();

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist;
    }

    private static double getEarthRadius(DistanceUnit unit) {
        switch (unit) {
            case MILES: return EARTH_RADIUS_MILES;
            case KILOMETERS: return EARTH_RADIUS_KM;
            default: return EARTH_RADIUS_KM;
        }
    }
}
