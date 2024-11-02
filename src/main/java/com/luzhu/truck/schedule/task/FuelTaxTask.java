package com.luzhu.truck.schedule.task;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.schedule.service.FuelTaxService;
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
public class FuelTaxTask {
    @Autowired
    private FuelTaxService fuelTaxService;
    @Autowired
    private CarFeeDao carFeeDao;
    @Value("${env.time.offset}")
    private String timeOffset;
    @Scheduled(cron = "0 1 0 1 3,6,9,12 ?", zone = "Asia/Taipei")
    public void generateFuelTax() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = now.format(formatter);

        List<CarFee> usingCarFee = carFeeDao.getUsingCarFee();

        log.info(String.format("[排程FuelTaxTask]每季建立燃料費開始: param:yearMonth:%s, now:%s, 產出燃料稅的車號:%s", yearMonth, now, usingCarFee.stream().map(CarFee::getCarLicenseNum).collect(Collectors.toList())));
        LocalDateTime start = LocalDateTime.now();
        try {
            int i = fuelTaxService.seasonlyInsertFee(yearMonth, now, usingCarFee);
            LocalDateTime end = LocalDateTime.now();
            log.info("[排程FuelTaxTask]每季建立燃料費結束, 花費時間:" + Duration.between(start, end).getSeconds()
                    + "燃料稅筆數:" + i);
        } catch (Exception e) {
            log.error("每季產出燃料稅錯誤:" + e.getMessage());
        }

    }

}
