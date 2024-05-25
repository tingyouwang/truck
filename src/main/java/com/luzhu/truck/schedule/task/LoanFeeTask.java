package com.luzhu.truck.schedule.task;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.loanfeesetting.LoanFeeSettingDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import com.luzhu.truck.schedule.service.LoanFeeService;
import com.luzhu.truck.schedule.service.UnionFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LoanFeeTask {
    @Autowired
    private LoanFeeSettingDao loanFeeSettingDao;
    @Autowired
    private LoanFeeService loanFeeService;

    @Scheduled(cron = "0 6 0 1 * ?")
    public void generateMonthBill() {
        LocalDate now = LocalDate.now(ZoneOffset.ofHours(0));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = now.format(formatter);

        List<LoanFeeSetting> usingLoanFeeSetting = loanFeeSettingDao.getUsingLoanFeeSetting(now);
        log.info(String.format("[LoanFeeTask] generateMonthBill param:yearMonth:%s, now:%s, 產出公會費的車號:%s", yearMonth, now,
                usingLoanFeeSetting.stream().map(LoanFeeSetting::getCarLicenseNum).collect(Collectors.toList())));
        LocalDateTime start = LocalDateTime.now();
        try {
            int insertCount = loanFeeService.monthlyInsertFee(yearMonth, start, usingLoanFeeSetting);
            LocalDateTime end = LocalDateTime.now();
            log.info("[LoanFeeTask] 花費時間:" + Duration.between(start, end).getSeconds() + "成功插入筆數:" + insertCount);
        } catch (Exception e) {
            log.error("[LoanFeeTask] generateMonthBill 錯誤:" + e.getMessage());
        }

    }
}
