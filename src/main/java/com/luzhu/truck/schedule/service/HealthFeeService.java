package com.luzhu.truck.schedule.service;

import com.luzhu.truck.dao.Healthfee.HealthFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.healthfee.HealthFee;
import com.luzhu.truck.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthFeeService {
    @Autowired
    private HealthFeeDao healthFeeDao;
    public int monthlyInsertFee(String yearMonth, LocalDateTime now, List<CarFee> usingCarFee) {
        long utcEpochSecond = DateTimeUtil.toUtcEpochSecond(now);

        List<HealthFee> healthFees = usingCarFee.stream()
                .map(dto -> {
                    HealthFee healthFee = new HealthFee();
                    healthFee.setAmount(BigDecimal.valueOf(dto.getHealthyFee()));
                    healthFee.setCarLicenseNum(dto.getCarLicenseNum());
                    healthFee.setExpenseYearMonth(yearMonth);
                    healthFee.setCreateTime(utcEpochSecond);
                    return healthFee;
                })
                .collect(Collectors.toList());

        //每月健保
        return healthFeeDao.saveAll(healthFees).size();
    }

}
