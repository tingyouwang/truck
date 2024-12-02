package com.luzhu.truck.controller.othergivebackmoney;

import com.luzhu.truck.dto.othergivebackmoney.AddOtherGiveBackMoneyParam;
import com.luzhu.truck.dto.othergivebackmoney.UpdateOtherGiveBackMoneyParam;
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
    public ResponseModel<Object> addOtherLendMoney(@RequestBody @Valid AddOtherGiveBackMoneyParam param) {
        otherGiveBackMoneyService.addOtherGiveBackMoney(param);

        return new ResponseModel<>();
    }
    @PostMapping("/updateOtherGiveBackMoney")
    public ResponseModel<Object> updateOtherLendMoney(@RequestBody @Valid UpdateOtherGiveBackMoneyParam param) {
        otherGiveBackMoneyService.updateOtherGiveBackMoney(param);

        return new ResponseModel<>();
    }
}
