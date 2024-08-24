package com.luzhu.truck.service.invoice;

import com.luzhu.truck.dao.invoice.InvoiceDao;
import com.luzhu.truck.dto.invoice.AddInvoiceParam;
import com.luzhu.truck.dto.invoice.GetInvoiceParam;
import com.luzhu.truck.dto.invoice.UpdateInvoiceParam;
import com.luzhu.truck.entity.invoice.Invoice;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.util.DateTimeUtil;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceDao invoiceDao;
    @Transactional
    public void addInvoice(AddInvoiceParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = invoiceDao.insertInvoice(param.getInvoiceNum(), param.getInvoiceDate(), param.getHandleDate(),
                param.getInvoiceAmount(), param.getInvoiceTax(), param.getCarAgency(), param.getCarAgencyNum(),
                param.getDisable(), param.getNote(), param.getTaxMonth(), param.getCarLicenseNum()
        , param.getType(), l);
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.INSERT_ERROR));
    }

    @Transactional
    public void updateInvoice(UpdateInvoiceParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = invoiceDao.updateInvoice(param.getId(), param.getHandleDate(),
                param.getInvoiceDate(), param.getInvoiceNum(), param.getInvoiceAmount(),
                param.getInvoiceTax(), param.getCarAgency(), param.getCarAgencyNum(), param.getNote(),
                param.getDisable(), param.getTaxMonth(), l);
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    public PageResult<Invoice> getInvoiceByType(GetInvoiceParam param) {
        Page<Invoice> invoices = invoiceDao.getAllByType(param.getCarLicenseNum(), DateTimeUtil.getMonthFirst(param.getExpenseYearMonth()),
                DateTimeUtil.getMonthLastDate(param.getExpenseYearMonth()), param.getType(), param.getPageable());
        return new PageResult<>(invoices);
    }
}
