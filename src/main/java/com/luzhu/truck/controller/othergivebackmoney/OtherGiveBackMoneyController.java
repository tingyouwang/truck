package com.luzhu.truck.controller.othergivebackmoney;

import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.dto.othergivebackmoney.AddOtherGiveBackMoneyParam;
import com.luzhu.truck.dto.othergivebackmoney.UpdateOtherGiveBackMoneyParam;
import com.luzhu.truck.entity.othergivebackmoney.OtherGiveBackMoney;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.othergivebackmoney.OtherGiveBackMoneyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otherGiveBackMoney")
@CrossOrigin("*")
public class OtherGiveBackMoneyController {
    @Autowired
    private OtherGiveBackMoneyService otherGiveBackMoneyService;
    @PostMapping("/addOtherGiveBackMoney")
    public ResponseModel<Object> addOtherGiveBackMoney(@RequestBody @Valid AddOtherGiveBackMoneyParam param) {
        otherGiveBackMoneyService.addOtherGiveBackMoney(param);

        return new ResponseModel<>();
    }
    @PostMapping("/updateOtherGiveBackMoney")
    public ResponseModel<Object> updateOtherGiveBackMoney(@RequestBody @Valid UpdateOtherGiveBackMoneyParam param) {
        otherGiveBackMoneyService.updateOtherGiveBackMoney(param);

        return new ResponseModel<>();
    }

    @PostMapping("/getOtherGiveBackMoney")
    public ResponseModel<PageResult<OtherGiveBackMoney>> getOtherGiveBackMoneyList(@RequestBody @Valid LicenseAndExpenseYearMonthParam param) {
        return new ResponseModel<>(otherGiveBackMoneyService.getOtherGiveBackMoneyList(param));
    }

    @PostMapping("/voidAndRebillToMonth")
    public ResponseModel<Object> voidAndRebillToMonth(@RequestBody @Valid VoidAndRebillToMonthParam param) {
        otherGiveBackMoneyService.voidAndRebillToMonth(param);
        return new ResponseModel<>();
    }
}
