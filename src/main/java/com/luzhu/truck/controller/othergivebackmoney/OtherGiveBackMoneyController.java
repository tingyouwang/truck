package com.luzhu.truck.controller.othergivebackmoney;

import com.luzhu.truck.dto.othergivebackmoney.AddOtherGiveBackMoneyParam;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.othergivebackmoney.OtherGiveBackMoneyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otherGiveBackMoney")
public class OtherGiveBackMoneyController {
    @Autowired
    private OtherGiveBackMoneyService otherGiveBackMoneyService;
    @PostMapping("/addOtherGiveBackMoney")
    public ResponseModel<Object> addOtherLendMoney(@RequestBody @Valid AddOtherGiveBackMoneyParam param) {
        otherGiveBackMoneyService.addOtherGiveBackMoney(param);

        return new ResponseModel<>();
    }
}
