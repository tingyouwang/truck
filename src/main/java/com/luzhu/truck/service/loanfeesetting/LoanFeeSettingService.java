package com.luzhu.truck.service.loanfeesetting;

import com.luzhu.truck.dao.loanfeesetting.LoanFeeSettingDao;
import com.luzhu.truck.dto.insurancecompany.AddInsuranceComParam;
import com.luzhu.truck.dto.loanfeesetting.AddLoanFeeSettingParam;
import com.luzhu.truck.dto.loanfeesetting.QueryAllByCarLicenseNumParam;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class LoanFeeSettingService {
    @Autowired
    private LoanFeeSettingDao loanFeeSettingDao;

    public PageResult<LoanFeeSetting> getLoanFeeSetting(QueryAllByCarLicenseNumParam param) {
        Page<LoanFeeSetting> allByCarLicenseNum = loanFeeSettingDao.getAllByCarLicenseNum(param.getCarLicenseNum(), param.getPageable());
        return new PageResult<>(allByCarLicenseNum);
    }

    @Transactional
    public void addLoanFeeSetting(AddLoanFeeSettingParam addLoanFeeSettingParam) {
//        //存入時間時區為UTC
//        OffsetDateTime startDatetime = addLoanFeeSettingParam.getStartDate().atOffset(ZoneOffset.UTC);
//        OffsetDateTime endDatetime = addLoanFeeSettingParam.getEndDate().atOffset(ZoneOffset.UTC);
        LocalDate start = LocalDate.parse(addLoanFeeSettingParam.getStartDate());
        LocalDate end = LocalDate.parse(addLoanFeeSettingParam.getEndDate());

        LoanFeeSetting loanFeeSetting = new LoanFeeSetting();
        loanFeeSetting.setCarLicenseNum(addLoanFeeSettingParam.getCarLicenseNum());
        loanFeeSetting.setLoanCompany(addLoanFeeSettingParam.getLoanCompany());
        loanFeeSetting.setStartDate(start);
//        loanFeeSetting.setStartDatetime(startDatetime.toLocalDateTime());
        loanFeeSetting.setEndDate(end);
//        loanFeeSetting.setEndDatetime(endDatetime.toLocalDateTime());
        loanFeeSetting.setTotalAmount(addLoanFeeSettingParam.getTotalAmount().doubleValue());
        loanFeeSetting.setMonthPayAmount(addLoanFeeSettingParam.getMonthPayAmount().doubleValue());
        loanFeeSetting.setNote(addLoanFeeSettingParam.getNote());

        loanFeeSettingDao.save(loanFeeSetting);
    }

    public static void main(String[] args) {
//        LocalDateTime now = LocalDateTime.now();
        //前端送的沒時區
        String ss = "2024-06-24 01:20:59";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.parse(ss, formatter);

        //先轉成有時區
        ZonedDateTime zonedDateTime = now.atZone(ZoneOffset.ofHours(8));

        //再把時區轉成+0
        ZonedDateTime utc = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);

        LocalDate localDate = utc.toLocalDate();
        LocalDateTime localDateTime = utc.toLocalDateTime();
        LocalTime localTime = utc.toLocalTime();

        String a = "a";
    }
}
