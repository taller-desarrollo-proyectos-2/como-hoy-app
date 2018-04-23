package com.fiuba.gaff.comohoy.services.location;

import android.content.Context;

import com.fiuba.gaff.comohoy.model.Location;

public class GpsLocationService implements LocationService {

    public Location getLocation(Context context){
        Location location = new Location(0, 0);
        GpsTracker gpsTracker = new GpsTracker(context);
        if(gpsTracker.canGetLocation()){
            location.setLatitud(gpsTracker.getLatitude());
           location.setLongitud(gpsTracker.getLongitude());
        }else{
            gpsTracker.showSettingsAlert();
        }
        return location;
    }
}
