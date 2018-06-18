package com.fiuba.gaff.comohoy.firebase;

import com.fiuba.gaff.comohoy.services.ServiceLocator;
import com.fiuba.gaff.comohoy.services.notifications.NotificationService;
import com.fiuba.gaff.comohoy.services.notifications.SendInstanceIdCallback;
import com.google.firebase.iid.FirebaseInstanceIdService;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        getNotificationService().sendInstanceIdToken(token, new SendInstanceIdCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "Se envió el instance Id token " + token + "con éxito" );
            }

            @Override
            public void onError(String reason) {
                Log.w(TAG, reason);
            }
        });
    }

    private NotificationService getNotificationService() {
        return ServiceLocator.get(NotificationService.class);
    }
}
