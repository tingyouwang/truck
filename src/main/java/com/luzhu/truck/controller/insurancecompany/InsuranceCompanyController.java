package com.luzhu.truck.controller.insurancecompany;

import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.insurancecompany.AddInsuranceComParam;
import com.luzhu.truck.dto.insurancecompany.InsuranceComDropDownList;
import com.luzhu.truck.dto.insurancecompany.UpdateInsuranceComParam;
import com.luzhu.truck.dto.insurancecompany.UpdateInsuranceCompanyStatusParam;
import com.luzhu.truck.entity.insurancecompany.InsuranceCompany;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.insurancecompany.InsuranceCompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insuranceCompany")
@CrossOrigin("*")
public class InsuranceCompanyController {
    @Autowired
    private InsuranceCompanyService insuranceCompanyService;
    @PostMapping("/getInsuranceCompany")
    public ResponseModel<PageResult<InsuranceCompany>> getInsuranceCompany(@RequestBody BaseParam param) {
        PageResult<InsuranceCompany> insuranceCompany = insuranceCompanyService.getInsuranceCompany(param);
        return new ResponseModel<>(insuranceCompany);
    }

    @PostMapping("/getInsuranceCompanyDropDown")
    public ResponseModel<List<InsuranceComDropDownList>> getInsuranceCompanyDropDown() {
        return new ResponseModel<>(insuranceCompanyService.getInsuranceComDropDown());
    }

    @PostMapping("/addInsuranceCompany")
    public ResponseModel<Object> addInsuranceCompany(@RequestBody @Valid AddInsuranceComParam addInsuranceComParam) {
        insuranceCompanyService.addInsuranceCom(addInsuranceComParam);

        return new ResponseModel<>();
    }

    @PostMapping("/updateInsuranceCompany")
    public ResponseModel<Object> updateInsuranceCompany(@RequestBody @Valid UpdateInsuranceComParam updateCarAgencyParam) {
        insuranceCompanyService.updateInsuranceCompany(updateCarAgencyParam);

        return new ResponseModel<>();
    }

//    @PostMapping("/deleteInsuranceCompany/{id}")
//    public ResponseModel<Object> deleteInsuranceCompany(@PathVariable int id) {
//        insuranceCompanyService.deleteInsuranceCompany(id);
//
//        return new ResponseModel<>();
//    }

    @PostMapping("/disableInsuranceCompany")
    public ResponseModel<Object> disableInsuranceCompany(@RequestBody @Valid UpdateInsuranceCompanyStatusParam param) {
        insuranceCompanyService.updateInsuranceCompanyStatus(param);

        return new ResponseModel<>();
    }
}
