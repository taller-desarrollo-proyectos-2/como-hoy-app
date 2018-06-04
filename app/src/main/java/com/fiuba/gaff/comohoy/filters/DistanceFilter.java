package com.fiuba.gaff.comohoy.filters;

import android.content.Context;

import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.location.LocationService;
import com.fiuba.gaff.comohoy.utils.LocationUtils;

import java.util.ArrayList;
import java.util.List;

public class DistanceFilter implements Filter {
    public float minDistance;
    public float maxDistance;
    public Context context;

    public DistanceFilter(float min, float max, Context mContext) {
        minDistance = min;
        maxDistance = max;
        context = mContext;
    }

    @Override
    public List<Commerce> apply(List<Commerce> commerceList) {
        List<Commerce> filteredList = new ArrayList<>();
        LocationService locationService = ServiceLocator.get(LocationService.class);
        Location mCurrentLocation = locationService.getLocation(context);
        for (int i = 0; i < commerceList.size(); i++) {
            Commerce commerce = commerceList.get(i);
            double puntuationConvert = LocationUtils.GetDistanceBetween(mCurrentLocation, commerce.getLocation(), LocationUtils.DistanceUnit.KILOMETERS);
            if ((puntuationConvert >= minDistance) && (puntuationConvert <= maxDistance)){
                filteredList.add(commerce);
            }
        }
        return filteredList;
    }
}