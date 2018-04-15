package com.fiuba.gaff.comohoy.services.facebook;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fiuba.gaff.comohoy.LoginActivity;
import com.fiuba.gaff.comohoy.R;
import com.fiuba.gaff.comohoy.networking.DownloadCallback;
import com.fiuba.gaff.comohoy.networking.HttpMethodType;
import com.fiuba.gaff.comohoy.networking.NetworkFragment;
import com.fiuba.gaff.comohoy.networking.NetworkObject;
import com.fiuba.gaff.comohoy.networking.NetworkResult;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseFacebookService implements FacebookService {

    private static final String POST_USERID_URL = "http://34.237.197.99:9000/api/v1/commerces";

    private Context mContext;
    private CallbackManager mCallbackManager;
    private boolean mDownloading = false;
    private String mAuthToken = null;

    public BaseFacebookService(Context context) {
        mContext = context;
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public String getAuthToken() {
        if (mAuthToken == null) {
            Log.e("FacebookService", "Auth token is null");
        }
        return mAuthToken;
    }

    @Override
    public void initializeLoginButton(Activity activity, LoginCallback loginCallback) {
        LoginButton loginButton = (LoginButton) activity.findViewById(R.id.login_button);
        setLoginButtonPermissions(loginButton);

        FacebookCallback<LoginResult> facebookCallback = createFacebookLoginCallback(loginCallback, activity.getFragmentManager());
        registerLoginButtonCallbacks(loginButton, facebookCallback);

    }

    @Override
    public void initializeLoginButton(Fragment fragment, LoginCallback loginCallback) {
        LoginButton loginButton = (LoginButton) fragment.getView().findViewById(R.id.login_button);
        setLoginButtonPermissions(loginButton);
        loginButton.setFragment(fragment);

        FacebookCallback<LoginResult> facebookCallback = createFacebookLoginCallback(loginCallback, fragment.getFragmentManager());
        registerLoginButtonCallbacks(loginButton, facebookCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackManager callbackManager = getCallbackManager();
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setLoginButtonPermissions(LoginButton loginButton) {
        loginButton.setReadPermissions("email");
    }

    private void registerLoginButtonCallbacks(LoginButton loginButton, FacebookCallback<LoginResult> loginCallback) {
        CallbackManager callbackManager = getCallbackManager();
        loginButton.registerCallback(callbackManager, loginCallback);
    }

    private FacebookCallback<LoginResult> createFacebookLoginCallback(final LoginCallback loginCallback, final FragmentManager fragmentManager) {
        FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //login result tiene access token y otros valores relevantes a la sesion
                Log.i("LoginActivity", "Login to facebook");

                requestAuthToken(loginCallback, fragmentManager);
            }

            @Override
            public void onCancel() {
                Log.i("LoginActivity", "Cancel login");
                loginCallback.onCancel();
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("LoginActivity", "error logging in");
                loginCallback.onError();
            }
        };
        return facebookCallback;
    }

    private void requestAuthToken(final LoginCallback loginCallback, final FragmentManager fragmentManager) {
        NetworkObject requestTokenObject = createRequestTokenObject();
        NetworkFragment networkFragment = NetworkFragment.getInstance(fragmentManager, requestTokenObject);
        if (!mDownloading) {
            mDownloading = true;
            networkFragment.startDownload(new DownloadCallback<NetworkResult>() {
                @Override
                public void onResponseReceived(NetworkResult result) {
                    try {
                        mAuthToken = new JSONObject(result.mResultValue).getString("authorization");
                        loginCallback.onSuccess();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loginCallback.onError();
                    }
                    mDownloading = false;
                }

                @Override
                public NetworkInfo getActiveNetworkInfo(Context context) {
                    ConnectivityManager connectivityManager =
                            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    return networkInfo;
                }

                @Override
                public void onProgressUpdate(int progressCode, int percentComplete) {}

                @Override
                public void onFinishDownloading() {
                    mDownloading = false;
                }
            });
        }
    }

    private NetworkObject createRequestTokenObject() {
        String requestBody = createRequestTokenJson().toString();
        NetworkObject networkObject = new NetworkObject(POST_USERID_URL, HttpMethodType.POST, requestBody);
        return networkObject;
    }

    private JSONObject createRequestTokenJson() {
        JSONObject requestTokenJsonObject = new JSONObject();
        try {
            final String userIdField = "token";
            String userId = Profile.getCurrentProfile().getId();
            requestTokenJsonObject.put(userIdField, userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestTokenJsonObject;
    }

    private CallbackManager getCallbackManager() {
        if (mCallbackManager == null) {
            mCallbackManager = CallbackManager.Factory.create();
        }
        return mCallbackManager;
    }
}
