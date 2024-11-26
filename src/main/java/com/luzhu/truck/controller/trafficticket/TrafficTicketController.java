package com.luzhu.truck.controller.trafficticket;

import com.luzhu.truck.dto.lendmoney.AddLendMoneyParam;
import com.luzhu.truck.dto.trafficticket.AddTrafficTicketParam;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.trafficticket.TrafficTicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trafficTicket")
@CrossOrigin("*")
public class TrafficTicketController {
    @Autowired
    private TrafficTicketService trafficTicketService;
    @PostMapping("/addTrafficTicket")
    public ResponseModel<Object> addLendMoney(@RequestBody @Valid AddTrafficTicketParam param) {
        trafficTicketService.addTicket(param);

        return new ResponseModel<>();
    }

}
