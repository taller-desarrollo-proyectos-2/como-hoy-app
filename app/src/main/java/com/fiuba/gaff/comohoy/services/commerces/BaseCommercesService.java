package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.content.Context;
import android.media.FaceDetector;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fiuba.gaff.comohoy.model.Commerce;
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
            JSONObject currCommerceJson = commercesJson.getJSONObject(i);
            String businessName = currCommerceJson.getString("businessName");
            String name = currCommerceJson.getString("name");
            int id = currCommerceJson.getInt("id");
            Commerce commerce = new Commerce(name);
            commerce.setBusinessName(businessName);
            commerce.setId(id);
            mCommerces.put(id, commerce);
        }
    }

}
