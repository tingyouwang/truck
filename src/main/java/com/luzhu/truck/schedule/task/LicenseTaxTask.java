package com.luzhu.truck.schedule.task;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.schedule.service.LicenseTaxService;
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
public class LicenseTaxTask {
    @Autowired
    private LicenseTaxService licenseTaxService;
    @Autowired
    private CarFeeDao carFeeDao;
    @Scheduled(cron = "0 1 0 1 3,9 ?")
    public void generateFuelTax() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(0));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = now.format(formatter);

        List<CarFee> usingCarFee = carFeeDao.getUsingCarFee();

        log.info(String.format("[排程LicenseTaxTask]每半年建立牌照稅開始: param:yearMonth:%s, now:%s, 產出牌照稅的車號:%s", yearMonth, now, usingCarFee.stream().map(CarFee::getCarLicenseNum).collect(Collectors.toList())));
        LocalDateTime start = LocalDateTime.now();
        try {
            int i = licenseTaxService.halfYearInsertFee(yearMonth, now, usingCarFee);
            LocalDateTime end = LocalDateTime.now();
            log.info("[排程LicenseTaxTask]每半年建立牌照稅結束, 花費時間:" + Duration.between(start, end).getSeconds()
                    + "牌照稅筆數:" + i);
        } catch (Exception e) {
            log.error("每季產出牌照稅錯誤:" + e.getMessage());
        }

    }


}
