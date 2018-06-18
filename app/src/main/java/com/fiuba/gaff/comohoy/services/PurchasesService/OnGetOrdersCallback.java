package com.fiuba.gaff.comohoy.services.PurchasesService;

import com.fiuba.gaff.comohoy.model.purchases.backend.Request;

import java.util.List;

public interface OnGetOrdersCallback {
    void onSuccess(List<Request> orders);
    void onError(String reason);
}
