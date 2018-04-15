package com.fiuba.gaff.comohoy.services.facebook;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;
import com.fiuba.gaff.comohoy.services.CustomService;

public interface FacebookService extends CustomService {
    String getAuthToken();
    void initializeLoginButton(Activity activity, LoginCallback loginCallback);
    void initializeLoginButton(Fragment fragment, LoginCallback loginCallback);
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
