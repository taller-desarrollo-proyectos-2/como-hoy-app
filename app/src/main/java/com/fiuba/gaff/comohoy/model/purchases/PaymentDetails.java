package com.fiuba.gaff.comohoy.model.purchases;

public class PaymentDetails {
    private String mCustomerFullName;
    private PaymentMethod mPaymentMethod;
    private CreditCardDetails mCardDetails;
    private int mAmountToCharge;

    public PaymentDetails(int amountToCharge, String customerName, PaymentMethod paymentMethod) {
        mAmountToCharge = amountToCharge;
        mCustomerFullName = customerName;
        mPaymentMethod = paymentMethod;
    }

    public PaymentDetails(int amountToCharge, String customerName, PaymentMethod paymentMethod, CreditCardDetails cardDetails) {
        this(amountToCharge, customerName, paymentMethod);
        mCardDetails = cardDetails;
    }
}
