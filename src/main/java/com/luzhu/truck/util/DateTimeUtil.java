package com.luzhu.truck.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static LocalDate getMonthFirst(String billDate) {
        return YearMonth.parse(billDate, DateTimeFormatter.ofPattern("yyyy-MM")).atDay(1);
    }

    public static LocalDate getMonthLastDate(String billDate) {
        return YearMonth.parse(billDate, DateTimeFormatter.ofPattern("yyyy-MM")).atEndOfMonth();
    }

    public static LocalDate transferWestDate(String date) {
        DateTimeFormatter minguoFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd")
                .withChronology(java.time.chrono.MinguoChronology.INSTANCE);

        return LocalDate.parse(date, minguoFormatter);
    }

    public static String transferWestDateStr(String date) {
        DateTimeFormatter minguoFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd")
                .withChronology(java.time.chrono.MinguoChronology.INSTANCE);

        return LocalDate.parse(date, minguoFormatter).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
