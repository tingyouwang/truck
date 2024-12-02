package com.luzhu.truck.controller.returnmoney;

import com.luzhu.truck.dto.returnmoney.AddReturnMoneyParam;
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
}
