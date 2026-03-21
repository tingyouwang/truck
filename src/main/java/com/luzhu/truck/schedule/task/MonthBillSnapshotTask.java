package com.luzhu.truck.schedule.task;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.service.bill.BillService;
import com.luzhu.truck.service.monthbillsnapshot.MonthBillSnapshotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class MonthBillSnapshotTask {

    @Autowired
    private CarCache carCache;
    @Autowired
    private BillService billService;
    @Autowired
    private MonthBillSnapshotService monthBillSnapshotService;
    @Value("${env.time.offset}")
    private String timeOffset;

    /**
     * 每日 01:00:00（台北）为全部车辆生成/更新「当前月份」账单快照，逻辑同 {@code BillController#generateBillSnapshot}
     */
    @Scheduled(cron = "0 0 1 * * ?", zone = "Asia/Taipei")
    public void generateDailyBillSnapshots() {
        LocalDateTime start = LocalDateTime.now();
        try {
            LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
            String yearMonth = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<CarInfo> allCars = carCache.getAllCars("all");
            log.info("[MonthBillSnapshotTask] 开始: yearMonth={}, 车辆数={}", yearMonth, allCars.size());

            int ok = 0;
            int fail = 0;
            for (CarInfo car : allCars) {
                try {
                    MonthBillReq req = new MonthBillReq();
                    req.setCarLicenseNum(car.getLicenseNumber());
                    req.setBillDate(yearMonth);
                    MonthBillResponse monthBill = billService.getMonthBillForceRecalculate(req);
                    monthBillSnapshotService.saveSnapshot(
                            car.getLicenseNumber(),
                            yearMonth,
                            monthBill,
                            "AUTO",
                            now
                    );
                    ok++;
                } catch (Exception e) {
                    fail++;
                    log.error("[MonthBillSnapshotTask] 车牌={} 快照失败: {}", car.getLicenseNumber(), e.getMessage(), e);
                }
            }
            LocalDateTime end = LocalDateTime.now();
            log.info("[MonthBillSnapshotTask] 结束: 成功={}, 失败={}, 耗时={}秒",
                    ok, fail, Duration.between(start, end).getSeconds());
        } catch (ExecutionException e) {
            log.error("[MonthBillSnapshotTask] 取得车辆列表失败", e);
        } catch (Exception e) {
            log.error("[MonthBillSnapshotTask] 排程执行错误", e);
        }
    }
}
