package com.fiuba.gaff.comohoy.services.PurchasesService;

public interface OnSubmitPurchaseCallback {
    void onSuccess();
    void onError(String reason);
}