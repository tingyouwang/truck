package com.luzhu.truck.service.invoice;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.invoice.InvoiceDao;
import com.luzhu.truck.dto.bill.MonthBillReq;
import com.luzhu.truck.dto.bill.MonthBillResponse;
import com.luzhu.truck.dto.car.CarFeeJoinInvoiceDto;
import com.luzhu.truck.dto.invoice.AddInvoiceParam;
import com.luzhu.truck.dto.invoice.GetInvoiceParam;
import com.luzhu.truck.dto.invoice.UpdateInvoiceParam;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.invoice.Invoice;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.service.bill.BillService;
import com.luzhu.truck.service.monthbillsnapshot.MonthBillSnapshotService;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.util.DateTimeValidate;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(param.getCarLicenseNum());

        Double taxPercent = 0.0;
        switch (param.getType().toUpperCase()) {
            case "SALE":
                taxPercent = carFee.getSaleTax();
                break;
            case "GAS" :
                taxPercent = carFee.getGasTax();
                break;
            case "OFFSET":
                taxPercent = carFee.getBuyTax();
                break;
        }

        BigDecimal taxAmount = param.getAmount().multiply(BigDecimal.valueOf(taxPercent));

        int insertCount = invoiceDao.insertInvoice(param.getInvoiceNum(), param.getInvoiceDate(), param.getHandleDate(),
                param.getAmount(), taxAmount, param.getCarAgency(), param.getCarAgencyId(),
                param.getDisable(), param.getNote(), param.getTaxMonth(), param.getCarLicenseNum()
        , param.getType(), l);
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.INSERT_ERROR));
    }

    @Transactional
    public void updateInvoice(UpdateInvoiceParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFeeJoinInvoiceDto carFeeByInvoiceId = invoiceDao.getCarFeeByInvoiceId(param.getId());
        Validator.isFalseThrow(0 == carFeeByInvoiceId.getDisable(),
                new AppException(SystemExceptionEnum.INVOICE_DISABLE));

        Double taxPercent = 0.0;
        switch (carFeeByInvoiceId.getType().toUpperCase()) {
            case "SALE":
                taxPercent = carFeeByInvoiceId.getSaleTax();
                break;
            case "GAS" :
                taxPercent = carFeeByInvoiceId.getGasTax();
                break;
            case "OFFSET":
                taxPercent = carFeeByInvoiceId.getBuyTax();
                break;
        }

        BigDecimal taxAmount = param.getAmount().multiply(BigDecimal.valueOf(taxPercent));

        int insertCount = invoiceDao.updateInvoice(param.getId(), param.getHandleDate(),
                param.getInvoiceDate(), param.getInvoiceNum(), param.getAmount(),
                taxAmount, param.getCarAgency(), param.getCarAgencyId(), param.getNote(),
                param.getDisable(), param.getTaxMonth(), l);
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));

        refreshMonthBillSnapshotAfterInvoiceUpdate(carFeeByInvoiceId, param);
    }

    /**
     * 發票異動後依稅額所屬月份重算帳單並寫入 month_bill_snapshot（與手動生成快照相同來源資料）。
     * 若未帶 tax_month 則略過（無法對應帳單月份）。
     */
    private void refreshMonthBillSnapshotAfterInvoiceUpdate(CarFeeJoinInvoiceDto carFee,
                                                            UpdateInvoiceParam param) {

        //不要使用taxMonth, 請使用param.getInvoiceDate() 取得YYYY-MM-DD，再轉換為YYYY-MM
        LocalDate invoiceDate = LocalDate.parse(param.getInvoiceDate());
        String invoiceYearMonth = invoiceDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        DateTimeValidate.checkYearMonth(invoiceYearMonth);
        String carLicenseNum = carFee.getCarLicenseNum();
        if (carLicenseNum == null || carLicenseNum.isBlank()) {
            return;
        }
        MonthBillReq req = new MonthBillReq();
        req.setCarLicenseNum(carLicenseNum);
        req.setBillDate(invoiceYearMonth);
        MonthBillResponse monthBill = billService.getMonthBillForceRecalculate(req);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(Integer.parseInt(timeOffset)));
        monthBillSnapshotService.saveSnapshot(carLicenseNum, invoiceYearMonth, monthBill, "INVOICE", now, "發票更新");
    }

    public PageResult<Invoice> getInvoiceByType(GetInvoiceParam param) {
        Page<Invoice> invoices = invoiceDao.getAllByType(param.getCarLicenseNum(), DateTimeUtil.getMonthFirst(param.getExpenseYearMonth()),
                DateTimeUtil.getMonthLastDate(param.getExpenseYearMonth()), param.getType(), param.getPageable());
        return new PageResult<>(invoices);
    }
}
