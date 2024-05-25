package com.luzhu.truck.schedule.service;

import com.luzhu.truck.dao.Healthfee.HealthFeeDao;
import com.luzhu.truck.dao.car.CarDao;
import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.healthfee.HealthFee;
import com.luzhu.truck.entity.laborInsurance.LaborInsurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HealthFeeService {
    @Autowired
    private HealthFeeDao healthFeeDao;
    public void monthlyInsertFee(String yearMonth, LocalDateTime now, List<CarFee> usingCarFee) {
        List<HealthFee> healthFees = new ArrayList<>();

        usingCarFee.stream().peek(dto -> {
            //健保
            HealthFee healthFee = new HealthFee();
            healthFee.setAmount(BigDecimal.valueOf(dto.getHealthyFee()));
            healthFee.setCarLicenseNum(dto.getCarLicenseNum());
            healthFee.setExpenseYearMonth(yearMonth);
            healthFee.setCreateTime(String.valueOf(now.toEpochSecond(ZoneOffset.UTC)));

            healthFees.add(healthFee);
        }).collect(Collectors.toList());

        //每月健保
        healthFeeDao.saveAll(healthFees);
    }

}
