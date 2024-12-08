package com.luzhu.truck.service.insurancefeesetting;

import com.luzhu.truck.dao.insurancefee.InsuranceFeeDao;
import com.luzhu.truck.dao.insurnacefeesetting.InsuranceFeeSettingDao;
import com.luzhu.truck.dto.car.LicenseNumPageParam;
import com.luzhu.truck.dto.insurancefeesetting.*;
import com.luzhu.truck.entity.insurancefee.InsuranceFee;
import com.luzhu.truck.entity.insurancefeesetting.InsuranceFeeSetting;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            int insertCount = insuranceFeeSettingDao.insertInsuranceFeeSetting(param.getCarLicenseNum(), param.getInsuranceCom(), param.getStartDate(),
                    param.getEndDate(), param.getAmount(), param.getInsuranceType(), param.getInsuranceNum(), param.getInsuranceCardNum(),
                    l, l);
            Validator.isFalseThrow(1 == insertCount,
                    new AppException(SystemExceptionEnum.UPDATE_ERROR));
        } catch (DataIntegrityViolationException e) {
            log.error("Primary key conflict: " + e.getMessage());
            throw new AppException(SystemExceptionEnum.PRIMARY_KEY_CONFLICT);
        }

//        InsuranceFee insuranceFee = new InsuranceFee();
//        insuranceFee.setCarLicenseNum(param.getCarLicenseNum());
//        insuranceFee.setAmount(new BigDecimal(param.getAmount()));
//        insuranceFee.setExpenseYearMonth(yearMonth);
//        insuranceFee.setCreateTime(l);
//        insuranceFee.setInsuranceCardNum(param.getInsuranceCardNum());
//        insuranceFee.setStatus("ENABLE");
//
//        //todo 如何檢查插入成功
//        insuranceFeeDao.save(insuranceFee);
    }

    @Transactional
    public void updateInsuranceFeeSetting(UpdateInsuranceFeeSettingParam param) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        long l = now.toEpochSecond(ZoneOffset.UTC);

        LocalDate localDate = now.toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        int updateCount = insuranceFeeSettingDao.updateInsuranceFeeSetting(param.getCarLicenseNum(), param.getInsuranceCom(), param.getStartDate(), param.getEndDate(),
                param.getPayUsDate(), param.getAmount(), param.getInsuranceType(), param.getInsuranceNum(), param.getQuitDate(), l,
                "ADMIN", "ENABLE", param.getInsuranceCardNum());
        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        //連同帳單一起修改
//        int i = insuranceFeeDao.updateInsuranceFee(param.getCarLicenseNum(), param.getInsuranceNum(),
//                param.getAmount(), param.getInsuranceCardNum(), yearMonth);

//        Validator.isFalseThrow(1 == i,
//                new AppException(SystemExceptionEnum.UPDATE_ERROR));

    }

    @Transactional
    public void updateInsuranceFeeSettingStatus(UpdateInsuranceFeeSettingStatusParam param) {
        long l = LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC);

        int updateCount = insuranceFeeSettingDao.updateInsuranceFeeSettingStatus(param.getCarLicenseNum(), "DISABLE", param.getInsuranceCardNum(), l);
        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateGenerateBillStatus(List<InsuranceFeeSetting> usingInsuranceFeeSetting) {
        long l = LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC);

        usingInsuranceFeeSetting = usingInsuranceFeeSetting.stream().peek(setting -> {
            setting.setGenerateBill("Y");
            setting.setUpdateTime(l);
        }).toList();
        insuranceFeeSettingDao.saveAll(usingInsuranceFeeSetting);
    }

    public InsuranceFeeSetting getSingleInsuranceSetting(GetSingleInsuranceSettingParam param) {
        return insuranceFeeSettingDao.getInsuranceFeeSettingByPk(param.getCarLicenseNum(), param.getInsuranceCardNum());
    }

    public PageResult<InsuranceFeeSetting> getInsuranceFeeSetting(LicenseNumPageParam param) {
        Page<InsuranceFeeSetting> allInsuranceFeeSetting = insuranceFeeSettingDao.getInsuranceFeeSettingByLicenseNum(param.getCarLicenseNum(), param.getPageable());

//        List<InsuranceFeeSettingDto> collect = allInsuranceFeeSetting.stream().map(dto -> {
//            InsuranceFeeSettingDto d = new InsuranceFeeSettingDto();
//            d.setCarLicenseNum(dto.getCarLicenseNum());
//            d.setInsuranceCardNum(dto.getInsuranceCardNum());
//            d.setStartDate(DateTimeUtil.parseToMinguoDate(dto.getStartDate()));
//            d.setEndDate(DateTimeUtil.parseToMinguoDate(dto.getEndDate()));
//            if (null != dto.getPayUsDate()) {
//                d.setPayUsDate(DateTimeUtil.parseToMinguoDate(dto.getPayUsDate()));
//            }
//
//            if (null != dto.getQuitDate()) {
//                d.setQuitDate(DateTimeUtil.parseToMinguoDate(dto.getQuitDate()));
//            }
//
//            d.setCreateTime(dto.getCreateTime());
//            d.setUpdateTime(dto.getUpdateTime());
//            d.setUpdateBy(dto.getUpdateBy());
//            d.setStatus(dto.getStatus());
//            d.setInsuranceType(dto.getInsuranceType());
//            d.setInsuranceNum(dto.getInsuranceNum());
//            return d;
//        }).collect(Collectors.toList());
//
//        Page<InsuranceFeeSettingDto> insuranceFeeSettingDtos = new PageImpl<>(collect, allInsuranceFeeSetting.getPageable(), allInsuranceFeeSetting.getTotalElements());

//        return new PageImpl<>(dtos, insuranceFeeSettingPage.getPageable(), insuranceFeeSettingPage.getTotalElements());

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
            insuranceFee.setInsuranceCardNum(dto.getInsuranceCardNum());
            insuranceFee.setStatus("ENABLE");

            return insuranceFee;
        }).collect(Collectors.toList());

        return insuranceFeeDao.saveAll(insuranceFees).size();
    }
}
