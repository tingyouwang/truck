package com.luzhu.truck.controller.bill;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.controller.report.ReportService;
import com.luzhu.truck.dto.bill.*;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.response.ResponseEnum;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.service.bill.BillService;
import com.luzhu.truck.service.monthbillsnapshot.MonthBillSnapshotService;
import com.luzhu.truck.util.DateTimeValidate;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    @Autowired
    private MonthBillSnapshotService monthBillSnapshotService;
    @Value("${env.time.offset}")
    private String timeOffset;
    
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

    @Deprecated
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

    @PostMapping("/generateBillSnapshot")
    public ResponseModel<Object> generateBillSnapshot(@RequestBody @Valid MonthBillReq req) {
        try {
            // 检查车辆是否存在
            List<CarInfo> allCars = carCache.getAllCars("all");
            Optional<CarInfo> searchCarOpt = allCars.stream()
                .filter(car -> car.getLicenseNumber().equalsIgnoreCase(req.getCarLicenseNum()))
                .findFirst();
            
            if (searchCarOpt.isEmpty()) {
                log.info("查無此車主");
                return new ResponseModel<>(ResponseEnum.DATA_IS_EMPTY);
            }
            
            // 计算账单（强制重新计算，不使用快照）
            MonthBillResponse monthBill = billService.getMonthBillForceRecalculate(req);
            
            // 保存快照
            LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
            monthBillSnapshotService.saveSnapshot(
                req.getCarLicenseNum(), 
                req.getBillDate(), 
                monthBill, 
                "MANUAL", 
                now
            );
            
            log.info("手动生成账单快照成功: 车牌={}, 月份={}", req.getCarLicenseNum(), req.getBillDate());
            return new ResponseModel<>();
        } catch (Exception e) {
            log.error("生成账单快照失败", e);
            return new ResponseModel<>(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 前台依目前畫面/編輯內容提交一筆快照，不呼叫帳單重算。
     * 請求體可沿用 {@link MonthBillResponse} 結構置於 billData。
     */
    @PostMapping("/submitBillSnapshot")
    public ResponseModel<Object> submitBillSnapshot(@RequestBody @Valid SaveClientMonthBillSnapshotReq req) {
        DateTimeValidate.checkYearMonth(req.getBillDate());
        try {
            List<CarInfo> allCars = carCache.getAllCars("all");
            Optional<CarInfo> searchCarOpt = allCars.stream()
                    .filter(car -> car.getLicenseNumber().equalsIgnoreCase(req.getCarLicenseNum()))
                    .findFirst();
            if (searchCarOpt.isEmpty()) {
                log.info("查無此車主");
                return new ResponseModel<>(ResponseEnum.DATA_IS_EMPTY);
            }
            LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
            monthBillSnapshotService.saveSnapshotAndCascadeForward(
                    req.getCarLicenseNum(),
                    req.getBillDate(),
                    req.getBillData(),
                    "USER",
                    now,
                    req.getRemark()
            );
            log.info("前台提交账单快照成功: 车牌={}, 月份={}", req.getCarLicenseNum(), req.getBillDate());
            return new ResponseModel<>();
        } catch (Exception e) {
            log.error("前台提交账单快照失败", e);
            return new ResponseModel<>(ResponseEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 查詢指定車牌、月份之帳單快照變更歷程（由舊到新，含每次儲存後的完整明細 JSON 還原結果）
     */
    @GetMapping("/monthBillSnapshotHistory")
    public ResponseModel<List<MonthBillSnapshotHistoryItemDto>> monthBillSnapshotHistory(
            @RequestParam String carLicenseNum,
            @RequestParam String billDate) {
        DateTimeValidate.checkYearMonth(billDate);
        List<MonthBillSnapshotHistoryItemDto> list =
                monthBillSnapshotService.listSnapshotHistory(carLicenseNum, billDate);
        return new ResponseModel<>(list);
    }

}
