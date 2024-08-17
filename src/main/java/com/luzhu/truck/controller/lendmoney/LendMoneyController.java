package com.luzhu.truck.controller.lendmoney;

import com.luzhu.truck.dto.lendmoney.AddLendMoneyParam;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.lendmoney.LendMoneyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lendMoney")
public class LendMoneyController {
    @Autowired
    private LendMoneyService lendMoneyService;
    @PostMapping("/addLendMoney")
    public ResponseModel<Object> addLendMoney(@RequestBody @Valid AddLendMoneyParam param) {
        lendMoneyService.addLendMoney(param);

        return new ResponseModel<>();
    }

}
