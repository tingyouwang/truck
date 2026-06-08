package com.luzhu.truck.service.receiveoffset;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.receiveoffset.ReceiveOffsetDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.dto.receiveoffset.AddReceiveOffsetParam;
import com.luzhu.truck.dto.receiveoffset.UpdateReceiveOffsetParam;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.receiveoffset.ReceiveOffset;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.service.bill.BillService;
import com.luzhu.truck.service.monthbillsnapshot.MonthBillSnapshotService;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.util.DateTimeValidate;
import com.luzhu.truck.util.OtherLendMoneyTypeUtil;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class ReceiveOffsetService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Autowired
    private ReceiveOffsetDao receiveOffsetDao;
    @Autowired
    private CarFeeDao carFeeDao;
    @Autowired
    private BillService billService;
    @Autowired
    private MonthBillSnapshotService monthBillSnapshotService;

    @Transactional
    public void addReceiveOffset(AddReceiveOffsetParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(param.getCarLicenseNum());
        Double taxPercent = carFee.getReceipTax();
        BigDecimal taxAmount = param.getReceiptAmount().multiply(BigDecimal.valueOf(taxPercent != null ? taxPercent : 0.0));

        int insertCount = receiveOffsetDao.insertReceiveOffset(param.getCarLicenseNum(), param.getPayDate(), taxAmount,
                param.getReceiptAmount(), OtherLendMoneyTypeUtil.NORMAL, null, null,
                param.getDisable(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.INSERT_ERROR));

        refreshMonthBillSnapshotsForPayMonths("收據抵收新增", param.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(param.getPayDate()));
    }

    @Transactional
    public void updateReceiveOffset(UpdateReceiveOffsetParam param) {
        ReceiveOffset before = receiveOffsetDao.findById(param.getId().intValue())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == before.getDisable(),
                new AppException(SystemExceptionEnum.RECEIVE_OFFSET_DISABLE));
        Validator.isFalseThrow(!OtherLendMoneyTypeUtil.isAdjustmentRow(before.getType()),
                new AppException(SystemExceptionEnum.RECEIVE_OFFSET_ADJUSTMENT_NO_REBILL));

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(param.getCarLicenseNum());
        Double taxPercent = carFee.getReceipTax();
        BigDecimal taxAmount = param.getReceiptAmount().multiply(BigDecimal.valueOf(taxPercent != null ? taxPercent : 0.0));

        int updateCount = receiveOffsetDao.updateReceiveOffset(param.getCarLicenseNum(), param.getPayDate(), taxAmount,
                param.getReceiptAmount(), param.getDisable(), param.getNote(), l, param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForPayMonths("收據抵收更新", before.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(before.getPayDate()),
                DateTimeUtil.toBillYearMonth(param.getPayDate()));
    }

    /**
     * 報廢轉月：原列 disable=1 並保留於原月明細；新增 ADJUSTMENT 列於目標月入帳。
     */
    @Transactional
    public void voidAndRebillToMonth(VoidAndRebillToMonthParam param) {
        ReceiveOffset src = receiveOffsetDao.findById(param.getId())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == src.getDisable(),
                new AppException(SystemExceptionEnum.RECEIVE_OFFSET_DISABLE));
        Validator.isFalseThrow(src.getRebillTargetReceiveOffsetId() == null,
                new AppException(SystemExceptionEnum.RECEIVE_OFFSET_ALREADY_REBILLED));
        Validator.isFalseThrow(src.getRebillSourceReceiveOffsetId() == null,
                new AppException(SystemExceptionEnum.RECEIVE_OFFSET_ADJUSTMENT_NO_REBILL));
        Validator.isFalseThrow(!OtherLendMoneyTypeUtil.isAdjustmentRow(src.getType()),
                new AppException(SystemExceptionEnum.RECEIVE_OFFSET_ADJUSTMENT_NO_REBILL));

        DateTimeValidate.checkYearMonth(param.getTargetBillYearMonth());
        String srcBillYearMonth = DateTimeUtil.toBillYearMonth(src.getPayDate());

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        // 目標月份與來源月份相同：僅作廢原列，不新增調整列（否則兩筆相抵等於未報廢）
        if (param.getTargetBillYearMonth().equals(srcBillYearMonth)) {
            src.setDisable(1);
            src.setLastModifyTime(l);
            if (param.getNote() != null && !param.getNote().isBlank()) {
                src.setNote(param.getNote());
            }
            receiveOffsetDao.save(src);
            refreshMonthBillSnapshotsForPayMonths("收據抵收報廢", src.getCarLicenseNum(), srcBillYearMonth);
            return;
        }

        YearMonth targetYm = YearMonth.parse(param.getTargetBillYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));
        String newPayDate = targetYm.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE);

        ReceiveOffset neu = new ReceiveOffset();
        BeanUtils.copyProperties(src, neu, "id", "rebillSourceReceiveOffsetId", "rebillTargetReceiveOffsetId");
        neu.setType(OtherLendMoneyTypeUtil.toAdjustmentType());
        neu.setPayDate(newPayDate);
        neu.setDisable(0);
        neu.setRebillSourceReceiveOffsetId(src.getId());
        neu.setRebillTargetReceiveOffsetId(null);
        neu.setCreateTime(l);
        neu.setLastModifyTime(l);
        if (param.getNote() != null && !param.getNote().isBlank()) {
            neu.setNote(param.getNote());
        }
        neu = receiveOffsetDao.save(neu);

        int marked = receiveOffsetDao.markOriginalReceiveOffsetRebilled(src.getId(), neu.getId(), l);
        Validator.isFalseThrow(marked == 1,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForPayMonths("收據抵收報廢轉月", src.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(src.getPayDate()),
                param.getTargetBillYearMonth());
    }

    private void refreshMonthBillSnapshotsForPayMonths(String historyRemark, String carLicenseNum, String... yearMonths) {
        if (carLicenseNum == null || carLicenseNum.isBlank()) {
            return;
        }
        Set<String> months = new LinkedHashSet<>();
        for (String ym : yearMonths) {
            if (ym != null && !ym.isBlank()) {
                months.add(ym);
            }
        }
        if (months.isEmpty()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        for (String billDate : months) {
            DateTimeValidate.checkYearMonth(billDate);
            MonthBillReq req = new MonthBillReq();
            req.setCarLicenseNum(carLicenseNum);
            req.setBillDate(billDate);
            MonthBillResponse monthBill = billService.getMonthBillForceRecalculate(req);
            monthBillSnapshotService.saveSnapshot(carLicenseNum, billDate, monthBill, "RECEIVE_OFFSET", now, historyRemark);
        }
    }

    public PageResult<ReceiveOffset> getReceiveOffsetList(LicenseAndExpenseYearMonthParam param) {
        String expenseYearMonth = param.getExpenseYearMonth();
        DateTimeValidate.checkYearMonth(expenseYearMonth);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(expenseYearMonth, dateTimeFormatter);
        LocalDate monthFirst = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();
        Page<ReceiveOffset> list = receiveOffsetDao.getList(param.getCarLicenseNum(), monthFirst, monthEnd, param.getPageable());

        return new PageResult<>(list);
    }
}
