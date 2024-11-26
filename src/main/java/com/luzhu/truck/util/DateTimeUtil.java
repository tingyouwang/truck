package com.luzhu.truck.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DateTimeUtil {
//    @Value("${env.time.offset}")
    private static String timeOffset = "8";
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

    public static List<String> transferWestDateStr(List<?> dates) {
        List<String> result = new ArrayList<>(dates.size());
        for (Object date : dates) {
            result.add(transferWestDateStr(date.toString()));
        }
        return result;
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

    public static String parseToMinguoDate(LocalDate westDate) {
        DateTimeFormatter minguoFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd")
                .withChronology(java.time.chrono.MinguoChronology.INSTANCE);
        return westDate.format(minguoFormatter);
    }

    /**
     * 西元年月轉成民國年月
     * @param yearMonthStr 2024-06
     * @return
     */
    public static String parseToMinguoDateYearMonth(String yearMonthStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        yearMonthStr = yearMonthStr + "-01";

        LocalDate west = LocalDate.parse(yearMonthStr, dateTimeFormatter);

        DateTimeFormatter minguoFormatter = DateTimeFormatter.ofPattern("yyy-MM")
                .withChronology(java.time.chrono.MinguoChronology.INSTANCE);

        return west.format(minguoFormatter);
    }

    public static long toUtcEpochSecond(LocalDate now) {
        LocalDateTime localDateTime = now.atStartOfDay();
        return toUtcEpochSecond(localDateTime);
    }

    public static long toUtcEpochSecond(LocalDateTime now) {
        // 将其转换为 UTC+0 的 ZonedDateTime
        ZonedDateTime utcTime = now.atZone(ZoneOffset.ofHours(Integer.parseInt(timeOffset))).withZoneSameInstant(ZoneOffset.UTC);

        // 转换为 epoch second
        return utcTime.toEpochSecond();
    }

}
