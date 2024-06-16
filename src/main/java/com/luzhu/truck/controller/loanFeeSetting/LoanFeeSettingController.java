package com.luzhu.truck.controller.loanFeeSetting;

import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.loanfeesetting.QueryAllByCarLicenseNumParam;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.loanfeesetting.LoanFeeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loanFeeSetting")
public class LoanFeeSettingController {
    @Autowired
    private LoanFeeSettingService loanFeeSettingService;
    @GetMapping("/queryByCarLicenseNum")
    public ResponseModel<PageResult> getLoanFeeSetting(@RequestBody QueryAllByCarLicenseNumParam param) {
        PageResult<LoanFeeSetting> insuranceCompany = loanFeeSettingService.getInsuranceCompany(param);
        return new ResponseModel<>(insuranceCompany);
    }
}
