package com.fiuba.gaff.comohoy.comparators;

import android.content.Context;

import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.location.LocationService;
import com.fiuba.gaff.comohoy.utils.LocationUtils;

import java.util.Comparator;

public class CommerceLocationComparator implements Comparator<Commerce>{

    private Location mCurrentLocation;

    public CommerceLocationComparator(Context context) {
        LocationService locationService = ServiceLocator.get(LocationService.class);
        mCurrentLocation = locationService.getLocation(context);
    }

    @Override
    public int compare(Commerce commerce1, Commerce commerce2) {
        double distanceToCommerce1 = LocationUtils.GetDistanceBetween(mCurrentLocation, commerce1.getLocation(), LocationUtils.DistanceUnit.KILOMETERS);
        double distanceToCommerce2 = LocationUtils.GetDistanceBetween(mCurrentLocation, commerce2.getLocation(), LocationUtils.DistanceUnit.KILOMETERS);
        if (distanceToCommerce1 < distanceToCommerce2) {
            return 1;
        } else if (distanceToCommerce1 > distanceToCommerce2) {
            return -1;
        }
        return 0;
    }
}
