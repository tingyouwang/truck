package com.luzhu.truck.controller.payinterest;

import com.luzhu.truck.dto.payinterest.AddPayInterestParam;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.payinterest.PayInterestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payInterest")
@CrossOrigin("*")
public class PayInterestController {
    @Autowired
    private PayInterestService payInterestService;
    @PostMapping("/addPayInterest")
    public ResponseModel<Object> addPayInterest(@RequestBody @Valid AddPayInterestParam param) {
        payInterestService.addPayInterest(param);

        return new ResponseModel<>();
    }
}
