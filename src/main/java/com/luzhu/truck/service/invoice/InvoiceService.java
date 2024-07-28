package com.luzhu.truck.service.invoice;

import com.luzhu.truck.dao.invoice.InvoiceDao;
import com.luzhu.truck.dto.invoice.AddInvoiceParam;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceDao invoiceDao;
    @Transactional
    public void addInvoice(AddInvoiceParam param) {

        int insertCount = invoiceDao.insertInvoice(param.getInvoiceNum(), param.getInvoiceDate(), param.getHandleDate(),
                param.getInvoiceAmount(), param.getInvoiceTax(), param.getCarAgency(), param.getCarAgencyNum(),
                param.getDisable(), param.getNote(), param.getTaxMonth(), param.getCarLicenseNum());
        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }
}
