package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;

import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.services.CustomService;

import java.util.List;

public interface CommercesService extends CustomService {

    void updateCommercesData(Activity activity, UpdateCommercesCallback callback);
    List<Commerce> getCommerces();
    Commerce getCommerce(int id);
}
