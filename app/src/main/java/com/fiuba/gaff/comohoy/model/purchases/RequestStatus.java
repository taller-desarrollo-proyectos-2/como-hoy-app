package com.fiuba.gaff.comohoy.model.purchases;

public enum RequestStatus{
    WaitingConfirmation("Esperando confirmación"),
    OnPreparation("En preparación"),
    OnTheWay("En camino"),
    Delivered("Entregado"),
    CanceledByUser("Cancelado"),
    CanceledByCommerce("Rechazado"),
    Unknown("Indeterminado");

    private final String mText;

    RequestStatus(String text) {
        mText = text;
    }

    public String toString() {
        return mText;
    }

    public static RequestStatus fromString(String value) {
        switch (value) {
            case "WAITING_CONFIRMATION": return WaitingConfirmation;
            case "ON_PREPARATION": return  OnPreparation;
            case "ON_THE_WAY": return OnTheWay;
            case "DELIVERED": return Delivered;
            case "CANCELLED_BY_USER": return  CanceledByUser;
            case "CANCELLED_BY_COMMERCE": return CanceledByCommerce;
            default: return Unknown;
        }
    }
}