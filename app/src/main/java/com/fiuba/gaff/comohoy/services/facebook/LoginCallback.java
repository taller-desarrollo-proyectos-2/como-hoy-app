package com.fiuba.gaff.comohoy.services.facebook;

public interface LoginCallback {
    void onSuccess();
    void onCancel();
    void onError(String reason);
}
