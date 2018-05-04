package com.fiuba.gaff.comohoy.model;

import java.util.ArrayList;
import java.util.List;

public class OpeningTime {
    private Day mDay;
    private List<TimeInterval> mOpeningTimes;

    public OpeningTime() {
        mDay = Day.Monday;
        mOpeningTimes = new ArrayList<>();
    }

    public Day getDay() {
        return mDay;
    }

    public void setDay(Day day) {
        mDay = day;
    }

    public List<TimeInterval> getOpeningTimes() {
        return mOpeningTimes;
    }

    public void setOpeningTimes(List<TimeInterval> openingTimes) {
        mOpeningTimes = openingTimes;
    }

    public void addTimeInterval(TimeInterval timeInterval) {
        mOpeningTimes.add(timeInterval);
    }
}
