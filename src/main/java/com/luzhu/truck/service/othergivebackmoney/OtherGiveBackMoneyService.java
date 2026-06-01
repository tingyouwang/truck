package com.luzhu.truck.service.othergivebackmoney;

import com.luzhu.truck.dao.othergivebackmoney.OtherGiveBackMoneyDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.dto.othergivebackmoney.AddOtherGiveBackMoneyParam;
import com.luzhu.truck.dto.othergivebackmoney.UpdateOtherGiveBackMoneyParam;
import com.luzhu.truck.entity.othergivebackmoney.OtherGiveBackMoney;
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
public class OtherGiveBackMoneyService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Autowired
    private OtherGiveBackMoneyDao otherGiveBackMoneyDao;
    @Autowired
    private BillService billService;
    @Autowired
    private MonthBillSnapshotService monthBillSnapshotService;

    @Transactional
    public void addOtherGiveBackMoney(AddOtherGiveBackMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = otherGiveBackMoneyDao.insertOtherGiveBackMoney(param.getCarLicenseNum(), param.getGiveBackDate(), param.getAmount(),
                OtherLendMoneyTypeUtil.NORMAL, null, null, param.getDisable(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.INSERT_ERROR));

        refreshMonthBillSnapshotsForGiveBackMonths("其他抵收新增", param.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(param.getGiveBackDate()));
    }

    @Transactional
    public void updateOtherGiveBackMoney(UpdateOtherGiveBackMoneyParam param) {
        OtherGiveBackMoney before = otherGiveBackMoneyDao.findById(param.getId().intValue())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == before.getDisable(),
                new AppException(SystemExceptionEnum.OTHER_GIVE_BACK_MONEY_DISABLE));
        Validator.isFalseThrow(!OtherLendMoneyTypeUtil.isAdjustmentRow(before.getType()),
                new AppException(SystemExceptionEnum.OTHER_GIVE_BACK_MONEY_ADJUSTMENT_NO_REBILL));

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int updateCount = otherGiveBackMoneyDao.updateOtherGiveBackMoney(param.getCarLicenseNum(), param.getGiveBackDate(), param.getAmount(),
                param.getDisable(), param.getNote(), l, param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForGiveBackMonths("其他抵收更新", before.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(before.getGiveBackDate()),
                DateTimeUtil.toBillYearMonth(param.getGiveBackDate()));
    }

    /**
     * 報廢轉月：原列 disable=1 並保留於原月明細；新增 ADJUSTMENT 列於目標月入帳。
     */
    @Transactional
    public void voidAndRebillToMonth(VoidAndRebillToMonthParam param) {
        OtherGiveBackMoney src = otherGiveBackMoneyDao.findById(param.getId())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == src.getDisable(),
                new AppException(SystemExceptionEnum.OTHER_GIVE_BACK_MONEY_DISABLE));
        Validator.isFalseThrow(src.getRebillTargetOtherGiveBackMoneyId() == null,
                new AppException(SystemExceptionEnum.OTHER_GIVE_BACK_MONEY_ALREADY_REBILLED));
        Validator.isFalseThrow(src.getRebillSourceOtherGiveBackMoneyId() == null,
                new AppException(SystemExceptionEnum.OTHER_GIVE_BACK_MONEY_ADJUSTMENT_NO_REBILL));
        Validator.isFalseThrow(!OtherLendMoneyTypeUtil.isAdjustmentRow(src.getType()),
                new AppException(SystemExceptionEnum.OTHER_GIVE_BACK_MONEY_ADJUSTMENT_NO_REBILL));

        DateTimeValidate.checkYearMonth(param.getTargetBillYearMonth());
        YearMonth targetYm = YearMonth.parse(param.getTargetBillYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));
        String newGiveBackDate = targetYm.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE);

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        OtherGiveBackMoney neu = new OtherGiveBackMoney();
        BeanUtils.copyProperties(src, neu, "id", "rebillSourceOtherGiveBackMoneyId", "rebillTargetOtherGiveBackMoneyId");
        neu.setType(OtherLendMoneyTypeUtil.toAdjustmentType());
        neu.setGiveBackDate(newGiveBackDate);
        neu.setDisable(0);
        neu.setRebillSourceOtherGiveBackMoneyId(src.getId());
        neu.setRebillTargetOtherGiveBackMoneyId(null);
        neu.setCreateTime(l);
        neu.setLastModifyTime(l);
        if (param.getNote() != null && !param.getNote().isBlank()) {
            neu.setNote(param.getNote());
        }
        neu = otherGiveBackMoneyDao.save(neu);

        int marked = otherGiveBackMoneyDao.markOriginalOtherGiveBackMoneyRebilled(src.getId(), neu.getId(), l);
        Validator.isFalseThrow(marked == 1,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForGiveBackMonths("其他抵收報廢轉月", src.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(src.getGiveBackDate()),
                param.getTargetBillYearMonth());
    }

    private void refreshMonthBillSnapshotsForGiveBackMonths(String historyRemark, String carLicenseNum, String... yearMonths) {
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
            monthBillSnapshotService.saveSnapshot(carLicenseNum, billDate, monthBill, "OTHER_GIVE_BACK_MONEY", now, historyRemark);
        }
    }

    public PageResult<OtherGiveBackMoney> getOtherGiveBackMoneyList(LicenseAndExpenseYearMonthParam param) {
        String expenseYearMonth = param.getExpenseYearMonth();
        DateTimeValidate.checkYearMonth(expenseYearMonth);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(expenseYearMonth, dateTimeFormatter);
        LocalDate monthFirst = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();
        Page<OtherGiveBackMoney> list = otherGiveBackMoneyDao.getList(param.getCarLicenseNum(), monthFirst, monthEnd, param.getPageable());

        return new PageResult<>(list);
    }
}
