package com.example.backend4rate.models.enums;

public enum Months {
    JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4), MAY(5), JUNE(6), JULY(7),
    AUGUST(8), SEPTEMBER(9), OCTOBER(10), NOVEMBER(11), DECEMBER(12);

    private final int brojMeseca;

    Months(int brojMeseca) {
        this.brojMeseca = brojMeseca;
    }

    public int getNumberOfMonth() {
        return brojMeseca;
    }

    public static Months fromNumber(int monthNumber) {
        for (Months month : Months.values()) {
            if (month.getNumberOfMonth() == monthNumber) {
                return month;
            }
        }
        throw new IllegalArgumentException("Invalid month number: " + monthNumber);
    }
}
