package com.luzhu.truck.service.receiveoffset;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.receiveoffset.ReceiveOffsetDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.receiveoffset.AddReceiveOffsetParam;
import com.luzhu.truck.dto.receiveoffset.UpdateReceiveOffsetParam;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.receiveoffset.ReceiveOffset;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.util.DateTimeValidate;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class ReceiveOffsetService {
    @Autowired
    private ReceiveOffsetDao receiveOffsetDao;
    @Autowired
    private CarFeeDao carFeeDao;
    @Transactional
    public void addReceiveOffset(AddReceiveOffsetParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(param.getCarLicenseNum());
        Double taxPercent = carFee.getReceipTax();
        BigDecimal taxAmount = param.getReceiptAmount().multiply(BigDecimal.valueOf(taxPercent));

        int insertCount = receiveOffsetDao.insertReceiveOffset(param.getCarLicenseNum(), param.getPayDate(), taxAmount,
                param.getReceiptAmount(), param.getDisable(), param.getNote(), l , l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateReceiveOffset(UpdateReceiveOffsetParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(param.getCarLicenseNum());
        Double taxPercent = carFee.getReceipTax();
        BigDecimal taxAmount = param.getReceiptAmount().multiply(BigDecimal.valueOf(taxPercent));

        int updateCount = receiveOffsetDao.updateReceiveOffset(param.getCarLicenseNum(), param.getPayDate(), taxAmount,
                param.getReceiptAmount(), param.getDisable(), param.getNote(), l , param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
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
