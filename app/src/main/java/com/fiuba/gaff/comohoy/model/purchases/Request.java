package com.fiuba.gaff.comohoy.model.purchases;

import com.fiuba.gaff.comohoy.model.Plate;

import java.util.Date;
import java.util.List;

public class Request {

    private Long id;
    private List<Plate> mPlates;
    private RequestStatus mStatus;
    private Date mInitDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Plate> getPlates() {
        return mPlates;
    }

    public void setPlates(List<Plate> plates) {
        mPlates = plates;
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
}