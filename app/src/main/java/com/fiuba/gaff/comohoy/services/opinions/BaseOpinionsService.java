package com.fiuba.gaff.comohoy.services.opinions;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.fiuba.gaff.comohoy.model.Opinion;
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
import java.util.List;

public class BaseOpinionsService implements OpinionsService {

    private static final String GET_OPINIONS_URL_FORMAT = "http://34.237.197.99:9000/api/v1/qualifications?commerceId=%d";
    private static final String POST_OPINION_URL = "http://34.237.197.99:9000/api/v1/qualifications";

    private List<Opinion> mOpinions;

    @Override
    public void getOpinions(Activity activity,int commerceId, final OnGetOpinionsCallback callback) {
        NetworkObject getRequestsNetworkObject = createGetOpinionsNetworkObject(commerceId);
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), getRequestsNetworkObject);
        networkFragment.startDownload(new DownloadCallback<NetworkResult>() {
            @Override
            public void onResponseReceived(@NonNull NetworkResult result) {
                if (result.mException == null) {
                    try {
                        List<Opinion> opinions = parseOpinionsFromResponse(result.mResultValue);
                        callback.onSuccess(opinions);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("No se pudiieron obetener sus órdenes. Revise su conección");
                    }
                } else {
                    callback.onError("No se pudiieron obetener sus órdenes. Revise su conección");
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
    public void publishOpinion(Activity activity, Opinion opinion, final PublishOpinionCallback callback) {
        NetworkObject networkObject = createPublishOpinionNetworkObject(opinion);
        NetworkFragment networkFragment = NetworkFragment.getInstance(activity.getFragmentManager(), networkObject);
        networkFragment.startDownload(new DownloadCallback<NetworkResult>() {
            @Override
            public void onResponseReceived(@NonNull NetworkResult result) {
                if (result.mException == null) {
                    callback.onSuccess();
                } else {
                    callback.onError(result.mException.getMessage());
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

    private NetworkObject createGetOpinionsNetworkObject(int commerceId) {
        String requestUri = String.format(GET_OPINIONS_URL_FORMAT, commerceId);
        NetworkObject networkObject = new NetworkObject(requestUri, HttpMethodType.GET);
        networkObject.setAuthToken(ServiceLocator.get(FacebookService.class).getAuthToken());
        return networkObject;
    }

    private List<Opinion> parseOpinionsFromResponse(String response) throws JSONException {
        List<Opinion> opinions = new ArrayList<>();
        try {
            JSONArray opinionsJson = new JSONArray(response);
            for(int i = 0; i < opinionsJson.length(); i++) {
                JSONObject opinionJson = opinionsJson.getJSONObject(i);
                Opinion opinion = getOpinionFromJson(opinionJson);
                opinions.add(opinion);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return opinions;
    }

    private NetworkObject createPublishOpinionNetworkObject(Opinion opinion) {
        String requestBody = createSubmitOpinionJson(opinion).toString();
        NetworkObject networkObject = new NetworkObject(POST_OPINION_URL, HttpMethodType.POST, requestBody);
        networkObject.setAuthToken(ServiceLocator.get(FacebookService.class).getAuthToken());
        return networkObject;
    }

    private JSONObject createSubmitOpinionJson(Opinion opinion) {
        JSONObject submitOpinionJson = new JSONObject();
        try {
            JSONObject orderIdJson = new JSONObject();
            orderIdJson.put("id", opinion.getOrderId());
            submitOpinionJson.put("request", orderIdJson);
            submitOpinionJson.put("score", opinion.getPuntuation());
            submitOpinionJson.put("comment", opinion.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return submitOpinionJson;
    }

    private Opinion getOpinionFromJson(JSONObject opinionJson) throws JSONException {
        Long id = opinionJson.getLong("id");
        Opinion opinion = new Opinion(id);
        opinion.setPuntuation(opinionJson.getInt("score"));

        String comment = cleanString(opinionJson.getString("comment"));
        String response = cleanString(opinionJson.getString("response"));

        opinion.setDescription(comment);
        opinion.setReplica(response);

        JSONObject requestJson = opinionJson.getJSONObject("request");
        opinion.setOrderId(requestJson.getLong("id"));

        return opinion;
    }

    private String cleanString(String string) {
        if (string.equals("null")) {
            string = "";
        }
        return string;
    }

}
