package com.luzhu.truck.service.returnmoney;

import com.luzhu.truck.dao.returnmoney.ReturnMoneyDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.dto.returnmoney.AddReturnMoneyParam;
import com.luzhu.truck.dto.returnmoney.UpdateReturnMoneyParam;
import com.luzhu.truck.entity.returnmoney.ReturnMoney;
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
public class ReturnMoneyService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Autowired
    private ReturnMoneyDao returnMoneyDao;
    @Autowired
    private BillService billService;
    @Autowired
    private MonthBillSnapshotService monthBillSnapshotService;

    @Transactional
    public void addReturnMoney(AddReturnMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = returnMoneyDao.insertReturnMoney(param.getCarLicenseNum(), param.getPayDate(), param.getAmount(),
                OtherLendMoneyTypeUtil.NORMAL, null, null, param.getDisable(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.INSERT_ERROR));

        refreshMonthBillSnapshotsForPayMonths("入款退回新增", param.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(param.getPayDate()));
    }

    @Transactional
    public void updateReturnMoney(UpdateReturnMoneyParam param) {
        ReturnMoney before = returnMoneyDao.findById(param.getId().intValue())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == before.getDisable(),
                new AppException(SystemExceptionEnum.RETURN_MONEY_DISABLE));
        Validator.isFalseThrow(!OtherLendMoneyTypeUtil.isAdjustmentRow(before.getType()),
                new AppException(SystemExceptionEnum.RETURN_MONEY_ADJUSTMENT_NO_REBILL));

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int updateCount = returnMoneyDao.updateReturnMoney(param.getCarLicenseNum(), param.getPayDate(), param.getAmount(),
                param.getDisable(), param.getNote(), l, param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForPayMonths("入款退回更新", before.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(before.getPayDate()),
                DateTimeUtil.toBillYearMonth(param.getPayDate()));
    }

    /**
     * 報廢轉月：原列 disable=1 並保留於原月明細；新增 ADJUSTMENT 列於目標月入帳。
     */
    @Transactional
    public void voidAndRebillToMonth(VoidAndRebillToMonthParam param) {
        ReturnMoney src = returnMoneyDao.findById(param.getId())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == src.getDisable(),
                new AppException(SystemExceptionEnum.RETURN_MONEY_DISABLE));
        Validator.isFalseThrow(src.getRebillTargetReturnMoneyId() == null,
                new AppException(SystemExceptionEnum.RETURN_MONEY_ALREADY_REBILLED));
        Validator.isFalseThrow(src.getRebillSourceReturnMoneyId() == null,
                new AppException(SystemExceptionEnum.RETURN_MONEY_ADJUSTMENT_NO_REBILL));
        Validator.isFalseThrow(!OtherLendMoneyTypeUtil.isAdjustmentRow(src.getType()),
                new AppException(SystemExceptionEnum.RETURN_MONEY_ADJUSTMENT_NO_REBILL));

        DateTimeValidate.checkYearMonth(param.getTargetBillYearMonth());
        YearMonth targetYm = YearMonth.parse(param.getTargetBillYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));
        String newPayDate = targetYm.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE);

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        ReturnMoney neu = new ReturnMoney();
        BeanUtils.copyProperties(src, neu, "id", "rebillSourceReturnMoneyId", "rebillTargetReturnMoneyId");
        neu.setType(OtherLendMoneyTypeUtil.toAdjustmentType());
        neu.setPayDate(newPayDate);
        neu.setDisable(0);
        neu.setRebillSourceReturnMoneyId(src.getId());
        neu.setRebillTargetReturnMoneyId(null);
        neu.setCreateTime(l);
        neu.setLastModifyTime(l);
        if (param.getNote() != null && !param.getNote().isBlank()) {
            neu.setNote(param.getNote());
        }
        neu = returnMoneyDao.save(neu);

        int marked = returnMoneyDao.markOriginalReturnMoneyRebilled(src.getId(), neu.getId(), l);
        Validator.isFalseThrow(marked == 1,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForPayMonths("入款退回報廢轉月", src.getCarLicenseNum(),
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
            monthBillSnapshotService.saveSnapshot(carLicenseNum, billDate, monthBill, "RETURN_MONEY", now, historyRemark);
        }
    }

    public PageResult<ReturnMoney> getReturnMoneyList(LicenseAndExpenseYearMonthParam param) {
        String expenseYearMonth = param.getExpenseYearMonth();
        DateTimeValidate.checkYearMonth(expenseYearMonth);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(expenseYearMonth, dateTimeFormatter);
        LocalDate monthFirst = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();
        Page<ReturnMoney> list = returnMoneyDao.getList(param.getCarLicenseNum(), monthFirst, monthEnd, param.getPageable());

        return new PageResult<>(list);
    }
}
