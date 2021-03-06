package com.fiuba.gaff.comohoy.model.purchases;

public class PaymentDetails {
    private Address mShippingAddress;
    private PaymentMethod mPaymentMethod;
    private CreditCardDetails mCardDetails;
    private double mAmountToCharge;

    public PaymentDetails() {
        reset();
    }

    public Address getShippingAddress() {
        return mShippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        mShippingAddress = shippingAddress;
    }

    public PaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        mPaymentMethod = paymentMethod;
    }

    public CreditCardDetails getCardDetails() {
        return mCardDetails;
    }

    public void setCardDetails(CreditCardDetails cardDetails) {
        mCardDetails = cardDetails;
    }

    public double getAmountToCharge() {
        return mAmountToCharge;
    }

    public void setAmountToCharge(double amountToCharge) {
        mAmountToCharge = amountToCharge;
    }

    public void reset() {
        mAmountToCharge = 0;
        mPaymentMethod = PaymentMethod.Cash;
        mShippingAddress = new Address();
    }
}
