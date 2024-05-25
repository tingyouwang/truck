package com.luzhu.truck.schedule.service;

import com.luzhu.truck.dao.managefee.ManageFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.managefee.ManageFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManageFeeService {
    @Autowired
    private ManageFeeDao manageFeeDao;

    public void monthlyInsertFee(String yearMonth, LocalDateTime now, List<CarFee> usingCarFee) {
        List<ManageFee> manageFees = new ArrayList<>();

        usingCarFee.stream().peek(dto -> {
            ManageFee manageFee = new ManageFee();
            manageFee.setAmount(BigDecimal.valueOf(dto.getHealthyFee()));
            manageFee.setCarLicenseNum(dto.getCarLicenseNum());
            manageFee.setExpenseYearMonth(yearMonth);
            manageFee.setCreateTime(String.valueOf(now.toEpochSecond(ZoneOffset.UTC)));

            manageFees.add(manageFee);
        }).collect(Collectors.toList());

        manageFeeDao.saveAll(manageFees);
    }
}
