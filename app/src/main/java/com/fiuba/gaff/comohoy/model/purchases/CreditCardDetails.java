package com.fiuba.gaff.comohoy.model.purchases;

import java.util.Date;

public class CreditCardDetails {
    private String mOwnerName = "";
    private CreditCardType mCardType;
    private String mCardNumber = "";
    private String mSecurityNumber = "";
    private Date mExpireDate;

    public CreditCardDetails(CreditCardType type) {
        mCardType = type;
        mExpireDate = new Date();
    }

    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String ownerName) {
        mOwnerName = ownerName;
    }

    public CreditCardType getCardType() {
        return mCardType;
    }

    public void setCardType(CreditCardType cardType) {
        mCardType = cardType;
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

    public Date getExpireDate() {
        return mExpireDate;
    }

    public void setExpireDate(Date expireDate) {
        mExpireDate = expireDate;
    }
}
