package com.fiuba.gaff.comohoy.model.purchases;

import java.util.Date;

public class CreditCardDetails {
    private String mOwnerName = "";
    private String mCardNumber = "";
    private String mSecurityNumber = "";
    private String mExpireDate = "";

    public CreditCardDetails() {
    }

    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String ownerName) {
        mOwnerName = ownerName;
    }

    public String getCardNumber() {
        return mCardNumber;
    }

    public void setCardNumber(String cardNumber) {
        mCardNumber = cardNumber;
    }

    public String getSecurityNumber() {
        return mSecurityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        mSecurityNumber = securityNumber;
    }

    public String getExpireDate() {
        return mExpireDate;
    }

    public void setExpireDate(String expireDate) {
        mExpireDate = expireDate;
    }
}
