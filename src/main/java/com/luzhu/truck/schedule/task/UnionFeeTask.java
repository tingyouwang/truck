package com.luzhu.truck.schedule.task;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.schedule.service.ManageFeeService;
import com.luzhu.truck.schedule.service.UnionFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class UnionFeeTask {
    @Autowired
    private CarFeeDao carFeeDao;
    @Autowired
    private UnionFeeService unionFeeService;
    @Value("${env.time.offset}")
    private String timeOffset;

    @Scheduled(cron = "0 5 0 1 * ?", zone = "Asia/Taipei")
    public void generateMonthBill() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = now.format(formatter);

        List<CarFee> usingCarFee = carFeeDao.getUsingCarFeeForUnionFee();
        log.info(String.format("[UnionFeeTask] generateMonthBill param:yearMonth:%s, now:%s, 產出公會費的車號:%s", yearMonth, now,
                usingCarFee.stream().map(CarFee::getCarLicenseNum).collect(Collectors.toList())));
        LocalDateTime start = LocalDateTime.now();
        try {
            int i = unionFeeService.monthlyInsertFee(yearMonth, now, usingCarFee);
            LocalDateTime end = LocalDateTime.now();
            log.info("[UnionFeeTask] 花費時間:" + Duration.between(start, end).getSeconds() + "公會費筆數:" + i);
        } catch (Exception e) {
            log.error("[UnionFeeTask] generateMonthBill 錯誤:" + e.getMessage());
        }

    }
}
