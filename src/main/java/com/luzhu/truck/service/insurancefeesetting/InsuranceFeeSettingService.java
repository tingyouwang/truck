package com.luzhu.truck.service.insurancefeesetting;

import com.luzhu.truck.dao.insurancefee.InsuranceFeeDao;
import com.luzhu.truck.dao.insurnacefeesetting.InsuranceFeeSettingDao;
import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.car.LicenseNumPageParam;
import com.luzhu.truck.dto.insurancefeesetting.AddInsuranceFeeSettingParam;
import com.luzhu.truck.dto.insurancefeesetting.DeleteInsuranceSettingParam;
import com.luzhu.truck.dto.insurancefeesetting.UpdateInsuranceFeeSettingParam;
import com.luzhu.truck.entity.insurancefee.InsuranceFee;
import com.luzhu.truck.entity.insurancefeesetting.InsuranceFeeSetting;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
        insuranceFee.setAmount(new BigDecimal(param.getAmount()));
        insuranceFee.setExpenseYearMonth(yearMonth);
        insuranceFee.setCreateTime(l);
        insuranceFee.setInsuranceCardNum(param.getInsuranceCardNum());
        insuranceFee.setStatus("ENABLE");

        //todo 如何檢查插入成功
        insuranceFeeDao.save(insuranceFee);

    }

    @Transactional
    public void updateInsuranceFeeSetting(UpdateInsuranceFeeSettingParam param) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        long l = now.toEpochSecond(ZoneOffset.UTC);

        LocalDate localDate = now.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = localDate.format(formatter);

        int updateCount = insuranceFeeSettingDao.updateInsuranceFeeSetting(param.getCarLicenseNum(), param.getInsuranceCom(), param.getStartDate(), param.getEndDate(),
                param.getPayUsDate(), param.getAmount(), param.getInsuranceType(), param.getInsuranceNum(), param.getQuitDate(), l,
                "ADMIN", "ENABLE", param.getOriginalInsuranceCardNum(), param.getInsuranceCardNum());
        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        //連同帳單一起修改
        int i = insuranceFeeDao.updateInsuranceFee(param.getCarLicenseNum(), param.getOriginalInsuranceCardNum(),
                param.getAmount(), param.getInsuranceCardNum(), yearMonth);

        Validator.isFalseThrow(1 == i,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

    }

    public PageResult<InsuranceFeeSetting> getInsuranceFeeSetting(LicenseNumPageParam param) {
        Page<InsuranceFeeSetting> allInsuranceFeeSetting = insuranceFeeSettingDao.getInsuranceFeeSettingByLicenseNum(param.getCarLicenseNum(), param.getPageable());
        return new PageResult<>(allInsuranceFeeSetting);

    }

    @Transactional
    public void deleteInsuranceFeeSetting(DeleteInsuranceSettingParam param) {
        int updateCount = insuranceFeeSettingDao.updateInsuranceFeeSettingById(param.getCarLicenseNum(), param.getInsuranceCardNum());
        int updateFeeCount = insuranceFeeDao.disableInsuranceFee(param.getCarLicenseNum(), param.getInsuranceCardNum());
        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.DELETE_ERROR));
        Validator.isFalseThrow(1 == updateFeeCount,
                new AppException(SystemExceptionEnum.DELETE_ERROR));
    }

    @Transactional
    public int monthlyInsertFee(String yearMonth, LocalDate now, List<InsuranceFeeSetting> usingInsuranceFeeSetting) {
        long utcEpochSecond = DateTimeUtil.toUtcEpochSecond(now);

        List<InsuranceFee> insuranceFees = usingInsuranceFeeSetting.stream().map(dto -> {
            InsuranceFee insuranceFee = new InsuranceFee();
            insuranceFee.setAmount(BigDecimal.valueOf(dto.getAmount()));
            insuranceFee.setCarLicenseNum(dto.getCarLicenseNum());
            insuranceFee.setExpenseYearMonth(yearMonth);
            insuranceFee.setCreateTime(utcEpochSecond);

            return insuranceFee;
        }).collect(Collectors.toList());

        return insuranceFeeDao.saveAll(insuranceFees).size();
    }
}
