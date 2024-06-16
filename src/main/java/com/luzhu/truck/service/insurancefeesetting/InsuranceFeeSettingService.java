package com.luzhu.truck.service.insurancefeesetting;

import com.luzhu.truck.dao.insurancefee.InsuranceFeeDao;
import com.luzhu.truck.dao.insurnacefeesetting.InsuranceFeeSettingDao;
import com.luzhu.truck.dto.insurancefeesetting.AddInsuranceFeeSettingParam;
import com.luzhu.truck.entity.insurancefee.InsuranceFee;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class InsuranceFeeSettingService {
    @Autowired
    private InsuranceFeeSettingDao insuranceFeeSettingDao;
    @Autowired
    private InsuranceFeeDao insuranceFeeDao;
    @Transactional
    public void addInsuranceFeeSetting(AddInsuranceFeeSettingParam param) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        long l = now.toEpochSecond(ZoneOffset.UTC);

        LocalDate localDate = now.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = localDate.format(formatter);

        int insertCount = insuranceFeeSettingDao.insertInsuranceFeeSetting(param.getCarLicenseNum(), param.getInsuranceCom(), param.getStartDate(),
                param.getEndDate(), param.getAmount(), param.getInsuranceType(), param.getInsuranceNum(), param.getInsuranceCardNum(),
                l, l);
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        InsuranceFee insuranceFee = new InsuranceFee();
        insuranceFee.setCarLicenseNum(param.getCarLicenseNum());
        insuranceFee.setAmount(param.getAmount());
        insuranceFee.setExpenseYearMonth(yearMonth);
        insuranceFee.setCreateTime(l);

        //todo 如何檢查插入成功
        insuranceFeeDao.save(insuranceFee);

    }
}
