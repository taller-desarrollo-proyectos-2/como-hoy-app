package com.fiuba.gaff.comohoy.model.purchases;

public interface CreditCardValidator {
    boolean validateNumber(String number);
    boolean validateSecurityNumber(String number);
}
