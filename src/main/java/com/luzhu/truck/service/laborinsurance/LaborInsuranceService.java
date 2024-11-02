package com.luzhu.truck.service.laborinsurance;

import com.luzhu.truck.dao.laborinsurance.LaborInsuranceDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.healthfee.HealthFee;
import com.luzhu.truck.entity.laborInsurance.LaborInsurance;
import com.luzhu.truck.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LaborInsuranceService {
    @Autowired
    private LaborInsuranceDao laborInsuranceDao;
    public int monthlyInsertFee(String yearMonth, LocalDateTime now, List<CarFee> usingCarFee) {
        List<LaborInsurance> laborInsurances = new ArrayList<>();

        long utcEpochSecond = DateTimeUtil.toUtcEpochSecond(now);

        usingCarFee.stream().peek(dto -> {
            //勞保
            LaborInsurance laborInsurance = new LaborInsurance();
            laborInsurance.setAmount(BigDecimal.valueOf(dto.getLaborFee()));
            laborInsurance.setCarLicenseNum(dto.getCarLicenseNum());
            laborInsurance.setExpenseYearMonth(yearMonth);
            laborInsurance.setCreateTime(utcEpochSecond);
            laborInsurances.add(laborInsurance);

        }).collect(Collectors.toList());

        //每月勞保
        return laborInsuranceDao.saveAll(laborInsurances).size();

    }
}
