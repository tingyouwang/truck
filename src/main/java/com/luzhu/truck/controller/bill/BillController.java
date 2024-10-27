package com.luzhu.truck.controller.bill;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.dto.bill.MonthBillDetailReq;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.bill.MonthsBillDetailResponse;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.response.ResponseEnum;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.bill.BillService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseModel<MonthBillResponse> getMonthBill(@RequestBody @Valid MonthBillReq req) throws ExecutionException {
        List<CarInfo> allCars = carCache.getAllCars("all");

        Optional<CarInfo> searchCarOpt = allCars.stream().filter(car -> car.getId() == req.getId()).findFirst();

        if (searchCarOpt.isPresent()) {
            MonthBillResponse monthBill = billService.getMonthBill(req);
            return new ResponseModel<>(monthBill);
        } else {
            //todo 待確認拋錯為甚麼沒有response to 前端
            log.info("查無此車主");
            return new ResponseModel<>(ResponseEnum.DATA_IS_EMPTY);
        }


    }

    @PostMapping("/monthBillDetail")
    public ResponseModel<MonthsBillDetailResponse> getMonthBillDetail(@RequestBody @Valid MonthBillDetailReq req) throws ExecutionException {
        List<CarInfo> allCars = carCache.getAllCars("all");

        Optional<CarInfo> searchCarOpt = allCars.stream().filter(car -> car.getId() == req.getId()).findFirst();

        if (searchCarOpt.isPresent()) {
            MonthsBillDetailResponse billDetail = billService.getBillDetail(req);
            return new ResponseModel<>(billDetail);
        } else {
            log.info("查無此車主");
            return new ResponseModel<>(ResponseEnum.DATA_IS_EMPTY);
        }
    }

}
