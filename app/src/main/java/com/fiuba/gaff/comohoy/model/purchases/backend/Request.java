package com.fiuba.gaff.comohoy.model.purchases.backend;

import com.fiuba.gaff.comohoy.model.Extra;
import com.fiuba.gaff.comohoy.model.purchases.RequestStatus;

import java.util.Date;
import java.util.List;

public class Request {

    private Long id;
    private int mCommerceId;
    private List<SingleRequest> mSingleRequests;
    private RequestStatus mStatus;
    private boolean isQualified;
    private Date mInitDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCommerceId() {
        return mCommerceId;
    }

    public void setCommerceId(int commerceId) {
        mCommerceId = commerceId;
    }

    public List<SingleRequest> getSingleRequests() {
        return mSingleRequests;
    }

    public boolean isQualified() {
        return isQualified;
    }

    public void setQualified(boolean qualified) {
        isQualified = qualified;
    }

    public void setSingleRequests(List<SingleRequest> singleRequests) {
        mSingleRequests = singleRequests;
    }

    public RequestStatus getStatus() {
        return mStatus;
    }

    public void setStatus(RequestStatus status) {
        mStatus = status;
    }

    public Date getInitDate() {
        return mInitDate;
    }

    public void setInitDate(Date initDate) {
        mInitDate = initDate;
    }

    public double getPrice() {
        double price = 0;
        for (SingleRequest singleRequest : mSingleRequests) {
            double extrasPrice = 0;
            for (Extra extra : singleRequest.getPlate().getExtras()) {
                extrasPrice += extra.getPrice();
            }
            double platePrice = singleRequest.getPlate().getPrice() + extrasPrice;
            price = platePrice * singleRequest.getQuantity();
        }
        return price;
    }
}