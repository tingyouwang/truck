package com.luzhu.truck.service.givebackmoney;

import com.luzhu.truck.dao.givebackmoney.GiveBackMoneyDao;
import com.luzhu.truck.dto.LicenseAndExpenseYearMonthParam;
import com.luzhu.truck.dto.givebackmoney.AddGiveBackMoneyParam;
import com.luzhu.truck.dto.givebackmoney.UpdateGiveBackMoneyParam;
import com.luzhu.truck.entity.givebackmoney.GiveBackMoney;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class GiveBackMoneyService {
    @Autowired
    private GiveBackMoneyDao giveBackMoneyDao;
    @Transactional
    public void addGiveBackMoney(AddGiveBackMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = giveBackMoneyDao.insertLendMoney(param.getCarLicenseNum(), param.getGiveBackDate(), param.getAmount(), param.getType(), param.getExpireDate(),
                param.getInterestAmount(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    @Transactional
    public void updateGiveBackMoney(UpdateGiveBackMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int updateCount = giveBackMoneyDao.updateGiveBackMoney(param.getCarLicenseNum(), param.getGiveBackDate(), param.getAmount(), param.getType(), param.getExpireDate(),
                param.getInterestAmount(), param.getNote(), l, param.getId());

        Validator.isFalseThrow(1 == updateCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }

    public PageResult<GiveBackMoney> getGiveBackMoneyList(LicenseAndExpenseYearMonthParam param) {
        String expenseYearMonth = param.getExpenseYearMonth();
        DateTimeValidate.checkYearMonth(expenseYearMonth);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(expenseYearMonth, dateTimeFormatter);
        LocalDate monthFirst = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();
        Page<GiveBackMoney> list = giveBackMoneyDao.getList(param.getCarLicenseNum(), monthFirst, monthEnd, param.getPageable());

        return new PageResult<>(list);
    }
}
