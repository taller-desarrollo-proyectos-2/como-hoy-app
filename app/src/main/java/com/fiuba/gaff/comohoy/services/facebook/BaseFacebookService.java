package com.fiuba.gaff.comohoy.services.facebook;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fiuba.gaff.comohoy.R;

public class BaseFacebookService implements FacebookService {

    private CallbackManager mCallbackManager;

    public BaseFacebookService() {
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void initializeLoginButton(Activity activity, FacebookCallback<LoginResult> loginCallback) {
        LoginButton loginButton = (LoginButton) activity.findViewById(R.id.login_button);
        setLoginButtonPermissions(loginButton);
        registerLoginButtonCallbacks(loginButton, loginCallback);

    }

    @Override
    public void initializeLoginButton(Fragment fragment, FacebookCallback<LoginResult> loginCallback) {
        LoginButton loginButton = (LoginButton) fragment.getView().findViewById(R.id.login_button);
        setLoginButtonPermissions(loginButton);
        loginButton.setFragment(fragment);
        registerLoginButtonCallbacks(loginButton, loginCallback);
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

    private CallbackManager getCallbackManager() {
        if (mCallbackManager == null) {
            mCallbackManager = CallbackManager.Factory.create();
        }
        return mCallbackManager;
    }
}
