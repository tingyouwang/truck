package com.luzhu.truck.controller.givebackmoney;

import com.luzhu.truck.dto.givebackmoney.AddGiveBackMoneyParam;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.givebackmoney.GiveBackMoneyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/giveBackMoney")
@CrossOrigin("*")
public class GiveBackMoneyController {
    @Autowired
    private GiveBackMoneyService giveBackMoneyService;
    @PostMapping("/addGiveBackMoney")
    public ResponseModel<Object> addLendMoney(@RequestBody @Valid AddGiveBackMoneyParam param) {
        giveBackMoneyService.addGiveBackMoney(param);

        return new ResponseModel<>();
    }

}
