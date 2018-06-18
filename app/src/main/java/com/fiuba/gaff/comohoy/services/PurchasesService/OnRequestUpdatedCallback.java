package com.fiuba.gaff.comohoy.services.PurchasesService;


public interface OnRequestUpdatedCallback {
    void onSuccess();
    void onError(String reason);
}
