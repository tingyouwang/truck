package com.luzhu.truck.service.lendmoney;

import com.luzhu.truck.dao.lendmoney.LendMoneyDao;
import com.luzhu.truck.dto.lendmoney.AddLendMoneyParam;
import com.luzhu.truck.exception.AppException;
import com.luzhu.truck.exception.SystemExceptionEnum;
import com.luzhu.truck.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class LendMoneyService {
    @Autowired
    private LendMoneyDao lendMoneyDao;
    @Transactional
    public void addLendMoney(AddLendMoneyParam param) {
        long l = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        int insertCount = lendMoneyDao.insertLendMoney(param.getCarLicenseNum(), param.getLendDate(), param.getAmount(), param.getType(), param.getExpireDate(),
                param.getInterestAmount(), param.getNote(), l, l);

        Validator.isFalseThrow(1 == insertCount,
                new AppException(SystemExceptionEnum.UPDATE_ERROR));
    }
}
