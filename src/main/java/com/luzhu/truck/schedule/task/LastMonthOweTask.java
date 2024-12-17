package com.luzhu.truck.schedule.task;

import com.luzhu.truck.schedule.service.LastMonthOweService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class LastMonthOweTask {
    @Autowired
    private LastMonthOweService lastMonthOweService;
    @Value("${env.time.offset}")
    private String timeOffset;
    @Scheduled(cron = "0 30 0 1 * ?", zone = "Asia/Taipei")
    public void generateLastMonthOwe() throws ExecutionException {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = now.minusMonths(1).format(formatter);
        log.info(String.format("start task generateLastMonthOwe : startTime: %s", now));
        lastMonthOweService.addLastMonthOwe(yearMonth, now);
        log.info(String.format("end task generateLastMonthOwe : endTime: %s", LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)))));

    }
}
