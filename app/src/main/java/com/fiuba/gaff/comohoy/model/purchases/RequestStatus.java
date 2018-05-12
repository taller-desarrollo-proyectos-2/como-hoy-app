package com.fiuba.gaff.comohoy.model.purchases;

public enum RequestStatus{
    ON_PREPARATION("En preparación"),
    ON_THE_WAY("En camino"),
    DELIVERED("Entregado"),
    CANCELED_BY_USER("Cancelado"),
    CANCELED_BY_COMMERCE("Rechazado");

    private final String mText;

    RequestStatus(String text) {
        mText = text;
    }

    public String toString() {
        return mText;
    }
}