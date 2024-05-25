package com.luzhu.truck.schedule.task;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.schedule.service.ManageFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
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
        manageFeeService.monthlyInsertFee(yearMonth, now, usingCarFee);

    }
}
