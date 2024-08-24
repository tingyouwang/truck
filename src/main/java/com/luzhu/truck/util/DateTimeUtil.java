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
}
