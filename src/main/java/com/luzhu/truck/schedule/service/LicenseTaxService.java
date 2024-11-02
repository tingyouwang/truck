package com.luzhu.truck.schedule.service;

import com.luzhu.truck.dao.licensetax.LicenseTaxDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.fuel.FuelTax;
import com.luzhu.truck.entity.licensetax.LicenseTax;
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
public class LicenseTaxService {
    @Autowired
    private LicenseTaxDao licenseTaxDao;
    @Transactional
    public int halfYearInsertFee(String yearMonth, LocalDateTime now, List<CarFee> usingCarFee) {
        long utcEpochSecond = DateTimeUtil.toUtcEpochSecond(now);

        List<LicenseTax> licenseTaxes = usingCarFee.stream().map(dto -> {
            LicenseTax licenseTax = new LicenseTax();
            licenseTax.setAmount(BigDecimal.valueOf(dto.getLicenseTaxFirstHalf()));
            licenseTax.setCarLicenseNum(dto.getCarLicenseNum());
            licenseTax.setExpenseYearMonth(yearMonth);
            licenseTax.setCreateTime(utcEpochSecond);

            return licenseTax;
        }).collect(Collectors.toList());

        return licenseTaxDao.saveAll(licenseTaxes).size();
    }

}
