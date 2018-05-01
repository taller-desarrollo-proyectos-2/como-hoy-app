package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fiuba.gaff.comohoy.comparators.CommerceLocationComparator;
import com.fiuba.gaff.comohoy.model.CategoriesList;
import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Day;
import com.fiuba.gaff.comohoy.model.Extra;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.model.OpeningTime;
import com.fiuba.gaff.comohoy.model.Plate;
import com.fiuba.gaff.comohoy.model.TimeInterval;
import com.fiuba.gaff.comohoy.networking.DownloadCallback;
import com.fiuba.gaff.comohoy.networking.HttpMethodType;
import com.fiuba.gaff.comohoy.networking.NetworkFragment;
import com.fiuba.gaff.comohoy.networking.NetworkObject;
import com.fiuba.gaff.comohoy.networking.NetworkResult;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.facebook.FacebookService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCommercesService implements CommercesService {

    private static final String REQUEST_COMMERCES_URL = "http://34.237.197.99:9000/api/v1/commerces";
    private static final String REQUEST_COMMERCES_WITH_LOC_FORMAT = "http://34.237.197.99:9000/api/v1/commerces?lat=%f&lng=%f";
    private Context mContext;

    private Map<Integer, Commerce> mCommerces;

    public BaseCommercesService(Context context){
        mContext = context;
    }

    @Override
    public void updateCommercesData(Activity activity, final UpdateCommercesCallback callback) {
        NetworkObject updateCommercesNetworkObject = createUpdateCommercesNetworkObject(REQUEST_COMMERCES_URL);
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), updateCommercesNetworkObject);
        downloadCommerces(networkFragment, callback);
    }

    @Override
    public void updateCommercesWithLocation(Activity activity, final UpdateCommercesCallback callback, Location location) {
        final String uri = String.format(REQUEST_COMMERCES_WITH_LOC_FORMAT, location.getLatitud(), location.getLongitud());
        NetworkObject updateCommercesNetworkObject = createUpdateCommercesNetworkObject(uri);
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), updateCommercesNetworkObject);
        downloadCommerces(networkFragment, callback);
    }

    @Override
    public List<Commerce> getCommerces() {
        return new ArrayList<>(mCommerces.values());
    }

    @Override
    public List<Commerce> getCommercesSortedBy(Context context, SortCriteria sortCriteria) {
        List<Commerce> commerces = new ArrayList<>(mCommerces.values());
        switch (sortCriteria) {
            case Closeness:
                Collections.sort(commerces, new CommerceLocationComparator(context));
                break;
            default:
                Collections.sort(commerces, new CommerceLocationComparator(context));
        }
        return commerces;
    }

    @Override
    public List<List<Category>> getUsedCategories() {
        CategoriesList categorias = new CategoriesList(mContext);
        return categorias.getListOfList();
    }

    @Override
    public Commerce getCommerce(int id) {
        if (mCommerces.containsKey(id)) {
            return mCommerces.get(id);
        }
        Log.w("CommercesService", "There is no commerce with id " + id);
        return null;
    }

    private NetworkObject createUpdateCommercesNetworkObject(String url) {
        NetworkObject updateCommercesNetworkObject = new NetworkObject(url, HttpMethodType.GET);
        String authToken = ServiceLocator.get(FacebookService.class).getAuthToken();
        updateCommercesNetworkObject.setAuthToken(authToken);
        return updateCommercesNetworkObject;
    }

    private void getCommercesFromResponse(String response) throws JSONException {
        JSONArray commercesJson = new JSONArray(response);
        for(int i = 0; i < commercesJson.length(); i++) {
            JSONObject commerceJson = commercesJson.getJSONObject(i);

            int commerceId = commerceJson.getInt("id");
            Commerce commerce = new Commerce(commerceId);
            commerce.setName(commerceJson.getString("name"));
            commerce.setBusinessName(commerceJson.getString("businessName"));
            commerce.setCategories(getCommerceCategories(commerceJson));
            commerce.setPlates(getCommercePlates(commerceJson));
            commerce.setLocation(getCommerceLocation(commerceJson));
            commerce.setOpeningTimes(getOpeningTimes(commerceJson));

            mCommerces.put(commerceId, commerce);
        }
    }

    private Location getCommerceLocation(JSONObject commerceJson) throws  JSONException {
        JSONObject locationObject = commerceJson.getJSONObject("location");
        double lat = locationObject.getDouble("lat");
        double lon = locationObject.getDouble("lng");
        return new Location(lat, lon);
    }

    private List<Category> getCommerceCategories(JSONObject commerceJson) throws  JSONException {
        List<Category> categories = new ArrayList<>();
        JSONArray categoriesArray = commerceJson.getJSONArray("categories");
        for(int i = 0; i < categoriesArray.length(); i++) {
            JSONObject categoryObject = categoriesArray.getJSONObject(i);
            Long id = categoryObject.getLong("id");
            String name = categoryObject.getString("name");
            Category category = new Category(id, name);
            categories.add(category);
        }
        return categories;
    }

    private HashMap<Long, Plate> getCommercePlates(JSONObject commerceJson) {
        HashMap<Long, Plate> plates = new HashMap<>();
        try {
            JSONArray platesArray = commerceJson.getJSONArray("plates");
            int a = platesArray.length();
            for (int i = 0; i < platesArray.length(); ++i) {
                JSONObject plateJson = platesArray.getJSONObject(i);
                Plate plate = new Plate(plateJson.getLong("id"));
                plate.setName(plateJson.getString("name"));
                plate.setPrice(plateJson.getDouble("price"));
                plate.setSuitableForCeliac(plateJson.getBoolean("glutenFree"));
                plate.setCategories(getPlateCategories(plateJson));
                plate.setExtras(getPlateExtras(plateJson));

                plates.put(plate.getId(), plate);
            }
        } catch (JSONException e) {
            Log.e("CommerceService", "Error parsing plates: " + e.getMessage());
            e.printStackTrace();
        }
        return plates;
    }

    private List<Category> getPlateCategories(JSONObject plateJson) throws JSONException {
        List<Category> categories = new ArrayList<>();
        JSONObject categotyObject = plateJson.getJSONObject("category");
        Long id = categotyObject.getLong("id");
        String name = categotyObject.getString("name");
        Category category = new Category(id, name);
        categories.add(category);
        return categories;
    }

    private List<Extra> getPlateExtras(JSONObject plateJson) {
        List<Extra> extras = new ArrayList<>();
        try {
            JSONArray extrasArray = plateJson.getJSONArray("optionals");
            for(int i = 0; i < extrasArray.length(); i++) {
                JSONObject extraObject = extrasArray.getJSONObject(i);
                Long id = extraObject.getLong("id");
                Extra extra = new Extra(id);
                extra.setName(extraObject.getString("name"));
                extra.setPrice(extraObject.getDouble("price"));
                extras.add(extra);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return extras;
    }

    private HashMap<Day, OpeningTime> getOpeningTimes(JSONObject commerceJson) throws JSONException {
        HashMap<Day, OpeningTime> openingTimesMap = new HashMap<>();
        JSONArray timesArray = commerceJson.getJSONArray("times");
        for(int i = 0; i < timesArray.length(); i++) {
            JSONObject openingTimeObject = timesArray.getJSONObject(i);
            OpeningTime openingTime = getOpeningTime(openingTimeObject);
            openingTimesMap.put(openingTime.getDay(), openingTime);
        }
        return openingTimesMap;
    }

    private OpeningTime getOpeningTime(JSONObject openingTimeJson) {
        OpeningTime openingTime = new OpeningTime();
        try {
            Day day = Day.fromString(openingTimeJson.getString("day"));
            openingTime.setDay(day);

            Long fromHour = openingTimeJson.getLong("fromHour");
            Long toHour = openingTimeJson.getLong("toHour");
            TimeInterval timeInterval = new TimeInterval(new Timestamp(fromHour), new Timestamp(toHour));
            openingTime.setOpeningTimes(timeInterval);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return openingTime;
    }

    private void downloadCommerces(final NetworkFragment networkFragment, final UpdateCommercesCallback callback) {
        mCommerces = new HashMap<>();
        networkFragment.startDownload(new DownloadCallback<NetworkResult>() {

            @Override
            public void onResponseReceived(NetworkResult result) {
                if (result.mException == null) {
                    try {
                        getCommercesFromResponse(result.mResultValue);
                        callback.onCommercesUpdated();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.e("CommercesService", result.mException.getMessage());
                    callback.onError("No se pudieron cargar comercios. Verifique su conección de internet");
                }
            }

            @Override
            public NetworkInfo getActiveNetworkInfo(Context context) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo;
            }

            @Override
            public void onProgressUpdate(int progressCode, int percentComplete) {

            }

            @Override
            public void onFinishDownloading() {

            }
        });
    }
}
