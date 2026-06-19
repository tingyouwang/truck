package com.luzhu.truck.schedule.service;

import com.luzhu.truck.dao.loanfee.LoanFeeDao;
import com.luzhu.truck.entity.loanfee.LoanFee;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import com.luzhu.truck.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanFeeService {
    @Autowired
    private LoanFeeDao loanFeeDao;

    @Transactional
    public int monthlyInsertFee(String yearMonth, LocalDate now, List<LoanFeeSetting> loanFeeSettings) {
        long utcEpochSecond = DateTimeUtil.toUtcEpochSecond(now);

        List<LoanFee> loanFees = loanFeeSettings.stream()
                .filter(setting -> "Y".equals(setting.getIncludeInBill()))
                .map(dto -> {
            LoanFee loanFee = new LoanFee();
            loanFee.setAmount(BigDecimal.valueOf(dto.getMonthPayAmount()));
            loanFee.setCarLicenseNum(dto.getCarLicenseNum());
            loanFee.setExpenseYearMonth(yearMonth);
            loanFee.setCreateTime(utcEpochSecond);

            return loanFee;
        }).collect(Collectors.toList());

        if (loanFees.isEmpty()) {
            return 0;
        }

        return loanFeeDao.saveAll(loanFees).size();
    }



}
