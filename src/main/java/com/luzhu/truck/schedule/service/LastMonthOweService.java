package com.luzhu.truck.schedule.service;

import com.luzhu.truck.cache.CarCache;
import com.luzhu.truck.dao.lastmonthowe.LastMonthOweDao;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.car.CarInfo;
import com.luzhu.truck.entity.lastmonthowe.LastMonthOwe;
import com.luzhu.truck.service.bill.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class LastMonthOweService {
    @Autowired
    private BillService billService;
    @Autowired
    private CarCache carCache;
    @Autowired
    private LastMonthOweDao lastMonthOweDao;
    public void addLastMonthOwe(String yearMonth, LocalDateTime now) throws ExecutionException {
        List<CarInfo> allCars = carCache.getAllCars("all");

        List<LastMonthOwe> lastMonthOwes = new ArrayList<>();
        for (CarInfo car : allCars) {
            CompletableFuture<LastMonthOwe> future = CompletableFuture.supplyAsync(() -> {
                MonthBillReq monthBillReq = new MonthBillReq();
                monthBillReq.setBillDate(yearMonth);
                monthBillReq.setCarLicenseNum(car.getLicenseNumber());
                monthBillReq.setId(car.getId());
                monthBillReq.setOwnerName(car.getOwnerName());

                MonthBillResponse monthBill = billService.getMonthBill(monthBillReq);
                LastMonthOwe lastMonthOwe = new LastMonthOwe();
                lastMonthOwe.setExpenseYearMonth(yearMonth);
                lastMonthOwe.setAmount(monthBill.getTotalSum());
                lastMonthOwe.setCarLicenseNum(car.getLicenseNumber());
                lastMonthOwe.setCreateTime(String.valueOf(now.toEpochSecond(ZoneOffset.UTC)));

                return lastMonthOwe;
            });
            lastMonthOwes.add(future.join());
        }
        log.info(String.format("task generateLastMonthOwe: lastMonthOwes 數量:%s", lastMonthOwes.size()));
        for (var lastMonthOwe : lastMonthOwes) {
            log.info(String.format("task generateLastMonthOwe: sql param: amount:%s, carLicenseNum:%s", lastMonthOwe.getAmount()
                    ,lastMonthOwe.getCarLicenseNum()));
        }
        lastMonthOweDao.saveAll(lastMonthOwes);
    }
}
