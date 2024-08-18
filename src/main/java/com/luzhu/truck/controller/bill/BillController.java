package com.luzhu.truck.controller.bill;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.bill.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/bill")
@Slf4j
public class BillController {
    @Autowired
    private CarCache carCache;
    @Autowired
    private BillService billService;
    @PostMapping("/monthBill")
    public ResponseModel getMonthBill(@RequestBody MonthBillReq req) throws ExecutionException {
        List<CarInfo> allCars = carCache.getAllCars("all");

        Optional<CarInfo> searchCarOpt = allCars.stream().filter(car -> car.getId() == req.getId()).findFirst();

        if (searchCarOpt.isPresent()) {
            MonthBillResponse monthBill = billService.getMonthBill(req);
            return new ResponseModel<>(monthBill);
        } else {
            //todo 待確認拋錯為甚麼沒有response to 前端
            log.info("查無此車主");
            throw new AppException(SystemExceptionEnum.NO_DATA);
        }


    }

}
