package com.luzhu.truck.controller.bill;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.controller.report.ReportService;
import com.luzhu.truck.dto.bill.*;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.response.ResponseEnum;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.bill.BillService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/bill")
@CrossOrigin("*")
@Slf4j
public class BillController {
    @Autowired
    private CarCache carCache;
    @Autowired
    private BillService billService;
    @Autowired
    private ReportService reportService;
    @PostMapping("/monthBill")
    public ResponseModel<MonthBillResponse> getMonthBill(@RequestBody @Valid MonthBillReq req) throws ExecutionException {
        List<CarInfo> allCars = carCache.getAllCars("all");

//        Optional<CarInfo> searchCarOpt = allCars.stream().filter(car -> car.getId() == req.getId()).findFirst();
        Optional<CarInfo> searchCarOpt = allCars.stream().filter(car -> car.getLicenseNumber().equalsIgnoreCase(req.getCarLicenseNum())).findFirst();

        if (searchCarOpt.isPresent()) {
            MonthBillResponse monthBill = billService.getMonthBill(req);
            return new ResponseModel<>(monthBill);
        } else {
            //todo 待確認拋錯為甚麼沒有response to 前端
            log.info("查無此車主");
            return new ResponseModel<>(ResponseEnum.DATA_IS_EMPTY);
        }
    }

    @PostMapping("/generateCurrentMonthBill")
    public ResponseModel<Object> generateCurrentMonthBill(@RequestBody @Valid GenerateCurrentMonthBillReq req) throws ExecutionException {
        List<CarInfo> allCars = carCache.getAllCars("all");

        Optional<CarInfo> searchCarOpt = allCars.stream().filter(car -> car.getLicenseNumber().equalsIgnoreCase(req.getCarLicenseNum())).findFirst();

        if (searchCarOpt.isPresent()) {
            billService.generateCurrentMonthBill(req);
            return new ResponseModel<>();
        } else {
            //todo 待確認拋錯為甚麼沒有response to 前端
            log.info("查無此車主");
            return new ResponseModel<>(ResponseEnum.DATA_IS_EMPTY);
        }
    }

    @PostMapping("/monthBillDetail")
    public ResponseModel<MonthsBillDetailResponse> getMonthBillDetail(@RequestBody @Valid MonthBillDetailReq req, HttpServletResponse response) throws Exception {
        List<CarInfo> allCars = carCache.getAllCars("all");

//        Optional<CarInfo> searchCarOpt = allCars.stream().filter(car -> car.getId() == req.getId()).findFirst();
        Optional<CarInfo> searchCarOpt = allCars.stream().filter(car -> car.getLicenseNumber().equalsIgnoreCase(req.getCarLicenseNum())).findFirst();

        if (searchCarOpt.isPresent()) {
            MonthsBillDetailResponse billDetail = billService.getBillDetail(req);
            if ("Y".equals(req.getPrint())) {

                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=report.pdf");
                OutputStream out = response.getOutputStream();
                reportService.billDetailPDF(out, billDetail, req, searchCarOpt.get());
                out.flush();
            }

            return new ResponseModel<>(billDetail);
        } else {
            log.info("查無此車主");
            return new ResponseModel<>(ResponseEnum.DATA_IS_EMPTY);
        }
    }

    @GetMapping("/ha")
    public ResponseModel<String > ds(HttpServletResponse response) throws Exception {
        return new ResponseModel<>("haha");
    }

}
