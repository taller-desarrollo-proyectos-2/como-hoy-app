package com.fiuba.gaff.comohoy.services.PurchasesService;

import android.app.Activity;

import com.fiuba.gaff.comohoy.model.purchases.PaymentDetails;
import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;
import com.fiuba.gaff.comohoy.model.purchases.backend.Request;
import com.fiuba.gaff.comohoy.services.CustomService;

import java.util.List;

public interface PurchasesService extends CustomService {
    Cart getCart();
    PaymentDetails getPaymentDetails();
    List<Request> getOrdersCached();
    void getOrdersFromServer(Activity activity, OnGetOrdersCallback callback);
    void submitPurchase(Activity activity, OnSubmitPurchaseCallback callback);
    void setPaymentDetails(PaymentDetails paymentDetails);
    void assignCommerce(int commerceId);
    void addPlateOrderToCart(PlateOrder plateOrder);
    void modifyPlateOrder(Long orderId, PlateOrder plateOrder);
    void removePlateOrder(Long orderId);
    void clearCart();
    void clearPaymentDetails();
    boolean isCartEmpty();

}
