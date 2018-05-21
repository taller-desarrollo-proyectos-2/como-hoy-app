package com.fiuba.gaff.comohoy.services.notifications;

public interface SendInstanceIdCallback {
    void onSuccess();
    void onError(String reason);
}
