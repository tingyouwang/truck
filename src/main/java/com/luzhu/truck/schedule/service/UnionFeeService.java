package com.luzhu.truck.schedule.service;

import com.luzhu.truck.dao.managefee.ManageFeeDao;
import com.luzhu.truck.dao.unionfee.UnionFeeDao;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.managefee.ManageFee;
import com.luzhu.truck.entity.unionfee.UnionFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnionFeeService {
    @Autowired
    private UnionFeeDao unionFeeDao;
    public int monthlyInsertFee(String yearMonth, LocalDateTime now, List<CarFee> usingCarFee) {
        List<UnionFee> unionFees = new ArrayList<>();

        usingCarFee.stream().peek(dto -> {
            UnionFee unionFee = new UnionFee();
            unionFee.setAmount(BigDecimal.valueOf(dto.getHealthyFee()));
            unionFee.setCarLicenseNum(dto.getCarLicenseNum());
            unionFee.setExpenseYearMonth(yearMonth);
            unionFee.setCreateTime(String.valueOf(now.toEpochSecond(ZoneOffset.UTC)));

            unionFees.add(unionFee);
        }).collect(Collectors.toList());

        return unionFeeDao.saveAll(unionFees).size();
    }



}
