package com.fiuba.gaff.comohoy;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.fiuba.gaff.comohoy.services.commerces.MockCommercesService;
import com.fiuba.gaff.comohoy.services.picasso.PicassoService;
import com.fiuba.gaff.comohoy.services.PurchasesService.BasePurchasesService;
import com.fiuba.gaff.comohoy.services.PurchasesService.PurchasesService;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.commerces.BaseCommercesService;
import com.fiuba.gaff.comohoy.services.commerces.CommercesService;
import com.fiuba.gaff.comohoy.services.facebook.BaseFacebookService;
import com.fiuba.gaff.comohoy.services.facebook.FacebookService;
import com.fiuba.gaff.comohoy.services.location.GpsLocationService;
import com.fiuba.gaff.comohoy.services.location.LocationService;

import java.util.Locale;

public class ComoHoyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // this method fires once as well as constructor
        // but also application has context here
        bindServices(getApplicationContext());
        setLanguageToSpanish(getApplicationContext());
    }

    private void bindServices(Context applicationContext) {
        ServiceLocator.init(applicationContext);
        ServiceLocator.bindCustomServiceImplementation(FacebookService.class, BaseFacebookService.class);
        ServiceLocator.bindCustomServiceImplementation(CommercesService.class, BaseCommercesService.class);
        ServiceLocator.bindCustomServiceImplementation(LocationService.class, GpsLocationService.class);
        ServiceLocator.bindCustomServiceImplementation(PurchasesService.class, BasePurchasesService.class);
        ServiceLocator.bindCustomServiceImplementation(PicassoService.class, PicassoService.class);
    }

    public void setLanguageToSpanish(Context context)
    {
        Configuration config = context.getResources().getConfiguration();
        String languageCode = "es";
        if (!config.locale.getLanguage().equals(languageCode))
        {
            Locale locale = new Locale(languageCode);
            Locale.setDefault(locale);
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }
}
