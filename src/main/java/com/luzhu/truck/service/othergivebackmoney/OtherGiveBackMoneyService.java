package com.luzhu.truck.service.othergivebackmoney;

import com.luzhu.truck.dao.othergivebackmoney.OtherGiveBackMoneyDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.othergivebackmoney.AddOtherGiveBackMoneyParam;
import com.luzhu.truck.dto.othergivebackmoney.UpdateOtherGiveBackMoneyParam;
import com.luzhu.truck.entity.othergivebackmoney.OtherGiveBackMoney;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.util.DateTimeValidate;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class OtherGiveBackMoneyService {
    @Autowired
    private OtherGiveBackMoneyDao otherGiveBackMoneyDao;
    @Transactional
    public void addOtherGiveBackMoney(AddOtherGiveBackMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = otherGiveBackMoneyDao.insertOtherGiveBackMoney(param.getCarLicenseNum(), param.getGiveBackDate(), param.getAmount(), param.getDisable(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateOtherGiveBackMoney(UpdateOtherGiveBackMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int updateCount = otherGiveBackMoneyDao.updateOtherGiveBackMoney(param.getCarLicenseNum(), param.getGiveBackDate(), param.getAmount(), param.getDisable(), param.getNote(), l, param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
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
