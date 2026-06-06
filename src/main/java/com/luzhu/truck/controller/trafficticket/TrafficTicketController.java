package com.luzhu.truck.controller.trafficticket;

import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.dto.trafficticket.AddTrafficTicketParam;
import com.luzhu.truck.dto.trafficticket.UpdateTrafficTicketParam;
import com.luzhu.truck.entity.trafficticket.TrafficTicket;
import com.luzhu.truck.response.PageResult;
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
    public ResponseModel<Object> addTrafficTicket(@RequestBody @Valid AddTrafficTicketParam param) {
        trafficTicketService.addTrafficTicket(param);

        return new ResponseModel<>();
    }

    @PostMapping("/updateTrafficTicket")
    public ResponseModel<Object> updateTrafficTicket(@RequestBody @Valid UpdateTrafficTicketParam param) {
        trafficTicketService.updateTrafficTicket(param);

        return new ResponseModel<>();
    }

    @PostMapping("/getTrafficTicket")
    public ResponseModel<PageResult<TrafficTicket>> getTrafficTicketList(@RequestBody @Valid LicenseAndExpenseYearMonthParam param) {
        return new ResponseModel<>(trafficTicketService.getTrafficTicketList(param));
    }

    @PostMapping("/voidAndRebillToMonth")
    public ResponseModel<Object> voidAndRebillToMonth(@RequestBody @Valid VoidAndRebillToMonthParam param) {
        trafficTicketService.voidAndRebillToMonth(param);
        return new ResponseModel<>();
    }

}
