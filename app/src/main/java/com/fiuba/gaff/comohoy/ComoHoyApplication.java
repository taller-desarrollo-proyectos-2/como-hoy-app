package com.fiuba.gaff.comohoy;

import android.app.Application;
import android.content.Context;

import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.BaseCommercesService;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.commerces.MockCommercesService;
import com.fiuba.gaff.comohoy.services.facebook.BaseFacebookService;
import com.fiuba.gaff.comohoy.services.facebook.FacebookService;
import com.fiuba.gaff.comohoy.services.location.GpsLocationService;
import com.fiuba.gaff.comohoy.services.location.LocationService;

public class ComoHoyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // this method fires once as well as constructor
        // but also application has context here
        bindServices(getApplicationContext());
    }

    private void bindServices(Context applicationContext) {
        ServiceLocator.init(applicationContext);
        ServiceLocator.bindCustomServiceImplementation(FacebookService.class, BaseFacebookService.class);
        ServiceLocator.bindCustomServiceImplementation(CommercesService.class, BaseCommercesService.class);
        ServiceLocator.bindCustomServiceImplementation(LocationService.class, GpsLocationService.class);
    }

}
