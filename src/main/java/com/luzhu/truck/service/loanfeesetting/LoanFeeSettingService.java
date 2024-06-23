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

@Service
public class LoanFeeSettingService {
    @Autowired
    private LoanFeeSettingDao loanFeeSettingDao;

    public PageResult<LoanFeeSetting> getLoanFeeSetting(QueryAllByCarLicenseNumParam param) {
        Page<LoanFeeSetting> allByCarLicenseNum = loanFeeSettingDao.getAllByCarLicenseNum(param.getCarLicenseNum(), param.getPageable());
        return new PageResult<>(allByCarLicenseNum);
    }

    @Transactional
    public void addInsuranceCom(AddLoanFeeSettingParam addLoanFeeSettingParam) {
        int insertCount = loanFeeSettingDao.insertLoanFeeSetting(addLoanFeeSettingParam.getCarLicenseNum(), addLoanFeeSettingParam.getLoanCompany(), addLoanFeeSettingParam.getStartDate(),
                addLoanFeeSettingParam.getEndDate(), addLoanFeeSettingParam.getTotalAmount(), addLoanFeeSettingParam.getMonthPayAmount());
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }
}
