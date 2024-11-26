package com.luzhu.truck.controller.loanFeeSetting;

import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.insurancecompany.AddInsuranceComParam;
import com.luzhu.truck.dto.loanfeesetting.AddLoanFeeSettingParam;
import com.luzhu.truck.dto.loanfeesetting.QueryAllByCarLicenseNumParam;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.loanfeesetting.LoanFeeSettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/loanFeeSetting")
@CrossOrigin("*")
public class LoanFeeSettingController {
    @Autowired
    private LoanFeeSettingService loanFeeSettingService;
    @PostMapping("/queryByCarLicenseNum")
    public ResponseModel<PageResult> getLoanFeeSetting(@RequestBody QueryAllByCarLicenseNumParam param) {
        PageResult<LoanFeeSetting> insuranceCompany = loanFeeSettingService.getLoanFeeSetting(param);
        return new ResponseModel<>(insuranceCompany);
    }
    @PostMapping("/addLoanFeeSetting")
    public ResponseModel<Object> addInsuranceCompany(@RequestBody @Valid AddLoanFeeSettingParam addLoanFeeSettingParam) {
        loanFeeSettingService.addLoanFeeSetting(addLoanFeeSettingParam);

        return new ResponseModel<>();
    }
}
