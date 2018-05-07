package com.fiuba.gaff.comohoy.model.purchases;

public class Address {

    private String mStreetName;
    private String mStreetNumber;
    private String mAdditionalInformation;

    public Address() {
        mStreetName = "";
        mStreetNumber = "";
        mAdditionalInformation = "";
    }

    public String getStreetName() {
        return mStreetName;
    }

    public void setStreetName(String streetName) {
        mStreetName = streetName;
    }

    public String getStreetNumber() {
        return mStreetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        mStreetNumber = streetNumber;
    }

    public String getAdditionalInformation() {
        return mAdditionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        mAdditionalInformation = additionalInformation;
    }
}
