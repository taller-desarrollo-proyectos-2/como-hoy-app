package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.content.Context;
import android.media.FaceDetector;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fiuba.gaff.comohoy.model.Category;
import com.fiuba.gaff.comohoy.model.Commerce;
import com.fiuba.gaff.comohoy.model.Extra;
import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.model.Plate;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCommercesService implements CommercesService {

    private static final String REQUEST_COMMERCES_URL = "http://34.237.197.99:9000/api/v1/commerces";

    private Map<Integer, Commerce> mCommerces;

    @Override
    public void updateCommercesData(Activity activity, final UpdateCommercesCallback callback) {
        mCommerces = new HashMap<>();
        NetworkObject updateCommercesNetworkObject = createOnUpdateCommercesList();
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), updateCommercesNetworkObject);
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

    @Override
    public List<Commerce> getCommerces() {
        return new ArrayList<>(mCommerces.values());
    }

    @Override
    public Commerce getCommerce(int id) {
        if (mCommerces.containsKey(id)) {
            return mCommerces.get(id);
        }
        Log.w("CommercesService", "There is no commerce with id " + id);
        return null;
    }

    private NetworkObject createOnUpdateCommercesList() {
        NetworkObject updateCommercesNetworkObject = new NetworkObject(REQUEST_COMMERCES_URL, HttpMethodType.GET);
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
            commerce.setmLocation(getCommerceLocation(commerceJson));

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

    private List<Plate> getCommercePlates(JSONObject commerceJson) {
        List<Plate> plates = new ArrayList<>();
        try {
            JSONArray platesArray = commerceJson.getJSONArray("plates");
            int a = platesArray.length();
            for (int i = 0; i < platesArray.length(); ++i) {
                JSONObject plateJson = platesArray.getJSONObject(i);
                Plate plate = new Plate(plateJson.getLong("id"));
                plate.setName(plateJson.getString("name"));
                plate.setPrice(plateJson.getDouble("price"));
                plate.setCategories(getPlateCategories(plateJson));
                plate.setExtras(getPlateExtras(plateJson));

                plates.add(plate);
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

    private List<Extra> getPlateExtras(JSONObject plateJson) throws  JSONException {
        List<Extra> extras = new ArrayList<>();
        /*JSONArray extrasArray = plateJson.getJSONArray("optionals");
        for(int i = 0; i < extrasArray.length(); i++) {
            JSONObject extraObject = extrasArray.getJSONObject(i);
            Long id = extraObject.getLong("id");
            Extra extra = new Extra(id);
            extras.add(extra);
        }*/
        return extras;
    }

}
