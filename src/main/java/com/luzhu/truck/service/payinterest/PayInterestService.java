package com.luzhu.truck.service.payinterest;

import com.luzhu.truck.dao.payinterest.PayInterestDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.dto.payinterest.AddPayInterestParam;
import com.luzhu.truck.dto.payinterest.UpdatePayInterestParam;
import com.luzhu.truck.entity.payinterest.PayInterest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class PayInterestService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Autowired
    private PayInterestDao payInterestDao;
    @Autowired
    private BillService billService;
    @Autowired
    private MonthBillSnapshotService monthBillSnapshotService;

    @Transactional
    public void addPayInterest(AddPayInterestParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = payInterestDao.insertPayInterest(param.getCarLicenseNum(), param.getPayDate(),
                param.getAmount(), OtherLendMoneyTypeUtil.NORMAL, null, null,
                param.getDisable(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.INSERT_ERROR));

        refreshMonthBillSnapshotsForPayMonths("代支利息新增", param.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(param.getPayDate()));
    }

    @Transactional
    public void updatePayInterest(UpdatePayInterestParam param) {
        PayInterest before = payInterestDao.findById(param.getId().intValue())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == before.getDisable(),
                new AppException(SystemExceptionEnum.PAY_INTEREST_DISABLE));
        Validator.isFalseThrow(!OtherLendMoneyTypeUtil.isAdjustmentRow(before.getType()),
                new AppException(SystemExceptionEnum.PAY_INTEREST_ADJUSTMENT_NO_REBILL));

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int updateCount = payInterestDao.updatePayInterest(param.getCarLicenseNum(), param.getPayDate(),
                param.getAmount(), param.getDisable(), param.getNote(), l, param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForPayMonths("代支利息更新", before.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(before.getPayDate()),
                DateTimeUtil.toBillYearMonth(param.getPayDate()));
    }

    /**
     * 報廢轉月：原列 disable=1 並保留於原月明細；新增 ADJUSTMENT 列於目標月入帳。
     */
    @Transactional
    public void voidAndRebillToMonth(VoidAndRebillToMonthParam param) {
        PayInterest src = payInterestDao.findById(param.getId())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == src.getDisable(),
                new AppException(SystemExceptionEnum.PAY_INTEREST_DISABLE));
        Validator.isFalseThrow(src.getRebillTargetPayInterestId() == null,
                new AppException(SystemExceptionEnum.PAY_INTEREST_ALREADY_REBILLED));
        Validator.isFalseThrow(src.getRebillSourcePayInterestId() == null,
                new AppException(SystemExceptionEnum.PAY_INTEREST_ADJUSTMENT_NO_REBILL));
        Validator.isFalseThrow(!OtherLendMoneyTypeUtil.isAdjustmentRow(src.getType()),
                new AppException(SystemExceptionEnum.PAY_INTEREST_ADJUSTMENT_NO_REBILL));

        DateTimeValidate.checkYearMonth(param.getTargetBillYearMonth());
        YearMonth targetYm = YearMonth.parse(param.getTargetBillYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));
        String newPayDate = targetYm.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE);

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        PayInterest neu = new PayInterest();
        BeanUtils.copyProperties(src, neu, "id", "rebillSourcePayInterestId", "rebillTargetPayInterestId");
        neu.setType(OtherLendMoneyTypeUtil.toAdjustmentType());
        neu.setPayDate(newPayDate);
        neu.setDisable(0);
        neu.setRebillSourcePayInterestId(src.getId());
        neu.setRebillTargetPayInterestId(null);
        neu.setCreateTime(l);
        neu.setLastModifyTime(l);
        if (param.getNote() != null && !param.getNote().isBlank()) {
            neu.setNote(param.getNote());
        }
        neu = payInterestDao.save(neu);

        int marked = payInterestDao.markOriginalPayInterestRebilled(src.getId(), neu.getId(), l);
        Validator.isFalseThrow(marked == 1,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForPayMonths("代支利息報廢轉月", src.getCarLicenseNum(),
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
            monthBillSnapshotService.saveSnapshot(carLicenseNum, billDate, monthBill, "PAY_INTEREST", now, historyRemark);
        }
    }

    public PageResult<PayInterest> getPayInterestList(LicenseAndExpenseYearMonthParam param) {
        String expenseYearMonth = param.getExpenseYearMonth();
        DateTimeValidate.checkYearMonth(expenseYearMonth);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(expenseYearMonth, dateTimeFormatter);
        LocalDate monthFirst = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();
        Page<PayInterest> list = payInterestDao.getList(param.getCarLicenseNum(), monthFirst, monthEnd, param.getPageable());

        return new PageResult<>(list);
    }
}
