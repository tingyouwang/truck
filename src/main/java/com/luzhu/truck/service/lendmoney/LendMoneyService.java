package com.luzhu.truck.service.lendmoney;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.lendmoney.LendMoneyDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.dto.lendmoney.AddLendMoneyParam;
import com.luzhu.truck.dto.lendmoney.UpdateLendMoneyParam;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.lendmoney.LendMoney;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.service.bill.BillService;
import com.luzhu.truck.service.monthbillsnapshot.MonthBillSnapshotService;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.util.DateTimeValidate;
import com.luzhu.truck.util.LendMoneyTypeUtil;
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
public class LendMoneyService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Autowired
    private LendMoneyDao lendMoneyDao;
    @Autowired
    private CarFeeDao carFeeDao;
    @Autowired
    private BillService billService;
    @Autowired
    private MonthBillSnapshotService monthBillSnapshotService;

    @Transactional
    public void addLendMoney(AddLendMoneyParam param) {
        Validator.isFalseThrow(LendMoneyTypeUtil.isAllowedManualType(param.getType()),
                new AppException(SystemExceptionEnum.PARAM_ERROR));
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(param.getCarLicenseNum());
        Double taxPercent = carFee.getOweTax();
        BigDecimal taxAmount = param.getAmount().multiply(BigDecimal.valueOf(taxPercent != null ? taxPercent : 0.0));

        int insertCount = lendMoneyDao.insertLendMoney(param.getCarLicenseNum(), param.getLendDate(), param.getAmount(),
                param.getType(), null, null, param.getExpireDate(), taxAmount, param.getDisable(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.INSERT_ERROR));

        refreshMonthBillSnapshotsForLendMonths("借款新增", param.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(param.getLendDate()));
    }

    @Transactional
    public void updateLendMoney(UpdateLendMoneyParam param) {
        LendMoney before = lendMoneyDao.findById(param.getId().intValue())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == before.getDisable(),
                new AppException(SystemExceptionEnum.LEND_MONEY_DISABLE));
        Validator.isFalseThrow(LendMoneyTypeUtil.isAllowedManualType(param.getType()),
                new AppException(SystemExceptionEnum.PARAM_ERROR));

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(param.getCarLicenseNum());
        Double taxPercent = carFee.getOweTax();
        BigDecimal taxAmount = param.getAmount().multiply(BigDecimal.valueOf(taxPercent != null ? taxPercent : 0.0));

        int updateCount = lendMoneyDao.updateLendMoney(param.getCarLicenseNum(), param.getLendDate(), param.getAmount(), param.getType(),
                param.getExpireDate(), taxAmount, param.getDisable(), param.getNote(), l, param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForLendMonths("借款更新", before.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(before.getLendDate()),
                DateTimeUtil.toBillYearMonth(param.getLendDate()));
    }

    /**
     * 報廢轉月：原列 disable=1 並保留於原月明細；新增一筆 *_ADJUSTMENT 於目標月入帳。
     */
    @Transactional
    public void voidAndRebillToMonth(VoidAndRebillToMonthParam param) {
        LendMoney src = lendMoneyDao.findById(param.getId())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == src.getDisable(),
                new AppException(SystemExceptionEnum.LEND_MONEY_DISABLE));
        Validator.isFalseThrow(src.getRebillTargetLendMoneyId() == null,
                new AppException(SystemExceptionEnum.LEND_MONEY_ALREADY_REBILLED));
        Validator.isFalseThrow(src.getRebillSourceLendMoneyId() == null,
                new AppException(SystemExceptionEnum.LEND_MONEY_ADJUSTMENT_NO_REBILL));
        Validator.isFalseThrow(LendMoneyTypeUtil.isAllowedManualType(src.getType()),
                new AppException(SystemExceptionEnum.PARAM_ERROR));

        DateTimeValidate.checkYearMonth(param.getTargetBillYearMonth());
        YearMonth targetYm = YearMonth.parse(param.getTargetBillYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));
        String newLendDate = targetYm.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE);

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        LendMoney neu = new LendMoney();
        BeanUtils.copyProperties(src, neu, "id", "rebillSourceLendMoneyId", "rebillTargetLendMoneyId");
        neu.setType(LendMoneyTypeUtil.toAdjustmentType(src.getType()));
        neu.setLendDate(newLendDate);
        neu.setDisable(0);
        neu.setRebillSourceLendMoneyId(src.getId());
        neu.setRebillTargetLendMoneyId(null);
        neu.setCreateTime(l);
        neu.setLastModifyTime(l);
        if (param.getNote() != null && !param.getNote().isBlank()) {
            neu.setNote(param.getNote());
        }
        neu = lendMoneyDao.save(neu);

        int marked = lendMoneyDao.markOriginalLendMoneyRebilled(src.getId(), neu.getId(), l);
        Validator.isFalseThrow(marked == 1,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForLendMonths("借款報廢轉月", src.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(src.getLendDate()),
                param.getTargetBillYearMonth());
    }

    private void refreshMonthBillSnapshotsForLendMonths(String historyRemark, String carLicenseNum, String... yearMonths) {
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
            monthBillSnapshotService.saveSnapshot(carLicenseNum, billDate, monthBill, "LEND_MONEY", now, historyRemark);
        }
    }

    public PageResult<LendMoney> getLendMoneyList(LicenseAndExpenseYearMonthParam param) {
        String expenseYearMonth = param.getExpenseYearMonth();
        DateTimeValidate.checkYearMonth(expenseYearMonth);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(expenseYearMonth, dateTimeFormatter);
        LocalDate monthFirst = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();
        Page<LendMoney> list = lendMoneyDao.getList(param.getCarLicenseNum(), monthFirst, monthEnd, param.getPageable());

        return new PageResult<>(list);
    }
}
