package com.luzhu.truck.service.loanfeesetting;

import com.luzhu.truck.dao.loanfeesetting.LoanFeeSettingDao;
import com.luzhu.truck.dto.loanfeesetting.QueryAllByCarLicenseNumParam;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import com.luzhu.truck.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class LoanFeeSettingService {
    @Autowired
    private LoanFeeSettingDao loanFeeSettingDao;

    public PageResult<LoanFeeSetting> getInsuranceCompany(QueryAllByCarLicenseNumParam param) {
        Page<LoanFeeSetting> allByCarLicenseNum = loanFeeSettingDao.getAllByCarLicenseNum(param.getCarLicenseNum(), param.getPageable());
        return new PageResult<>(allByCarLicenseNum);
    }
}
