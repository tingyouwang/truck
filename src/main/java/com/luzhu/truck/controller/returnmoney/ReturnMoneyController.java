package com.luzhu.truck.controller.returnmoney;

import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.returnmoney.AddReturnMoneyParam;
import com.luzhu.truck.dto.returnmoney.UpdateReturnMoneyParam;
import com.luzhu.truck.entity.returnmoney.ReturnMoney;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.returnmoney.ReturnMoneyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/returnMoney")
@CrossOrigin("*")
public class ReturnMoneyController {
    @Autowired
    private ReturnMoneyService returnMoneyService;

    @PostMapping("/addReturnMoney")
    public ResponseModel<Object> addReturnMoney(@RequestBody @Valid AddReturnMoneyParam param) {
        returnMoneyService.addReturnMoney(param);

        return new ResponseModel<>();
    }
    @PostMapping("/updateReturnMoney")
    public ResponseModel<Object> updateReturnMoney(@RequestBody @Valid UpdateReturnMoneyParam param) {
        returnMoneyService.updateReturnMoney(param);

        return new ResponseModel<>();
    }

    @PostMapping("/getReturnMoney")
    public ResponseModel<PageResult<ReturnMoney>> getReturnMoneyList(@RequestBody @Valid LicenseAndExpenseYearMonthParam param) {
        return new ResponseModel<>(returnMoneyService.getReturnMoneyList(param));
    }
}
