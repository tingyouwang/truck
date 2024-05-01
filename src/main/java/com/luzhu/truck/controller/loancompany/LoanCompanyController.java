package com.luzhu.truck.controller.loancompany;

import com.luzhu.truck.dto.BaseParam;
import com.luzhu.truck.dto.insurancecompany.AddInsuranceComParam;
import com.luzhu.truck.dto.insurancecompany.UpdateInsuranceComParam;
import com.luzhu.truck.dto.loancompany.AddLoadComParam;
import com.luzhu.truck.dto.loancompany.UpdateLoanComParam;
import com.luzhu.truck.entity.loancompany.LoanCompany;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.loancompany.LoanCompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loanCompany")
public class LoanCompanyController {
    @Autowired
    private LoanCompanyService loanCompanyService;
    @GetMapping("/getLoanCompany")
    public ResponseModel<PageResult<LoanCompany>> getLoanCompany(@RequestBody BaseParam param) {
        PageResult<LoanCompany> carAgency = loanCompanyService.getLoanCompany(param);

        return new ResponseModel<>(carAgency);
    }

    @PostMapping("/addLoanCompany")
    public ResponseModel<Object> addInsuranceCompany(@RequestBody @Valid AddLoadComParam param) {
        loanCompanyService.addLoanCom(param);

        return new ResponseModel<>();
    }

    @PostMapping("/updateLoanCompany")
    public ResponseModel<Object> updateInsuranceCompany(@RequestBody @Valid UpdateLoanComParam param) {
        loanCompanyService.updateLoanCom(param);

        return new ResponseModel<>();
    }

    @PostMapping("/deleteLoanCompany/{id}")
    public ResponseModel<Object> deleteCarAgency(@PathVariable int id) {
        loanCompanyService.deleteLoanCompany(id);

        return new ResponseModel<>();
    }
}
