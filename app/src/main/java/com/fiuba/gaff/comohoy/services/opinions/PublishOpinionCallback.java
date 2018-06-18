package com.fiuba.gaff.comohoy.services.opinions;

public interface PublishOpinionCallback {
    void onSuccess();
    void onError(String reason);
}
