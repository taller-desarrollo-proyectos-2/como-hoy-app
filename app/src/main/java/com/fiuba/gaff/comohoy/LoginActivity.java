package com.fiuba.gaff.comohoy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.facebook.FacebookService;
import com.fiuba.gaff.comohoy.services.facebook.LoginCallback;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private View mLoginFormView;
    private ProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        Button loginButton = (Button) findViewById(R.id.login_button);

        initializeLoginButton(loginButton);

        FacebookService facebookService = getFacebookService();
        if (facebookService.isLoggedIn()) {
            showProgress(true);
            facebookService.loginWithAccesToken(this, createLoginCallback());
        }
    }

    private void initializeLoginButton(Button mLoginButton) {
        final FacebookService facebookService = getFacebookService();
        facebookService.initializeLoginButton(this, createLoginCallback());

        Button loginButton = (Button) mLoginButton;
        loginButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (!facebookService.isLoggedIn()) {
                    showProgress(true);
                }
            }
        });
    }

    private LoginCallback createLoginCallback() {
        return new LoginCallback() {
            public void onSuccess() {
                showProgress(false);
                Intent goToMainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(goToMainActivityIntent);
                // finish();
            }

            @Override
            public void onCancel() {
                showProgress(false);
            }

            public void onError(String reason) {
                showProgress(false);
                showErrorToast(reason);
            }
        };
    }


    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FacebookService facebookService = getFacebookService();
        facebookService.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private FacebookService getFacebookService() {
        return ServiceLocator.get(FacebookService.class);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

