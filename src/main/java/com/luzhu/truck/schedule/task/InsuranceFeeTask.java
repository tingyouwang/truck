package com.luzhu.truck.schedule.task;

import com.luzhu.truck.dao.insurnacefeesetting.InsuranceFeeSettingDao;
import com.luzhu.truck.entity.insurancefeesetting.InsuranceFeeSetting;
import com.luzhu.truck.schedule.service.LoanFeeService;
import com.luzhu.truck.service.insurancefeesetting.InsuranceFeeSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InsuranceFeeTask {
    @Autowired
    private InsuranceFeeSettingDao insuranceFeeSettingDao;
    @Autowired
    private InsuranceFeeSettingService insuranceFeeSettingService;
    @Value("${env.time.offset}")
    private String timeOffset;

    @Scheduled(cron = "0 7 0 1 * ?", zone = "Asia/Taipei")
    public void generateMonthBill() {
        LocalDate now = LocalDate.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = now.format(formatter);

        List<InsuranceFeeSetting> usingInsuranceFeeSetting = insuranceFeeSettingDao.getUsingInsuranceFeeSetting(now);

        log.info(String.format("[InsuranceFeeTask] generateMonthBill param:yearMonth:%s, now:%s, 產出保險費的車號:%s", yearMonth, now,
                usingInsuranceFeeSetting.stream().map(InsuranceFeeSetting::getCarLicenseNum).collect(Collectors.toList())));

        LocalDateTime start = LocalDateTime.now();
        try {
            int insertCount = insuranceFeeSettingService.monthlyInsertFee(yearMonth, now, usingInsuranceFeeSetting);

            LocalDateTime end = LocalDateTime.now();
            log.info("[InsuranceFeeTask] 花費時間:" + Duration.between(start, end).getSeconds() + "成功插入筆數:" + insertCount);
        } catch (Exception e) {
            log.error("[InsuranceFeeTask] generateMonthBill 錯誤:" + e.getMessage());
        }

    }
}
