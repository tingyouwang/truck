package com.luzhu.truck.schedule.task;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.schedule.service.ManageFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ManageFeeTask {
    @Autowired
    private CarFeeDao carFeeDao;
    @Autowired
    private ManageFeeService manageFeeService;

    @Scheduled(cron = "0 4 0 1 * ?")
    public void generateMonthBill() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(0));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = now.format(formatter);

        List<CarFee> usingCarFee = carFeeDao.getUsingCarFeeForManageFee();
        log.info(String.format("[ManageFeeTask] generateMonthBill param:yearMonth:%s, now:%s, 產出公會費的車號:%s", yearMonth, now,
                usingCarFee.stream().map(CarFee::getCarLicenseNum).collect(Collectors.toList())));
        LocalDateTime start = LocalDateTime.now();
        try {
            manageFeeService.monthlyInsertFee(yearMonth, now, usingCarFee);
        } catch (Exception e) {
            log.error("[ManageFeeTask] generateMonthBill 錯誤:" + e.getMessage());
        }
        LocalDateTime end = LocalDateTime.now();
        log.info("[ManageFeeTask] 花費時間:" + Duration.between(start, end).getSeconds());

    }
}
