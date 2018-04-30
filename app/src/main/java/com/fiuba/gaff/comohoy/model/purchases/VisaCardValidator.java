package com.fiuba.gaff.comohoy.model.purchases;

public class VisaCardValidator implements CreditCardValidator {
    @Override
    public boolean validateNumber(String number) {
        return true;
    }

    @Override
    public boolean validateSecurityNumber(String number) {
        return true;
    }
}
