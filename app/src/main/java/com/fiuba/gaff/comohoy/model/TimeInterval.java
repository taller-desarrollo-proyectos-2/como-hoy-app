package com.fiuba.gaff.comohoy.model;

import java.sql.Timestamp;
import java.util.Date;

public class TimeInterval {
    private Date mFrom;
    private Date mTo;

    public TimeInterval(Timestamp from, Timestamp to) {
        mFrom = new Date(from.getTime());
        mTo = new Date(to.getTime());;
    }

    public Date getFromTime() {
        return mFrom;
    }

    public Date getToTime() {
        return mTo;
    }
}
