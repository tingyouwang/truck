package com.luzhu.truck.controller.otherlendmoney;

import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.dto.otherlendmoney.AddOtherLendMoneyParam;
import com.luzhu.truck.dto.otherlendmoney.UpdateOtherLendMoneyParam;
import com.luzhu.truck.entity.otherlendmoney.OtherLendMoney;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.otherlendmoney.OtherLendMoneyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otherLendMoney")
@CrossOrigin("*")
public class OtherLendMoneyController {
    @Autowired
    private OtherLendMoneyService otherLendMoneyService;
    @PostMapping("/addOtherLendMoney")
    public ResponseModel<Object> addOtherLendMoney(@RequestBody @Valid AddOtherLendMoneyParam param) {
        otherLendMoneyService.addOtherLendMoney(param);

        return new ResponseModel<>();
    }

    @PostMapping("/updateOtherLendMoney")
    public ResponseModel<Object> updateOtherLendMoney(@RequestBody @Valid UpdateOtherLendMoneyParam param) {
        otherLendMoneyService.updateOtherLendMoney(param);

        return new ResponseModel<>();
    }

    @PostMapping("/getOtherLendMoney")
    public ResponseModel<PageResult<OtherLendMoney>> getOtherLendMoneyList(@RequestBody @Valid LicenseAndExpenseYearMonthParam param) {
        return new ResponseModel<>(otherLendMoneyService.getOtherLendMoneyList(param));
    }

    @PostMapping("/voidAndRebillToMonth")
    public ResponseModel<Object> voidAndRebillToMonth(@RequestBody @Valid VoidAndRebillToMonthParam param) {
        otherLendMoneyService.voidAndRebillToMonth(param);
        return new ResponseModel<>();
    }
}
