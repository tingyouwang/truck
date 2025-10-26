package com.luzhu.truck.controller.loanFeeSetting;

import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.insurancecompany.AddInsuranceComParam;
import com.luzhu.truck.dto.loanfeesetting.AddLoanFeeSettingParam;
import com.luzhu.truck.dto.loanfeesetting.QueryAllByCarLicenseNumParam;
import com.luzhu.truck.entity.Car;
import com.luzhu.truck.entity.loanfeesetting.LoanFeeSetting;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.loanfeesetting.LoanFeeSettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.luzhu.truck.exception.SystemExceptionEnum.END_DATE_EARLY_THAN_START_DATE;

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
        LocalDate start = LocalDate.parse(addLoanFeeSettingParam.getStartDate());
        LocalDate end = LocalDate.parse(addLoanFeeSettingParam.getEndDate());
        if (!end.isAfter(start)) throw new AppException(END_DATE_EARLY_THAN_START_DATE);
        loanFeeSettingService.addLoanFeeSetting(addLoanFeeSettingParam);

        return new ResponseModel<>();
    }
}
