package com.luzhu.truck.controller.insurancefeesetting;

import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.insurancecompany.AddInsuranceComParam;
import com.luzhu.truck.dto.insurancecompany.UpdateInsuranceComParam;
import com.luzhu.truck.dto.insurancefeesetting.AddInsuranceFeeSettingParam;
import com.luzhu.truck.entity.insurancecompany.InsuranceCompany;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.insurancecompany.InsuranceCompanyService;
import com.luzhu.truck.service.insurancefeesetting.InsuranceFeeSettingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insuranceFeeSetting")
public class InsuranceFeeSettingController {
    @Autowired
    private InsuranceFeeSettingService insuranceFeeSettingService;

    //todo
//    @GetMapping("/getInsuranceCompany")
//    public ResponseModel<PageResult<InsuranceCompany>> getInsuranceCompany(@RequestBody BaseParam param) {
//        PageResult<InsuranceCompany> insuranceCompany = insuranceCompanyService.getInsuranceCompany(param);
//        return new ResponseModel<>(insuranceCompany);
//    }

    @PostMapping("/addInsuranceFeeSetting")
    public ResponseModel<Object> addInsuranceCompany(@RequestBody @Valid AddInsuranceFeeSettingParam addInsuranceFeeSettingParam) {
        insuranceFeeSettingService.addInsuranceFeeSetting(addInsuranceFeeSettingParam);

        return new ResponseModel<>();
    }

//    @PostMapping("/updateInsuranceCompany")
//    public ResponseModel<Object> updateInsuranceCompany(@RequestBody @Valid UpdateInsuranceComParam updateCarAgencyParam) {
//        insuranceCompanyService.updateInsuranceCompany(updateCarAgencyParam);
//
//        return new ResponseModel<>();
//    }
//
//    @PostMapping("/deleteInsuranceCompany/{id}")
//    public ResponseModel<Object> deleteInsuranceCompany(@PathVariable int id) {
//        insuranceCompanyService.deleteInsuranceCompany(id);
//
//        return new ResponseModel<>();
//    }
}
