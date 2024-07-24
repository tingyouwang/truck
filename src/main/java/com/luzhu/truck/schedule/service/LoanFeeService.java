package com.luzhu.truck.schedule.service;

import com.luzhu.truck.dao.loanfee.LoanFeeDao;
import com.luzhu.truck.entity.loanfee.LoanFee;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanFeeService {
    @Autowired
    private LoanFeeDao loanFeeDao;

    public int monthlyInsertFee(String yearMonth, LocalDateTime now, List<LoanFeeSetting> loanFeeSettings) {
        List<LoanFee> loanFees = new ArrayList<>();

        loanFeeSettings.stream().peek(dto -> {
            LoanFee loanFee = new LoanFee();
            loanFee.setAmount(BigDecimal.valueOf(dto.getMonthPayAmount()));
            loanFee.setCarLicenseNum(dto.getCarLicenseNum());
            loanFee.setExpenseYearMonth(yearMonth);
            loanFee.setCreateTime(String.valueOf(now.toEpochSecond(ZoneOffset.UTC)));

            loanFees.add(loanFee);
        }).collect(Collectors.toList());

        return loanFeeDao.saveAll(loanFees).size();
    }



}
