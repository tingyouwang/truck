package com.luzhu.truck.schedule.task;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.schedule.service.HealthFeeService;
import com.luzhu.truck.service.laborinsurance.LaborInsuranceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class HealthTask {
    @Autowired
    private HealthFeeService healthFeeService;
    @Autowired
    private LaborInsuranceService laborInsuranceService;
    @Autowired
    private CarFeeDao carFeeDao;

    @Scheduled(cron = "0 1 0 1 * ?")
//    @Scheduled(cron = "0/10 * * * * *")
    public void generateHealthFee() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(0));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = now.format(formatter);

        List<CarFee> usingCarFee = carFeeDao.getUsingCarFee();

        log.info(String.format("[排程HealthTask]每月月初產出勞健保費用開始: param:yearMonth:%s, now:%s, 產出勞健保的車號:%s", yearMonth, now, usingCarFee));
        LocalDateTime start = LocalDateTime.now();
        try {
            healthFeeService.monthlyInsertFee(yearMonth, now, usingCarFee);
            laborInsuranceService.monthlyInsertFee(yearMonth, now, usingCarFee);
        } catch (Exception e) {
            log.error("每月月初產出勞健保費用錯誤:" + e.getMessage());
        }
        LocalDateTime end = LocalDateTime.now();
        log.info("[排程HealthTask]每月月初產出勞健保費用結束, 花費時間:" + Duration.between(start, end).getSeconds());

    }
}
