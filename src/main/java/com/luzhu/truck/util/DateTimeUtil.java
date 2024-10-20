package com.luzhu.truck.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
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

        String result = tryParseFullDate(date, minguoFormatter);

        if (result.isEmpty()) {
            result = tryParseYearMonth(date, minguoFormatter);
        }

        return result;
    }
    private static String tryParseYearMonth(String date, DateTimeFormatter inputFormatter ) {
        String forFormatDate = date + "-01";
        try {
            LocalDate localDate = LocalDate.parse(forFormatDate, inputFormatter);
            return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        } catch (DateTimeParseException e) {
            log.error(String.format("input 日期格式錯誤:%s, 自行拼接日期01:%s", date, forFormatDate));
            throw e;
//            return "";
        }
    }
    private static String tryParseFullDate(String date, DateTimeFormatter inputFormatter) {
        try {
            LocalDate localDate = LocalDate.parse(date, inputFormatter);
            return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return "";
        }
    }
}
