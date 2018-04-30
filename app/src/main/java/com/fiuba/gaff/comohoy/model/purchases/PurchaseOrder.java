package com.fiuba.gaff.comohoy.model.purchases;

import java.util.List;

public class PurchaseOrder {
    private PaymentDetails mPaymentDetails;
    private List<PlateOrder> mPlateOrders;

    public PaymentDetails getPaymentDetails() {
        return mPaymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        mPaymentDetails = paymentDetails;
    }

    public List<PlateOrder> getPlateOrders() {
        return mPlateOrders;
    }

    public void setPlateOrders(List<PlateOrder> plateOrders) {
        mPlateOrders = plateOrders;
    }
}
