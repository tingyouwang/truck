package com.luzhu.truck.service.invoice;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.invoice.InvoiceDao;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.car.CarFeeJoinInvoiceDto;
import com.luzhu.truck.dto.invoice.AddInvoiceParam;
import com.luzhu.truck.dto.invoice.GetInvoiceParam;
import com.luzhu.truck.dto.invoice.UpdateInvoiceParam;
import com.luzhu.truck.dto.invoice.VoidAndRebillToMonthParam;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.invoice.Invoice;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.service.bill.BillService;
import com.luzhu.truck.service.monthbillsnapshot.MonthBillSnapshotService;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.util.DateTimeValidate;
import com.luzhu.truck.util.InvoiceTypeUtil;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class InvoiceService {
    @Value("${env.time.offset}")
    private String timeOffset;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private CarFeeDao carFeeDao;
    @Autowired
    private BillService billService;
    @Autowired
    private MonthBillSnapshotService monthBillSnapshotService;

    @Transactional
    public void addInvoice(AddInvoiceParam param) {
        Validator.isFalseThrow(InvoiceTypeUtil.isAllowedManualType(param.getType()),
                new AppException(SystemExceptionEnum.PARAM_ERROR));
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(param.getCarLicenseNum());

        double taxPercent = InvoiceTypeUtil.taxPercentForCarFee(carFee, param.getType());
        BigDecimal taxAmount = param.getAmount().multiply(BigDecimal.valueOf(taxPercent));

        int insertCount = invoiceDao.insertInvoice(param.getInvoiceNum(), param.getInvoiceDate(), param.getHandleDate(),
                param.getAmount(), taxAmount, param.getCarAgency(), param.getCarAgencyId(),
                param.getDisable(), param.getNote(), param.getTaxMonth(), param.getCarLicenseNum(),
                param.getType(), null, null, l);
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.INSERT_ERROR));

        refreshMonthBillSnapshotsForHandleMonths("發票新增", param.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(param.getHandleDate()));
    }

    @Transactional
    public void updateInvoice(UpdateInvoiceParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFeeJoinInvoiceDto before = invoiceDao.getCarFeeByInvoiceId(param.getId());
        Validator.isFalseThrow(0 == before.getDisable(),
                new AppException(SystemExceptionEnum.INVOICE_DISABLE));

        Double taxPercent = InvoiceTypeUtil.taxPercentForJoinDto(before, before.getType());
        BigDecimal taxAmount = param.getAmount().multiply(BigDecimal.valueOf(taxPercent));

        int insertCount = invoiceDao.updateInvoice(param.getId(), param.getHandleDate(),
                param.getInvoiceDate(), param.getInvoiceNum(), param.getAmount(),
                taxAmount, param.getCarAgency(), param.getCarAgencyId(), param.getNote(),
                param.getDisable(), param.getTaxMonth(), l);
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForHandleMonths("發票更新", before.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(before.getHandleDate()),
                DateTimeUtil.toBillYearMonth(param.getHandleDate()));
    }

    /**
     * 報廢轉月：原列 disable=1 並保留於原月明細；新增一筆 *_ADJUSTMENT 於目標月入帳。
     */
    @Transactional
    public void voidAndRebillToMonth(VoidAndRebillToMonthParam param) {
        Invoice src = invoiceDao.findById(param.getId())
                .orElseThrow(() -> new AppException(SystemExceptionEnum.NO_DATA));
        Validator.isFalseThrow(0 == src.getDisable(),
                new AppException(SystemExceptionEnum.INVOICE_DISABLE));
        Validator.isFalseThrow(src.getRebillTargetInvoiceId() == null,
                new AppException(SystemExceptionEnum.INVOICE_ALREADY_REBILLED));
        Validator.isFalseThrow(src.getRebillSourceInvoiceId() == null,
                new AppException(SystemExceptionEnum.INVOICE_ADJUSTMENT_NO_REBILL));
        Validator.isFalseThrow(InvoiceTypeUtil.isAllowedManualType(src.getType()),
                new AppException(SystemExceptionEnum.PARAM_ERROR));

        DateTimeValidate.checkYearMonth(param.getTargetBillYearMonth());
        String srcBillYearMonth = DateTimeUtil.toBillYearMonth(src.getHandleDate());

        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        // 目標月份與來源月份相同：僅作廢原列，不新增調整列（否則兩筆相抵等於未報廢）
        if (param.getTargetBillYearMonth().equals(srcBillYearMonth)) {
            src.setDisable(1);
            src.setLastModifyTime(l);
            if (param.getNote() != null && !param.getNote().isBlank()) {
                src.setNote(param.getNote());
            }
            invoiceDao.save(src);
            refreshMonthBillSnapshotsForHandleMonths("發票報廢", src.getCarLicenseNum(), srcBillYearMonth);
            return;
        }

        YearMonth targetYm = YearMonth.parse(param.getTargetBillYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));
        String newHandleDate = targetYm.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE);

        Invoice neu = new Invoice();
        BeanUtils.copyProperties(src, neu, "id", "rebillSourceInvoiceId", "rebillTargetInvoiceId");
        neu.setType(InvoiceTypeUtil.toAdjustmentType(src.getType()));
        neu.setHandleDate(newHandleDate);
        neu.setDisable(0);
        neu.setRebillSourceInvoiceId(src.getId());
        neu.setRebillTargetInvoiceId(null);
        neu.setLastModifyTime(l);
        if (param.getNote() != null && !param.getNote().isBlank()) {
            neu.setNote(param.getNote());
        }
        neu = invoiceDao.save(neu);

        int marked = invoiceDao.markOriginalInvoiceRebilled(src.getId(), neu.getId(), l);
        Validator.isFalseThrow(marked == 1,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotsForHandleMonths("報廢轉月", src.getCarLicenseNum(),
                DateTimeUtil.toBillYearMonth(src.getHandleDate()),
                param.getTargetBillYearMonth());
    }

    /**
     * 依處理日期所屬帳單月份重算並寫入 month_bill_snapshot，並追加 month_bill_snapshot_history。
     */
    private void refreshMonthBillSnapshotsForHandleMonths(String historyRemark, String carLicenseNum, String... yearMonths) {
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
            monthBillSnapshotService.saveSnapshot(carLicenseNum, billDate, monthBill, "INVOICE", now, historyRemark);
        }
    }

    public PageResult<Invoice> getInvoiceByType(GetInvoiceParam param) {
        String base = param.getType().toUpperCase();
        String adj = InvoiceTypeUtil.toAdjustmentType(base);
        Page<Invoice> invoices = invoiceDao.getAllByType(param.getCarLicenseNum(), DateTimeUtil.getMonthFirst(param.getExpenseYearMonth()),
                DateTimeUtil.getMonthLastDate(param.getExpenseYearMonth()), base, adj, param.getPageable());
        return new PageResult<>(invoices);
    }
}
