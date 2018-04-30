package com.fiuba.gaff.comohoy.model;

public enum Day {
    Monday("Lunes"),
    Tuesday("Martes"),
    Wednesday("Miercoles"),
    Thursday("Jueves"),
    Friday("Viernes"),
    Saturday("Sabado"),
    Sunday("Domingo");

    private final String mDayText;

    Day(String day) {
        mDayText = day;
    }

    public static Day fromString(String dayString) {
        dayString = dayString.toLowerCase();
        switch (dayString) {
            case "lunes": return Day.Monday;
            case "martes": return Day.Tuesday;
            case "miercoles": return Day.Wednesday;
            case "jueves": return Day.Thursday;
            case "viernes": return Day.Friday;
            case "sabado": return Day.Saturday;
            case "domingo": return Day.Sunday;
            default: return Day.Monday;
        }
    }

    @Override
    public String toString() {
        return mDayText;
    }
}
