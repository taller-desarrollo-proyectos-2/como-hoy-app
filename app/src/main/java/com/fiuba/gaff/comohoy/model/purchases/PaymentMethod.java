package com.fiuba.gaff.comohoy.model.purchases;

public enum PaymentMethod {
    Cash("Efectivo"),
    CreditCard("Tarjeta de Crédito");

    private final String text;

    PaymentMethod(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static PaymentMethod fromString(String paymentMethodString) {
        switch (paymentMethodString) {
            case "Efectivo": return PaymentMethod.Cash;
            case "Tarjeta de Crédito": return PaymentMethod.CreditCard;
            default: return PaymentMethod.Cash;
        }
    }
}
