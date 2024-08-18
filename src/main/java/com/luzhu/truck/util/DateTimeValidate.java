package com.luzhu.truck.util;

import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.ValidateExceptionEnum;
import lombok.extern.slf4j.Slf4j;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateTimeValidate {
    public static boolean checkYearMonth(String yearMonth) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM");
        try {
            YearMonth.parse(yearMonth, pattern);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ValidateExceptionEnum.PARAM_ERROR);
        }
        return true;
    }
}
