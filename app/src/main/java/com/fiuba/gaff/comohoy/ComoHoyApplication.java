package com.fiuba.gaff.comohoy;

import android.app.Application;
import android.content.Context;

import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.facebook.BaseFacebookService;
import com.fiuba.gaff.comohoy.services.facebook.FacebookService;

public class ComoHoyApplication extends Application {
    public ComoHoyApplication() {
        // this method fires only once per application start.
        // getApplicationContext returns null here
        super();
    }

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
    }

}
