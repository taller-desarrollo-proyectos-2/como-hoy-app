package com.fiuba.gaff.comohoy.model.purchases;

import java.util.ArrayList;
import java.util.List;

public class PlateOrder {
    private Long mOrderId;
    private int mCommerceId;
    private Long mPlateId;
    private List<Long> mExtrasId;
    private int mQuantity;
    private String mClarifications = "";

    public PlateOrder(Long orderId) {
        mOrderId = orderId;
        mExtrasId = new ArrayList<>();
    }

    public int getCommerceId() {
        return mCommerceId;
    }

    public void setCommerceId(int commerceId) {
        mCommerceId = commerceId;
    }

    public Long getPlateId() {
        return mPlateId;
    }

    public void setPlateId(Long plateId) {
        mPlateId = plateId;
    }

    public List<Long> getExtrasId() {
        return mExtrasId;
    }

    public void setExtrasId(List<Long> extrasId) {
        mExtrasId = extrasId;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public String getClarifications() {
        return mClarifications;
    }

    public void setClarifications(String clarifications) {
        mClarifications = clarifications;
    }
}
