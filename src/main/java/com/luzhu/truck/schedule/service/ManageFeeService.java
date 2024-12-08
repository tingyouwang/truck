package com.luzhu.truck.schedule.service;

import com.luzhu.truck.dao.managefee.ManageFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.managefee.ManageFee;
import com.luzhu.truck.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public int monthlyInsertFee(String yearMonth, LocalDateTime now, List<CarFee> usingCarFee) {
        long utcEpochSecond = DateTimeUtil.toUtcEpochSecond(now);

        List<ManageFee> manageFees = usingCarFee.stream().map(dto -> {
            ManageFee manageFee = new ManageFee();
            manageFee.setAmount(BigDecimal.valueOf(dto.getManageFee()));
            manageFee.setCarLicenseNum(dto.getCarLicenseNum());
            manageFee.setExpenseYearMonth(yearMonth);
            manageFee.setCreateTime(utcEpochSecond);

            return manageFee;
        }).collect(Collectors.toList());

        return manageFeeDao.saveAll(manageFees).size();
    }
}
