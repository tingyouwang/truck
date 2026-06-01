package com.luzhu.truck.util;

import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.ValidateExceptionEnum;
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

    /**
     * 民國轉西元
     * @param date 民國
     * @return
     */
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

    /**
     * 西元轉民國
     * @param date 2024-12-01 或 2024-12
     * @return
     */
    public static String tryToMinguoDateStr(String date) {
        DateTimeFormatter westFormatter = DateTimeFormatter.ISO_DATE;
        if (date.split("-").length == 2) {
            return tryParseMinguoYearMonth(date, westFormatter);
        } else {
             return tryParseFullMinguoDate(date, westFormatter);
        }
    }

    private static String tryParseFullMinguoDate(String date, DateTimeFormatter inputFormatter) {
        DateTimeFormatter minguoFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd")
                .withChronology(java.time.chrono.MinguoChronology.INSTANCE);
        try {
            LocalDate localDate = LocalDate.parse(date, inputFormatter);
            return localDate.format(minguoFormatter);
        } catch (DateTimeParseException e) {
            return "";
        }
    }

    private static String tryParseMinguoYearMonth(String date, DateTimeFormatter inputFormatter ) {
        DateTimeFormatter minguoFormatter = DateTimeFormatter.ofPattern("yyy-MM")
                .withChronology(java.time.chrono.MinguoChronology.INSTANCE);
        String forFormatDate = date + "-01";
        try {
            LocalDate localDate = LocalDate.parse(forFormatDate, inputFormatter);
            return localDate.format(minguoFormatter);
        } catch (DateTimeParseException e) {
            log.error(String.format("input 日期格式錯誤:%s, 自行拼接日期01:%s", date, forFormatDate));
            throw e;
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

    /**
     * 轉成年月
     * @param fullDate 2024-12-01
     * @return
     */
    public static String fullDateToYearMonth(String fullDate) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM");
        return LocalDate.parse(fullDate, DateTimeFormatter.ISO_DATE).format(pattern);
    }

    /**
     * 帳務日期字串（西元 yyyy-MM-dd 或民國 yyy-MM-dd）轉為帳單月份 yyyy-MM。
     */
    public static String toBillYearMonth(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            LocalDate d = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return d.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        } catch (DateTimeParseException ignored) {
        }
        String west = transferWestDateStr(dateStr);
        if (west == null || west.isEmpty()) {
            return null;
        }
        if (west.length() >= 10) {
            LocalDate d = LocalDate.parse(west.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);
            return d.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }
        LocalDate d = LocalDate.parse(west + "-01", DateTimeFormatter.ISO_LOCAL_DATE);
        return d.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

}
