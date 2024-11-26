package com.luzhu.truck.controller.otherlendmoney;

import com.luzhu.truck.dto.otherlendmoney.AddOtherLendMoneyParam;
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
}
