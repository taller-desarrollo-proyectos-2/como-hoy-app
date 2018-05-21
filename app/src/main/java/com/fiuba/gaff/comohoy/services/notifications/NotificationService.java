package com.fiuba.gaff.comohoy.services.notifications;

import android.app.Activity;

import com.fiuba.gaff.comohoy.services.CustomService;


public interface NotificationService extends CustomService {
    void sendInstanceIdToken(String token, SendInstanceIdCallback callback);
}
