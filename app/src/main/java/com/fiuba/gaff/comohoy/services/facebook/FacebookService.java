package com.fiuba.gaff.comohoy.services.facebook;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;
import com.fiuba.gaff.comohoy.services.CustomService;

public interface FacebookService extends CustomService {
    boolean isLoggedIn();
    void loginWithAccesToken (Activity activity, LoginCallback loginCalback);
    void initializeLoginButton(Activity activity, LoginCallback loginCallback);
    void initializeLoginButton(Fragment fragment, LoginCallback loginCallback);
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
