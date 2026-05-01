package com.luzhu.truck.controller.lendmoney;

import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.lendmoney.AddLendMoneyParam;
import com.luzhu.truck.dto.lendmoney.UpdateLendMoneyParam;
import com.luzhu.truck.entity.lendmoney.LendMoney;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.lendmoney.LendMoneyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lendMoney")
@CrossOrigin("*")
public class LendMoneyController {
    @Autowired
    private LendMoneyService lendMoneyService;
    @PostMapping("/addLendMoney")
    public ResponseModel<Object> addLendMoney(@RequestBody @Valid AddLendMoneyParam param) {
        lendMoneyService.addLendMoney(param);

        return new ResponseModel<>();
    }

    @PostMapping("/updateLendMoney")
    public ResponseModel<Object> updateLendMoney(@RequestBody @Valid UpdateLendMoneyParam param) {
        lendMoneyService.updateLendMoney(param);

        return new ResponseModel<>();
    }

    @PostMapping("/getLendMoney")
    public ResponseModel<PageResult<LendMoney>> getLendMoneyList(@RequestBody @Valid LicenseAndExpenseYearMonthParam param) {
        return new ResponseModel<>(lendMoneyService.getLendMoneyList(param));
    }

}
