package com.luzhu.truck.controller.givebackmoney;

import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.givebackmoney.AddGiveBackMoneyParam;
import com.luzhu.truck.dto.givebackmoney.UpdateGiveBackMoneyParam;
import com.luzhu.truck.entity.givebackmoney.GiveBackMoney;
import com.luzhu.truck.response.PageResult;
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
    public ResponseModel<Object> addGiveBackMoney(@RequestBody @Valid AddGiveBackMoneyParam param) {
        giveBackMoneyService.addGiveBackMoney(param);

        return new ResponseModel<>();
    }

    @PostMapping("/updateGiveBackMoney")
    public ResponseModel<Object> updateGiveBackMoney(@RequestBody @Valid UpdateGiveBackMoneyParam param) {
        giveBackMoneyService.updateGiveBackMoney(param);

        return new ResponseModel<>();
    }

    @PostMapping("/getLendMoney")
    public ResponseModel<PageResult<GiveBackMoney>> getGiveBackMoneyList(@RequestBody @Valid LicenseAndExpenseYearMonthParam param) {
        return new ResponseModel<>(giveBackMoneyService.getGiveBackMoneyList(param));
    }

}
