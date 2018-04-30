package com.fiuba.gaff.comohoy.model;

public class OpeningTime {
    private Day mDay;
    private TimeInterval mOpeningTimes;

    public OpeningTime() {
        mDay = Day.Monday;
    }

    public Day getDay() {
        return mDay;
    }

    public void setDay(Day day) {
        mDay = day;
    }

    public TimeInterval getOpeningTimes() {
        return mOpeningTimes;
    }

    public void setOpeningTimes(TimeInterval openingTimes) {
        mOpeningTimes = openingTimes;
    }
}
