package com.luzhu.truck.service.monthbillsnapshot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luzhu.truck.dao.lastmonthowe.LastMonthOweDao;
import com.luzhu.truck.dao.monthbillsnapshot.MonthBillSnapshotDao;
import com.luzhu.truck.dao.monthbillsnapshot.MonthBillSnapshotHistoryDao;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.bill.MonthBillSnapshotHistoryItemDto;
import com.luzhu.truck.entity.lastmonthowe.LastMonthOwe;
import com.luzhu.truck.entity.monthbillsnapshot.MonthBillSnapshot;
import com.luzhu.truck.entity.monthbillsnapshot.MonthBillSnapshotHistory;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.util.DateTimeValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MonthBillSnapshotService {

    @Autowired
    private MonthBillSnapshotDao monthBillSnapshotDao;
    @Autowired
    private MonthBillSnapshotHistoryDao monthBillSnapshotHistoryDao;
    @Autowired
    private LastMonthOweDao lastMonthOweDao;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 保存账单快照（同一車牌+月份已存在則更新，避免 uk_car_month 重複鍵）
     */
    @Transactional
    public MonthBillSnapshot saveSnapshot(String carLicenseNum, String billYearMonth,
                                          MonthBillResponse billResponse,
                                          String snapshotType, LocalDateTime now) {
        return saveSnapshot(carLicenseNum, billYearMonth, billResponse, snapshotType, now, null);
    }

    /**
     * @param remark 選填，僅寫入歷史表（主表 remark 不由此更新）
     */
    @Transactional
    public MonthBillSnapshot saveSnapshot(String carLicenseNum, String billYearMonth,
                                          MonthBillResponse billResponse,
                                          String snapshotType, LocalDateTime now, String remark) {
        Optional<MonthBillSnapshot> existingOpt = monthBillSnapshotDao.findByCarAndMonth(carLicenseNum, billYearMonth);
        MonthBillSnapshot snapshot = existingOpt.orElseGet(MonthBillSnapshot::new);
        snapshot.setCarLicenseNum(carLicenseNum);
        snapshot.setBillYearMonth(billYearMonth);

        snapshot.setLastMonthOweAmount(billResponse.getLastMonthOweAmount());
        snapshot.setManageFee(billResponse.getManageFee());
        snapshot.setUnionFee(billResponse.getUnionFee());
        snapshot.setLaborInsuranceFee(billResponse.getLaborInsuranceFee());
        snapshot.setHealthFee(billResponse.getHealthFee());
        snapshot.setInsuranceFee(billResponse.getInsuranceFee());
        snapshot.setLicenseTaxFee(billResponse.getLicenseTaxFee());
        snapshot.setFuelTaxFee(billResponse.getFuelTaxFee());
        snapshot.setLoanFee(billResponse.getLoanFee());

        snapshot.setInvoiceSaleAmount(billResponse.getInvoiceSaleAmount());
        snapshot.setInvoiceSaleAmountTax(billResponse.getInvoiceSaleAmountTax());
        snapshot.setInvoiceOffsetAmount(billResponse.getInvoiceOffsetAmount());
        snapshot.setInvoiceOffsetAmountTax(billResponse.getInvoiceOffsetAmountTax());
        snapshot.setInvoiceGasAmount(billResponse.getInvoiceGasAmount());
        snapshot.setInvoiceGasAmountTax(billResponse.getInvoiceGasAmountTax());

        snapshot.setLendMoney(billResponse.getLendMoney());
        snapshot.setLendMoneyInterest(billResponse.getLendMoneyInterest());
        snapshot.setGiveBackMoney(billResponse.getGiveBackMoney());
        snapshot.setGiveBackInterest(billResponse.getGiveBackInterest());

        snapshot.setOtherLendMoneyAmount(billResponse.getOtherLendMoneyAmount());
        snapshot.setOtherGiveBackMoneyAmount(billResponse.getOtherGiveBackMoneyAmount());
        snapshot.setTrafficSum(billResponse.getTrafficSum());
        snapshot.setPayInterest(billResponse.getPayInterest());
        snapshot.setReceiveOffset(billResponse.getReceiveOffset());
        snapshot.setReturnMoney(billResponse.getReturnMoney());

        snapshot.setTotalSum(billResponse.getTotalSum());

        long epoch = DateTimeUtil.toUtcEpochSecond(now);
        if (existingOpt.isEmpty()) {
            snapshot.setCreateTime(epoch);
            snapshot.setUpdateTime(null);
        } else {
            snapshot.setUpdateTime(epoch);
        }
        snapshot.setSnapshotType(snapshotType);

        MonthBillSnapshot saved = monthBillSnapshotDao.save(snapshot);
        appendHistory(carLicenseNum, billYearMonth, billResponse, snapshotType, epoch, remark);
        return saved;
    }

    private void appendHistory(String carLicenseNum, String billYearMonth,
                               MonthBillResponse billResponse, String snapshotType,
                               long changedAtEpoch, String remark) {
        if (remark != null && remark.length() > 500) {
            remark = remark.substring(0, 500);
        }
        try {
            MonthBillSnapshotHistory row = new MonthBillSnapshotHistory();
            row.setCarLicenseNum(carLicenseNum);
            row.setBillYearMonth(billYearMonth);
            row.setChangedAt(changedAtEpoch);
            row.setSnapshotType(snapshotType);
            row.setBillPayloadJson(objectMapper.writeValueAsString(billResponse));
            row.setRemark(remark);
            monthBillSnapshotHistoryDao.save(row);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("快照歷程 JSON 序列化失敗", e);
        }
    }

    /**
     * 保存快照，並將本次修改往後同步至該車已存在快照的月份：
     * 後續每個月的「上月欠款」改為上一個月調整後的「本月欠款」，並依此重新計算該月「本月欠款」，
     * 其餘欄位維持不變。同時同步更新 last_month_owe（供尚未產生快照的月份於重算時取用）。
     */
    @Transactional
    public MonthBillSnapshot saveSnapshotAndCascadeForward(String carLicenseNum, String billYearMonth,
                                                            MonthBillResponse billResponse,
                                                            String snapshotType, LocalDateTime now, String remark) {
        billResponse.roundAllFieldsToInteger();
        billResponse.calculateTotalSum();
        billResponse.roundAllFieldsToInteger();

        MonthBillSnapshot saved = saveSnapshot(carLicenseNum, billYearMonth, billResponse, snapshotType, now, remark);

        long epoch = DateTimeUtil.toUtcEpochSecond(now);
        updateLastMonthOwe(carLicenseNum, billYearMonth, saved.getTotalSum(), epoch);

        BigDecimal carryOverTotal = saved.getTotalSum();
        List<MonthBillSnapshot> futureSnapshots =
                monthBillSnapshotDao.findByCarAndMonthAfterOrderByMonthAsc(carLicenseNum, billYearMonth);
        String cascadeRemark = "因 " + billYearMonth + " 帳單修改，自動同步調整上月欠款/本月欠款";
        for (MonthBillSnapshot future : futureSnapshots) {
            MonthBillResponse futureResponse = convertToResponse(future);
            futureResponse.setLastMonthOweAmount(carryOverTotal);
            futureResponse.roundAllFieldsToInteger();
            futureResponse.calculateTotalSum();
            futureResponse.roundAllFieldsToInteger();

            MonthBillSnapshot updated = saveSnapshot(carLicenseNum, future.getBillYearMonth(),
                    futureResponse, snapshotType, now, cascadeRemark);
            updateLastMonthOwe(carLicenseNum, future.getBillYearMonth(), updated.getTotalSum(), epoch);
            carryOverTotal = updated.getTotalSum();
        }
        return saved;
    }

    private void updateLastMonthOwe(String carLicenseNum, String expenseYearMonth, BigDecimal amount, long epoch) {
        LastMonthOwe lastMonthOwe = new LastMonthOwe();
        lastMonthOwe.setCarLicenseNum(carLicenseNum);
        lastMonthOwe.setExpenseYearMonth(expenseYearMonth);
        lastMonthOwe.setAmount(amount);
        lastMonthOwe.setCreateTime(epoch);
        lastMonthOweDao.save(lastMonthOwe);
    }

    /**
     * 查詢該車該月快照的變更歷程（時間由舊到新）
     */
    public List<MonthBillSnapshotHistoryItemDto> listSnapshotHistory(String carLicenseNum, String billYearMonth) {
        DateTimeValidate.checkYearMonth(billYearMonth);
        List<MonthBillSnapshotHistory> rows =
                monthBillSnapshotHistoryDao.findByCarAndMonthOrderByChangedAtAsc(carLicenseNum, billYearMonth);
        List<MonthBillSnapshotHistoryItemDto> out = new ArrayList<>(rows.size());
        for (MonthBillSnapshotHistory h : rows) {
            MonthBillSnapshotHistoryItemDto d = new MonthBillSnapshotHistoryItemDto();
            d.setId(h.getId());
            d.setChangedAt(h.getChangedAt());
            d.setSnapshotType(h.getSnapshotType());
            d.setRemark(h.getRemark());
            try {
                d.setBillData(objectMapper.readValue(h.getBillPayloadJson(), MonthBillResponse.class));
            } catch (JsonProcessingException e) {
                log.warn("快照歷史 JSON 解析失敗 id={}", h.getId(), e);
            }
            out.add(d);
        }
        return out;
    }

    /**
     * 查询快照
     */
    public Optional<MonthBillSnapshot> getSnapshot(String carLicenseNum, String billYearMonth) {
        return monthBillSnapshotDao.findByCarAndMonth(carLicenseNum, billYearMonth);
    }

    /**
     * 检查快照是否存在
     */
    public boolean snapshotExists(String carLicenseNum, String billYearMonth) {
        return monthBillSnapshotDao.existsByCarAndMonth(carLicenseNum, billYearMonth);
    }

    /**
     * 将快照转换为MonthBillResponse
     */
    public MonthBillResponse convertToResponse(MonthBillSnapshot snapshot) {
        MonthBillResponse response = new MonthBillResponse();

        response.setLastMonthOweAmount(snapshot.getLastMonthOweAmount());
        response.setManageFee(snapshot.getManageFee());
        response.setUnionFee(snapshot.getUnionFee());
        response.setLaborInsuranceFee(snapshot.getLaborInsuranceFee());
        response.setHealthFee(snapshot.getHealthFee());
        response.setInsuranceFee(snapshot.getInsuranceFee());
        response.setLicenseTaxFee(snapshot.getLicenseTaxFee());
        response.setFuelTaxFee(snapshot.getFuelTaxFee());
        response.setLoanFee(snapshot.getLoanFee());

        response.setInvoiceSaleAmount(snapshot.getInvoiceSaleAmount());
        response.setInvoiceSaleAmountTax(snapshot.getInvoiceSaleAmountTax());
        response.setInvoiceOffsetAmount(snapshot.getInvoiceOffsetAmount());
        response.setInvoiceOffsetAmountTax(snapshot.getInvoiceOffsetAmountTax());
        response.setInvoiceGasAmount(snapshot.getInvoiceGasAmount());
        response.setInvoiceGasAmountTax(snapshot.getInvoiceGasAmountTax());

        response.setLendMoney(snapshot.getLendMoney());
        response.setLendMoneyInterest(snapshot.getLendMoneyInterest());
        response.setGiveBackMoney(snapshot.getGiveBackMoney());
        response.setGiveBackInterest(snapshot.getGiveBackInterest());

        response.setOtherLendMoneyAmount(snapshot.getOtherLendMoneyAmount());
        response.setOtherGiveBackMoneyAmount(snapshot.getOtherGiveBackMoneyAmount());
        response.setTrafficSum(snapshot.getTrafficSum());
        response.setPayInterest(snapshot.getPayInterest());
        response.setReceiveOffset(snapshot.getReceiveOffset());
        response.setReturnMoney(snapshot.getReturnMoney());

        response.setTotalSum(snapshot.getTotalSum());

        // 统一输出取整：先取整组成字段，再重新计算 totalSum，最后再取整一次
        response.roundAllFieldsToInteger();
        response.calculateTotalSum();
        response.roundAllFieldsToInteger();

        return response;
    }
}
