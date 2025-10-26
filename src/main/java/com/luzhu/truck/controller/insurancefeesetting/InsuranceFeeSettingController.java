package com.luzhu.truck.controller.insurancefeesetting;

import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.car.LicenseNumPageParam;
import com.luzhu.truck.dto.insurancefeesetting.*;
import com.luzhu.truck.entity.insurancefeesetting.InsuranceFeeSetting;
import com.luzhu.truck.entity.insurancefeesetting.InsuranceFeeSettingDto;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.insurancefeesetting.InsuranceFeeSettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.luzhu.truck.exception.SystemExceptionEnum.END_DATE_EARLY_THAN_START_DATE;

@RestController
@RequestMapping("/insuranceFeeSetting")
@CrossOrigin("*")
public class InsuranceFeeSettingController {
    @Autowired
    private InsuranceFeeSettingService insuranceFeeSettingService;

    @PostMapping("/getInsuranceFeeSetting")
    public ResponseModel<PageResult<InsuranceFeeSetting>> getInsuranceCompany(@RequestBody LicenseNumPageParam param) {
        PageResult<InsuranceFeeSetting> insuranceFeeSetting = insuranceFeeSettingService.getInsuranceFeeSetting(param);
        return new ResponseModel<>(insuranceFeeSetting);
    }

    @PostMapping("/getSingleInsuranceFeeSetting")
    public ResponseModel<InsuranceFeeSetting> getSingleInsuranceCompany(@RequestBody GetSingleInsuranceSettingParam param) {
        InsuranceFeeSetting insuranceFeeSetting = insuranceFeeSettingService.getSingleInsuranceSetting(param);
        return new ResponseModel<>(insuranceFeeSetting);
    }

    @PostMapping("/addInsuranceFeeSetting")
    public ResponseModel<Object> addInsuranceCompany(@RequestBody @Valid AddInsuranceFeeSettingParam addInsuranceFeeSettingParam) {
        LocalDate start = LocalDate.parse(addInsuranceFeeSettingParam.getStartDate());
        LocalDate end = LocalDate.parse(addInsuranceFeeSettingParam.getEndDate());
        if (!end.isAfter(start)) throw new AppException(END_DATE_EARLY_THAN_START_DATE);
        insuranceFeeSettingService.addInsuranceFeeSetting(addInsuranceFeeSettingParam);

        return new ResponseModel<>();
    }

    @PostMapping("/updateInsuranceFeeSetting")
    public ResponseModel<Object> updateInsuranceCompany(@RequestBody @Valid UpdateInsuranceFeeSettingParam param) {
        insuranceFeeSettingService.updateInsuranceFeeSetting(param);

        return new ResponseModel<>();
    }

    @PostMapping("/updateInsuranceFeeSettingStatus")
    public ResponseModel<Object> updateInsuranceCompany(@RequestBody @Valid UpdateInsuranceFeeSettingStatusParam param) {
        insuranceFeeSettingService.updateInsuranceFeeSettingStatus(param);

        return new ResponseModel<>();
    }

//    @PostMapping("/updateInsuranceCompany")
//    public ResponseModel<Object> updateInsuranceCompany(@RequestBody @Valid UpdateInsuranceComParam updateCarAgencyParam) {
//        insuranceCompanyService.updateInsuranceCompany(updateCarAgencyParam);
//
//        return new ResponseModel<>();
//    }
//
    @PostMapping("/deleteInsuranceFeeSetting")
    public ResponseModel<Object> deleteInsuranceCompany(@RequestBody @Valid DeleteInsuranceSettingParam param) {
        insuranceFeeSettingService.deleteInsuranceFeeSetting(param);

        return new ResponseModel<>();
    }
}
