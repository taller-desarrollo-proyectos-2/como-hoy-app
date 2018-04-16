package com.fiuba.gaff.comohoy.services.commerces;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class BaseCommercesService implements CommercesService {

    private static final String REQUEST_COMMERCES_URL = "http://34.237.197.99:9000/api/v1/commerces";

    private List<Commerce> mCommerces;

    @Override
    public void updateCommercesData(Activity activity, final UpdateCommercesCallback callback) {
        mCommerces = new ArrayList<>();
        NetworkObject updateCommercesNetworkObject = new NetworkObject(REQUEST_COMMERCES_URL, HttpMethodType.GET);
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), updateCommercesNetworkObject);
        networkFragment.startDownload(new DownloadCallback<NetworkResult>() {

            @Override
            public void onResponseReceived(NetworkResult result) {
                if (result.mException == null) {
                    callback.onCommercesUpdated();
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
        return mCommerces;
    }

    @Override
    public Commerce getCommerce(int index) {
        return mCommerces.get(index);
    }

}
