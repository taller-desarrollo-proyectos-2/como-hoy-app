package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.content.Context;

import com.fiuba.gaff.comohoy.filters.CategoryFilter;
import com.fiuba.gaff.comohoy.filters.Filter;
import com.fiuba.gaff.comohoy.model.CategoryUsageData;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.services.CustomService;

import java.util.List;

public interface CommercesService extends CustomService {

    void updateCommercesData(Activity activity, UpdateCommercesCallback callback);
    void updateCommercesWithLocation(Activity activity, UpdateCommercesCallback callback, Location location);
    void addToFavourites(Activity activity, int commerceId, AddToFavouriteCallback callback);
    void removeFromFavourites(Activity activity, int commerceId, RemoveFromFavouritesCallback callback);
    List<Commerce> getFavouritesCommerces();
    List<Commerce> getCommerces();
    List<Commerce> getCommercesSortedBy(Context context, SortCriteria sortCriteria);
    List<CategoryUsageData> getUsedCategoriesUsageData();
    Commerce getCommerce(int id);
    void addCategoryFilter(CategoryFilter categoryFilter);
    void addFilter(Filter filter);
    void clearCategoryFilters();
    void clearFilters();
    boolean isDownloading();
}
