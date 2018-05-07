package com.fiuba.gaff.comohoy.services.PurchasesService;

import com.fiuba.gaff.comohoy.model.purchases.PaymentDetails;
import com.fiuba.gaff.comohoy.model.purchases.PlateOrder;
import com.fiuba.gaff.comohoy.services.CustomService;

import java.util.List;
import java.util.Map;

public interface PurchasesService extends CustomService {
    Cart getCart();
    PaymentDetails getPaymentDetails();
    void submitPurchase();
    void setPaymentDetails(PaymentDetails paymentDetails);
    void assignCommerce(int commerceId);
    void addPlateOrderToCart(PlateOrder plateOrder);
    void modifyPlateOrder(Long orderId, PlateOrder plateOrder);
    void removePlateOrder(Long orderId);
    void clearCart();
    void clearPaymentDetails();
    boolean isCartEmpty();

}
