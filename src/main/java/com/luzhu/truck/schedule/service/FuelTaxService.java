package com.luzhu.truck.schedule.service;

import com.luzhu.truck.dao.fueltax.FuelTaxDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.fuel.FuelTax;
import com.luzhu.truck.entity.healthfee.HealthFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuelTaxService {
    @Autowired
    private FuelTaxDao fuelTaxDao;
    public int seasonlyInsertFee(String yearMonth, LocalDateTime now, List<CarFee> usingCarFee) {
        List<FuelTax> fuelTaxes = new ArrayList<>();

        usingCarFee.stream().peek(dto -> {
            FuelTax fuelTax = new FuelTax();
            fuelTax.setAmount(BigDecimal.valueOf(dto.getFuelTaxSpring()));
            fuelTax.setCarLicenseNum(dto.getCarLicenseNum());
            fuelTax.setExpenseYearMonth(yearMonth);
            fuelTax.setCreateTime(String.valueOf(now.toEpochSecond(ZoneOffset.UTC)));

            fuelTaxes.add(fuelTax);
        }).collect(Collectors.toList());

        return fuelTaxDao.saveAll(fuelTaxes).size();
    }

}
