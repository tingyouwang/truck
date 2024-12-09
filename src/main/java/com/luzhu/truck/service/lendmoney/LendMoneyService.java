package com.luzhu.truck.service.lendmoney;

import com.luzhu.truck.dao.carfee.CarFeeDao;
import com.luzhu.truck.dao.lendmoney.LendMoneyDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.car.CarFeeJoinInvoiceDto;
import com.luzhu.truck.dto.lendmoney.AddLendMoneyParam;
import com.luzhu.truck.dto.lendmoney.UpdateLendMoneyParam;
import com.luzhu.truck.entity.carfee.CarFee;
import com.luzhu.truck.entity.lendmoney.LendMoney;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LendMoneyService {
    @Autowired
    private LendMoneyDao lendMoneyDao;
    @Autowired
    private CarFeeDao carFeeDao;
    @Transactional
    public void addLendMoney(AddLendMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(param.getCarLicenseNum());

//        借款利率
        Double taxPercent = carFee.getOweTax();
        BigDecimal taxAmount = param.getAmount().multiply(BigDecimal.valueOf(taxPercent));

        int insertCount = lendMoneyDao.insertLendMoney(param.getCarLicenseNum(), param.getLendDate(), param.getAmount(), param.getType(), param.getExpireDate(),
                taxAmount, param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateLendMoney(UpdateLendMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        CarFee carFee = carFeeDao.getCarFeeByLicenseNum(param.getCarLicenseNum());
        Double taxPercent = carFee.getOweTax();
        BigDecimal taxAmount = param.getAmount().multiply(BigDecimal.valueOf(taxPercent));

        int updateCount = lendMoneyDao.updateLendMoney(param.getCarLicenseNum(), param.getLendDate(), param.getAmount(), param.getType(),
                param.getExpireDate(), taxAmount, param.getNote(), l, param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
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
