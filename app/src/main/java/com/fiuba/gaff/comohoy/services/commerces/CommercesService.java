package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.content.Context;

import com.fiuba.gaff.comohoy.model.Categorie;
import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.services.CustomService;

import java.util.List;

public interface CommercesService extends CustomService {

    void updateCommercesData(Activity activity, UpdateCommercesCallback callback);
    void updateCommercesWithLocation(Activity activity, UpdateCommercesCallback callback, Location location);
    List<Commerce> getCommerces();
    List<Commerce> getCommercesSortedBy(Context context, SortCriteria sortCriteria);
    List<Category> getUsedCategories();
    Commerce getCommerce(int id);
}
