package com.luzhu.truck.controller.payinterest;

import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.dto.payinterest.AddPayInterestParam;
import com.luzhu.truck.dto.payinterest.UpdatePayInterestParam;
import com.luzhu.truck.entity.payinterest.PayInterest;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.payinterest.PayInterestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payInterest")
@CrossOrigin("*")
public class PayInterestController {
    @Autowired
    private PayInterestService payInterestService;
    @PostMapping("/addPayInterest")
    public ResponseModel<Object> addPayInterest(@RequestBody @Valid AddPayInterestParam param) {
        payInterestService.addPayInterest(param);

        return new ResponseModel<>();
    }

    @PostMapping("/updatePayInterest")
    public ResponseModel<Object> updatePayInterest(@RequestBody @Valid UpdatePayInterestParam param) {
        payInterestService.updatePayInterest(param);

        return new ResponseModel<>();
    }

    @PostMapping("/getPayInterest")
    public ResponseModel<PageResult<PayInterest>> getPayInterestList(@RequestBody @Valid LicenseAndExpenseYearMonthParam param) {
        return new ResponseModel<>(payInterestService.getPayInterestList(param));
    }

    @PostMapping("/voidAndRebillToMonth")
    public ResponseModel<Object> voidAndRebillToMonth(@RequestBody @Valid VoidAndRebillToMonthParam param) {
        payInterestService.voidAndRebillToMonth(param);
        return new ResponseModel<>();
    }
}
